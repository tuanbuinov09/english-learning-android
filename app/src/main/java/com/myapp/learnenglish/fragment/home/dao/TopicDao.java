package com.myapp.learnenglish.fragment.home.dao;

import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.myapp.learnenglish.fragment.home.model.Topic;

import java.util.List;

public class TopicDao implements Dao<Topic> {
    private DatabaseReference databaseReference;

    public TopicDao() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Topics");
    }

    @Override
    public Query get() {
        return databaseReference.orderByKey();
    }

    @Override
    public void save(Topic topic) {

    }

    @Override
    public void update(Topic topic) {

    }

    @Override
    public void delete(Topic topic) {

    }
}
