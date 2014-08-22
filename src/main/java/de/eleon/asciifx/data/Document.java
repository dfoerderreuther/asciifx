package de.eleon.asciifx.data;

import com.google.common.base.Objects;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Document {

    private final String filename;

    private StringProperty content = new SimpleStringProperty("");

    public Document(String filename) {
        this.filename = filename;
    }

    public Document(String filename, String content) {
        this.filename = filename;
        this.content.set(content);
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content.get();
    }


    public void setContent(String content) {
        this.content.set(content);
    }

    public Property<String> content() {
        return content;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("filename", filename)
                .add("content", content)
                .toString();
    }
}
