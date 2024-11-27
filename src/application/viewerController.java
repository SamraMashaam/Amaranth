package application;
import backend.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.DatePicker;

import javafx.stage.Stage;

public class viewerController {
	Stage s;
	Scene sc;
	Parent root;
	static private String token;
	@FXML
	private TextField uname;
	@FXML
	private PasswordField pass;
	@FXML
	private TextField unamer;
	@FXML
	private PasswordField passr;
	@FXML
	private TextField email;
	@FXML
	private DatePicker dob;
	@FXML
	private TextField uname2;
	@FXML
	private PasswordField pass2;
	@FXML
	private TextField email2;
	@FXML
	private DatePicker dob2;
	@FXML
	private Hyperlink home;
	@FXML
	ListView<String> watchList;
	
	

	
//	public void initialize(URL arg0, ResourceBundle arg1) {
//		Viewer temp = null;
//		for(int i =0; i<ViewerStorage.viewerCount;i++) {
//			if(ViewerStorage.viewerList.get(i).getUsername() == token) {
//				temp = ViewerStorage.viewerList.get(i);
//				break;
//			}
//		}
//		
//		for(int i = 0; i<temp.getCount(); i++) {
//			
//		watchList.getItems().add(temp.getWatchHistory()[i].getTitle());
//		}
//		
//		
//	}
	
