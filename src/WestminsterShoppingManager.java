import java.io.*;
import java.util.*;
import javax.swing.*;

public class WestminsterShoppingManager implements ShoppingManger {
    private List<Product> productList;
    private Scanner scanner;
    private ShoppingGUI shoppingGUI;
    private ShoppingCart shoppingCart;


    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.shoppingCart = new ShoppingCart();
        this.shoppingGUI = new ShoppingGUI(this, this.shoppingCart);
    }
    @Override
    public void addProduct(Product product) {
        if (productList.size() < 50) {
            productList.add(product);
            System.out.println("Product added successfully.");
        } else {
            System.out.println("The system can have a maximum of 50 products. Cannot add more products.");
        }
    }

    @Override
    public void deleteProduct(String productId) {
        Product deletedProduct = null;
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                deletedProduct = product;
                break;
            }
        }
        if (deletedProduct != null) {
            productList.remove(deletedProduct);



            System.out.println("Product deleted successfully: " + deletedProduct.getProductDetails());
            System.out.println("Total number of products left in the system: " + productList.size());
        } else {
            System.out.println("Product not found with ID: " + productId);
        }
    }

    @Override
    public void printProducts() {

        Collections.sort(productList, Comparator.comparing(Product::getProductId));

        for (Product product : productList) {
            String productType;
            if (product instanceof Electronics) {
                productType = "Electronics";
            } else {
                productType = "Clothing";
            }
            System.out.println(productType + " - " + product.getProductDetails());
        }
    }
    @Override
    public void saveProducts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            for (Product product : productList) {
                String formattedProductDetails = formatProductDetails(product);
                writer.write(formattedProductDetails);
                writer.newLine();
            }
            System.out.println("Products saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String formatProductDetails(Product product) {
        StringBuilder formattedDetails = new StringBuilder();

        formattedDetails.append("Product ID: ").append(product.getProductId())
                .append(", Product Name: ").append(product.getProductName())
                .append(", Available Items: ").append(product.getAvailableItems())
                .append(", Price: ").append(product.getPrice());

        if (product instanceof Electronics) {
            Electronics electronicProduct = (Electronics) product;
            formattedDetails.append(", Brand: ").append(electronicProduct.getBrand())
                    .append(", Warranty Period: ").append(electronicProduct.getWarrantyPeriod());
        } else if (product instanceof Clothing) {
            Clothing clothingProduct = (Clothing) product;
            formattedDetails.append(", Size: ").append(clothingProduct.getSize())
                    .append(", Color: ").append(clothingProduct.getColor());
        }
        return formattedDetails.toString();
    }
    @Override
    public void loadProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product loadedProduct = parseProductDetails(line);
                if (loadedProduct != null) {
                    productList.add(loadedProduct);
                }
            }
            System.out.println("Products loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading products from file.");
            e.printStackTrace();
        }
    }
    private Product parseProductDetails(String line) {
        String[] parts = line.split(", ");
        if (parts.length >= 4) {
            String productId = getValue(parts[0]);
            String productName = getValue(parts[1]);
            int availableItems = Integer.parseInt(getValue(parts[2]));
            double price = Double.parseDouble(getValue(parts[3]));

            if (line.contains(", Brand: ")) {
                String brand = getValue(parts[4]);
                int warrantyPeriod = Integer.parseInt(getValue(parts[5]));

                return new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
            } else if (line.contains(", Size: ")) {
                String size = getValue(parts[4]);
                String color = getValue(parts[5]);

                return new Clothing(productId, productName, availableItems, price, size, color);
            }
        }
        return null;
    }
    private String getValue(String part) {
        return part.substring(part.indexOf(":") + 2);
    }
    public void showConsoleMenu() {
        while (true) {
            System.out.println("\n-------- Console Menu --------");
            System.out.println("1. Add a new product to the system");
            System.out.println("2. Delete a product from the system");
            System.out.println("3. Print the list of products in the system");
            System.out.println("4. Save products to file");
            System.out.println("5. Load products from file");
            System.out.println("6. Open GUI");
            System.out.println("7. Exit");

            System.out.print("Enter your choice (1-7): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    addNewProduct(scanner);
                    break;
                case 2:
                    deleteProduct(scanner);
                    break;
                case 3:
                    printProducts();
                    break;
                case 4:
                    saveProducts();
                    break;
                case 5:
                    loadProducts();
                    break;
                case 6:
                    openShoppingGUI();
                    break;
                case 7:
                    System.out.println("Exiting the console. Goodbye!");
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }
    public void addNewProduct(Scanner scanner) {
        System.out.println("Adding a new product to the system...");

        // Ask the user for common product details
        System.out.print("Enter product ID: ");
        String productId = scanner.nextLine();

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter available items: ");
        int availableItems = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        // Ask the user for product type (Electronics or Clothing)
        System.out.print("Enter product type (1 for Electronics, 2 for Clothing): ");
        int productTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Based on the product type, ask for additional details and create the product instance
        Product newProduct = null;

        switch (productTypeChoice) {
            case 1:
                // Electronics
                System.out.print("Enter brand: ");
                String brand = scanner.nextLine();

                System.out.print("Enter warranty period (in months): ");
                int warrantyPeriod = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                newProduct = new Electronics(productId, productName, availableItems, price, brand, warrantyPeriod);
                break;

            case 2:
                // Clothing
                System.out.print("Enter size: ");
                String size = scanner.nextLine();

                System.out.print("Enter color: ");
                String color = scanner.nextLine();

                newProduct = new Clothing(productId, productName, availableItems, price, size, color);
                break;

            default:
                System.out.println("Invalid product type choice. Product not added.");
                return;
        }
        // Add the new product to the system
        addProduct(newProduct);
        System.out.println("Product added successfully: " + newProduct.getProductDetails());
    }

    public void deleteProduct(Scanner scanner) {
        System.out.print("Enter the product ID to delete: ");
        String productId = scanner.nextLine();
        deleteProduct(productId);
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void addProductToCartById(String productId) {
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                shoppingCart.addProduct(product);
                System.out.println("Product added to the shopping cart: " + product.getProductDetails());
                return;
            }
        }
        System.out.println("Product not found with ID: " + productId);
    }

    public Product getProductById(String productId) {
        // Iterate through the product list and find the product by ID
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null; // Return null if the product is not found
    }
    private void openShoppingGUI() {
        SwingUtilities.invokeLater(() -> {
            ShoppingGUI shoppingGUI = new ShoppingGUI(this,this.shoppingCart);
            shoppingGUI.setVisible(true);
        });
    }

}






