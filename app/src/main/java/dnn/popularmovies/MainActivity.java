package dnn.popularmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static Toast alert;
    //adapter list movie
    public static ListMovie adapter;
    static Activity context;
    //list movie returned json api
    public static ArrayList<Movie> moviesList;
    //format date
    public static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

    static ListView list;
    static String[] titles = new String[]{};
    static String[] thumbs = new String[]{};
    static ProgressDialog progress;
    static int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        moviesList = new ArrayList<Movie>();
        alert = Toast.makeText(context, "", Toast.LENGTH_LONG);
        //progress loading movies
        progress = ProgressDialog.show(this, "Wait",
                "Loading Movies - Most Popular", true);

        list = (ListView) findViewById(R.id.list);
        //if titles is empty so dont update adapter
        if (titles.length != 0) {
            MainActivity.adapter = new
                    ListMovie(MainActivity.context, MainActivity.titles, MainActivity.thumbs);
            MainActivity.list.setAdapter(MainActivity.adapter);
        }
        MainActivity.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //get movie from list after click
                Movie m = moviesList.get(position);
                progress = ProgressDialog.show(context, "Wait",
                        "Loading Movie", true);
                Intent inte = new Intent(MainActivity.this, Detail.class);
                //pass data movie to intent
                inte.putExtra("sinopse", m.getSinopse());
                inte.putExtra("title", m.getTitle());
                inte.putExtra("rated", String.valueOf(m.getVoteAverage()));
                inte.putExtra("poster", m.getPoster());
                inte.putExtra("release", sd.format(m.getReleaseDate()));
                inte.putExtra("id", m.getId());
                inte.putExtra("position", position);
                startActivity(inte);
            }
        });
        //get movies from api url
        new GetMoviesTask().execute(Util.MOST_POPULAR);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get menu setting
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                //get movies by most popular
                progress = ProgressDialog.show(this, "Wait",
                        "Loading Movies - Most Popular", true);
                new GetMoviesTask().execute(Util.MOST_POPULAR);
                return true;
            case R.id.rated:
                //get movies by highest rated
                progress = ProgressDialog.show(this, "Wait",
                        "Loading Movies - Highest Rated", true);
                new GetMoviesTask().execute(Util.HIGHEST_RATED);
                return true;
            case R.id.favorites:
                Intent inte = new Intent(MainActivity.this, MoviesOffline.class);
                startActivity(inte);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
