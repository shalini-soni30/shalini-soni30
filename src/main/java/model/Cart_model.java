package model;
import java.util.Date;
public class Cart_model {

	private String productName ,cust_name;
    private int orderID,productId,productQuantity ;
    private double perProductRate, productTotalPrice,totalCart_amount;

	// Create a java.util.Date object
	Date utilDate = new Date();
	
	/*// Convert to java.sql.Date
	Date sqlDate = new Date(utilDate.getTime());*/
	  public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public double getPerProductRate() {
		return perProductRate;
	}

	public void setPerProductRate(int perProductRate) {
		this.perProductRate = perProductRate;
	}

	public double getProductTotalPrice() {
		return productTotalPrice;
	}

	public void setProductTotalPrice(int productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}

	public double getTotalCart_amount() {
		return totalCart_amount;
	}

	public void setTotalCart_amount(double totalCart_amount) {
		this.totalCart_amount = totalCart_amount;
	}

	public Date getUtilDate() {
		return utilDate;
	}

	public void setUtilDate(Date utilDate) {
		this.utilDate = utilDate;
	}

	public Cart_model(int order_ID,String customerName, Date oredrDAte,String productName, int productQuantity, double perProductRate ,double productTotalPrice,double totalcartAmount) {
		super();
		this.orderID = order_ID;
		this.utilDate=oredrDAte;
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.perProductRate = perProductRate;
		this.productTotalPrice= productTotalPrice;
		this.totalCart_amount=totalcartAmount;
	}
	

	public Cart_model() {
		super();
		// TODO Auto-generated constructor stub
	}


	  
	
}
