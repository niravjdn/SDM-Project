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

## Team Leader For All Iterations
 Iteration 1 - Nirav Patel
 Iteration 2 - Nirav Patel
 Iteration 3 - Nirav Patel
 Iteration 4 - Nirav Patel

 
 ## Trello Public URL For Task Division
 https://trello.com/b/gLlDT1V7/sdm-project

## How to set up project

- Install Spring Tool SUite 4 (https://spring.io/tools)
- Install XAMPP and set up DB (https://www.apachefriends.org/download.html)
- Install lombok - Follow Step 2,3 and 4 (https://howtodoinjava.com/automation/lombok-eclipse-installation-examples/)

- **Import** is a maven project in IDE
- Run as spring application
- Stop Application
- Run trigger.sql (try to execute trigger one by one) or import it in database
- If you want dummy data, run 'Only Data.sql' also.

 ## Credentials for login (Clerk and Admin)

 | Username | Password ID | 
 | --- | --- | 
 | user@niravjdn.xyz  | user |
 | user2@niravjdn.xyz  | user |
 |admin@niravjdn.xyz | admin|

 ## Database Set up (Not Required for Iteration 1 and 2)
 - DB Name : zadmin_sdm
 - DB Username : root
 - DB Password : (No Password)


 ### How to set up DB 

Set up these properties with proper credentials in application.properties.

 ```
spring.datasource.url = jdbc:mysql://127.0.0.1:3306/zadmin_sdm?useSSL=true&characterEncoding=utf8&sessionVariables=storage_engine=InnoDB
spring.datasource.username = root
```

Also change in DBConfig Configuration class.

**don't push change in application.properties to github unless it is really necessary.**

## Optimistic Concurrency - Demo in Video


[![Alt text](https://img.youtube.com/vi/pNt12cmOwUo/0.jpg)](https://www.youtube.com/watch?v=pNt12cmOwUo)
