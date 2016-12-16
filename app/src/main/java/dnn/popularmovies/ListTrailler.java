package dnn.popularmovies;

import android.app.Activity;
import android.content.Context;
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
import java.util.List;

/**
 * Created by deivi on 14/12/2016.
 */
public class ListTrailler extends ArrayAdapter<Trailler> {
    private Context context;
    private List<Trailler> traillers = null;

    public ListTrailler(Context context, List<Trailler> traillers) {
        super(context, 0, traillers);
        this.traillers = traillers;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Trailler t = traillers.get(position);

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_traillers, null);

        TextView title = (TextView) view.findViewById(R.id.tiltle);
        title.setText((position+1)+""+" - "+t.getTitle());

        return view;
    }


}
