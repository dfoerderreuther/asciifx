package de.eleon.asciifx;

import de.eleon.asciifx.fx.controller.ControllerFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main extends Application {

    private ApplicationContext applicationContext;

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");
        ControllerFactory controllerFactory = applicationContext.getBean(ControllerFactory.class);

        primaryStage.setTitle("AsciiFX");

        setUserAgentStylesheet(STYLESHEET_MODENA);

        Pane p = controllerFactory.load("/fxml/main/main.fxml");
        Scene mainScene = new Scene(p);
        stage = primaryStage;

        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
