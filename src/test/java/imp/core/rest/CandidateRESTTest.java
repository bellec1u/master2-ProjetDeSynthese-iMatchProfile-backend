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
import io.restassured.response.Response;
import java.time.LocalDate;
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

    private static String tokenTest;
    
    private static Candidate candidateCreate;

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
        String candidateTestPwd = "testpassword";
        user.setPassword(candidateTestPwd);
        user.setRole(User.Role.CANDIDATE);

        skillTest = new Skill();
        skillTest.setDescription("Compétence Fonctionnel Test");
        skillTest.setType(Skill.Typeskill.FONCTIONNELLES);

        candidateTest = new Candidate();
        candidateTest.setUser(user);
        candidateTest.setBirthDate(LocalDate.of(1980, 1, 9));

        candidateTest.setDescription("Description test");
        candidateTest.addSkill(skillTest);
        em.persist(skillTest);
        em.persist(candidateTest);

        // New skill for update testing
        newSkillTest = new Skill();
        newSkillTest.setDescription("New test skill");
        newSkillTest.setType(Skill.Typeskill.METIER);
        em.persist(newSkillTest);

        // Candidate for create testing
        User user2 = new User();
        user2.setEmail("Createprénom.Createnom@mail.fr");
        user2.setFirstname("Createprénom");
        user2.setLastname("Createnom");
        user2.setPassword("createpassword");
        user2.setRole(User.Role.CANDIDATE);

        candidateCreate = new Candidate();
        candidateCreate.setUser(user2);
        candidateCreate.setBirthDate(LocalDate.of(1973, 5, 25));
        candidateCreate.setDescription("Description create");
        // ^ DON'T PERSIST IT ON THIS METHOD ^

        // commit the new entities for the tests
        em.getTransaction().commit();
        
        // using the REST API to get the access token for candidateTest
        JSONObject cred = new JSONObject();
        cred.put("email", candidateTest.getUser().getEmail());
        cred.put("password", candidateTestPwd);
        tokenTest = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(cred)
                .post("http://localhost:8080/imp/api/login")
                .path("accessToken");
    }

    @After
    public void tearDown() {
        // remove all test entities
        em.getTransaction().begin();
       
        //em.merge(candidateTest);
        em.remove(em.merge(candidateTest));
        em.remove(em.merge(candidateTest.getUser()));
        em.remove(em.merge(skillTest));

        em.remove(em.merge(newSkillTest));

        // candidateCreate is inserted by a POST in a test method
        // so delete it only if possible (id is set), after the test method call
        if (candidateCreate.getId() != null) {
            em.remove(em.merge(candidateCreate));
            em.remove(em.merge(candidateCreate.getUser()));
        }

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
                .body("birthDate", equalTo(candidateTest.getBirthDate().toString()))
                .body("user.id", equalTo(Math.toIntExact(candidateTest.getUser().getId())))
                .body("user.lastname", equalTo(candidateTest.getUser().getLastname()))
                .body("user.firstname", equalTo(candidateTest.getUser().getFirstname()))
                .body("user.email", equalTo(candidateTest.getUser().getEmail()))
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
    public void createValid() {
        JSONObject json = new JSONObject();
        json.put("id", candidateCreate.getId());
        json.put("description", candidateCreate.getDescription());
        json.put("birthDate", candidateCreate.getBirthDate().toString());

        JSONObject newUser = new JSONObject();
        newUser.put("id", candidateCreate.getUser().getId());
        newUser.put("lastname", candidateCreate.getUser().getLastname());
        newUser.put("firstname", candidateCreate.getUser().getFirstname());
        newUser.put("email", candidateCreate.getUser().getEmail());
        newUser.put("password", candidateCreate.getUser().getPassword());
        newUser.put("role", candidateCreate.getUser().getRole());

        json.put("user", newUser);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().post(API_URL)
                .then().statusCode(201)
                .extract()
                .response();

        int candidateCreateId = response.path("id");
        int userCreateId = response.path("user.id");

        // testing that the candidate has been created
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + candidateCreateId)
                .then().statusCode(200)
                .body("id", equalTo(candidateCreateId))
                .body("description", equalTo(candidateCreate.getDescription()))
                .body("birthDate", equalTo(candidateCreate.getBirthDate().toString()))
                .body("user.lastname", equalTo(candidateCreate.getUser().getLastname()))
                .body("user.firstname", equalTo(candidateCreate.getUser().getFirstname()))
                .body("user.email", equalTo(candidateCreate.getUser().getEmail()))
                .body("user.reportNumber", equalTo(Math.toIntExact(candidateCreate.getUser().getReportNumber())))
                .body("user.role", equalTo(candidateCreate.getUser().getRole().toString()))
                .body("user.state", equalTo(candidateCreate.getUser().getState().toString()));
        // set id of candidate for removing it later
        candidateCreate.setId(new Long(candidateCreateId));
        candidateCreate.getUser().setId(new Long(userCreateId));
    }
    
    @Test
    public void createInvalidUser() {
        JSONObject json = new JSONObject();
        json.put("id", candidateCreate.getId());
        json.put("description", candidateCreate.getDescription());
        json.put("birthDate", candidateCreate.getBirthDate().toString());

        JSONObject newUser = new JSONObject();
        newUser.put("id", candidateCreate.getUser().getId());
        newUser.put("lastname", candidateCreate.getUser().getLastname());
        newUser.put("firstname", candidateCreate.getUser().getFirstname());
        newUser.put("email", candidateCreate.getUser().getEmail());
        newUser.put("password", candidateCreate.getUser().getPassword());
        // Role omitted on purpose

        json.put("user", newUser);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().post(API_URL)
                .then().statusCode(400);
    }
    
    @Test
    public void createAlreadyUsedEmail() {
        JSONObject json = new JSONObject();
        json.put("id", candidateTest.getId());
        json.put("description", candidateTest.getDescription());
        json.put("birthDate", candidateTest.getBirthDate().toString());

        JSONObject newUser = new JSONObject();
        newUser.put("id", candidateTest.getUser().getId());
        newUser.put("lastname", candidateTest.getUser().getLastname());
        newUser.put("firstname", candidateTest.getUser().getFirstname());
        newUser.put("email", candidateTest.getUser().getEmail());
        newUser.put("password", candidateTest.getUser().getPassword());
        newUser.put("role", candidateTest.getUser().getRole());

        json.put("user", newUser);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().post(API_URL)
                .then().statusCode(409);        
    }

    @Test
    public void updateAuthorized() {
        String newDescription = "new description";
        LocalDate newBirthDate = LocalDate.of(1993, 10, 18);
        String newLastname = "NewLastname";
        String newFirstname = "NewFirstname";
        String newEmail = "newmail@mail.fr";
        String newPassword = "newpassword";
        int newReportNumber = 1;
        User.State newState = User.State.SUSPENDED;

        JSONObject json = new JSONObject();
        json.put("id", candidateTest.getId());
        json.put("description", newDescription);
        json.put("birthDate", newBirthDate.toString());

        JSONObject newUser = new JSONObject();
        newUser.put("id", candidateTest.getUser().getId());
        newUser.put("lastname", newLastname);
        newUser.put("firstname", newFirstname);
        newUser.put("email", newEmail);
        newUser.put("password", newPassword);
        newUser.put("reportNumber", newReportNumber);
        newUser.put("role", candidateTest.getUser().getRole());
        newUser.put("state", newState);

        json.put("user", newUser);

        JSONArray skills = new JSONArray();
        JSONObject s1 = new JSONObject();
        s1.put("description", skillTest.getDescription());
        s1.put("type", skillTest.getType());
        s1.put("id", skillTest.getId());
        JSONObject s2 = new JSONObject();
        s2.put("description", newSkillTest.getDescription());
        s2.put("type", newSkillTest.getType());
        s2.put("id", newSkillTest.getId());
        skills.add(s1);
        skills.add(s2);
        
        json.put("skills", skills);
        given().contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+ tokenTest)
                .body(json)
                .when().put(API_URL + candidateTest.getId())
                .then().statusCode(200)
                .body("id", equalTo(Math.toIntExact(candidateTest.getId())))
                .body("description", equalTo(newDescription))
                .body("birthDate", equalTo(newBirthDate.toString()))
                .body("user.id", equalTo(Math.toIntExact(candidateTest.getUser().getId())))
                .body("user.lastname", equalTo(newLastname))
                .body("user.firstname", equalTo(newFirstname))
                .body("user.email", equalTo(newEmail))
                .body("user.reportNumber", equalTo(Math.toIntExact(newReportNumber)))
                .body("user.role", equalTo(candidateTest.getUser().getRole().toString()))
                .body("user.state", equalTo(newState.toString()))
                .body("skills.size()", equalTo(2));
        
        // testing that the candidate has been updated
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + candidateTest.getId())
                .then().statusCode(200)
                .body("id", equalTo(Math.toIntExact(candidateTest.getId())))
                .body("description", equalTo(newDescription))
                .body("birthDate", equalTo(newBirthDate.toString()))
                .body("user.id", equalTo(Math.toIntExact(candidateTest.getUser().getId())))
                .body("user.lastname", equalTo(newLastname))
                .body("user.firstname", equalTo(newFirstname))
                .body("user.email", equalTo(newEmail))
                .body("user.reportNumber", equalTo(Math.toIntExact(newReportNumber)))
                .body("user.role", equalTo(candidateTest.getUser().getRole().toString()))
                .body("user.state", equalTo(newState.toString()))
                .body("skills.size()", equalTo(2));
    }

    @Test
    public void updateUnauthorized() {
        String newDescription = "new description";
        LocalDate newBirthDate = LocalDate.of(1993, 10, 18);
        String newLastname = "NewLastname";
        String newFirstname = "NewFirtname";
        String newEmail = "newmail@mail.fr";
        String newPassword = "newpassword";
        int newReportNumber = 1;
        User.State newState = User.State.SUSPENDED;

        JSONObject json = new JSONObject();
        json.put("id", candidateTest.getId());
        json.put("description", newDescription);
        json.put("birthDate", newBirthDate.toString());

        JSONObject newUser = new JSONObject();
        newUser.put("id", candidateTest.getUser().getId());
        newUser.put("lastname", newLastname);
        newUser.put("firstname", newFirstname);
        newUser.put("email", newEmail);
        newUser.put("password", newPassword);
        newUser.put("reportNumber", newReportNumber);
        newUser.put("role", candidateTest.getUser().getRole());
        newUser.put("state", newState);

        json.put("user", newUser);

        JSONArray skills = new JSONArray();
        JSONObject s1 = new JSONObject();
        s1.put("description", skillTest.getDescription());
        s1.put("type", skillTest.getType());
        s1.put("id", skillTest.getId());
        JSONObject s2 = new JSONObject();
        s2.put("description", newSkillTest.getDescription());
        s2.put("type", newSkillTest.getType());
        s2.put("id", newSkillTest.getId());
        skills.add(s1);
        skills.add(s2);
        
        json.put("skills", skills);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().put(API_URL + candidateTest.getId())
                .then().statusCode(401);
    }

    @Test
    public void deleteAuthorized() {
        given().contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+ tokenTest)
                .when().delete(API_URL + candidateTest.getId())
                .then().statusCode(204);
        
        // testing that the candidate doesn't exist anymore
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + candidateTest.getId())
                .then().statusCode(404);
    }

    @Test
    public void deleteUnauthorized() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().delete(API_URL + candidateTest.getId())
                .then().statusCode(401);
    }

}
