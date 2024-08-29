import java.sql.SQLException;
import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Transaction extends Application {
  Scene scene;
  BorderPane bp;
  GridPane gp;
  Navbar navbar = new Navbar();
  Label name, phonenumber, address, tranId, totalprice, detail;
  VBox vb;
  Label title = new Label("'s Purchase History");
  String transactionID, phnumber, Address, Total;
  String username = "";
  String tempID = "";
  int total = 0;
  Connect connect = Connect.getInstance();
  Vector<HistoryData> historyDataList = new Vector<>();
  Vector<String> IDList = new Vector<>();
  ObservableList<String> ids = FXCollections.observableArrayList(IDList);
  ObservableList<HistoryData> historyList = FXCollections.observableArrayList(historyDataList);
  ListView<String> historyLV = new ListView<>();
  ListView<HistoryData> detailLV = new ListView<>();

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    launch(args);
  }

  public void setName(String username) {
    this.username = username;
    navbar.setName(username);
    title.setText(username + "'s Purchase History");
  }

  public void setUserId(String username) {
    String query = String.format("SELECT userID, address, phone_num\n" +
        "FROM user\n" +
        "WHERE username = '%s'", username);
    connect.rs = connect.execQuery(query);
    try {
      while (connect.rs.next()) {
        Address = connect.rs.getString("address");
        phnumber = connect.rs.getString("phone_num");
        tempID = connect.rs.getString("userID");
      }
      name = new Label("Username: " + username);
      phonenumber = new Label("Phone Number: " + phnumber);
      address = new Label("Address: " + Address);
    } catch (SQLException e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }

  public void initialize() {
    setUserId(username);
    bp = new BorderPane();
    gp = new GridPane();
    navbar.setEventHandler();
    vb = new VBox();

    tranId = new Label("Select a Transaction to view Details");
    detail = new Label("");
    totalprice = new Label("Total : ");
  }

  public void setElements() {
    scene = new Scene(bp, 800, 600);
    navbar.setEventHandler();
    vb.getChildren().addAll(historyLV);
    bp.setTop(navbar);
    bp.setCenter(gp);

    gp.add(title, 0, 0);
    gp.add(vb, 0, 1);
    gp.add(tranId, 1, 1);
    gp.add(detail, 1, 2);
    getData(tempID);
    for (HistoryData h : historyDataList) {
      historyList.add(h);
    }
    IDList.clear();
    ids.clear();
    for (HistoryData history : historyList) {
      String transactionID = history.getTransactionID();
      boolean found = false;
      for (String id : IDList) {
        if (id.equals(transactionID)) {
          found = true;
          break;
        }
      }
      if (!found) {
        IDList.add(transactionID);
      }
    }
    ids.addAll(IDList);
    historyLV.setItems(ids);
    detailLV.setItems(historyList);
    detailLV.toString();
  }

  private void getData(String id) {
    historyDataList.clear();
    String query = String.format("SELECT * FROM product p JOIN transaction_detail td \n"
        + "ON td.productID = p.productID JOIN transaction_header th \n"
        + "ON th.transactionID = td.transactionID \n"
        + "WHERE th.userID IN ('%s')", id);
    connect.rs = connect.execQuery(query);

    try {
      while (connect.rs.next()) {
        String transactionId = connect.rs.getString("transactionID");
        String userId = connect.rs.getString("userID");
        String productName = connect.rs.getString("product_name");
        Integer quantity = connect.rs.getInt("quantity");
        Double price = connect.rs.getDouble("product_price");
        Double total = price * quantity;
        historyDataList.add(new HistoryData(transactionId, userId, productName, quantity, price, total));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void setPosition() {
    gp.setPadding(new Insets(20));
    gp.setVgap(10);
    gp.setHgap(10);

    title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
    tranId.setStyle("-fx-font-weight: bold;");
    name.setStyle("-fx-font-weight: bold;");

    GridPane.setColumnSpan(title, 2);
    GridPane.setRowSpan(vb, 15);
    GridPane.setColumnSpan(detailLV, 2);
    GridPane.setRowSpan(detailLV, 7);

    historyLV.setMaxSize(150, 325);
    detailLV.setMinSize(250, 100);
    detailLV.setMaxSize(400, 200);
  }

  private void getTransactionDetail(String temp) {
    double total = 0;
    ObservableList<HistoryData> temp2 = FXCollections.observableArrayList();
    for (HistoryData h : historyDataList) {
      if (temp.equals(h.getTransactionID())) {
        temp2.add(h);
        total += h.getTotal();
      }
    }
    detailLV.setItems(temp2);
    detailLV.toString();
    tranId.setText("Transaction ID : " + temp);
    totalprice.setText(String.format("Total : Rp.%.0f", total));
  }

  public void setEventHandler() {
    if (historyList.isEmpty()) {
      tranId.setText("There's No History");
      detail.setText("Consider Purchasing Our Product");
    } else {
      historyLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (oldValue != null) {
          gp.getChildren().remove(name);
          gp.getChildren().remove(phonenumber);
          gp.getChildren().remove(address);
          gp.getChildren().remove(totalprice);
          gp.getChildren().remove(detailLV);
        }
        if (newValue != null) {
          gp.getChildren().remove(detail);
          gp.add(name, 1, 2);
          gp.add(phonenumber, 1, 3);
          gp.add(address, 1, 4);
          gp.add(totalprice, 1, 5);
          gp.add(detailLV, 1, 6);
          getTransactionDetail(historyLV.getSelectionModel().selectedItemProperty().getValue());
        }
      });
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    // TODO Auto-generated method stub
    initialize();
    setElements();
    setPosition();
    setEventHandler();
    primaryStage.setTitle("History");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}
