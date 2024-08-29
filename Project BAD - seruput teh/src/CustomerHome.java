import java.sql.SQLException;
import java.util.Vector;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CustomerHome extends Application {
  Scene scene;
  BorderPane bp = new BorderPane();
  GridPane gp = new GridPane();
  FlowPane fp = new FlowPane();
  Vector<User> userList = new Vector<>();

  Navbar navbar = new Navbar();


  // Labels
  Label title = new Label("SeRuput Teh");
  Label welcome = new Label("Welcome, ");
  Label detail = new Label("Select a product to view");
  Label price = new Label("");
  Label name = new Label("");
  Label quantity = new Label("");
  Label totalPrice = new Label("");

  Vector<Product> product = new Vector<>();
  ObservableList<Product> productList = FXCollections.observableArrayList(product);
  ListView<Product> productLV = new ListView<>(productList);

  Spinner<Integer> qSpinner = new Spinner<>();
  Button addCart = new Button("Add To Cart");
  Connect connect = Connect.getInstance();
  private ChangeListener<Integer> qSpinnerListener;

  String productId, productName, productPrice, productDesc;
  int q = 1;
  String username = "";
  String tempId = "temp";

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    launch(args);
  }

  public void setName(String username) {
    this.username = username;
    navbar.setName(username);
    welcome.setText("Welcome, " + username);
  }

  public void setElements() {
    scene = new Scene(bp, 800, 600);
    navbar.setEventHandler();
    addCart.setMinWidth(150);

    // setting borderpane
    bp.setTop(navbar);
    bp.setCenter(gp);

    // gridpane
    gp.add(title, 0, 0);
    gp.add(productLV, 0, 1);
    gp.add(welcome, 1, 1);
    gp.add(detail, 1, 2);
    gp.add(price, 1, 3);

    SpinnerValueFactory<Integer> qValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1);
    qSpinner.setValueFactory(qValue);

    getUserData();
    getProductData();
    for (Product p : product) {
      productList.add(p);
    }
    productLV.setItems(productList);
    productLV.toString();
    productLV.setMinSize(300, 325);
    detail.setWrapText(true);
  }

  public void setEventHandler() {
    productLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (oldValue != null) {
        gp.getChildren().remove(fp);
        gp.getChildren().remove(addCart);
        qSpinner.getValueFactory().setValue(1);
      }
      if (newValue != null) {
        welcome.setText(newValue.getProduct_name());
        detail.setText(newValue.getProduct_des());
        price.setText(String.format("Price : Rp.%.0f", newValue.getProduct_price()));
        quantity.setText("Quantity : ");
        gp.add(fp, 1, 4);
        gp.add(addCart, 1, 5);

        if (qSpinnerListener != null) {
          qSpinner.valueProperty().removeListener(qSpinnerListener);
        }

        qSpinnerListener = (spinnerObservable, oldValueSpinner, newValueSpinner) -> {
          //int q = newValueSpinner;
          double total = newValue.getProduct_price() * newValueSpinner;
          totalPrice.setText(String.format("Total : Rp.%.0f", total));

          if (!fp.getChildren().contains(totalPrice)) {
            fp.getChildren().add(totalPrice);
          }
        };

        qSpinner.valueProperty().addListener(qSpinnerListener);
        fp.getChildren().remove(totalPrice);
      }
    });

    addCart.setOnMouseClicked(e -> {
      // variable ngecheck datanya ada atau ga biar di update + nyambung ke cart
      q = qSpinner.getValue();
      if (q >= 1) {
      String tempProductId = productLV.getSelectionModel().getSelectedItem().getProductID();
      Integer tempQuantity = q;

      getUserData();
      for(User us: userList) {
        if(username.equals(us.getUsername())) {
          tempId = us.getUserID();
        }
      }
        addCartData(tempProductId, tempId ,tempQuantity);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Added to Cart");
        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait();

      } else if (q < 1){
        Alert alert2 = new Alert(AlertType.ERROR);
        alert2.setHeaderText("Failed to Add");
        alert2.showAndWait();
      }
    });

  }

  public void setPosition() {
    gp.setPadding(new Insets(20));
    gp.setVgap(10);
    gp.setHgap(10);

    fp.setHgap(10);
    fp.getChildren().addAll(quantity, qSpinner);

    title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
    welcome.setStyle("-fx-font-weight: bold;");

    GridPane.setColumnSpan(title, 2);
    GridPane.setRowSpan(productLV, 6);

  }

  private void getProductData() {
    String query = "SELECT * FROM product";
    connect.rs = connect.execQuery(query);

    try {
      while (connect.rs.next()) {
        String id = connect.rs.getString("ProductID");
        String productName = connect.rs.getString("Product_name");
        Double productPrice = connect.rs.getDouble("Product_price");
        String productDesc = connect.rs.getString("Product_des");
        product.add(new Product(id, productName, productPrice, productDesc));

      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void addCartData(String productID, String userID , Integer quantity) {
    boolean check = isDuplicate(productID, userID);
    if (check == true) {
      String query = String.format("UPDATE Cart\n"
                  + "SET quantity = quantity + %s\n" 
                  + "WHERE productID = '%s'"
                  ,quantity, productID);
      connect.execUpdate(query);
  } else {
      String query = String.format("INSERT INTO cart VALUES('%s','%s',%s)", productID, userID, quantity);
      connect.execUpdate(query);
  }
  }

  private void getUserData() {
      String query = "SELECT * FROM user";
      connect.rs = connect.execQuery(query);

      try {
        while(connect.rs.next())
        {
          String id = connect.rs.getString("UserID");
          String username = connect.rs.getString("Username");
          String password = connect.rs.getString("Password");
          String role = connect.rs.getString("Role");
          String address = connect.rs.getString("Address");				
          String phone_num = connect.rs.getString("Phone_num");
          String gender = connect.rs.getString("Gender");
          userList.add(new User(id, username, password, role, address, phone_num, gender));
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }

  private boolean isDuplicate(String productID, String userID) {
      String query = String.format("SELECT * FROM cart WHERE productID = '%s' AND userID = '%s'", productID, userID);
      connect.execQuery(query);
      try {
          return connect.rs.next();
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }finally{
        try {
          if (connect.rs != null) {
                  connect.rs.close();
          }
        } catch (SQLException e) {
              e.printStackTrace();
        }
      }
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    // TODO Auto-generated method stub
    setElements();
    setPosition();
    setEventHandler();
    primaryStage.setTitle("Home");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}
