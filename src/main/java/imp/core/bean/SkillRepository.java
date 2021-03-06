/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.Skill;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Leopold
 */
@Stateless
public class SkillRepository extends AbstractRepository<Skill> {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public SkillRepository() {
        super(Skill.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Skill> getAll() {
        return executeNamedQuery("Skill.findAll");
    }

    public List<Skill> getLikeDescription(String description) {
        return em.createNamedQuery("Skill.likeDescription")
                .setParameter("description", description)
                .getResultList();
    }

}
