package order.management;

import org.springframework.data.repository.CrudRepository;

import order.management.entity.ApplicationUser;



public interface ApplicationUserRepository extends CrudRepository<ApplicationUser,Long>{
	
	
	ApplicationUser findByUsername(String username);
	
}
