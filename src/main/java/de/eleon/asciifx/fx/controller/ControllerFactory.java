package de.eleon.asciifx.fx.controller;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ControllerFactory {

    @Autowired
    ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public <T> T load(String url) {
        try (InputStream fxmlStream = getClass().getResourceAsStream(url)) {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                public Object call(Class<?> clazz) {
                    return applicationContext.getBean(clazz);
                }
            });
            return (T)loader.load(fxmlStream);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public <T, P, C> T loadSubController(String url, final P parent, final C control) {
        try (InputStream fxmlStream = getClass().getResourceAsStream(url)) {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                public Object call(Class<?> clazz) {
                    Object bean = applicationContext.getBean(clazz);
                    SubController<P, C> subController = (SubController<P, C>) bean;
                    subController.initParent(parent);
                    subController.initControl(control);
                    return subController;
                }
            });
            return (T)loader.load(fxmlStream);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

}
