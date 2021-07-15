https://billykorando.com/2019/12/30/building-a-custom-spring-boot-starter/

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

10 - jason web token
  look at step_10_jwt.png to see why it is useful to use jwt when we have many applications who try to connect in our,
  in that case we can not use basic authentication or form authentication

  jwt is stateless that mean you don't have to store it in db
  you don't need to put it in session
  it account be use across many services (so recommended when we have many services that access our application)
  
  becareful of what is under
  
  if secret key is compromise we are in trouble
  we don't know when the user is login and when is logout
  token can be stolen

a user each time is log himself we give him a token even if the previous one has not expired
what we must do is to keep somewhere in db a relation between token and user, if is ask a new one by re login himself
we must invalidate the prévious one by exprire it or something else to avoid to have token valid who can be stolen