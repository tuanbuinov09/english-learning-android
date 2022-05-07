package com.myapp.daoSQLServer;

import com.myapp.jdbc.JDBCController;
import com.myapp.model.EnWord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EnWordDAO {
    private JDBCController jdbcController = new JDBCController();
    private Connection connection;

    public EnWordDAO() {
        connection = jdbcController.ConnnectionData(); // Tạo kết nối tới database
    }

    public EnWord getOneEnWord(int id) throws SQLException{
        EnWord enWord = new EnWord();

        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "select * from en_word where id = " + id;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            enWord.setId(id);
            enWord.setWord(rs.getString("word"));
            enWord.setPronunciation(rs.getString("pronunciation"));
            enWord.setListMeaning(new MeaningDAO().getAllMeaningOfEnWord(id));
        }
        connection.close();// Đóng kết nối
        return enWord;
    }

    public List<EnWord> getAllEnWord() throws SQLException {
        List<EnWord> list = new ArrayList<>();
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "select id from en_word";
        // Thực thi câu lệnh SQL trả về đối tượng ResultSet. // Mọi kết quả trả về sẽ được lưu trong ResultSet
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(getOneEnWord(rs.getInt("id")));// Đọc dữ liệu từ ResultSet
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
