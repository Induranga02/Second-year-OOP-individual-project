import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

public class ShoppingCartGUI extends JFrame {
    private ShoppingCart shoppingCart;
    private DefaultListModel<Product> cartListModel;
    private JList<Product> cartList;
    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private JLabel totalLabel;
    private JLabel finalPriceLabel;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private WestminsterShoppingManager shoppingManager;

    public ShoppingCartGUI(ShoppingCart shoppingCart, JTable productTable, DefaultTableModel tableModel, WestminsterShoppingManager shoppingManager) {
        this.shoppingCart = shoppingCart;
        this.productTable = productTable;
        this.tableModel = tableModel;
        this.shoppingManager = shoppingManager;

        setTitle("Shopping Cart");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        cartListModel = new DefaultListModel<>();
        cartList = new JList<>(cartListModel);
        finalPriceLabel = new JLabel("Final Price: $0.00");

        // Initialize the cart table model with columns: Product, Quantity, Price
        cartTableModel = new DefaultTableModel(new Object[]{"Product", "Quantity", "Price"}, 0);
        cartTable = new JTable(cartTableModel);

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        JButton addToCartButton = new JButton("Add to Cart");

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedProductToCart();
                updateCart();
                updateFinalPrice();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(cartScrollPane, BorderLayout.CENTER);
        mainPanel.add(finalPriceLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addToCartButton);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        updateCart();
        updateFinalPrice();
    }

    private void addSelectedProductToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) tableModel.getValueAt(selectedRow, 0);
            shoppingCart.addProductById(shoppingManager, productId);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to add to the cart.");
        }
    }

    private void updateCart() {
        // Clear existing data in the cart table
        cartTableModel.setRowCount(0);

        List<Product> cartProducts = shoppingCart.getProducts();
        for (Product product : cartProducts) {
            Object[] rowData = {product.getProductId(), product.getProductName(), product.getPrice()};
            cartTableModel.addRow(rowData);
        }
    }

    private void updateFinalPrice() {
        DecimalFormat df = new DecimalFormat("#0.00");
        finalPriceLabel.setText("Final Price: $" + df.format(shoppingCart.calculateTotalCost()));
    }
}
