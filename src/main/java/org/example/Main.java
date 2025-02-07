package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    // user neon: goraxi2379@kuandika.com
    // password neon: goraxi2379@kuandika.comQ
    // postgresql://neondb_owner:npg_kmwOYPX2K8zA@ep-late-smoke-a9ou6rhk-pooler.gwc.azure.neon.tech/neondb?sslmode=require

    private static final String URL = "jdbc:postgresql://ep-late-smoke-a9ou6rhk-pooler.gwc.azure.neon.tech:5432/neondb";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_kmwOYPX2K8zA";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    public static void executeUpdate(Connection con, String sql, Object... params) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Operation successful!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void createGame(Connection con, Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter developer: ");
        String developer = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter release date (YYYY-MM-DD): ");
        String releaseDateStr = scanner.nextLine();


        LocalDate releaseDate = null;
        try {
            releaseDate = LocalDate.parse(releaseDateStr);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        String sql = "INSERT INTO games (title, developer, publisher, release_date, price) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(con, sql, title, developer, publisher, Date.valueOf(releaseDate), price);
    }

    public static void readGames(Connection con) {
        String sql = "SELECT * FROM games";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d\n Title: %s\n Developer: %s\n Publisher: %s\n Release Date: %s\n Price: %.2f%n",
                        rs.getInt("id"), rs.getString("title"), rs.getString("developer"),
                        rs.getString("publisher"), rs.getDate("release_date"), rs.getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void updateGame(Connection con, Scanner scanner) {
        System.out.print("Enter the name of the game to search for : ");
        String title = scanner.nextLine();
        System.out.print("Enter new price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        String sql = "UPDATE games SET price = ? WHERE title = ?";
        executeUpdate(con, sql, price, title);
    }

    public static void deleteGame(Connection con, Scanner scanner) {
        System.out.print("Enter game ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM games WHERE id = ?";
        executeUpdate(con, sql, id);
    }


    public static void main(String[] args) {
//        var con = connect();
//        String sql = """
//                CREATE TABLE IF NOT EXISTS games (
//                    id SERIAL PRIMARY KEY,
//                    title VARCHAR(100) NOT NULL,
//                    developer VARCHAR(100) NOT NULL,
//                    publisher VARCHAR(100) NOT NULL,
//                    release_date DATE NOT NULL,
//                    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0)
//                );
//                """;
//        try(var command = con.createStatement())
//        {
//            int rows = command.executeUpdate(sql);
//            System.out.println("Таблицю створено. Оновлено записів: " + rows);
//        } catch(SQLException ex) {
//            System.out.println("Щось пішло не так "+ ex.getMessage());
//        }

        Connection con = connect();
        if (con == null) return;

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Add Game\n2. View Games\n3. Update Game\n4. Delete Game\n5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> createGame(con, scanner);
                    case 2 -> readGames(con);
                    case 3 -> updateGame(con, scanner);
                    case 4 -> deleteGame(con, scanner);
                    case 5 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        }


    }


}