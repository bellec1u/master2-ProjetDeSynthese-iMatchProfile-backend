/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Exemple;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author auktis
 */
@Stateless
public class ExempleRepository extends AbstractRepository<Exemple> {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;
    
    public ExempleRepository() {
        super(Exemple.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Exemple> getAll() {
        return executeNamedQuery("Exemple.findAll");
    }

    /**
     * Only for test purposes
     * @return 
     */
    public Exemple add() {
        Exemple ex = new Exemple();
        ex.setName("John");
        em.persist(ex);
        return ex;
    }
    
}
