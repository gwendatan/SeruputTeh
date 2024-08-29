import java.sql.SQLException;
import java.util.Optional;
import java.util.Vector;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Cart extends Application { // karena transactionID pasti ada merah2 di eclipsenya

	Scene scene, scene1;
	BorderPane bp;
	GridPane gp;
	FlowPane qtyFP, btnFP;
	Navbar navbar = new Navbar();

	// labels
	Label title = new Label(" 's Cart");
	Label prodName = new Label("");
	Label welcome, detail, prodDetail, prodPrice, quantity, totalPrice, totalCart, orderInfo, name, phoneNumber,
			address;

	// spinner
	Spinner<Integer> qtySpinner = new Spinner<>();

	// button
	Button updateCart, removeFromCart, makePurchase;

	// list view
	ListView<CartData> cartLV = new ListView<>();

	// initialisasi connection ke database
	Connect connect = Connect.getInstance();

	// fetch dari db -> masukin ke vector -> display ke tabelnya
	Vector<CartData> carts = new Vector<>();
	ObservableList<CartData> cartList = FXCollections.observableArrayList(carts);

	String username = "";
	String phonenumber, addressdata;
	String userID = " ";
	Integer qty;
	Double price, totalprice, totalcart;
	Integer q = 0;

	private ChangeListener<Integer> qSpinnerListener;

	public void setName(String username) {
		this.username = username;
		navbar.setName(username);
		title.setText(username + "'s Cart");
		prodName.setText("Welcome, " + username);
	}

	public void setUserId(String username) {
		String query = String.format("SELECT userID, address, phone_num\n" + "FROM user\n" + "WHERE username = '%s'",
				username);
		connect.rs = connect.execQuery(query);
		try {
			while (connect.rs.next()) {
				userID = connect.rs.getString("userID");
				addressdata = connect.rs.getString("address");
				phonenumber = connect.rs.getString("phone_num");
			}
			name = new Label("Username: " + username);
			phoneNumber = new Label("Phone Number: " + phonenumber);
			address = new Label("Address: " + addressdata);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void setTotalPrice(Double totalprice) {
		this.totalprice = totalprice;
		totalPrice.setText(String.format("Total : Rp.%.0f", totalprice));
	}

	public void initialize() {
		setUserId(username);
		bp = new BorderPane();
		gp = new GridPane();
		qtyFP = new FlowPane();
		btnFP = new FlowPane();

		// labels
		title = new Label(username + "'s Cart"); // pakai nama orang
		prodName = new Label("Welcome, " + username); // pakai nama orang
		prodDetail = new Label("Select a product to add and remove");

		prodPrice = new Label(""); // harga produk
		quantity = new Label("Quantity: "); // quantity
		totalPrice = new Label(""); // total

		// ‘[quantity]x [product name]([total price])’
		totalCart = new Label(" "); // total price of cart
		totalCart.setText(String.format("Total : Rp.%.0f", getTotalPrice()));

		orderInfo = new Label("Order Information");
		name = new Label("Username: " + username); // username
		phoneNumber = new Label("Phone Number: " + phonenumber); // phone number
		address = new Label("Address: " + addressdata); // address

		// button
		updateCart = new Button("Update Cart");
		removeFromCart = new Button("Remove From Cart");
		makePurchase = new Button("Make Purchase");

	}

	public void setElements() {
		scene = new Scene(bp, 800, 600);
		navbar.setEventHandler();

		// button width
		updateCart.setMinWidth(150);
		removeFromCart.setMinWidth(150);
		makePurchase.setMinWidth(150);

		// bp
		bp.setTop(navbar);
		bp.setCenter(gp);
		btnFP.setHgap(10);

		// gridpane
		gp.add(title, 0, 0);
		gp.add(cartLV, 0, 1);
		gp.add(prodName, 1, 1);
		gp.add(prodDetail, 1, 2);
		gp.add(prodPrice, 1, 3);

		gp.add(totalCart, 0, 7);
		gp.add(orderInfo, 0, 8);
		gp.add(name, 0, 9);
		gp.add(phoneNumber, 0, 10);
		gp.add(address, 0, 11);
		gp.add(makePurchase, 0, 12);

		// value spinner
		SpinnerValueFactory<Integer> qtyValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(-10, 10, 1);
		qtySpinner.setValueFactory(qtyValue);

		// list view
		getCartData();
		for (CartData cd : carts) {
			cartList.add(cd);
		}
		cartLV.setItems(cartList);
		cartLV.toString();
		totalCart.setText(String.format("Total Price: Rp.%.0f", getTotalPrice()));
		cartLV.setMinSize(300, 200);

		prodDetail.setWrapText(true);
	}

	public void setPosition() {
		gp.setPadding(new Insets(20));
		gp.setVgap(10);
		gp.setHgap(10);

		qtyFP.setHgap(10);
		qtyFP.getChildren().addAll(quantity, qtySpinner, totalPrice);

		btnFP.getChildren().add(updateCart);
		btnFP.getChildren().add(removeFromCart);

		title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		prodName.setStyle("-fx-font-weight: bold;");
		orderInfo.setStyle("-fx-font-weight: bold;");

		GridPane.setColumnSpan(title, 2);
		GridPane.setRowSpan(cartLV, 5);
	}

	private void confirmPopUp(Stage stage) {
		makePurchase.setOnMouseClicked(e -> {
			initialize();
			scene1 = new Scene(bp, 400, 300);
			stage.setScene(scene1);
			stage.setTitle("Order Confirmation");
			Label heading, confirm;
			Button yesButton, noButton;
			HBox hbox;
			VBox vbox;

			heading = new Label("Order Confirmation");
			heading.setStyle("-fx-text-fill: #E2E2E2;-fx-font-weight: bold; -fx-font-size: 20px;");
			
			bp.setTop(gp);
			gp.setStyle("-fx-background-color: #434748;");//blue
			bp.setStyle("-fx-background-color: #6B94AD;");//black
			
			confirm = new Label("Are you sure you want to make purchase?");
			confirm.setStyle("-fx-font-size: 16px;");
			
			yesButton = new Button("Yes");
			yesButton.setStyle("-fx-background-color: #E2E2E2;");
			
			noButton = new Button("No");
			noButton.setStyle("-fx-background-color: #E2E2E2;");
			
			vbox = new VBox(10);
			hbox = new HBox(10);

			yesButton.setMinWidth(150);
			noButton.setMinWidth(150);

			
			hbox.getChildren().addAll(yesButton, noButton);
			vbox.getChildren().addAll(confirm, hbox);

			gp.setAlignment(Pos.CENTER);
			hbox.setAlignment(Pos.CENTER);
			vbox.setAlignment(Pos.CENTER);

			gp.add(heading, 0,0);
			bp.setCenter(vbox);
			
			bp.setMinWidth(40);
			bp.setMinHeight(40);


			yesButton.setOnMouseClicked(e1 -> {
				if (cartList.isEmpty()) {
		            Alert alert2 = new Alert(AlertType.ERROR);
		            alert2.setHeaderText("Failed to Make Transaction");
		            alert2.showAndWait();
		            
		          } else if(!cartList.isEmpty()){
		            // Pindah ke TransactionHeader & TransactionDetail
		            
		        	Alert alert3 = new Alert(AlertType.INFORMATION);
		        	alert3.setTitle("Message");
		        	alert3.setHeaderText("Successfully purchased");
		            alert3.showAndWait();
		        	  
		        	String transactionID = generateTransactionID();
		            addTransactionHeaderData(transactionID, userID);
		            for (CartData c : cartList) {
		              String productID = c.getProductID();
		              Integer quantity = c.getQuantity();
		              addTransactionDetailData(transactionID, productID, quantity);
		            }
		          }
				
				String query = String.format("DELETE FROM cart");
		        connect.execUpdate(query);
		        refreshLV();
		
		        prodName.setText("No Item in Cart");
		        prodDetail.setText("Consider adding one!");
		        prodPrice.setText("");
		        totalprice = (double) 0;
		        setTotalPrice(totalprice);
		        
		        try {
			        start(stage);
			      } catch (Exception e3) {
			        // TODO Auto-generated catch block
			        e3.printStackTrace();
			      }
			});
			
			noButton.setOnMouseClicked(e2 ->{
				try {
			        start(stage);
			      } catch (Exception e4) {
			        // TODO Auto-generated catch block
			        e4.printStackTrace();
			      }
			});
		});
		
		cartList.clear();
		refreshLV();
		
	}

	public void setEventHandler() {
		if (cartList.isEmpty()) {
			prodName.setText("No Item in Cart");
			prodDetail.setText("Consider adding one!");
		} else if (!cartList.isEmpty()) {
			cartLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				if (oldValue != null) {
					gp.getChildren().remove(qtyFP);
					gp.getChildren().remove(btnFP);
					qtySpinner.getValueFactory().setValue(1);
				}
				if (newValue != null) {
					prodName.setText(newValue.getProductName());
					prodDetail.setText(newValue.getProductDes());
					gp.add(qtyFP, 1, 4);
					gp.add(btnFP, 1, 5);

					price = newValue.getProductPrice() * qtySpinner.getValue();
					prodPrice.setText(String.format("Price : Rp.%.0f", price));

					if (qSpinnerListener != null) {
						qtySpinner.valueProperty().removeListener(qSpinnerListener);
					}

					qSpinnerListener = (spinnerObservable, oldValueSpinner, newValueSpinner) -> {
						int q = newValueSpinner;
						double totalprice = newValue.getProductPrice() * q;
						totalPrice.setText(String.format("Total : Rp.%.0f", totalprice));

						if (!qtyFP.getChildren().contains(totalPrice)) {
							qtyFP.getChildren().add(totalPrice);
						}
					};
					qtySpinner.valueProperty().addListener(qSpinnerListener);
					qtyFP.getChildren().remove(totalPrice);
				}

			});

			updateCart.setOnMouseClicked(e -> {
				prodName.setText("Welcome, " + username);
				prodDetail.setText("Select a product to add and remove");
				prodPrice.setText("");
				if (cartList.isEmpty()) {
					prodName.setText("No Item in Cart");
					prodDetail.setText("Consider adding one!");
				}

				q = qtySpinner.getValue();
				int check = cartLV.getSelectionModel().getSelectedItem().getQuantity();
				if (q >= 1) {
					String tempProductID = cartLV.getSelectionModel().getSelectedItem().getProductID();
					Integer tempQuantity = q + check;
					updateQuantity(tempProductID, tempQuantity);
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Message");
					alert.setHeaderText("Updated Cart");
					
					ButtonType okButton = new ButtonType("OK");
					alert.getButtonTypes().setAll(okButton);
					alert.showAndWait();
				} else if (q == 0) {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setHeaderText("Not a Valid Amount");
					alert2.showAndWait();
				} else if (q <= 0) {
					if ((q + check) > 0) {
						String tempProductID = cartLV.getSelectionModel().getSelectedItem().getProductID();
						Integer tempQuantity = check + q;
						updateQuantity(tempProductID, tempQuantity);
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Message");
						alert.setHeaderText("Updated Cart");
						
						ButtonType okButton = new ButtonType("OK");
						alert.getButtonTypes().setAll(okButton);
						alert.showAndWait();
						
					} else if ((q + check) == 0) {
						String tempProductID = cartLV.getSelectionModel().getSelectedItem().getProductID();
						cartLV.getItems().removeAll(cartLV.getSelectionModel().getSelectedItems());
						
						String query = String.format("DELETE FROM Cart\n" + "WHERE productId = '%s'", tempProductID);
						connect.execUpdate(query);
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Message");
						alert.setHeaderText("Updated Cart");
						
						ButtonType okButton = new ButtonType("OK");
						alert.getButtonTypes().setAll(okButton);
						alert.showAndWait();
						refreshLV();
					} else {
						Alert alert2 = new Alert(AlertType.ERROR);
						alert2.setHeaderText("Not a Valid Amount");
						alert2.showAndWait();
					}
				}

			});

			removeFromCart.setOnMouseClicked(e -> {
				String tempProductID = cartLV.getSelectionModel().getSelectedItem().getProductID();
				cartLV.getItems().removeAll(cartLV.getSelectionModel().getSelectedItems());
				String query = String.format("DELETE FROM Cart\n" + "WHERE productID = '%s'", tempProductID);
				connect.execUpdate(query);

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Message");
				alert.setHeaderText("Deleted from Cart");
				alert.showAndWait();
				refreshLV();
				prodPrice.setText("");
				if (cartList.isEmpty()) {
					prodName.setText("No Item in Cart");
					prodDetail.setText("Consider adding one!");
				} else {
					prodName.setText("Welcome, " + username);
					prodDetail.setText("Select a product to add and remove");
				}
			});

		}
	}

	private String generateTransactionID() {
		String transactionID = "TR001";
		String query = "SELECT transactionID FROM transaction_header";
		connect.rs = connect.execQuery(query);

		int index = 1;
		try {
			while (connect.rs.next()) {
				String checkID = connect.rs.getString("transactionID");
				if (transactionID.equals(checkID)) {
					index++;
					transactionID = String.format("TR%03d", index);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactionID;
	}

	public void updateQuantity(String productID, int quantity) {
		String query = String.format("UPDATE Cart\n" + "SET quantity = %s\n" + "WHERE productID = '%s'", quantity,
				productID);
		connect.execUpdate(query);
		refreshLV();
	}

	private void getCartData() {
		// Select from DB
		String query = "SELECT c.quantity, c.productID, c.userID, p.product_name, p.product_des, p.product_price\n"
				+ "FROM cart c JOIN product p\n" + "ON c.productID = p.productID\n"
				+ "WHERE p.productID IN (SELECT c.productID FROM cart c)\n";

		connect.rs = connect.execQuery(query);

		try {
			while (connect.rs.next()) {
				String productID = connect.rs.getString("productID");
				String userID = connect.rs.getString("userID");
				Integer quantity = connect.rs.getInt("quantity");
				String productName = connect.rs.getString("product_name");
				String productDes = connect.rs.getString("product_des");
				Double price = connect.rs.getDouble("product_price");
				cartList.add(new CartData(productID, userID, quantity, productName, productDes, price));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void addTransactionHeaderData(String transactionID, String userID) {
		String query = String.format("INSERT INTO transaction_header VALUES('%s','%s')", transactionID, userID);
		connect.execUpdate(query);
	}

	private void addTransactionDetailData(String transactionID, String productID, Integer quantity) {
		String query = String.format("INSERT INTO transaction_detail VALUES('%s','%s',%s)", transactionID, productID,
				quantity);
		connect.execUpdate(query);
	}

	public double getTotalPrice() {
		double total = 0;
		for (CartData item : cartList) {
			total += (item.getQuantity() * item.getProductPrice());
		}
		return total;
	}

	public void refreshLV() {
		cartList.clear();
		getCartData();
		for (CartData c : carts) {
			cartList.add(c);
		}
		cartLV.setItems(cartList);
		cartLV.toString();

		totalCart.setText(String.format("Total Price: Rp.%.0f", getTotalPrice()));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
		setElements();
		setEventHandler();
		setPosition();
		setUserId(username);
		confirmPopUp(primaryStage);
		primaryStage.setTitle("Home");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}