package backend;

import java.sql.*;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class creator {
    int ID;
    String username;
    boolean isFlagged;
    private Map<String, VideoMetadata> videoLibrary = new HashMap<>();
    private VideoCapture camera;
    private boolean isStreaming = false;

    public creator(int i, String u) {
        isFlagged = false;
        ID = i;
        username = u;
    }

    // Method to handle flagging form
    public void flagForm(String f) {
        System.out.println(f);
        // Handle flagging functionality here
    }

    // Method to upload video with metadata input
    public void uploadVideo(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv"));
        
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
        	showMetadataDialog(selectedFile, stage);
        	System.out.println("Video selected: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("No file selected.");
        }
    }


    // Method to show metadata dialog for the video
    private void showMetadataDialog(File file, Stage primaryStage) {
        Dialog<VideoMetadata> metadataDialog = new Dialog<>();
        metadataDialog.setTitle("Video Metadata");
        metadataDialog.setHeaderText("Enter details for the uploaded video:");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        metadataDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField();
        titleField.setPromptText("Enter video title");
        ComboBox<String> ratingComboBox = new ComboBox<>();
        ratingComboBox.getItems().addAll("Kids", "Adults");
        ratingComboBox.setPromptText("Select rating");

        VBox dialogContent = new VBox(10, new Label("Title:"), titleField, new Label("Rating:"), ratingComboBox);
        dialogContent.setPadding(new Insets(10));

        metadataDialog.getDialogPane().setContent(dialogContent);

        metadataDialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new VideoMetadata(titleField.getText(), ratingComboBox.getValue());
            }
            return null;
        });

        metadataDialog.showAndWait().ifPresent(metadata -> {
            if (metadata.isComplete()) {
                try {
                    // Ensure the video file is saved in a consistent location
                    String storageDir = "C:/videos"; // Replace with your storage directory path
                    Files.createDirectories(Paths.get(storageDir)); // Ensure the directory exists
                    String newFilePath = storageDir + File.separator + metadata.getTitle() + ".mp4";
                    Files.move(file.toPath(), new File(newFilePath).toPath());

                    // Save metadata to the database (Optional if you have a database connection)
                    saveVideoToDatabase(newFilePath, metadata);

                    System.out.println("Uploaded and saved to database: " + metadata.getTitle());
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                    System.out.println("Failed to save video to the database.");
                }
            } else {
                System.out.println("Incomplete Metadata. Both title and rating are required.");
            }
        });
    }

    // Method to save video metadata to database
    private void saveVideoToDatabase(String filePath, VideoMetadata metadata) throws SQLException {
        try (Connection conn = connectToDatabase()) {
            if (conn != null) {
                String sql = "INSERT INTO videos (path, title, rating, creator_id) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, filePath);
                pstmt.setString(2, metadata.getTitle());
                pstmt.setString(3, metadata.getRating());
                pstmt.setInt(4, this.ID); // Storing creator's ID along with the video
                pstmt.executeUpdate();
            }
        }
    }

    // Method to connect to the database
    private Connection connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/Amaranth";
            String user = "root";
            String password = "mumtaz sana";
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Database Error: Could not connect to the database.");
            return null;
        }
    }

    // Method to start livestreaming
    public void startLivestream(Stage primaryStage) {
        camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            return;
        }

        // Set frame size
        Size frameSize = new Size(640, 480);

        // Initialize VideoWriter for saving the stream
        String outputPath = "livestream_output.mp4";  // Specify output file path
        int fourcc = VideoWriter.fourcc('X', 'V', 'I', 'D');  // XVID codec
        double fps = 30.0;  // Frames per second

        VideoWriter videoWriter = new VideoWriter(outputPath, fourcc, fps, frameSize, true);

        if (!videoWriter.isOpened()) {
            return;
        }

        Canvas canvas = new Canvas(frameSize.width, frameSize.height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        VBox vbox = new VBox(canvas);
        vbox.setStyle("-fx-alignment: center;");

        Scene videoScene = new Scene(vbox, frameSize.width, frameSize.height);
        Stage videoStage = new Stage();
        videoStage.setTitle("Livestream");
        videoStage.setScene(videoScene);
        videoStage.show();

        // Thread to handle livestream
        Thread streamThread = new Thread(() -> {
            Mat frame = new Mat();
            while (isStreaming) {
                camera.read(frame);
                if (!frame.empty()) {
                    // Convert frame to Image for displaying
                    Image image = matToImage(frame);
                    gc.drawImage(image, 0, 0);

                    // Write the frame to video file
                    videoWriter.write(frame);
                }
            }

            // Release resources when livestream ends
            camera.release();
            videoWriter.release();
        });

        isStreaming = true;
        streamThread.start();
    }

    // Method to convert OpenCV Mat frame to JavaFX Image
    private Image matToImage(Mat frame) {
        BufferedImage bufferedImage = (BufferedImage) HighGui.toBufferedImage(frame);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    // Method to stop livestream
    private void stopLivestream() {
        isStreaming = false;
    }

    // VideoMetadata inner class to store metadata of videos
    private static class VideoMetadata {
        private final String title;
        private final String rating;

        public VideoMetadata(String title, String rating) {
            this.title = title;
            this.rating = rating;
        }

        public String getTitle() {
            return title;
        }

        public String getRating() {
            return rating;
        }

        public boolean isComplete() {
            return title != null && !title.isBlank() && rating != null;
        }
    }
}
