package com.example.baseproj;

import com.example.baseproj.Data.DBConnect;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DriverController {

    public TableView Cust_table;
    public TextField h_idTF;
    public TextField h_nameTF;
    public TextField h_addrTF;
    public TextField h_phoneTF;
    public TextField b_idTF;
    public TextField b_hidTF;
    public TextField b_indateTF;
    public TextField b_outdateTF;
    public TextField b_numpepTF;
    public TextField b_cidTF;
    public TextField b_priceTF;
    public TextField b_dateTF;
    public TextField tfname;
    public PasswordField tfpass;
    private ObservableList<ObservableList> data;
    private ObservableList<ObservableList> Hdata;
    public TableView Hot_table;
    public TableView Book_table;
    private ObservableList<ObservableList> Bdata;

    public TextField c_idTF;
    public TextField c_nameTF;
    public TextField c_addrTF;
    public TextField c_phoneTF;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public Button SignInBT;
    @FXML
    private Label welcomeText;
    private static String dbURL;
    private static String dbUsername = "root";
    private static String dbPassword = "123456";
    private static String URL = "localhost";
    private static String port = "3306";
    private static String dbName = "bakr_and_mohammad";
    private static Connection con;

    private static void connectDB() throws ClassNotFoundException, SQLException {

        dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
        Properties p = new Properties();
        p.setProperty("user", dbUsername);
        p.setProperty("password", dbPassword);
        p.setProperty("useSSL", "false");
        p.setProperty("autoReconnect", "true");
        Class.forName("com.mysql.cj.jdbc.Driver");

        con = DriverManager.getConnection(dbURL, p);

    }

    private boolean flagC = false;
    private boolean flagH = false;
    private  boolean flagB = false;


    public static void  ExecuteStatement(String SQL) throws SQLException, ClassNotFoundException {
        connectDB();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(SQL);
            stmt.close();

        } catch (SQLException s) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please, make sure  add a valid value.\n Please, check the keys");
            alert.setTitle("ERORR  !!!");
            alert.show();
            System.out.println("SQL statement is not executed!");
            System.out.println(s);

        }

    }
