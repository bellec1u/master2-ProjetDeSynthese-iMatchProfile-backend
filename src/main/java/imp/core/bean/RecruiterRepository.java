/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.post.Post;
import imp.core.entity.user.Recruiter;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Mohamed
 */
@Stateless
public class RecruiterRepository extends AbstractRepository<Recruiter> {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public RecruiterRepository() {
        super(Recruiter.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Recruiter> getAll() {
        return executeNamedQuery("Recruiter.findAll");
    }
    
    public Long findOwnerIdOfThePost(Long idPost) {
        Post p = em.find(Post.class, idPost);
        
        return getEntityManager()
                .createNamedQuery("Recruiter.findCreatorOfPost", Recruiter.class)
                .setParameter("post", p)
                .getSingleResult()
                .getUser().getId();
    }

}
