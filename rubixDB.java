import javax.print.DocFlavor;
import java.sql.*;
import java.util.Scanner;

/**
 * Created by su7163if on 4/5/2017.
 */
public class rubixDB {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cubes";
    static final String USER = "richard";
    static final String PASS = System.getenv("MYSQL_PW");
    static Scanner stringScanner = new Scanner(System.in);
    static Scanner numScanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);

            Connection connection = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASS);
            Statement statement = connection.createStatement();
            //If records table doesn't exist, creates new one.
            statement.execute("CREATE TABLE IF NOT EXISTS records ( Id INT NOT NULL AUTO_INCREMENT, SolvedBy varchar(50), TimeToSolve double, PRIMARY KEY(Id))");
            ResultSet rs = statement.executeQuery("SELECT * from records");

            //new entry prepared statement
            String newEntry = "INSERT INTO records VALUES (?,?)";
            PreparedStatement entryStatement = connection.prepareStatement(newEntry);

            //Update time prepared statment.
            String updateTime = "UPDATE records SET TimeToSolve = ? WHERE SolvedBy = ?";
            PreparedStatement updateTimeStatement = connection.prepareStatement(updateTime);

            //prepared statement using the near operator to search database.
            String searchName = "SELECT * FROM records WHERE SolvedBy LIKE ? '%'";
            PreparedStatement searchNameStatement = connection.prepareStatement(searchName);
            //Adding data to table.
            /*statement.execute("INSERT INTO records VALUES ('CubeStormer II robot', 5.270)");
            statement.execute("INSERT INTO records VALUES ('Fakhri Raihaan (Using his feet)', 27.93)");
            statement.execute("INSERT INTO records VALUES ('Ruxin Liu (Age 3)', 99.33)");
            statement.execute("INSERT INTO records VALUES ('Mats Valk (human record holder)', 6.27)");*/

            //Asks user to select an option 1 or 2
            System.out.println("What do you want to do?");
            System.out.println("1. Create new entry\n2. Update entry time\n3. Search for name");
            int selection = numScanner.nextInt();
            //if they selected 1 they create a new entry for the data table.
            if(selection == 1){
                System.out.println("Enter name:");
                String name = stringScanner.nextLine();
                System.out.println("Enter time in seconds:");
                double time = numScanner.nextDouble();
                entryStatement.setString(1, name);
                entryStatement.setDouble(2, time);

                //executing prepared statement.
                entryStatement.execute();
                System.out.println("Added " + name + "with time: " + time);

                //if they selected 2 all data in table is displayed and they input the name and new time to update.
            }else if(selection == 2){
                while (rs.next()) {
                    System.out.println(rs.getString(1) + " solved the Rubix cube in " + rs.getDouble(2));
                    System.out.println("******");
                }
                System.out.println("Enter name of entry you would like to update:");
                String name = stringScanner.nextLine();
                System.out.println("Enter new time");
                double time = numScanner.nextDouble();
                updateTimeStatement.setDouble(1, time);
                updateTimeStatement.setString(2, name);

                updateTimeStatement.executeUpdate();
                System.out.println(name + " updated with time: " + time);

                //searches database from name that starts with what is typed.
            }else if(selection == 3){
                System.out.println("Enter name to search");
                String name = stringScanner.nextLine();
                searchNameStatement.setString(1, name);

                ResultSet search = searchNameStatement.executeQuery();
                int results = 0;
                //goes through the results of the search and displays them for the user.
                while (search.next()){
                    results++;
                    System.out.println(search.getString(1) + " solved the Rubix cube in " + search.getDouble(2));
                    System.out.println("******");

                }
                //if results variable is greater than 0 then user can choose which one to update the time for.
                if(results > 0){
                    System.out.println("Enter name of entry you would like to update:");
                    String name2 = stringScanner.nextLine();
                    System.out.println("Enter new time");
                    double time = numScanner.nextDouble();
                    updateTimeStatement.setDouble(1, time);
                    updateTimeStatement.setString(2, name2);

                    updateTimeStatement.executeUpdate();
                    System.out.println(name2 + " updated with time: " + time);
                }else{
                    System.out.println("No entries found that match search string.");
                    return;
                }
            }else {
                System.out.println("please enter 1, 2, or 3");
                return;
            }


            updateTimeStatement.close();
            entryStatement.close();
            rs.close();
            statement.close();
            connection.close();
        }catch (SQLException e){
            System.out.println("error\n" + e);
        }catch (ClassNotFoundException cnf){
            System.out.println("Class not found\n" + cnf);
        }

    }
}
