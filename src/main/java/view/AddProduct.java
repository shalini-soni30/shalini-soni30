package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// Controller import
import controller.InventoryDao;
// model import
import model.Inventory_model;
public class AddProduct {
	  JLabel l1, l2, l3, l4;
      final JTextField tf1, tf2, tf3, tf4;
      JButton b1, b2;
    public AddProduct() {
        JFrame f = new JFrame("ADD PRODUCT");

      

        l1 = new JLabel("Product Name");
        l1.setBounds(20, 10, 100, 50);

        tf1 = new JTextField();
        tf1.setBounds(130, 25, 200, 25);

        l2 = new JLabel("Product Quantity");
        l2.setBounds(20, 60, 100, 50);

        tf2 = new JTextField();
        tf2.setBounds(130, 75, 200, 25);

        l3 = new JLabel("Per Product Rate");
        l3.setBounds(20, 110, 100, 50);

        tf3 = new JTextField();
        tf3.setBounds(130, 125, 200, 25);

        l4 = new JLabel("Total Price");
        l4.setBounds(20, 160, 100, 50);
        
        tf4 = new JTextField();
        tf4.setBounds(130, 175, 200, 25);
        tf4.setEditable(false); // Total price should be calculated, not manually entered

        // Document listeners for dynamic total price calculation
        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotalPrice();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotalPrice();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotalPrice();
            }

            private void calculateTotalPrice() {
                try {
                    int quantity = Integer.parseInt(tf2.getText());
                    int rate = Integer.parseInt(tf3.getText());
                    tf4.setText(String.valueOf(quantity * rate));
                } catch (NumberFormatException ex) {
                    tf4.setText(""); // Clear total price if invalid input
                }
            }
        };
        tf2.getDocument().addDocumentListener(listener);
        tf3.getDocument().addDocumentListener(listener);

        b1 = new JButton("ADD PRODUCT");
        b1.setBounds(30, 250, 130, 30);

        
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productName = tf1.getText();
                int productQuantity;
                int perProductRate;
                int totalPrice;

                try {
                	
                    productQuantity = Integer.parseInt(tf2.getText());
                    perProductRate = Integer.parseInt(tf3.getText());
                    totalPrice = Integer.parseInt(tf4.getText());
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(f, "Please enter valid numbers for Quantity, Rate, and Price.");
                    return;
                }

                InventoryDao db = new InventoryDao();   //calling controller
                try {
                    //  you have a method like this in your InventoryDao
                	//int a=   db.addProduct(productName, productQuantity, perProductRate, totalPrice);//passing argument to controller ->
                	//This int a catching value returned from controller method addProduct
                	
                	//constructor of model page receive this argument
                	Inventory_model s=new Inventory_model(productName, productQuantity, perProductRate, productQuantity*perProductRate);//passing argument to controller ->//with model class
					//"s" catching here the value from inventory model
                	
                	//This int a catching value returned from controller method addProduct
					int a= db.addProduct(s);//this "s" values passing to controller db addproduct
                	if(a>0)
					{
                	
                	JOptionPane.showMessageDialog(f, "Product added successfully!");
                	 tf1.setText("");
                     tf2.setText("");
                     tf3.setText("");
                     tf4.setText("");
					}
                	else
                		 JOptionPane.showMessageDialog(f,"Data Not Inserted !"); 
                	
                } catch (ClassNotFoundException |SQLException ex) {
                    JOptionPane.showMessageDialog(f, "Error adding product to the database.");
                    ex.printStackTrace();
                }
            }
        });

        b2 = new JButton("CLEAR");
        b2.setBounds(200, 250, 90, 30);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tf1.setText("");
                tf2.setText("");
                tf3.setText("");
                tf4.setText("");
            }
        });

        f.add(l1);
        f.add(l2);
        f.add(l3);
        f.add(l4);
        f.add(tf1);
        f.add(tf2);
        f.add(tf3);
        f.add(tf4);
        f.add(b1);
        f.add(b2);

        f.setSize(400, 350);
        f.setLayout(null);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
