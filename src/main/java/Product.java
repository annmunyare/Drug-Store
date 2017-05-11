import org.sql2o.*;
import java.util.*;

public class Product{
	
	private int id;
	private String name;
	private String description;
	public int price;
	public int quantity;
	
	public static final int MAX_INVENTORY = 10;
	public static final int MIN_INVENTORY = 0;
	
	public Product(String name, String description, int price){
		this.name = name;
		this.description = description;
		quantiy = 0;
	}
	
	public String getName(){
		return name
	}
	
	 public String getDescription() {
    return description;
  }

  public int getPrice() {
    return price;
  }

  public int getId() {
    return id;
  }
	
	
  public int getQuantity() {
    return quantity;
  }
	
	public static List <Product> all() {
    String sql = "SELECT * FROM products;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).throwOnMappingFailure(false)
				.executeAndFetch(Product.class);
    }
  }
	
	public static Product findProduct(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM products WHERE name = :name";
      return con.createQuery(sql)
        .addParameter("name", name)
        .throwOnMappingFailure(false).executeAndFetchFirst(Product.class);
    }
  }
	
	
	public void restock(){
		quantity = MAX_QUANTITY;
		try(Connection con = DB.sql2o.open()){
			String sql = "UPDATE products SET quantity = :quantity WHERE id = :id;";
			con.createQuery(sql)
				.addParameter("quantity",quantity)
				.addParameter("id", this.id)
				.throwOnMappingFailure(false)
				.executeUpdate();
		}
	}
	
	public void save(){
		try(Connection con = DB.sql2o.open()){
			String sql = "INSERT INTO products (name,description,price,quantity) VALUES (:name,:description,:price,:quantity);";
			this.id = (int) con.createQuery(sql,true)
				.addParameter("name",this.name)
				.addParameter("description",this.description)
				.addParameter("price",this.price)
				.addParameter("quantity",this.quantity)
				.executeUpdate()
				.getKey();
		}
	}
	
	public void update(String name, String description, int price) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE products SET name = :name, description=:description, price=:price WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("description", description)
        .addParameter("price", price)
        .addParameter("id", this.id)
        .throwOnMappingFailure(false)
        .executeUpdate();
    }
  }
	
	public void subtractQuantity() {
    this.Quantity--;
    updateQuantity();
  }
	
	public void updateQuantity(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE products SET quantity = :quantity WHERE id = :id";
      con.createQuery(sql)
        .addParameter("quantity", this.quantity)
        .addParameter("id", this.id)
        .throwOnMappingFailure(false)
        .executeUpdate();
    }
  }
	
	@Override
    public boolean equals(Object otherProduct) {
      if (!(otherProduct instanceof Product)) {
        return false;
      } else {
        Product newProduct = (Product) otherProduct;
        return this.getName().equals(newProduct.getName()) &&
               this.getDescription().equals(newProduct.getDescription()) &&
               this.getPrice() == newProduct.getPrice();
    }
  }
	
	 public void delete() {
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM products WHERE id =:id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
	
	public void depleteQuantity(int amount){
    if (!(inStock())){
       throw new UnsupportedOperationException("We are out of stock");
     } else {
       subtractQuantity();
    }
  }
	
	public boolean inStock() {
    if (Quantity <= MIN_Quantity) {
      return false;
    } else{
    return true;
    }
  }

 


}