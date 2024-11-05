package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.InventoryDao;
import model.Inventory_model;

public class Home {

    private JTable table;
    private DefaultTableModel tableModel;
    private InventoryDao dao;
    private JTextField searchField;
    private JButton searchButton;
  
////Home constructor/////////////////////////////////////////////
    public Home() {
        dao = new InventoryDao();
        JFrame frame = createFrame();
        JPanel buttonPanel = createButtonPanel();
        JPanel searchPanel = createSearchPanel();
        JScrollPane scrollPane = createTableScrollPane();
       
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(searchPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
   //////////// to check column count in default Mpodel////////////////     
       /* int columnCount = tableModel.getColumnCount();
        System.out.println("Column count: " + columnCount);*/
    }
///////////////////////jframe ////////////////////////////////
    private JFrame createFrame() {
        JFrame frame = new JFrame("INVENTORY MANAGEMENT SYSTEM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 550);
        frame.setLayout(new BorderLayout());
        return frame;
    }
/////////////////////// Buttonpanel///////////////////////
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton addButton = new JButton("Add");
        JButton searchButton = new JButton("Sell Medicine");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton purchaseButton = new JButton("Purchase");
       // JButton historyButton = new JButton("History");
        JButton showButton = new JButton("Refresh");

        
        addButton.addActionListener(e -> new AddProduct());
        updateButton.addActionListener(e -> updateSelectedRow());
        deleteButton.addActionListener(e -> deleteSelectedRow());
        showButton.addActionListener(e -> loadTableData());
        searchButton.addActionListener(e -> new SearchProduct());
        purchaseButton.addActionListener(e -> new PurchaseOrder());
       // historyButton.addActionListener(e -> new SellMedecine());
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(purchaseButton);
       // buttonPanel.add(historyButton);
        buttonPanel.add(showButton);

        return buttonPanel;
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
    ////////////////Table scrollpane/////////////////
    private JScrollPane createTableScrollPane() {
        String[] columnNames = {"ID", "Product Name", "Quantity", "Rate","Total Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        loadTableData();
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                	 // Show a message if no row is selected
                    JOptionPane.showMessageDialog(null, "No row selected", "Selection Error", JOptionPane.WARNING_MESSAGE);

                }
            }
        });

        return new JScrollPane(table);
    }
    
 
  
//////////////////////Load Data////////////////
    private void loadTableData() {
    	  // Clear existing rows
        tableModel.setRowCount(0);
        try {
            List<Inventory_model> products = dao.retrieveProducts();
            for (Inventory_model product : products) {
                tableModel.addRow(new Object[]{product.getProductId(), product.getProductName(), product.getProductQuantity(), product.getPerProductRate(),product.getProductTotalPrice()});
            }
        } catch (ClassNotFoundException | SQLException ex) {
        	 JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
       
  /////////////////////Update select Row////////////////////
    private void updateSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            int quantity = (int) tableModel.getValueAt(selectedRow, 2);
            int rate = (int) tableModel.getValueAt(selectedRow, 3);
            int totPrice = (int) tableModel.getValueAt(selectedRow, 4);

          new Update(id, name, quantity, rate,totPrice);
        } else {
            JOptionPane.showMessageDialog(null, "No row selected for update.");
        }
    }
///////////////////////////Delete Select Row///////////////////
      private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
        	   // Show confirmation dialog
            int response = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this row?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            // Check user's response
            if (response == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0); // Assuming ID is in the first column

            // Remove row from table
            tableModel.removeRow(selectedRow);

            // Remove row from the database
            try {
                try {
					dao.deleteProduct(id);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // Ensure you have a method for deleting a product in your InventoryDao
            } catch (SQLException ex) {
                ex.printStackTrace(); // Handle exception properly in production
            }
        } 
       }else {
            JOptionPane.showMessageDialog(null, "No row selected for deletion.");
        }
    }
/////////////////////////////Main Function///////////////////
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}
