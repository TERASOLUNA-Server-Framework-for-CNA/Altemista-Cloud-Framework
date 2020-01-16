
# Reminders (implementation with MyBatis and Apache Tiles + JSP)

Reminders is the official demo application built on top Altemista Cloud Framework (ACF).

The application is a simple task manager application where you can:

- add new tasks
- edit tasks
- mark tasks as completed
- view completed tasks
- view current uncompleted tasks
- remove tasks from the system

There are different implementations of these application using different modules and features of the framework.
This one uses:

- Asynchronous and scheduled executions
- JDBC/SQL: MyBatis
- Apache Tiles + JSP
- Security
- RESTful services publisher
- Monitoring


## First Steps

You will require Apache Maven for install and execute this demo application.
Additionally, you need access to the ACF artifacts either locally or through a repository which contains them.

```
mvn clean install
``` 

### Default Users of the application

The application has implemented a login and shows different tasks according the user logged.
Following, the default combinations of user / password :

- Admin / 1234
- Mario / password
- app / 1234

### Accessing the UI

This application has an user interface built using Apache Tiles + JSP that can be accessed via '/reminders-mybatis-tiles-webapp'.

### Consuming API REST

Under the endpoint 'reminders-mybatis-tiles-webapp/app/api/reminders/' there is an API REST to consume all data shown in the presentation layer.

It is securized, so please remember to login using one of the credentials previously shown.

The detail of the API is:

- / GET : get all incomplete tasks
- /?completed=true GET : get all completed tasks
- / POST : create a new task. As parameters you should sent a TaskDTO.
- /{id} PUT : updates the task with the id selected. As parameters you should sent a TaskDTO.
- /{id}/done PUT : Marks this task as done.
- /{id} DELETE : Deletes this task as done.

### Monitoring

Monitoring module is added and configure for checking that the application is deployed and the database is available.
The Logging wrapper is configured so, all the monitoring checks will be shown as traces in console.

In addition, as the scheduled module is added too, an scheduled task is configured to execute every 30 seconds all indicators.
