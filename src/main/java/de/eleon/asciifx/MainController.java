package de.eleon.asciifx;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import de.eleon.asciifx.data.Document;
import de.eleon.asciifx.data.Documents;
import de.eleon.asciifx.fx.controller.ControllerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class MainController {

    public static final Logger log = Logger.getLogger(MainController.class);


    @Autowired
    ControllerFactory controllerFactory;

    @Autowired
    Documents documents;

    @FXML
    TabPane editorTabPane;

    @FXML
    AnchorPane editor;

    @FXML
    public TextField newFile;

    public void initialize() {
        documents.create("test");
        documents.create("document1");
        documents.create("document2");
        documents.create("document3");

        editorTabPane.getTabs().clear();

        for (String filename : documents.list()) {
            open(documents.read(filename));
        };

        //editorTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    private void open(Document document) {

        AnchorPane child = controllerFactory.loadSubController("/fxml/editor/editor.fxml", this, document);
        child.setUserData(document);
        if (FluentIterable.from(editorTabPane.getTabs()).firstMatch(new Predicate<Tab>() {
            @Override
            public boolean apply(Tab input) {
                return input.equals(document.getFilename());
            }
        }).isPresent()) return;
        Tab tab = new Tab();
        tab.setText(document.getFilename());
        tab.setContent(child);

        editorTabPane.getTabs().add(tab);
        editorTabPane.getSelectionModel().select(tab);

    }

    public void create(ActionEvent actionEvent) {
        String filename = newFile.getText().trim();
        if (!filename.isEmpty()) {
            open(documents.create(filename));
        }
    }

    public void changeDirectory(ActionEvent actionEvent) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setInitialDirectory(new File(documents.getRoot()));
        File file = fileChooser.showDialog(Main.stage);
        if (file != null) {
            log.info(file.getAbsolutePath());
            documents.setRoot(file.getAbsolutePath());
            editorTabPane.getTabs().clear();
        }
    }
}
