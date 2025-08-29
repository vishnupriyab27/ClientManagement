package com.assignment.dto;

import java.util.List;

public class AppUserWrapper {
    public List<AppUser> users;

    public List<AppUser> getAppUsers() {
        return users;
    }

    public void setAppUsers(List<AppUser> users) {
        this.users = users;
    }
}
