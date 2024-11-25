package Project;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ContentCreator extends Application {

    private Map<String, VideoMetadata> videoLibrary = new HashMap<>();
    private VideoCapture camera;
    private boolean isStreaming = false;

    @Override
    public void start(Stage primaryStage) {
        // Load OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Header label with Amaranth styling
        Label welcomeLabel = new Label("Welcome, Content Creator!");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ffffff;");

        Button uploadVideoButton = createStyledButton("Upload Video");
        Button livestreamButton = createStyledButton("Start Livestream");
        Button viewLibraryButton = createStyledButton("View Video Library");

        uploadVideoButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mov"));
            fileChooser.setTitle("Choose Video File");
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                showMetadataDialog(file, statusLabel);
            } else {
                statusLabel.setText("No file selected.");
            }
        });

        livestreamButton.setOnAction(event -> {
            if (!isStreaming) {
                startLivestream(primaryStage);
                statusLabel.setText("Livestream started!");
            } else {
                stopLivestream();
                statusLabel.setText("Livestream stopped!");
            }
        });

        viewLibraryButton.setOnAction(event -> {
            if (videoLibrary.isEmpty()) {
                showAlert("Video Library", "No videos uploaded.");
            } else {
                showLibraryDialog(primaryStage);
            }
        });

        VBox vbox = new VBox(20, welcomeLabel, uploadVideoButton, livestreamButton, viewLibraryButton, statusLabel);
        vbox.setPadding(new Insets(30));
        vbox.setStyle("""
                -fx-background-color: #000000; /* Black background */
                -fx-alignment: center;
                """);

        Scene scene = new Scene(vbox, 400, 350);
        primaryStage.setTitle("Content Creator Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("""
                -fx-background-color: #800000;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 10px 20px;
                -fx-background-radius: 25px;
                -fx-border-color: #e52b50;
                -fx-border-width: 2px;
                """);
        button.setOnMouseEntered(event -> button.setStyle("""
                -fx-background-color: #e52b50;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 10px 20px;
                -fx-background-radius: 25px;
                -fx-border-color: #ffffff;
                -fx-border-width: 2px;
                """));
        button.setOnMouseExited(event -> button.setStyle("""
                -fx-background-color: #800000;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 10px 20px;
                -fx-background-radius: 25px;
                -fx-border-color: #e52b50;
                -fx-border-width: 2px;
                """));
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private void showMetadataDialog(File file, Label statusLabel) {
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
        dialogContent.setStyle("""
                -fx-background-color: #000000; /* Black background */
                -fx-text-fill: white;
                """);

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
                    String newFilePath = file.getParent() + File.separator + metadata.getTitle() + ".mp4";
                    Files.move(file.toPath(), new File(newFilePath).toPath());
                    videoLibrary.put(newFilePath, metadata);
                    statusLabel.setText("Uploaded and renamed to: " + metadata.getTitle());
                } catch (IOException e) {
                    showAlert("Error", "Failed to rename the file.");
                }
            } else {
                showAlert("Incomplete Metadata", "Both title and rating are required.");
            }
        });
    }

    private void showLibraryDialog(Stage primaryStage) {
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(videoLibrary.keySet());
        listView.setStyle("""
                -fx-background-color: #000000; /* Black background */
                -fx-text-fill: white;
                -fx-border-color: #800000;
                -fx-border-width: 2px;
                """);

        Button viewButton = createStyledButton("View");
        Button deleteButton = createStyledButton("Delete");
        Button editButton = createStyledButton("Edit");

        VBox vbox = new VBox(10, listView, new HBox(10, viewButton, deleteButton, editButton));
        vbox.setPadding(new Insets(10));
        vbox.setStyle("""
                -fx-background-color: #000000; /* Black background */
                """);

        Scene libraryScene = new Scene(vbox, 400, 400);
        Stage libraryStage = new Stage();
        libraryStage.setTitle("Video Library");
        libraryStage.setScene(libraryScene);
        libraryStage.show();

        viewButton.setOnAction(event -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) playVideo(selected, primaryStage);
        });

        deleteButton.setOnAction(event -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                videoLibrary.remove(selected);
                listView.getItems().remove(selected);
            }
        });

        editButton.setOnAction(event -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                VideoMetadata metadata = videoLibrary.get(selected);
                showMetadataDialog(new File(selected), new Label());
            }
        });
    }

    private void startLivestream(Stage primaryStage) {
        camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            showAlert("Error", "Could not open camera!");
            return;
        }

        // Set the frame size for the stream (ensure it matches the capture resolution)
        Size frameSize = new Size(640, 480);  // Adjust this if needed

        // Initialize VideoWriter for saving the stream
        String outputPath = "livestream_output.mp4";  // Specify the output file path
        int fourcc = VideoWriter.fourcc('X', 'V', 'I', 'D');  // XVID codec
        double fps = 30.0;  // Frames per second

        // Initialize VideoWriter with XVID codec and correct frame size
        VideoWriter videoWriter = new VideoWriter(outputPath, fourcc, fps, frameSize, true);

        if (!videoWriter.isOpened()) {
            showAlert("Error", "Could not initialize VideoWriter!");
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

        Thread streamThread = new Thread(() -> {
            Mat frame = new Mat();
            while (isStreaming) {
                camera.read(frame);

                if (!frame.empty()) {
                    // Convert frame to Image for displaying
                    Image image = matToImage(frame);
                    gc.drawImage(image, 0, 0);

                    // Write the frame to the video file
                    videoWriter.write(frame);
                }
            }

            // Release resources when the livestream ends
            camera.release();
            videoWriter.release();
        });

        isStreaming = true;
        streamThread.start();
    }



    private Image matToImage(Mat frame) {
        BufferedImage bufferedImage = (BufferedImage) HighGui.toBufferedImage(frame);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private void stopLivestream() {
        isStreaming = false;
    }

    private void playVideo(String videoPath, Stage primaryStage) {
        javafx.scene.media.Media media = new javafx.scene.media.Media(new File(videoPath).toURI().toString());
        javafx.scene.media.MediaPlayer mediaPlayer = new javafx.scene.media.MediaPlayer(media);
        javafx.scene.media.MediaView mediaView = new javafx.scene.media.MediaView(mediaPlayer);

        VBox vbox = new VBox(mediaView);
        vbox.setStyle("-fx-alignment: center;");

        Scene videoScene = new Scene(vbox, 640, 480);
        Stage videoStage = new Stage();
        videoStage.setTitle("Video Playback");
        videoStage.setScene(videoScene);
        videoStage.show();

        mediaPlayer.play();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("""
                -fx-background-color: #000000; /* Black background */
                -fx-text-fill: white;
                """);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

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
