package HotelReservations;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelReservationSystem {
	private static final String url = "jdbc:mysql://localhost:3306/?user=root";

	private static final String username = "root";

	private static final String password = "root";

	public static void main(String[] args) throws InterruptedException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Statement statement = connection.createStatement();
			while (true) {
				System.out.println();
				System.out.println("Hotel Reservation System");
				System.out.println();
				Scanner scanner = new Scanner(System.in);
				System.out.println("1. Reserv a Room ");
				System.out.println("2. View Reservation");
				System.out.println("3. Get Room Number");
				System.out.println("4. Update Reservation ");
				System.out.println("5. Delete Reservation ");
				System.out.println("0. Exit");
				System.out.println();
				System.out.println("Please,Choose an option:");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1:
					reserveRoom(connection, scanner, statement);
					break;
				case 2:
					viewReservation(scanner, statement);
					break;
				case 3:
					getRoomNumber(connection, scanner, statement);
					break;
				case 4:
					updateReservation(connection, scanner, statement);
					break;
				case 5:
					deleteReservation(connection, scanner, statement);
					break;
				case 0:
					exit();
					scanner.close();
					return;
				default:
					System.out.println("Invalid choice !! please try again");
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void reserveRoom(Connection connection, Scanner scanner, Statement statement) {
		try {
			System.out.print("Enter Guest Name: ");
			String name = scanner.next();
			System.out.print("Enter Room Number: ");
			int roomnumber = scanner.nextInt();
			System.out.print("Enter Contact Number: ");
			String contactnumber = scanner.next();

			String sql = "insert into hotel_db.reservations(guest_name,room_number,contact_number)" + "values('" + name
					+ "', '" + roomnumber + "', '" + contactnumber + "')";

			int count = statement.executeUpdate(sql);
			if (count > 0) {
				System.out.println("Congratulation!!,Your Reservation Done..");
			} else {
				System.out.println("Oops!!,Reservation failed..");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	private static void viewReservation(Scanner scanner, Statement statement) {
		String sql = "select * from hotel_db.reservations";
		try {
			ResultSet rs = statement.executeQuery(sql);
			System.out.println("Current Reservation: ");
			while (rs.next()) {
				int reservid = rs.getInt("reservation_id");
				String guestname = rs.getString("guest_name");
				int roomnumber = rs.getInt("room_number");
				String contnumber = rs.getString("contact_number");

				System.out.println("Reservation ID: " + reservid);
				System.out.println("Guest Name: " + guestname);
				System.out.println("Room Number: " + roomnumber);
				System.out.println("Contact Number: " + contnumber);
				System.out.println();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void getRoomNumber(Connection connection, Scanner scanner, Statement statement) {
		System.out.print("Enter Reservation Id: ");
		int reservationId = scanner.nextInt();
		System.out.print("Enter Guest Name: ");
		String guestName = scanner.next();

		String sql = "select room_number from hotel_db.reservations" + " where reservation_id=" + reservationId
				+ " AND guest_name='" + guestName + "'";
		try {
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				int roomNo = rs.getInt("room_number");

				System.out.println(
						"Room number for Reservation ID " + reservationId + "and guest" + guestName + "is: " + roomNo);
			} else {
				System.out.println("Room Number not found for the given ID and guest name.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void updateReservation(Connection connection, Scanner scanner, Statement statement) {
		System.out.println("Enter Reservation ID: ");
		int reservationId = scanner.nextInt();
		if (!reservationExist(connection, reservationId, statement)) {
			System.out.println("Reservation not found for this given Id..");
			return;
		}
		System.out.println("Enter new Guest Name: ");
		String newGuestName = scanner.next();
		System.out.println("Enter new Room Number: ");
		int newRoomNumber = scanner.nextInt();
		System.out.println("Enter new Contact Number:");
		String newContactNumber = scanner.next();

		String sql = "update hotel_db.reservations set guest_name='" + newGuestName + "'," + "room_number="
				+ newRoomNumber + "," + "contact_number='" + newContactNumber + "'" + "where reservation_id="
				+ reservationId;
		try {
			int count = statement.executeUpdate(sql);
			if (count > 0) {
				System.out.println("Reservation Updated Successfullu!!");
			} else {
				System.out.println("Reservation Update failed!!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void deleteReservation(Connection connection, Scanner scanner, Statement statement) {
		System.out.println("Enter Reservation Id:");
		int reservationId = scanner.nextInt();

		if (!reservationExist(connection, reservationId, statement)) {
			System.out.println("Reservation not found for this given Id..");
			return;

		}
		String sql = "delete from hotel_db.reservations where reservation_id=" + reservationId;

		try {
			int count = statement.executeUpdate(sql);
			if (count > 0) {
				System.out.println("Reservation deleted successfully!!");
			} else {
				System.out.println("Reservation deletion failed!!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static boolean reservationExist(Connection connection, int reservationId, Statement statement) {
		String sql = "select reservation_id from hotel_db.reservations where reservation_id=" + reservationId;
		try {
			ResultSet rs = statement.executeQuery(sql);
			return rs.next();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private static void exit() throws InterruptedException {
		System.out.print("Exiting system");
		int i = 5;
		while (i != 0) {
			System.out.print(".");
			Thread.sleep(450);
			i--;
		}
		System.out.println();
		System.out.println("Thank you for using Hotel Reservation System!!");

	}

}
