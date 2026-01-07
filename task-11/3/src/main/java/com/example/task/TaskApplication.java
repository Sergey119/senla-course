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
			CarPlaceDAO carPlaceDAO = new CarPlaceDAO();
			TechnicianDAO technicianDAO = new TechnicianDAO();
			CustomerDAO customerDAO = new CustomerDAO();
			ServiceAdvisorDAO serviceAdvisorDAO = new ServiceAdvisorDAO();
			OrderDAO orderDAO = new OrderDAO();

			Builder builder = new Builder(
					carPlaceDAO, technicianDAO, customerDAO, serviceAdvisorDAO, orderDAO
			);
			builder.buildMenu();

			Scanner scanner = new Scanner(System.in);
			Navigator navigator = new Navigator(builder.getRootMenu(), scanner);

			runApplication(navigator, scanner);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void runApplication(Navigator navigator, Scanner scanner) {
		boolean running = true;

		while (running) {
			try {
				navigator.printMenu();
				String input = scanner.nextLine().trim();

				if ("0".equals(input)) {
					System.out.println("Stopping the application");
					running = false;
					continue;
				}

				try {
					int choice = Integer.parseInt(input);
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
