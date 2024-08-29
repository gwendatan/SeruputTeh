import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Navbar extends BorderPane {
  Scene scene;
  MenuBar mb;
  Menu Home, Cart, Account;
  MenuItem HomePage, MyCart, PurchaseHistory, Logout;
  String username = " ";
  String tempId = " ";

  public Navbar() {
    setMenu();
  }

  public void setName(String username) {
  this.username = username;
  }

  public void setUserId(String tempId) {
  this.tempId = tempId;
  }

  public void setMenu() {
    HomePage = new MenuItem("HomePage");
    Home = new Menu("Home", null, HomePage);

    MyCart = new MenuItem("MyCart");
    Cart = new Menu("Cart", null, MyCart);

    PurchaseHistory = new MenuItem("PurchaseHistory");
    Logout = new MenuItem("Logout");
    Account = new Menu("Account", null, PurchaseHistory, Logout);

    mb = new MenuBar();
    mb.getMenus().addAll(Home, Cart, Account);

    setTop(mb);
  }


  public void setEventHandler() {
    HomePage.setOnAction(e -> {
      CustomerHome ch = new CustomerHome();
      ch.setName(username);
      navigateTo(ch);
    });

    MyCart.setOnAction(e -> {
      Cart c = new Cart();
      c.setName(username);
      navigateTo(c);
    });

    PurchaseHistory.setOnAction(e -> {
      Transaction t = new Transaction();
      t.setName(username);
      navigateTo(t);
    });

    Logout.setOnAction(e -> navigateTo(new Login()));
  }

  private void navigateTo(Application app) {
    Stage stage = (Stage) getScene().getWindow();
    try {
      app.start(stage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
