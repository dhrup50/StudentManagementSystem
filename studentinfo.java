package dhrup1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class studentinfo {

    public static void main(String[] args) throws SQLException {
        System.out.println("Welcome to SRI");

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/std11_db", "root", "dhup");
        Scanner scr = new Scanner(System.in);

        System.out.println("1. Admin login");
        System.out.println("2. Student login");
        System.out.println("Choose any option:");
        int ch = scr.nextInt();

        switch (ch) {
            case 1:
                adminLogin(con, scr);
                break;
            case 2:
                studentLogin(con, scr);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }

        con.close();
        scr.close();
    }

    private static void adminLogin(Connection con, Scanner scr) throws SQLException {
        System.out.println("Admin login");
        System.out.println("Enter your username:");
        String username = scr.next();
        System.out.println("Enter your password:");
        String password = scr.next();

        String query = "SELECT * FROM admin1 WHERE username=? AND password=?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            System.out.println("Login successful!");
            adminOptions(con, scr);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void adminOptions(Connection con, Scanner scr) throws SQLException {
        while (true) {
            System.out.println("Welcome to the admin page!");
            System.out.println("1. Enter student info");
            System.out.println("2. Show student info");
            System.out.println("3. Delete student");
            System.out.println("4. Update student marks");
            System.out.println("5. Exit");
            System.out.println("Choose an option:");
            int choice = scr.nextInt();

            switch (choice) {
                case 1:
                    addStudent(con, scr);
                    break;
                case 2:
                    showStudentInfo(con);
                    break;
                case 3:
                    deleteStudent(con, scr);
                    break;
                case 4:
                    updateStudentMarks(con, scr);
                    break;
                case 5:
                    System.out.println("Exiting admin menu...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addStudent(Connection con, Scanner scr) throws SQLException {
        System.out.println("Enter student info:");
        System.out.println("Enter Name:");
        String name = scr.next();
        System.out.println("Enter Rollno:");
        int rollNo = scr.nextInt();
        System.out.println("Enter Address:");
        String address = scr.next();
        System.out.println("Enter Mobile No:");
        String mobileNo = scr.next();

        String query = "INSERT INTO STUDENT1(Name, rollno, address, mobno) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setInt(2, rollNo);
        stmt.setString(3, address);
        stmt.setString(4, mobileNo);

        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Student added successfully.");
        } else {
            System.out.println("Failed to add student details.");
        }
    }

    private static void showStudentInfo(Connection con) throws SQLException {
        System.out.println("Displaying all student information:");
        String query = "SELECT * FROM STUDENT1 ";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        System.out.println("Name | Rollno | Address | Mobno");
        while (rs.next()) {
            String name = rs.getString("Name");
            int rollNo = rs.getInt("rollno");
            String address = rs.getString("address");
            String mobileNo = rs.getString("mobno");

            System.out.println(name + " | " + rollNo + " | " + address + " | " + mobileNo);
        }
    }

    private static void deleteStudent(Connection con, Scanner scr) throws SQLException {
        System.out.println("Enter the roll number of the student to delete:");
        int rollNo = scr.nextInt();

        String query = "DELETE FROM STUDENT1 WHERE rollno=?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, rollNo);

        int rowsDeleted = stmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Failed to delete student. Please check the roll number.");
        }
    }

    private static void updateStudentMarks(Connection con, Scanner scr) throws SQLException {
        System.out.println("Enter the roll number of the student to update marks:");
        int rollNo = scr.nextInt();
        System.out.println("Enter new marks for English:");
        int english = scr.nextInt();
        System.out.println("Enter new marks for Maths:");
        int maths = scr.nextInt();
        System.out.println("Enter new marks for Biology:");
        int biology = scr.nextInt();
        System.out.println("Enter new marks for Physics:");
        int physics = scr.nextInt();
        System.out.println("Enter new marks for Chemistry:");
        int chemistry = scr.nextInt();

        int total = english + maths + biology + physics + chemistry;
        double percentage = (total / 5.0); // Assuming 5 subjects
        double average = total / 5.0;

        String query = "UPDATE marks2 SET English=?, Maths=?, Biology=?, Physics=?, Chemistry=?, Total=?, Percentage=?, Average=? WHERE rollno=?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, english);
        stmt.setInt(2, maths);
        stmt.setInt(3, biology);
        stmt.setInt(4, physics);
        stmt.setInt(5, chemistry);
        stmt.setInt(6, total);
        stmt.setDouble(7, percentage);
        stmt.setDouble(8, average);
        stmt.setInt(9, rollNo); // Fixed duplicate parameter index

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Student marks updated successfully.");
        } else {
            System.out.println("Failed to update marks. Please check the roll number.");
        }
    }

    private static void studentLogin(Connection con, Scanner scr) throws SQLException {
        System.out.println("Student login");
        System.out.println("Enter your roll number:");
        int rollNo = scr.nextInt();

        String query = "SELECT * FROM marks2 WHERE rollno=?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, rollNo);

        ResultSet rs = stmt.executeQuery();
        System.out.println("English | Maths | Biology | Physics | Chemistry | Total | Percentage | Average | Rollno");

        if (rs.next()) {
            do {
                int english = rs.getInt("English");
                int maths = rs.getInt("Maths");
                int biology = rs.getInt("Biology");
                int physics = rs.getInt("Physics");
                int chemistry = rs.getInt("Chemistry");
                int total = rs.getInt("Total");
                double percentage = rs.getDouble("Percentage"); // Fixed type
                double average = rs.getDouble("Average"); // Fixed type
                int rollno = rs.getInt("Rollno");

                System.out.println(english + " | " + maths + " | " + biology + " | " + physics + " | " + chemistry + " | "
                        + total + " | " + percentage + " | " + average + " | " + rollno);
            } while (rs.next());
        } else {
            System.out.println("Invalid roll number or no data found.");
        }
    }
}

 

	 

	