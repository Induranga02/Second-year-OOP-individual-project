import java.util.List;

public interface ShoppingManger {
    void addProduct(Product product);

    void deleteProduct(String productId);

    void printProducts();

    void saveProducts();
    void loadProducts();

}
