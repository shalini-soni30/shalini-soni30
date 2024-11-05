/*package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.InventoryDao;
import model.Inventory_model;

public class DeleteProduct {
	public DeleteProduct(){
		JFrame deletef = new JFrame("DELETE PRODUCT");
        JLabel l1, l2, l3, l4;
        final JTextField tf1, tf2, tf3, tf4;
        JButton b1, b2;

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

        b1 = new JButton("DELETE PRODUCT");
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
                    JOptionPane.showMessageDialog(updatef, "Please enter valid numbers for Quantity, Rate, and Price.");
                    return;
                }

                InventoryDao db = new InventoryDao();   //calling controller
                try {
                   
                	//constructor of model page receive this argument
                	Inventory_model s=new Inventory_model(productName, productQuantity, perProductRate, totalPrice);//passing argument to controller ->//with model class
					//"s" catching here the value from inventory model
                	
                	//This int a catching value returned from controller method addProduct
					int a= db.updateProduct(s);//this "s" values passing to controller db addproduct
                	if(a>0)
					{
                	
                	JOptionPane.showMessageDialog(updatef, "Product updated successfully!");
                	 tf1.setText("");
                     tf2.setText("");
                     tf3.setText("");
                     tf4.setText("");
					}
                	else
                		 JOptionPane.showMessageDialog(updatef,"Data Not updated !"); 
                	
                } catch (ClassNotFoundException |SQLException ex) {
                    JOptionPane.showMessageDialog(updatef, "Error updating product to the database.");
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

        deletef.add(l1);
        deletef.add(l2);
        deletef.add(l3);
        deletef.add(l4);
        deletef.add(tf1);
        deletef.add(tf2);
        deletef.add(tf3);
        deletef.add(tf4);
        deletef.add(b1);
        deletef.add(b2);

        deletef.setSize(400, 350);
        deletef.setLayout(null);
        deletef.setVisible(true);
        deletef.setLocationRelativeTo(null);
        deletef.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}
}
*/