package org.orion.common.rbac;

public class LoginResult {

    private UserLoginHistory loginHistory;
    private User user;
    private int status;

    public UserLoginHistory getLoginHistory() {
        return loginHistory;
    }

    public void setLoginHistory(UserLoginHistory loginHistory) {
        this.loginHistory = loginHistory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
