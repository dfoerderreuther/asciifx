package de.eleon.asciifx.files;

import de.eleon.asciifx.data.FileManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static de.eleon.matchers.PathMatcher.exist;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FileManagerTest {

    @Rule
    public TemporaryFolder home = new TemporaryFolder();

    FileManager underTest;

    @Before
    public void setUp() throws IOException {
        home.create();
        underTest = new FileManager(Paths.get(home.getRoot().toString()));
    }

    @Test
    public void shouldCreateNewFile() throws Exception {
        underTest.create("test.ad");
        assertThat(Paths.get(home.getRoot().toString(), "test.ad"), exist());
    }

    @Test
    public void shouldDeleteFile() throws Exception {
        home.newFile("test.ad");

        underTest.delete("test.ad");
        assertThat(Paths.get(home.getRoot().toString(), "test.ad"), not(exist()));
    }

    @Test
    public void shouldListFiles() throws Exception {
        underTest.create("a.ad");
        underTest.create("b.ad");

        List<String> files = underTest.list();
        assertThat(files, contains("a.ad", "b.ad"));
    }

    @Test
    public void shouldWriteAndRead() throws Exception {
        underTest.create("test.ad");
        underTest.write("test.ad", "write test");

        String read = underTest.read("test.ad");

        assertThat(read, equalTo("write test"));
    }

}