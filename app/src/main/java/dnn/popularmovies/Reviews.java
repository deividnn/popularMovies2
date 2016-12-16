package dnn.popularmovies;

import java.io.Serializable;

/**
 * Created by deivi on 15/12/2016.
 */
public class Reviews implements Serializable {

    private int id;
    private String author;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
