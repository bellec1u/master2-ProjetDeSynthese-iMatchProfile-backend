/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Post;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Leopold
 */
@Stateless
public class PostRepository extends AbstractRepository<Post> {
    
     @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;
    
    public PostRepository() {
        super(Post.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
