/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

/**
 *
 * @author Leopold
 */
@Entity
@NamedQueries({
 
})
@Table(name = "Users")
public class User implements Serializable {

    public static enum Role {CANDIDATE, RECRUITER, MODERATOR};
    
    public static enum State {OK, SUSPENDED, CLOSED, BANNED};
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "lastname")
    private String lastname;
    
    @Column(name = "firstname")
    private String firstname;
    
    /**
     * Link between the entity User and the entities Recruiter and Candidate.
     */
    @Column(name = "role")
    private Role role;
    
    /**
     * Number of reports that this user has received.
     */
    @Column(name = "report_number")
    private long reportNumber = 0;
    
    @Column(name = "state")
    private State state = State.OK;
    
    public User() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(long reportNumber) {
        this.reportNumber = reportNumber;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.User{" + "id=" + id + ", email=" + email + ", password=" + password + ", lastname=" + lastname + ", firstname=" + firstname + ", role=" + role + ", reportNumber=" + reportNumber + ", state=" + state + '}';
    }
    
}
