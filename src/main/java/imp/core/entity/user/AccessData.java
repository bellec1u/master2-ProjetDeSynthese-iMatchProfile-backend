/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.user;

import java.io.Serializable;

/**
 *
 * @author master2-lmfi
 */
public class AccessData implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String accessToken;
    
    public AccessData() {}

    public AccessData(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "AccessData{" + "accessToken=" + accessToken + '}';
    }
    
}
