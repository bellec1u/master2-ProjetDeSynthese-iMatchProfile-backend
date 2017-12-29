/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.user;

import imp.core.entity.post.Post;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author alexis
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Recruiter.findAll",
            query = "SELECT r FROM Recruiter r")
    ,
    @NamedQuery(name = "Recruiter.findCreatorOfPost",
            query = "SELECT r FROM Recruiter r WHERE :post MEMBER OF r.posts")
})
@Table(name = "Recruiters")
public class Recruiter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Link to basics profile informations
     */
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @Column(name = "company")
    private String company;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    //TODO
    // photo
    
    public Recruiter() {
        this.posts = new ArrayList<>();
    }

    public Recruiter(User user, String company) {
        this.user = user;
        this.company = company;
        this.posts = new ArrayList<>();
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<Post> getPost() {
        return posts;
    }

    public void setPost(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post p) {
        this.posts.add(p);
    }

    public Post getPostById(long id) {
        for (Post p : this.posts) {
            if (id == p.getId()) {
                return p;
            }
        }
        return null;
    }

    public void setPostById(Post post) {
        for (int i = 0; i < this.posts.size(); i++) {
            if (post.getId() == this.posts.get(i).getId()) {
                this.posts.set(i, post);
            }
        }
    }
    
    public void removePostById(Object id) {
        int index = -1;
        for (int i = 0; i < this.posts.size(); i++) {
            if (id == this.posts.get(i).getId()) {
                index = i;
            }
        }
        this.posts.remove(index);
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
        if (!(object instanceof Recruiter)) {
            return false;
        }
        Recruiter other = (Recruiter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Recruiter{" + "id=" + id + ", user=" + user + ", company=" + company + '}';
    }

}
