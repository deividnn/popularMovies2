package dnn.popularmovies;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deivi on 14/12/2016.
 */
public class GetMoviesTask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {


        if (params.length == 0) {
            return null;
        }
        try {
            String sortingCriteria = params[0];


            Uri dataMovie = Uri.parse(Util.URL_API + sortingCriteria + "&api_key=" + Util.API_KEY).buildUpon()
                    .build();
            String response = null;

            //get json from url api by criteria
            return getJSON(dataMovie);
            //return response;
        } catch (Exception e) {
            MainActivity.alert.setText("Error " + e.toString());
            MainActivity.alert.setDuration(Toast.LENGTH_LONG);
            MainActivity.alert.show();
            return null;
        }

    }


    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            //load data from json
            loadInfo(response);
            MainActivity.total = MainActivity.moviesList.size();
            MainActivity.titles = new String[MainActivity.moviesList.size()];
            MainActivity.thumbs = new String[MainActivity.moviesList.size()];
            int pos = 0;
            //populate arrays titles and thumbs
            for (Movie m : MainActivity.moviesList) {
                MainActivity.titles[pos] = m.getTitle();
                MainActivity.thumbs[pos] = m.getThumb();
                pos++;

            }

            //update adapter
            MainActivity.adapter = new
                    ListMovie(MainActivity.context, MainActivity.titles, MainActivity.thumbs);
            MainActivity.list.setAdapter(MainActivity.adapter);

        } else {
            MainActivity.alert.setText("No Internet Conection");
            MainActivity.alert.setDuration(Toast.LENGTH_SHORT);
            MainActivity.alert.show();
            MainActivity.progress.dismiss();
        }

    }



    public  void loadInfo(String jsonString) {
        MainActivity.moviesList.clear();
        try {
            if (jsonString != null) {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");
                //add movie to list

                for (int i = 0; i <= moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    Movie movieItem = new Movie();
                    movieItem.setTitle(movie.getString("title"));
                    movieItem.setId(movie.getInt("id"));
                    if (movie.getString("overview") == "null") {
                        movieItem.setSinopse("No Overview was Found");
                    } else {
                        movieItem.setSinopse(movie.getString("overview"));
                    }
                    if (movie.getString("release_date") != "null") {
                        if (!movie.getString("release_date").isEmpty()) {
                            movieItem.setReleaseDate(MainActivity.sd.parse(movie.getString("release_date")));
                        }
                    }
                    movieItem.setVoteAverage(Double.valueOf(movie.getString("vote_average")));
                    movieItem.setPoster(movie.getString("poster_path"));
                    movieItem.setThumb(movie.getString("poster_path"));
                    if (movie.getString("poster_path") == "null") {
                        movieItem.setPoster("");
                        movieItem.setThumb("");
                    }
                    if (!movieItem.getPoster().equals("")) {
                        MainActivity.moviesList.add(movieItem);
                    }
                    Log.d("item", movieItem.toString());

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getJSON(Uri builtUri) {
        InputStream inputStream;
        StringBuffer buffer;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJson = null;

        try {
            //connect to api
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            inputStream = urlConnection.getInputStream();
            buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            moviesJson = buffer.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {

                }
            }
        }

        return moviesJson;
    }



}
