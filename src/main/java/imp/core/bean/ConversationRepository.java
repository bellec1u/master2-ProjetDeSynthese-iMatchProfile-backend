/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.conversation.Conversation;
import imp.core.entity.conversation.Message;
import imp.core.entity.user.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Leopold
 */
@Stateless
public class ConversationRepository extends AbstractRepository<Conversation> {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public ConversationRepository() {
        super(Conversation.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Conversation> findByUserId(Long id) {
        List<Conversation> conversations = em
                .createNamedQuery("Conversation.findByUserId", Conversation.class)
                .setParameter("id", id)
                .getResultList();

        return conversations;
    }

    public Conversation addMessage(Message message, Long id1, Long id2) {
        try {
            Conversation conversation = em
                    .createNamedQuery("Conversation.findByUsers", Conversation.class)
                    .setParameter("id1", id1)
                    .setParameter("id2", id2)
                    .getSingleResult();

            
            User user1 = em
                    .createNamedQuery("User.findById", User.class)
                    .setParameter("id", id1)
                    .getSingleResult();
            
            message.setAuthor(user1);
            
            conversation.addMessage(message);

            return super.edit(conversation);
        } catch (NoResultException e) {
            // first message of a new conversation 
            User user1 = em
                    .createNamedQuery("User.findById", User.class)
                    .setParameter("id", id1)
                    .getSingleResult();

            User user2 = em
                    .createNamedQuery("User.findById", User.class)
                    .setParameter("id", id2)
                    .getSingleResult();

            Conversation conversation = new Conversation(user1, user2);

            message.setAuthor(user1);
            conversation.addMessage(message);

            return super.create(conversation);
        }
    }

}
