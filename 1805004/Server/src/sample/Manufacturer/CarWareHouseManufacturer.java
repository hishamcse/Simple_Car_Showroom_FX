package sample.Manufacturer;

import java.sql.*;
import java.util.Arrays;

public class CarWareHouseManufacturer {

    private static final String DB_CONNECTOR = "jdbc:sqlite:CarDataBase.db";

    private static final String TABLE_NAME = "Cars";
    private static final String INSERT_FORMAT = "CarReg,YearMade,Colour1,Colour2,Colour3,CarMake,CarModel,Price,Quantity,CarImage";

    private static final String QUERY_FOR_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String QUERY_ALL_REG = "SELECT CarReg FROM " + TABLE_NAME;
    private static final String QUERY_TO_ADD = "INSERT INTO " + TABLE_NAME + '(' + INSERT_FORMAT + ')' + " VALUES(?,?,?,?,?,?,?,?,?,?)";
    private static final String QUERY_TO_UPDATE = "UPDATE " + TABLE_NAME + " SET CarReg=?,YearMade=?,Colour1=?,Colour2=?,Colour3=?,CarMake=?," +
            "CarModel=?,Price=?,Quantity=?,CarImage=? WHERE CarReg=?";
    private static final String QUERY_TO_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE CarReg=?";

    private Connection connection;

    private PreparedStatement viewAll;
    private PreparedStatement queryReg;
    private PreparedStatement insertCar;
    private PreparedStatement updateCar;
    private PreparedStatement deleteCar;

    public boolean open() {

        try {
            connection = DriverManager.getConnection(DB_CONNECTOR);
            viewAll = connection.prepareStatement(QUERY_FOR_ALL);
            insertCar = connection.prepareStatement(QUERY_TO_ADD);
            queryReg = connection.prepareStatement(QUERY_ALL_REG);
            updateCar = connection.prepareStatement(QUERY_TO_UPDATE);
            deleteCar = connection.prepareStatement(QUERY_TO_DELETE);

            return true;
        } catch (SQLException e) {
            System.out.println("couldn't find:" + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (viewAll != null) {
                viewAll.close();
            }
            if (insertCar != null) {
                insertCar.close();
            }
            if (queryReg != null) {
                queryReg.close();
            }
            if (updateCar != null) {
                updateCar.close();
            }
            if (deleteCar != null) {
                deleteCar.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String view_all_cars() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_FOR_ALL);

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                String s = resultSet.getString("CarReg") + "," + resultSet.getString("YearMade") + "," + resultSet.getString("Colour1") + "," +
                        resultSet.getString("Colour2") + "," + resultSet.getString("Colour3") + "," + resultSet.getString("CarMake") + "," +
                        resultSet.getString("CarModel") + "," + resultSet.getString("Price") + "," + resultSet.getString("Quantity") + ",";
                if (resultSet.getString("CarImage").length() > 2) {
                    s = s + resultSet.getString("CarImage");
                } else {
                    s = s + " ";
                }
                sb.append(s).append("\n");
            }
            resultSet.close();

            sb.deleteCharAt(sb.length() - 1);
            System.out.println(sb.toString());
            System.out.println("Done");

            String[] strings = sb.toString().split("\n");
            String[] strings1 = strings[0].split(",");
            System.out.println(Arrays.toString(strings1));

            return sb.toString();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String addCar(String str) throws SQLException {
        String[] splits = str.split(",");
        String reg = splits[0];
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_ALL_REG);
        while (resultSet.next()) {
            if (resultSet.getString("CarReg").equalsIgnoreCase(reg)) {
                System.err.println("Error!!This registration number has already been used");
                return "Error";
            }
        }

        for (int i = 0; i < 10; i++) {
            insertCar.setString(i + 1, splits[i]);
        }

        int affectedRows = insertCar.executeUpdate();  //means how many rows it affects

        if (affectedRows != 1) {
            System.err.println("couldn't insert Car");
            return "Error";
        }
        return "Successful";
    }

    public String updateCar(String str) throws SQLException {
        String[] splits = str.split(",");
        String prev_Reg = splits[0];
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_ALL_REG);
        int c = 0;
        while (resultSet.next()) {
            if (resultSet.getString("CarReg").equalsIgnoreCase(prev_Reg)) {
                c = 1;
                break;
            }
        }
        resultSet.close();
        if (c == 0) {
            System.err.println("Error! this registration no doesn't exist");
            return "Error";
        }

        for (int i = 1; i <= 10; i++) {
            updateCar.setString(i, splits[i]);
        }
        updateCar.setString(11, splits[0]);

        int affectedRows = updateCar.executeUpdate();  //means how many rows it affects

        if (affectedRows != 1) {
            System.err.println("couldn't Update Car");
            return "Error";
        }
        return view_all_cars();
    }

    public String deleteCar(String reg) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_ALL_REG);
        int c = 0;
        while (resultSet.next()) {
            if (resultSet.getString("CarReg").equalsIgnoreCase(reg)) {
                c = 1;
                break;
            }
        }
        resultSet.close();
        if (c == 0) {
            System.err.println("Error! this registration no doesn't exist");
            return "Error";
        }

        deleteCar.setString(1, reg);
        int affectedRows = deleteCar.executeUpdate();  //means how many rows it affects

        if (affectedRows != 1) {
            System.err.println("couldn't Update Car");
            return "Error";
        }
        return "Successful";
    }
}