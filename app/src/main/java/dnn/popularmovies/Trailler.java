package dnn.popularmovies;

import java.io.Serializable;

/**
 * Created by deivi on 15/12/2016.
 */
public class Trailler implements Serializable {

    private int id;
    private String title;
    private String link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}
