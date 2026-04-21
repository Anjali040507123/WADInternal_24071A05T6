package CRUDApp;

import java.sql.*;
import java.util.Scanner;

public class CRUDApp {

    static final String URL = "jdbc:mysql://localhost:3306/studentdb";
    static final String USER = "root";
    static final String PASSWORD = "Anjali@123";

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    // ✅ CONNECT DATABASE
    static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to MySQL successfully");
        } catch (Exception e) {
            System.out.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }
    }

    static boolean checkConnection() {
        return con != null;
    }

    // ✅ CREATE TABLE
    static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Students (
                RNO INT PRIMARY KEY,
                NAME VARCHAR(50) NOT NULL,
                MARK1 INT,
                MARK2 INT,
                MARK3 INT
            )
        """;

        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✅ Table ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ INSERT
    static void insert() {
        String sql = "INSERT INTO Students VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Roll No: ");
            int rno = sc.nextInt();

            System.out.print("Enter Name: ");
            String name = sc.next();

            System.out.print("Enter 3 Marks: ");
            int m1 = sc.nextInt();
            int m2 = sc.nextInt();
            int m3 = sc.nextInt();

            ps.setInt(1, rno);
            ps.setString(2, name);
            ps.setInt(3, m1);
            ps.setInt(4, m2);
            ps.setInt(5, m3);

            ps.executeUpdate();
            System.out.println("✅ Record Inserted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ VIEW ALL
    static void viewAll() {
        String sql = "SELECT * FROM Students";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nRNO\tNAME\tMARKS");

            while (rs.next()) {
                System.out.println(
                    rs.getInt(1) + "\t" +
                    rs.getString(2) + "\t[" +
                    rs.getInt(3) + "," +
                    rs.getInt(4) + "," +
                    rs.getInt(5) + "]"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ SEARCH (NEW FEATURE 🔥)
    static void search() {
        String sql = "SELECT * FROM Students WHERE RNO = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Roll No: ");
            int rno = sc.nextInt();

            ps.setInt(1, rno);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Found:");
                System.out.println(
                    rs.getInt(1) + " " +
                    rs.getString(2) + " " +
                    rs.getInt(3) + "," +
                    rs.getInt(4) + "," +
                    rs.getInt(5)
                );
            } else {
                System.out.println("⚠️ Record Not Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ UPDATE
    static void update() {
        String sql = "UPDATE Students SET NAME=? WHERE RNO=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Roll No: ");
            int rno = sc.nextInt();

            System.out.print("Enter New Name: ");
            String name = sc.next();

            ps.setString(1, name);
            ps.setInt(2, rno);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0 ? "✅ Updated" : "⚠️ Not Found");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ DELETE
    static void delete() {
        String sql = "DELETE FROM Students WHERE RNO=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Roll No: ");
            int rno = sc.nextInt();

            ps.setInt(1, rno);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0 ? "✅ Deleted" : "⚠️ Not Found");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ MAIN MENU
    public static void main(String[] args) {

        connect();

        if (!checkConnection()) {
            System.out.println("❌ Exiting...");
            return;
        }

        createTable();

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Insert");
            System.out.println("2. View All");
            System.out.println("3. Search");
            System.out.println("4. Update");
            System.out.println("5. Delete");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> insert();
                case 2 -> viewAll();
                case 3 -> search();
                case 4 -> update();
                case 5 -> delete();
                case 6 -> {
                    try { con.close(); } catch (Exception e) {}
                    System.out.println("👋 Exiting...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }
}