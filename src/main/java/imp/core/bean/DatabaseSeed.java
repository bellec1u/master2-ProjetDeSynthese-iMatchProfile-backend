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
        
        Skill skill = new Skill();
        skill.setDescription("Banque de détail");
        skill.setType(Typeskill.METIER);
        
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
        
        Candidate cand = new Candidate();
        cand.setUser(user);
        cand.setDescription("Je cherche un stage de ouf !");        
        cand.addSkill(skill);
        Recruiter r = new Recruiter(user,"test");
        
        Post p = new Post("AP-20161107-1","Chef de projet","7 dans la fonction de Chef de projet","PM7",58000,70000,"CDI","Nancy","CP-Axions","Assistance MOA",r);
        p.setDescription("Pour l’un de ses clients qui est un grand groupe dans le domaine bancaire,CP-Axions recherche un chef de projet qui sera rattaché(e) à la Direction Projet et impliqué(e) principalement dans les missions suivantes :...");
        p.setImportantNotes("Certains déplacements au sein de l’Union Européenne peuvent s’avérer nécessaire.");
        PostSkill ps = new PostSkill(p,skill,Type.OBLIGATOIRE);
        PostSkill ps2 = new PostSkill(p,skill2,Type.PLUS);
        PostSkill ps3 = new PostSkill(p,skill3,Type.OBLIGATOIRE);
        PostSkill ps4 = new PostSkill();
        ps4.setPost(p);
        ps4.setSkill(skill4);
        PostSkill ps5 = new PostSkill(p,skill5,Type.OBLIGATOIRE);

        em.persist(skill);
        em.persist(skill2);
        em.persist(skill3);
        em.persist(skill4);
        em.persist(skill5);
        em.persist(cand);
        em.persist(r);
        em.persist(p);
        em.persist(ps);
        em.persist(ps2);   
        em.persist(ps3);
        em.persist(ps4);    
        em.persist(ps5);
    }
}