	@FXML
	public void pressButton(ActionEvent event) throws Exception, SQLException, ClassNotFoundException {               
	    try {
	    	//check login
	    	String driver = "com.mysql.cj.jdbc.Driver";
	    	Class.forName(driver);
	    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");
	    	
	    	String username = uname.getText();
	    	String password = pass.getText();
	    	
	    	token = username;
	    	
	    	System.out.println(username);
	    	System.out.println(password);
	    	
	    	Statement stm = conn.createStatement();
	    	String q = "select * from viewer where username='"+username+"' and password='"+password+"'";
	    	ResultSet rs = stm.executeQuery(q);
	    	
	    	if(rs.next()) {
	    		Parent root = FXMLLoader.load(getClass().getResource("viewInterface.fxml"));
	    		s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    		sc = new Scene(root);
	    		s.setScene(sc);
	    		s.show();
	    	}
	    	else {
	    		JOptionPane.showMessageDialog(null, "username or password is incorrect", "Error", 0);
	    		uname.setText("");
	    		pass.setText("");
	    	}
	    	
	    	conn.close();
	    	
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	public void openRegister(ActionEvent event) throws Exception {               
	    try {
	    		
	    		Parent root = FXMLLoader.load(getClass().getResource("vsignup.fxml"));
	    		s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    		sc = new Scene(root);
	    		s.setScene(sc);
	    		s.show();
	    	
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	@FXML
	public void pressRegister(ActionEvent event) throws Exception, SQLException, ClassNotFoundException {               
	    try {

	    	String username = unamer.getText();
	    	String password = passr.getText();
	    	String em = email.getText();
	    	String d = dob.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	    	
	    	System.out.println(username);
	    	System.out.println(password);
	    	System.out.println(em);
	    	System.out.println(d);
	    	
	    	String driver = "com.mysql.cj.jdbc.Driver";
	    	Class.forName(driver);
	    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");

	    	Statement stm = conn.createStatement();
	    	String q = "select * from viewer where username='"+username+"'";
	    	ResultSet rs = stm.executeQuery(q);
	    	
	    	if(rs.next()) {
	    		JOptionPane.showMessageDialog(null, "username already taken", "Error", 0);
	    		unamer.setText("");
	    	}
	    	else {
	    	
	    	int a = (d.charAt(2) - '0')*10;
	    	int b = (d.charAt(3) - '0') + a;
	    	
	    	System.out.println(b);
	    	int diff = 24 - b;
	    	System.out.println(diff);
	    	Boolean agecheck = false;
	    	if(diff <= 13) {
	    		agecheck = true;
	    	}
	    	System.out.println(agecheck);
	    	
	    	ViewerStorage list = new ViewerStorage();
	    	list.addViewer(username, em, d, agecheck, password);
	    	list.display();
	    	

	    	Parent root = FXMLLoader.load(getClass().getResource("viewInterface.fxml"));
	    	s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	   		sc = new Scene(root);
	   		s.setScene(sc);
	   		s.show();
	    	}
	    	
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	@FXML
	public void openLogin(ActionEvent event) throws Exception {               
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
	
	public void logout(ActionEvent event) throws Exception {               
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
	
	public void openUpdate(ActionEvent event) throws Exception 
	{
		 try {
			
		    	Parent root = FXMLLoader.load(getClass().getResource("update.fxml"));
		    	s = (Stage)(((Node)event.getSource()).getScene().getWindow());
		    	sc = new Scene(root);
		    	s.setScene(sc);
		    	s.show();
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
	}
	
	@FXML
	public void update(ActionEvent event) throws Exception, SQLException, ClassNotFoundException {               
	    try {
	    	
	    	String username = uname2.getText();
	    	String password = pass2.getText();
	    	String em = email2.getText();
	    	String d = dob2.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	    	String t = token;
	    	
	    	System.out.println(username);
	    	System.out.println(password);
	    	System.out.println(em);
	    	System.out.println(d);
	    	
	    	int a = (d.charAt(2) - '0')*10;
	    	int b = (d.charAt(3) - '0') + a;
	    	
	    	System.out.println(b);
	    	int diff = 24 - b;
	    	System.out.println(diff);
	    	Boolean agecheck = false;
	    	if(diff <= 13) {
	    		agecheck = true;
	    	}
	    	System.out.println(agecheck);
	    	
	    	
	    	String driver = "com.mysql.cj.jdbc.Driver";
	    	Class.forName(driver);
	    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");
	    	System.out.println(t);
	    	Statement stm = conn.createStatement();
	    	String test = "select * from Amaranth.viewer where username='"+t+"'";
	    	ResultSet res = stm.executeQuery(test);
	    	if(res.next())
	    		System.out.println("found");
	    	else
	    		System.out.println("not found");
	    	String q = "UPDATE Amaranth.viewer SET username = '"+username+"',password = '"+password+"', email = '"+em+"', DOB = '"+d+"',isKid = "+agecheck+" WHERE username = '" + token + "'";
	    	
	    	int rs = stm.executeUpdate(q);
	    	
	    	
	    	System.out.println(rs);
	    	token = username;
	    	ViewerStorage list = new ViewerStorage();
	    	list.init();
	    	conn.close();
	    	
	    	
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	public void delete(ActionEvent event) throws Exception, SQLException, ClassNotFoundException {               
	    try {
	    	String t =token;
	    	
	    	String driver = "com.mysql.cj.jdbc.Driver";
	    	Class.forName(driver);
	    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");
	    	System.out.println(t);
	    	Statement stm = conn.createStatement();
	    	String test = "select * from Amaranth.viewer where username='"+t+"'";
	    	ResultSet res = stm.executeQuery(test);
	    	if(res.next())
	    		System.out.println("found");
	    	else
	    		System.out.println("not found");
	    	String q = "delete from Amaranth.viewer WHERE username = '" + token + "'";
	    	
	    	int rs = stm.executeUpdate(q);
	    	
	    	System.out.println(rs);
	    	
	    	ViewerStorage list = new ViewerStorage();
	    	list.init();
	    	conn.close();
	    	Parent root = FXMLLoader.load(getClass().getResource("vlogin.fxml"));
	    	s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    	sc = new Scene(root);
	    	s.setScene(sc);
	    	s.show();
	    	
	    	
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void search(ActionEvent event) throws Exception {
		String check = JOptionPane.showInputDialog("Enter Keyword", "");
		System.out.println(check);
		if(check == null || check.equals(""))
			return;
		//searching

		 try {
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("search.fxml"));
			 Parent root = loader.load();
			 searchController controller = loader.getController(); // Get the controller
			 
			 //mark the current viewer
			 for(int i =0; i<ViewerStorage.getViewerCount();i++) {
				 
					if(ViewerStorage.getViewerList().get(i).getUsername().equals(token)) {
						
						controller.setViewer(ViewerStorage.getViewerList().get(i));
						System.out.println(ViewerStorage.getViewerList().get(i).getUsername()); 
						break;
					}
				}
			 //populate list
			 //controller.searchList.getItems().add("Extra Video"); // Set additional data if needed
			 controller.initialize(check);
			 Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			 stage.setScene(new Scene(root));
			 stage.show();
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
		
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
	
	public void goBackmain(ActionEvent event) throws Exception {               
	    try {
	    	Parent root = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
	    	s = (Stage)(((Node)event.getSource()).getScene().getWindow());
	    	sc = new Scene(root);
	    	s.setScene(sc);
	    	s.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void homepage(ActionEvent event) throws Exception 
	{
		 try {
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
			 Parent root = loader.load();
			 listController controller = loader.getController(); // Get the controller
			 
			 //mark the current viewer
			 for(int i =0; i<ViewerStorage.getViewerCount();i++) {
				 
					if(ViewerStorage.getViewerList().get(i).getUsername().equals(token)) {
						
						controller.setViewer(ViewerStorage.getViewerList().get(i));
						System.out.println(ViewerStorage.getViewerList().get(i).getUsername()); 
						break;
					}
				}
			 
			 controller.videoList.getItems().add("Extra Video"); // dummy data
			 //populate list
			 Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			 stage.setScene(new Scene(root));
			 stage.show();
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
	}
	
	public void creator(ActionEvent event) throws Exception 
	{
		 try {
		    	Parent root = FXMLLoader.load(getClass().getResource("creatorreg.fxml"));
		    	s = (Stage)(((Node)event.getSource()).getScene().getWindow());
		    	sc = new Scene(root);
		    	s.setScene(sc);
		    	s.show();
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
	}
	
	public void settings(ActionEvent event) throws Exception 
	{
		 try {
			 	
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("setting.fxml"));
			 Parent root = loader.load();
			 viewerController controller = loader.getController();
			 Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			 stage.setScene(new Scene(root));
			 stage.show();
		     Viewer temp = null;
		     //get current viewer
				for(int i =0; i<ViewerStorage.getViewerCount();i++) {
					if(ViewerStorage.getViewerList().get(i).getUsername().equals(token)) {
						temp = ViewerStorage.getViewerList().get(i);
						break;
					}
				}
				
				for(int i = 0; i<temp.getCount(); i++) {
				//populate list using their watch history
				controller.watchList.getItems().add(temp.getWatchHistory()[i].getTitle());
				}
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
	}
	
	
}
