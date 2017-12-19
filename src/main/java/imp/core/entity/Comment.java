/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package imp.core.entity;

import imp.core.entity.user.User;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Mohamed
 */
@Entity
@Table(name = "Comments")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Comment's content
     */
    @Column(name = "content")
    private String content;

    /**
     * Status of the comment (valid / need to be modified / ...)
     */
    @Column(name = "status")
    private boolean status;

    /**
     * User who receives this comment
     */
    @ManyToOne
    @JoinColumn(name = "commented_user")
    private User commentedUser;

    /**
     * User who wrote this comment
     */
    @ManyToOne
    @JoinColumn(name = "commenting_user")
    private User commentingUser;

    public Comment() {

    }

    public Comment(String content, boolean status, User commentedUser, User commentingUser) {
        this.content = content;
        this.status = status;
        this.commentedUser = commentedUser;
        this.commentingUser = commentingUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUserFor() {
        return commentedUser;
    }

    public void setUserFor(User commentedUser) {
        this.commentedUser = commentedUser;
    }

    public User getUserFrom() {
        return commentingUser;
    }

    public void setUserFrom(User userFrom) {
        this.commentingUser = userFrom;
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
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Comment[ "
                + "id=" + id + ", "
                + "content=" + content + ", "
                + "status=" + status + ", "
                + "commentedUser=" + commentedUser + ", "
                + "commentingUser=" + commentingUser
                + " ]";
    }

}
