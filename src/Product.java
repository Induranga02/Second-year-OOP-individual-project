
public abstract class Product {
    private String productId;
    private String productName;
    private int availableItems;
    private double price;

    public Product(String productId,String productName,int availableItems,double price){
        this.productId=productId;
        this.productName=productName;
        this.availableItems=availableItems;
        this.price=price;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductName() {
        return productName;
    }
    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }
    public int getAvailableItems() {
        return availableItems;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    }
    public abstract String getProductDetails();

}
