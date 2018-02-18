/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean.authentication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author auktis
 */
@Startup
@Singleton
public class KeyManager {

    private String jwtKey;
    
    @PostConstruct
    private void readConfig() {
        System.out.println("Loading JWT key");
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("jwt-config.json");
        if (input == null) {
            jwtKey = "secret-key";
            return;
        }
        
        String config = new BufferedReader(new InputStreamReader(input))
                .lines().collect(Collectors.joining("\n"));
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(config).getAsJsonObject();
        jwtKey = object.get("secret-key").getAsString();
        
        System.out.println("Key used: " + jwtKey);
    }

    public String getJwtKey() {
        return jwtKey;
    }
    
}
