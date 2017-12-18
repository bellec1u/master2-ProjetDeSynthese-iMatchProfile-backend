/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.post.Post;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
        em.persist(p);
        return p;
    }
    
    public List<Post> getPostsByRecruiter(Long id){
        TypedQuery<Post> query = em.createNamedQuery("Post.findPostsByRec", Post.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
    
    
}
