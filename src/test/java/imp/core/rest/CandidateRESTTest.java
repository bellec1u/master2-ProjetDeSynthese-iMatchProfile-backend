/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.entity.user.Candidate;
import imp.core.entity.Skill;
import imp.core.entity.user.User;
import static io.restassured.RestAssured.given;
import java.time.LocalDate;
import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.MediaType;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author alexis
 */
public class CandidateRESTTest {
    
    private final static String API_URL = "http://localhost:8080/imp/api/candidates/";
    
    private static EntityManagerFactory emf;
    
    private static EntityManager em;

    private static Candidate candidateTest;
    
    private static Skill skillTest;
        
    private static Skill newSkillTest;
    
    public CandidateRESTTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        RESTSetupHelper.setUpServer();
        emf = Persistence.createEntityManagerFactory("imp-test-pu");
    }
    
    @AfterClass
    public static void tearDownClass() {
        emf.close();
    }
    
    @Before
    public void setUp() {
        // init the entity manager
        em = emf.createEntityManager();

        em.getTransaction().begin();
        // create a new candidate with a new skill
        User user = new User();
        user.setEmail("Testprénom.Testnom@mail.fr");
        user.setFirstname("Testprénom");
        user.setLastname("Testnom");
        user.setPassword("testpassword");
        user.setRole(User.Role.CANDIDATE);
        
        skillTest = new Skill();
        skillTest.setDescription("Compétence Fonctionnel Test");
        skillTest.setType(Skill.Typeskill.FONCTIONNELLES);
        
        candidateTest = new Candidate();
        candidateTest.setUser(user);
        candidateTest.setBirthDate(LocalDate.of(1980, 1, 9));

        candidateTest.setDescription("Description test");
        candidateTest.addSkill(skillTest);
        
        newSkillTest = new Skill();
        newSkillTest.setDescription("New test skill");
        newSkillTest.setType(Skill.Typeskill.METIER);
        
        em.persist(skillTest);
        em.persist(candidateTest);

        em.persist(newSkillTest);
        
        // commit the new entities for the tests
        em.getTransaction().commit();
    }
    
    @After
    public void tearDown() {
        // remove all test entities
        em.getTransaction().begin();
        em.remove(em.merge(candidateTest));
        em.remove(em.merge(skillTest));
        em.getTransaction().commit();

        // if one transaction has not finished
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }

        // close the entity manager
        if (em.isOpen()) {
            em.close();
        }
    }

    @Test
    public void getAll() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL)
                .then().statusCode(200)
                .body("size()", greaterThan(0));
    }
    
    @Test
    public void getByIdExisting() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + candidateTest.getId())
                .then().statusCode(200)
                .body("id", equalTo(Math.toIntExact(candidateTest.getId())))
                .body("description", equalTo(candidateTest.getDescription()))
                .body("user.id", equalTo(Math.toIntExact(candidateTest.getUser().getId())))
                .body("user.lastname", equalTo(candidateTest.getUser().getLastname()))
                .body("user.firstname", equalTo(candidateTest.getUser().getFirstname()))
                .body("user.email", equalTo(candidateTest.getUser().getEmail()))
                .body("user.password", equalTo(candidateTest.getUser().getPassword()))
                .body("user.reportNumber", equalTo(Math.toIntExact(candidateTest.getUser().getReportNumber())))
                .body("user.role", equalTo(candidateTest.getUser().getRole().toString()))
                .body("user.state", equalTo(candidateTest.getUser().getState().toString()))
                .body("skills.size()", equalTo(1));
    }
    
    @Test
    public void getByIdInexisting() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + "-1")
                .then().statusCode(404);
    }
    
    @Test
    public void updateExisting() {
        String newDescription = "new description";
        String newEmail = "newmail@mail.fr";
                        
        JSONObject json = new JSONObject();
        json.put("id", candidateTest.getId());
        json.put("description", newDescription);
        json.put("birthDate", candidateTest.getBirthDate());
        
        JSONObject newUser = new JSONObject();
        newUser.put("id", candidateTest.getUser().getId());
        newUser.put("lastname", candidateTest.getUser().getLastname());
        newUser.put("firstname", candidateTest.getUser().getFirstname());
        newUser.put("email", newEmail);
        newUser.put("password", candidateTest.getUser().getPassword());
        newUser.put("reportNumber", candidateTest.getUser().getReportNumber());
        newUser.put("role", candidateTest.getUser().getRole());
        newUser.put("state", candidateTest.getUser().getState());
        
        json.put("user", newUser);
        
        JSONArray skills = new JSONArray();
        JSONObject s1 = new JSONObject();
        s1.put("description", skillTest.getDescription());
        s1.put("id", skillTest.getId());
        JSONObject s2 = new JSONObject();
        s2.put("description", newSkillTest.getDescription());
        s2.put("id", newSkillTest.getId());
        skills.add(s1);
        skills.add(s2);
        
        json.put("skills", skills);
        
        given().contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().put(API_URL + candidateTest.getId())
                .then().statusCode(200)
                .body("id", equalTo(Math.toIntExact(candidateTest.getId())))
                .body("description", equalTo(newDescription))
                .body("user.id", equalTo(Math.toIntExact(candidateTest.getUser().getId())))
                .body("user.lastname", equalTo(candidateTest.getUser().getLastname()))
                .body("user.firstname", equalTo(candidateTest.getUser().getFirstname()))
                .body("user.email", equalTo(newEmail))
                .body("user.password", equalTo(candidateTest.getUser().getPassword()))
                .body("user.reportNumber", equalTo(Math.toIntExact(candidateTest.getUser().getReportNumber())))
                .body("user.role", equalTo(candidateTest.getUser().getRole().toString()))
                .body("user.state", equalTo(candidateTest.getUser().getState().toString()))
                .body("skills.size()", equalTo(2));
    }
    
    @Test
    public void updateInexisting() {
        String newDescription = "new description";
        String newEmail = "newmail@mail.fr";
                        
        JSONObject json = new JSONObject();
        json.put("id", candidateTest.getId());
        json.put("description", newDescription);
        json.put("birthDate", candidateTest.getBirthDate());
        
        JSONObject newUser = new JSONObject();
        newUser.put("id", candidateTest.getUser().getId());
        newUser.put("lastname", candidateTest.getUser().getLastname());
        newUser.put("firstname", candidateTest.getUser().getFirstname());
        newUser.put("email", newEmail);
        newUser.put("password", candidateTest.getUser().getPassword());
        newUser.put("reportNumber", candidateTest.getUser().getReportNumber());
        newUser.put("role", candidateTest.getUser().getRole());
        newUser.put("state", candidateTest.getUser().getState());
        
        json.put("user", newUser);
        
        JSONArray skills = new JSONArray();
        JSONObject s1 = new JSONObject();
        s1.put("description", skillTest.getDescription());
        s1.put("id", skillTest.getId());
        JSONObject s2 = new JSONObject();
        s2.put("description", newSkillTest.getDescription());
        s2.put("id", newSkillTest.getId());
        skills.add(s1);
        skills.add(s2);
        
        json.put("skills", skills);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().put(API_URL + "-1")
                .then().statusCode(404);
    }
    
    @Test
    public void deleteExisting() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().delete(API_URL + candidateTest.getId())
                .then().statusCode(204);
    }
    
    @Test
    public void deleteInexisting() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().delete(API_URL + "-1")
                .then().statusCode(404);
    }
    
}
