package com.myapp.daoSQLServer;

import com.myapp.jdbc.JDBCController;
import com.myapp.model.Meaning;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MeaningDAO {
    private JDBCController jdbcController = new JDBCController();
    private Connection connection;

    public MeaningDAO() {
        connection = jdbcController.ConnnectionData(); // Tạo kết nối tới database
    }

    public ArrayList<Meaning> getAllMeaningOfEnWord(int enWordId) throws SQLException{
        ArrayList<Meaning> listMeaning = new ArrayList<Meaning>();

        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "select meaning.id, en_word_id, part_of_speech.name, meaning from meaning inner join part_of_speech on part_of_speech.id = meaning.part_of_speech_id where en_word_id = " + enWordId;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            Meaning meaning = new Meaning();

            meaning.setId(rs.getInt("id"));
            meaning.setEnWordId(rs.getInt("en_word_id"));
            meaning.setPartOfSpeechName(rs.getString("name"));
            meaning.setMeaning(rs.getString("meaning"));
            meaning.setListExampleDetails(new ExampleDetailDAO().getAllExampleDetailOfMeaning(rs.getInt("id")));

            listMeaning.add(meaning);
        }

        connection.close();// Đóng kết nối
        return listMeaning;
    }
}
