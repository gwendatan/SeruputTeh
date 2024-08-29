public class CartData {

    private String productID;
    private String userID;
    private Integer quantity;
    private String productName;
    private String productDes;
    private Double productPrice;

    public CartData(String productID, String userID, Integer quantity, String productName, String productDes,
        Double productPrice) {
      super();
      this.productID = productID;
      this.userID = userID;
      this.quantity = quantity;
      this.productName = productName;
      this.productDes = productDes;
      this.productPrice = productPrice;
    }

    public String getProductID() {
      return productID;
    }

    public void setProductID(String productID) {
      this.productID = productID;
    }

    public String getUserID() {
      return userID;
    }

    public void setUserID(String userID) {
      this.userID = userID;
    }

    public Integer getQuantity() {
      return quantity;
    }

    public void setQuantity(Integer quantity) {
      this.quantity = quantity;
    }

    public String getProductName() {
      return productName;
    }

    public void setProductName(String productName) {
      this.productName = productName;
    }

    public String getProductDes() {
      return productDes;
    }

    public void setProductDes(String productDes) {
      this.productDes = productDes;
    }

    public Double getProductPrice() {
      return productPrice;
    }

    public void setProductPrice(Double productPrice) {
      this.productPrice = productPrice;
    }

    @Override
    public String toString() {
      String temp;
      temp = String.format("%dx %s (Rp.%.0f)", getQuantity(), getProductName(),getProductPrice()*getQuantity());
      return temp;
    }

  }
