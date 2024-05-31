package my.example.a_workflows_as_code.types;

import java.util.List;

public class Update {
    private final String userId;
    private final String role;
    private final List<String> permissions;

    public Update(String userId, String role, List<String> permissions) {
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

    public List<String> getPermissions() {
        return permissions;
    }
}
