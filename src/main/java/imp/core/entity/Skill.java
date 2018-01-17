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
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Auktis
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Skill.findAll",
            query = "SELECT s FROM Skill s"),
    @NamedQuery(name = "Skill.likeDescription",
            query = "SELECT s FROM Skill s WHERE LOWER(s.description) LIKE LOWER(CONCAT('%', :description, '%'))")
})
@Table(name = "Skills")
public class Skill implements Serializable {

    static public enum Typeskill {
        METIER, FONCTIONNELLES, TECHNQUES, LINGUISTIQUES
    };

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private Typeskill type;

    @Column(name = "description")
    private String description;

    public Skill() {
    }

    public Skill(Typeskill type, String description) {
        this.type = type;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Typeskill getType() {
        return type;
    }

    public void setType(Typeskill type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Skill{" + "id=" + id + ", type=" + type + ", description=" + description + '}';
    }

}
