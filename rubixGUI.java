import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by su7163if on 4/12/2017.
 */
public class rubixGUI extends JFrame{
    private JPanel rubixPanel;
    private JTable rubixTable;
    private JLabel lblName;
    private JLabel lblTime;
    private JButton btnAdd;
    private JButton btnDelete;
    private JTextField txtName;
    private JTextField txtTime;
    private JButton btnUpdateTime;

    protected rubixGUI(recordsDataModel rdm) {
        setContentPane(rubixPanel);
        pack();
        setVisible(true);


        rubixTable.setModel(rdm);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                double time = Double.parseDouble(txtTime.getText());
                dataBase.newEntry(name, time);
            }
        });
        btnUpdateTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                double time = Double.parseDouble(txtTime.getText());
                dataBase.updateTime(name, time);
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = rubixTable.getSelectedRow();
                if(currentRow == -1){
                    JOptionPane.showMessageDialog(rubixPanel, "Please select a record to delete");
                }

            }
        });
    }


}


