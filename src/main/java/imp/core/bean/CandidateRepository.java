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

/**
 *
 * @author alexis
 */
@Stateless
public class CandidateRepository extends AbstractRepository<Candidate> {
    
    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public CandidateRepository() {
        super(Candidate.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Candidate> getAll() {
        return executeNamedQuery("Candidate.findAll");
    }
    
}
