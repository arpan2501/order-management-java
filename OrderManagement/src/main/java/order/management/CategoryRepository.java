package order.management;

import org.springframework.data.repository.CrudRepository;

import order.management.entity.Category;

public interface CategoryRepository extends CrudRepository<Category,String>{

}
