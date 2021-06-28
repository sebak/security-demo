# spring-boot security learning

- step 1 manage Basic Auth:

username and password must be sent in base64 in the header of the request to server, the issue with
basic auth is that the user or client for each request to the server must sent username and password. it can only be good to do that when you access external API

- step 2 create our own users

we must have 

1- a unique username

2- encrypted password

3- Roles (ROLE_NAME) to allow according to the role name to access or not a resources or endpoint

4- Authorities or Permissions

5 - Secure resources with annotations

6 - Understand CSRF

7 - Form Based Auth

8 - Remember Me feature

9 - Db Authenticate: we use a real db instead of in memory one

10- and more others thing