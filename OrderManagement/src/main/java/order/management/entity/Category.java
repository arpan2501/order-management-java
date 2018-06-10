package order.management.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category {

	public Category(String categoryName) {
		super();
		this.categoryName = categoryName;
	}

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Category [categoryName=");
		builder.append(categoryName);
		builder.append(", productList=");
		builder.append(productList);
		builder.append("]");
		return builder.toString();
	}



	@Id
	private String categoryName;
	
	@OneToMany(mappedBy="category")
	private List<Product> productList;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}
