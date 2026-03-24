package com.example.task;

import com.example.task.ui.Builder;
import com.example.task.ui.Navigator;
import com.example.task.util.HibernateUtil;

import java.util.Scanner;

public class TaskApplication {

	public static void main(String[] args) {
		System.out.println("--- Hibernate DAO Implementation ---");

		try {
			// Инициализация Hibernate
			System.out.println("Initializing Hibernate...");
			HibernateUtil.getSessionFactory();

			var builder = new Builder();
			builder.buildMenu();

			var scanner = new Scanner(System.in);
			var navigator = new Navigator(builder.getRootMenu(), scanner);

			runApplication(navigator, scanner);

		} catch (Exception e) {
			System.err.println("Application error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Завершение работы Hibernate
			HibernateUtil.shutdown();
			System.out.println("Hibernate shutdown complete.");
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
					System.out.println("Invalid input. Please enter a number.");
				}

			} catch (Exception e) {
				System.err.println("Menu error: " + e.getMessage());
			}
		}

		scanner.close();
		System.out.println("Application terminated.");
	}
}