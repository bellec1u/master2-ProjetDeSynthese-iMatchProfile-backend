/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Auktis
 */
@Entity
@Table(name = "Skills")
public class Skill implements Serializable {

    static public enum Type {WORK, FUNCTIONAL, TECHNICAL, LINGUISTIC};
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "type")
    private Type type;
    
    @Column(name = "description")
    private String description;
    
    @OneToMany(mappedBy="skill", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<PostSkill> postskill;
    
    @ManyToMany(mappedBy="skills")
    private Set<Candidate> candidates;
     
    public Skill() {
    }

    public Skill(Type type, String description) {
        this.type = type;
        this.description = description;
        this.postskill = new ArrayList<>();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<PostSkill> getSkill() {
      return postskill;
    }

    public void setSkill(List<PostSkill> skill) {
        this.postskill = skill;
    }
    
    public void addSkill(PostSkill s) {
        s.setSkill(this);
        this.postskill.add(s);
    }

    public List<PostSkill> getPostskill() {
        return postskill;
    }

    public void setPostskill(List<PostSkill> postskill) {
        this.postskill = postskill;
    }

    public Set<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(Set<Candidate> candidates) {
        this.candidates = candidates;
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
        if (!(object instanceof Skill)) {
            return false;
        }
        Skill other = (Skill) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Skill{" + "id=" + id + ", type=" + type + ", description=" + description + ", skill=" + postskill + '}';
    }
    
}
