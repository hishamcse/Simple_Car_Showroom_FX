package sample.AdminPanel;

import java.sql.*;

public class Admin {

    private static final String DB_CONNECTOR = "jdbc:sqlite:CarDataBase.db";

    private static final String TABLE_NAME = "Admin";

    private static final String QUERY_FOR_ALL_ADMINS = "SELECT * FROM " + TABLE_NAME;

    private Connection connection;

    private PreparedStatement queryAll;

    public boolean open() {
        try {
            connection = DriverManager.getConnection(DB_CONNECTOR);
            queryAll = connection.prepareStatement(QUERY_FOR_ALL_ADMINS);

            return true;
        } catch (SQLException e) {
            System.out.println("couldn't find:" + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (queryAll != null) {
                queryAll.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String adminLogin(String str) throws SQLException {
        String[] splits = str.split(",");
        String name = splits[0];
        String password = splits[1];
        String s = "Unsuccessful";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_FOR_ALL_ADMINS);
        while (resultSet.next()) {
            if (resultSet.getString("Name").equals(name) && resultSet.getString("Password").equals(password)) {
                s = resultSet.getString("Name");
            }
        }
        return s;
    }
}
