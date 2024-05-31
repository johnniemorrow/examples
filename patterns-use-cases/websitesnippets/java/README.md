# Java website snippets

These are code snippets for the website in Java.


To run them:
```shell
./gradlew run
```

## Requests:

### a. Workflows as code - WORKS
```shell
curl localhost:8080/RoleUpdateService/applyRoleUpdate  \
    -H 'content-type: application/json' \
    -d '{"role": "admin", "userId": "joe", "permissions": ["read", "write"]}'
```

### b. Calls and webhooks - FIX
```shell
curl localhost:8080/PaymentService/processPayment  \
    -H 'content-type: application/json' \
    -d '{"amount": 100, "paymentMethodId": "joe", "delayedStatus": false}'
```

### c. Async tasks - WORKS
Run the TaskClient first.
And then:
```shell
curl localhost:8000/charge  \
    -H 'content-type: application/json' \
    -d '{"amount": 100, "paymentMethodId": "joe", "paymentId":"124"}'
```
This returns an invocation ID

Then use the invocation ID to do
```shell
curl localhost:8000/status  -d 'inv_17BVnjO2zyaq5A4FSShUJXlQxG7bVtoVEZ'
```

Gives back" `Payment processed`

