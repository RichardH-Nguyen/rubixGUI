import java.sql.*;

/**
 * Created by NinjaHunter on 4/15/17.
 */
public class dataBase {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cubes";
    static final String USER = "richard";
    static final String PASS = System.getenv("MYSQL_PW");

    private static Connection conn = null;
    private static Statement statement = null;
    private static ResultSet rs = null;
    private static recordsDataModel recordModel;

    public static boolean setup(){
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASS);
            statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS records ( Id INT NOT NULL AUTO_INCREMENT, SolvedBy varchar(50), TimeToSolve double, PRIMARY KEY(Id))");
        }catch (SQLException e){
            System.out.println("error");
        }catch (ClassNotFoundException cnf){
            System.out.println("Class not found");
        }
        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }catch (SQLException e){
            System.out.println("error");
        }
        return false;
    }

    protected static void newEntry(String name, double time){
        try {
            String newEntry = "INSERT INTO records VALUES (?,?)";
            PreparedStatement entryStatement = conn.prepareStatement(newEntry);
            entryStatement.setString(1, name);
            entryStatement.setDouble(2, time);

            entryStatement.execute();

        }catch(SQLException e){
            System.out.println();
        }
    }

    protected static void updateTime(String name, double time){
        try {
            String updateTime = "UPDATE records SET TimeToSolve = ? WHERE SolvedBy = ?";
            PreparedStatement updateTimeStatement = conn.prepareStatement(updateTime);
            updateTimeStatement.setDouble(1, time);
            updateTimeStatement.setString(2, name);

            updateTimeStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println();
        }
    }
    public static boolean getAllRecords(){

        try{

            if (rs!=null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM records";
            rs = statement.executeQuery(getAllData);

            if (recordModel == null) {
                //If no current movieDataModel, then make one
                recordModel = new recordsDataModel(rs);
            } else {
                //Or, if one already exists, update its ResultSet
                recordModel.updateResultSet(rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading movies");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }
    private static boolean recordsTableExists() throws SQLException {

        String checkExists = "SHOW TABLES LIKE records";   //Can query the database schema
        ResultSet tablesRS = statement.executeQuery(checkExists);
        if (tablesRS.next()) {    //If ResultSet has a next row, it has at least one row... that must be our table
            return true;
        }
        return false;

    }


}
