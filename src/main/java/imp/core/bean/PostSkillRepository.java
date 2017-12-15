/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Post;
import imp.core.entity.PostSkill;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author dyasar
 */
@Stateless
public class PostSkillRepository extends AbstractRepository<PostSkill>{
@PersistenceContext(unitName = "imp-pu")
    private EntityManager em;
    
   public PostSkillRepository() {
        super(PostSkill.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
     public List<PostSkill> getAll() {
        return executeNamedQuery("PostSkill.findAll");
    }
    
     public List<PostSkill> getPostsById(Long id){
        TypedQuery<PostSkill> query = em.createNamedQuery("PostSkill.findPostsById", PostSkill.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}