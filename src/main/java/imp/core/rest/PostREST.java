/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.ExempleRepository;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Leopold
 */

@Stateless
@Path("posts")
public class PostREST {
    
    @EJB
    private ExempleRepository exempleRepo;
    
}
