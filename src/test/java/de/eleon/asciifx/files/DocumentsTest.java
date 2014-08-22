package de.eleon.asciifx.files;


import de.eleon.asciifx.data.Document;
import de.eleon.asciifx.data.Documents;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static de.eleon.asciifx.data.Documents.*;
import static de.eleon.matchers.PathMatcher.exist;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DocumentsTest {

    @Rule
    public TemporaryFolder home = new TemporaryFolder();

    Documents underTest;

    @Before
    public void setUp() throws IOException {
        home.create();
        underTest = new Documents();
        underTest.setRoot(home.getRoot().toString());
    }

    @Test
    public void shouldCreate() throws Exception {
        underTest.create("a");
        underTest.create("b".concat(AD_EXT));

        assertThat(path("a".concat(AD_EXT)), exist());
        assertThat(path("b".concat(AD_EXT)), exist());
    }

    @Test
    public void shouldWriteAndRead() throws Exception {
        underTest.create("test");
        underTest.write(new Document("test", "== Level1"));

        assertThat(underTest.read("test").getContent(), equalTo("== Level1"));
        String html = new String(Files.readAllBytes(path(HTML_DIRECTORY, "test".concat(HTML_EXT))));
        assertThat(html, startsWith("<div"));
    }

    @Test
    public void shouldList() throws Exception {
        underTest.create("a");
        underTest.create("b");

        List<String> list = underTest.list();
        assertThat(list, contains("a".concat(AD_EXT), "b".concat(AD_EXT), "html"));
    }

    @Test
    public void shouldGetAsciidocName() throws Exception {
        String name = Documents.asciidocName("a");
        assertThat(name, is("a".concat(AD_EXT)));
    }

    @Test
    public void shouldGetHtmldocName() throws Exception {
        String name = Documents.htmldocName("a");
        assertThat(name, is("a".concat(HTML_EXT)));
    }

    private Path path(String... filename) {
        return Paths.get(home.getRoot().toString(), filename);
    }


}