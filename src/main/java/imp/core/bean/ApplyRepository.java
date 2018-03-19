/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.post.Apply;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Karim
 */
@Stateless
public class ApplyRepository extends AbstractRepository<Apply> {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public ApplyRepository() {
        super(Apply.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Apply> getAppliedCandidates(Long postId) {
        List<Apply> associatedCandidate = em
                .createNamedQuery("Apply.findByPost", Apply.class)
                .setParameter("postid", postId)
                .getResultList();

        return associatedCandidate;        
    }

    public boolean exist(Long postId, Long candidateId) {
         List<Apply> associatedCandidate = em
                .createNamedQuery("Apply.hasApplied", Apply.class)
                .setParameter("postid", postId)
                .setParameter("candidateId", candidateId)
                .getResultList();
              if(associatedCandidate.isEmpty())
                  return false;
              return true;
    }

    public List<Apply> getByCandidate(Long id) {
                List<Apply> associatedCandidate = em
                .createNamedQuery("Apply.findByCandidate", Apply.class)
                .setParameter("id", id)
                .getResultList();

        return associatedCandidate;
    }

    public List<Apply> getByPost(Long id) {
         return em.createNamedQuery("Apply.findByPost", Apply.class)
                .setParameter("postid", id)
                .getResultList();
    }

    public List<Apply> getAll() {
        return executeNamedQuery("Apply.findAll");
    }
}