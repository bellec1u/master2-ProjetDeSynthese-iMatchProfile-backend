/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.validator;

import imp.core.bean.SkillRepository;
import imp.core.entity.Skill;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author alexis
 */
public class SkillExistsValidator implements ConstraintValidator<SkillExists, Skill> {
 
    // Injected with JNDI Lookup cause @EJB annotation can fail
    private SkillRepository skillRepository;
    
    @Override
    public void initialize(SkillExists constraintAnnotation) {
        try {
            Context context = new InitialContext();
            skillRepository = (SkillRepository)context.lookup("java:global/IMP-core/SkillRepository");
        } catch (NamingException ex) {
            Logger.getLogger(SkillExistsValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean isValid(Skill value, ConstraintValidatorContext context) {
        return value!=null && skillRepository.getById(value.getId()) != null;
    }
    
}