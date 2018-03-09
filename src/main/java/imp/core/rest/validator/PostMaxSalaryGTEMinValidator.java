/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.validator;

import imp.core.entity.post.Post;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author alexis
 */
public class PostMaxSalaryGTEMinValidator implements ConstraintValidator<PostMaxSalaryGTEMin, Post> {

    @Override
    public void initialize(PostMaxSalaryGTEMin constraintAnnotation) {
    }

    @Override
    public boolean isValid(Post value, ConstraintValidatorContext context) {
        return value.getMaxSalary() >= value.getMinSalary();
    }
    
}
