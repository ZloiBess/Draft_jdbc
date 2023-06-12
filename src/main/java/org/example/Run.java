package org.example;

import org.example.manager.UserManager;

import java.io.IOException;
import java.sql.SQLException;

public class Run {
    public static void main(String[] args) throws SQLException, IOException {
        UserManager userManager = UserManager.getUserManager("mysql.properties", true);

    }
}