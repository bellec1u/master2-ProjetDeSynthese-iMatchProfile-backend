/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Mohamed
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Notification.findAll",
            query = "SELECT n FROM Notification n")
    ,
    @NamedQuery(name = "Notification.findByUserId",
            query = "SELECT n FROM Notification n WHERE n.user.id = :id"),
    @NamedQuery(name = "Notification.findById",
            query = "SELECT n FROM Notification n WHERE n.id = :id")
})
@Table(name = "Notifications")
public class Notification {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "message")
    private String message;
    
    @Column(name= "link")
    private String link;
    
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;
    
    @Column(name= "state")
    private boolean state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
    
    

    public Notification() {
    }
    
    

    public Notification(String message, String link, User user, boolean state) {
        this.message = message;
        this.link = link;
        this.user = user;
        this.state = state;
    }
    
    

    @Override
    public String toString() {
        return "Notification{" + "id=" + id + ", message=" + message + ", link=" + link + ", user=" + user + '}';
    }
    
    
}
