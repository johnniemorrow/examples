const restate = require("@restatedev/restate-sdk");

// *** BEGIN SNIPPET ***

const svc = restate.service({
  name: "roleUpdate",
  handlers: {
    applyRoleUpdate: async (ctx, update) => {
      const { userId, role, permissions } = update;
        
      const success = await ctx.run("apply new role", () =>
        applyUserRole(userId, role)
      );

      if (!success) {
        return;
      }
        
      for (const permission of permissions) {
        await ctx.run("apply permission", () =>
          applyPermission(userId, permission)
        );
      }
    }
  }
});

// *** END SNIPPET ***

// ----------------------- Stubs to please the compiler -----------------------

function applyUserRole(userId, userRole) {}
  
function applyPermission(userId, permission) {}