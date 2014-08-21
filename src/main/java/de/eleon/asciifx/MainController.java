package de.eleon.asciifx;

import de.eleon.asciifx.data.Document;
import de.eleon.asciifx.data.Documents;
import de.eleon.asciifx.fx.controller.ControllerFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
    TreeView treeView;

    @FXML
    AnchorPane editor;

    @FXML
    ToggleButton viewToggle;


    public void initialize() {
        documents.create("test");
        documents.create("document1");
        documents.create("document2");
        documents.create("document3");

        open(documents.read("test"));

        initTree();

        initViewToggle();

    }

    private void initTree() {
        TreeItem<String> rootItem = new TreeItem<String> ("Inbox");
        rootItem.setExpanded(true);
        for (String name : documents.list()) {
            TreeItem<String> item = new TreeItem<String>(name);
            rootItem.getChildren().add(item);
        }
        treeView.setRoot(rootItem);
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<String> selected = (TreeItem<String>)newValue;
                Document read = documents.read(selected.getValue());
                log.info("selected = [" + selected.getValue() + " / " + read.getFilename() + "]");
                open(read);
            }
        });
    }

    private void initViewToggle() {
        viewToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    editor.setTopAnchor(editor, 0d);
                } else {
                    editor.setTopAnchor(editor, 300d);
                }
            }
        });
        viewToggle.selectedProperty().set(true);
    }

    private void open(Document document) {

        AnchorPane child = controllerFactory.loadSubController("/fxml/editor/editor.fxml", this, document);
        child.setUserData(document);
        Tab tab = new Tab();
        tab.setText(document.getFilename());
        tab.setContent(child);

        editorTabPane.getTabs().add(tab);
        editorTabPane.getSelectionModel().select(tab);
    }


}
