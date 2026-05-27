package edziekanat.isi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IsiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsiApplication.class, args);
	}

}
