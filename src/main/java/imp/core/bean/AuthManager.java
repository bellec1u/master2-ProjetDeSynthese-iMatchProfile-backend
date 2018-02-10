/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import imp.core.entity.user.AccessData;
import imp.core.entity.user.Candidate;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author auktis
 */
@Stateless
public class AuthManager {

    @EJB
    private UserRepository userRepository;

    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    private final static String key = "secretKey";

    public AccessData login(String email, String password) {
        List<User> list = userRepository.findByEmail(email);
        if (list.isEmpty()) {
            return null;
        }

        User u = list.get(0);
        if (!u.getPassword().equals(password)) {
            return null;
        }

        if (u.getRole().equals(User.Role.RECRUITER)) {

        }
        Long id = -1L;
        switch (u.getRole()) {
            case RECRUITER:
                id = em
                        .createNamedQuery("Recruiter.findByUserId", Recruiter.class)
                        .setParameter("id", u.getId())
                        .getSingleResult()
                        .getId();
                break;
            case CANDIDATE:
                id = em
                        .createNamedQuery("Candidate.findByUserId", Candidate.class)
                        .setParameter("id", u.getId())
                        .getSingleResult()
                        .getId();
                break;
        }

        String jwtToken = Jwts.builder()
                .setSubject(u.getEmail())
                .claim("id", id + "")
                .claim("role", u.getRole())
                .setExpiration(toDate(LocalDateTime.now().plusHours(2L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        return new AccessData(jwtToken);
    }

    public void validateToken(String token) throws SignatureException {
        Jwts.parser().setSigningKey(key).parseClaimsJws(token);
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
