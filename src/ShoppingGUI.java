import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShoppingGUI extends JFrame {
    private WestminsterShoppingManager shoppingManager;
    private ShoppingCart shoppingCart;

    private JComboBox<String> productTypeDropdown;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton addToCartButton;
    private JButton shoppingCartButton; // New button for opening ShoppingCartGUI
    private JPanel productDetailsPanel;

    public ShoppingGUI(WestminsterShoppingManager shoppingManager,ShoppingCart shoppingCart) {
        this.shoppingManager = shoppingManager;
        this.shoppingCart=shoppingCart;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Westminster Shopping Centre");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Create components
        productTypeDropdown = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Type", "Price", "Available Items", "Additional Info"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        productTable = new JTable(tableModel);
        addToCartButton = new JButton("Add to Cart");
        shoppingCartButton = new JButton("Shopping Cart"); // New button for opening ShoppingCartGUI
        productDetailsPanel = new JPanel();

        // Create main panel using BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Dropdown and ShoppingCart panel
        JPanel dropdownAndCartPanel = createDropdownAndCartPanel();
        mainPanel.add(dropdownAndCartPanel);

        // Table scroll pane
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        mainPanel.add(tableScrollPane);

        // Product details panel
        JPanel detailsWrapperPanel = new JPanel();
        detailsWrapperPanel.setLayout(new BoxLayout(detailsWrapperPanel, BoxLayout.X_AXIS));
        detailsWrapperPanel.add(Box.createHorizontalStrut(10)); // Add some horizontal space

        detailsWrapperPanel.add(productDetailsPanel);
        mainPanel.add(detailsWrapperPanel);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel);

        // Add main panel to the frame
        add(mainPanel);

        // Set up event listeners
        productTypeDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTableData();
                clearProductDetails();
            }
        });

        // Add an action listener to the "Add to Cart" button
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedProductToCart();
            }
        });

        // Add an action listener to the "Shopping Cart" button
        shoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openShoppingCartGUI();
            }
        });

        // Add a list selection listener to the table
        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                displayProductDetails(selectedRow);
            }
        });

        // Set initial data
        updateTableData();
    }

    private JPanel createDropdownAndCartPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        JPanel dropdownPanel = new JPanel();
        dropdownPanel.add(new JLabel("Select Product Type: "));
        dropdownPanel.add(productTypeDropdown);
        dropdownPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(shoppingCartButton);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(1)); // Add some vertical space
        panel.add(dropdownPanel);

        return panel;
    }


    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.add(addToCartButton);
        return panel;
    }

    private void displayProductDetails(int selectedRow) {
        productDetailsPanel.removeAll();
        productDetailsPanel.setLayout(new GridLayout(0, 2));

        String productID = (String) tableModel.getValueAt(selectedRow, 0);
        String productName = (String) tableModel.getValueAt(selectedRow, 1);
        String productType = (String) tableModel.getValueAt(selectedRow, 2);
        double productPrice = (double) tableModel.getValueAt(selectedRow, 3);
        int availableItems = (int) tableModel.getValueAt(selectedRow, 4);
        String additionalInfo = (String) tableModel.getValueAt(selectedRow, 5);

        JLabel idLabel = new JLabel("Product ID:");
        JLabel nameLabel = new JLabel("Product Name:");
        JLabel typeLabel = new JLabel("Product Type:");
        JLabel priceLabel = new JLabel("Product Price:");
        JLabel availableItemsLabel = new JLabel("Available Items:");
        JLabel additionalInfoLabel = new JLabel("Additional Info:");

        JLabel idValueLabel = new JLabel(productID);
        JLabel nameValueLabel = new JLabel(productName);
        JLabel typeValueLabel = new JLabel(productType);
        JLabel priceValueLabel = new JLabel(String.valueOf(productPrice));
        JLabel availableItemsValueLabel = new JLabel(String.valueOf(availableItems));
        JLabel additionalInfoValueLabel = new JLabel(additionalInfo);

        productDetailsPanel.add(idLabel);
        productDetailsPanel.add(idValueLabel);
        productDetailsPanel.add(nameLabel);
        productDetailsPanel.add(nameValueLabel);
        productDetailsPanel.add(typeLabel);
        productDetailsPanel.add(typeValueLabel);
        productDetailsPanel.add(priceLabel);
        productDetailsPanel.add(priceValueLabel);
        productDetailsPanel.add(availableItemsLabel);
        productDetailsPanel.add(availableItemsValueLabel);
        productDetailsPanel.add(additionalInfoLabel);
        productDetailsPanel.add(additionalInfoValueLabel);

        productDetailsPanel.revalidate();
        productDetailsPanel.repaint();
    }

    private void clearProductDetails() {
        productDetailsPanel.removeAll();
        productDetailsPanel.revalidate();
        productDetailsPanel.repaint();
    }

    private void updateTableData() {
        // Clear existing data
        tableModel.setRowCount(0);

        // Get selected product type
        String selectedType = (String) productTypeDropdown.getSelectedItem();

        // Set up cell renderer for highlighting low available items in red
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int availableItemsColumnIndex = table.getColumnModel().getColumnIndex("Available Items");
                int availableItems = (int) table.getModel().getValueAt(row, availableItemsColumnIndex);
                if (availableItems < 3) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }
                return c;
            }
        };

        // Apply cell renderer to all columns
        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Populate the table with product data
        for (Product product : shoppingManager.getProductList()) {
            if ("All".equals(selectedType) || (product instanceof Electronics && "Electronics".equals(selectedType))
                    || (product instanceof Clothing && "Clothing".equals(selectedType))) {
                Object[] rowData = {
                        product.getProductId(),
                        product.getProductName(),
                        product.getClass().getSimpleName(),
                        product.getPrice(),
                        product.getAvailableItems(),
                        getAdditionalInfo(product)
                };
                tableModel.addRow(rowData);
            }
        }
    }
    private String getAdditionalInfo(Product product) {
        if (product instanceof Electronics) {
            Electronics electronicProduct = (Electronics) product;
            return "Brand: " + electronicProduct.getBrand() + ", Warranty: " + electronicProduct.getWarrantyPeriod() + " months";
        } else if (product instanceof Clothing) {
            Clothing clothingProduct = (Clothing) product;
            return "Size: " + clothingProduct.getSize() + ", Color: " + clothingProduct.getColor();
        } else {
            return "";
        }
    }

    private void addSelectedProductToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) tableModel.getValueAt(selectedRow, 0);
            shoppingManager.addProductToCartById(productId);
            JOptionPane.showMessageDialog(this, "Product added to cart.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to add to the cart.");
        }
    }
    private void openShoppingCartGUI() {
        // Open the ShoppingCartGUI
        ShoppingCartGUI cartGUI = new ShoppingCartGUI(shoppingCart,productTable,tableModel,shoppingManager);
        cartGUI.setVisible(true);
    }

}



