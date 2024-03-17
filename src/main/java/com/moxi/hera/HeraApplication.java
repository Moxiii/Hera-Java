package com.moxi.hera;

import com.moxi.hera.User.Model.User;
import com.moxi.hera.User.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HeraApplication {
	private final UserRepository userRepository;



	public HeraApplication(UserRepository userRepository ) {
		this.userRepository = userRepository;
	}

	@Bean
	public CommandLineRunner defaultDataInitializer() {
		return args -> {
			if (userRepository.count() == 0) {
				User moxi = new User("moxi", "moxi", "10-10-2001", "moxi@moxi.com", "ee");
				User test = new User("test", "test", "10-10-2001", "test@test.com", "ee");
				userRepository.save(moxi);
				userRepository.save(test);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HeraApplication.class, args);
	}

}
