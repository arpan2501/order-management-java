package order.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/second")
public class SecondaryController {

	
	@GetMapping("/2")
	public void testing(){
		System.out.println("k2");
	}
	
}
