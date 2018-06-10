package order.management;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import order.management.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product,Long>{
	
	public List<Product> getProductByCategoryCategoryName(String categoryName,Pageable pageable);
	
	public long countProductByCategoryCategoryName(String categoryName);

}
