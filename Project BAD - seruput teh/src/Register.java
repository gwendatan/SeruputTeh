import java.util.Vector;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Register extends Application {

  Vector<User> user = new Vector<>();
  Connect connect = Connect.getInstance();
  Scene scene;
  BorderPane bp;
  GridPane gp;
  FlowPane fp, fp1;

  Label registerLbl, usernameLbl, emailLbl, passwordLbl, confirmpasswordLbl, phonenumberLbl, addressLbl, genderLbl,
      descriptionLbl, loginLbl;
  TextField usernameTF, emailTF, phonenumberTF;
  TextArea addressTA;
  PasswordField passwordPF, confirmpasswordPF;
  ToggleGroup genderTG;

  RadioButton maleRB, femaleRB;
  Button registerBtn;
  CheckBox tnc;

  String id, inpUsername, inpEmail, inpPassword, inpAddress, role, inpPhone_num, inpGender;
  int count = 1;

  public void initialize() {
    bp = new BorderPane();
    gp = new GridPane();
    fp = new FlowPane();
    fp1 = new FlowPane();
    genderTG = new ToggleGroup();
    scene = new Scene(bp, 800, 600);

    registerLbl = new Label("Register");
    usernameLbl = new Label("Username : ");
    emailLbl = new Label("Email : ");
    passwordLbl = new Label("Password : ");
    confirmpasswordLbl = new Label("Confirm password : ");
    phonenumberLbl = new Label("Phone number : ");
    addressLbl = new Label("Address : ");
    genderLbl = new Label("Gender : ");
    descriptionLbl = new Label("Have an account?");
    loginLbl = new Label("login here");

    usernameTF = new TextField();
    emailTF = new TextField();
    phonenumberTF = new TextField();

    addressTA = new TextArea();

    passwordPF = new PasswordField();
    confirmpasswordPF = new PasswordField();

    maleRB = new RadioButton("Male");
    femaleRB = new RadioButton("Female");

    tnc = new CheckBox("i agree to all terms and condition");

    registerBtn = new Button("Register");

    getUserData();

  }

  public void setElements() {
    bp.setCenter(gp);

    usernameTF.setPromptText("input username...");
    emailTF.setPromptText("input email...");
    passwordPF.setPromptText("input password...");
    confirmpasswordPF.setPromptText("input confirm password...");
    phonenumberTF.setPromptText("input phone number...");
    addressTA.setPromptText("input address...");

    gp.setAlignment(Pos.CENTER);
    gp.add(registerLbl, 1, 0);
    gp.add(usernameLbl, 0, 1);
    gp.add(usernameTF, 1, 1);
    gp.add(emailLbl, 0, 2);
    gp.add(emailTF, 1, 2);
    gp.add(passwordLbl, 0, 3);
    gp.add(passwordPF, 1, 3);
    gp.add(confirmpasswordLbl, 0, 4);
    gp.add(confirmpasswordPF, 1, 4);
    gp.add(phonenumberLbl, 0, 5);
    gp.add(phonenumberTF, 1, 5);
    gp.add(addressLbl, 0, 6);
    gp.add(addressTA, 1, 6);
    gp.add(genderLbl, 0, 7);
    gp.add(fp, 1, 7);
    gp.add(tnc, 1, 8);
    gp.add(fp1, 1, 9);
    gp.add(registerBtn, 1, 10);

    registerBtn.setMinWidth(110);
    Font titleFont = Font.font("Arial", FontWeight.BOLD, 30);
    registerLbl.setFont(titleFont);
    loginLbl.setTextFill(Color.BLUE);
  }

  public void setPosition() {
    gp.setHgap(10);
    gp.setVgap(10);
    fp.setHgap(10);
    fp1.setHgap(5);

    // GridPane.setColumnSpan(registerBtn, 3);

    fp.getChildren().addAll(maleRB, femaleRB);
    fp1.getChildren().addAll(descriptionLbl, loginLbl);

    maleRB.setToggleGroup(genderTG);
    femaleRB.setToggleGroup(genderTG);
  }

  public void setEventHandler() {
    registerBtn.setOnAction(e -> {
      //all field must be filled
      inpUsername = usernameTF.getText();
      inpEmail = emailTF.getText();
      inpPassword = passwordPF.getText();
      inpPhone_num = phonenumberTF.getText();
      inpAddress = addressTA.getText();
      if (usernameTF.getText().isEmpty() || emailTF.getText().isEmpty()
          || passwordPF.getText().isEmpty()
          || confirmpasswordPF.getText().isEmpty()
          || phonenumberTF.getText().isEmpty()
          || addressTA.getText().isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Failed to Register");
        alert.setContentText("All fields must be filled");
        alert.showAndWait();

      //gender selected
      } else if (!maleRB.isSelected() && !femaleRB.isSelected()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Failed to Register");
        alert.setContentText("All fields must be filled");
        alert.showAndWait();

      //tnc checked
      } else if (!tnc.isSelected()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Failed to Register");
        alert.setContentText("You must agree to the Terms and Condition!");
        alert.showAndWait();

      //username must be 5-20 characters 
      } else if(usernameTF.getLength() < 5 || usernameTF.getLength() > 20)	{
          Alert alert = new Alert (AlertType.ERROR);
          alert.setHeaderText("Failed to Register");
          alert.setContentText("Username must be 5-20 characters");
          alert.showAndWait();

      //email ends with '@gmail.com'  -> tidak sesuai soal
//      } else if (!emailTF.getText().endsWith("@gmail.com")) {
//          Alert alert = new Alert(AlertType.ERROR);
//          alert.setHeaderText("Failed to Register");
//          alert.setContentText("Email must ends with '@gmail.com'");
//          alert.showAndWait();

      //password must be alphanumeric
      } else if (!passwordPF.getText().matches("^[a-zA-Z0-9]+$")) {
          Alert alert = new Alert (AlertType.ERROR);
          alert.setHeaderText("Failed to Register");
          alert.setContentText("Password must be alphanumeric");
          alert.showAndWait();

      //password must be at least 5 characters  
      } else if(passwordPF.getLength() < 5)	{
          Alert alert = new Alert (AlertType.ERROR);
          alert.setHeaderText("Failed to Register");
          alert.setContentText("Password must be at least 5 characters");
          alert.showAndWait();

      //confirm password must equals to password   
      } else if (!confirmpasswordPF.getText().equals(passwordPF.getText())){
          Alert alert = new Alert (AlertType.ERROR);
          alert.setHeaderText("Failed to Register");
          alert.setContentText("Please re-confirm password");
          alert.showAndWait();

      //phone number must be numeric & must start with '+62'
      } else if(!phonenumberTF.getText().matches("^\\+62[0-9]+$")){
          Alert alert = new Alert (AlertType.ERROR);
          alert.setHeaderText("Failed to Register");
          alert.setContentText("Phone number must be numeric and starts with '+62'");
          alert.showAndWait();

      // username must be unique
      } else if(checkUsername() == false){
        Alert alert = new Alert (AlertType.ERROR);
        alert.setHeaderText("Failed to Register");
          alert.setContentText("Username must be unique");
          alert.showAndWait();

      //success regist
      } else {
        addUserData(inpUsername, inpPassword, inpAddress, inpPhone_num, inpGender);
        Alert alert = new Alert(AlertType.INFORMATION);      
        alert.setTitle("Success");
        alert.setHeaderText("Registered Successfully!");
        alert.showAndWait();

       openLogin(new Login());
      }
    });

    loginLbl.setOnMouseClicked(e -> {
      openLogin(new Login());
    });
  }

  private void openLogin(Application app) {
      Stage stage = (Stage) scene.getWindow();
      try {
        app.start(stage);
      } catch (Exception e) {
        e.printStackTrace();
    }	      
 }

  private boolean checkUsername() {
    for (User us : user) {
    if (us.getUsername().equals(inpUsername)) {
      return false;
    }
    }
    return true;
  }

  public void addUserData(String username, String password, String address, String phone_num, String gender) {
    id = String.format("CU%03d", count);
    for (User us : user) {
    while (us.getUserID().equals(id)) {
      count++;
      id = String.format("CU%03d", count);
    }
    }
    id = String.format("CU%03d", count);
    role = "User";
    String query = String.format("INSERT INTO user VALUES ('%s','%s','%s','%s','%s','%s','%s')", id, username, password, role, address, phone_num, gender);
    connect.execUpdate(query); 

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
        user.add(new User(id, username, password, role, address, phone_num, gender));
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
    primaryStage.setTitle("Register");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
