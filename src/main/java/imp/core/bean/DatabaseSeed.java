/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Candidate;
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
        
        Skill skill = new Skill();
        skill.setDescription("Java");
        
        Candidate cand = new Candidate();
        cand.setUser(user);
        cand.setDescription("Je cherche un stage de ouf !");        
        cand.addSkill(skill);
     
        em.persist(skill);
        em.persist(cand);
    }
}
