/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Exemple;
import imp.core.entity.Post;
import java.util.List;
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
    
    public List<Post> getAll() {
        return executeNamedQuery("Post.findAll");
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
        /**
     * Only for test purposes
     * @return 
     */
    public Post add() {
        Post p = new Post();
        p.setTitle("Chef de projet");
        em.persist(p);
        return p;
    }
    
    
}
