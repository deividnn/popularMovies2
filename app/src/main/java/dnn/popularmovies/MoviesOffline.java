package dnn.popularmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by deivi on 15/12/2016.
 */
public class MoviesOffline extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_offline);

        DBController crud = new DBController(getBaseContext());
        Cursor c = crud.loadData();
        ListView lvItems = (ListView) findViewById(R.id.list);
        //set movies from db into cursoradapter
        MovieCursorAdapter todoAdapter = new MovieCursorAdapter(this, c);
        lvItems.setAdapter(todoAdapter);
    }
}
