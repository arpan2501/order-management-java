package order.management.to;

import java.util.List;

import order.management.entity.Product;

public class ProductPageTO {

	private List<Product> productList;
	
	private long allProductLength;

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public long getAllProductLength() {
		return allProductLength;
	}

	public void setAllProductLength(long allProductLength) {
		this.allProductLength = allProductLength;
	}
	
	
	
	
}
