package sample.Viewer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarWareHouseViewer {

    private static final String DB_CONNECTOR = "jdbc:sqlite:CarDataBase.db";

    private static final String TABLE_NAME = "Cars";

    private static final String QUERY_FOR_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String QUERY_ALL_REG = "SELECT CarReg FROM " + TABLE_NAME;
    private static final String QUERY_BY_REG = "SELECT * FROM " + TABLE_NAME + " WHERE CarReg=?";
    private static final String QUERY_BY_MAKE = "SELECT * FROM " + TABLE_NAME + " WHERE CarMake=?";
    private static final String QUERY_BY_MAKE_AND_MODEL = "SELECT * FROM " + TABLE_NAME + " WHERE CarMake=? AND CarModel=?";
    private static final String UPDATE_QUANTITY = "UPDATE " + TABLE_NAME + " SET Quantity=? WHERE CarReg=?";

    private Connection connection;

    private PreparedStatement viewAll;
    private PreparedStatement queryByReg;
    private PreparedStatement queryAllReg;
    private PreparedStatement queryMake;
    private PreparedStatement queryMakeModel;
    private PreparedStatement updateQuantity;

    public boolean open() {
        try {
            connection = DriverManager.getConnection(DB_CONNECTOR);
            viewAll = connection.prepareStatement(QUERY_FOR_ALL);
            queryAllReg = connection.prepareStatement(QUERY_ALL_REG);
            queryByReg = connection.prepareStatement(QUERY_BY_REG);
            queryMake = connection.prepareStatement(QUERY_BY_MAKE);
            queryMakeModel = connection.prepareStatement(QUERY_BY_MAKE_AND_MODEL);
            updateQuantity = connection.prepareStatement(UPDATE_QUANTITY);

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
            if (queryByReg != null) {
                queryByReg.close();
            }
            if (queryAllReg != null) {
                queryAllReg.close();
            }
            if (queryMake != null) {
                queryMake.close();
            }
            if (queryMakeModel != null) {
                queryMakeModel.close();
            }
            if (updateQuantity != null) {
                updateQuantity.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String Car_By_Reg(String regNo) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_ALL_REG);
        int c = 0;
        while (resultSet.next()) {
            if (resultSet.getString("CarReg").equalsIgnoreCase(regNo)) {
                queryByReg.setString(1, resultSet.getString("CarReg"));
                c = 1;
                break;
            }
        }
        if (c == 0) {
            return "Error";
        }

        resultSet = queryByReg.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getString("CarReg").equalsIgnoreCase(regNo)) {
                String s = resultSet.getString("CarReg") + "," + resultSet.getString("YearMade") + "," + resultSet.getString("Colour1") + "," +
                        resultSet.getString("Colour2") + "," + resultSet.getString("Colour3") + "," + resultSet.getString("CarMake") + "," +
                        resultSet.getString("CarModel") + "," + resultSet.getString("Price") + "," + resultSet.getString("Quantity") + ",";
                if (resultSet.getString("CarImage").length() > 2) {
                    s = s + resultSet.getString("CarImage");
                } else {
                    s = s + " ";
                }
                return s;
            }
        }
        resultSet.close();
        return "Error";
    }

    public String Cars_By_MakeAndModel(String str) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet;
        StringBuilder sb = new StringBuilder();
        String[] strings = str.split(",");
        String maker = strings[0];
        String model = strings[1];
        List<String> list = new ArrayList<>();
        if (model.equalsIgnoreCase("any")) {
            resultSet = statement.executeQuery(QUERY_FOR_ALL);
            int c = 0;
            while (resultSet.next()) {
                if (resultSet.getString("CarMake").equalsIgnoreCase(maker)) {
                    queryMake.setString(1, resultSet.getString("CarMake"));
                    c = 1;
                    ResultSet resultSet2 = queryMake.executeQuery();
                    while (resultSet2.next()) {
                        String s = resultSet2.getString("CarReg") + "," + resultSet2.getString("YearMade") + "," + resultSet2.getString("Colour1") + "," +
                                resultSet2.getString("Colour2") + "," + resultSet2.getString("Colour3") + "," + resultSet2.getString("CarMake") + "," +
                                resultSet2.getString("CarModel") + "," + resultSet2.getString("Price") + "," + resultSet2.getString("Quantity") + ",";
                        if (resultSet2.getString("CarImage").length() > 2) {
                            s = s + resultSet2.getString("CarImage");
                        } else {
                            s = s + " ";
                        }
                        if (!list.contains(s)) {
                            sb.append(s).append("\n");
                        }
                        list.add(s);
                    }
                    resultSet2.close();
                }
            }
            resultSet.close();
            if (c == 0) {
                System.err.println("Error!No Such Car Make Exists");
                return "Error";
            }

            sb.deleteCharAt(sb.length() - 1);
        } else {
            resultSet = statement.executeQuery(QUERY_FOR_ALL);
            int c = 0;
            while (resultSet.next()) {
                if (resultSet.getString("CarMake").equalsIgnoreCase(maker)) {
                    queryMakeModel.setString(1, resultSet.getString("CarMake"));
                    if (resultSet.getString("CarModel").equalsIgnoreCase(model)) {
                        queryMakeModel.setString(2, resultSet.getString("CarModel"));
                        c = 1;
                        break;
                    }
                }
            }
            if (c == 0) {
                System.err.println("Error!No Such Car Make Or Car Model Exists");
                return "Error";
            }

            resultSet = queryMakeModel.executeQuery();
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
            sb.deleteCharAt(sb.length() - 1);
            resultSet.close();
        }
        return sb.toString();
    }

    public String buy_A_Car(String regNo) throws SQLException {
        queryByReg.setString(1, regNo);
        ResultSet resultSet = queryByReg.executeQuery();
        int i = 0;
        while (resultSet.next()) {
            if (resultSet.getString("CarReg").equalsIgnoreCase(regNo)) {
                i = Integer.parseInt(resultSet.getString("Quantity"));
                break;
            }
        }
        resultSet.close();

        if (i == 0) {
            return "Error";
        }
        updateQuantity.setString(1, String.valueOf(i - 1));
        updateQuantity.setString(2, regNo);

        updateQuantity.executeUpdate();

        return "Success";
    }
}