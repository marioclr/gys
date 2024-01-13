package gob.issste.gys;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class })
@RestController
public class JdbcTemplateDemo01Application {

	@GetMapping("/status")
	public String getStatus() {
		return "Application is running...!!!";
	}

	public void print() {
		System.out.println("Current Time : " + new Date());
	}

	public static void main(String[] args) {
		SpringApplication.run(JdbcTemplateDemo01Application.class, args);
	}

}