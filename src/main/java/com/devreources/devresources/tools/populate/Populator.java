package com.devreources.devresources.tools.populate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Populator {
    private final Properties properties;
    private final Connection connection;
    private final Statement statement;

    public Populator(Properties properties) throws SQLException {
        this.properties = properties;

        String dbUrl = properties.getProperty(DatabaseConstants.DATABASE_URL);
        String dbUsername = properties.getProperty(DatabaseConstants.DATABASE_USERNAME);
        String dbPassword = properties.getProperty(DatabaseConstants.DATABASE_PASSWORD);

        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        statement = connection.createStatement();
    }

    public void populate() {
        createTables();
    }

    private void createTables() {
        String sql = "CREATE TABLE Category (" +
                "id INT NOT NULL," +
                "title varchar(255)," +
                "PRIMARY KEY (id)" +
                ")";

        try {
            statement.execute(sql);
            System.out.println("Successfully created table Category");
        } catch (SQLException e) {
            System.out.println("Error creating table Category");
            e.printStackTrace();
        }

        sql = "CREATE TABLE Tag (" +
                "id INT NOT NULL," +
                "title VARCHAR(255)," +
                "PRIMARY KEY (id)" +
                ")";

        try {
            statement.execute(sql);
            System.out.println("Successfully created table Tag");
        } catch (SQLException e) {
            System.out.println("Error creating table Tag");
            e.printStackTrace();
        }

        sql = "CREATE TABLE User (" +
                "id INT NOT NULL," +
                "username VARCHAR(255)," +
                "name VARCHAR(255)," +
                "password VARCHAR(255)," +
                "email VARCHAR(255)," +
                "PRIMARY KEY (id)" +
                ")";

        try {
            statement.execute(sql);
            System.out.println("Successfully created table User");
        } catch (SQLException e) {
            System.out.println("Error creating table User");
            e.printStackTrace();
        }

        sql = "CREATE TABLE Resource (" +
                "id INT NOT NULL," +
                "author_id INT NOT NULL ," +
                "category_id INT NOT NULL," +
                "title VARCHAR(255)," +
                "link VARCHAR(255)," +
                "source VARCHAR(255)," +
                "description LONGTEXT," +
                "timestamp TIMESTAMP," +
                "PRIMARY KEY (id)," +
                "FOREIGN KEY (author_id)  REFERENCES User(id)," +
                "FOREIGN KEY (category_id)  REFERENCES Category(id)" +
                ")";

        try {
            statement.execute(sql);
            System.out.println("Successfully created table Resources");
        } catch (SQLException e) {
            System.out.println("Error creating table Resources");
            e.printStackTrace();
        }

        sql = "CREATE TABLE Review (" +
                "id INT NOT NULL," +
                "resource_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "description LONGTEXT," +
                "PRIMARY KEY (id)," +
                "FOREIGN KEY (resource_id)  REFERENCES Resource(id)," +
                "FOREIGN KEY (user_id)  REFERENCES User(id)" +
                ")";

        try {
            statement.execute(sql);
            System.out.println("Successfully created table Review");
        } catch (SQLException e) {
            System.out.println("Error creating table Review");
            e.printStackTrace();
        }

        sql = "CREATE TABLE ResourceTag (" +
                "id INT NOT NULL," +
                "resource_id INT NOT NULL," +
                "tag_id INT NOT NULL," +
                "PRIMARY KEY (id)," +
                "FOREIGN KEY (resource_id)  REFERENCES Resource(id)," +
                "FOREIGN KEY (tag_id)  REFERENCES Tag(id)" +
                ")";

        try {
            statement.execute(sql);
            System.out.println("Successfully created table ResourceTag");
        } catch (SQLException e) {
            System.out.println("Error creating table ResourceTag");
            e.printStackTrace();
        }

        sql = "CREATE TABLE Favorite (" +
                "id INT NOT NULL," +
                "resource_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "timestamp TIMESTAMP," +
                "PRIMARY KEY (id)," +
                "FOREIGN KEY (resource_id)  REFERENCES Resource(id)," +
                "FOREIGN KEY (user_id)  REFERENCES User(id)" +
                ")";

        try {
            statement.execute(sql);
            System.out.println("Successfully created table Favorite");
        } catch (SQLException e) {
            System.out.println("Error creating table Favorite");
            e.printStackTrace();
        }
    }
}
