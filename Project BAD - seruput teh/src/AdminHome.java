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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminHome extends Application {
  Scene scene;
  BorderPane bp;
  GridPane gp;
  String username = "";

  AdminNavbar adminNavbar = new AdminNavbar();
  Label title, name, detail, price, totalPrice, quantity;
  Label welcome = new Label("Welcome, ");

  Connect connect = Connect.getInstance();
  Vector<Product> product = new Vector<>();
  ObservableList<Product> products = FXCollections.observableArrayList(product);
  ListView<Product> productLV = new ListView<>(products);

  public void setName(String username) {
    this.username = username;
    adminNavbar.setName(username);
    welcome.setText("Welcome, " + username);
  }

  public void initialize() {
    bp = new BorderPane();
    gp = new GridPane();


    title = new Label("SeRuput Teh");
    detail = new Label("Select a product to view");
    price = new Label("");
    name = new Label("");
    quantity = new Label("");
    totalPrice = new Label("");
  }

  public void setElements() {
    scene = new Scene(bp, 800, 600);
    adminNavbar.setEventHandler();

    bp.setTop(adminNavbar);
    bp.setCenter(gp);

    gp.add(title, 0, 0);
    gp.add(productLV, 0, 1);
    gp.add(welcome, 1, 1);
    gp.add(detail, 1, 2);
    gp.add(price, 1, 3);

    getProductData();
    for (Product p : product) {
      products.add(p);
    }
    productLV.setItems(products);
    productLV.toString();
    productLV.setMinSize(300, 325);
    detail.setWrapText(true);
  }

  public void setEventHandler() {
    productLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        welcome.setText(newValue.getProduct_name());
        detail.setText(newValue.getProduct_des());
        price.setText(String.format("Price : Rp.%.0f", newValue.getProduct_price()));
      }
    });

  }

  public void setPosition() {
    gp.setPadding(new Insets(20));
    gp.setVgap(10);
    gp.setHgap(10);

    title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
    welcome.setStyle("-fx-font-weight: bold;");

    GridPane.setColumnSpan(title, 2);
    GridPane.setRowSpan(productLV, 6);
  }

  public static void main(String[] args) {
    launch(args);
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

  @Override
  public void start(Stage primaryStage) throws Exception {
    // TODO Auto-generated method stub
    initialize();
    setElements();
    setPosition();
    setEventHandler();
    primaryStage.setTitle("Home");
    primaryStage.setScene(scene);
    primaryStage.show();

  }

}
