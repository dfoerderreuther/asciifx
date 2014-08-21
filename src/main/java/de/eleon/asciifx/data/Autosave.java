package de.eleon.asciifx.data;

import com.google.common.collect.Queues;
import com.google.common.eventbus.EventBus;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

import java.util.Queue;

public class Autosave extends javafx.concurrent.Service<Void> {

    private Documents documents;

    private final Queue<Document> documentQueue = Queues.newArrayBlockingQueue(100);

    EventBus eventBus = new EventBus();

    public Autosave(Documents documents) {
        this.documents = documents;
        this.start();
        this.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                saved();
            }
        });
    }

    public void registerSaveListener(Object object) {
        eventBus.register(object);
    }

    public void unregisterSaveListener(Object object) {
        eventBus.unregister(object);
    }

    public void save(Document document) {
        if (!documentQueue.contains(document)) {
            eventBus.post(new AutosaveEvent(AutosaveEvent.Type.START));
            documentQueue.add(document);
        }
    }

    private void saved() {
        eventBus.post(new AutosaveEvent(AutosaveEvent.Type.STOP));
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(!isCancelled()) {
                    if (!documentQueue.isEmpty()) {
                        updateProgress(0, 1);
                        Document document = documentQueue.remove();
                        documents.write(document);
                        updateProgress(1, 1);
                    }
                }
                return null;
            }
        };
    }
}
