/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.user;

import imp.core.entity.Skill;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author alexis
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Candidate.findAll",
            query = "SELECT c FROM Candidate c")
})
@Table(name = "Candidates")
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Link to basics profile informations
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;

    /**
     * Date of birth of the user
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    /**
     * Description of the user
     */
    @Column(name = "description")
    private String description = "";

    //TODO
    // photo
    /**
     * Skills of the user
     */
    @ManyToMany
    private List<Skill> skills = new ArrayList<Skill>();

    public Candidate() {
    }

    public Candidate(User user, Date birthDate, String description) {
        this.user = user;
        this.birthDate = birthDate;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
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
        if (!(object instanceof Candidate)) {
            return false;
        }
        Candidate other = (Candidate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Candidate[ "
                + "id=" + id + ", "
                + "user=" + user + ", "
                + "birthDate=" + birthDate + ", "
                + "description=" + description
                + " ]";
    }
}