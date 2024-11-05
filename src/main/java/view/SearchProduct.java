package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.InventoryDao;
import model.Inventory_model;

public class SearchProduct {
    private JTextField searchField, orderByNameField,orderDateField,totalAmountField,cashField,returnCashField;
    private JButton searchButton;
    private JTable searchTable;
    private DefaultTableModel productTableModel;
    private InventoryDao dao;

    // Cart table
    private JTable cartTable;
    private DefaultTableModel cartTableModel;

    public SearchProduct() {
        dao = new InventoryDao(); // Initialize InventoryDao
        JFrame frame = createFrame();
        JPanel searchPanel = createSearchPanel();
        JScrollPane productScrollPane = createProductTableScrollPane();
        JScrollPane cartScrollPane = createCartTableScrollPane();
        JPanel paymentPanel = createPaymentPanel();
        // Main panel to hold search, form, and cart
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(productScrollPane, BorderLayout.WEST);
        mainPanel.add(cartScrollPane, BorderLayout.CENTER);
        mainPanel.add(paymentPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
 
  

	// Create JFrame
    private JFrame createFrame() {
        JFrame frame = new JFrame("SELL MEDICINE");
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    // Create search panel with field and button
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
       panel.setLayout(new BorderLayout());
       
       // Create a sub-panel for the order fields
       JPanel orderPanel = new JPanel();
       orderPanel.setLayout(new GridLayout(1, 2)); // 1 row, 2 columns


        // Order By Name Text Field and Label
        JLabel orderByNameLabel = new JLabel("Order By Name:");
        orderByNameField = new JTextField(15);

        // Order By Date Button
        JLabel orderDateLabel = new JLabel("Order Date:");
        orderDateField = new JTextField(15);
        
        // Add order fields to the order panel
        orderPanel.add(orderByNameLabel);
        orderPanel.add(orderByNameField);
        orderPanel.add(orderDateLabel);
        orderPanel.add(orderDateField);
        
        // Create a panel for the search field and button
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        // Search Field and Button 
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
     // Add search components to the search panel
        searchPanel.add(new JLabel("Search:")); // Label for search field
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add the order panel and search panel to the main panel
        panel.add(orderPanel, BorderLayout.NORTH);
        panel.add(searchPanel, BorderLayout.WEST);
       
        return panel;
    }
    

    // Create product table scroll pane
    private JScrollPane createProductTableScrollPane() {
        String[] columnNames = {"Product Name"};
        productTableModel = new DefaultTableModel(columnNames, 0);
        searchTable = new JTable(productTableModel);
        loadTableData(); // Initial load of product data
     
        searchTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = searchTable.getSelectedRow();
                if (selectedRow != -1) {
                    String productName = productTableModel.getValueAt(selectedRow, 0).toString();
                    openAddToCartForm(productName);
                }
            }
        });

