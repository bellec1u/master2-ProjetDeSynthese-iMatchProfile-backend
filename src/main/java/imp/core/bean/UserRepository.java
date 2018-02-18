/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.user.Candidate;
import imp.core.entity.user.User;
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
public class UserRepository extends AbstractRepository<User> {
    
    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public UserRepository() {
        super(User.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<User> findByEmail(String email) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        return query.getResultList();
    }
    
    public <T> T getConcreteUser(Class<T> userClass, Long userId) {
        String query = userClass == Candidate.class ? 
            "Candidate.findByUserId" :
            "Recruiter.findByUserId";
        
        return em.createNamedQuery(query, userClass)
                .setParameter("id", userId)
                .getSingleResult();
    }
    
}
