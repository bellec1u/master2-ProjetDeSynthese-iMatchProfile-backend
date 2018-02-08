/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import com.google.gson.Gson;
import imp.core.entity.Matching;
import imp.core.entity.Skill;
import imp.core.entity.post.Post;
import imp.core.entity.post.PostSkill;
import imp.core.entity.post.PostSkill.Type;
import imp.core.entity.user.Candidate;
import imp.core.entity.user.Recruiter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Leopold
 */
@Stateless
public class PostRepository extends AbstractRepository<Post> {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public PostRepository() {
        super(Post.class);
    }

    public List<Post> getAll() {
        return executeNamedQuery("Post.findAll");
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Post addPost(Long recruiterId, Post post) {
        List<PostSkill> lps = new ArrayList<>();
        // for each postskills
        for (PostSkill ps : post.getPostskill()) {
            // search it in the db
            try {
                TypedQuery<PostSkill> query = getEntityManager()
                        .createNamedQuery("PostSkill.findBySkillAndType", PostSkill.class);
                // if find it -> add it to the post
                lps.add(query.setParameter("id_skill", ps.getSkill().getId())
                        .setParameter("type", ps.getType())
                        .getSingleResult());
            } catch (NoResultException e) {
                // otherwise -> create a new postskill and add it to the post
                PostSkill nps = new PostSkill(ps.getSkill(), ps.getType());
                lps.add(nps);
            }
        }
        // set the new postskills list
        post.setPostskill(lps);
        // get the good recruiter
        TypedQuery<Recruiter> query = getEntityManager()
                .createNamedQuery("Recruiter.findById", Recruiter.class);
        Recruiter recruiter = query.setParameter("id", recruiterId).getSingleResult();
        // add the new post to the recruiter
        recruiter.addPost(post);
        // update the recruiter
        //edit(recruiter);
        Post newPost = recruiter.getPost().get(recruiter.getPost().size() - 1);
        
        // generate all matchings for newPost
        generateMatchingForPost(newPost);
        
        return newPost;
    }

    public Post edit(long id, Post post) {
        List<PostSkill> lps = new ArrayList<>();
        // for each postskill
        for (PostSkill ps : post.getPostskill()) {
            try {
                TypedQuery<PostSkill> query = getEntityManager()
                        .createNamedQuery("PostSkill.findBySkillAndType", PostSkill.class);
                // if find it -> add it to the post
                lps.add(query.setParameter("id_skill", ps.getSkill().getId())
                        .setParameter("type", ps.getType())
                        .getSingleResult());
            } catch (NoResultException e) {
                // if not exist, create it and change in post
                PostSkill nps = new PostSkill(ps.getSkill(), ps.getType());
                lps.add(nps);
            }
        }
        // set the list of postskill
        post.setPostskill(lps);
        // update the post
        return super.edit(post);
    }

    @Override
    public void removeById(Object id) {
        // get the post
        Post p = super.getById(id);

        // get the recruiter of the post
        TypedQuery<Recruiter> query = getEntityManager().createNamedQuery("Recruiter.findCreatorOfPost", Recruiter.class);
        Recruiter r = query.setParameter("post", p).getSingleResult();

        //remove the post from posts of recruiter
        r.removePostById(id);

        // remove the post
        super.removeById(id);
    }

    /**
     * return all users with matching %
     *
     * @param id
     * @return
     */
    public JSONArray getBySkills(Long id) throws ParseException {
        List<Matching> matchings = em
                .createNamedQuery("Matching.findByPost", Matching.class)
                .setParameter("id", id)
                .getResultList();

        // sort by percent
        Collections.sort(matchings, new Comparator<Matching>() {
            @Override
            public int compare(Matching o1, Matching o2) {
                if (o1.getPercent() > o2.getPercent()) {
                    return -1;
                } else {
                    return 1;
                }
            }

        });

        // product json
        JSONArray json = new JSONArray();
        for (Matching matching : matchings) {
            // generate json
            JSONObject obj = new JSONObject();

            String candidateJSON = (new Gson()).toJson(matching.getCandidate());
            JSONParser parser = new JSONParser();

            obj.put("candidate", (JSONObject) parser.parse(candidateJSON));
            obj.put("percent", matching.getPercent());

            json.add(obj);
        }
        return json;
    }

    public List<Candidate> getCandidatebyMandatorySkills(Long postId) {

        List<Skill> skillsNeeded = new ArrayList<>();

        // get the post
        Post p = super.getById(postId);

        //get Mandatory skills
        for (PostSkill ps : p.getPostskill()) {
            if (Type.OBLIGATOIRE.equals(ps.getType())) {
                Skill s = ps.getSkill();
                skillsNeeded.add(s);
            }
        }

        // get all users
        List<Candidate> candidates = em.createNamedQuery("Candidate.findAll", Candidate.class)
                .getResultList();

        List<Candidate> res = new ArrayList<>();

        for (Candidate c : candidates) {
            int tmp = 0;
            for (Skill s : c.getSkills()) {
                if (skillsNeeded.contains(s)) {
                    tmp++;
                }
            }
            if (tmp == skillsNeeded.size()) {
                res.add(c);
            }
        }
        return res;
    }

    @Schedule
    public void checkMatching() {
        // clear matching table
        em.createNamedQuery("Matching.cleanTable").executeUpdate();
        
        // get all posts and candidates available
        List<Post> posts = getAll();
        List<Candidate> candidates = em
                .createNamedQuery("Candidate.findAll", Candidate.class)
                .getResultList();

        // for each post from posts
        for (Post post : posts) {
            // for each candidate from candidates
            for (Candidate candidate : candidates) {
                // generate matching
                generateMatching(post, candidate);
            }
        }
    }

    private void generateMatchingForPost(Post post) {
        List<Candidate> candidates = em
                .createNamedQuery("Candidate.findAll", Candidate.class)
                .getResultList();

        // for each candidate from candidates
        for (Candidate candidate : candidates) {
            // generate matching
            generateMatching(post, candidate);
        }
    }

    private void generateMatching(Post post, Candidate candidate) {
        List<Object[]> skillsNeeded = new ArrayList<>();

        // tot = skill * coef
        double tot = 0;
        int coef = 2;

        for (PostSkill ps : post.getPostskill()) {
            if (Type.OBLIGATOIRE.equals(ps.getType())) {
                Object[] s = {ps.getSkill().getDescription(), coef};
                skillsNeeded.add(s);
                tot += coef;
            } else if (Type.PLUS.equals(ps.getType())) {
                Object[] s = {ps.getSkill().getDescription(), 1};
                skillsNeeded.add(s);
                tot += 1;
            }
        }

        // matching calculation
        double x = 0;
        // for each skills of the candidate
        for (Skill s : candidate.getSkills()) {
            // for each skills of the post
            for (Object[] skillNeeded : skillsNeeded) {
                // if both skill description are equals
                if (s.getDescription().equals(skillNeeded[0])) {
                    // skill matching
                    x += 1 * ((int) skillNeeded[1]);
                }
            }
        }

        // generate %
        double percent = (x / tot) * 100;

        // if percent > 0 -> add it
        if (percent > 0) {
            Matching matching = new Matching(candidate, post, percent);
            em.persist(matching);
        }
    }

}
