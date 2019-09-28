# SDM-Project

## This repository contains code of SDM Project for Fall 2019.


## Team Member Details

| Name | Student ID | Email ID |
| --- | --- | --- |
| Nirav Ashvinkumar Patel | 40081268 | niravjdn@gmail.com |
| Avinash Damodaran | 40078258 | avinashdamu323@gmail.com |
| Jemish Kishor Paghadar | 40080723 | jemish.paghadar27@gmail.com |
| Charles Jebalitherson Augustin Moses | 40084105  | charlesjebalitherson@gmail.com |
| Vikramjit Singh | 40075774 | vikramsandhu008@gmail.com |

## Team Leader Iteration 1
 Nirav Patel
 
 ## Trello Public URL For Task Division
 https://trello.com/b/gLlDT1V7/sdm-project

 ## Credentials for login (Clerk and Admin)

 | Username | Password ID | 
| --- | --- | --- |
 | user@niravjdn.xyz  | user |
 |admin@niravjdn.xyz | admin|

 ## Database Set up
 - DB Name : zadmin_sdm
 - DB Username : root
 - DB Password : (No Password)

 ### How to set up DB 

if you don't want to connect mysql db, create db with above configuration and When you run your application it will create table and row data automatically.

if you want to use embeded db, then  comment this lines in application.properties

 ```
spring.datasource.url = jdbc:mysql://127.0.0.1:3306/zadmin_sdm?useSSL=true&characterEncoding=utf8&sessionVariables=storage_engine=InnoDB
spring.datasource.username = root
```

and uncomment this string

```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

** don't push change in application.properties to github unless it is really necessary. **

