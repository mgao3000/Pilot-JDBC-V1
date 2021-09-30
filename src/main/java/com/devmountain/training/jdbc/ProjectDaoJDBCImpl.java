package com.devmountain.training.jdbc;

import com.devmountain.training.dao.ProjectDao;
import com.devmountain.training.model.ProjectModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDaoJDBCImpl implements ProjectDao {
    private Logger logger = LoggerFactory.getLogger(ProjectDaoJDBCImpl.class);

    //STEP 1: Database information
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/pilot";
    private static final String USER = "admin";
    private static final String PASS = "password";

    @Override
    public ProjectModel save(ProjectModel project) {
        ProjectModel savedProject = null;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database for saving a projectModel...");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Insert statement...");
            String SQL_INSERT = "INSERT INTO PROJECT (NAME, DESCRIPTION, CREATE_DATE) VALUES (?, ?, ?)";;
//            preparedStatement = dbConnection.prepareStatement(SQL_INSERT);
            preparedStatement = dbConnection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
//            preparedStatement.setLong(1, project.getId());
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getDescription());
            preparedStatement.setTimestamp(3, project.getCreateDateInTimestamp());

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                savedProject = project;

            rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) {
                Long generatedId = rs.getLong(1);
                if(generatedId != null)
                    savedProject.setId(generatedId);
            }

        }
        catch(SQLException e){
            logger.error("SQLException is caught when trying to save a project. The input project =" + project + ", the error = " + e.getMessage());
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
        return savedProject;
    }

    @Override
    public ProjectModel update(ProjectModel project) {
        ProjectModel updatedProject = null;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database for updating a projectModel");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Updating statement...");

            String SQL_UPDATE = "UPDATE PROJECT SET name=?, description=? , create_date=? where id=?";
            preparedStatement = dbConnection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getDescription());
            preparedStatement.setTimestamp(3, project.getCreateDateInTimestamp());
            preparedStatement.setLong(4, project.getId());

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                updatedProject = project;

        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to update a Project. The input project =" + project + ", the error = " + e.getMessage());
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
        return updatedProject;
    }

    @Override
    public boolean deleteByName(String projectName) {
        boolean isProjectDeleted = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database...");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Deleting a projectModel by projectName statement...");

            String SQL_DELETE = "DELETE FROM PROJECT where NAME=?";
            preparedStatement = dbConnection.prepareStatement(SQL_DELETE);
            preparedStatement.setString(1, projectName);

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                isProjectDeleted = true;

        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to delete a Project by ProjectName. The input ProjectName =" + projectName + ", the error = " + e.getMessage());
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
        return isProjectDeleted;
    }

    @Override
    public boolean deleteById(Long projectId) {
        boolean isProjectDeleted = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            //STEP 2: Open a connection
            logger.info("Connecting to database...");
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            logger.debug("Deleting a projectModel by projectId statement...");

            String SQL_DELETE = "DELETE FROM PROJECT where id=?";
            preparedStatement = dbConnection.prepareStatement(SQL_DELETE);
            preparedStatement.setLong(1, projectId);

            int row = preparedStatement.executeUpdate();
            if(row > 0)
                isProjectDeleted = true;

        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to delete a Project by ProjectId. The input ProjectId =" + projectId + ", the error = " + e.getMessage());
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
        return isProjectDeleted;
    }

    @Override
    public boolean delete(ProjectModel project) {
        Long projectId = project.getId();
        return deleteById(projectId);
    }

    @Override
    public List<ProjectModel> getProjects() {
        List<ProjectModel> projects = new ArrayList<ProjectModel>();
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
            sql = "SELECT * FROM PROJECT";
            rs = stmt.executeQuery(sql);

            //STEP 4: Extract data from result set
            while(rs.next()) {
                //Retrieve by column name
                Long id  = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Timestamp createDate = rs.getTimestamp("create_date");

                //Fill the object
                ProjectModel project = new ProjectModel();
                project.setId(id);
                project.setName(name);
                project.setDescription(description);
                project.setCreateDate(createDate);
                projects.add(project);
            }
        }
        catch(SQLException e){
            logger.error("SQLException is caught when trying to select all Projects. the error = " + e.getMessage());
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
        return projects;
    }

    @Override
    public ProjectModel getProjectById(Long id) {
        ProjectModel retrievedProject = null;
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
            sql = "SELECT * FROM PROJECT where id = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            rs = preparedStatement.executeQuery();

            //STEP 4: Extract data from result set
            if(rs.next()) {
                //Retrieve by column name
                Long deptId  = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Timestamp createDate = rs.getTimestamp("create_date");

                //Fill the object
                retrievedProject = new ProjectModel();
                retrievedProject.setId(deptId);
                retrievedProject.setName(name);
                retrievedProject.setDescription(description);
                retrievedProject.setCreateDate(createDate);
            }
        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to select a project by projectId. The input projectId =" + id + ", the error = " + e.getMessage());
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

        return retrievedProject;
    }

    @Override
    public ProjectModel getProjectByName(String projectName) {
        ProjectModel retrievedProject = null;
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
            sql = "SELECT * FROM PROJECT where name = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, projectName);
            rs = preparedStatement.executeQuery();

            //STEP 4: Extract data from result set
            if(rs.next()) {
                //Retrieve by column name
                Long deptId  = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Timestamp createDate = rs.getTimestamp("create_date");

                //Fill the object
                retrievedProject = new ProjectModel();
                retrievedProject.setId(deptId);
                retrievedProject.setName(name);
                retrievedProject.setDescription(description);
                retrievedProject.setCreateDate(createDate);
            }
        }
        catch(Exception e){
            logger.error("SQLException is caught when trying to select a project by projectName. The input projectName =" + projectName + ", the error = " + e.getMessage());
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

        return retrievedProject;
    }
}
