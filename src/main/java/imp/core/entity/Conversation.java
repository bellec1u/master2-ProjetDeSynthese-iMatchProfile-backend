/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity;

import imp.core.entity.user.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author yasar
 */
@Entity
@Table(name = "Conversations")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * User who start the conversation
     */
    @OneToOne
    @JoinColumn(name = "user1")
    private User User1;

    /**
     * Second user of the conversation
     */
    @OneToOne
    @JoinColumn(name = "user2")
    private User User2;

    /**
     * list of messages corresponding to the conversation
     */
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messages;

    public Conversation() {
    }

    public Conversation(User User1, User User2) {
        this.User1 = User1;
        this.User2 = User2;
        this.messages = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser1() {
        return User1;
    }

    public void setUser1(User User1) {
        this.User1 = User1;
    }

    public User getUser2() {
        return User2;
    }

    public void setUser2(User User2) {
        this.User2 = User2;
    }

    public List<Message> getMsg() {
        return messages;
    }

    public void setMsg(List<Message> msg) {
        this.messages = msg;
    }

    public void addMessage(Message m) {
        m.setConversation(this);
        this.messages.add(m);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conversation)) {
            return false;
        }
        Conversation other = (Conversation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Conversation[ "
                + "id=" + id + ", "
                + "User1=" + User1 + ", "
                + "User2=" + User2 + ", "
                + "messages=" + messages
                + " ]";
    }

}
