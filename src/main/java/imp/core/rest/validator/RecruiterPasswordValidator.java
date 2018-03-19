/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.validator;

import imp.core.entity.user.Recruiter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author alexis
 */
public class RecruiterPasswordValidator implements ConstraintValidator<RecruiterPassword, Recruiter> {

    private int minSize;
    
    private boolean required;
        
    @Override
    public void initialize(RecruiterPassword constraintAnnotation) {
        minSize = constraintAnnotation.minSize();
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Recruiter value, ConstraintValidatorContext context) {
        return UserPasswordValidator.isValid(value.getUser(), context, minSize, required);
    }
    
    
}
