package order.management;

import org.springframework.data.repository.CrudRepository;

import order.management.entity.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart,Long> {

}
