# Pilot JDBC Sample V1
The purpose of this sample project is to show the basic usage of Java plain 
JDBC API. Three dummy tables (MAJOR, STUDENT, PROJECT) are created tp 
display the basic data relationship. The relationship between MAJOR and 
STUDENT is ONE to MANY and the relationship between STUDENT and PROJECT 
is MANY to MANY. 

Here are the DDL used to define the three tables: 
```SQL DDL
DROP TABLE IF EXISTS major CASCADE;
DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS project CASCADE;

CREATE TABLE major (
    /*id                INTEGER NOT NULL default nextval('major_id_seq'), */
    id                BIGSERIAL NOT NULL,
    name              VARCHAR(30) not null unique,
    description       VARCHAR(150)
);

ALTER TABLE major ADD CONSTRAINT major_pk PRIMARY KEY ( id );
ALTER TABLE MAJOR ADD CONSTRAINT UQ_major_name UNIQUE(name);

CREATE TABLE student (
    /*id              INTEGER NOT NULL default nextval('student_id_seq'),*/
    id              BIGSERIAL NOT NULL,
    login_name            VARCHAR(30) not null unique,
    password        VARCHAR(64),
    first_name      VARCHAR(30),
    last_name       VARCHAR(30),
    email           VARCHAR(50),
    address         VARCHAR(150),
    enrolled_date      date default CURRENT_DATE,
    major_id   bigint NOT NULL
);

ALTER TABLE student ADD CONSTRAINT student_pk PRIMARY KEY ( id );
ALTER TABLE STUDENT ADD CONSTRAINT UQ_login_name UNIQUE(login_name);
ALTER TABLE STUDENT ADD CONSTRAINT UQ_email UNIQUE(email);

CREATE TABLE project (
    /*id             INTEGER NOT NULL default nextval('project_id_seq'),*/
    id             BIGSERIAL NOT NULL,
    name   VARCHAR(30),
    description       VARCHAR(150),
    create_date    date default CURRENT_DATE
);

ALTER TABLE project ADD CONSTRAINT project_pk PRIMARY KEY ( id );
ALTER TABLE PROJECT ADD CONSTRAINT UQ_project_name UNIQUE(name);

CREATE TABLE student_project (
    student_id    BIGINT NOT NULL,
    project_id    BIGINT NOT NULL
);

ALTER TABLE student
    ADD CONSTRAINT student_major_fk FOREIGN KEY ( major_id )
        REFERENCES major ( id );

ALTER TABLE student_project
    ADD CONSTRAINT student_fk FOREIGN KEY ( student_id )
        REFERENCES student ( id );

ALTER TABLE student_project
    ADD CONSTRAINT project_fk FOREIGN KEY ( project_id )
        REFERENCES project ( id );
```
## DAO pattern 
DAO pattern is used to separate the actual implementation of data 
accessing API to DB tables from the high level business services. for 
those who are not familiar with DAO pattern, please refer to 
[Data Access Object Pattern](https://www.tutorialspoint.com/design_pattern/data_access_object_pattern.htm)

## About the JDBC implementation in this version -- V1
In this Version, the basic CRUD operations are implemented for the three 
tables (MAJOR, STUDENT and PROJECT). The three tables are treated as 
independent tables and the relationships (one-to-many, many-to-many) are not
considered. In the next version, we will refactor and optimize the code to 
implement those relationships. 

## About Unit Test code
Unit Tests are written using JUnit4 to make sure the JDBC implementation 
is correct. For those who are not familiar with JUnit, please refer 
[Most Common JUnit 4 Annotations](https://www.swtestacademy.com/junit4/)
You only need to understand the usage of the following JUnit annotations now: 
### @Test, @Before, @After, @BeforeClass and @AfterClass

