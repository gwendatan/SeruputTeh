import java.util.Vector;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Login extends Application {
  Scene scene;
  BorderPane bp;
  GridPane gp;
  FlowPane fp;

  Label titleLbl, usernameLbl, passwordLbl, registerLbl, descriptionLbl;
  TextField usernameTF;
  PasswordField passwordPF;
  Button loginBtn;
  Connect connect = Connect.getInstance();
  Vector<User> userList = new Vector<>();

  String tempId = "temp";


  public void initialize() {
  getUserData();
    bp = new BorderPane();
    gp = new GridPane();
    fp = new FlowPane();
    scene = new Scene(bp, 800, 600);
    titleLbl = new Label("Login");
    usernameLbl = new Label("Username : ");
    passwordLbl = new Label("Password : ");
    registerLbl = new Label("register here");
    descriptionLbl = new Label("Don’t have an account yet?");

    usernameTF = new TextField();
    passwordPF = new PasswordField();
    loginBtn = new Button("Login");
  }

  public void setElements() {
    bp.setCenter(gp);

    usernameTF.setPromptText("input username...");
    passwordPF.setPromptText("input password...");

    gp.setAlignment(Pos.CENTER);
    gp.add(titleLbl, 1, 0);
    gp.add(usernameLbl, 0, 1);
    gp.add(passwordLbl, 0, 2);
    gp.add(usernameTF, 1, 1);
    gp.add(passwordPF, 1, 2);
    gp.add(fp, 1, 3);
    gp.add(loginBtn, 1, 4);

    loginBtn.setMinWidth(100);
    registerLbl.setTextFill(Color.BLUE);

  }

  public void setPosition() {
    gp.setVgap(10);
    fp.setHgap(5);

    fp.getChildren().addAll(descriptionLbl, registerLbl);

    Font titleFont = Font.font("Arial", FontWeight.BOLD, 35);
    titleLbl.setFont(titleFont);

    // Create ColumnConstraints and set width for each column
    ColumnConstraints col1 = new ColumnConstraints();
    ColumnConstraints col2 = new ColumnConstraints();

    col1.setPercentWidth(10);
    col2.setPercentWidth(40);

    // Apply column constraints to the GridPane
    gp.getColumnConstraints().setAll(col1, col2);
  }

  public void setEventHandler() {
    loginBtn.setOnMouseClicked(e -> {
      boolean check = false;
      int count = -1;
      String username = usernameTF.getText();
      String password = passwordPF.getText();

      if (username.isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Failed to Login");
        alert.setContentText("All fields must be filled");
        alert.showAndWait();
      } else if (password.isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Failed to Login");
        alert.setContentText("All fields must be filled");
        alert.showAndWait();
      } else {
        for(User user: userList) {
        if(username.equals(user.getUsername()) && password.equals(user.getPassword())) {
          check = true;
          if(user.getRole().equals("Admin")) {
            AdminHome ah = new AdminHome();
            ah.setName(username);
              openNew(ah);
            } else if(user.getRole().equals("User")) {
              CustomerHome ch = new CustomerHome();
              ch.setName(username);
              openNew(ch);

            }
          }
        }
      }

        if(check == false) {
          Alert alert = new Alert(AlertType.ERROR);
          alert.setHeaderText("Failed to Login");
          alert.setContentText("Invalid credential");
          alert.showAndWait();
      }
  });

    registerLbl.setOnMouseClicked(e1 -> {
      openNew(new Register());
    });
 }

 private void openNew(Application app) {
        Stage stage = (Stage) scene.getWindow();
        try {
          app.start(stage);
        } catch (Exception e) {
          e.printStackTrace();
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
 public static void main(String[] args) {
      // TODO Auto-generated method stub
      launch(args);
    }

  @Override
  public void start(Stage primaryStage) throws Exception {
    // TODO Auto-generated method stub
    initialize();
    setElements();
    setPosition();
    setEventHandler();
    primaryStage.setTitle("Login");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}
