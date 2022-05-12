package com.myapp.learnenglish.fragment.home.dao;

import com.google.firebase.database.Query;

import java.util.List;

public interface Dao<T> {
    Query get();

    void save(T t);

    void update(T t);

    void delete(T t);
}
