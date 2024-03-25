public class Electronics extends Product {
    private String brand;
    private int warrantyPeriod;
    public Electronics(String productId, String productName, int availableItems, double price, String brand, int warrantyPeriod) {
        super(productId, productName, availableItems, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }
    @Override
    public String getProductDetails() {
        return "Electronics - " + super.getProductName() + " | Brand: " + brand + " | Warranty: " + warrantyPeriod + " months";
    }
}

