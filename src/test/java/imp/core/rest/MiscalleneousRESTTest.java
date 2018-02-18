/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import static io.restassured.RestAssured.given;
import javax.ws.rs.core.MediaType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author alexis
 */
public class MiscalleneousRESTTest {
    
    public MiscalleneousRESTTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        RESTSetupHelper.setUpServer();
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
    public void routeUndefined() {
        given().contentType(MediaType.APPLICATION_JSON)
                .when().get("http://localhost:8080/imp/api/route-undefined")
                .then().statusCode(404);
    }
}
