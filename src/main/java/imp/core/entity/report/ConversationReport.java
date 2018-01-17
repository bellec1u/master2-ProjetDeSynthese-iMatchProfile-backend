/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.report;

import imp.core.entity.conversation.Conversation;
import imp.core.entity.user.User;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author yasar
 */
@Entity
@Table(name = "Conversation_reports")
public class ConversationReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reported conversation
     */
    @ManyToOne
    private Conversation reportedConversation;

    /**
     * User who report the comment
     */
    @ManyToOne
    private User reportingUser;

    public ConversationReport() {
    }

    public ConversationReport(Conversation reportedConversation, User reportingUser) {
        this.reportedConversation = reportedConversation;
        this.reportingUser = reportingUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conversation getReportedConversation() {
        return reportedConversation;
    }

    public void setReportedConversation(Conversation reportedConversation) {
        this.reportedConversation = reportedConversation;
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
        if (!(object instanceof ConversationReport)) {
            return false;
        }
        ConversationReport other = (ConversationReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CommentReport[ "
                + "id=" + id + ", "
                + "reportedConversation=" + reportedConversation + ", "
                + "reportingUser=" + reportingUser
                + " ]";
    }

}
