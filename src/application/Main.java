package application;
import backend.*;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {			
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("mainPage.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ViewerStorage list = new ViewerStorage();
		VideoStorage vids = new VideoStorage();
//		String d1 = "1-2-03";
//		list.addViewer("saeed123", "rehal@email.com", d1, false , "1234");
//		list.addViewer("ahmed123", "ahmed@email.com", d1, false, "1234");
//		list.addViewer("ali123", "ali@email.com", d1, true, "1234");
//		list.addViewer("daniyal123", "dan@email.com", d1, false, "1234");
//		list.addViewer("fatima123", "fatima@email.com", d1, true, "1234");
		modStorage moderators = new modStorage();
		
		moderators.init();
		list.init();
		list.display();
		 System.out.println(ViewerStorage.getViewerCount());
		vids.init();
		vids.display();
		
		launch(args);
	}
}
