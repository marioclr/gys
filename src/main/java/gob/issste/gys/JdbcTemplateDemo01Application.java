package gob.issste.gys;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import gob.issste.gys.service.SecurityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static gob.issste.gys.service.SecurityService.SUPER_SECRET_KEY;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class })
@RestController
public class JdbcTemplateDemo01Application {
	@GetMapping("/status")
	public String getStatus() {
		return "Application is running...!!!";
	}

	public void print() {
		System.out.println("Current Time : " + new Date());
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(JdbcTemplateDemo01Application.class, args);
		SecurityService.SUPER_SECRET_KEY = SecurityService.generateSecretKey();
	}

}