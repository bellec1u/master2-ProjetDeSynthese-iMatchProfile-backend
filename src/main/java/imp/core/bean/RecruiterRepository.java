/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.post.Post;
import imp.core.entity.post.PostSkill;
import imp.core.entity.user.Recruiter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public List<Recruiter> getAll() {
        return executeNamedQuery("Recruiter.findAll");
    }

    public Post addPost(Long id, Post post) {
        List<PostSkill> lps = new ArrayList<>();
        // for each postskills
        for (PostSkill ps : post.getPostskill()) {
            // search it in the db
            try {
                TypedQuery<PostSkill> query = getEntityManager()
                        .createNamedQuery("PostSkill.findBySkillAndType", PostSkill.class);
                // if find it -> add it to the post
                lps.add(query.setParameter("id_skill", ps.getSkill().getId())
                        .setParameter("type", ps.getType())
                        .getSingleResult());
            } catch (NoResultException e) {
                // otherwise -> create a new postskill and add it to the post
                PostSkill nps = new PostSkill(ps.getSkill(), ps.getType());
                lps.add(nps);
            }
        }
        // set the new postskills list
        post.setPostskill(lps);
        // get the good recruiter
        Recruiter recruiter = super.getById(id);
        // add the new post to the recruiter
        recruiter.addPost(post);
        // update the recruiter
        edit(recruiter);
        return recruiter.getPost().get( recruiter.getPost().size() - 1 );
    }

}
