package de.eleon.asciifx.data;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;

import static java.lang.System.getProperty;

@Service
public class Documents {

    public static final String AD_EXT = ".ad";
    public static final String HTML_EXT = ".html";
    public static final String HTML_DIRECTORY = "html";

    private FileManager asciiDocs;
    private FileManager htmlDocs;
    private String root;

    public Documents() {
        setRoot(String.format("%s/%s", getProperty("user.home"), "asciifx"));
    }

    public void setRoot(String root) {
        this.root = root;
        asciiDocs = new FileManager(Paths.get(root));
        htmlDocs = new FileManager(Paths.get(root, HTML_DIRECTORY));
    }

    public Document create(String filename) {
        String basename = basename(filename);
        asciiDocs.create(asciidocName(basename));
        return new Document(basename);
    }

    public void write(Document document) {
        String basename = document.getFilename();
        asciiDocs.write(asciidocName(basename), document.getContent());
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        String html = asciidoctor.render(document.getContent(), OptionsBuilder.options().docType("utf-8"));
        htmlDocs.write(htmldocName(basename), html);
    }

    public List<String> list() {
        return asciiDocs.list(AD_EXT);
    }

    public Document read(String filename) {
        String basename = basename(filename);
        Document document = new Document(basename);
        document.setContent(asciiDocs.read(asciidocName(basename)));
        return document;
    }

    public static String asciidocName(String basename) {
        return basename.concat(AD_EXT);
    }

    public static String htmldocName(String basename) {
        return basename.concat(HTML_EXT);
    }

    public String htmldocUrl(String basename) {
        return "file://" + htmlDocs.pathOf(basename.concat(HTML_EXT)).toString();
    }

    private static String basename(String filename) {
        if (filename.endsWith(AD_EXT)) {
            return filename.substring(0, filename.length() - AD_EXT.length());
        }
        return filename;
    }


    public String htmldocContent(String filename) {
        return htmlDocs.read(htmldocName(filename));
    }

    public String getRoot() {
        return root;
    }
}
