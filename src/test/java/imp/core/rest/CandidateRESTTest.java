/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.entity.user.Candidate;
import imp.core.entity.Skill;
import imp.core.entity.user.User;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alexis
 */
public class CandidateRESTTest {
    
    /*@PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    private static Candidate candidateInserted;
    
    private static Skill skillInserted;*/
        
    public CandidateRESTTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        RESTSetupHelper.setUpServer();
                
        /*User user = new User();
        user.setEmail("Testprénom.Testnom@mail.fr");
        user.setFirstname("Testprénom");
        user.setLastname("Testnom");
        user.setPassword("testpassword");
        user.setRole(User.Role.CANDIDATE);
        
        skillInserted = new Skill();
        skillInserted.setDescription("Compétence Fonctionnel Test");
        skillInserted.setType(Skill.Typeskill.FONCTIONNELLES);
        
        candidateInserted = new Candidate();
        candidateInserted.setUser(user);
        Calendar cal = Calendar.getInstance();
        cal.set(1980, Calendar.JANUARY, 9);
        candidateInserted.setBirthDate(cal.getTime());
        candidateInserted.setDescription("Description test");
        candidateInserted.addSkill(skillInserted);*/
        
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}