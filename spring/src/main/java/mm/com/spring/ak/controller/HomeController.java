package mm.com.spring.ak.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping(value = "api/getData")
	public String getDate() {
		return "get server data";
	}
}
