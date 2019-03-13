/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetaPraim;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author lesya
 * класс, предназначенный для создания оконного приложения + обеспечения работоспособности всех оконных форм
 */
public class ClientApi extends JFrame {

    /**
     * @param args the command line arguments
     */
    
    private JTextArea resultArea, addAreaDep, addAreaEmp;
    private JButton showDep, showEmpl, addNewDep, addNewEmpl;
    private JScrollPane scrollPaneRes, scrollPaneAddEm, scrollPaneAddDep;
    private Container cont;
    private DBConnect DBCon;
    
    private ClientApi() {
        setSize(445, 300);
        setTitle("Departmental accounting");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addComponents();
        setResizable(false);
        setVisible(true);
        repaint();
    }
    
    public void addComponents() { //создание оконного приложения
        DBCon = new DBConnect();
        try {
            cont = getContentPane();
            cont.setLayout(null);

            resultArea = new JTextArea();
            resultArea.setLineWrap(true);
            scrollPaneRes = new JScrollPane(resultArea);
            scrollPaneRes.setBounds(15, 15, 300, 80);
            cont.add(scrollPaneRes);
            
            showDep = new JButton("Hierarchy");
            showDep.setBounds(330, 15, 100, 40);
            cont.add(showDep);
            showDep.addActionListener((ActionEvent e) -> {  //вывод всех отделов
                resultArea.setText(DBCon.selectSt(DBCon.selectDep()).toLowerCase());
            });
            
            showEmpl = new JButton("Employee");
            showEmpl.setBounds(330, 60, 100, 40);
            cont.add(showEmpl);
            showEmpl.addActionListener((ActionEvent e) -> {  //вывод всех сотрудников
                if (addAreaDep.getText().isEmpty()) { 
                resultArea.setText(DBCon.selectSt(DBCon.selectEmpl()));
                } else {
                    resultArea.setText(DBCon.selectSt(DBCon.selectEmplByDep(addAreaDep.getText().toUpperCase())));
                }
            });
            
            addAreaDep = new JTextArea();
            addAreaDep.setLineWrap(true);
            scrollPaneAddEm = new JScrollPane(addAreaDep);
            scrollPaneAddEm.setBounds(15, 115, 200, 50);
            cont.add(scrollPaneAddEm);
            addAreaDep.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addAreaDep.setText(null);
                }
            });
            
            addAreaEmp = new JTextArea();
            addAreaEmp.setLineWrap(true);
            scrollPaneAddDep = new JScrollPane(addAreaEmp);
            scrollPaneAddDep.setBounds(230, 115, 200, 50);
            cont.add(scrollPaneAddDep);
            addAreaEmp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addAreaEmp.setText(null);
                }
            });
            
            addNewDep = new JButton("New department");
            addNewDep.setBounds(50, 180, 120, 40);
            cont.add(addNewDep);
            addNewDep.addActionListener((ActionEvent e) -> {
                if (!addAreaDep.getText().isEmpty()) {
                    DBCon.insertSt(DBCon.insertDep(addAreaDep.getText()));  //добавление нового отдела
                    addAreaDep.setText("New department was added");
                } else {
                    addAreaDep.setText("This field can not be empty!");
                }
            });
            
            addNewEmpl = new JButton("New employee");
            addNewEmpl.setBounds(270, 180, 120, 40);
            cont.add(addNewEmpl);
            addNewEmpl.addActionListener((ActionEvent e) -> {  //добавление нового сотрудника
                if (addAreaDep.getText().isEmpty() || addAreaEmp.getText().isEmpty()) {
                    addAreaDep.setText("These fields can not be empty!");
                } else {
                    DBCon.insertSt(DBCon.insertEmpl(addAreaEmp.getText(), addAreaDep.getText().toUpperCase()));
                    addAreaDep.setText(null);
                    addAreaEmp.setText("New employee was added");                    
                }
            });
           
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        new ClientApi();
    }
    
}
