//package com.example.powerlogger;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
////import com.example.powerlogger.dao.ExerciseDao;
//import com.example.powerlogger.model.Exercise;
//
//@Database(entities = Exercise.class, version = 1, exportSchema = false)
//public abstract class AppDatabase extends RoomDatabase {
//    private static final String DB_NAME = "powerloggerdb";
//    private static AppDatabase instance;
//
//    public static synchronized AppDatabase getInstance(Context context) {
//        if (instance == null) {
//            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
//                    .fallbackToDestructiveMigration()
//                    .build();
//        }
//        return instance;
//    }
//
//    public abstract ExerciseDao exerciseDao();
//}
