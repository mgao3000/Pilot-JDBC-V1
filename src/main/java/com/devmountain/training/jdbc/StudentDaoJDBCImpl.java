package com.devmountain.training.jdbc;

import com.devmountain.training.dao.StudentDao;
import com.devmountain.training.model.StudentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoJDBCImpl implements StudentDao {
    private Logger logger = LoggerFactory.getLogger(StudentDaoJDBCImpl.class);

    //STEP 1: Database information
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/pilot";
    private static final String USER = "admin";
    private static final String PASS = "password";

    @Override
    public StudentModel save(StudentModel student, Long majorId) {
        StudentModel savedStudent = null;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database for saving a studentModel...");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Insert statement...");
            String SQL_INSERT = "INSERT INTO STUDENT (login_name, password, first_name, last_name, email, address, enrolled_DATE, major_id) VALUES (?, ?, ?,?,?,?,?,?)";;
//            preparedStatement = dbConnection.prepareStatement(SQL_INSERT);
            preparedStatement = dbConnection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
//            preparedStatement.setLong(1, department.getId());
            preparedStatement.setString(1, student.getLoginName());
            preparedStatement.setString(2, student.getPassword());
            preparedStatement.setString(3, student.getFirstName());
            preparedStatement.setString(4, student.getLastName());
            preparedStatement.setString(5, student.getEmail());
            preparedStatement.setString(6, student.getAddress());
            preparedStatement.setTimestamp(7, student.getEnrolledDateInTimestamp());
            preparedStatement.setLong(8, majorId);

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                savedStudent = student;

            rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) {
                Long generatedId = rs.getLong(1);
                if(generatedId != null)
                    savedStudent.setId(generatedId);
            }

        }
        catch(SQLException e){
            logger.error("SQLException is caught when trying to save a student. The input student =" + student + ", the error = " + e.getMessage());
        }
        finally {
            //STEP 4: finally block used to close resources
            try {
                if(rs != null) rs.close();
                if(preparedStatement != null) preparedStatement.close();
                if(dbConnection != null) dbConnection.close();
            }
            catch(SQLException se) {
                logger.error("SQLException is caught when trying to close preparedStatement or dbConnection, error = " + se.getMessage());
            }
        }
        return savedStudent;
    }

    @Override
    public StudentModel update(StudentModel student) {
        StudentModel updatedStudent = null;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database for updating a studentModel");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Updating statement...");

            String SQL_UPDATE = "UPDATE STUDENT SET login_name=?, password=?, email=?, address=? where id=?";
            preparedStatement = dbConnection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, student.getLoginName());
            preparedStatement.setString(2, student.getPassword());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getAddress());
            preparedStatement.setLong(5, student.getId());

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                updatedStudent = student;

        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to update a Student. The input student =" + student + ", the error = " + e.getMessage());
        }
        finally {
            //STEP 4: finally block used to close resources
            try {
                if(preparedStatement != null) preparedStatement.close();
                if(dbConnection != null) dbConnection.close();
            }
            catch(SQLException se) {
                logger.error("SQLException is caught when trying to close preparedStatement or dbConnection, error = " + se.getMessage());
            }
        }
        return updatedStudent;
    }

    @Override
    public boolean deleteByLoginName(String loginName) {
        boolean isStudentDeleted = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database...");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Deleting a studentModel by loginName statement...");

            String SQL_DELETE = "DELETE FROM STUDENT where LOGIN_NAME=?";
            preparedStatement = dbConnection.prepareStatement(SQL_DELETE);
            preparedStatement.setString(1, loginName);

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                isStudentDeleted = true;

        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to delete a Student by LoginName. The input loginName =" + loginName + ", the error = " + e.getMessage());
        }
        finally {
            //STEP 4: finally block used to close resources
            try {
                if(preparedStatement != null) preparedStatement.close();
                if(dbConnection != null) dbConnection.close();
            }
            catch(SQLException se) {
                logger.error("SQLException is caught when trying to close preparedStatement or dbConnection, error = " + se.getMessage());
            }
        }
        return isStudentDeleted;
    }

    @Override
    public boolean deleteById(Long studentId) {
        boolean isStudentDeleted = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database...");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Deleting a projectModel by projectId statement...");

            String SQL_DELETE = "DELETE FROM STUDENT where id=?";
            preparedStatement = dbConnection.prepareStatement(SQL_DELETE);
            preparedStatement.setLong(1, studentId);

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                isStudentDeleted = true;

        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to delete a Student by studentId. The input studentId =" + studentId + ", the error = " + e.getMessage());
        }
        finally {
            //STEP 4: finally block used to close resources
            try {
                if(preparedStatement != null) preparedStatement.close();
                if(dbConnection != null) dbConnection.close();
            }
            catch(SQLException se) {
                logger.error("SQLException is caught when trying to close preparedStatement or dbConnection, error = " + se.getMessage());
            }
        }
        return isStudentDeleted;
    }

    @Override
    public boolean delete(StudentModel student) {
        Long studentId = student.getId();
        return deleteById(studentId);
    }

    @Override
    public List<StudentModel> getStudents() {
        List<StudentModel> students = new ArrayList<StudentModel>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM STUDENT";
            rs = stmt.executeQuery(sql);

            //STEP 4: Extract data from result set
            while(rs.next()) {
                //Retrieve by column name
                Long id  = rs.getLong("id");
                String loginName = rs.getString("login_name");
                String password = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                Timestamp enrolledDate = rs.getTimestamp("enrolled_date");
                Long majorId  = rs.getLong("major_id");

                //Fill the object
                StudentModel student = new StudentModel();
                student.setId(id);
                student.setLoginName(loginName);
                student.setPassword(password);
                student.setFirstName(firstName);
                student.setLastName(lastName);
                student.setEmail(email);
                student.setAddress(address);
                student.setEnrolledDate(enrolledDate);
                student.setMajorId(majorId);
                students.add(student);
            }
        }
        catch(SQLException e){
            logger.error("SQLException is caught when trying to select all Students. the error = " + e.getMessage());
        }
        finally {
            //STEP 5: finally block used to close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                logger.error("SQLException is caught when trying to close preparedStatement or dbConnection, error = " + se.getMessage());
            }
        }
        return students;
    }

    @Override
    public StudentModel getStudentById(Long id) {
        StudentModel retrievedStudent = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Creating statement...");
            String sql;
            sql = "SELECT * FROM STUDENT where id = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            rs = preparedStatement.executeQuery();

            //STEP 4: Extract data from result set
            if(rs.next()) {
                //Retrieve by column name
                Long studentId  = rs.getLong("id");
                String loginName = rs.getString("login_name");
                String password = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                Timestamp enrolledDate = rs.getTimestamp("enrolled_date");
                Long majorId  = rs.getLong("major_id");

                //Fill the object
                retrievedStudent = new StudentModel();
                retrievedStudent.setId(studentId);
                retrievedStudent.setLoginName(loginName);
                retrievedStudent.setPassword(password);
                retrievedStudent.setFirstName(firstName);
                retrievedStudent.setLastName(lastName);
                retrievedStudent.setEmail(email);
                retrievedStudent.setAddress(address);
                retrievedStudent.setEnrolledDate(enrolledDate);
                retrievedStudent.setMajorId(majorId);
            }
        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to select a student by studentId. The input studentId =" + id + ", the error = " + e.getMessage());
        }
        finally {
            //STEP 5: finally block used to close resources
            try {
                if(rs != null) rs.close();
                if(preparedStatement != null) preparedStatement.close();
                if(conn != null) conn.close();
            }
            catch(SQLException se) {
                logger.error("SQLException is caught when trying to close preparedStatement or dbConnection, error = " + se.getMessage());
            }
        }

        return retrievedStudent;
    }

    @Override
    public StudentModel getStudentByLoginName(String loginName) {
        StudentModel retrievedStudent = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Creating statement...");
            String sql;
            sql = "SELECT * FROM STUDENT where login_name = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, loginName);
            rs = preparedStatement.executeQuery();

            //STEP 4: Extract data from result set
            if(rs.next()) {
                //Retrieve by column name
                Long studentId  = rs.getLong("id");
                String retrievedLoginName = rs.getString("login_name");
                String password = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                Timestamp enrolledDate = rs.getTimestamp("enrolled_date");
                Long majorId  = rs.getLong("major_id");

                //Fill the object
                retrievedStudent = new StudentModel();
                retrievedStudent.setId(studentId);
                retrievedStudent.setLoginName(retrievedLoginName);
                retrievedStudent.setPassword(password);
                retrievedStudent.setFirstName(firstName);
                retrievedStudent.setLastName(lastName);
                retrievedStudent.setEmail(email);
                retrievedStudent.setAddress(address);
                retrievedStudent.setEnrolledDate(enrolledDate);
                retrievedStudent.setMajorId(majorId);
            }
        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to select a student by loginName. The input loginName =" + loginName + ", the error = " + e.getMessage());
        }
        finally {
            //STEP 5: finally block used to close resources
            try {
                if(rs != null) rs.close();
                if(preparedStatement != null) preparedStatement.close();
                if(conn != null) conn.close();
            }
            catch(SQLException se) {
                logger.error("SQLException is caught when trying to close preparedStatement or dbConnection, error = " + se.getMessage());
            }
        }

        return retrievedStudent;
    }
}
