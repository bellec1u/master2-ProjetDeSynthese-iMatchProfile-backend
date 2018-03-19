/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.password;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jcajce.provider.digest.SHA3;

/**
 * Utility class for password hashing and salt generation.
 *
 * @author alexis
 */
public class Passwords {

    private static final Random RANDOM = new SecureRandom();

    /**
     * Generates a random salt.
     *
     * @return a byte array with a 64 byte length salt.
     */
    public static Byte[] getSalt64() {
        final byte[] salt = new byte[64];
        RANDOM.nextBytes(salt);
        return ArrayUtils.toObject(salt);
    }
    
    /**
     * Generates a new hashed password encoded in Base64
     *
     * @param password to be hashed
     * @param salt the randomly generated salt
     * @return a Base64 encoded string of the password
     */
    public static String hash(final String password, final Byte[] salt) {
        try {
            final byte[] passwordBytes = password.getBytes("UTF-8");
            final byte[] all = ArrayUtils.addAll(passwordBytes, ArrayUtils.toPrimitive(salt));
            SHA3.DigestSHA3 md = new SHA3.Digest512();
            md.update(all);
            return Base64.getEncoder().encodeToString(md.digest());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Passwords.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
