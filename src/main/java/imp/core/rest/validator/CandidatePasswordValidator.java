/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.validator;

import imp.core.entity.user.Candidate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author alexis
 */
public class CandidatePasswordValidator implements ConstraintValidator<CandidatePassword, Candidate> {

    private int minSize;
    
    private boolean required;
        
    @Override
    public void initialize(CandidatePassword constraintAnnotation) {
        minSize = constraintAnnotation.minSize();
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Candidate value, ConstraintValidatorContext context) {
        return UserPasswordValidator.isValid(value.getUser(), context, minSize, required);
    }
    
    
}
