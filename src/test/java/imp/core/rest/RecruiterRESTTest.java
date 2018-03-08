/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.entity.post.Post;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import static io.restassured.RestAssured.given;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.MediaType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import io.restassured.response.Response;
import javax.persistence.EntityManager;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Mohamed
 */
public class RecruiterRESTTest {
    
    private static final String API_URL = "http://localhost:8080/imp/api/recruiters/";
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    private Recruiter recruiterTest;
    private Recruiter recruiterCreate;
    
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

        // create a new recruiter for the tests
        User u = new User();
        u.setEmail("test.test@test.test");
        u.setFirstname("test");
        u.setLastname("test");
        u.setPassword("passtest");
        u.setRole(User.Role.RECRUITER);
        recruiterTest = new Recruiter(u, "Test&Co");

        Post postTest = new Post("test reference", "test title", "test experience", "test salary index", 1000, 2000, "test contract type", "test workplace", "test organization", "test work unit");
        postTest.setDescription("test description test test test");
        postTest.setImportantNotes("test important notes");

        recruiterTest.addPost(postTest);
        
        em.persist(recruiterTest);

        // Recruiter for create testing
        User user2 = new User();
        user2.setEmail("Createprénom.Createnom@mail.fr");
        user2.setFirstname("Createprénom");
        user2.setLastname("Createnom");
        user2.setPassword("createpassword");
        user2.setRole(User.Role.RECRUITER);

        recruiterCreate = new Recruiter();
        recruiterCreate.setUser(user2);
        recruiterCreate.setCompany("Createcompany");
        // ^ DON'T PERSIST IT ON THIS METHOD ^

        // commit the new entities for the tests
        em.getTransaction().commit();
    }

    @After
    public void tearDown() {
        
        // remove all test entities
        em.getTransaction().begin();
        em.remove(em.merge(recruiterTest));
        
        // recruiterCreate is inserted by a POST in a test method
        // so delete it only if possible (id is set), after the test method call
        if (recruiterCreate.getId() != null) {
            em.remove(em.merge(recruiterCreate));
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
    public void getByIdExisting() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + recruiterTest.getId())
                .then().statusCode(200)
                .body("id", equalTo(Math.toIntExact(recruiterTest.getId())))
                .body("company", equalTo(recruiterTest.getCompany()))
                .body("user.id", equalTo(Math.toIntExact(recruiterTest.getUser().getId())))
                .body("user.lastname", equalTo(recruiterTest.getUser().getLastname()))
                .body("user.firstname", equalTo(recruiterTest.getUser().getFirstname()))
                .body("user.email", equalTo(recruiterTest.getUser().getEmail()))
                .body("user.reportNumber", equalTo(Math.toIntExact(recruiterTest.getUser().getReportNumber())))
                .body("user.role", equalTo(recruiterTest.getUser().getRole().toString()))
                .body("user.state", equalTo(recruiterTest.getUser().getState().toString()));
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
        json.put("id", recruiterCreate.getId());
        json.put("company", recruiterCreate.getCompany());

        JSONObject newUser = new JSONObject();
        newUser.put("id", recruiterCreate.getUser().getId());
        newUser.put("lastname", recruiterCreate.getUser().getLastname());
        newUser.put("firstname", recruiterCreate.getUser().getFirstname());
        newUser.put("email", recruiterCreate.getUser().getEmail());
        newUser.put("password", recruiterCreate.getUser().getPassword());
        newUser.put("role", recruiterCreate.getUser().getRole());

        json.put("user", newUser);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().post(API_URL)
                .then().statusCode(201)
                .extract()
                .response();

        int recruiterCreateId = response.path("id");
        int userCreateId = response.path("user.id");

        // testing that the recruiter has been created
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + recruiterCreateId)
                .then().statusCode(200)
                .body("id", equalTo(recruiterCreateId))
                .body("company", equalTo(recruiterCreate.getCompany()))
                .body("user.lastname", equalTo(recruiterCreate.getUser().getLastname()))
                .body("user.firstname", equalTo(recruiterCreate.getUser().getFirstname()))
                .body("user.email", equalTo(recruiterCreate.getUser().getEmail()))
                .body("user.reportNumber", equalTo(Math.toIntExact(recruiterCreate.getUser().getReportNumber())))
                .body("user.role", equalTo(recruiterCreate.getUser().getRole().toString()))
                .body("user.state", equalTo(recruiterCreate.getUser().getState().toString()));
        
        // set id of recruiter for removing it later
        recruiterCreate.setId(new Long(recruiterCreateId));
        recruiterCreate.getUser().setId(new Long(userCreateId));
    }
    
    @Test
    public void createInvalidUser() {
        JSONObject json = new JSONObject();
        json.put("id", recruiterCreate.getId());
        json.put("company", recruiterCreate.getCompany());

        JSONObject newUser = new JSONObject();
        newUser.put("id", recruiterCreate.getUser().getId());
        newUser.put("lastname", recruiterCreate.getUser().getLastname());
        newUser.put("firstname", recruiterCreate.getUser().getFirstname());
        newUser.put("email", recruiterCreate.getUser().getEmail());
        newUser.put("password", recruiterCreate.getUser().getPassword());
        // Role omitted on purpose

        json.put("user", newUser);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().post(API_URL)
                .then().statusCode(400);
    }
    
    @Test
    public void createInvalidCompany() {
        JSONObject json = new JSONObject();
        json.put("id", recruiterCreate.getId());
        // Company omitted on purpose
        
        JSONObject newUser = new JSONObject();
        newUser.put("id", recruiterCreate.getUser().getId());
        newUser.put("lastname", recruiterCreate.getUser().getLastname());
        newUser.put("firstname", recruiterCreate.getUser().getFirstname());
        newUser.put("email", recruiterCreate.getUser().getEmail());
        newUser.put("password", recruiterCreate.getUser().getPassword());
        newUser.put("role", recruiterCreate.getUser().getRole());
        
        json.put("user", newUser);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().post(API_URL)
                .then().statusCode(400);
    }
    
    @Test
    public void createAlreadyUsedEmail() {
        JSONObject json = new JSONObject();
        json.put("id", recruiterTest.getId());
        json.put("company", recruiterTest.getCompany());

        JSONObject newUser = new JSONObject();
        newUser.put("id", recruiterTest.getUser().getId());
        newUser.put("lastname", recruiterTest.getUser().getLastname());
        newUser.put("firstname", recruiterTest.getUser().getFirstname());
        newUser.put("email", recruiterTest.getUser().getEmail());
        newUser.put("password", recruiterTest.getUser().getPassword());
        newUser.put("role", recruiterTest.getUser().getRole());

        json.put("user", newUser);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .when().post(API_URL)
                .then().statusCode(409);        
    }
    
    @Test
    public void getPostsByIdExisting() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + recruiterTest.getId() + "/posts")
                .then().statusCode(200)
                .body("size()", greaterThan(0));
    }
    
    @Test
    public void getPostsByIdInexisting() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + "-1" + "/posts")
                .then().statusCode(404);
    }
    
}
