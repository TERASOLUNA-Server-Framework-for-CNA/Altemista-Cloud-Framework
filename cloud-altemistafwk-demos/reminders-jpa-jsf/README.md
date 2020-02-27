
# Reminders (implementation with JPA and JSF)

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

- Object-relational mapping (O/RM): Spring Data JPA
- JSF
- Performance
- Security
- SOAP Web Service Interface
- SOAP Web Service Publisher

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

This application has an user interface built using JSF that can be accessed via '/reminders-jpa-jsf-webapp/'.

### Consuming Web Services

Under '/reminders-jpa-jsf-webapp/soap' is located a SOAP api to consume the application.

In addition, in '/reminders-jpa-jsf-webapp/soap/RemindersWS?wsdl', is available the WSDL that allows you to check
the different methods the api has as well as import it in some software (i.e SoapUI) to test the endpoints.

### Checking performance traces

The Performance module is added to the webapp layer, measuring the time taken to perform each request to the application.

The configuration is the default given by the module so all performance traces will be written in console one a request is executed.

