package controller;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Inventory_model;
import model.Cart_model;

public class InventoryDao {
	String driver="com.mysql.cj.jdbc.Driver";
	String db="inventoryproject";
	String url="jdbc:mysql://localhost:3306/"+db;
	String dbname="root";
	String dbpass="abc123";
	
	Connection getConnect() throws ClassNotFoundException, SQLException
	   {
		   Class.forName(driver);
		   Connection con=DriverManager.getConnection(url,dbname,dbpass);
		   return con;
	   }


	public int addProduct(Inventory_model s) throws ClassNotFoundException, SQLException
	{
		String query="INSERT INTO product_tbl (Product_name,Quantity,per_productrate,Total_price) values (?,?,?,?)";
		try (Connection con=getConnect();PreparedStatement ps=con.prepareStatement(query)){
		ps.setString(1, s.getProductName());
		ps.setInt(2, s.getProductQuantity());
		ps.setInt(3, s.getPerProductRate());
		ps.setInt(4, s.getProductTotalPrice());
		
		int a= ps.executeUpdate();
		return a;
		}
	}
		
	    // Existing methods...

	    public int updateProduct(Inventory_model product) throws SQLException, ClassNotFoundException {
	        String query = "UPDATE product_tbl SET Product_name = ?, Quantity = ?, per_productrate = ?, Total_price = ?  WHERE ID = ?";
	        try (Connection con = getConnect();
	        	PreparedStatement ps = con.prepareStatement(query)) {
	        	 ps.setString(1, product.getProductName());
	             ps.setInt(2, product.getProductQuantity());
	             ps.setInt(3, product.getPerProductRate());
	             ps.setInt(4, product.getProductTotalPrice());
	             ps.setInt(5, product.getProductId());  // Ensure ID is passed
	             //ps.executeUpdate();
	            return ps.executeUpdate();  // Return the number of affected rows
	        }
	    }
	

    public int deleteProduct(int id) throws ClassNotFoundException, SQLException {
        String query = "DELETE FROM product_tbl WHERE ID = ?";
        try (Connection con = getConnect();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
    // Method to retrieve a product by name
    public Inventory_model getProductByName(String productName) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM product_tbl WHERE product_name = ?";
        try (Connection con = getConnect();PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Inventory_model(
                    rs.getInt("ID"),
                    rs.getString("product_name"),
                    rs.getInt("Quantity"),
                    rs.getInt("per_productrate"),
                    rs.getInt("Total_price")
                );
            }
        }
        return null; // Return null or throw an exception if not found
    }
	
 
    public List<Inventory_model> retrieveProducts() throws ClassNotFoundException, SQLException {
        List<Inventory_model> products = new ArrayList<>();
        String query = "SELECT * FROM product_tbl";
        try (Connection con = getConnect(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
            	Inventory_model model = new Inventory_model(
                    rs.getInt("ID"),
                    rs.getString("Product_name"),
                    rs.getInt("Quantity"),
                    rs.getInt("per_productrate"),
                    rs.getInt("Total_price")
                );
                products.add(model);
            }
        }
        return products;
    }
	
    public void insertOrder(String orderByName, String orderDate, String productName, int quantity, double rate, double totalPrice, double totalAmount) throws SQLException, ClassNotFoundException {
        // Prepare SQL insert statement
        String sql = "INSERT INTO cart_tbl (cust_name, order_date, product_name, quantity,per_productrate, total_price, total_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnect(); // Assume getConnection() handles your DB connection
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, orderByName);
            pstmt.setString(2, orderDate);
            pstmt.setString(3, productName);
            pstmt.setInt(4, quantity);
            pstmt.setDouble(5, rate);
            pstmt.setDouble(6, totalPrice);
            pstmt.setDouble(7, totalAmount);
            pstmt.executeUpdate();
        }
    }
    
    public List<Cart_model> retrieveOders() throws ClassNotFoundException, SQLException {
        List<Cart_model> orders  = new ArrayList<>();
        String query = "SELECT * FROM cart_tbl";
        try (Connection con = getConnect(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
            	Cart_model model = new Cart_model(
                    rs.getInt("order_id"),
                    rs.getString("cust_name"),
                    rs.getDate("order_date"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getInt("per_productrate"),
                    rs.getInt("total_price"),
                    rs.getDouble("total_amount")
                );
            	orders .add(model);
            }
        }
        return orders ;
    }
	

}
