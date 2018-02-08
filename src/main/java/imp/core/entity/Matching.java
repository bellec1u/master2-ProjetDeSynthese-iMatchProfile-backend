/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity;

import imp.core.entity.post.Post;
import imp.core.entity.user.Candidate;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Leopold
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Matching.findByPost",
            query = "SELECT m FROM Matching m WHERE m.post.id = :id"),
    @NamedQuery(name = "Matching.cleanTable",
            query = "DELETE FROM Matching")
})
@Table(name = "Matchings")
public class Matching implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double percent;
    @ManyToOne
    private Candidate candidate;
    @ManyToOne
    private Post post;

    public Matching() {
        
    }
    
    public Matching(Candidate candidate, Post post, double percent) {
        this.candidate = candidate;
        this.post = post;
        this.percent = percent;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
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
        if (!(object instanceof Matching)) {
            return false;
        }
        Matching other = (Matching) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Matching{" + "id=" + id + ", candidate=" + candidate + ", post=" + post + ", percent=" + percent + '}';
    }
    
}
