package com.devmountain.training.jdbc;

import com.devmountain.training.dao.MajorDao;
import com.devmountain.training.model.MajorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MajorDaoJDBCImpl implements MajorDao {
    private Logger logger = LoggerFactory.getLogger(MajorDaoJDBCImpl.class);

    //STEP 1: Database information
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/pilot";
    private static final String USER = "admin";
    private static final String PASS = "password";

    @Override
    public MajorModel save(MajorModel major) {
        MajorModel savedMajor = null;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database for saving a majorModel...");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Insert statement...");
//            String SQL_INSERT = "INSERT INTO DEPARTMENT (ID, NAME, DESCRIPTION, LOCATION) VALUES (?,?,?,?)";;
            String SQL_INSERT = "INSERT INTO MAJOR (NAME, DESCRIPTION) VALUES (?,?)";;
//            preparedStatement = conn.prepareStatement(SQL_INSERT);
            preparedStatement = dbConnection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
//            preparedStatement.setLong(1, major.getId());
            preparedStatement.setString(1, major.getName());
            preparedStatement.setString(2, major.getDescription());

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                savedMajor = major;

            rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) {
                Long generatedId = rs.getLong(1);
                if(generatedId != null)
                    savedMajor.setId(generatedId);
            }

        }
        catch(SQLException e){
            logger.error("SQLException is caught when trying to save a Major. The input major =" + major + ", the error = " + e.getMessage());
        }
        finally {
            //STEP 4: finally block used to close resources
            try {
                if(rs != null) rs.close();
                if(preparedStatement != null) preparedStatement.close();
                if(dbConnection != null) dbConnection.close();
            }
            catch(SQLException se) {
                logger.error("SQLException is caught when trying to close resultSet, preparedStatement or dbConnection, error = " + se.getMessage());
            }
        }
        return savedMajor;
    }

    @Override
    public MajorModel update(MajorModel major) {
        MajorModel updatedMajor = null;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database for updating a majorModel");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Updating statement...");

            String SQL_UPDATE = "UPDATE MAJOR SET name=?, description=? where id=?";
            preparedStatement = dbConnection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, major.getName());
            preparedStatement.setString(2, major.getDescription());
            preparedStatement.setLong(3, major.getId());

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                updatedMajor = major;

        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to update a Major. The input major =" + major + ", the error = " + e.getMessage());
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
        return updatedMajor;
    }

    @Override
    public boolean deleteByName(String majorName) {
        boolean isMajorDeleted = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database...");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Deleting a majorModel by majorName statement...");

            String SQL_DELETE = "DELETE FROM MAJOR where NAME=?";
            preparedStatement = dbConnection.prepareStatement(SQL_DELETE);
            preparedStatement.setString(1, majorName);

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                isMajorDeleted = true;

        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to delete a Major by majorName. The input majorName =" + majorName + ", the error = " + e.getMessage());
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
        return isMajorDeleted;
    }

    @Override
    public boolean deleteById(Long majorId) {
        boolean isMajorDeleted = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database...");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Deleting a majorModel by majorId statement...");

            String SQL_DELETE = "DELETE FROM MAJOR where id=?";
            preparedStatement = dbConnection.prepareStatement(SQL_DELETE);
            preparedStatement.setLong(1, majorId);

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                isMajorDeleted = true;

        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to delete a Major by majorId. The input majorId =" + majorId + ", the error = " + e.getMessage());
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
        return isMajorDeleted;
    }

    @Override
    public boolean delete(MajorModel major) {
        Long majorId = major.getId();
        return deleteById(majorId);
    }

    @Override
    public List<MajorModel> getMajors() {
        List<MajorModel> majors = new ArrayList<MajorModel>();
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
            sql = "SELECT * FROM MAJOR";
            rs = stmt.executeQuery(sql);

            //STEP 4: Extract data from result set
            while(rs.next()) {
                //Retrieve by column name
                Long id  = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                //Fill the object
                MajorModel major = new MajorModel();
                major.setId(id);
                major.setName(name);
                major.setDescription(description);
                majors.add(major);
            }
        }
        catch(SQLException e){
            logger.error("SQLException is caught when trying to select all Majors. the error = " + e.getMessage());
        }
        finally {
            //STEP 4: finally block used to close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                logger.error("SQLException is caught when trying to close preparedStatement or dbConnection, error = " + se.getMessage());
            }
        }
        return majors;
    }

    @Override
    public MajorModel getMajorById(Long id) {
            MajorModel retrievedMajor = null;
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
                sql = "SELECT * FROM MAJOR where id = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                rs = preparedStatement.executeQuery();

                //STEP 4: Extract data from result set
                if(rs.next()) {
                    //Retrieve by column name
                    Long deptId  = rs.getLong("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");

                    //Fill the object
                    retrievedMajor = new MajorModel();
                    retrievedMajor.setId(deptId);
                    retrievedMajor.setName(name);
                    retrievedMajor.setDescription(description);
                }
            }
            catch(Exception e){
                logger.error("SQLException is caught when trying to select a Major by majorId. The input majorId =" + id + ", the error = " + e.getMessage());
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

            return retrievedMajor;
    }

    @Override
    public MajorModel getMajorByName(String majorName) {
        MajorModel retrievedMajor = null;
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
            sql = "SELECT * FROM MAJOR where name = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, majorName);
            rs = preparedStatement.executeQuery();

            //STEP 4: Extract data from result set
            if(rs.next()) {
                //Retrieve by column name
                Long deptId  = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                //Fill the object
                retrievedMajor = new MajorModel();
                retrievedMajor.setId(deptId);
                retrievedMajor.setName(name);
                retrievedMajor.setDescription(description);
            }
        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to select a Major by majorId. The input majorName =" + majorName + ", the error = " + e.getMessage());
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

        return retrievedMajor;
    }

}
