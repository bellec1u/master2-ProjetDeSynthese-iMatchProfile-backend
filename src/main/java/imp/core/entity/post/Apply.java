/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.post;

import imp.core.entity.user.Candidate;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Karim
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Apply.findAll",
            query = "SELECT a FROM Apply a"),
    @NamedQuery(name = "Apply.findByPost",
            query = "SELECT a FROM Apply a WHERE a.post.id = :postid"),
    @NamedQuery(name = "Apply.hasApplied",
            query = "SELECT a FROM Apply a WHERE a.post.id = :postid AND a.candidat.id = :candidateId"),
    @NamedQuery(name = "Apply.findByCandidate",
            query = "SELECT a FROM Apply a WHERE a.candidat.id = :id")       
})@Table(name = "Apply")
public class Apply implements Serializable {

    public static enum State {
        INPROCESS, ACCEPTED
    };
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "state")
    private Apply.State state;
        
    @ManyToOne
    private Post post;
    
    @ManyToOne
    private Candidate candidat;

    public Apply() {
    }

    public Apply(Post post, Candidate candidat) {
        this.post = post;
        this.candidat = candidat;
        this.state = state.INPROCESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Candidate getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidate candidat) {
        this.candidat = candidat;
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
        if (!(object instanceof Apply)) {
            return false;
        }
        Apply other = (Apply) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imp.core.entity.post.Apply[ id=" + id + " ]";
    }
    
}
