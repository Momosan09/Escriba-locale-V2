package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	Escriba e = new Escriba();
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			AnchorPane pantallaInicial = FXMLLoader.load(getClass().getResource("/application/res/pantallaPrincipal.fxml"));
			root.setCenter(pantallaInicial);
			Scene scene = new Scene(root,640,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
