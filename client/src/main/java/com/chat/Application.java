package com.chat;

import com.chat.configuration.AbstractJavaFxApplicationSupport;
import com.chat.configuration.ControllerConfiguration.ViewHolder;
import com.chat.controller.ViewController;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport {

    @Value("${ui.title}")
    private String windowTitle;

    @Value("${ui.form}")
    private String htmlMessageUIForm;

    @Autowired
    @Qualifier("mainView")
    private ViewHolder viewHolder;

    @Override
    public void start(Stage stage) {
        GridPane parent = (GridPane) viewHolder.getView();
        WebView webView = initWebView();
        ((Pane) parent.getChildren().get(0)).getChildren().add(webView);
        Scene scene = new Scene(parent);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    private WebView initWebView() {
        WebView webView = new WebView();
        webView.setLayoutX(23);
        webView.setLayoutY(57);
        webView.setPrefWidth(386);
        webView.setPrefHeight(291);
        webView.getEngine().setJavaScriptEnabled(true);
        webView.getEngine()
                .load(getClass().getClassLoader().getResource(htmlMessageUIForm).toString());

        ViewController controller = (ViewController) viewHolder.getController();
        controller.htmlMessageView = webView;
        return webView;
    }

    public static void main(String[] args) {
        launchApp(Application.class, args);
    }

}