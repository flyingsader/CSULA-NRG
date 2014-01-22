
import java.sql.*;

import javax.swing.JFrame;

public class ConnectDatabase{

	public static void main(String args[]){


		try {

			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");
			/*This is the connection url for MySQL database. 
			 *Each driver has a different syntax for the url.
			 *In our case, we provide a host, a port and a database name.
			 **/ 
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");
			/*
			 * The createStatement() method of the connection object creates 
			 * a Statement object for sending SQL statements to the database
			 * The createStatement() method of the connection object executes the given SQL statement, 
			 * which returns a single ResultSet object. 
			 * */
			Statement stmt = connection.createStatement();
			/*try{
				String table = 
						"CREATE TABLE Employee(Emp_Id INT(10), Emp_Name VARCHAR(20) NOT NULL, Emp_Hire_Date DATE NOT NULL, Emp_Birth_Date DATE NOT NULL, Emp_Sex VARCHAR(5) NOT NULL, Emp_Job_Status VARCHAR(5) NOT NULL, Emp_Pay_Type VARCHAR(5) NOT NULL, Emp_Annual_Salary DOUBLE NOT NULL, Emp_Years_Of_Service INT(10) NOT NULL, PRIMARY KEY(Emp_Id))";
				stmt.executeUpdate(table);
				System.out.println("Table creation process successfully!");
			}
			catch(SQLException s){
				System.out.println("Table already exists!");
			}*/

			//BEFORE CONNECTION CLOSE!!!!!
			connection.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void Insert(String lastName, String HireDate, String birthDate,
			String sex, String jobStatus, String payType, double salary, int years) {
		try {

			String url = "jdbc:mysql://localhost:3306/cs245_final";

			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();

			try {
				String insert = "INSERT INTO employee(LastName,Hire,Birthdate,Sex,JobStatus,Pay,AnnualSalary,YearsOfService) VALUES(\'" + lastName + "\', \'" + HireDate + "\', \'"
						+ birthDate + "\', \'" + sex + "\', \'" + jobStatus
						+ "\', \'" + payType + "\', \'" + salary + "\', \'"
						+ years + "\');";
				stmt.executeUpdate(insert);
				System.out.println("New Employee Added");
			} catch (SQLException s) {
				System.out.println("Code Fail!");
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void Delete(int id) {
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				String delete = "DELETE FROM employee WHERE EmployeeID = "
						+ id + ";";
				stmt.executeUpdate(delete);
				System.out.println("Employee  deleted!");
			} catch (SQLException s) {
				System.out.println("Delete failed");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String[][] Search(int id) {
		String[][] Data = null;
		try {
			String url = "jdbc:mysql://localhost:3306/nrg";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				ResultSet count = stmt
						.executeQuery("SELECT count(*) FROM employee WHERE EmployeeID = \'"
								+ id + "\';");
				count.next();
				int size = count.getInt(1);
				Data = new String[size][9];

				ResultSet execute = stmt
						.executeQuery("SELECT * FROM employee WHERE EmployeeID = \'"
								+ id + "\';");
				for (int i = 0; execute.next(); i++) {
					for (int j = 0; j < 9; j++) {
						Data[i][j] = execute.getString(j + 1);
					}
				}
				System.out.println("Search complete");
			} catch (SQLException s) {
				System.out.println("Search failed: Employee(s) with id = " + id
						+ " not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data;
	}

	public static String[][] Search1() {
		String[][] Data = null;
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();

			try {
				ResultSet count = stmt
						.executeQuery("SELECT count(*) FROM employee WHERE Birthdate < '1972-12-03' AND JobStatus = 'FT' AND Pay = 'S' AND YearsOfService > '3';");
				count.next();
				int size = count.getInt(1);
				Data = new String[size][9];

				ResultSet execute = stmt
						.executeQuery("SELECT * FROM employee WHERE Birthdate < '1972-12-03' AND JobStatus = 'FT' AND Pay = 'S' AND YearsOfService > '3';");
				for (int i = 0; execute.next(); i++) {
					for (int j = 0; j < 9; j++) {
						Data[i][j] = execute.getString(j + 1);
					}
				}
				System.out.println("Search complete");
			} catch (SQLException s) {
				System.out.println("Search failed: Employee(s) not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data;
	}

	public static String[][] Search2() {
		String[][] Data = null;
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				ResultSet count = stmt
						.executeQuery("SELECT count(*) FROM employee WHERE Sex = 'F' AND AnnualSalary > '40000.00';");
				count.next();
				int size = count.getInt(1);
				Data = new String[size][9];

				ResultSet execute = stmt
						.executeQuery("SELECT * FROM employee WHERE Sex = 'F' AND AnnualSalary > '40000.00';");
				for (int i = 0; execute.next(); i++) {
					for (int j = 0; j < 9; j++) {
						Data[i][j] = execute.getString(j + 1);
					}
				}
				System.out.println("Search complete");
			} catch (SQLException s) {
				System.out.println("Search failed: Employee(s) not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data;
	}

	public static String[][] Search3() {
		String[][] Data = null;
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				ResultSet count = stmt
						.executeQuery("SELECT count(*) FROM employee WHERE LastName IN (SELECT LastName FROM employee GROUP BY LastName HAVING COUNT(LastName) > 1) AND AnnualSalary > '100000';");
				count.next();
				int size = count.getInt(1);
				Data = new String[size][9];
				ResultSet execute = stmt
						.executeQuery("SELECT * FROM employee WHERE LastName IN (SELECT LastName FROM employee GROUP BY LastName HAVING COUNT(LastName) > 1) AND AnnualSalary > '100000';");
				for (int i = 0; execute.next(); i++) {
					for (int j = 0; j < 9; j++) {

						Data[i][j] = execute.getString(j + 1);
					}
				}
				System.out.println("Search complete");
			} catch (SQLException s) {
				System.out.println("Search failed: Employee(s) not found");
			}


			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data;
	}

	public static String[][] Display() {
		String Data[][] = null;
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();

			try {
				ResultSet count = stmt
						.executeQuery("SELECT count(*) FROM employee;");
				count.next();
				int size = count.getInt(1);

				Data = new String[size][9];
				/*if(size>=100){
					String[][] DataTwo=new String[size*2][9]; 

				}*/
				// size[0].length	
				ResultSet execute = stmt
						.executeQuery("SELECT * FROM employee ORDER BY EmployeeID;");
				for (int i = 0; execute.next(); i++) {
					for (int j = 0; j < 9; j++) {
						Data[i][j] = execute.getString(j + 1);
					}
				}
				System.out.println("Code Executed Successfully!!");
			} catch (SQLException s) {
				System.out.println("ERROR!!!");
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data;
	}

	public static void UpdateId(int id, int id2) {
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				String updateid = "UPDATE employee SET EmployeeID = \'" + id2
						+ "\' WHERE EmployeeID = \'" + id + "\';";
				stmt.executeUpdate(updateid);
				System.out.println("Employee updated");
			} catch (SQLException s) {
				System.out
				.println("Delete failed: Employee id is incorrect/ not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void UpdateLast(int id, String last) {
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;
			System.out.println("Connecting database...");
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				String updatelast = "UPDATE employee SET LastName = \'"
						+ last + "\' WHERE EmployeeID = \'" + id + "\';";
				System.out.println(updatelast);
				stmt.executeUpdate(updatelast);
				System.out.println("Employee updated");
			} catch (SQLException s) {
				s.printStackTrace();
				System.out
				.println("Delete failed: Employee id is incorrect/ not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdateHire(int id, String hire) {
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;


			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				String updatehire = "UPDATE employee SET Hire = \'" + hire
						+ "\' WHERE EmployeeID = \'" + id + "\';";
				stmt.executeUpdate(updatehire);
				System.out.println("Employee updated");
			} catch (SQLException s) {
				System.out
				.println("Delete failed: Employee id is incorrect/ not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdateBirth(int id, String birth) {
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				String updatebirth = "UPDATE employee SET BirthDate = \'"
						+ birth + "\' WHERE EmployeeID = \'" + id + "\';";
				stmt.executeUpdate(updatebirth);
				System.out.println("Employee updated");
			} catch (SQLException s) {
				System.out
				.println("Delete failed: Employee id is incorrect/ not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdateSex(int id, String sex) {
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;


			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				String updatesex = "UPDATE employee SET Sex = \'\"" + sex
						+ "\"\' WHERE EmployeeID = \'" + id + "\';";
				stmt.executeUpdate(updatesex);
				System.out.println("Employee updated");
			} catch (SQLException s) {
				System.out
				.println("Delete failed: Employee id is incorrect/ not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdateStatus(int id, String status) {
		try {	String url = "jdbc:mysql://localhost:3306/cs245_final";
		String username = "root";
		String password = "963Qsb85c";
		Connection connection = null;


		System.out.println("Connecting database...");

		connection = DriverManager.getConnection(url, username, password);
		System.out.println("Database connected!");

		Statement stmt = connection.createStatement();
		try {
			String updatestatus = "UPDATE employee SET JobStatus = \'\""
					+ status + "\"\' WHERE EmployeeID = \'" + id + "\';";
			stmt.executeUpdate(updatestatus);
			System.out.println("Employee updated");
		} catch (SQLException s) {
			System.out
			.println("Delete failed: Employee id is incorrect/ not found");
		}

		connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdatePay(int id, String pay) {
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				String updatepay = "UPDATE employee SET Pay = \'\"" + pay
						+ "\"\' WHERE EmployeeID = \'" + id + "\';";
				stmt.executeUpdate(updatepay);
				System.out.println("Employee updated");
			} catch (SQLException s) {
				System.out
				.println("Delete failed: Employee id is incorrect/ not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdateSalary(int id, double salary) {
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963Qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				String updatesalary = "UPDATE employee SET AnnualSalary = \'"
						+ salary + "\' WHERE EmployeeID = \'" + id + "\';";
				stmt.executeUpdate(updatesalary);
				System.out.println("Employee updated");
			} catch (SQLException s) {
				System.out
				.println("Delete failed: Employee id is incorrect/ not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UpdateYears(int id, int years) {
		try {
			String url = "jdbc:mysql://localhost:3306/cs245_final";
			String username = "root";
			String password = "963qsb85c";
			Connection connection = null;

			System.out.println("Connecting database...");

			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");

			Statement stmt = connection.createStatement();
			try {
				String updateyears = "UPDATE employee SET YearsOfService = \'"
						+ years + "\' WHERE EmployeeID = \'" + id + "\';";
				stmt.executeUpdate(updateyears);
				System.out.println("Employee updated");
			} catch (SQLException s) {
				System.out
				.println("Delete failed: Employee id is incorrect/ not found");
			}

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
