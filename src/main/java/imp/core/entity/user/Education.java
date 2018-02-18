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
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Leopold
 */
@Entity
@Table(name = "Educations")
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    @NotBlank(message = "education.name.notBlank")
    private String name;
    
    @Column(name = "obtaining_date")
    @NotBlank(message = "education.obtainingDate.notBlank")
    private String obtainingDate;
    
    @Column(name = "description")
    private String description;

    public Education() {
        
    }
    
    public Education(String name, String obtainingDate, String description) {
        this.name = name;
        this.obtainingDate = obtainingDate;
        this.description = description;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObtainingDate() {
        return obtainingDate;
    }

    public void setObtainingDate(String obtainingDate) {
        this.obtainingDate = obtainingDate;
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
        if (!(object instanceof Education)) {
            return false;
        }
        Education other = (Education) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Education{" + "id=" + id + ", name=" + name + ", obtainingDate=" + obtainingDate + ", description=" + description + '}';
    }
    
}
