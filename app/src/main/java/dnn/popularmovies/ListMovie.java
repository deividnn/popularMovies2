package dnn.popularmovies;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by deivi on 14/12/2016.
 */
public class ListMovie extends ArrayAdapter<String> {
    static int count;

    private final Activity context;
    private final String[] titles;
    private final String[] thumbs;

    public ListMovie(Activity context,
                     String[] titles, String[] thumbs) {
        super(context, R.layout.list_movie, titles);
        this.context = context;
        this.titles = titles;
        this.thumbs = thumbs;
        count = 0;


    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_movie, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.thumb);
        txtTitle.setText(titles[position]);

        //download thumb movie
        new DownloadImageTask(imageView)
                .execute(thumbs[position]);
        return rowView;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
            Log.d("baxando", "");
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = Util.THUMB_URL + urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //close progressbar if download all thumbs
            if (count == MainActivity.moviesList.size()) {
                MainActivity.progress.dismiss();
            }
        }
    }
}
