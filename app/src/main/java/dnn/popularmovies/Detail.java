package dnn.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
public class Detail extends AppCompatActivity {

    ImageView bmImage;
    TextView title, sinopse, release, rated;
    static ListTrailler adapter;
    static ListReviews adapter2;
    static ListView list;
    static ListView list2;
    static int total;
    static Activity context;
    public static ArrayList<Trailler> trt;
    static String titleMovie;
    int id;
    Button favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        context = this;
        trt = new ArrayList<>();
        bmImage = (ImageView) findViewById(R.id.posterd);
        title = (TextView) findViewById(R.id.titled);
        rated = (TextView) findViewById(R.id.ratedd);
        sinopse = (TextView) findViewById(R.id.sinopsed);
        release = (TextView) findViewById(R.id.releaseDated);
        favorite=(Button) findViewById(R.id.favorited);


        //get data from intent
        Bundle extras = getIntent().getExtras();
        title.setText(extras.getString("title"));
        //set title movie to share
        titleMovie = title.getText().toString();

        rated.setText("Vote Average:" + extras.getString("rated"));
        sinopse.setText("Plot Synopsis:" + extras.getString("sinopse"));
        release.setText("Release Date:" + extras.getString("release"));

        //get position movie in movielist
        int posi = extras.getInt("position");
        //get id movie
        id = extras.getInt("id");

        //verify if movie its marke as favorite
        DBController crud = new DBController(getBaseContext());
        if(crud.loadDataByid(String.valueOf(id)).getCount()>0){
            favorite.setText("Favorited");
            favorite.setBackgroundColor(Color.BLUE);
        }else{
            favorite.setText("Mark it as a Favorite");
            favorite.setBackgroundColor(Color.YELLOW);
        }

        MainActivity.moviesList.get(posi).setTraillers(new ArrayList<Trailler>());
        MainActivity.moviesList.get(posi).setReviews(new ArrayList<Reviews>());

        list = (ListView) findViewById(R.id.list_trail);
        setListViewHeightBasedOnChildren(list);
        adapter = new
                ListTrailler(context, MainActivity.moviesList.get(posi).getTraillers());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Trailler t = trt.get(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + t.getLink())));

            }
        });


        list2 = (ListView) findViewById(R.id.list_rev);
        setListViewHeightBasedOnChildren(list2);
        adapter2 = new
                ListReviews(context, MainActivity.moviesList.get(posi).getReviews());
        list2.setAdapter(adapter2);

        //donwload poster movie
        new DownloadImageTask(bmImage).execute(extras.getString("poster"));
        //get traillers movie by id
        new TraillerTask(posi).execute(String.valueOf(id));
        //get reviews movie by id
        new ReviewTask(posi).execute(String.valueOf(id));
    }


    //mark or unmarked movie favorite
    public void markFavorite(View v) {
        DBController crud = new DBController(getBaseContext());

        //get byte array from imageview
        Bitmap bitmap = ((BitmapDrawable) bmImage.getDrawable()).getBitmap();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] img = bos.toByteArray();

        //unmarked movie favorited
        if(favorite.getText().toString().equals("Favorited")){
            crud.deleteByid(String.valueOf(id));
            favorite.setText("Mark it as a Favorite");
            favorite.setBackgroundColor(Color.YELLOW);

        }else {
            //insert movie and marked as favorited
            if (crud.insert(
                    title.getText().toString(),
                    sinopse.getText().toString(),
                    rated.getText().toString(),
                    release.getText().toString(),
                    img,
                    String.valueOf(id)
            ).equals("ok")) {
                favorite.setText("Favorited");
                favorite.setBackgroundColor(Color.BLUE);
                Toast.makeText(this, "movie marked as favorite", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "error mark movie as favorite!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    //share first trailler movie
    public void shareTrailler(View v) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Trailler " + titleMovie +
                " " + "http://www.youtube.com/watch?v=" + trt.get(0).getLink());
        startActivity(Intent.createChooser(sharingIntent, "Share using"));

    }

    //update hight listview inside scrollview
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
            Log.d("baxando", "");
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = Util.POSTER_URL + urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            MainActivity.progress.dismiss();

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
        Log.d("retorno t", moviesJson);
        return moviesJson;
    }


    private class TraillerTask extends AsyncTask<String, Void, String> {
        int position;

        public TraillerTask(int position) {
            this.position = position;
        }

        protected String doInBackground(String... urls) {
            String url = Util.URL_API + "/movie/" + urls[0] + "/videos?api_key=" + Util.API_KEY;

            Uri dataMovie = Uri.parse(url).buildUpon()
                    .build();
            String response = null;

            //get json from url api by criteria
            return getJSON(dataMovie);

        }

        protected void onPostExecute(String result) {
            if (result != null) {
                //load data from json
                loadInfoTrailler(result, position);

                //update adapter
                Detail.adapter = new
                        ListTrailler(Detail.context, MainActivity.moviesList.get(position).getTraillers());
                Detail.list.setAdapter(Detail.adapter);

                setListViewHeightBasedOnChildren(Detail.list);
            }

        }
    }


    public static void loadInfoTrailler(String jsonString, int position) {
        MainActivity.moviesList.get(position).setTraillers(new ArrayList<Trailler>());
        try {
            if (jsonString != null) {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");
                //add trailler to list
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject trailler = moviesArray.getJSONObject(i);
                    Trailler t = new Trailler();
                    t.setId(MainActivity.moviesList.get(position).getTraillers().size() + 1);
                    t.setTitle(trailler.getString("name"));
                    t.setLink(trailler.getString("key"));
                    if (trailler.getString("site").equals("YouTube")) {
                        MainActivity.moviesList.get(position).getTraillers().add(t);
                        trt.add(t);
                        total++;
                    }

                }

            }
        } catch (Exception e) {
            Log.d("erro trailler", e.toString());
        }
    }


    private class ReviewTask extends AsyncTask<String, Void, String> {
        int position;

        public ReviewTask(int position) {
            this.position = position;
        }

        protected String doInBackground(String... urls) {
            String url = Util.URL_API + "/movie/" + urls[0] + "/reviews?api_key=" + Util.API_KEY;

            Uri dataMovie = Uri.parse(url).buildUpon()
                    .build();
            String response = null;

            //get json from url api by criteria
            return getJSON(dataMovie);

        }

        protected void onPostExecute(String result) {
            if (result != null) {
                //load data from json
                loadInfoReview(result, position);

                //update adapter
                Detail.adapter2 = new
                        ListReviews(Detail.context, MainActivity.moviesList.get(position).getReviews());
                Detail.list2.setAdapter(Detail.adapter2);
                setListViewHeightBasedOnChildren(Detail.list2);
            }

        }
    }


    public static void loadInfoReview(String jsonString, int position) {
        MainActivity.moviesList.get(position).setReviews(new ArrayList<Reviews>());
        try {
            if (jsonString != null) {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");
                //add review to list
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject rev = moviesArray.getJSONObject(i);
                    Reviews t = new Reviews();
                    t.setId(MainActivity.moviesList.get(position).getReviews().size() + 1);
                    t.setAuthor(rev.getString("author"));
                    t.setContent(rev.getString("content"));
                    MainActivity.moviesList.get(position).getReviews().add(t);
                }

            }
        } catch (Exception e) {
            Log.d("erro trailler", e.toString());
        }
    }
}
