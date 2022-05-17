package com.myapp.dtbassethelper;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.SetOptions;
import com.myapp.GlobalVariables;
import com.myapp.model.EnWord;
import com.myapp.model.ExampleDetail;
import com.myapp.model.Meaning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    FirebaseDatabase rootNode; //f_instanse
    DatabaseReference userref; //f_db
    private static DatabaseAccess instance;
    Cursor c = null;
    public String iduser;
    Map<String, String> user; // Map lưu dữ liệu dạng String : String --> Hoten: Thien
    Map<String, Long> diem; //Firebase sử dụng kiểu Long thay vì Int

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    public ArrayList<EnWord> getAllEnWord() {
        ArrayList<EnWord> list = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("select id, word, pronunciation from en_word", null);
        while (cursor.moveToNext()) {
            list.add(getOneEnWord(cursor.getInt(0)));
        }
        cursor.close();
        return list;
    }

    public ArrayList<EnWord> getAllEnWord_NoPopulate() {
        ArrayList<EnWord> list = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("select id, word, pronunciation from en_word", null);
        while (cursor.moveToNext()) {
            list.add(new EnWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), getOneMeaningOfEnWord(cursor.getInt(0))));
        }
        cursor.close();
        return list;
    }

    public ArrayList<EnWord> getSavedWord_NoPopulateFromIdList(ArrayList<Integer> listId) {
        ArrayList<EnWord> list = new ArrayList<>();
        for (int id : listId) {
            Cursor cursor;
            cursor = db.rawQuery("select id, word, pronunciation from en_word where id = " + id, null);
            while (cursor.moveToNext()) {
                list.add(new EnWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), getOneMeaningOfEnWord(cursor.getInt(0))));
            }
            cursor.close();
        }
        return list;
    }

    public ArrayList<EnWord> getAllEnWord_NoPopulateWithOffsetLimit(int offset, int limit) {
        ArrayList<EnWord> list = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("select id, word, pronunciation from en_word limit " + limit + " offset " + offset, null);
        while (cursor.moveToNext()) {
            list.add(new EnWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), getOneMeaningOfEnWord(cursor.getInt(0))));
        }
        cursor.close();
        return list;
    }

    public ArrayList<EnWord> searchEnWord_NoPopulateWithOffsetLimit(String query, int offset, int limit) {
//        System.out.println("====="+query);
        ArrayList<EnWord> list = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("select id, word, pronunciation from en_word where word like '" + query + "%' limit " + limit + " offset " + offset, null);
        while (cursor.moveToNext()) {
            list.add(new EnWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), getOneMeaningOfEnWord(cursor.getInt(0))));
        }
        cursor.close();
        return list;
    }

    public ArrayList<EnWord> getFakeSavedWord_NoPopulateWithOffsetLimit(int offset, int limit) {
        ArrayList<EnWord> list = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("select id, word, pronunciation from en_word limit " + limit + " offset " + offset, null);
        while (cursor.moveToNext()) {
            list.add(new EnWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), getOneMeaningOfEnWord(cursor.getInt(0))));
        }
        cursor.close();
        return list;
    }

    public EnWord getOneEnWord(int id) {
        EnWord enWord = new EnWord();
        Cursor cursor;
        cursor = db.rawQuery("select * from en_word where id = " + id, new String[]{});
        while (cursor.moveToNext()) {
            enWord.setId(id);
            enWord.setWord(cursor.getString(1));
            enWord.setPronunciation(cursor.getString(2));
            enWord.setListMeaning(getAllMeaningOfEnWord(id));
        }
//        System.out.println("-----------"+enWord.toString());
        cursor.close();
        return enWord;
    }

    public EnWord getOneEnWord(String keyword) {
        EnWord enWord = null;
        Cursor cursor;
        cursor = db.rawQuery("select * from en_word where word = '" + keyword + "'", null);
        while (cursor.moveToNext()) {
            enWord = new EnWord();
            enWord.setId(cursor.getInt(0));
            enWord.setWord(cursor.getString(1));
            enWord.setPronunciation(cursor.getString(2));
        }
//        System.out.println("-----------"+enWord.toString());
        cursor.close();
        return enWord;
    }

    public ArrayList<Meaning> getOneMeaningOfEnWord(int enWordId) {
        ArrayList<Meaning> listMeaning = new ArrayList<Meaning>();
        Cursor cursor;
        cursor = db.rawQuery("select meaning from meaning where en_word_id = " + enWordId + " limit 1", new String[]{});
        while (cursor.moveToNext()) {
            Meaning meaning = new Meaning();
//            meaning.setId(cursor.getInt(0));
            meaning.setMeaning(cursor.getString(0));
            listMeaning.add(meaning);
            break;
        }

        cursor.close();
        return listMeaning;
    }

    public ArrayList<Meaning> getAllMeaningOfEnWord(int enWordId) {
        ArrayList<Meaning> listMeaning = new ArrayList<Meaning>();
        Cursor cursor;
        cursor = db.rawQuery("select meaningTB.id, part_of_speech.name, meaning from (select id, part_of_speech_id, meaning from meaning " +
                "where en_word_id = " + enWordId + ") as meaningTB inner join part_of_speech on part_of_speech.id = meaningTB.part_of_speech_id ", new String[]{});

        while (cursor.moveToNext()) {
            Meaning meaning = new Meaning();

            meaning.setId(cursor.getInt(0));
            meaning.setEnWordId(enWordId);
            meaning.setPartOfSpeechName(cursor.getString(1));
            meaning.setMeaning(cursor.getString(2));
            meaning.setListExampleDetails(getAllExampleDetailOfMeaning(cursor.getInt(0)));

            listMeaning.add(meaning);
        }
        cursor.close();
        return listMeaning;
    }

    public ArrayList<ExampleDetail> getAllExampleDetailOfMeaning(int meaning_id) {
        ArrayList<ExampleDetail> listExampleDetail = new ArrayList<ExampleDetail>();
        Cursor cursor;
//        c = db.rawQuery("select example_detail.id, example, example_meaning from meaning inner join example_detail on meaning.id = example_detail.meaning_id where meaning_id = " + meaning_id, new String[]{});
        cursor = db.rawQuery("select example_detail.id, example, example_meaning from example_detail where meaning_id = " + meaning_id, new String[]{});

        while (cursor.moveToNext()) {
            ExampleDetail exampleDetail = new ExampleDetail();
            exampleDetail.setId(cursor.getInt(0));
            exampleDetail.setMeaningId(meaning_id);
            exampleDetail.setExample(cursor.getString(1));
            exampleDetail.setExampleMeaning(cursor.getString(2));

            listExampleDetail.add(exampleDetail);
        }
        cursor.close();
        return listExampleDetail;
    }

    public List<String> getWord() {
        List<String> list = new ArrayList<>();
        c = db.rawQuery("select * from en_word", new String[]{});
        while (c.moveToNext()) {
            String id = c.getColumnName(1);
            String word = c.getString(1);
            System.out.println("------------" + word);
            list.add(word);
        }
        return list;
    }


    public Boolean insertData(String iduser, String hoten, String email, String sdt, int diem) {
        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_User", iduser);
        contentValues.put("HoTen", hoten);
        contentValues.put("Point", diem);
        contentValues.put("Email", email);
        contentValues.put("SDT", sdt);
        long result = db.insert("User", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Boolean checktaikhoan(String email) {
        db = openHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from User where Email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public void CapNhatUser(String iduser) {

        //Kiểm tra xem dữ liệu đã có trong SQLite chưa
        db = openHelper.getWritableDatabase();
        //Lấy dữ liệu từ Firebase xuống
        rootNode = FirebaseDatabase.getInstance();
        userref = rootNode.getReference("User").child(iduser);
        Cursor cursor = db.rawQuery("Select * from User where ID_User = ?", new String[]{iduser});
        if (cursor.getCount() > 0) {
            //Cập Nhật User từ FireBase
            //TH1: Đã có dữ liệu ở SQLite
            userref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = (Map<String, String>) dataSnapshot.getValue();
                    diem = (Map<String, Long>) dataSnapshot.getValue();

                    db = openHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("HoTen", user.get("hoTen"));

                    int Point = diem.get("point").intValue();
                    contentValues.put("Point", Point);
                    contentValues.put("SDT", user.get("sdt"));
                    //db.rawQuery("Select * from User where ID_User = ?", new String[]{iduser});
                    db.update("User", contentValues, "ID_User = ?", new String[]{iduser});
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        } else {
            //Cập Nhật User từ FireBase
            //TH2: Chưa có dữ liệu ở SQLite
            rootNode = FirebaseDatabase.getInstance();
            userref = rootNode.getReference("User").child(iduser);

            userref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = (Map<String, String>) dataSnapshot.getValue();
                    diem = (Map<String, Long>) dataSnapshot.getValue();

                    db = openHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("ID_User", iduser);
                    contentValues.put("HoTen", user.get("hoTen"));
                    int Point = diem.get("point").intValue();
                    contentValues.put("Point", Point);
                    contentValues.put("Email", user.get("email"));
                    contentValues.put("SDT", user.get("sdt"));
                    db.insert("User", null, contentValues);
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
    }

    public Boolean capnhatthongtin(String iduser, String hoten, String sdt) {
        rootNode = FirebaseDatabase.getInstance();
        userref = rootNode.getReference("User").child(iduser);

        userref.child("hoTen").setValue(hoten);
        userref.child("sdt").setValue(sdt);


        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("HoTen", hoten);
        contentValues.put("SDT", sdt);
        Cursor cursor = db.rawQuery("Select * from User where ID_User = ?", new String[]{iduser});
        if (cursor.getCount() > 0) {
            long result = db.update("User", contentValues, "ID_User = ?", new String[]{iduser});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void capnhatdiem0(String iduser, int Point, int PointPlus) {

        //Cập Nhật User lên FireBase
        rootNode = FirebaseDatabase.getInstance();
        userref = rootNode.getReference("User").child(iduser).child("point");
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                long value = dataSnapshot.getValue(Long.class);
                Log.d(TAG, "Value is: " + value);
                db = openHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("Point", value + 0);
                Cursor cursor = db.rawQuery("Select * from User where ID_User = ?", new String[]{iduser});
                if (cursor.getCount() > 0) {
                    long result = db.update("User", contentValues, "ID_User = ?", new String[]{iduser});
                    if (result == -1) {
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
//        ValueEventListener changeListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String title = dataSnapshot.child("iduser").child("point").getValue(String.class);
//                Log.d(TAG, title);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };


//        for (DataSnapshot child : userref.getChildren()) {
//            Log.i(TAG, child.getKey());
//            Log.i(TAG, child.getValue(String.class));
//        }

        //userref.child("point").setValue(Point + PointPlus);
//        userref.child(iduser).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                User user = dataSnapshot.getValue(User.class);
//
//                //long point1 = (long) dataSnapshot.getValue(User.class);
//                Log.d(TAG, "User name: " + user.getHoTen() + ", email " + user.getEmail());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//// Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
        //Cập nhật dữ liệu lên SQLite
//        db = openHelper.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("Point", Point + PointPlus);
//        Cursor cursor = db.rawQuery("Select * from User where ID_User = ?", new String[]{iduser});
//        if (cursor.getCount() > 0) {
//            long result = db.update("User", contentValues, "ID_User = ?", new String[]{iduser});
//            if (result == -1) {
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return false;
//        }
    }

    public Boolean capnhatdiem(String iduser, int Point, int PointPlus) {

        //Cập Nhật User lên FireBase
        rootNode = FirebaseDatabase.getInstance();
        userref = rootNode.getReference("User").child(iduser);
        userref.child("point").setValue(Point + PointPlus);

        //Cập nhật dữ liệu lên SQLite
        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Point", Point + PointPlus);
        Cursor cursor = db.rawQuery("Select * from User where ID_User = ?", new String[]{iduser});
        if (cursor.getCount() > 0) {
            long result = db.update("User", contentValues, "ID_User = ?", new String[]{iduser});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

//    public boolean synchSavedWordToSQLite(String userId, ArrayList<Integer> listSavedWordId) {
//        db = openHelper.getWritableDatabase();
//        for(int wordId:listSavedWordId){
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("en_word_id", wordId);
//            contentValues.put("user_id", userId);
//            Cursor cursor = db.rawQuery("Select * from saved_word where user_id = ?", new String[]{userId});
//            if (cursor.getCount() > 0) {
//                long result = db.update("saved_word", contentValues, "ID_User = ?", new String[]{userId});
//                if (result == -1) {
//                    return false;
//                } else {
//                    return true;
//                }
//            } else {
//
//                return false;
//            }
//        }
//
//        return true;
//    }


    public boolean synchSavedWordToSQLite(String userId, ArrayList<Integer> listSavedWordId) {
        db = openHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("delete from saved_word where user_id = ?", new String[]{userId});
        for (int wordId : listSavedWordId) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("en_word_id", wordId);
            contentValues.put("user_id", userId);
            cursor = db.rawQuery("select * from saved_word where user_id = ? and en_word_id = " + wordId, new String[]{userId});
            if (cursor.getCount() > 0) {

            } else {
                long result = db.insert("saved_word", null, contentValues);
            }
            cursor.close();
        }
        ArrayList<Integer> list = getListSavedWordIdFromSQLite(userId);

        return true;
    }

    public ArrayList<Integer> getListSavedWordIdFromSQLite(String userId) {
        ArrayList<Integer> list = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("select en_word_id from saved_word where user_id = '" + userId + "'", null);
        while (cursor.moveToNext()) {
            list.add(cursor.getInt(0));
        }
        cursor.close();
        return list;
    }


    public boolean synchSavedWordToFirebase(String userId) {
        db = openHelper.getWritableDatabase();
        ArrayList<Integer> listSavedWordId = getListSavedWordIdFromSQLite(userId);

        for (int wordId : listSavedWordId) {
            // xóa trước
            GlobalVariables.db.collection("saved_word").document(userId + wordId + "")
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            GlobalVariables.listSavedWordId.remove(GlobalVariables.listSavedWordId.indexOf(wordId));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
// xóa xong thêm

            HashMap<String, Object> map = new HashMap<>();
            map.put("user_id", userId);
            map.put("word_id", wordId);
            GlobalVariables.db.collection("saved_word")
                    .document(userId + wordId + "").set(map, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //them ca vao trong nay cho de dung
                            GlobalVariables.listSavedWordId.add(wordId);
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        return true;
    }

    public boolean saveOneWord(String userId, int wordId) {
        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("en_word_id", wordId);
        long result = db.insert("saved_word", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void unSaveOneWord(String userId, int wordId) {
//        db = openHelper.getWritableDatabase();
////        ContentValues contentValues = new ContentValues();
////        contentValues.put("user_id", userId);
////        contentValues.put("en_word_id", wordId);
//        Cursor result = db.rawQuery("delete from saved_word where en_word_id = "+wordId+" and user_id = '"+userId+"'", null);
        db = openHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Log.d(TAG, "We Are Trying to Delete Item From DataBase!!");
            Log.d(TAG, "this is an item: " + userId + ", " + wordId);
            Log.d(TAG, "this is an item: " + "delete from saved_word where user_id = '" + userId + "' and en_word_id = " + wordId);
            db.execSQL("DELETE FROM saved_word WHERE user_id ='" + userId + "' and en_word_id = " + wordId);
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete user from database" + e.toString());
        }

        ArrayList<Integer> list = getListSavedWordIdFromSQLite(userId);
    }

    public String getCurrentUserId__OFFLINE() {
        db = openHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select user_id from current_user", new String[]{});
        String userid = "";
        while (c.moveToNext()) {
            userid = c.getString(0);
        }
        c.close();
        return userid;
    }

    public boolean setCurrentUserId__OFFLINE(String userId) {
        db = openHelper.getWritableDatabase();
        db.execSQL("DELETE FROM current_user");
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        long result = db.insert("current_user", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void removeCurrentUserId__OFFLINE() {
        db = openHelper.getWritableDatabase();
        db = openHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Log.d(TAG, "We Are Trying to Delete Item From DataBase!!");
            db.execSQL("DELETE FROM current_user");
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete user from database" + e.toString());
        }

    }
}
