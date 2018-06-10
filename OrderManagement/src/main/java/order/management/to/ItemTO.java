package order.management.to;

import order.management.entity.Product;

public class ItemTO {

	private Long id;
	
	private int quantity;
	
	private Long shoppingCartId;
	
	private Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItemTO [id=");
		builder.append(id);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", shoppingCartId=");
		builder.append(shoppingCartId);
		builder.append(", product=");
		builder.append(product);
		builder.append("]");
		return builder.toString();
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Long getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(Long shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	
}
