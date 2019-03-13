/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetaPraim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lesya
 * класс, предназначенный для работы с БД
 */
public class DBConnect {
    private String url = "jdbc:derby://localhost:1527/MetaPrime";
    private String user = "meta";
    private String password = "meta";
    private Connection conn;
    private Statement st;
    private ResultSet rs;
    
    DBConnect() {
        connect();
    }
    
    public void connect() {  //метод, обеспечивающий подключение к БД
        try {
            conn = DriverManager.getConnection(url, user, password);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public String selectDep() {  //возвращает select-запрос для выборки названий отделов
        return "SELECT DEPARTMENTS.NAME_DEP FROM DEPARTMENTS";
    }
    
    public String selectEmpl() {  //возвращает select-запрос для выборки имен сотрудников
        return "SELECT EMPLOYEE.NAME_EMPL FROM EMPLOYEE";
    }
    
    public String selectEmplByDep(String dep) {  //возвращает select-запрос для выборки имен сотрудников по названию отдела
        return "SELECT EMPLOYEE.NAME_EMPL FROM EMPLOYEE JOIN DEPARTMENTS ON EMPLOYEE.ID_DEP = DEPARTMENTS.ID_DEP\n" +
               "WHERE DEPARTMENTS.NAME_DEP = '" + dep + "'";
    }
    
    public int nextIdDep() {  //возвращает следующий свободный id в таблице отделов
        int id = 0;
        String sql = "SELECT MAX(DEPARTMENTS.ID_DEP) FROM DEPARTMENTS";
        try {
            rs = st.executeQuery(sql);
            rs.absolute(1); 
            id = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println("Error 1: " + ex.getMessage());
        }
        return (id + 1);
    }
    
    public int nextIdEmpl() {  //возвращает следующий свободный id в таблице сотрудников
        int id = 0;
        String sql = "SELECT MAX(EMPLOYEE.ID_EMPL) FROM EMPLOYEE";
        try {
            rs = st.executeQuery(sql);
            rs.absolute(1); 
            id = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println("Error 2: " + ex.getMessage());
        }
        return (id + 1);
    }
    
    public int getFK(String dep) { //возвращает fk в таблице отделов по названию отдела
        int id = 0;
        String sql = "SELECT DEPARTMENTS.ID_DEP FROM DEPARTMENTS WHERE DEPARTMENTS.NAME_DEP = '" + dep + "'";
        try {
            rs = st.executeQuery(sql);
            rs.absolute(-1); 
            id = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println("Error 3: " + ex.getMessage());
        }
        return id;
    }
    
    public String insertDep(String value) {  //возвращает insert-запрос для таблицы отделов
        return "INSERT INTO DEPARTMENTS VALUES (" + nextIdDep() + ", '" + value + "')";
    }
    
    public String insertEmpl(String name, String dep) {  //возвращает insert-запрос для таблицы сотрудников
        return "INSERT INTO EMPLOYEE VALUES (" + nextIdEmpl() + ", '" + name + "', " + getFK(dep) + ")";
    }
        
    public void insertSt(String sql) {  //выполняет insert-запрос
        try {
            st.addBatch(sql);
            st.executeBatch();
        } catch (SQLException ex) {
            System.out.println("Error 4: " + ex.getMessage());
        }
    }
    
    public String selectSt(String sql) {  //выполняет select-запрос
        StringBuilder ans = new StringBuilder();
        try {
            rs = st.executeQuery(sql);
            rs.absolute(0);
            while (rs.next()) {
                ans.append(rs.getString(1)).append("\n");
            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println("Error 5: " + ex.getMessage());;
        }
        return ans.toString();
    }
}
