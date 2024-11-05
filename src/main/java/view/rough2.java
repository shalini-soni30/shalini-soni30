/*package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;  

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.InventoryDao;
import model.Inventory_model;

import java.sql.SQLException;
import java.util.List;
public class Home {
	 JTable table;
	
	public Home() {
		
		
		   // Create the frame
		 JFrame frame=new JFrame("INVENTORY MANAGEMENT SYSTEM"); 
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(820, 550);
         frame.setLayout(null); // Set layout to null
         
         // Create the panel for buttons
         JPanel buttonPanel = new JPanel();
         buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Arrange buttons horizontally
         buttonPanel.setBounds(10, 10, 780, 50); // Set bounds for button panel
		
         // Create buttons
         JButton addButton = new JButton("Add");
         JButton searchButton = new JButton("Search");
         JButton updateButton = new JButton("Update");
         JButton deleteButton = new JButton("Delete");
         JButton purchaseButton = new JButton("Purchase");
         JButton historyButton = new JButton("History");
         JButton showButton = new JButton("Show");
         
         ////add page call///
         addButton.addActionListener(new ActionListener(){  
        	 public void actionPerformed(ActionEvent e){  
        	            new AddProduct();
        	         }  
        	     });  
         
         ////update page call///
         updateButton.addActionListener(new ActionListener(){  
        	 public void actionPerformed(ActionEvent e){  
        		
        			  new Update();
        	         }  
        	     });  
         

         deleteButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 deleteSelectedRow();
             }
         }); 
         // Add buttons to panel
         buttonPanel.add(addButton);
         buttonPanel.add(searchButton);
         buttonPanel.add(updateButton);
         buttonPanel.add(deleteButton);
         buttonPanel.add(purchaseButton);
         buttonPanel.add(historyButton);
         buttonPanel.add(showButton);
         
         // Create table data and column names
         String[] columnNames = {"ID", "Product Name", "Quantity", "Rate"};
         Object[][] data = {
             {"1", "Sample Product", "10", "$100"},
             {"2", "Another Product", "5", "$50"}
         };// static data
         
        
         InventoryDao dao = new InventoryDao();
         String[] columnNames = {"ID", "Product Name", "Quantity", "Rate"};

         Object[][] data = {};
       // Dynamic data acc to copilot
         try {
             List<Object[]> products = (List<Object[]>) dao.retrieveProducts();
             data = products.toArray(new Object[0][]);
         } catch (ClassNotFoundException | SQLException ex) {
             ex.printStackTrace(); // Handle exception properly
         }
         try {
             List<Inventory_model> products =dao.retrieveProducts();
            for(Inventory_model modeldata:products){
            	System.out.println(products);
            }
           
         } catch (ClassNotFoundException | SQLException ex) {
             ex.printStackTrace(); // Handle exception properly
         }
         
 
         // Create the table
         table = new JTable(data, columnNames);
         JScrollPane scrollPane = new JScrollPane(table);
         
         // Set bounds for scroll pane (and thus the table)
         scrollPane.setBounds(10, 70, 780, 400); // Set bounds for table scroll pane

         private void deleteSelectedRow() {
             int selectedRow = table.getSelectedRow();
             if (selectedRow >= 0) {
                 int id = (int) tableModel.getValueAt(selectedRow, 0); // Assuming ID is in the first column

                 // Remove row from table
                 tableModel.removeRow(selectedRow);

                 // Remove row from the database
                 try {
                     dao.deleteProduct(id); // Ensure you have a method for deleting a product in your InventoryDao
                 } catch (SQLException ex) {
                     ex.printStackTrace(); // Handle exception properly in production
                 }
             } else {
                 JOptionPane.showMessageDialog(null, "No row selected for deletion.");
             }
         }

         
         // Add components to frame
         frame.add(buttonPanel);
         frame.add(scrollPane);
         
         // Make the frame visible
         frame.setVisible(true);
         frame.setLocationRelativeTo(null);      
	}

}
*/