package com.myapp.sqlite.dao;

import java.util.List;

public interface Dao<T> {

    void fillData();

    boolean insertOne(T t);

    boolean updateOne(T t);

    boolean deleteOne(T t);

    List<T> getAll();

    T getOne(String id);

    List<T> search(String keyword);
}
