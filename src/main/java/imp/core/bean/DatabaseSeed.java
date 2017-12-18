/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.post.Post;
import imp.core.entity.post.PostSkill;
import imp.core.entity.post.PostSkill.Type;
import imp.core.entity.user.Candidate;
import imp.core.entity.Skill;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Singleton to feed the database with some datas.
 *
 * @author alexis
 */
@Singleton
@Startup
public class DatabaseSeed {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    @PostConstruct
    public void seed() {
        // ---------- ---------- ---------- ---------- Skills
        Skill java = new Skill();
        java.setDescription("Java");

        Skill angular = new Skill();
        angular.setDescription("Angular");

        Skill j2ee = new Skill();
        j2ee.setDescription("J2EE");

        Skill bdd = new Skill();
        bdd.setDescription("Base de données relationnelle");

        Skill marketing = new Skill();
        marketing.setDescription("Marketing");

        em.persist(angular);
        em.persist(java);
        em.persist(j2ee);
        em.persist(bdd);
        em.persist(marketing);

        // ---------- ---------- ---------- ---------- Candidates
        User user = new User();
        user.setEmail("jean.pierre@mail.fr");
        user.setFirstname("Jean");
        user.setLastname("Pierre");
        user.setPassword("password");
        user.setRole(User.Role.CANDIDATE);

        Candidate cand = new Candidate();
        cand.setUser(user);
        cand.setDescription("Je cherche un stage !");
        cand.addSkill(java);
        cand.addSkill(marketing);

        em.persist(cand);

        // ---------- ---------- ---------- ---------- Recruiters
        User user2 = new User();
        user2.setEmail("titi.toto@mail.fr");
        user2.setFirstname("titi");
        user2.setLastname("toto");
        user2.setPassword("password");
        user2.setRole(User.Role.RECRUITER);

        Recruiter r = new Recruiter(user2, "tititotoCorp");

        Post p = new Post("AP-20161107-1", "Chef de projet", "7 dans la fonction de Chef de projet", "PM7", 58000, 70000, "CDI", "Nancy", "CP-Axions", "Assistance MOA"/*, r*/);
        p.setDescription("Pour l’un de ses clients qui est un grand groupe dans le domaine bancaire,CP-Axions recherche un chef de projet qui sera rattaché(e) à la Direction Projet et impliqué(e) principalement dans les missions suivantes :...");
        p.setImportantNotes("Certains déplacements au sein de l’Union Européenne peuvent s’avérer nécessaire.");
        
        PostSkill ps = new PostSkill(java, PostSkill.Type.OBLIGATOIRE);
        PostSkill ps2 = new PostSkill(j2ee, PostSkill.Type.PLUS);
        
        p.addSkill(ps);
        p.addSkill(ps2);
        
        r.addPost(p);
        
        em.persist(r);
        em.flush();
    }
}
