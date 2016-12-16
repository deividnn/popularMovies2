package dnn.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by deivi on 15/12/2016.
 */
public class DBController {

    private SQLiteDatabase db;
    private DB dbx;

    public DBController(Context context) {
        dbx = new DB(context);
    }


    //insert  movie
    public String insert(String title, String synopsis, String rating,
                         String release, byte[] poster, String idm) {
        ContentValues valores;
        long resultado;

        db = dbx.getWritableDatabase();
        valores = new ContentValues();
        valores.put(DB.TITLE, title);
        valores.put(DB.SYNOPSIS, synopsis);
        valores.put(DB.RATING, rating);
        valores.put(DB.RELEASE, release);
        valores.put(DB.POSTER, poster);
        valores.put(DB.IDM, idm);

        resultado = db.insert(DB.TABLE, null, valores);
        db.close();

        if (resultado == -1)
            return "Error";

        return "ok";
    }

    //load movie by id
    public Cursor loadDataByid(String idm) {
        Cursor cursor;
        String[] campos = {dbx.IDM};
        String where = dbx.IDM + "='" + idm + "'";
        db = dbx.getReadableDatabase();
        cursor = db.query(dbx.TABLE, campos, where, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    //delete movie by id
    public void deleteByid(String idm){
        String where = dbx.IDM + "='" + idm+"'";
        db = dbx.getReadableDatabase();
        db.delete(dbx.TABLE,where,null);
        db.close();
    }

    //load movies
    public Cursor loadData(){
        Cursor cursor;
        String[] campos =  {dbx.ID,dbx.TITLE,dbx.POSTER,dbx.SYNOPSIS,dbx.RATING,dbx.RELEASE};
        db = dbx.getReadableDatabase();
        cursor = db.query(dbx.TABLE, campos, null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

}
