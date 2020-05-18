package view;

import model.entities.User;
import utils.StockColors;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;


public class SharesListView extends JPanel{
    private static final String[] columnNames = { "Users", "Email", "Stock Value", "Total Balance"};
    private String[][] userList = {{ "", "", "", ""}};
    private static final int anchuraPanel = 1080;
    private static final int alturaPanel = 768;
    protected StockColors color;
    private JTable jtSharesList;
    private JButton jbReturn;
    private JPanel jpCenter;
    private JPanel jpSouth;
    JScrollPane scrollPane;
    private ListSelectionModel selectionModel;

    public SharesListView () {
        color = new StockColors();
        initUI();
    }

    private void initUI() {
        //Main Panel
        this.setLayout(new BorderLayout());
        this.setBackground(color.getWHITE());

        //Center panel with data table
        jpCenter = new JPanel(new BorderLayout());
        jpCenter.setBackground(color.getWHITE());
        String[][] userRow = {};
        jtSharesList = new JTable(userList, columnNames);
        jtSharesList.setRowHeight(40);
        jtSharesList.setBackground(Color.WHITE);
        selectionModel = jtSharesList.getSelectionModel();
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(jtSharesList);
        scrollPane.setBackground(color.getWHITE());
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        JTableHeader tableHeader = jtSharesList.getTableHeader();
        tableHeader.setBackground(color.getWHITE());
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jpCenter.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        jpCenter.add(scrollPane);
        this.add(jpCenter, BorderLayout.CENTER);

        //Return button
        jpSouth = new JPanel();
        jpSouth.setLayout(new BoxLayout(jpSouth, BoxLayout.Y_AXIS));
        jbReturn = new JButton();
        jbReturn.setText("RETURN");
        jbReturn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jbReturn.setBackground(color.getYELLOW());
        jbReturn.setAlignmentX(Component.CENTER_ALIGNMENT);
        jbReturn.setBorder(BorderFactory.createMatteBorder(5,40,5,40, color.getYELLOW()));
        jbReturn.setContentAreaFilled(false);
        jbReturn.setOpaque(true);
        jpSouth.add(jbReturn);
        jpSouth.setBackground(Color.WHITE);
        jpSouth.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        this.add(jpSouth, BorderLayout.SOUTH);
    }

    /**
     *  Registers controller for this view
     *
     *  @param controller View Controller
     */
    public void registerController (ListSelectionListener controller) {
        selectionModel.addListSelectionListener(controller);
    }

    /**
     * Returns the selected user nickname
     *
     * @param selectedRow selected user row to return
     * @return user data selected
     */
    public String getSelectedUser(int selectedRow){
        User user = new User();
        user.setNickname(jtSharesList.getModel().getValueAt(selectedRow, 0).toString());
        return user.getNickname();
    }

    /**
     * Sets the new data for the table and updates it
     *
     * @param userList String list with data
     */
    public void setUserList(String[][] userList) {
        this.userList = userList;
    }

    /**
     *  Empty the table
     */
    public void emptyTable(){
        this.jtSharesList.setModel(new DefaultTableModel());
        DefaultTableModel tablemodel = (DefaultTableModel) jtSharesList.getModel();
        tablemodel.setRowCount(0);
    }

    /**
     *  Fills table with userList and columnNames
     */
    public void fillData() {
        this.jtSharesList.setModel(new DefaultTableModel());
        DefaultTableModel model = (DefaultTableModel) jtSharesList.getModel();
        model.setRowCount(0);
        for (String col : columnNames) {
            model.addColumn(col);
        }
        for (String[] row : userList) {
            model.addRow(row);
        }
        this.jtSharesList.setModel(model);
    }

    /**
     *  Return the selectionModel for the JTable
     */
    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }
}
