package com.example.task;

import com.example.task.dao.*;
import com.example.task.ui.Builder;
import com.example.task.ui.Navigator;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {
		System.out.println("--- Test console application for testing DAO ---");

		try {
			var carPlaceDAO = new CarPlaceDAO();
			var technicianDAO = new TechnicianDAO();
			var customerDAO = new CustomerDAO();
			var serviceAdvisorDAO = new ServiceAdvisorDAO();
			var orderDAO = new OrderDAO();

			var builder = new Builder(
					carPlaceDAO, technicianDAO, customerDAO, serviceAdvisorDAO, orderDAO
			);
			builder.buildMenu();

			var scanner = new Scanner(System.in);
			var navigator = new Navigator(builder.getRootMenu(), scanner);

			runApplication(navigator, scanner);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void runApplication(Navigator navigator, Scanner scanner) {
		var running = true;

		while (running) {
			try {
				navigator.printMenu();
				var input = scanner.nextLine().trim();

				if ("0".equals(input)) {
					System.out.println("Stopping the application");
					running = false;
					continue;
				}

				try {
					var choice = Integer.parseInt(input);
					navigator.navigate(choice);

				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		scanner.close();
		System.out.println("Bye!");
	}

}
