/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.user.Recruiter;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
