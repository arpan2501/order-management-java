package order.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class NormalController {

	@GetMapping("/1")
	public void testing(){
		System.out.println("k");
	}
	
}