//    private static void getCustomerData() throws SQLException, ClassNotFoundException {
//
//        String SQL;
//        StringBuilder s = new StringBuilder();
//        connectDB();
//        System.out.println("Connection established");
//
//
//        SQL = "select CustomerID,CustomerName,CustomerAddress,PhoneNumber from Customers order by CustomerID";
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery(SQL);
//
//        while (rs.next())
//            s.append(new Customers(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3),
//                    rs.getString(4))+"\n");
//
//        rs.close();
//        stmt.close();
//
//        con.close();
//        System.out.println("Connection closed ");
//        System.out.println(s);
//    }

    public void SignInBT(ActionEvent actionEvent) throws IOException {

        if (tfname.getText().equalsIgnoreCase("admin")||tfpass.getText().equalsIgnoreCase("admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("sceneAdmin.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WRONG !!");
            alert.setContentText("Invalid Arguments ");
            alert.show();
        }
    }

    public void c_insert(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(c_idTF.getText());
            String name = c_nameTF.getText();
            String addr = c_addrTF.getText();
            String phone = c_phoneTF.getText();
            ExecuteStatement("INSERT INTO customers (CustomerID, CustomerName, CustomerAddress, PhoneNumber) VALUES("
                    + id + ", '" + name + "', '" + addr + "', '" + phone + "');");
            c_nameTF.clear();
            c_addrTF.clear();
            c_phoneTF.clear();
            c_idTF.clear();
            buildData(); // Refreshing the TableView after insert operation
            Cust_table.getColumns().remove(0);
            Cust_table.getColumns().remove(0);
            Cust_table.getColumns().remove(0);
            Cust_table.getColumns().remove(0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }



    //    public void refreshTable() {
//        // Clear existing table columns
//        Cust_table.getColumns().clear();
//        Cust_table.getItems().clear();
//        Cust_table.refresh();
//        // Re-build data
//        buildData("Some string"); // Replace "Some string" with the actual string you want to pass
//    }
public void buildData() throws SQLException {

    Connection c;
    //TableView temp = new TableView<String>();
    data = FXCollections.observableArrayList();
    try {
        c = DBConnect.connect();
        //SQL FOR SELECTING ALL OF CUSTOMER
        String SQL = ("select * from Customers");
        //ResultSet
        ResultSet rs = c.createStatement().executeQuery(SQL);

        /**
         * ********************************
         * TABLE COLUMN ADDED DYNAMICALLY *
         *********************************
         */
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
             int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setMinWidth(140);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });


           // temp.getColumns().addAll(col);
            Cust_table.getColumns().addAll(col);
            System.out.println("Column [" + i + "] ");
        }

        /**
         * ******************************
         * Data added to ObservableList *
         *******************************
         */
        while (rs.next()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //Iterate Column
                row.add(rs.getString(i));
            }
            System.out.println("Row [1] added " + row);
            data.add(row);

        }

        //FINALLY ADDED TO TableView
       // temp.setItems(data);
        Cust_table.setItems(data); // safdasf asf asfasgasgasgg
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error on Building Data");
    }
   // Cust_table =temp;
}


    public void buildDataB() {
        Connection c;
        Bdata = FXCollections.observableArrayList();
        try {
            c = DBConnect.connect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = ("select * from bookings");
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setMinWidth(150);
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                Book_table.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                Bdata.add(row);

            }

            //FINALLY ADDED TO TableView
            Book_table.setItems(Bdata); // safdasf asf asfasgasgasgg
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }
    public void buildDataH() {
        Connection c;
        Hdata = FXCollections.observableArrayList();
        try {
            c = DBConnect.connect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = ("select * from hotels");
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setMinWidth(140);
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                Hot_table.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                Hdata.add(row);

            }

            //FINALLY ADDED TO TableView
            Hot_table.setItems(Hdata); // safdasf asf asfasgasgasgg
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    public void c_print(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (!flagC){
            buildData();
            flagC = true;
        }
        else {
            buildData();
            Cust_table.getColumns().remove(0);
            Cust_table.getColumns().remove(0);
            Cust_table.getColumns().remove(0);
            Cust_table.getColumns().remove(0);



        }


    }

    public void H_Print(ActionEvent actionEvent) {
        if (!flagH){
            buildDataH();
        flagH = true;
    }else{
            buildDataH();
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
        }
    }

    public void B_print(ActionEvent actionEvent) {
        if (!flagB) {
            buildDataB();
            flagB = true;
        } else {
            buildDataB();
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
        }
    }

    public void c_update(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(c_idTF.getText());
            String name = c_nameTF.getText();
            String addr = c_addrTF.getText();
            String phone = c_phoneTF.getText();

            connectDB();
            ExecuteStatement(  "UPDATE customers SET CustomerName = '" + name + "', CustomerAddress = '" + addr + "', PhoneNumber = '" + phone + "' WHERE CustomerID = " + id + ";");
            c_nameTF.clear();
            c_addrTF.clear();
            c_phoneTF.clear();
            c_idTF.clear();
            buildData(); // Refreshing the TableView after insert operation
            Cust_table.getColumns().remove(0);
            Cust_table.getColumns().remove(0);
            Cust_table.getColumns().remove(0);
            Cust_table.getColumns().remove(0);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void c_delete(ActionEvent actionEvent) {
        		try {
                    int id = Integer.parseInt(c_idTF.getText());
			connectDB();
			ExecuteStatement("delete from  bookings where CustomerID = " + id + ";");
                    ExecuteStatement("delete from  customers where CustomerID = " + id + ";");
                    c_idTF.clear();
                    buildData(); // Refreshing the TableView after insert operation
                    Cust_table.getColumns().remove(0);
                    Cust_table.getColumns().remove(0);
                    Cust_table.getColumns().remove(0);
                    Cust_table.getColumns().remove(0);

		} catch (SQLException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
			e.printStackTrace();
	}
    }

    public void h_insert(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(h_idTF.getText());
            String name = h_nameTF.getText();
            String addr = h_addrTF.getText();
            String phone = h_phoneTF.getText();
            ExecuteStatement("INSERT INTO hotels (HotelID, HotelName, HotelAddress, PhoneNumber) VALUES("
                    + id + ", '" + name + "', '" + addr + "', '" + phone + "');");
            h_nameTF.clear();
            h_addrTF.clear();
            h_phoneTF.clear();
            h_idTF.clear();
            buildDataH(); // Refreshing the TableView after insert operation
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void h_delete(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(h_idTF.getText());
            connectDB();
            ExecuteStatement("delete from  bookings where HotelID = " + id + ";");
            ExecuteStatement("delete from  hotels where HotelID = " + id + ";");
            h_idTF.clear();
            buildDataH(); // Refreshing the TableView after insert operation
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void h_update(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(h_idTF.getText());
            String name = h_nameTF.getText();
            String addr = h_addrTF.getText();
            String phone = h_phoneTF.getText();

            connectDB();
            ExecuteStatement(  "UPDATE hotels SET HotelName = '" + name + "', HotelAddress = '" + addr + "', PhoneNumber = '" + phone + "' WHERE HotelID = " + id + ";");
            h_nameTF.clear();
            h_addrTF.clear();
            h_phoneTF.clear();
            h_idTF.clear();
            buildDataH(); // Refreshing the TableView after insert operation
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);
            Hot_table.getColumns().remove(0);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void b_insert(ActionEvent actionEvent) {

        try {
            int bid = Integer.parseInt(b_idTF.getText());
            int cid = Integer.parseInt(b_cidTF.getText());
            int hid = Integer.parseInt(b_hidTF.getText());
            String indate = b_indateTF.getText();
            String outdate = b_outdateTF.getText();
            String date = b_dateTF.getText();
            int numpop = Integer.parseInt(b_numpepTF.getText());
            double price = Double.parseDouble(b_priceTF.getText());

            ExecuteStatement("INSERT INTO bookings (BookingID, CustomerID, HotelID,BookingDate, CheckInDate, CheckOutDate,NumberOfPeople, BookingPrice) VALUES("
                    + bid + ", '" + cid + "', '" + hid + "', '" + date +"', '" + indate +"', '" + outdate +"', '" + numpop +"', '" + price + "');");
            b_idTF.clear();
            b_hidTF.clear();
            b_indateTF.clear();
            b_outdateTF.clear();
            b_numpepTF.clear();
            b_cidTF.clear();
            b_priceTF.clear();
            b_dateTF.clear();
            buildDataB(); // Refreshing the TableView after insert operation
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numeric values.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred:");
            e.printStackTrace();
        }
        B_print(new ActionEvent());
    }

    public void b_update(ActionEvent actionEvent) {

        try {
            int bid = Integer.parseInt(b_idTF.getText());
            int cid = Integer.parseInt(b_cidTF.getText());
            int hid = Integer.parseInt(b_hidTF.getText());
            String indate = b_indateTF.getText();
            String outdate = b_outdateTF.getText();
            String date = b_dateTF.getText();
            int numpop = Integer.parseInt(b_numpepTF.getText());
            double price = Double.parseDouble(b_priceTF.getText());
            connectDB();

            ExecuteStatement("UPDATE bookings SET BookingDate = '" + date + "', CheckInDate = '" + indate + "', CheckOutDate = '" + outdate + "', NumberOfPeople = " + numpop + ", BookingPrice = " + price + " WHERE BookingID = " + bid + " AND CustomerID = " + cid + " AND HotelID = " + hid + ";");
            b_idTF.clear();
            b_hidTF.clear();
            b_indateTF.clear();
            b_outdateTF.clear();
            b_numpepTF.clear();
            b_cidTF.clear();
            b_priceTF.clear();
            b_dateTF.clear();
            buildDataB(); // Refreshing the TableView after insert operation
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void b_delete(ActionEvent actionEvent) {


        try {
            int bid = Integer.parseInt(b_idTF.getText());

            connectDB();

            ExecuteStatement("DELETE FROM bookings Where BookingID =" + bid + ";");
            b_idTF.clear();
            b_hidTF.clear();
            b_indateTF.clear();
            b_outdateTF.clear();
            b_numpepTF.clear();
            b_cidTF.clear();
            b_priceTF.clear();
            b_dateTF.clear();
            buildDataB(); // Refreshing the TableView after insert operation
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
            Book_table.getColumns().remove(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}