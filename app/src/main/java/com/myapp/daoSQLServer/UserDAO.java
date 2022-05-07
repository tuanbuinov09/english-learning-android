package com.myapp.daoSQLServer;

import com.myapp.jdbc.JDBCController;
import com.myapp.model.EnWord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private JDBCController jdbcController = new JDBCController();
    private Connection connection;

    public UserDAO() {
        connection = jdbcController.ConnnectionData(); // Tạo kết nối tới database
    }


    public List<EnWord> getSavedWordlist(String username) throws SQLException {
        List<EnWord> list = new ArrayList<>();
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "select en_word_id from saved_word where username='"+username+"'";
        // Thực thi câu lệnh SQL trả về đối tượng ResultSet. // Mọi kết quả trả về sẽ được lưu trong ResultSet
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new EnWordDAO().getOneEnWord(rs.getInt("en_word_id")));
        }
        connection.close();// Đóng kết nối
        return list;
    }

//    public boolean Insert(User objUser) throws SQLException {
//        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
//        String sql = "insert in to User(Name) values(" + objUser.getName() + ")";
//        if (statement.executeUpdate(sql) > 0) { // Dùng lệnh executeUpdate cho các lệnh CRUD
//            connection.close();
//            return true;
//        } else {
//            connection.close();
//            return false;
//        }
//    }
//
//    public boolean Update(User objUser) throws SQLException {
//        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
//        String sql = "Update User set Name = " + objUser.getName() + " where ID = " + objUser.getID();
//        if (statement.executeUpdate(sql) > 0) {
//            connection.close();
//            return true;
//        } else
//            connection.close();
//        return false;
//    }
//
//    public boolean Delete(User objUser) throws SQLException {
//        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
//        String sql = "delete from User where ID = " + objUser.getID();
//        if (statement.executeUpdate(sql) > 0){
//            connection.close();
//            return true;
//        }
//
//        else
//            connection.close();
//        return false;
//    }
}
