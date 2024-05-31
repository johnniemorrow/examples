package my.example.a_workflows_as_code;

import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import my.example.a_workflows_as_code.types.Update;

import static my.example.a_workflows_as_code.utils.Stubs.applyPermission;
import static my.example.a_workflows_as_code.utils.Stubs.applyUserRole;
import static dev.restate.sdk.JsonSerdes.*;

// *** BEGIN SNIPPET ***

@Service
public class RoleUpdateService {

    @Handler
    public void applyRoleUpdate(Context ctx, Update update) {

        boolean success = ctx.run("apply new role", BOOLEAN,
            () -> applyUserRole(update.getUserId(), update.getRole()));

        if (!success) {
            return;
        }

        for (String permission : update.getPermissions()) {
            ctx.run("apply permission",
                () -> applyPermission(update.getUserId(), permission));
        }
    }
}

// *** END SNIPPET ***
