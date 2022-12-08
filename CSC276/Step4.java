//To connect, your code should include the elements below. (All this code is also publicly available in demoRdb.java.)
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SQLConnector{

	private Connection dbConnection; //the connection to the db

	public static void main(String[] args) {
		SQLConnector sqlConn = new SQLConnector();
		sqlConn.go();
	}
  
  private void go(){
     connectToDB();
     insert(); 
  }

  private void connectToDB(){ //need to be connected to the JAR 
    try { //you want try/catch everywhere because smth always goes wrong with sql
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance(); //connecting to the driver or something 
        dbConnection = DriverManager.getConnection("jdbc:mysql://cscmysql.lemoyne.edu/game276[username]?user=[username]&password=[password]"); //plug in your DB name, username, and password
        //the above code makes dbConnection a connection to the server we need
    }
    //all the code inside a catch block is error handling. the main code is in the try block.
    catch (SQLException ex) { 
        dbConnection = null;
        reportSQLError(ex, "Couldn't connect to DB.");
    }
    catch (Exception ex) {
        dbConnection = null;
        System.out.println(ex);
    }
 }

    private void insert(){ //there's probably a better way to do this
        //the way you do this will depend on your database structure.
        //let's assume you have a table Purchases with two fields: purchaseId, purchaseName, and price.
        //purchaseId is autonumbered, so we do not directly insert it 
        String insert = "insert into Purchases (purchaseName, price) VALUES ('Laptop', 459.99)";
        //your final code should have variables or function calls, for example: VALUES ('" + purchaseName + "'," + getPrice() + ");";
			PreparedStatement statement = null; //a prepared statment turns your string into a command for the DB (probably). it also prevents sql injection.
            try {
                    statement = dbConnection.prepareStatement(insert);
                    statement.executeUpdate();
                }
			  catch (SQLException ex) {
                reportSQLError(ex, "Could not insert.");
            }
            closeStatement(statement);
    }

    private void closeStatement(Statement statement){
        if (statement != null) {
			try {
				statement.close();
			}
			catch (SQLException sqlEx) {
			}
			statement = null;
		}
    }

    private void insertWithParameters() { //haven't went into this one yet 
		String contactName = getStringFromUser("Enter contact name to add: ");
		final String INSERT_CONTACT = "INSERT INTO Contact (name) VALUES (?);";
		PreparedStatement stmt = null;
		try {
			stmt = dbConnection.prepareStatement(INSERT_CONTACT);
			stmt.setString(1, contactName);
			int rowCount = stmt.executeUpdate();
			if (rowCount == 0)
				System.out.println("Logic error: did not insert new contact data <" + contactName + ">.");
			else if (rowCount > 1)
				System.out.println("Logic error: inserted " + rowCount + " rows of contact data for  <" + contactName + ">.");
		}
		catch (SQLException ex) {
			reportSQLError(ex, "insertContact");
		}
		closeStatement(stmt);
	}

    private void reportSQLError(SQLException ex, String methodName) {
		System.out.println("SQLException in " + methodName + ":");
		System.out.println("SQLException: " + ex.getMessage());
		System.out.println("SQLState: " + ex.getSQLState());
		System.out.println("VendorError: " + ex.getErrorCode());
	}

}