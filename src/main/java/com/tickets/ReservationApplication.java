package com.tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class to start the reservation application.
 */
@SpringBootApplication
public class ReservationApplication {
	/**
	 * Main method to start the Spring Boot application.
	 * @param args Command line arguments.
	 */

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
	}

}
