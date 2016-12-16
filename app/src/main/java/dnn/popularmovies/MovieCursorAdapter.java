package dnn.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by deivi on 16/12/2016.
 */
public class MovieCursorAdapter extends CursorAdapter {
    public MovieCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_movie_offline, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView title = (TextView) view.findViewById(R.id.titled);
        TextView synopsis = (TextView) view.findViewById(R.id.sinopsed);
        TextView release = (TextView) view.findViewById(R.id.releaseDated);
        TextView rating = (TextView) view.findViewById(R.id.ratedd);
        ImageView posters=(ImageView)view.findViewById(R.id.posterd);

        String titlec = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String synopsisc = cursor.getString(cursor.getColumnIndexOrThrow("synopsis"));
        String releasec = cursor.getString(cursor.getColumnIndexOrThrow("release"));
        String ratingc = cursor.getString(cursor.getColumnIndexOrThrow("rating"));
        byte[] poster =cursor.getBlob(cursor.getColumnIndexOrThrow("poster"));

        title.setText(titlec);
        synopsis.setText(synopsisc);
        release.setText(releasec);
        rating.setText(ratingc);

        Bitmap bmp = BitmapFactory.decodeByteArray(poster, 0, poster.length);
        posters.setImageBitmap(bmp);


    }
}
