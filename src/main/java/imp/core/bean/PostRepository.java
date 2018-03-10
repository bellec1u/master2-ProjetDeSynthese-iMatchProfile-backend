/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Matching;
import imp.core.entity.Skill;
import imp.core.entity.post.Post;
import imp.core.entity.post.PostSkill;
import imp.core.entity.post.PostSkill.Type;
import imp.core.entity.user.Candidate;
import imp.core.entity.user.Notification;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.mail.*;
import javax.mail.internet.*;

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
    
    public List<Post> getAll(int limit) {
        return getEntityManager().createNamedQuery("Post.findAll", Post.class).setMaxResults(limit).getResultList();
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
            TypedQuery<PostSkill> query = getEntityManager()
                    .createNamedQuery("PostSkill.findBySkillAndType", PostSkill.class);

            List<PostSkill> findPostSkills = query.setParameter("id_skill", ps.getSkill().getId())
                    .setParameter("type", ps.getType())
                    .getResultList();
            
            // if find it -> add it to the post
            if (!findPostSkills.isEmpty()) {
                lps.add(findPostSkills.get(0));
            }
            else {
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
        
        // send email and notification to candidates that match with this post
        sendNotificationsCandidate(post);
        
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
        
        // remove matchings
        List<Matching> matchings = em
                .createNamedQuery("Matching.findByPost", Matching.class)
                .setParameter("id", id)
                .getResultList();
        
        for (Matching m : matchings) { 
            em.remove(getEntityManager().merge(m));
        }

        // get the recruiter of the post
        TypedQuery<Recruiter> query = getEntityManager().createNamedQuery("Recruiter.findCreatorOfPost", Recruiter.class);
        Recruiter r = query.setParameter("post", p).getSingleResult();

        //remove the post from posts of recruiter
        r.removePostById(id);
        
        // remove the post
        super.removeById(id);
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
        
        if(skillsNeeded.isEmpty())
            return null;

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
        double percent = (Math.round((x / tot) * 100 * 100)) / 100.0;

        // if percent > 0 -> add it
        if (percent > 0) {
            Matching matching = new Matching(candidate, post, percent);
            em.persist(matching);
        }
    }
    
    private void sendEmail(String userEmail, Post post){
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "25");
        
        String from = "no-reply@imp.com";

        try {
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(false);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(userEmail));
            message.setSubject("Vous avez un Match ! - "+post.getTitle() + " - ");
            message.setText("Lien vers l'offre : "
                + "\n\n http://localhost:4200/post/"+post.getId());

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void sendNotification(User user, Post post){
        String message = "Vous avez un Match ! - "+post.getTitle() + " - " ;
        String link = "/post/" + post.getId();
        Notification notif = new Notification(message,link,user,true);
        em.persist(notif);
    }
    
    private void sendNotificationsCandidate(Post post){
        List<Candidate> candidateMandatorySkills = new ArrayList<>();
        candidateMandatorySkills = getCandidatebyMandatorySkills(post.getId());
        if(candidateMandatorySkills != null){
        for (Candidate candidateMandatorySkill : candidateMandatorySkills) {
            // sendEmail is commented for dev environnement
            //sendEmail(candidateMandatorySkill.getUser().getEmail(),post);
            sendNotification(candidateMandatorySkill.getUser(),post);
        }
        }
    }

    public Recruiter isMyPost(Long postId) {
        Post p = em.find(Post.class, postId);
        List<Recruiter> r =  em.createNamedQuery("Recruiter.findCreatorOfPost", Recruiter.class)
                .setParameter("post", p)
                .getResultList();
        return r.get(0);
    }
}
