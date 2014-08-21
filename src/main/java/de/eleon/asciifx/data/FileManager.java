package de.eleon.asciifx.data;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import static java.nio.file.Files.*;

public class FileManager {

    private Path root;

    public FileManager(Path root) {
        this.root = root;
        if (!this.root.toFile().exists()) {
            try {
                createDirectories(this.root);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void create(String filename) {
        try {
            if (!exists(path(filename))) {
                createFile(path(filename));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String read(String filename) {
        try {
            return new String(readAllBytes(path(filename)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void write(String filename, String content) {
        try {
            Files.write(path(filename), content.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void delete(String filename) {
        try {
            Files.delete(path(filename));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<String> list() {
        List<String> list = Lists.newArrayList();
        try {
            Files.list(root).forEach(new Consumer<Path>() {
                @Override
                public void accept(Path path) {
                    list.add(path.getFileName().toString());
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return list;
    }

    public Path pathOf(String filename) {
        return path(filename);
    }

    private Path path(String filename) {
        return Paths.get(root.toString(), filename);
    }
}
