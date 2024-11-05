package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddToCartForm extends JFrame {
   // private JTextField productIdField;
    private JTextField productNameField;
    private JTextField productQuantityField;
    private JTextField productRateField;
    private JTextField totalPriceField;
    private DefaultTableModel cartTableModel;
	private SearchProduct searchProduct;

    public AddToCartForm(DefaultTableModel cartTableModel, int pro_ID, String productName, int pro_Quantity, int pro_rate,int pro_totprice, SearchProduct searchProduct) {
        this.cartTableModel = cartTableModel;
        this.searchProduct = searchProduct; // Store the reference
        setTitle("Add to Cart");
        
        JLabel l1, l2, l3, l4;
        l1 = new JLabel("Product Name");
        l1.setBounds(20, 10, 100, 50);

        productNameField = new JTextField(productName);
        productNameField.setBounds(130, 25, 200, 25);

        l2 = new JLabel("Product Quantity");
        l2.setBounds(20, 60, 100, 50);

        productQuantityField = new JTextField();
        productQuantityField.setBounds(130, 75, 200, 25);

        l3 = new JLabel("Per Product Rate");
        l3.setBounds(20, 110, 100, 50);

        productRateField = new JTextField();
        productRateField.setBounds(130, 125, 200, 25);

        l4 = new JLabel("Total Price");
        l4.setBounds(20, 160, 100, 50);
        
        totalPriceField = new JTextField();
        totalPriceField.setBounds(130, 175, 200, 25);
        totalPriceField.setEditable(false); 
        int DbQuantity=pro_Quantity;
       // productIdField = new JTextField(productId);
        int ProductID=pro_ID;
    
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBounds(150, 250, 130, 30);
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

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
                    int quantity = Integer.parseInt(productQuantityField.getText());
                    int rate = Integer.parseInt(productRateField.getText());
                    totalPriceField.setText(String.valueOf(quantity * rate));
                } catch (NumberFormatException ex) {
                	totalPriceField.setText(""); // Clear total price if invalid input
                }
            }
        };
        productQuantityField.getDocument().addDocumentListener(listener);
        productRateField.getDocument().addDocumentListener(listener);
        add(l1);
        add(l2);
        add(l3);
        add(l4);
        add(new JLabel("Product Name:"));
        add(productNameField);
        add(new JLabel("Quantity:"));
        add(productQuantityField);
        add(new JLabel("Rate:"));
        add(productRateField);
        add(new JLabel("Total Price:"));
        add(totalPriceField);
        add(addToCartButton);

       
        setSize(400, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        
    }

    private void addToCart() {
        String productName = productNameField.getText();
        String quantity = productQuantityField.getText();
        if (quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quantity cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if quantity is invalid
        }

        int quantityInt = Integer.parseInt(quantity); // Safely parse integer
        double rate = Double.parseDouble(productRateField.getText()) ;
        double totalPrice = quantityInt * rate;

        if (productName.isEmpty() || (quantity.isEmpty()) || (rate<=0) || (totalPrice<=0)) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cartTableModel.addRow(new Object[]{productName, quantity, rate, totalPrice});
        // Update the total amount in SearchProduct
        searchProduct.updateTotalAmount();
        dispose(); // Close the form after adding to cart
        // Clear form fields No Need just in case
      /*  private void clearForm() {
            productIdField.setText("");
            productNameField.setText("");
            productQuantityField.setText("");
            productRateField.setText("");
            totalPriceField.setText("");
        }*/
    }
}
