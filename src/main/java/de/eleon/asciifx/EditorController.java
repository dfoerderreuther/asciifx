package de.eleon.asciifx;

import com.google.common.eventbus.Subscribe;
import de.eleon.asciifx.data.Autosave;
import de.eleon.asciifx.data.AutosaveEvent;
import de.eleon.asciifx.data.Document;
import de.eleon.asciifx.data.Documents;
import de.eleon.asciifx.fx.controller.SubController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class EditorController implements SubController<MainController, Document> {

    public static final Logger log = Logger.getLogger(EditorController.class);

    @FXML
    WebView view;

    @FXML
    TextArea editor;

    Document document;

    @FXML
    ProgressIndicator progress;

    Documents documents = new Documents();
    Autosave autosave = new Autosave(documents);

    @Override
    public void initParent(MainController parent) {

    }

    @Override
    public void initControl(Document document) {
        this.document = document;
    }

    public void initialize() {
        autosave.registerSaveListener(this);
        initializeEditor();
        initializeWebView();
    }

    @Subscribe
    public void onSave(AutosaveEvent autosaveEvent) {
        switch (autosaveEvent.type()) {
            case START:
                progress.setProgress(-1);
                break;
            case STOP:
                progress.setProgress(0);
                initializeWebView();
                break;
        }

    }

    private void initializeWebView() {
        //log.info("load " + document.getFilename());
        view.getEngine().load(documents.htmldocUrl(document.getFilename()));
    }


    private void initializeEditor() {
        Bindings.bindBidirectional(editor.textProperty(), document.content());
        editor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                autosave.save(document);
            }
        });
    }

}
