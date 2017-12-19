/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.post;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
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
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author yasar
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Post.findAll",
            query = "SELECT p FROM Post p")
})
@Table(name = "Posts")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * Date of publication of the post
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "publication_date")
    private LocalDateTime publicationDate;
    
    /**
     * Reference of the specific post
     */
    @Column(name = "reference")
    private String reference;
    
    /**
     * Title of the post
     */
    @Column(name = "title")
    private String title;
    
    @Column(name = "experience")
    private String experience;
    
    /**
     * Salary of the post
     */
    @Column(name = "salary_index")
    private String salaryIndex;
    
    /**
     * Minimal salary
     */
    @Column(name = "minimal_salary")
    private int minSalary;
    
    /**
     * Maximal salary
     */
    @Column(name = "maximal_salary")
    private int maxSalary;
    
    /**
     * Type of the contract
     */
    @Column(name = "contract_type")
    private String contractType;
    
    /**
     * Description of the post
     */
    @Column(name = "description")
    private String description;
    
    /**
     * Importants points of the post
     */
    @Column(name = "important_notes")
    private String importantNotes;
    
    
    // Position context

    /**
     * Localisation of the job
     */
    @Column(name = "workplace")
    private String workplace;
    
    /**
     * Company of the post
     */
    @Column(name = "organisation")
    private String organization;
    
    /**
     * Service of the post (info/...)
     */
    @Column(name = "work_unit")
    private String workUnit;
    
    /**
     * Recruiter of the post
     */
//    @ManyToOne
//    private Recruiter recruiter; 
    
    /**
     * Skills recommanded for the post
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private List<PostSkill> postskill;
    
    
    
     
    public Post() {
    }

    public Post(String reference, String title,String experience, String salaryIndex, int minSalary, int maxSalary, String contractType, String workplace, String organization, String workUnit/*,Recruiter recruiter*/) {
        this.reference = reference;
        this.title = title;
        this.experience = experience;
        this.salaryIndex = salaryIndex;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.contractType = contractType;
        this.workplace = workplace;
        this.organization = organization;
        this.workUnit = workUnit;
        this.postskill = new ArrayList<>();
//        this.recruiter = recruiter;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalaryIndex() {
        return salaryIndex;
    }

    public void setSalaryIndex(String salaryIndex) {
        this.salaryIndex = salaryIndex;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImportantNotes() {
        return importantNotes;
    }

    public void setImportantNotes(String importantNotes) {
        this.importantNotes = importantNotes;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    public List<PostSkill> getSkill() {
        return postskill;
    }

    public void setSkill(List<PostSkill> skill) {
        this.postskill = skill;
    }
    
    public void addSkill(PostSkill s) {
        this.postskill.add(s);
    }

//    public Recruiter getRecruiter() {
//        return recruiter;
//    }
//
//    public void setRecruiter(Recruiter recruiter) {
//        this.recruiter = recruiter;
//    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<PostSkill> getPostskill() {
        return postskill;
    }

    public void setPostskill(List<PostSkill> postskill) {
        this.postskill = postskill;
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
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", publicationDate=" + publicationDate + ", reference=" + reference + ", title=" + title + ", experience=" + experience + ", salaryIndex=" + salaryIndex + ", minSalary=" + minSalary + ", maxSalary=" + maxSalary + ", contractType=" + contractType + ", description=" + description + ", importantNotes=" + importantNotes + ", workplace=" + workplace + ", organization=" + organization + ", workUnit=" + workUnit + ", postskill=" + postskill + '}';
    }


   
}
