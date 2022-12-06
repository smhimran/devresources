package com.devreources.devresources.tools.populate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;


public class Runner {

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(Runner.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Populator populator;

        try {
            populator = new Populator(properties);
            populator.populate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
