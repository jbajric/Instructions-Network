# Instructions Network - Advanced Web Technologies 2020/2021

**Development team**: 
1. [Jasmin Bajrić](https://github.com/jbajric)
2. [Elma Bejtović](https://github.com/nejiram)
3. [Nejira Musić](https://github.com/ebejtovic1)

## Description
Instructions Network creates a platform to connect users, in need to get guided assistance about several topics/courses, with instructors that provide lectures about the topics that the users need. 

## Demo:
[Video URL](https://drive.google.com/file/d/1ID0OaJFmuFwkY8d97JqmGyOaF0jXCues/view?usp=sharing)

**[Server Configuration GitHub Repository](https://github.com/jbajric/serverconfig)**

# Starting the project

## First step
In your IDE, open the project and within the root directory for instructionnetwork execute:

```
maven clean
maven install
```

## Second step
Launch the following terminal commands:

```
docker compose build
docker compose up
```

# Created dummy users

**Instructors**

```
username: iprazina      password: password
username: ecogo         password: password
```

**Students**

```
username: nmusic        password: password
username: jbajric       password: password
username: ebejtovic     password: password
```

## Functionalities: 

1. Sign Up 
2. Login 
3. Preview of profile 
4. Update of profile data
5. Delete profile
6. Preview of schedule / free time
7. Adding appointment to schedule
8. Removing appointment from schedule
9. Preview of available appointments
10. Filter available appointments through filters
11. Reservation of appointment
12. Review of instructors
13. Preview of reservations
14. Cancel reservation
15. Creating an appointment
16. Preview of created appointments
17. Delete of created appointments
18. Logout
