package application;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SampleController {
	
	Stage s;
	Scene sc;
	Parent root;
	@FXML
	private TextField uname;
	@FXML
	private PasswordField pass;
	
	
	@FXML
	public void pressButton(ActionEvent event) throws Exception, SQLException, ClassNotFoundException {               
	    try {
	    	//check login
	    	String driver = "com.mysql.cj.jdbc.Driver";
	    	Class.forName(driver);
	    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");
	    	
	    	String username = uname.getText();
	    	String password = pass.getText();
	    	
	    	System.out.println(username);
	    	System.out.println(password);
	    	
	    	Statement stm = conn.createStatement();
	    	String q = "select * from moderator where username='"+username+"' and  password='"+password+"'";
	    	ResultSet rs = stm.executeQuery(q);
	    	
	    	if(rs.next()) {
	    		 FXMLLoader loader = new FXMLLoader(getClass().getResource("modInterface.fxml"));
	    		 Parent root = loader.load();
	    		 interfaceController controller = loader.getController();
	    		
	    		 Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
		         Scene scene = new Scene(root);
		         stage.setScene(scene);
		         stage.show();

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
}
