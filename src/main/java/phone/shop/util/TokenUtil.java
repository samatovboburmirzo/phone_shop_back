package phone.shop.util;

import phone.shop.dto.profile.UserDetails;
import phone.shop.types.ProfileRole;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {
    public static UserDetails getCurrentUser(HttpServletRequest request) throws RuntimeException {
        UserDetails currentUser = (UserDetails) request.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("METHOD NOT ALLOWED");
        }
        return currentUser;
    }

    public static UserDetails getCurrentUser(HttpServletRequest request, ProfileRole requiredRole) throws RuntimeException {
        UserDetails currentUser = (UserDetails) request.getAttribute("currentUser");
        if (currentUser == null || !currentUser.getRole().equals(requiredRole)) {
            throw new RuntimeException("METHOD NOT ALLOWED");
        }
        return currentUser;
    }
}
