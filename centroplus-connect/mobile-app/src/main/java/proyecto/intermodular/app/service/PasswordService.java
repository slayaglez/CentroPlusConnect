package proyecto.intermodular.app.service;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordService {
    private final int cost = 12; // mayor el numero, mayor la seguridad, mayor el tiempo

    public String hash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(cost));
    }

    public boolean verify(String plain, String storedHash) {
        if (storedHash == null || storedHash.isEmpty()) return false;
        return BCrypt.checkpw(plain, storedHash);
    }
}

