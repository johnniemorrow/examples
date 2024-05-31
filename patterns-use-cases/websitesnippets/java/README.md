# Java website snippets

These are code snippets for the website in Java.


To run them:
```shell
./gradlew run
```

## Requests:

a. Workflows as code
```shell
curl localhost:8080/RoleUpdateService/applyRoleUpdate  \
    -H 'content-type: application/json' \
    -d '{"role": "admin", "userId": "joe", "permissions": ["read", "write"]}'
```