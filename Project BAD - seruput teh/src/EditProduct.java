import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class EditProduct extends Application {
  Scene scene, addScene, updateScene, removeScene;
  BorderPane bp;
  GridPane gp;
  FlowPane fp;

  AdminNavbar adminNavbar = new AdminNavbar();

  Vector<Product> product;
  ObservableList<Product> products;
  ListView<Product> productLV;

  Label title, details, price;
  Label inpPrice, inpName, inpDesc, updateProduct, removeProduct;

  Button addBtn, backBtn, updateBtn, removeBtn;
  TextField productName, productPrice;
  TextField pName, pPrice;
  TextArea pDesc;
  Connect connect;
  String username = "";
  String newName, newDesc, toUpd;
  double newPrice;
  Product selectedProduct;

  Label name = new Label("Welcome, ");



  public void initialize() {
    bp = new BorderPane();
    gp = new GridPane();
    fp = new FlowPane();


    title = new Label("Manage Products");
    details = new Label("Select a Product to Update");
    price = new Label("");
    inpPrice = new Label("");
    inpName = new Label("");
    inpDesc = new Label("");
    updateProduct = new Label("Update Product");
    removeProduct = new Label("Are you sure, you want to remove this product?");
    product = new Vector<>();
    products = FXCollections.observableArrayList(product);
    productLV = new ListView<>(products);
    connect = Connect.getInstance();

    addBtn = new Button("Add Product");
    backBtn = new Button("Back");
    updateBtn = new Button("Update Product");
    removeBtn = new Button("Remove Product");

    pName = new TextField();
    pPrice = new TextField();
    pDesc = new TextArea();
  }

  public void setElements() {
    adminNavbar.setEventHandler();

    pName.setPromptText("Input product name..");
    pPrice.setPromptText("Input product price..");
    pDesc.setPromptText("Input product description..");

    inpName = new Label("Input product name");
  inpPrice = new Label("Input product price");
  inpDesc = new Label("Input product description..");

    fp.getChildren().addAll(updateBtn, removeBtn);
    bp.setTop(adminNavbar);
    bp.setCenter(gp);

    setElementsGP();
    gp.add(addBtn, 1, 4);

    getProductData();
    for (Product p : product) {
    products.add(p);
  }
    productLV.toString();
    productLV.setMinSize(300, 325);
    addBtn.setMinWidth(150);
    removeBtn.setMinWidth(150);
    updateBtn.setMinWidth(150);
    backBtn.setMinWidth(150);
    details.setWrapText(true);
  }

  private void setElementsGP() {
      gp.add(title, 0, 0);
      gp.add(productLV, 0, 1);
      gp.add(name, 1, 1);
      gp.add(details, 1, 2);
      gp.add(price, 1, 3);
  }

public void setEventHandler() {
    productLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (oldValue != null) {
        gp.getChildren().clear();
        setElementsGP();
      gp.add(addBtn, 1, 4);
      }
      if (newValue != null) {
      selectedProduct = newValue;
        name.setText(newValue.getProduct_name());
        details.setText(newValue.getProduct_des());
        price.setText(String.format("Price : Rp.%.0f", newValue.getProduct_price()));
        gp.add(fp, 1, 5);
      }
    });
  }

  public void setPosition() {
    gp.setPadding(new Insets(20));
    gp.setVgap(10);
    gp.setHgap(10);
    fp.setHgap(10);

    title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
    name.setStyle("-fx-font-weight: bold;");
    updateProduct.setStyle("-fx-font-weight: bold;");
    inpName.setStyle("-fx-font-weight: bold;");
    inpPrice.setStyle("-fx-font-weight: bold;");
    inpDesc.setStyle("-fx-font-weight: bold;");
    updateProduct.setStyle("-fx-font-weight: bold;");
    removeProduct.setStyle("-fx-font-weight: bold;");

    GridPane.setColumnSpan(title, 2);
    GridPane.setRowSpan(productLV, 6);
  }

  private void setOnAction(Stage stage) {
    addBtn.setOnMouseClicked( e -> {
      initialize();
      setElements();
      setPosition();
      addScene = new Scene(bp, 800, 600);
      stage.setScene(addScene);

      VBox inputProduct = new VBox();

      name.setText("Welcome, " + username);
      gp.getChildren().clear();
      setElementsGP();
      fp.getChildren().removeAll(updateBtn,removeBtn);
      fp.getChildren().addAll(addBtn, backBtn);

      inputProduct.getChildren().addAll(inpName, pName, inpPrice, pPrice, inpDesc, pDesc);
      inputProduct.setSpacing(10);
      gp.add(inputProduct, 1, 4);
      gp.add(fp, 1, 5);

      gp.setVgap(5);


      productLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            name.setText(newValue.getProduct_name());
            details.setText(newValue.getProduct_des());
            price.setText(String.format("Price : Rp.%.0f", newValue.getProduct_price()));
          }
        });

      backBtn.setOnMouseClicked(e1 ->{
        try {
        start(stage);
      } catch (Exception e2) {
        // TODO Auto-generated catch block
        e2.printStackTrace();
      }
      });

      addBtn.setOnMouseClicked(e2 ->{
       newName = pName.getText();
       newDesc = pDesc.getText();

       if(pName.getText().isEmpty() || pPrice.getText().isEmpty() || pDesc.getText().isEmpty()) {
         Alert alert = new Alert(AlertType.ERROR);
         alert.setHeaderText("Failed to Add");
         alert.setContentText("All fields must be filled");
         alert.showAndWait();
       } else if (checkName() == false) {
         Alert alert = new Alert(AlertType.ERROR);
         alert.setHeaderText("Failed to Add");
         alert.setContentText("Product name must be unique");
         alert.showAndWait();
       } else {
         try {
           newPrice = Double.parseDouble(pPrice.getText());

           if(newPrice <=0) {
             Alert alert = new Alert(AlertType.ERROR);
             alert.setHeaderText("Failed to Add");
             alert.setContentText("Product price must be more than 0");
             alert.showAndWait();
           } else {
             addProductData(newName, newPrice, newDesc);
             Alert alert = new Alert(AlertType.INFORMATION);
             alert.setHeaderText("Success");
             alert.setContentText("Product added to Database!");
             alert.showAndWait();
             name.setText("Welcome, " + username);
             try {
              start(stage);
              } catch (Exception e1) {
              e1.printStackTrace();
            }
           }
         } catch (NumberFormatException e3) {
           Alert alert = new Alert(AlertType.ERROR);
           alert.setHeaderText("Failed to Add");
           alert.setContentText("Product price should be numeric");
           alert.showAndWait();
         }
       }
      });
    });

    updateBtn.setOnMouseClicked(e -> {
      initialize();
      setElements();
      setPosition();
      updateScene = new Scene(bp, 800, 600);
      stage.setScene(updateScene);

      gp.getChildren().clear();
      setElementsGP();
      fp.getChildren().removeAll(updateBtn,removeBtn);
      fp.getChildren().addAll(updateBtn, backBtn);
      gp.add(updateProduct, 1, 4);
      gp.add(pPrice, 1, 5);
      gp.add(fp, 1, 6);

      productLV.getSelectionModel().select(selectedProduct);
      name.setText(selectedProduct.getProduct_name());
      details.setText(selectedProduct.getProduct_des());
      price.setText(String.format("Price : Rp.%.0f", selectedProduct.getProduct_price()));

      productLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { 
        if (newValue != null) {
          selectedProduct = newValue;
          name.setText(newValue.getProduct_name());
          details.setText(newValue.getProduct_des());
          price.setText(String.format("Price : Rp.%.0f", newValue.getProduct_price()));
          }
        });

      backBtn.setOnMouseClicked(e1 ->{
        try {
        start(stage);
      } catch (Exception e2) {
        // TODO Auto-generated catch block
        e2.printStackTrace();
      }
      });


      updateBtn.setOnMouseClicked(e2 ->{
         if(pPrice.getText().isEmpty()) {
           Alert alert = new Alert(AlertType.ERROR);
           alert.setHeaderText("Failed to Add");
           alert.setContentText("Field must be filled");
           alert.showAndWait();
         } else {
           try {
             newPrice = Double.parseDouble(pPrice.getText());
             if(newPrice <=0) {
               Alert alert = new Alert(AlertType.ERROR);
               alert.setHeaderText("Failed to Update");
               alert.setContentText("Product price must be more than 0");
               alert.showAndWait();
             } else {
               toUpd = selectedProduct.getProduct_name();
               updateProductData(toUpd, newPrice);
               Alert alert = new Alert(AlertType.INFORMATION);
               alert.setHeaderText("Success");
               alert.setContentText("Product has been updated!");
               alert.showAndWait();
               name.setText("Welcome, " + username);
               try {
                start(stage);
                } catch (Exception e21) {
                e21.printStackTrace();
                }

             }
           } catch (NumberFormatException e3) {
             Alert alert = new Alert(AlertType.ERROR);
             alert.setHeaderText("Failed to Update");
             alert.setContentText("Product price should be numeric");
             alert.showAndWait();
           }
         }
        });
      });

    removeBtn.setOnMouseClicked(e -> {
      initialize();
      setElements();
      setPosition();
      removeScene = new Scene(bp, 800, 600);
      stage.setScene(removeScene);

      gp.getChildren().clear();
      setElementsGP();
      fp.getChildren().removeAll(updateBtn,removeBtn);
      fp.getChildren().addAll(removeBtn, backBtn);
      gp.add(removeProduct, 1, 4);
      gp.add(fp, 1, 5);

      productLV.getSelectionModel().select(selectedProduct);
      name.setText(selectedProduct.getProduct_name());
      details.setText(selectedProduct.getProduct_des());
      price.setText(String.format("Price : Rp.%.0f", selectedProduct.getProduct_price()));

      productLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { 
        if (newValue != null) {
          selectedProduct = newValue;
          name.setText(newValue.getProduct_name());
          details.setText(newValue.getProduct_des());
          price.setText(String.format("Price : Rp.%.0f", newValue.getProduct_price()));
          }
        });

      backBtn.setOnMouseClicked(e1 ->{
        try {
        start(stage);
      } catch (Exception e2) {
        // TODO Auto-generated catch block
        e2.printStackTrace();
      }
      });

      removeBtn.setOnMouseClicked(e2 ->{
        if (selectedProduct != null) {
          String tempID = selectedProduct.getProductID();
          String query = String.format("DELETE FROM product\n" + "WHERE productID = '%s'", tempID);
          connect.execUpdate(query);

          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setHeaderText("Success");
          alert.setContentText("Product has been removed!");
          alert.showAndWait();

          product.clear();
          refreshList();
          productLV.setSelectionModel(null);
          name.setText("Welcome, " + username);

          try {
            start(stage);
          } catch(Exception e3){
            e3.printStackTrace();
          }
          } else if(selectedProduct == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Failed to Remove");
            alert.setContentText("Removal was unsuccessful");
            alert.showAndWait();
          }
        });
        });
  }

  private boolean checkName() {
    for (Product pro: product) {
      if(pro.getProduct_name().equals(newName)) {
        return false;
      }
    }
    return true;
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

  private void addProductData(String productName, Double productPrice, String productDesc) {
    int count = 1;
  String id = String.format("TE%03d", count);
    for(Product pro: product) {
      while (pro.getProductID().equals(id)) {
        count++;
        id = String.format("TE%03d", count);
      }
    }
    id = String.format("TE%03d", count);
  String query = String.format("INSERT INTO product VALUES('%s','%s',%s,'%s')", id, productName, productPrice, productDesc);
    connect.execUpdate(query);
    product.clear();
    refreshList();
  }

  private void updateProductData(String productName, Double productPrice) {
      String query = String.format("UPDATE product\n"
        + "SET product_price = %s \n"
        + "WHERE product_name  = '%s'"
        , productPrice, productName);
      connect.execUpdate(query);
      product.clear();
      refreshList();
  }


  private void refreshList() {
    getProductData();
    products = FXCollections.observableArrayList(product);
    productLV.setItems(products);
    productLV.toString();
    productLV.refresh();
  }

  public void setName(String username) {
      this.username = username;
      adminNavbar.setName(username);
      name.setText("Welcome, " + username);
    }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    initialize();
    setElements();
    setPosition();
    scene = new Scene(bp, 800, 600);
    setEventHandler();
    setOnAction(primaryStage);
    primaryStage.setTitle("Products");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}
