package de.eleon.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.nio.file.Path;

public class PathMatcher {

    public static BaseMatcher<Path> exist() {
        return new BaseMatcher<Path>() {
            @Override
            public boolean matches(Object item) {
                Path path = (Path) item;
                return path.toFile().exists();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("given path should exist");
            }
        };
    }

}
