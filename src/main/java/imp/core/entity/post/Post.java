/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.entity.post;

import imp.core.rest.validator.PostMaxSalaryGTEMin;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author yasar
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Post.findAll",
            query = "SELECT p FROM Post p"),
    @NamedQuery(name = "Post.findById",
            query = "SELECT p FROM Post p where p.id = :id")
})
@Table(name = "Posts")
@PostMaxSalaryGTEMin
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Date of publication of the post
     */
    @Column(name = "publication_date")
    private LocalDate publicationDate = LocalDate.now();
    
    /**
     * Reference of the specific post
     */
    @Column(name = "reference")
    @NotBlank(message = "{post.reference.notBlank}")
    private String reference;
    
    /**
     * Title of the post
     */
    @Column(name = "title")
    @NotBlank(message = "{post.title.notBlank}")
    @Size(min = 5, message = "{post.title.min}")
    private String title;
    
    @Column(name = "experience")
    @NotBlank(message = "{post.experience.notBlank}")
    private String experience;
    
    /**
     * Salary of the post
     */
    @Column(name = "salary_index")
    @NotBlank(message = "{post.salaryIndex.notBlank}")
    private String salaryIndex;
    
    /**
     * Minimal salary
     */
    @Column(name = "minimal_salary")
    @NotNull(message = "{post.minSalary.notNull}")
    @Min(value=1, message = "{post.minSalary.min}")
    private int minSalary;
    
    /**
     * Maximal salary
     */
    @Column(name = "maximal_salary")
    @NotNull(message = "{post.maxSalary.notNull}")
    @Min(value=1, message = "{post.maxSalary.min}")
    private int maxSalary;
    
    /**
     * Type of the contract
     */
    @Column(name = "contract_type")
    @NotBlank(message = "{post.contractType.notBlank}")
    private String contractType;
    
    /**
     * Description of the post
     */
    @Column(name = "description")
    @NotBlank(message = "{post.description.notBlank}")
    @Size(min = 10, message = "{post.description.size}")
    private String description;
    
    /**
     * Importants points of the post
     */
    @Column(name = "important_notes")
    @NotBlank(message = "{post.importantNotes.notBlank}")
    private String importantNotes;
    
    
    // Position context

    /**
     * Localisation of the job
     */
    @Column(name = "workplace")
    @NotBlank(message = "{post.workplace.notBlank}")
    private String workplace;
    
    /**
     * Company of the post
     */
    @Column(name = "organization")
    @NotBlank(message = "{post.organization.notBlank}")
    private String organization;
    
    /**
     * Service of the post (info/...)
     */
    @Column(name = "work_unit")
    @NotBlank(message = "{post.workUnit.notBlank}")
    private String workUnit;
    
    /**
     * Skills recommanded for the post
     */
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Valid
    @Size(min=1, message = "{post.postSkill.size}")
    private List<PostSkill> postSkill;
     
    public Post() {
    }

    public Post(String reference, String title,String experience, String salaryIndex, int minSalary, int maxSalary, String contractType, String workplace, String organization, String workUnit) {
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
        this.postSkill = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public List<PostSkill> getPostskill() {
        return postSkill;
    }

    public void setPostskill(List<PostSkill> postskill) {
        this.postSkill = postskill;
    }
    
    public void addPostskill(PostSkill postskill) {
        this.postSkill.add(postskill);
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
        return "Post{" + "id=" + id + ", publicationDate=" + publicationDate + ", reference=" + reference + ", title=" + title + ", experience=" + experience + ", salaryIndex=" + salaryIndex + ", minSalary=" + minSalary + ", maxSalary=" + maxSalary + ", contractType=" + contractType + ", description=" + description + ", importantNotes=" + importantNotes + ", workplace=" + workplace + ", organization=" + organization + ", workUnit=" + workUnit + ", postskill=" + postSkill + '}';
    }


   
}
