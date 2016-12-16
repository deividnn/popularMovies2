package dnn.popularmovies;

/**
 * Created by deivi on 15/12/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by deivid
 */

public class DB extends SQLiteOpenHelper {
    static final String BD = "movies.db";
    static final String TABLE = "movies";
    static final String ID = "_id";
    static final String TITLE = "title";
    static final String POSTER = "poster";
    static final String SYNOPSIS = "synopsis";
    static final String RATING = "rating";
    static final String RELEASE = "release";
    static final String IDM = "imd";
    static final int VERSAO = 2;

    public DB(Context context) {
        super(context, BD, null, VERSAO);
    }

    //create table movies
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + "("
                + ID + " integer primary key autoincrement,"
                + TITLE + " text,"
                + POSTER + " blob,"
                + SYNOPSIS + " text,"
                + RATING + " text,"
                + RELEASE + " text,"
                + IDM + " text"
                + ")";
        db.execSQL(sql);
    }

    //delete table movie to update version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}