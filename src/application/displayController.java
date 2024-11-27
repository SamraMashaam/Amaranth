package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import backend.VideoStorage;
import backend.Viewer;
import backend.mod;
import backend.report;
import backend.video;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class displayController {
	Stage s;
	Scene sc;
	Parent root;
	static video current;
	static Viewer vcurrent;
	static boolean liked=false;
    @FXML
    Label vTitle;
    @FXML
    Label likecount;
    @FXML
    ListView<String> vComment;

    public void setVideoTitle(String title) {
        vTitle.setText(title); // Set the video title
      
    }
    
    public void setLikecount() {
        likecount.setText(""+current.getLikeCount());
    }
    
	public void goBack(ActionEvent event) throws Exception {               
	    try {
	    	
	    	Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
	    	s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    	sc = new Scene(root);
	    	s.setScene(sc);
	    	s.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void reportvid() {
		String check = JOptionPane.showInputDialog("Enter Reason", " ");
		if(check.equals(null)) {return;}
		report r = new report(current,vcurrent.getUsername(),check);
		mod.reportList.add(r);
		mod.setRcount(mod.getRcount() + 1);
		mod.display();
	}
	
	public void like() throws Exception, SQLException, ClassNotFoundException{
		try {
		if(!current.isLiked()) {
		current.incrementLikes();
		current.setLiked(true);
		String driver = "com.mysql.cj.jdbc.Driver";
    	Class.forName(driver);
    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");
    	
    	Statement stm = conn.createStatement();
    	String test = "select * from Amaranth.video where title='"+vTitle.getText()+"'";
    	ResultSet res = stm.executeQuery(test);
    	if(res.next())
    		System.out.println("found");
    	else
    		System.out.println("not found");
    	String q = "UPDATE Amaranth.video SET likecount = '"+current.getLikeCount()+"' WHERE title = '" + vTitle.getText() + "'";
    	
    	int rs = stm.executeUpdate(q);
    	VideoStorage v = new VideoStorage();
    	v.init();
    	conn.close();
		}
		}
		catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void comment(ActionEvent event) {
		try {
			String check = JOptionPane.showInputDialog("Enter Comment", "");
			if(check == null || check.equals(""))
				return;
			current.getComments().add(check);
			System.out.println(check);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("comment.fxml"));
			Parent root = loader.load();
			displayController controller = loader.getController();
			
			 Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			 stage.setScene(new Scene(root));
			 stage.show();
			 System.out.println(current.getComments().size());
			 for(int i = 0; i<current.getComments().size();i++) {
					controller.vComment.getItems().add(current.getComments().get(i));
				}
		
		}
		catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	public void setVideo(video v) {
		current = v;
		System.out.println(current.getTitle());
	}
	
	public void setViewer(Viewer v) {
		vcurrent = v;
		System.out.println(vcurrent.getUsername());
	}
}
