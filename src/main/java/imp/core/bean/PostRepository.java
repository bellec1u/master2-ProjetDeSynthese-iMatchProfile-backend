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
 * @author Leopold
 */
@Stateless
public class PostRepository extends AbstractRepository<Post> {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public PostRepository() {
        super(Post.class);
    }

    public List<Post> getAll() {
        return executeNamedQuery("Post.findAll");
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Only for test purposes
     *
     * @return
     */
    public Post add() {
        Post p = new Post();
        em.persist(p);
        return p;
    }

    public Post edit(long id, Post post) {
        List<PostSkill> lps = new ArrayList<>();
        // for each postskill
        for (PostSkill ps : post.getPostskill()) {
            try {
                TypedQuery<PostSkill> query = getEntityManager()
                        .createNamedQuery("PostSkill.findBySkillAndType", PostSkill.class);
                // if find it -> add it to the post
                lps.add(query.setParameter("id_skill", ps.getSkill().getId())
                        .setParameter("type", ps.getType())
                        .getSingleResult());
            } catch (NoResultException e) {
                // if not exist, create it and change in post
                PostSkill nps = new PostSkill(ps.getSkill(), ps.getType());
                lps.add(nps);
            }
        }
        // set the list of postskill
        post.setPostskill(lps);
        // update the post
        return super.edit(post);
    }

    @Override
    public void removeById(Object id) {
        // get the post
        Post p = super.getById(id);
        
        // get the recruiter of the post
        TypedQuery<Recruiter> query = getEntityManager().createNamedQuery("Recruiter.findCreatorOfPost", Recruiter.class);
        Recruiter r = query.setParameter("post", p).getSingleResult();

        //remove the post from posts of recruiter
        r.removePostById(id);

        // remove the post
        super.removeById(id);
    }

}
