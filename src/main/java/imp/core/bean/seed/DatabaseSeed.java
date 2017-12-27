/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean.seed;

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

    private Skill java, angular, j2ee, bdd, marketing;
    
    @PostConstruct
    public void seed() {
        // ---------- ---------- ---------- ---------- Skills
        this.java = new Skill();
        java.setDescription("Java");

        this.angular = new Skill();
        angular.setDescription("Angular");

        this.j2ee = new Skill();
        j2ee.setDescription("J2EE");

        this.bdd = new Skill();
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

        p.addPostskill(ps);
        p.addPostskill(ps2);
        
        r.addPost(p);
        
        Post p2 = new Post("AP-20161107-2", "Chef de projet", "7 dans la fonction de Chef de projet", "PM7", 58000, 70000, "CDI", "Nancy", "CP-Axions", "Assistance MOA");
        p2.setDescription("Pour l’un de ses clients qui est un grand groupe dans le domaine bancaire,CP-Axions recherche un chef de projet qui sera rattaché(e) à la Direction Projet et impliqué(e) principalement dans les missions suivantes :...");
        p2.setImportantNotes("Certains déplacements au sein de l’Union Européenne peuvent s’avérer nécessaire.");
        
        p2.addPostskill(ps);
        
        r.addPost(p2);
        
        em.persist(r);
        em.flush();

        // ---------- ---------- ---------- ---------- For tests
        User u = new User();
        u.setEmail("test.test@test.test");
        u.setFirstname("test");
        u.setLastname("test");
        u.setPassword("passtest");
        u.setRole(User.Role.RECRUITER);

        Recruiter recru = new Recruiter(u, "Test&Co");

        Post post = new Post("test reference", "test title", "test experience", "test salaryindex", 1, 2, "test contracttype", "test workplace", "test organisation", "test workunit");
        post.setDescription("this is a test for testing a test in a class test");
        post.setImportantNotes("important notes of the test");

        post.addPostskill(ps);
        post.addPostskill(ps2);
        
        recru.addPost(post);
        
        em.persist(recru);
        em.flush();
    }
}
