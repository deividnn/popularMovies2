package dnn.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by deivi on 14/12/2016.
 */
public class ListReviews extends ArrayAdapter<Reviews> {
    private Context context;
    private List<Reviews> reviews = null;

    public ListReviews(Context context, List<Reviews> reviews) {
        super(context, 0, reviews);
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Reviews t = reviews.get(position);

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_reviews, null);

        TextView title = (TextView) view.findViewById(R.id.tiltle);
        title.setText((position+1)+" - "+t.getAuthor()+": "+t.getContent());

        return view;
    }


}
