package ge.nika.springbootdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootdemoApplication {

	private static Logger log = LoggerFactory.getLogger(SpringbootdemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringbootdemoApplication.class, args);
		log.info("---------------------------------"); // Added for style :D
		log.info("Application started successfully!");
	}

}
