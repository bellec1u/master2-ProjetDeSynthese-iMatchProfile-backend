/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.post.AssociatedCandidate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author dyasar
 */
@Stateless
public class AssociatedCandidateRepository extends AbstractRepository<AssociatedCandidate> {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public AssociatedCandidateRepository() {
        super(AssociatedCandidate.class);
    }  

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<AssociatedCandidate> getAssociatedCandidates(Long postId) {
        List<AssociatedCandidate> associatedCandidate = em
                .createNamedQuery("AssociatedCandidate.findByPost", AssociatedCandidate.class)
                .setParameter("postid", postId)
                .getResultList();

        return associatedCandidate;        
    }

    public boolean exist(Long postId, Long candidateId) {
         List<AssociatedCandidate> associatedCandidate = em
                .createNamedQuery("AssociatedCandidate.isAssociated", AssociatedCandidate.class)
                .setParameter("postid", postId)
                .setParameter("candidateId", candidateId)
                .getResultList();
              if(associatedCandidate.isEmpty())
                  return false;
              return true;
    }

}
