/*package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.InventoryDao;
import model.Inventory_model;

public class SearchProduct {
    private  JTextField searchField;
    private  JButton searchButton;
    private  JTable searchTable;
    private DefaultTableModel tableModel;
    private  InventoryDao dao;

    public SearchProduct() {
        dao = new InventoryDao(); // Initialize InventoryDao
        JFrame frame = createFrame();
        JPanel panel = createSearchPanel();
        JScrollPane scrollPane = createTableScrollPane();

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Create JFrame
    private JFrame createFrame() {
        JFrame frame = new JFrame("SEARCH PANEL");
        frame.setSize(800, 400);
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

    // Create table scroll pane
    private JScrollPane createTableScrollPane() {
        String[] columnNames = {"ID", "Product Name", "Quantity", "Rate", "Total Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        searchTable = new JTable(tableModel);
        loadTableData(); // Initial load of table data

        return new JScrollPane(searchTable);
    }

    // Load table data from the database
    private void loadTableData() {
        tableModel.setRowCount(0); // Clear existing rows
        try {
            List<Inventory_model> products = dao.retrieveProducts();
            for (Inventory_model product : products) {
                tableModel.addRow(new Object[]{
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
        tableModel.setRowCount(0); // Clear existing rows

        if (searchText.isEmpty()) {
            // If search text is empty, reload all data
            loadTableData();
            return;
        }

        try {
            List<Inventory_model> products = dao.retrieveProducts(); // Get all products
            for (Inventory_model product : products) {
                if (product.getProductName().toLowerCase().contains(searchText)) {
                    tableModel.addRow(new Object[]{
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
}
*/