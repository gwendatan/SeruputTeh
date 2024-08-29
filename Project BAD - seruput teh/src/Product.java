public class Product {

  private String productID;
  private String product_name;
  private Double product_price;
  private String product_des;

  public Product(String productID, String product_name, Double product_price, String product_des) {
    this.productID = productID;
    this.product_name = product_name;
    this.product_price = product_price;
    this.product_des = product_des;
  }

  public String getProductID() {
    return productID;
  }

  public void setProductID(String productID) {
    this.productID = productID;
  }

  public String getProduct_name() {
    return product_name;
  }

  public void setProduct_name(String product_name) {
    this.product_name = product_name;
  }

  public Double getProduct_price() {
    return product_price;
  }

  public void setProduct_price(Double product_price) {
    this.product_price = product_price;
  }

  public String getProduct_des() {
    return product_des;
  }

  public void setProduct_des(String product_des) {
    this.product_des = product_des;
  }

  
  @Override
  public String toString() {
    return product_name;
  }

}
