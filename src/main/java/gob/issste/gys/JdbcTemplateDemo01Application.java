package gob.issste.gys;

import gob.issste.gys.service.SecurityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class })
@RestController
public class JdbcTemplateDemo01Application {
	@GetMapping("/status")
	public String getStatus() {
		return "Application is running...!!!";
	}

	public static void main(String[] args) throws Exception {

		SpringApplication.run(JdbcTemplateDemo01Application.class, args);
		SecurityService.SUPER_SECRET_KEY = SecurityService.generateSecretKey();
		SecurityService.securityAESkeyGenerator();
	}

}