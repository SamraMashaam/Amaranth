package backend;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class VideoStorage 
{
	 public static  int videoCount;
	static  int videoID;
	 static ArrayList<video> videoList;
	
	public VideoStorage()
	{
		videoCount = 0;
		videoID = 0;
		setVideoList(new ArrayList<>());
	}
	
	//init function
	public void init() {
		try {
			String query = "select * from Amaranth.video";
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				video temp = new video(rs.getInt(1),rs.getString(3),rs.getBoolean(6),rs.getBoolean(9),rs.getInt(2),rs.getString(5),rs.getString(4),rs.getBoolean(7),rs.getBoolean(8),rs.getInt(12),rs.getInt(13)); 
				videoList.add(temp);
				videoCount++;
			}
			conn.close();
		}
		catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public Boolean addVideo( String t, Boolean spons, Boolean kids,int cr, String r, String path)
	{
		video v = new video( videoID,  t,  spons,  kids, cr,  r, path);
		++videoCount;
		++videoID;
		try {
		String query = "INSERT INTO Amaranth.video (videoID,creatorID,title,filepath,isFlag,isBan,forKid,isExclusive,isLivestream,likecount,viewcount) VALUES ('"+videoID+"','"+cr+"','"+t+"','"+path+"',"+false+","+false+","+kids+","+false+","+false+",'"+0+"','"+0+"');";
		String driver = "com.mysql.cj.jdbc.Driver";
		Class.forName(driver);
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");
		Statement stm = conn.createStatement();
		int rs = stm.executeUpdate(query);
		if(rs==0)
		{
			System.out.println("Insertion failed");
		}
		conn.close();
		}
		catch(Exception e) {
	        e.printStackTrace();
	    }
		
		return getVideoList().add(v);
	}
	
	ArrayList<video> getVideo(String search)
	{
		ArrayList<video> newvideoList =  new ArrayList<>();
		
		for(int i=0;i<videoCount;i++)
		{
			if(getVideoList().get(i).getTitle() == search)
			{
				newvideoList.add(getVideoList().get(i));
			}
		}
		return newvideoList;
	}
	
	video getVideo(int ID)
	{
		for(int i=0;i<videoCount;i++)
		{
			if(getVideoList().get(i).getID() == ID)
			{
				return getVideoList().get(i);
			}
		}
		return null;
	}
	
	Boolean removeVideo(int ID)
	{
		video v = getVideo(ID);
		if(v == null)
		{
			return false;
		}
		--videoCount;
		return getVideoList().remove(v);
	}

	public static ArrayList<video> getVideoList() {
		return videoList;
	}

	public static void setVideoList(ArrayList<video> videoList) {
		VideoStorage.videoList = videoList;
	}
	
	public void display()
	{
		System.out.print(videoCount);
		for(int i=0;i<videoCount;i++)
		{
			System.out.print(videoList.get(i).getTitle());
			System.out.println("\t");
			System.out.print(videoList.get(i).getPath());
			System.out.println("\n");
		}
	}

}
