package com.example.project1.Client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.OutputStream;


public class ClientApp {

    private static final String BASE_URL = "http://localhost:8080/api/items";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. GET all items");
            System.out.println("2. POST new item");
            System.out.println("3. PUT update item");
            System.out.println("4. DELETE item");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    getAllItems();
                    break;
                case 2:
                    postNewItem(scanner);
                    break;
                case 3:
                    putUpdateItem(scanner);
                    break;
                case 4:
                    deleteItem(scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void getAllItems() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            Scanner responseScanner = new Scanner(connection.getInputStream());
            while (responseScanner.hasNext()) {
                System.out.println(responseScanner.nextLine());
            }
            responseScanner.close();
        } else {
            System.out.println("Failed to get items. Response code: " + responseCode);
        }
    }

    private static void postNewItem(Scanner scanner) throws IOException {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter item description: ");
        String description = scanner.nextLine();
        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String jsonInputString = String.format("{\"name\":\"%s\",\"description\":\"%s\",\"price\":%.2f}", name, description, price);

        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 201) { // Updated to check for 201 Created
            System.out.println("Item created successfully.");
        } else {
            System.out.println("Failed to create item. Response code: " + responseCode);
        }
    }

    private static void putUpdateItem(Scanner scanner) throws IOException {
        System.out.print("Enter item ID: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // consume newline
        System.out.print("Enter new item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new item description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String jsonInputString = String.format("{\"name\":\"%s\",\"description\":\"%s\",\"price\":%.2f}", name, description, price);

        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            System.out.println("Item updated successfully.");
        } else {
            System.out.println("Failed to update item. Response code: " + responseCode);
        }
    }

    private static void deleteItem(Scanner scanner) throws IOException {
        System.out.print("Enter item ID: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // consume newline

        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        if (responseCode == 204) {
            System.out.println("Item deleted successfully.");
        } else {
            System.out.println("Failed to delete item. Response code: " + responseCode);
        }
    }
}

