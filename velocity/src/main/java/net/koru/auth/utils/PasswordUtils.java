package net.koru.auth.utils;

import com.google.common.hash.Hashing;
import lombok.experimental.UtilityClass;
import net.koru.auth.account.Account;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class PasswordUtils {

    public String hash(String password, String salt) {
        String hashedPassword = Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();
        return Hashing.sha512().hashString(hashedPassword + salt, StandardCharsets.UTF_8).toString();
    }

    public boolean verify(Account account, String password, String salt) {
        return account.getPassword().equals(hash(password, salt));
    }

}