## Link to db :

* http://localhost:5050

## Keycloak

Access admin console :

* _admini:admini_
* http://localhost:8080/admin/marc/console

```shell
TOKEN=$(curl --location 'http://localhost:8080/realms/marc/protocol/openid-connect/token' --header 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'username=rh1' --data-urlencode 'password=1234' --data-urlencode 'grant_type=password' --data-urlencode 'client_id=marcapp' --data-urlencode 'client_secret=marc-secret' --data-urlencode 'scope=openid' | jq -r '.access_token')
```

```shell
echo $TOKEN
```