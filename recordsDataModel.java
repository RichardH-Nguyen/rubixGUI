import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by NinjaHunter on 4/15/17.
 */
public class recordsDataModel extends AbstractTableModel{
    ResultSet resultSet;
    int numberOfRows;
    int numberOfColumns;

    public recordsDataModel (ResultSet rs){
        this.resultSet = rs;
        setup();
    }

    private void setup(){

        countRows();

        try{
            numberOfColumns = resultSet.getMetaData().getColumnCount();

        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }

    }
    public void updateResultSet(ResultSet newRS){
        resultSet = newRS;
        setup();
    }
    private void countRows() {
        numberOfRows = 0;
        try {

            resultSet.beforeFirst();

            while (resultSet.next()) {
                numberOfRows++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }

    }


    @Override
    public int getRowCount() {
        return numberOfRows;
    }

    @Override
    public int getColumnCount() {
        return numberOfColumns;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {

        resultSet.absolute(rowIndex + 1);
        Object o = resultSet.getObject(columnIndex + 1);
        return o;
    } catch (SQLException sqle) {
        // Display the text of the error message in the cell.
        // Hacky - you wouldn't do this in real code.
        // Maybe display a blank table, a user-friendly error message, and log the actual error for the developers
        return sqle.toString();
    }

    }
}
