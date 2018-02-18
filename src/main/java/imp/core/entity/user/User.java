/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Leopold
 */
@Entity
@Table(name = "Users")
@NamedQueries({
    @NamedQuery(name = "User.findByEmail",
            query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User implements Serializable {

    public static enum Role {
        CANDIDATE, RECRUITER, MODERATOR
    };

    public static enum State {
        OK, SUSPENDED, CLOSED, BANNED
    };

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
    @Column(name = "email")                                                                                                                                                                                     
    @NotNull(message = "{user.email.notNull}")
    @Email(message = "{user.email.email}")
    private String email;

    @Column(name = "password")
    @NotNull(message = "{user.password.notNull}")
    @Size(min = 6, message = "{user.password.min}")
    private String password;

    @Column(name = "lastname")
    @NotBlank(message = "{user.lastname.notBlank}")
    private String lastname;

    @Column(name = "firstname")
    @NotBlank(message = "{user.firstname.notBlank}")
    private String firstname;

    /**
     * Link between the entity User and the entities Recruiter and Candidate.
     */
    @Column(name = "role")
    @NotNull(message = "{user.role.notNull}")
    private Role role;

    /**
     * Number of reports that this user has received.
     */
    @Column(name = "report_number")
    @Min(value = 0, message = "{user.reportNumber.min}")
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
