package bartnik.master.app.relational.recipeforum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class RecipeForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeForumApplication.class, args);
	}

}
