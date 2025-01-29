# LocationServiceMicroservice 
### Locations service responsible for autocompletion of address 
### JAVA spring boot  mircoservise responsible for autocompletion of address/places and also can be used to fetch address details 
### it uses ola location places apis to fetch places locations and its details such as latitudue and longutiude 
### it supports retry mechanims if ola apis fails
### this microservice can halde upto 10000 request as the same time at the acurray perncentage of 99% and response time of 1.4 sec
### internal it uses caffeine com.github.ben-manes.caffeine cache libary to cache results and enhance media search 
### below is swagger link for futher apis doc wait for 60 secs to start the server , as (
Render spins down a Free web service that goes 15 minutes without receiving inbound traffic. Render spins the service back up whenever it next receives a request to process)

https://locationservice.onrender.com/swagger-ui/index.html#



