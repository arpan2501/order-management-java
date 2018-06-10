package order.management.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.net.SyslogOutputStream;
import order.management.ApplicationUserRepository;
import order.management.CategoryRepository;
import order.management.ItemRepository;
import order.management.ProductRepository;
import order.management.ShoppingCartRepository;
import order.management.entity.ApplicationUser;
import order.management.entity.Category;
import order.management.entity.Items;
import order.management.entity.Product;
import order.management.entity.ShoppingCart;
import order.management.to.ItemTO;
import order.management.to.ProductPageTO;
import order.management.to.ProductTO;
import order.management.to.SignUpUserTO;

@RestController()
@RequestMapping("/api")
public class MyController {

	@Autowired
	ApplicationUserRepository repo;

	@Autowired
	CategoryRepository catRepo;

	@Autowired
	ProductRepository prodRepo;

	@Autowired
	private BCryptPasswordEncoder pwd;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ShoppingCartRepository cartRepo;

	@Autowired
	ItemRepository itemRepo;

	@PostMapping("/registerUser")
	public void registerNewUser(@RequestBody SignUpUserTO user) {
		System.out.println("Hello!!");

		ApplicationUser signInUser = modelMapper.map(user, ApplicationUser.class);

		signInUser.setPassword(pwd.encode(user.getPassword()));

		repo.save(signInUser);
	}

	@GetMapping("/authenticate")
	public void isAuthenticated() {
		System.out.println("false!!");
	}

	@GetMapping("/products/{pageNumber}/{pageSize}")
	public ProductPageTO getProducts(@PathVariable int pageNumber, @PathVariable int pageSize) {
		List<Product> products = new ArrayList<Product>();

		prodRepo.findAll(PageRequest.of(pageNumber, pageSize)).forEach(products::add);

		ProductPageTO productDetails = new ProductPageTO();
		productDetails.setProductList(products);
		productDetails.setAllProductLength(prodRepo.count());

		return productDetails;
	}

	@GetMapping("/homeProducts/{categoryType}/{pageSize}/{pageNumber}")
	public ProductPageTO getHomePageProducts(@PathVariable("categoryType") String categoryType,
			@PathVariable("pageSize") int pageSize, @PathVariable("pageNumber") int pageNumber) {
		ProductPageTO productDetails = new ProductPageTO();

		List<Product> products = new ArrayList<Product>();
		if (categoryType.equals("All")) {
			prodRepo.findAll(PageRequest.of(pageNumber, pageSize)).forEach(products::add);
			productDetails.setAllProductLength(prodRepo.count());
		} else {
			prodRepo.getProductByCategoryCategoryName(categoryType, PageRequest.of(pageNumber, pageSize))
					.forEach(products::add);
			productDetails.setAllProductLength(prodRepo.countProductByCategoryCategoryName(categoryType));

		}

		productDetails.setProductList(products);

		return productDetails;
	}

	@GetMapping("/product/{id}")
	public Product getProduct(@PathVariable Long id) {

		Optional<Product> product = prodRepo.findById(id);

		return product.get();
	}

	@GetMapping("/categories")
	public List<Category> getCategories() {
		List<Category> categoryList = new ArrayList<Category>();

		catRepo.findAll().forEach(categoryList::add);

		return categoryList;
	}

	@PostMapping("/saveCategory")
	public void saveCategoryValue(@RequestBody String category) {
		Category cat = new Category(category);
		catRepo.save(cat);
	}

	@PostMapping("/saveProduct")
	public void saveNewProduct(@RequestBody ProductTO productTO) {

		System.out.println(productTO);

		Product product = modelMapper.map(productTO, Product.class);

		System.out.println(product);
		prodRepo.save(product);
	}

	@PostMapping("/updateProduct{id}")
	public void updateExistingProduct(@RequestBody ProductTO productTO, @PathVariable Long id) {

		System.out.println(productTO);
		if (prodRepo.existsById(id)) {
			productTO.setId(id);
			Product product = modelMapper.map(productTO, Product.class);

			System.out.println(product);
			prodRepo.save(product);
		}
	}

	@GetMapping("/createCart")
	public ShoppingCart generateCart() {
		ShoppingCart cart = new ShoppingCart(LocalDateTime.now());

		return cartRepo.save(cart);

	}

	

	@GetMapping("/getProductQuantity/{shoppingCartId}/{productId}")
	public Integer getItemQuantity(@PathVariable Long shoppingCartId, @PathVariable Long productId) {

		Items item = itemRepo.findByshoppingCartIdAndProductId(shoppingCartId, productId);
		if (item != null)
			return item.getQuantity();

		return 0;
	}

	@GetMapping("/updateProductItem/{shoppingCartId}/{productId}/{change}")
	public int removeItemFromCart(@PathVariable Long shoppingCartId, @PathVariable Long productId,
			@PathVariable String change) {

		Items item = itemRepo.findByshoppingCartIdAndProductId(shoppingCartId, productId);
		
		if (change.equals("remove")) {
			
			item.setQuantity(item.getQuantity() - 1);
			
			if(item.getQuantity()==0){
				itemRepo.delete(item);
				return 0;
			}
		} else {
			
			if (item != null) {
				item.setQuantity(item.getQuantity() + 1);
			} else {
				item = new Items();
				item.setProduct(prodRepo.findById(productId).get());
				item.setShoppingCart(cartRepo.findById(shoppingCartId).get());
				item.setQuantity(1);
			}
		}
		item = itemRepo.save(item);
		

		return item.getQuantity();
	}

	@GetMapping("/getCartItemsCount/{cartID}")
	public int getShoppingCartItemsCount(@PathVariable Long cartID) {

		List<Items> itemList = itemRepo.findByshoppingCartId(cartID);

		int totalCartCount = itemList.stream().mapToInt(item -> item.getQuantity()).sum();

		return totalCartCount;
	}
	
	@GetMapping("/getCartItems/{cartID}")
	public List<ItemTO> getShoppingCartItems(@PathVariable Long cartID) {

		List<Items> itemList = itemRepo.findByshoppingCartId(cartID);		
		
		List<ItemTO> itemTOList = new ArrayList<ItemTO>();
		
		itemList.stream().forEach(item -> {
			ItemTO itemTO = modelMapper.map(item, ItemTO.class);
			itemTOList.add(itemTO);
		});
		
		return itemTOList;
	}
	
	@DeleteMapping("/deleteItem/{cartID}/{productId}")
		public void deleteShoppingCartItem(@PathVariable Long cartID, @PathVariable Long productId){
		Items item = itemRepo.findByshoppingCartIdAndProductId(cartID, productId);
		itemRepo.delete(item);
	}
	
	@DeleteMapping("/clearCart/{cartID}")
	public void clearShoppingCart(@PathVariable Long cartID){
	itemRepo.removeByshoppingCartId(cartID);
}


}
