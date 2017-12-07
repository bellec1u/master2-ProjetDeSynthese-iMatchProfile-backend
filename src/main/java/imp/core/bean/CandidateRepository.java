/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Candidate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author alexis
 */
@Stateless
public class CandidateRepository extends AbstractRepository {
    
    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public CandidateRepository() {
        super(Candidate.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Returns the candidate corresponding at this id.
     * @param id The id for this candidate.
     * @return The candidate corresponding at this id or null if the candidate does not exist.
     */
    public Candidate getById(Long id) {
        TypedQuery<Candidate> query = em.createNamedQuery("Candidate.getById", Candidate.class);
        query.setParameter("id", id);
        List<Candidate> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}
