package com.myapp.daoSQLServer;

import com.myapp.jdbc.JDBCController;
import com.myapp.model.ExampleDetail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ExampleDetailDAO {
    private JDBCController jdbcController = new JDBCController();
    private Connection connection;

    public ExampleDetailDAO() {
        connection = jdbcController.ConnnectionData(); // Tạo kết nối tới database
    }

    public ArrayList<ExampleDetail> getAllExampleDetailOfMeaning(int meaning_id) throws SQLException{
        ArrayList<ExampleDetail> listExampleDetail = new ArrayList<ExampleDetail>();

        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "select example_detail.id, meaning_id, example, example_meaning from meaning inner join example_detail on meaning.id= example_detail.meaning_id where meaning_id = " + meaning_id;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            ExampleDetail exampleDetail = new ExampleDetail();
            exampleDetail.setId(rs.getInt("id"));
            exampleDetail.setMeaningId(rs.getInt("meaning_id"));
            exampleDetail.setExample(rs.getString("example"));
            exampleDetail.setExampleMeaning(rs.getString("example_meaning"));

            listExampleDetail.add(exampleDetail);
        }
        connection.close();// Đóng kết nối
        return listExampleDetail;
    }
}
