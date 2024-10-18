## Setup & Usage (Docker)

Follow the steps below to setup & run semantic-hub:

1. Run docker compose to launch all services (semantic hub, keycloak, postgres). 

    > $ docker compose -f docker-compose-tvs.yml up

2. The semantic hub will be launched on port [4242](http://localhost:4242) and keycloak will be launched in port [8080](http://localhost:8080)

3. We modified there `default-realm-import.json` config into this `tvs-demo-realm-import.json`

4. You can checkout the configurations in the [keycloak](http://localhost:8080). Use `keycloak` as username and password to login.

5. You can also try to go through the Swagger documentation and try exploring the APIs (which won't work) of [Semantic Hub](http://localhost:4242)

6. Now open postman and import the collection in the project repository named **Semantic Hub.postman_collection.json**

7. For the sake of simplicity, I have used hard coded authentication process in the Authentication tab of the `Get Models` request.

8. Click on the `Get access token` button to get and set the access token for authorization. 

9. Now perform the request to get the models. (As expected it won't work :p). Which will throw 403 permission denied error.

10. You are ready to explore further more by making changes by yourself. Just make sure to use the following command after making modifications in the codebase:

    > docker compose  -f docker-compose-tvs.yml up --build