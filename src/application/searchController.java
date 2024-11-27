package application;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import backend.VideoStorage;
import backend.Viewer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class searchController  {
	Stage s;
	Scene sc;
	Parent root;
	static Viewer currentViewer;
	static String currentDisplay;
	@FXML
	 ListView<String> videoList;
	  @FXML
	     Label vTitle;
	    @FXML
	    Label likecount;
	    @FXML
	    ListView<String> vComment;
	    @FXML
		ListView<String> searchList;
	
	

	public void initialize(String r) {	//the string search here is case-sensitive
		System.out.print(VideoStorage.videoCount);
		for(int i = 0; i<VideoStorage.videoCount; i++) {
			if( Arrays.asList(VideoStorage.getVideoList().get(i).getTitle().split(" ")).contains(r))
				searchList.getItems().add(VideoStorage.getVideoList().get(i).getTitle());
		}
		searchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    try {

			    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("display.fxml"));
			         Parent root = loader.load();
			         // Access the controller for display.fxml
			         displayController controller = loader.getController();

			         // Pass the selected video title to the display.fxml controller
			         controller.setVideoTitle(searchList.getSelectionModel().getSelectedItem());
			         for(int i = 0; i<VideoStorage.videoCount; i++) {
				 			if(VideoStorage.getVideoList().get(i).getTitle() == searchList.getSelectionModel().getSelectedItem()) {
				 				currentViewer.watchvideo(VideoStorage.getVideoList().get(i));
				 				controller.setVideo(VideoStorage.getVideoList().get(i));
				 				controller.setViewer(currentViewer);
				 				break;
				 			}
				     	}
			         controller.setLikecount();
			         
			         // Set the scene
			        
			         Stage stage = (Stage) searchList.getScene().getWindow();
			         Scene scene = new Scene(root);
			         stage.setScene(scene);
			         stage.show();

			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			
		});
		
	}
	
	
	
	public void setVideoTitle(String title) {
	    if (vTitle != null) {
	        vTitle.setText(title);
	    }
	}
	
	public void setViewer(Viewer v)
	{
		currentViewer = v;
	}

	
	public void goBack(ActionEvent event) throws Exception {               
	    try {
	    	Parent root = FXMLLoader.load(getClass().getResource("viewInterface.fxml"));
	    	s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    	sc = new Scene(root);
	    	s.setScene(sc);
	    	s.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	

}
