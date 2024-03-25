import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class ShoppingCart {
    public List<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }
    public void addProduct(Product product) {
        products.add(product);
    }
    public void removeProduct(Product product) {
        products.remove(product);
    }
    public List<Product> getProducts() {
        return products;
    }
    public double calculateTotalCost() {
        double totalCost = 0;
        for (Product product : products) {
            totalCost += product.getPrice();
        }
        return totalCost;
    }
    public void shoppingItems(ShoppingCart shoppingCart) {
        System.out.println("Products in the shopping cart:");
        for (Product product : shoppingCart.getProducts()) {
            System.out.println(product.getProductDetails());
        }
    }
    public void totalCost(ShoppingCart shoppingCart) {
        System.out.println("Total cost: $" + shoppingCart.calculateTotalCost());
    }

    public void addProductById(WestminsterShoppingManager shoppingManager, String productId) {
        for (Product product : shoppingManager.getProductList()) {
            if (product.getProductId().equals(productId)) {
                addProduct(product);
                return;
            }
        }
        System.out.println("Product not found with ID: " + productId);
    }

    public void removeProductById(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                removeProduct(product);
                return;
            }
        }
        System.out.println("Product not found with ID: " + productId);
    }
}


