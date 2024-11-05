package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import controller.InventoryDao;
import model.Cart_model;

public class PurchaseOrder {
	  private JTable table;
	    private DefaultTableModel tableModel;
	    private JTextField searchField;
	    private JButton searchButton;
	    private InventoryDao dao;
	public PurchaseOrder(){
		  JFrame frame = createFrame();
		  JPanel searchPanel = createSearchPanel();
	      JScrollPane scrollPane = createTableScrollPane();
		  
		  frame.add(searchPanel, BorderLayout.CENTER);
	      frame.add(scrollPane, BorderLayout.SOUTH);

	      frame.setVisible(true);
	      frame.setLocationRelativeTo(null);
	}
	
	///////////////////////jframe ////////////////////////////////
	private JFrame createFrame() {
		JFrame frame = new JFrame("PURCHASE ORDER");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(820, 550);
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
            List<Cart_model> products = dao.retrieveOders(); // Get all products
            for (Cart_model product : products) {
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
        String[] columnNames = {"Order ID","Customer Name ","Order Date", "Product Name", "Quantity", "Rate","Total Price","Total Amount"};
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
            List<Cart_model> orders = dao.retrieveOders();
            for (Cart_model product : orders) {
                tableModel.addRow(new Object[]{product.getOrderID(),product.getCust_name(), product.getUtilDate(), product.getProductName(), product.getProductQuantity(), product.getPerProductRate(),product.getProductTotalPrice(),product.getTotalCart_amount()});
            }
        } catch (ClassNotFoundException | SQLException ex) {
        	 JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
