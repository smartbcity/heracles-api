
## Execute integration test
 * Edit /etc/host

```
127.0.0.1	ca.bc-coop.bclan
127.0.0.1	peer0.bc-coop.bclan
127.0.0.1	orderer.bclan
```

 * Start test network based on bclan and with ssm and ex02 chaincode installed
```
docker-compose -f docker-compose-it.yaml up -d
```


## Build project

```
./gradlew build
```

## Run test

```
./gradlew clean test
```

## Configuration

In dev mode configuration can be change in coop-rest/src/main/resources/application.yml

## Run

```
./gradlew coop-rest:bootRun
```

## Swagger ui

```
http://localhost:9090/swagger-ui/index.html
```

## Request Rest API

```bash
curl -X GET "http://localhost:9090/v2?fcn=list&args=ssm" -H  "accept: application/json"
```

```bash
curl -X GET "http://localhost:9090/v2?fcn=list&args=ssm" -H  "accept: application/json"
```

```bash
curl -X GET "http://localhost:9090/v2?cmd=query&fcn=list&args=ssm" -H  "accept: application/json"
```

```bash
curl -X GET "http://localhost:9090/v2?cmd=query&fcn=admin&args=adrien" -H  "accept: application/json"
```

### Request with a Keycloak Bearer Token

Get token:
```bash
curl -X POST "https://$URL/auth/realms/$REALM/protocol/openid-connect/token" \ 
--header "ontent-Type: application/x-www-form-urlencoded" \
--data-urlencode "grant_type=client_credentials" \
--data-urlencode "client_id=$CLIENT_ID" \
--data-urlencode "client_secret=$CLIENT_SECRET"
```
The token is in the field `access_token` of the response

Use it in any request:
```bash
curl ... --header "Authorization: Bearer $TOKEN"
```

## Run docker

Example configuration to use ssm in BC1
```
cd infra
echo ca__ADMIN=${ca__ADMIN} >> .env
echo ca__PASSWD=${ca__PASSWD} >> .env

echo ca__ORG=CivisBlockchain >> .env

echo endorsers=peer0:CivisBlockchain,peer1:CivisBlockchain >> .env
echo channel=sandbox >> .env
echo ccid=ssm >> .env

echo config_file=../../../bc1/config.json >> .env
echo config_crypto=../../../bc1/crypto-config >> .env


docker-compose -f docker-compose.yaml up
```
