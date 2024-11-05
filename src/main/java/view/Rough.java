/*package view;
//////////////////////////Normal search ///////////////////////////////////
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import controller.InventoryDao;
import model.Inventory_model;

public class SearchProduct {
    private JTextField searchField;
    private JButton searchButton;
    private JTable searchTable;
    private DefaultTableModel productTableModel;
    private InventoryDao dao;
    
    // Form fields for product details
    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField productQuantityField;
    private JTextField productRateField;
    private JTextField totalPriceField;

    // Cart table
    private JTable cartTable;
    private DefaultTableModel cartTableModel;

    public SearchProduct() {
        dao = new InventoryDao(); // Initialize InventoryDao
        JFrame frame = createFrame();
        JPanel searchPanel = createSearchPanel();
        JPanel formPanel = createFormPanel();
        JScrollPane productScrollPane = createProductTableScrollPane();
        JScrollPane cartScrollPane = createCartTableScrollPane();

        // Main panel to hold search, form, and cart
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(productScrollPane, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        mainPanel.add(cartScrollPane, BorderLayout.EAST);

        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Create JFrame
    private JFrame createFrame() {
        JFrame frame = new JFrame("SEARCH PANEL");
        frame.setSize(1000, 500);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    // Create search panel with field and button
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        panel.add(searchField);
        panel.add(searchButton);
        return panel;
    }

    // Create product table scroll pane
    private JScrollPane createProductTableScrollPane() {
        String[] columnNames = {"ID", "Product Name", "Quantity", "Rate", "Total Price"};
        productTableModel = new DefaultTableModel(columnNames, 0);
        searchTable = new JTable(productTableModel);
        loadTableData(); // Initial load of product data

        searchTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = searchTable.getSelectedRow();
                if (selectedRow != -1) {
                    populateForm(selectedRow);
                }
            }
        });

        return new JScrollPane(searchTable);
    }

    // Create form panel for adding to cart
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2));

        // Initialize form fields
        productIdField = new JTextField();
        productNameField = new JTextField();
        productQuantityField = new JTextField();
        productRateField = new JTextField();
        totalPriceField = new JTextField();
        JButton addToCartButton = new JButton("Add to Cart");

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

        // Add fields to the panel
        formPanel.add(new JLabel("Product ID:"));
        formPanel.add(productIdField);
        formPanel.add(new JLabel("Product Name:"));
        formPanel.add(productNameField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(productQuantityField);
        formPanel.add(new JLabel("Rate:"));
        formPanel.add(productRateField);
        formPanel.add(new JLabel("Total Price:"));
        formPanel.add(totalPriceField);
        formPanel.add(addToCartButton);

        return formPanel;
    }

    // Create cart table scroll pane
    private JScrollPane createCartTableScrollPane() {
        String[] cartColumnNames = {"Product Name", "Quantity", "Rate", "Total Price"};
        cartTableModel = new DefaultTableModel(cartColumnNames, 0);
        cartTable = new JTable(cartTableModel);
        return new JScrollPane(cartTable);
    }

    // Load product data from the database
    private void loadTableData() {
        productTableModel.setRowCount(0); // Clear existing rows
        try {
            List<Inventory_model> products = dao.retrieveProducts();
            for (Inventory_model product : products) {
                productTableModel.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductQuantity(),
                    product.getPerProductRate(),
                    product.getProductTotalPrice()
                });
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Perform search based on user input
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        productTableModel.setRowCount(0); // Clear existing rows

        if (searchText.isEmpty()) {
            // If search text is empty, reload all data
            loadTableData();
            return;
        }

        try {
            List<Inventory_model> products = dao.retrieveProducts(); // Get all products
            for (Inventory_model product : products) {
                if (product.getProductName().toLowerCase().contains(searchText)) {
                    productTableModel.addRow(new Object[]{
                        product.getProductId(),
                        product.getProductName(),
                        product.getProductQuantity(),
                        product.getPerProductRate(),
                        product.getProductTotalPrice()
                    });
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error performing search: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Populate form fields with selected product data
    private void populateForm(int rowIndex) {
        productIdField.setText(productTableModel.getValueAt(rowIndex, 0).toString());
        productNameField.setText(productTableModel.getValueAt(rowIndex, 1).toString());
        productQuantityField.setText(productTableModel.getValueAt(rowIndex, 2).toString());
        productRateField.setText(productTableModel.getValueAt(rowIndex, 3).toString());
        totalPriceField.setText(productTableModel.getValueAt(rowIndex, 4).toString());
    }

    // Add product to cart
    private void addToCart() {
        String productName = productNameField.getText();
        String quantity = productQuantityField.getText();
        String rate = productRateField.getText();
        String totalPrice = totalPriceField.getText();

        if (productName.isEmpty() || quantity.isEmpty() || rate.isEmpty() || totalPrice.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cartTableModel.addRow(new Object[]{productName, quantity, rate, totalPrice});
        clearForm(); // Clear form fields after adding to cart
    }

    // Clear form fields
    private void clearForm() {
        productIdField.setText("");
        productNameField.setText("");
        productQuantityField.setText("");
        productRateField.setText("");
        totalPriceField.setText("");
    }
}
*/