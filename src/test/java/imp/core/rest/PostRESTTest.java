/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.entity.Skill;
import imp.core.entity.post.Post;
import imp.core.entity.post.PostSkill;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static org.hamcrest.Matchers.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 *
 * @author Leopold
 */
public class PostRESTTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    private Recruiter recruiterTest;
    private Post postTest;
    private PostSkill postskillTest;
    private Skill skillTest;

    public PostRESTTest() {
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
        // ---------- ---------- ---------- ---------- init the entity manager
        em = emf.createEntityManager();
        em.getTransaction().begin();

        // ---------- ---------- ---------- ---------- create a new skill for the tests
        skillTest = new Skill();
        skillTest.setDescription("test");

        em.persist(skillTest);

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

        postskillTest = new PostSkill(skillTest, PostSkill.Type.OBLIGATOIRE);

        postTest.addPostskill(postskillTest);

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
        em.remove(em.merge(postskillTest));
        em.remove(em.merge(skillTest));
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
    
    public JSONObject generateJSONObject() {
        JSONObject json = new JSONObject();
        json.put("contractType", "test contractType");
        json.put("description", "test description");
        json.put("experience", "test experience");
        json.put("importantNotes", "test importantNotes");
        json.put("maxSalary", 3);
        json.put("minSalary", 2);
        json.put("organization", "test organization");

        JSONArray skills = new JSONArray();
        JSONObject s1 = new JSONObject();
        JSONObject java = new JSONObject();
        java.put("description", "Java");
        java.put("id", 2);
        s1.put("skill", java);
        s1.put("type", "OBLIGATOIRE");
        skills.add(s1);
        JSONObject s2 = new JSONObject();
        JSONObject angular = new JSONObject();
        angular.put("description", "Angular");
        angular.put("id", 1);
        s2.put("skill", angular);
        s2.put("type", "OBLIGATOIRE");
        skills.add(s2);
        json.put("postskill", skills);

        json.put("reference", "test reference");
        json.put("salaryIndex", "test salaryIndex");
        json.put("title", "test title");
        json.put("workUnit", "test workUnit");
        json.put("workplace", "test workplace");

        return json;
    }

    @Test
    public void addPost() {
        given().contentType(MediaType.APPLICATION_JSON).body(generateJSONObject())
                .when().post("http://localhost:8080/imp/api/recruiters/" + recruiterTest.getId() + "/posts")
                .then().statusCode(201)
                .body("id", greaterThan(0))
                .body("contractType", equalTo("test contractType"))
                .body("description", equalTo("test description"))
                .body("experience", equalTo("test experience"))
                .body("importantNotes", equalTo("test importantNotes"))
                .body("maxSalary", equalTo(3))
                .body("minSalary", equalTo(2))
                .body("organization", equalTo("test organization"))
                .body("postskill.size()", equalTo(2))
                .body("reference", equalTo("test reference"))
                .body("salaryIndex", equalTo("test salaryIndex"))
                .body("title", equalTo("test title"))
                .body("workUnit", equalTo("test workUnit"))
                .body("workplace", equalTo("test workplace"));
    }

    @Test
    public void updatePost() {
        JSONObject newJson
                = given().contentType(MediaType.APPLICATION_JSON).body(generateJSONObject())
                        .when().post("http://localhost:8080/imp/api/recruiters/" + recruiterTest.getId() + "/posts")
                        .body().as(JSONObject.class);

        // update the json
        newJson.put("title", "new test title");

        // get id value 
        double id = (double) newJson.get("id");

        // need to reformat in int value
        newJson.put("maxSalary", 3);
        newJson.put("minSalary", 2);
        newJson.put("id", (int) id);

        given().contentType(MediaType.APPLICATION_JSON).body(newJson)
                .when().put("http://localhost:8080/imp/api/posts/" + (int) id)
                .then().statusCode(200)
                .body("id", equalTo((int) id))
                .body("contractType", equalTo("test contractType"))
                .body("description", equalTo("test description"))
                .body("experience", equalTo("test experience"))
                .body("importantNotes", equalTo("test importantNotes"))
                .body("maxSalary", equalTo(3))
                .body("minSalary", equalTo(2))
                .body("organization", equalTo("test organization"))
                .body("postskill.size()", equalTo(2))
                .body("reference", equalTo("test reference"))
                .body("salaryIndex", equalTo("test salaryIndex"))
                .body("title", equalTo("new test title"))
                .body("workUnit", equalTo("test workUnit"))
                .body("workplace", equalTo("test workplace"));
    }

    @Test
    public void deletePost() {
        JSONObject newJson = given().contentType(MediaType.APPLICATION_JSON).body(generateJSONObject())
                .when().post("http://localhost:8080/imp/api/recruiters/" + recruiterTest.getId() + "/posts")
                .body().as(JSONObject.class);

        // get id value 
        double id = (double) newJson.get("id");

        given().contentType(MediaType.APPLICATION_JSON)
                .when().delete("http://localhost:8080/imp/api/posts/" + (int) id)
                .then().statusCode(204);
    }

    @Test
    public void getAllPost() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("http://localhost:8080/imp/api/posts")
                .then().statusCode(200)
                        .body("size()", greaterThan(0));
    }

    @Test
    public void getOnePost() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("http://localhost:8080/imp/api/posts/" + postTest.getId())
                .then().statusCode(200)
                .body("id", equalTo(Math.toIntExact(postTest.getId())))
                .body("contractType", equalTo(postTest.getContractType()))
                .body("description", equalTo(postTest.getDescription()))
                .body("experience", equalTo(postTest.getExperience()))
                .body("importantNotes", equalTo(postTest.getImportantNotes()))
                .body("maxSalary", equalTo(postTest.getMaxSalary()))
                .body("minSalary", equalTo(postTest.getMinSalary()))
                .body("organization", equalTo(postTest.getOrganization()))
                .body("postskill.size()", equalTo(postTest.getPostskill().size()))
                .body("reference", equalTo(postTest.getReference()))
                .body("salaryIndex", equalTo(postTest.getSalaryIndex()))
                .body("title", equalTo(postTest.getTitle()))
                .body("workUnit", equalTo(postTest.getWorkUnit()))
                .body("workplace", equalTo(postTest.getWorkplace()));
    }
    
    @Test
    public void getOnePostWithElementDoesNotExist() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("http://localhost:8080/imp/api/posts/" + "100000")
                .then().statusCode(404);
    }
    
    @Test
    public void updatePostWithElementDoesNotExist() {
        JSONObject json = generateJSONObject();
        json.put("id", -1);

        given().contentType(MediaType.APPLICATION_JSON).body(json)
                .when().put("http://localhost:8080/imp/api/posts/" + "100000")
                .then().statusCode(404);
    }
    
    @Test
    public void deletePostWithElementDoesNotExist() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().delete("http://localhost:8080/imp/api/posts/" + "100000")
                .then().statusCode(404);
    }
    
    @Test
    public void methodeNotAllowed() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("http://localhost:8080/imp/api/posts/test/error")
                .then().statusCode(404);
    }

}
