package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.Inventory_model;
import controller.InventoryDao;

public class SellMedecine {
    private JTextField searchField, orderByNameField, orderDateField, totalAmountField, cashField, returnCashField;
    private JButton searchButton, purchaseOrderButton, goBackButton, qrCodeButton;
    private JTable searchTable, cartTable;
    private DefaultTableModel productTableModel, cartTableModel;
    private InventoryDao dao;

    public SellMedecine() {
        dao = new InventoryDao();
        JFrame frame = createFrame();
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Search Panel
        JPanel searchPanel = createSearchPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(searchPanel, gbc);

        // Product Table
        JScrollPane productScrollPane = createProductTableScrollPane();
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(productScrollPane, gbc);

        // Cart Table
        JScrollPane cartScrollPane = createCartTableScrollPane();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(cartScrollPane, gbc);

        // Payment Panel
        JPanel paymentPanel = createPaymentPanel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span across two columns
        gbc.weighty = 0;
        mainPanel.add(paymentPanel, gbc);

        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame("Sell Medicine");
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Search Products"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        JLabel searchLabel = new JLabel("Search:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(searchLabel, gbc);

        searchField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(searchField, gbc);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        gbc.gridx = 2;
        panel.add(searchButton, gbc);

        // Order By Section
        JPanel orderPanel = new JPanel(new GridLayout(2, 2));
        orderPanel.setBorder(BorderFactory.createTitledBorder("Order By"));
        
        orderByNameField = new JTextField(10);
        orderDateField = new JTextField(10);
        
        orderPanel.add(new JLabel("Order By Name:"));
        orderPanel.add(orderByNameField);
        orderPanel.add(new JLabel("Order Date:"));
        orderPanel.add(orderDateField);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3; // Span across all columns
        panel.add(orderPanel, gbc);

        return panel;
    }

    private JScrollPane createProductTableScrollPane() {
        String[] columnNames = {"Product Name"};
        productTableModel = new DefaultTableModel(columnNames, 0);
        searchTable = new JTable(productTableModel);
        loadTableData();

        searchTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = searchTable.getSelectedRow();
                if (selectedRow != -1) {
                    String productName = productTableModel.getValueAt(selectedRow, 0).toString();
                    openAddToCartForm(productName);
                }
            }
        });

        return new JScrollPane(searchTable);
    }

    private JScrollPane createCartTableScrollPane() {
        String[] cartColumnNames = {"Product Name", "Quantity", "Rate", "Total Price"};
        cartTableModel = new DefaultTableModel(cartColumnNames, 0);
        cartTable = new JTable(cartTableModel);
        return new JScrollPane(cartTable);
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Payment"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        totalAmountField = new JTextField(15);
        totalAmountField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        panel.add(totalAmountField, gbc);

        cashField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Cash:"), gbc);
        gbc.gridx = 1;
        panel.add(cashField, gbc);

        returnCashField = new JTextField(15);
        returnCashField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Return Cash:"), gbc);
        gbc.gridx = 1;
        panel.add(returnCashField, gbc);

        qrCodeButton = new JButton("Pay with QR Code");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(qrCodeButton, gbc);

        purchaseOrderButton = new JButton("Purchase Order");
        purchaseOrderButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Purchase Order Placed!"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(purchaseOrderButton, gbc);

        goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Going back to previous menu."));
        gbc.gridx = 1;
        panel.add(goBackButton, gbc);

        return panel;
    }

    private void loadTableData() {
        productTableModel.setRowCount(0);
        try {
            List<Inventory_model> products = dao.retrieveProducts();
            for (Inventory_model product : products) {
                productTableModel.addRow(new Object[]{product.getProductName()});
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        productTableModel.setRowCount(0);
        if (searchText.isEmpty()) {
            loadTableData();
            return;
        }
        try {
            List<Inventory_model> products = dao.retrieveProducts();
            for (Inventory_model product : products) {
                if (product.getProductName().toLowerCase().contains(searchText)) {
                    productTableModel.addRow(new Object[]{product.getProductName()});
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error performing search: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddToCartForm(String productName) {
        // Implement your logic to open the AddToCartForm
    }

    // Other methods remain unchanged
}
