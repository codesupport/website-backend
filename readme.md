#Api Service Demo
Demo Api-Service with SpringBoot and Hibernate

##Build the App
(From api-service root folder)
###Unix

``./mvnw clean package``

###Windows

``./mvnw.cmd clean package``


##Run the App

(From app-service root dir)

``java -jar target/api-service-0.0.1-SNAPSHOT.jar``

##Hit the endpoints

###Get user by ID

``http://localhost:8082/api/v1/users/2``

###Get all users

``http://localhost:8082/api/v1/users``
