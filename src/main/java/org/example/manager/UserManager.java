package org.example.manager;

import org.example.entity.User;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserManager {
    private static UserManager userManager;
    private Connection connection;

    private UserManager() {
    }

    public static UserManager getUserManager(String fileProperties, boolean createTable) throws SQLException, IOException {
        if (userManager == null) {
            userManager = new UserManager();
            userManager.init(fileProperties);
        }
        if(createTable) userManager.createTable();
        return userManager;
    }

    private void init(String fileProperties) throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/" + fileProperties));

        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        connection = DriverManager.getConnection(url, username, password);
    }

    private void createTable() throws SQLException {
        try {
            String sql = "CREATE TABLE user ("
                    + "id INT not null auto_increment,"
                    + "name VARCHAR(25),"
                    + "age INT,"
                    + "createDate DATE,"
                    + "primary key(id)"
                    + ");";

            PreparedStatement statement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
    }

    public boolean addUser(User user) {
        try {
            String sql = "insert into user (name, age, createDate) values (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setInt(2, user.getAge());
            statement.setDate(3, user.getCreateDate());

            connection.setAutoCommit(false);
            statement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUser(int id) {
        try {
            String sql = "select * from user where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setAge(resultSet.getInt("age"));
            user.setCreateDate(resultSet.getDate("createDate"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAll() {
        try {
            String sql = "select * from user";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                User user = getUser(resultSet.getInt("id"));
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteUserById(int id) {
        try {
            String sql = "delete from user where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            connection.setAutoCommit(false);
            statement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeNameById(int id, String newName) throws SQLException {
        try {
            String sql = "update user set name = ? where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(2, id);
            statement.setString(1, newName);

            connection.setAutoCommit(false);
            statement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            return false;
        }
    }


}
