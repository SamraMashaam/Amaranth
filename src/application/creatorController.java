package application;

import backend.creator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class creatorController {

    // FXML elements for Login Page
    @FXML private TextField uname;
    @FXML private PasswordField pass;
    @FXML private Button loginbtn;
    @FXML private Hyperlink signuplink;
    @FXML private Button backToMainButton;

    // FXML elements for Registration Page
    @FXML private TextField unamer;
    @FXML private PasswordField passr;
    @FXML private TextField email;
    @FXML private DatePicker dob;
    @FXML private Button registerbtn;
    @FXML private Hyperlink loginlink;

    // FXML elements for Content Creator Interface
    @FXML private Button uploadVideoBtn;
    @FXML private Button startStreamBtn;
    @FXML private Button viewLibraryBtn;
    @FXML private Button viewStatsBtn;
    @FXML private Button manageAccountBtn; // New Button for Account Management

    private creator currentCreator;
    private AnchorPane creatorInterface;


    @FXML
    public void initialize() {
        currentCreator = new creator(1, "contentCreator");
    }

    // Handle login button click event
    @FXML
    private void pressButton(ActionEvent e) {
        String username = uname.getText();
        String password = pass.getText();

        if (validateCredentials(username, password)) {
            openContentCreatorInterface(e);
        } else {
            showErrorAlert("Login failed! Invalid username or password.");
        }
    }

    private boolean validateCredentials(String username, String password) {
        // Query for a viewer
        String viewerQuery = "SELECT * FROM viewer WHERE username = ? AND password = ?";
        String creatorQuery = "SELECT * FROM creator WHERE username = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth", "root", "mumtaz sana")) {
            
            // First check in the 'viewer' table
            PreparedStatement stmtViewer = conn.prepareStatement(viewerQuery);
            stmtViewer.setString(1, username);
            stmtViewer.setString(2, password);
            ResultSet rsViewer = stmtViewer.executeQuery();
            
            if (rsViewer.next()) {
                // User found in viewer table
                currentCreator = new creator(rsViewer.getInt("ID"), rsViewer.getString("username"));
                return true;
            }
            
            // If not found in viewer table, check in the 'creator' table
            PreparedStatement stmtCreator = conn.prepareStatement(creatorQuery);
            stmtCreator.setString(1, username);
            stmtCreator.setString(2, password);
            ResultSet rsCreator = stmtCreator.executeQuery();
            
            if (rsCreator.next()) {
                // User found in creator table
                currentCreator = new creator(rsCreator.getInt("ID"), rsCreator.getString("username"));
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace(); // Log error for debugging
        }
        
        return false;
    }

    private void openContentCreatorInterface(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/creatorInterface.fxml"));
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Content Creator Interface");
            stage.show();
             } catch (Exception er) {
            er.printStackTrace();
            showErrorAlert("Error opening Content Creator Interface.");
        }
    }

    // Open Registration Page
    @FXML
    private void openRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/creatorreg.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Creator Registration");
            stage.show();
            Stage currentStage = (Stage) signuplink.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error opening Registration Page.");
        }
    }

    @FXML
    private void registerCreator(ActionEvent e) {
        String username = unamer.getText();
        String password = passr.getText();
        String emailText = email.getText();
        String dobText = (dob.getValue() != null) ? dob.getValue().toString() : "";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth", "root", "mumtaz sana")) {
            String sql = "INSERT INTO users (username, password, email, dob) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, emailText);
            stmt.setString(4, dobText);
            stmt.executeUpdate();
            System.out.println("Registration successful for: " + username);
            openLoginPage(e);
        } catch (SQLException er) {
            er.printStackTrace();
            showErrorAlert("Failed to register user.");
        }
    }

    private void openLoginPage(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/clogin.fxml"));
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Content Creator Login");
            stage.show();
        } catch (Exception er) {
            er.printStackTrace();
            showErrorAlert("Error opening Login Page.");
        }
    }

    @FXML
    private void uploadVideo(ActionEvent e) {
        if (currentCreator != null) {
            currentCreator.uploadVideo((Stage)((Node)e.getSource()).getScene().getWindow());
        } else {
            showErrorAlert("Creator instance not initialized.");
        }
    }

    @FXML
    private void startStream(ActionEvent e) {
        if (currentCreator != null) {
            currentCreator.startLivestream((Stage)((Node)e.getSource()).getScene().getWindow());
        } else {
            showErrorAlert("Creator instance not initialized.");
        }
    }

//    // **New Functionality 1: View Library**
//    @FXML
//    private void viewLibrary() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/videoLibrary.fxml"));
//            Stage stage = new Stage();
//            stage.setScene(new Scene(loader.load()));
//            stage.setTitle("Video Library");
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            showErrorAlert("Error opening Video Library.");
//        }
//    }

//    // **New Functionality 2: View Statistics**
//    @FXML
//    private void viewStats() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/stats.fxml"));
//            Stage stage = new Stage();
//            stage.setScene(new Scene(loader.load()));
//            stage.setTitle("View Statistics");
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            showErrorAlert("Error opening Statistics Page.");
//        }
//    }
//
//    // **New Functionality 3: Manage Account**
//    @FXML
//    private void manageAccount() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/manageAccount.fxml"));
//            Stage stage = new Stage();
//            stage.setScene(new Scene(loader.load()));
//            stage.setTitle("Manage Account");
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            showErrorAlert("Error opening Account Management Page.");
//        }
//    }
//
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goBackmain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/mainPage.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Main Page");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error going back to Main Page.");
        }
    }
}
