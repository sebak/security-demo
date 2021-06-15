# spring-boot security learning

- Basic Auth:

username and password must be sent in base64 in the header of the request to server, the issue with
basic auth is that the user or client for each request to the server must sent username and password. it can only be good to do that when you access external API