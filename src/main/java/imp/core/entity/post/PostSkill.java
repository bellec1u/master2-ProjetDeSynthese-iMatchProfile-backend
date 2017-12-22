/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.post;

import imp.core.entity.Skill;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author dyasar
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "PostSkill.findBySkillAndType",
            query = "SELECT ps FROM PostSkill ps WHERE ps.skill.id = :id_skill AND ps.type = :type")
})
@Table(name = "PostSkills")
public class PostSkill implements Serializable {

    static public enum Type {
        OBLIGATOIRE, PLUS
    };

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Skill skill;

    @Column(name = "type")
    private Type type;

    public PostSkill() {
    }

    public PostSkill(Skill skill, Type type) {
        this.skill = skill;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
        if (!(object instanceof PostSkill)) {
            return false;
        }
        PostSkill other = (PostSkill) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PostSkill{" + "id=" + id + ", skill=" + skill + ", type=" + type + '}';
    }

}
