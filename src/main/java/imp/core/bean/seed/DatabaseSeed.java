/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean.seed;

import com.github.javafaker.Faker;
import imp.core.bean.PostRepository;
import imp.core.entity.user.Education;
import imp.core.entity.post.Post;
import imp.core.entity.post.PostSkill;
import imp.core.entity.user.Candidate;
import imp.core.entity.Skill;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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

    @EJB
    private PostRepository postRepository;
    
    private List<Skill> skills;
    private List<PostSkill> postskills;
    private List<String> emails;

    @PostConstruct
    public void seed() {
        Faker faker = new Faker();
        this.emails = new ArrayList<>();
        // ---------- ---------- ---------- ---------- Skills
        this.skills = new ArrayList<>();
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "Java"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "Angular"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "J2EE"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "BDD"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "Marketing"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "Python"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "Scala"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "PHP"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "TLA+"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "9gag"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "Suite office"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "Git"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "JavaScript"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "C"));
        this.skills.add(new Skill(Skill.Typeskill.TECHNIQUES, "C++"));

        for (Skill s : skills) {
            em.persist(s);
        }
        // force skills persistence to DB in order to get their id
        em.flush();
        
        this.postskills = new ArrayList<>();
        for (Skill s : skills) {
            if (faker.number().numberBetween(0, 100) < 50) {
                this.postskills.add(
                        new PostSkill(s, PostSkill.Type.PLUS)
                );
            } else {
                this.postskills.add(
                        new PostSkill(s, PostSkill.Type.OBLIGATOIRE)
                );
            }
        }

        // ---------- ---------- ---------- ---------- Candidates
        User usertest = new User();
        usertest.setEmail("john.doe@gmail.com");
        usertest.setFirstname("John");
        usertest.setLastname("Doe");
        usertest.setPassword("candidate");
        usertest.setRole(User.Role.CANDIDATE);

        Candidate candidatetest = new Candidate();
        candidatetest.setUser(usertest);

        em.persist(candidatetest);

        for (int i = 0; i < 10; i++) {
            User user = new User();
            String email = "";
            do {
                email = faker.internet().emailAddress();
            } while (emails.contains(email));
            emails.add(email);
            user.setEmail(email);
            user.setFirstname(faker.name().firstName());
            user.setLastname(faker.name().lastName());
            user.setPassword(faker.internet().password());
            user.setRole(User.Role.CANDIDATE);

            Candidate candidate = new Candidate();
            candidate.setUser(user);
            candidate.setBirthDate(LocalDate.of(faker.number().numberBetween(1940, 2000), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 25)));
            candidate.setDescription(faker.lorem().sentence(faker.number().numberBetween(5, 30)));
            for (int j = 0; j < skills.size(); j++) {
                if (faker.number().numberBetween(0, 100) < 50) {
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
        User usertest2 = new User();
        usertest2.setEmail("mr.recruiter@gmail.com");
        usertest2.setFirstname("Mr");
        usertest2.setLastname("Recruiter");
        usertest2.setPassword("recruiter");
        usertest2.setRole(User.Role.RECRUITER);

        Recruiter recruitertest = new Recruiter(usertest2, "Recruiter&Co");

        em.persist(recruitertest);

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

                for (int k = 0; k < postskills.size(); k++) {
                    if (faker.number().numberBetween(0, 100) < 10) {
                        post.addPostskill(postskills.get(k));
                    }
                    if (post.getPostskill().isEmpty()) {
                        post.addPostskill(postskills.get(0));
                    }
                }

                recruiter.addPost(post);
            }
            em.persist(recruiter);
        }

        postRepository.checkMatching();
    }
}
