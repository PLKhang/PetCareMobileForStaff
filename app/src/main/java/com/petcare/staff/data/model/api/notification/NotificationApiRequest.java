package com.petcare.staff.data.model.api.notification;

public class NotificationApiRequest {
    private String user_id;
    private String branch_id;
    private String token;

    public NotificationApiRequest(String user_id, String branch_id, String token) {
        this.user_id = user_id;
        this.branch_id = branch_id;
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
