/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Candidate;
import imp.core.entity.Post;
import imp.core.entity.PostSkill;
import imp.core.entity.PostSkill.Type;
import imp.core.entity.Skill;
import imp.core.entity.User;
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
        User user = new User();
        user.setEmail("jean.pierre@mail.fr");
        user.setFirstname("Jean");
        user.setLastname("Pierre");
        user.setPassword("password");
        user.setRole(User.Role.CANDIDATE);
        
        User user2 = new User();
        user2.setEmail("jeremy.laurent@mail.fr");
        user2.setFirstname("Jeremy");
        user2.setLastname("Laurent");
        user2.setPassword("password");
        user2.setRole(User.Role.CANDIDATE);

        User user3 = new User();
        user3.setEmail("jeanmichel.bertrandpaul@mail.fr");
        user3.setFirstname("Jean-michel");
        user3.setLastname("Bertrand-Paul");
        user3.setPassword("password");
        user3.setRole(User.Role.CANDIDATE);

        User user4 = new User();
        user4.setEmail("camille.courtal@mail.fr");
        user4.setFirstname("Camille");
        user4.setLastname("Courtal");
        user4.setPassword("password");
        user4.setRole(User.Role.CANDIDATE);
        
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

        Candidate cand = new Candidate();
        cand.setUser(user);
        cand.setDescription("Je cherche un stage !");        
        cand.addSkill(java);
        cand.addSkill(marketing);
        
        Candidate cand2 = new Candidate();
        cand2.setUser(user2);
        cand2.setDescription("Je sais tout faire ! Si vous avez besoin de quoi que ce soit, c'est facile pour moi ! Je suis le meilleur et je sais tout...");        
        cand2.addSkill(java);
        cand2.addSkill(marketing);
        cand2.addSkill(bdd);
        cand2.addSkill(angular);
        cand2.addSkill(j2ee);

        Candidate cand3 = new Candidate();
        cand3.setUser(user3);
        cand3.setDescription("Je suis actuellement à la recherche d'un CDI dans le domaine de la congolexicomatisation.");        
        cand3.addSkill(bdd);
        
        Candidate cand4 = new Candidate();
        cand4.setUser(user4);
        cand4.setDescription("Etudiante en licence de psychologie.");        
        
        Post p = new Post();
        p.setDescription("test");
        
        PostSkill ps = new PostSkill(p, angular,Type.MANDATORY);
     
        em.persist(angular);
        em.persist(java);
        em.persist(j2ee);
        em.persist(bdd);
        em.persist(marketing);
        
        em.persist(cand);
        em.persist(cand2);
        em.persist(cand3);
        em.persist(cand4);
        
        em.persist(p);
        em.persist(ps);
    }
}
