package util;

import javafx.scene.control.Button;

public class UserTM {
    private String name;
    private String userName;
    private String role;
    private Button remove;

    public UserTM() {
    }

    @Override
    public String toString() {
        return "UserTM{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", remove=" + remove +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Button getRemove() {
        return remove;
    }

    public void setRemove(Button remove) {
        this.remove = remove;
    }

    public UserTM(String name, String userName, String role, Button remove) {
        this.name = name;
        this.userName = userName;
        this.role = role;
        this.remove = remove;
    }
}
