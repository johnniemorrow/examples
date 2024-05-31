package my.example.a_workflows_as_code.types;

import java.util.List;

public class Update {
    private String userId;
    private String role;
    private String[] permissions;

    public Update(String userId, String role, String[] permissions) {
        this.userId = userId;
        this.role = role;
        this.permissions = permissions;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String[] getPermissions() {
        return permissions;
    }
}
