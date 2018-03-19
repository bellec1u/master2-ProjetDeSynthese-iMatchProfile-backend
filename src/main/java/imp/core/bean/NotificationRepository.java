/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.user.Notification;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mohamed
 */
@Stateless
public class NotificationRepository extends AbstractRepository<Notification> {
    
    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public NotificationRepository() {
        super(Notification.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }   
    
    public List<Notification> getNotificationsByUser(Long id){
        System.out.println("imp.core.bean.NotificationRepository.getNotificationsByUser()"+id);
        List<Notification> userNotifications = getEntityManager().createNamedQuery("Notification.findByUserId",Notification.class)
                .setParameter("id", id)
                .getResultList();
        return userNotifications;
    }
    
    public long getCountNoReadNotification(Long id){
        long count = getEntityManager().createNamedQuery("Notification.countNoReadNotification",Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
        System.out.println("imp.core.bean.NotificationRepository.getCountNoReadNotification()"+id);
        return count;
    }
    
}