        return new JScrollPane(searchTable);
    }

    private AddToCartForm currentAddToCartForm; // Add to cart form reference

    private void openAddToCartForm(String productName) {
        try {
            Inventory_model product = dao.getProductByName(productName); // Fetch complete product details
            if (currentAddToCartForm == null || !currentAddToCartForm.isDisplayable()) {
                currentAddToCartForm = new AddToCartForm(cartTableModel, 
                    product.getProductId(), 
                    product.getProductName(), 
                    product.getProductQuantity(), 
                    product.getPerProductRate(), 
                    product.getProductTotalPrice(),this);
            } else {
                currentAddToCartForm.toFront(); // Bring to front if already open
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error retrieving product details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
                productTableModel.addRow(new Object[]{ product.getProductName() }); // Only show product name
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateTotalAmount() {
        double totalAmount = 0.0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            totalAmount += (double) cartTableModel.getValueAt(i, 3); // Assuming Total Price is in column index 3
        }
       
		totalAmountField.setText(String.format("%.2f", totalAmount)); // Update totalAmountField
    }
    // Perform search based on user input
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        productTableModel.setRowCount(0); // Clear existing rows

        if (searchText.isEmpty()) {
            loadTableData(); // If search text is empty, reload all data
            return;
        }

        try {
            List<Inventory_model> products = dao.retrieveProducts(); // Get all products
            for (Inventory_model product : products) {
                if (product.getProductName().toLowerCase().contains(searchText)) {
                    productTableModel.addRow(new Object[]{ product.getProductName() }); // Only show product name
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error performing search: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    ///payment panel
    private JPanel createPaymentPanel() {
    	  JPanel paymentPanel = new JPanel();
          paymentPanel.setLayout(new GridLayout(6, 2)); // Use a grid layout for better organization

    	double change =calculateChange();

    	// Convert change to a formatted string
    	String cashChange = String.format("%.2f", change); // Format to two decimal places
      
        // Total Amount Label and Field
        JLabel totalAmountLabel = new JLabel("Total Amount:");
        totalAmountField = new JTextField(20);
        totalAmountField.setEditable(false); // Make it read-only for display purposes

        // Cash Label and Field
        JLabel cashLabel = new JLabel("Cash:");
         cashField = new JTextField(20);

        // Return Cash Label and Field
        JLabel returnCashLabel = new JLabel("Return Cash:");
        returnCashField = new JTextField(20);
        returnCashField.setEditable(false); // Make it read-only for display purposes
       
        // Payment Method Panel
        JPanel paymentMethodPanel = new JPanel();
        paymentMethodPanel.setLayout(new FlowLayout()); // Use FlowLayout for horizontal arrangement

        // Payment Method Label
        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        JButton qrCodeButton = new JButton("Pay with QR Code");

        // Add components to the payment method panel
        paymentMethodPanel.add(paymentMethodLabel);
        paymentMethodPanel.add(qrCodeButton);
        paymentMethodPanel.add(cashLabel);
        paymentMethodPanel.add(cashField);
        paymentMethodPanel.add(returnCashLabel);
        paymentMethodPanel.add(returnCashField);

        // Add DocumentListener to cashField
        cashField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	calculateChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	calculateChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            	calculateChange();
            }
        });

        // Purchase Order Button
        JButton purchaseOrderButton = new JButton("Purchase Order");
        // Action listener for purchase order button
        purchaseOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	  try {
					insertOrder();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // Call the method to insert order details
                // Implement purchase order logic here
                JOptionPane.showMessageDialog(paymentPanel, "Purchase Order Placed!");
            }
        });

        // Go Back Button
        JButton goBackButton = new JButton("Go Back");
        // Action listener for go back button
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement go back logic here
                JOptionPane.showMessageDialog(paymentPanel, "Going back to previous menu.");
            }
        });

        // Add components to the main panel
        paymentPanel.add(totalAmountLabel);
        paymentPanel.add(totalAmountField);
      //  paymentPanel.add(calculateChangeButton); // Add the button to calculate change

        paymentPanel.add(paymentMethodPanel); // Add payment method panel
        paymentPanel.add(new JLabel()); // Placeholder for alignment
        paymentPanel.add(purchaseOrderButton);
        paymentPanel.add(goBackButton);
        

        return paymentPanel;
    }
    
    
    	private double calculateChange() {
    		  JTextComponent outputArea = new JTextArea(5,20);
    		   double change = 0.0; // Initialize change variable
    		   // Check for null values before proceeding
    		    if (cashField == null) {
    		        System.out.println("cashField is null");
    		        return change;
    		    }
    		    
    		    if (totalAmountField == null) {
    		        System.out.println("totalAmountField is null");
    		        return change;
    		    }
			try {
    	        // Get the cash given and total due amounts from the text fields
    	        double totalAmount= Double.parseDouble(totalAmountField.getText()); // Correctly retrieves the string and parses it
    	        double givenCash = Double.parseDouble(cashField.getText()); // Same here

    	        // Check if the given cash is less than the total amount due
    	        if (givenCash < totalAmount) {
    	             outputArea.setText("Cash given is less than total due. Please provide enough cash.");
    	        } else {
    	            // Calculate the change to return
    	             change = givenCash - totalAmount;
    	            outputArea.setText("Change to return: $" + change);
    	        }
    	        returnCashField.setText(String.format("%.2f", change)); // Update the return cash field

    	    } catch (NumberFormatException e) {
    	    		 outputArea.setText("Invalid input. Please enter numeric values.");
    	    }
			
			return change;
    	}
    	
    	private void insertOrder() throws SQLException, ClassNotFoundException {
    	    // Gather necessary data
    	    String orderByName = orderByNameField.getText().trim();
    	    String orderDate = orderDateField.getText().trim(); // Assuming it's in a valid format
    	    double totalAmount = Double.parseDouble(totalAmountField.getText().trim());

    	    for (int i = 0; i < cartTableModel.getRowCount(); i++) {
    	        String productName = (String) cartTableModel.getValueAt(i, 0);
    	        String quantityStr= cartTableModel.getValueAt(i, 1).toString();
    	        int quantity = Integer.parseInt(quantityStr);
    	        double rate = (double) cartTableModel.getValueAt(i, 2);
    	        double totalPrice = (double) cartTableModel.getValueAt(i, 3);

    	        // Assuming InventoryDao has an insertOrder method
    	        dao.insertOrder(orderByName, orderDate, productName, quantity, rate, totalPrice, totalAmount);
    	    }
    	}

    
   
}
