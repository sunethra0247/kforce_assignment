# kforce_assignment

Endpoints :

-> /api/customer/{id} - to get one customer details
-> /api/customer/all - to get all customers all transaction details along with reward points
-> /api/customer/rewardDetails ?months=(3,,4,5 etc) default value 3 - this is to get each customer reward details in any 3 consecutive months

Assumptions :
-> All transaction amounts whole numbers
-> considering three month window as today -90 days

Swagger :
http://localhost:9090/swagger-ui/