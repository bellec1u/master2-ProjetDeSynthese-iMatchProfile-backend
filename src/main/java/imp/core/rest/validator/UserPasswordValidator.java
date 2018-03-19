/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.validator;

import imp.core.entity.user.User;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author alexis
 */
public class UserPasswordValidator {

    public static boolean isValid(User value, ConstraintValidatorContext context, int minSize, boolean required) {
        // valide si mise à jour et password null ou valide
        // OU si pas mise à jour et password valide,
        return (required && value.getPassword() != null && value.getPassword().length() >= minSize)
                || (!required && (value.getPassword() == null || value.getPassword().length() >= minSize));
    }
}
