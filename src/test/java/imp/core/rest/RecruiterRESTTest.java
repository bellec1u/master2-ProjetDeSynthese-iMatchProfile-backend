/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.entity.post.Post;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.MediaType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import javax.persistence.EntityManager;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
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
    private Post postTest;
    
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
        // ---------- ---------- ---------- ---------- init the entity manager
        em = emf.createEntityManager();
        em.getTransaction().begin();

        // ---------- ---------- ---------- ---------- create a new recruiter for the tests
        User u = new User();
        u.setEmail("test.test@test.test");
        u.setFirstname("test");
        u.setLastname("test");
        u.setPassword("passtest");
        u.setRole(User.Role.RECRUITER);
        recruiterTest = new Recruiter(u, "Test&Co");

        postTest = new Post("1", "1", "1", "1", 1, 2, "1", "1", "1", "1");
        postTest.setDescription("1 1 1");
        postTest.setImportantNotes("1 1");

        recruiterTest.addPost(postTest);

        em.persist(recruiterTest);

//        TypedQuery<Recruiter> query = em.createQuery("select r from Recruiter r", Recruiter.class);
//        System.out.println(query.getResultList());
        // ---------- ---------- ---------- ---------- commit the new entities for the tests
        em.getTransaction().commit();
    }

    @After
    public void tearDown() {
        
        // ---------- ---------- ---------- ---------- remove all test entities
        em.getTransaction().begin();
        em.remove(em.merge(recruiterTest));
        em.getTransaction().commit();

        // ---------- ---------- ---------- ---------- if one transaction has not finished
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }

        // ---------- ---------- ---------- ---------- close the entity manager
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
                .body("company", equalTo(recruiterTest.getCompany()));
    }

    @Test
    public void getByIdInexisting() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get(API_URL + "-1")
                .then().statusCode(404);
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
    
    @Test
    public void createRecruiterAccount() throws ParseException {
        JSONObject json = new JSONObject();
        json.put("company", "aaa");
        
        JSONObject user = new JSONObject();
        user.put("email", "a.a@a.fr");
        user.put("firstname", "a");
        user.put("lastname", "d");
        user.put("password", "f");
        user.put("reportNumber", "0");
        user.put("role", "RECRUITER");
        user.put("state", "OK");
        
        json.put("user", user);
                
        Recruiter res = given().contentType(MediaType.APPLICATION_JSON).body(json)
                .then().statusCode(200)
                .when().post(API_URL).body().as(Recruiter.class);
                
        assertEquals("a.a@a.fr", res.getUser().getEmail());
        assertEquals("a", res.getUser().getFirstname());
        assertEquals("d", res.getUser().getLastname());
        assertEquals("f", res.getUser().getPassword());
        assertEquals(0, res.getUser().getReportNumber());
    }
    
}
