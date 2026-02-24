package com.example.task;

import com.example.task.config.DataSourceConfig;
import com.example.task.config.HibernateConfig;
import com.example.task.config.LiquibaseConfig;
import com.example.task.ui.Builder;
import com.example.task.ui.Navigator;
import com.example.task.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class TaskApplication {

	public static void main(String[] args) {
		System.out.println("--- Hibernate DAO implementation with Liquibase ---");

		try (AnnotationConfigApplicationContext context =
					 new AnnotationConfigApplicationContext(
							 DataSourceConfig.class,
							 HibernateConfig.class,
							 LiquibaseConfig.class
					 )) {

			System.out.println("Spring context with liquibase migrations initialized");

			var builder = new Builder();
			builder.buildMenu();

			var scanner = new Scanner(System.in);
			var navigator = new Navigator(builder.getRootMenu(), scanner);

			runApplication(navigator, scanner);

		} catch (Exception e) {
			System.err.println("Application error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.shutdown();
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