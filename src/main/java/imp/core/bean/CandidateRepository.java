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
import imp.core.entity.user.Candidate;
import imp.core.entity.user.Notification;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author alexis
 */
@Stateless
public class CandidateRepository extends AbstractRepository<Candidate> {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public CandidateRepository() {
        super(Candidate.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Candidate> getAll() {
        return executeNamedQuery("Candidate.findAll");
    }

    @Override
    public Candidate edit(Candidate candidate) {
        super.edit(candidate);
        sendNotificationsRecruiter(candidate.getId());
        return candidate;
    }

    @Override
    public void removeById(Object id) {
        TypedQuery<Matching> query = getEntityManager()
                .createNamedQuery("Matching.findByCandidate", Matching.class);
        List<Matching> matchings = query.setParameter("id", id).getResultList();
        for (Matching matching : matchings) {
            em.remove(matching);
        }
        super.removeById(id);
    }

    public Candidate getCandidatebyMandatorySkills(Long postId, Long candidateId) {

        List<Skill> skillsNeeded = new ArrayList<>();

        // get the post
        Post p = em.createNamedQuery("Post.findById", Post.class)
                .setParameter("id", postId)
                .getSingleResult();

        //get Mandatory skills
        for (PostSkill ps : p.getPostskill()) {
            if (PostSkill.Type.OBLIGATOIRE.equals(ps.getType())) {
                Skill s = ps.getSkill();
                skillsNeeded.add(s);
            }
        }

        if (skillsNeeded.isEmpty()) {
            return null;
        }

        // get user
        Candidate candidate = super.getById(candidateId);

        int tmp = 0;
        for (Skill s : candidate.getSkills()) {
            if (skillsNeeded.contains(s)) {
                tmp++;
            }
        }
        if (tmp == skillsNeeded.size()) {
            return candidate;
        }
        return null;
    }

    public void sendNotification(User user, Post post, Long candidateId) {
        String message = "Un nouveau candidat correspond au poste - " + post.getTitle() + " - ";
        String link = "/profile/" + candidateId;
        Notification notif = new Notification(message, link, user, true);
        em.persist(notif);
    }

    private void sendEmail(String userEmail, Post post, Long candidateId) {
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
            message.setSubject("Un nouveau candidat correspond au poste - " + post.getTitle() + " - ");
            message.setText("Lien vers le profil du candidate : "
                    + "\n\n http://localhost:4200/profile/" + candidateId);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendNotificationsRecruiter(Long candidateId) {
        List<Recruiter> recruiters = em.createNamedQuery("Recruiter.findAll", Recruiter.class)
                .getResultList();
        for (Recruiter recruiter : recruiters) {
            for (Post post : recruiter.getPost()) {
                Candidate candidateMandatorySkills = getCandidatebyMandatorySkills(post.getId(), candidateId);
                if (candidateMandatorySkills != null) {
                    sendNotification(recruiter.getUser(), post, candidateId);
                }
                // sendEmail is commented for dev environnement
                //sendEmail(candidateMandatorySkills.getUser().getEmail(),post,candidateId);
            }
        }
    }

}
