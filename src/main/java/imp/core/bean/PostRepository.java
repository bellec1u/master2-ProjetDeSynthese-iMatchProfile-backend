/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import com.google.gson.Gson;
import imp.core.entity.Skill;
import imp.core.entity.post.Post;
import imp.core.entity.post.PostSkill;
import imp.core.entity.post.PostSkill.Type;
import imp.core.entity.user.Candidate;
import imp.core.entity.user.Recruiter;
import java.util.ArrayList;
import java.util.List;
import static javafx.scene.input.KeyCode.J;
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
        return recruiter.getPost().get(recruiter.getPost().size() - 1);
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
        JSONArray json = new JSONArray();

        // [nameSkill: String, coef: int]
        List<Object[]> skillsNeeded = new ArrayList<>();

        // get the post
        Post p = super.getById(id);
        for (PostSkill ps : p.getPostskill()) {
            if (Type.OBLIGATOIRE.equals(ps.getType())) {
                Object[] s = {ps.getSkill().getDescription(), 2};
                skillsNeeded.add(s);
            } else if (Type.PLUS.equals(ps.getType())) {
                Object[] s = {ps.getSkill().getDescription(), 1};
                skillsNeeded.add(s);
            }
        }

        // get all users
        List<Candidate> candidates = em.createNamedQuery("Candidate.findAll", Candidate.class)
                .getResultList();

        for (Candidate c : candidates) {
            double tot = 0;
            for (Object[] skillNeeded : skillsNeeded) {
                tot += 1 * ((int) skillNeeded[1]);
            }

            // calculation of the percentage of matching
            double x = 0;
            // for each skills of the candidate
            for (Skill s : c.getSkills()) {
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

            // generate json
            JSONObject obj = new JSONObject();
            
            String candidateJSON = (new Gson()).toJson(c);
            JSONParser parser = new JSONParser();
            
            obj.put("user", (JSONObject) parser.parse(candidateJSON));
            obj.put("percent", percent);
            
            json.add(obj);
        }

        return json;
    }

}
