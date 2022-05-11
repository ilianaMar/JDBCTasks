# JDBCTasks

### JDBC: Project setup
- create project setup with Maven
- add dependency for PostgresSql JDBC Driver, which corresponds with Java version 11 
and PostgresSQL version 14.2
- create database connections with provided configuration db properties, which is 
automatically closed
- create single database connection before tests start and close it after last test is executed 

### JDBC: Mapping data to POJOs
- Lombok usage
- JDBC Result Sets

### JDBC:DAO'S
- Use Lombok builder annotation
- Use DAO pattern to build application layer