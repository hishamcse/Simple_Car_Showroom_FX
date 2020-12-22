package sample.Manufacturer;

import java.sql.*;

public class Manufacturer {

    private static final String DB_CONNECTOR = "jdbc:sqlite:CarDataBase.db";

    private static final String TABLE_NAME = "Manufacturer";
    private static final String INSERT_FORMAT = "Name,Password,Image";

    private static final String QUERY_FOR_ALL_MANUFACTURERS = "SELECT * FROM " + TABLE_NAME;
    private static final String QUERY_ALL_NAMES = "SELECT Name FROM " + TABLE_NAME;
    private static final String QUERY_TO_ADD_MANUFACTURER = "INSERT INTO " + TABLE_NAME + '(' + INSERT_FORMAT + ')' + " VALUES(?,?,?)";
    private static final String QUERY_TO_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE Name=? AND Password=?";

    private Connection connection;

    private PreparedStatement queryAll;
    private PreparedStatement queryNames;
    private PreparedStatement insertManufacturer;
    private PreparedStatement deleteManufacturer;

    public boolean open() {
        try {
            connection = DriverManager.getConnection(DB_CONNECTOR);
            queryAll = connection.prepareStatement(QUERY_FOR_ALL_MANUFACTURERS);
            insertManufacturer = connection.prepareStatement(QUERY_TO_ADD_MANUFACTURER);
            queryNames = connection.prepareStatement(QUERY_ALL_NAMES);
            deleteManufacturer = connection.prepareStatement(QUERY_TO_DELETE);

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
            if (insertManufacturer != null) {
                insertManufacturer.close();
            }
            if (queryNames != null) {
                queryNames.close();
            }
            if (deleteManufacturer != null) {
                deleteManufacturer.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addManufacturer(String str) throws SQLException {
        String[] splits = str.split(",");
        String name = splits[0];
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_ALL_NAMES);
        while (resultSet.next()) {
            if (resultSet.getString("Name").equals(name)) {
                System.err.println("Error!!This account name has already been used");
                return false;
            }
        }

        for (int i = 0; i < 3; i++) {
            insertManufacturer.setString(i + 1, splits[i]);
        }

        int affectedRows = insertManufacturer.executeUpdate();  //means how many rows it affects

        if (affectedRows != 1) {
            System.err.println("couldn't insert manufacturer");
            return false;
        }
        return true;
    }

    public boolean deleteManufacturer(String str) throws SQLException {
        String[] splits = str.split(",");
        String name = splits[0];
        String pass = splits[1];
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_FOR_ALL_MANUFACTURERS);
        int c = 0;
        while (resultSet.next()) {
            if (resultSet.getString("Name").equals(name) && resultSet.getString("Password").equals(pass)) {
                c = 1;
                break;
            }
        }
        if (c == 0) {
            return false;
        }
        deleteManufacturer.setString(1, name);
        deleteManufacturer.setString(2, pass);
        deleteManufacturer.executeUpdate();
        return true;
    }

    public String manufacturerLogin(String str) throws SQLException {
        String[] splits = str.split(",");
        String name = splits[0];
        String password = splits[1];
        String s = "Unsuccessful";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_FOR_ALL_MANUFACTURERS);
        while (resultSet.next()) {
            if (resultSet.getString("Name").equals(name) && resultSet.getString("Password").equals(password)) {
                s = resultSet.getString("Name") + "," + resultSet.getString("Image");
            }
        }
        return s;
    }
}