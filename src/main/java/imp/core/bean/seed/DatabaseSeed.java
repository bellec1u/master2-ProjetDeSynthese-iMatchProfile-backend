/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean.seed;

import com.github.javafaker.Faker;
import imp.core.entity.user.Education;
import imp.core.entity.post.Post;
import imp.core.entity.post.PostSkill;
import imp.core.entity.user.Candidate;
import imp.core.entity.Skill;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Singleton to feed the database with some datas.
 *
 * @author alexis
 */
@Singleton
@Startup
public class DatabaseSeed {

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    private List<Skill> skills;

    @PostConstruct
    public void seed() {
        Faker faker = new Faker();

        // ---------- ---------- ---------- ---------- Skills
        this.skills = new ArrayList<>();
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "Java"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "Angular"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "J2EE"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "BDD"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "Marketing"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "Python"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "Scala"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "PHP"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "TLA+"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "9gag"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "Suite office"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "Git"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "JavaScript"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "C"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNQUES, "C++"));

        for (Skill s : skills) {
            em.persist(s);
        }

        // ---------- ---------- ---------- ---------- Candidates
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setEmail(faker.internet().emailAddress());
            user.setFirstname(faker.name().firstName());
            user.setLastname(faker.name().lastName());
            user.setPassword(faker.internet().password());
            user.setRole(User.Role.CANDIDATE);

            Candidate candidate = new Candidate();
            candidate.setUser(user);
            candidate.setBirthDate(LocalDate.of(faker.number().numberBetween(1940, 2000), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 25)));
            candidate.setDescription(faker.lorem().sentence(faker.number().numberBetween(5, 30)));
            for (int j = 0; j < skills.size(); j++) {
                if (faker.number().numberBetween(0, 100) < 10) {
                    candidate.addSkill(skills.get(j));
                }
            }

            for (int j = 0; j < faker.number().numberBetween(0, 10); j++) {
                Education education = new Education(faker.educator().course(), Integer.toString(faker.number().numberBetween(1940, 2017)), faker.lorem().sentence(faker.number().numberBetween(2, 10)));
                candidate.addEducation(education);
            }

            em.persist(candidate);
        }

        // ---------- ---------- ---------- ---------- Recruiters
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setEmail(faker.internet().emailAddress());
            user.setFirstname(faker.name().firstName());
            user.setLastname(faker.name().lastName());
            user.setPassword(faker.internet().password());
            user.setRole(User.Role.RECRUITER);

            Recruiter recruiter = new Recruiter(user, faker.company().industry());

            for (int j = 0; j < faker.number().numberBetween(0, 10); j++) {
                Post post = new Post(
                        faker.code().isbn10(),
                        faker.company().profession(),
                        faker.lorem().sentence(faker.number().numberBetween(2, 10)),
                        "PM7",
                        faker.number().numberBetween(10000, 50000),
                        faker.number().numberBetween(50001, 100000),
                        "CDI",
                        faker.address().cityName(),
                        faker.company().industry(),
                        faker.commerce().department()
                );
                post.setDescription(faker.lorem().sentence(faker.number().numberBetween(10, 30)));
                post.setImportantNotes(faker.lorem().sentence(faker.number().numberBetween(3, 10)));

                for (int k = 0; k < skills.size(); k++) {
                    if (faker.number().numberBetween(0, 100) < 10) {
                        if (faker.number().numberBetween(0, 100) < 50) {
                            PostSkill postSkill = new PostSkill(skills.get(k), PostSkill.Type.PLUS);
                            post.addPostskill( postSkill );
                        } else {
                            PostSkill postSkill = new PostSkill(skills.get(k), PostSkill.Type.OBLIGATOIRE);
                            post.addPostskill( postSkill );
                        }
                    }
                    if (post.getPostskill().size() == 0) {
                            PostSkill postSkill = new PostSkill(skills.get(0), PostSkill.Type.OBLIGATOIRE);
                            post.addPostskill( postSkill );
                    }
                }
                
                recruiter.addPost( post );
            }
            
            em.persist( recruiter );
        }
    }
}
