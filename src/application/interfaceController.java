package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import backend.VideoStorage;
import backend.mod;
import backend.video;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class interfaceController {
	
	Stage s;
	Scene sc;
	Parent root;
	
	@FXML
	private ListView<String> videoList;
	@FXML
	private ListView<String> watchList;
	@FXML
	 Label lFlag = new Label();
	@FXML
	 Label lRate = new Label();
	@FXML
	Label kids= new Label();
	@FXML
	 Label deets = new Label();
	@FXML
	 Label user = new Label();
	@FXML
	private ListView<String> rList;
	@FXML
	 Label vname = new Label();
	
	

	
	@FXML
	public void openHistory(ActionEvent event) throws Exception {               
	    try {
	    	Parent root = FXMLLoader.load(getClass().getResource("watchHs.fxml"));
	    	s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    	sc = new Scene(root);
	    	s.setScene(sc);
	    	s.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	public void openReport(ActionEvent event) throws Exception {               
		 try {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("report.fxml"));
		        Parent root = loader.load();
		        
		        // Access the controller to set video data
		        interfaceController controller = loader.getController();
		        for (int i = 0; i < mod.getRcount(); i++) {
		            controller.rList.getItems().add(mod.reportList.get(i).reportID);
		        }
		        controller.rList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						 for (int i = 0; i < mod.getRcount(); i++) {
					            if( controller.rList.getSelectionModel().getSelectedItem() == mod.reportList.get(i).reportID) {
					            	 controller.user.setText(mod.reportList.get(i).getReporterID());
						             controller.deets.setText(mod.reportList.get(i).getDeets()); 
						             controller.vname.setText(mod.reportList.get(i).getVideoID().getTitle());
					            }
					        }	
					}
					
				});
		        
		        controller.rList.setCellFactory(lv -> {

		            ListCell<String> cell = new ListCell<>();

		            ContextMenu contextMenu = new ContextMenu();


		            MenuItem editItem = new MenuItem();
		            editItem.textProperty().bind(Bindings.format("Flag"));
		            editItem.setOnAction(e -> {
		                
		                // code to edit item...
		                video v = getVid2(cell.itemProperty().toString(),controller);
		                v.flag(true);
		                controller.rList.getItems().remove(cell.getItem());
		            });
		            MenuItem banItem = new MenuItem();
		            banItem.textProperty().bind(Bindings.format("Ban"));
		            banItem.setOnAction(e -> {
		            	video v = getVid2(cell.itemProperty().toString(),controller);
		                v.setBan(true);
		            	controller.rList.getItems().remove(cell.getItem());
		            	});
		            MenuItem rateItem = new MenuItem();
		            rateItem.textProperty().bind(Bindings.format("Change Age Rating"));
		            rateItem.setOnAction(e -> {
		            	video v = getVid2(cell.itemProperty().toString(),controller);
		                if(v.getKidsContent())
		                	v.changeRating(false);
		                else
		                	v.changeRating(true);
		                controller.rList.getItems().remove(cell.getItem());
		            	});
		            MenuItem rejectItem = new MenuItem();
		            rejectItem.textProperty().bind(Bindings.format("Reject Report"));
		            rejectItem.setOnAction(e -> {
		                controller.rList.getItems().remove(cell.getItem());
		            	});
		            contextMenu.getItems().addAll(editItem, rateItem, banItem, rejectItem);

		            cell.textProperty().bind(cell.itemProperty());

		            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
		                if (isNowEmpty) {
		                    cell.setContextMenu(null);
		                } else {
		                    cell.setContextMenu(contextMenu);
		                }
		            });
		            return cell ;
		        });
		        

		        s = (Stage) (((Node) event.getSource()).getScene().getWindow());
		        sc = new Scene(root);
		        s.setScene(sc);
		        s.show();
		        }
		     catch (Exception e) {
		        e.printStackTrace();
		    }
	}
	
	public video getVid(String t, interfaceController controller)
	{
		 
         for(int i = 0; i<VideoStorage.videoCount; i++) {
	 			if(VideoStorage.getVideoList().get(i).getTitle() == controller.videoList.getSelectionModel().getSelectedItem()) {
	 				return VideoStorage.getVideoList().get(i);
	 			}
	     	}
         return null;
	}
	
	public video getVid2(String t, interfaceController controller)
	{
		 
         for(int i = 0; i<VideoStorage.videoCount; i++) {
	 			if(VideoStorage.getVideoList().get(i).getTitle() == controller.rList.getSelectionModel().getSelectedItem()) {
	 				return VideoStorage.getVideoList().get(i);
	 			}
	     	}
         return null;
	}
	
	
	@FXML
	public void openFlag(ActionEvent event) throws Exception {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("flagvid.fxml"));
	        Parent root = loader.load();
	        
	        // Access the controller to set video data
	        interfaceController controller = loader.getController();
	        controller.videoList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				    
				    	 video v = getVid(controller.videoList.getSelectionModel().getSelectedItem(),controller);
				    	 controller.lFlag.setText(""+v.getFlag());
			             controller.lRate.setText(""+v.getRating());
			             controller.kids.setText(""+v.getKidsContent());
				    
				}
				
			});
	        for (int i = 0; i < VideoStorage.videoCount; i++) {
	            controller.videoList.getItems().add(VideoStorage.getVideoList().get(i).getTitle());
	        }
	        controller.videoList.setCellFactory(lv -> {

	            ListCell<String> cell = new ListCell<>();

	            ContextMenu contextMenu = new ContextMenu();


	            MenuItem editItem = new MenuItem();
	            editItem.textProperty().bind(Bindings.format("Flag"));
	            editItem.setOnAction(e -> {
	                
	                // code to edit item...
	                video v = getVid(cell.itemProperty().toString(),controller);
	                v.flag(true);
	            });
	            MenuItem banItem = new MenuItem();
	            banItem.textProperty().bind(Bindings.format("Ban"));
	            banItem.setOnAction(e -> {
	            	video v = getVid(cell.itemProperty().toString(),controller);
	                v.setBan(true);
	            	controller.videoList.getItems().remove(cell.getItem());
	            	});
	            MenuItem rateItem = new MenuItem();
	            rateItem.textProperty().bind(Bindings.format("Change Age Rating"));
	            rateItem.setOnAction(e -> {
	            	video v = getVid(cell.itemProperty().toString(),controller);
	                if(v.getKidsContent())
	                	v.changeRating(false);
	                else
	                	v.changeRating(true);
	            	});
	            contextMenu.getItems().addAll(editItem, rateItem, banItem);

	            cell.textProperty().bind(cell.itemProperty());

	            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
	                if (isNowEmpty) {
	                    cell.setContextMenu(null);
	                } else {
	                    cell.setContextMenu(contextMenu);
	                }
	            });
	            return cell ;
	        });
	        

	        s = (Stage) (((Node) event.getSource()).getScene().getWindow());
	        sc = new Scene(root);
	        s.setScene(sc);
	        s.show();
	        }
	     catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@FXML
	public void goBack(ActionEvent event) throws Exception {               
	    try {
	    	Parent root = FXMLLoader.load(getClass().getResource("modInterface.fxml"));
	    	s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    	sc = new Scene(root);
	    	s.setScene(sc);
	    	s.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	@FXML
	public void logout(ActionEvent event) throws Exception {               
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
}
