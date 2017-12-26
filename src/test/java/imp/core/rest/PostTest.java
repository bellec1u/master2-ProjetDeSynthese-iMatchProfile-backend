/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.entity.post.Post;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.ws.rs.core.MediaType;
import static org.junit.Assert.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 *
 * @author Leopold
 */
public class PostTest {

    public PostTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = Integer.valueOf(8080);
        } else {
            RestAssured.port = Integer.valueOf(port);
        }

        String basePath = System.getProperty("server.base");
        if (basePath == null) {
            basePath = "/imp/api/";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;
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

    @Test
    public void addPost() {
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

        given().contentType(MediaType.APPLICATION_JSON).body(json)
                .when().post("http://localhost:8080/imp/api/recruiters/12/post")
                .then().statusCode(200)
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
}
