/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean.authentication;

import imp.core.bean.UserRepository;
import imp.core.entity.user.AccessData;
import imp.core.entity.user.Candidate;
import imp.core.entity.user.Recruiter;
import imp.core.entity.user.User;
import imp.core.password.Passwords;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author auktis
 */
@Stateless
public class AuthManager {

    @EJB
    private UserRepository userRepository;
    
    @EJB
    private KeyManager keyManager;
    
    
    public AccessData login(String email, String password) {
        List<User> list = userRepository.findByEmail(email);
        if (list.isEmpty()) {
            return null;
        }

        User u = list.get(0);
        // checking if (Base64 hashed) database password is equal to sent password
        if (!u.getPassword().equals(Passwords.hash(password, u.getPasswordSalt()))) {
            return null;
        }
        
        Long id = -1L;
        switch (u.getRole()) {
            case RECRUITER:
                Recruiter recruiter = userRepository.getConcreteUser(Recruiter.class, u.getId());
                id = recruiter.getId();
                break;
            case CANDIDATE:
                Candidate candidate = userRepository.getConcreteUser(Candidate.class, u.getId());
                id = candidate.getId();
                break;
        }

        String jwtToken = Jwts.builder()
                .setSubject(u.getEmail())
                .claim("id", id + "")
                .claim("idUser", u.getId() + "")
                .claim("role", u.getRole())
                .setExpiration(toDate(LocalDateTime.now().plusHours(2L)))
                .signWith(SignatureAlgorithm.HS512, keyManager.getJwtKey())
                .compact();

        return new AccessData(jwtToken);
    }

    /**
     * Checks if the token is valid and not expired and returns the body of the
     * token.
     * 
     * The method should catch all the runtime exceptions thrown by parseClaimsJws()
     * so they are not catched by the EJB Container
     * @param token
     * @return Claims
     */
    public Claims validateToken(String token) {
        try {
            return Jwts.parser().setSigningKey(keyManager.getJwtKey())
                    .parseClaimsJws(token).getBody();
        } catch (RuntimeException e) {
            System.err.println("TOKEN Exception");
            return null;
        }/* catch (MalformedJwtException | SignatureException | ExpiredJwtException e) {
            System.err.println("TOKEN ERROR");
            return false;
        }*/
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
