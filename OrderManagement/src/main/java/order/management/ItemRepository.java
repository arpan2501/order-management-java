package order.management;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import order.management.entity.Items;

public interface ItemRepository extends CrudRepository<Items, Long>{

	
	public Items findByshoppingCartIdAndProductId(Long shoppingCartId,Long productId);

	public List<Items> findByshoppingCartId(Long cartID);
	
	@Transactional
	public Long removeByshoppingCartId(Long cartID);

	
}
