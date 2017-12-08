/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Post;
import imp.core.entity.Recruiter;
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
    
    public Recruiter getById (long id){
        TypedQuery<Recruiter> query = em.createNamedQuery("Recruiter.findById", Recruiter.class);
        query.setParameter("id", id);
        List<Recruiter> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    
    
}
