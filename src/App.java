import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    static Scene scene;
    
    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(loadFXML("views/tela_inicial"), 640, 480);
        stage.setScene(scene);
        
        stage.setTitle("Estatísticas ENEM");
        stage.setMinHeight(350);
        stage.setMinWidth(450);

        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
