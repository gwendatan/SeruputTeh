public class HistoryData {

  private String transactionID;
  private String userID;
  private String productName;
  private Integer quantity;
  private Double price;
  private Double total;

  public HistoryData(String transactionID, String userID, String productName, Integer quantity, Double price,
      Double total) {
    super();
    this.transactionID = transactionID;
    this.userID = userID;
    this.productName = productName;
    this.quantity = quantity;
    this.price = price;
    this.total = total;
  }

  public String getTransactionID() {
    return transactionID;
  }

  public void setTransactionID(String transactionID) {
    this.transactionID = transactionID;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Double getTotal() {
    return total;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  @Override
  public String toString() {
    String temp;
    temp = String.format("%dx %s (Rp.% ,.0f)", getQuantity(), getProductName(), getTotal());
    return temp;
  }
}
