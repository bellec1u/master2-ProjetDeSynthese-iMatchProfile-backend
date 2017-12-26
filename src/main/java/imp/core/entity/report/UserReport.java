/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.report;

import imp.core.entity.user.User;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author yasar
 */
@Entity
@Table(name = "User_reports")
public class UserReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * User who receives this comment
     */
    @ManyToOne
    private User reportedUser;

    /**
     * User who wrote this comment
     */
    @ManyToOne
    private User reportingUser;

    public UserReport() {
    }

    public UserReport(User reportedUser, User reportingUser) {
        this.reportedUser = reportedUser;
        this.reportingUser = reportingUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
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
        if (!(object instanceof UserReport)) {
            return false;
        }
        UserReport other = (UserReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UserReport{" + "id=" + id + ", reportedUser=" + reportedUser + ", reportingUser=" + reportingUser + '}';
    }

}
