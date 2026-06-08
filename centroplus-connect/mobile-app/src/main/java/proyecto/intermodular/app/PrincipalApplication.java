package proyecto.intermodular.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                PrincipalApplication.class.getResource(
                        "/proyecto/intermodular/app/views/login.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 390, 640);
        scene.getStylesheets().add(
                PrincipalApplication.class.getResource(
                        "/css/estilos.css").toExternalForm()
        );

        stage.setTitle("CentroPlus Connect – Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
