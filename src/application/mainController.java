package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainController {
	Stage s;
	Scene sc;
	Parent root;
	@FXML
	public void viewer(ActionEvent event) throws Exception {               
	    try {
	    		
	    		Parent root = FXMLLoader.load(getClass().getResource("vlogin.fxml"));
	    		s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    		sc = new Scene(root);
	    		s.setScene(sc);
	    		s.show();
	    	
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void mod(ActionEvent event) throws Exception {               
	    try {
	    		
	    		Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
	    		s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    		sc = new Scene(root);
	    		s.setScene(sc);
	    		s.show();
	    	
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void creator(ActionEvent event) throws Exception {    
		//creator login page
		try {
    		
    		Parent root = FXMLLoader.load(getClass().getResource("clogin.fxml"));
    		s = (Stage)(((Node)event.getSource()).getScene().getWindow());
    		sc = new Scene(root);
    		s.setScene(sc);
    		s.show();
    	
    } catch(Exception e) {
        e.printStackTrace();
    }
}
	   	}

