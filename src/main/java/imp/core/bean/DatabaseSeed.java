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
import imp.core.entity.Recruiter;
import imp.core.entity.Skill;
import imp.core.entity.Skill.Typeskill;
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

        Skill skill0 = new Skill();
        skill0.setDescription("Banque de détail");
        skill0.setType(Typeskill.METIER);
        
        Skill skill2 = new Skill();
        skill2.setDescription("Télécommunications d'entreprise");
        skill2.setType(Typeskill.METIER);

        Skill skill3 = new Skill();
        skill3.setDescription("Microsoft Project 2013");
        skill3.setType(Typeskill.FONCTIONNELLES);
        
        Skill skill4 = new Skill();
        skill4.setDescription("Aucune");
        skill4.setType(Typeskill.TECHNQUES);
        
        Skill skill5 = new Skill();
        skill5.setDescription("Français C2");
        skill5.setType(Typeskill.LINGUISTIQUES);
        
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
        
        User u = new User();
        u.setEmail("cp-axions@mail.fr");
        u.setFirstname("Paul");
        u.setLastname("Jacky");
        u.setPassword("password");
        Recruiter r = new Recruiter(user,"test");
        
        Post p = new Post("AP-20161107-1","Chef de projet","7 dans la fonction de Chef de projet","PM7",58000,70000,"CDI","Nancy","CP-Axions","Assistance MOA",r);
        p.setDescription("Pour l’un de ses clients qui est un grand groupe dans le domaine bancaire,CP-Axions recherche un chef de projet qui sera rattaché(e) à la Direction Projet et impliqué(e) principalement dans les missions suivantes :...");
        p.setImportantNotes("Certains déplacements au sein de l’Union Européenne peuvent s’avérer nécessaire.");
     
        em.persist(angular);
        em.persist(java);
        em.persist(j2ee);
        em.persist(bdd);
        em.persist(marketing);
        
        PostSkill ps = new PostSkill(p,skill0,Type.OBLIGATOIRE);
        PostSkill ps2 = new PostSkill(p,skill2,Type.PLUS);
        PostSkill ps3 = new PostSkill(p,skill3,Type.OBLIGATOIRE);
        PostSkill ps4 = new PostSkill();
        ps4.setPost(p);
        ps4.setSkill(skill4);
        PostSkill ps5 = new PostSkill(p,skill5,Type.OBLIGATOIRE);

        em.persist(skill0);
        em.persist(skill2);
        em.persist(skill3);
        em.persist(skill4);
        em.persist(skill5);

        em.persist(cand);
        em.persist(cand2);
        em.persist(cand3);
        em.persist(cand4);
        
        em.persist(r);

        em.persist(p);
        em.persist(ps);
        em.persist(ps2);   
        em.persist(ps3);
        em.persist(ps4);    
        em.persist(ps5);
    }
}
