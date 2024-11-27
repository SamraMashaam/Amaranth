package backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class creatorStorage {

    // Method to store a creator's profile in the database
    public void storeCreatorProfile(creator creator) {
        try (Connection conn = connectToDatabase()) {
            if (conn != null) {
                String sql = "INSERT INTO creators (id, username, is_flagged) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, creator.ID);
                pstmt.setString(2, creator.username);
                pstmt.setBoolean(3, creator.isFlagged);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error storing creator profile: " + e.getMessage());
        }
    }

    // Method to get a list of all videos uploaded by a specific creator
    public List<VideoMetadata> getVideosByCreator(int creatorId) {
        List<VideoMetadata> videos = new ArrayList<>();
        try (Connection conn = connectToDatabase()) {
            if (conn != null) {
                String sql = "SELECT * FROM videos WHERE creator_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, creatorId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String title = rs.getString("title");
                    String rating = rs.getString("rating");
                    String filePath = rs.getString("path");
                    videos.add(new VideoMetadata(title, rating, filePath));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving videos: " + e.getMessage());
        }
        return videos;
    }

    // Method to delete a creator's profile from the database
    public void deleteCreatorProfile(int creatorId) {
        try (Connection conn = connectToDatabase()) {
            if (conn != null) {
                String sql = "DELETE FROM creators WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, creatorId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error deleting creator profile: " + e.getMessage());
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

    // Inner class to represent Video Metadata (used in storing and retrieving video details)
    public static class VideoMetadata {
        private final String title;
        private final String rating;
        private final String filePath;

        public VideoMetadata(String title, String rating, String filePath) {
            this.title = title;
            this.rating = rating;
            this.filePath = filePath;
        }

        public String getTitle() {
            return title;
        }

        public String getRating() {
            return rating;
        }

        public String getFilePath() {
            return filePath;
        }
    }
}
