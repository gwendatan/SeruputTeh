import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminNavbar extends BorderPane {
  Scene scene;
  MenuBar mb;
  Menu Home, ManageProducts, Account;
  MenuItem HomePage, ManageProductsItem, Logout;
  String username = " ";

  public AdminNavbar() {
    setMenu();
  }

  public void setName(String username) {
    this.username = username;
  }

  public void setMenu() {
    HomePage = new MenuItem("HomePage");
    Home = new Menu("Home", null, HomePage);

    ManageProductsItem = new MenuItem("Manage Products");
    ManageProducts = new Menu("Manage Products", null, ManageProductsItem);

    Logout = new MenuItem("Logout");
    Account = new Menu("Account", null, Logout);

    mb = new MenuBar();
    mb.getMenus().addAll(Home, ManageProducts, Account);

    setTop(mb);
  }

  public void setEventHandler() {
    HomePage.setOnAction(e -> {
      AdminHome ah = new AdminHome();
      ah.setName(username);
      navigateTo(ah);
    });

    ManageProductsItem.setOnAction(e -> {
      EditProduct ep = new EditProduct();
      ep.setName(username);
      navigateTo(ep);
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
