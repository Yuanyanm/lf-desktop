package top.lf.stage;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by YuanYan on 2018/11/4.
 */
public class MessagingDemo extends Application {
    // 1->5->8->2
    public void start(Stage stage) {
        Label label = new Label("message1");
        label.setPrefWidth(300);
        label.setPrefHeight(100);
        // some action
        label.setText("action done");
        label.setText("calling method.. wait for some time");
        Task<Void> task = new Task<Void>() {
            @Override
            protected void running() {
                System.out.println("=======0001");
                super.running();
            }

            @Override
            protected void succeeded() {
                System.out.println("=======0002");
                super.succeeded();
            }

            @Override
            protected void cancelled() {
                System.out.println("=======0003");
                super.cancelled();
            }

            @Override
            protected void failed() {
                System.out.println("=======0004");
                super.failed();
            }

            @Override public Void call() throws InterruptedException {
                updateMessage("message2");
                Thread.sleep(3000);
                updateMessage("message3");
                Thread.sleep(7000);
                updateMessage("time consuming method is done with success");
                System.out.println("=======0005");
                return null;
            }
        };

        //UI控件属性与Task属性绑定，“自动更新”UI控件
        label.textProperty().bind(task.messageProperty());
        task.setOnSucceeded(e -> {
            System.out.println("=======0008");
            label.textProperty().unbind();
            label.setText("operation completed successfully");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        stage.setScene(new Scene(label));
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}
