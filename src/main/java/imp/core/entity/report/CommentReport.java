/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.report;

import imp.core.entity.conversation.Comment;
import imp.core.entity.user.User;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author yasar
 */
@Entity
@Table(name = "Comment_reports")
public class CommentReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * Reported comment
     */
    @ManyToOne
    private Comment reportedComment;
    
    /**
     * User who report the comment
     */
    @ManyToOne
    private User reportingUser;

    
    
    
    
    public CommentReport() {
    }

    public CommentReport(Comment reportedComment, User reportingUser) {
        this.reportedComment = reportedComment;
        this.reportingUser = reportingUser;
    }
       
    
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
     public Comment getReportedComment() {
        return reportedComment;
    }

    public void setReportedComment(Comment reportedComment) {
        this.reportedComment = reportedComment;
    }

    public User getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
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
        if (!(object instanceof CommentReport)) {
            return false;
        }
        CommentReport other = (CommentReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CommentReport[ "
                + "id=" + id + ", "
                + "reportedComment=" + reportedComment + ", "
                + "reportingUser=" + reportingUser
                + " ]";
    }
    
}
