package backend;
import java.util.ArrayList;
import java.util.Date;

import java.sql.*;

public class ViewerStorage 
{
	static private ArrayList<Viewer> viewerList;
	private static  int viewerCount;
	static private int viewerID;
	
	public ViewerStorage()
	{
		setViewerList(new ArrayList<>(20));
		setViewerCount(0);
		viewerID = 0;
	}
	
	public void init() {
		try {
			String query = "select * from Amaranth.viewer";
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				Viewer temp = new Viewer(rs.getInt(1),rs.getString(2),rs.getString(4),rs.getString(5),rs.getBoolean(6),rs.getString(3)); 
				getViewerList().add(temp);
				setViewerCount(getViewerCount() + 1);
			}
			conn.close();
		}
		catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public Boolean addViewer(String username, String email, String d1, Boolean kids, String password)
	{
		Viewer viewer = new Viewer(viewerID, username, email, d1, kids, password);
		setViewerCount(getViewerCount() + 1);
		++viewerID;
		
		try {
		String query = "INSERT INTO Amaranth.viewer (ID, username, password, email, DOB, isKid, isCreator, isSubscriber) VALUES ('"+viewerID+"', '"+username+"','"+password+"','"+email+"','"+d1+"',"+kids+","+false+","+false+");";
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
//		Connection connection = null;
//		try {
//			connection = DBUtil.getConnection();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	             try {
//					PreparedStatement stmt = connection.prepareStatement(query);
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//	            
	             
		return getViewerList().add(viewer);
	}
	
	Viewer getViewer(int ID)
	{
		for(int i=0;i<getViewerCount();i++)
		{
			if(getViewerList().get(i).getViewerID() == ID)
			{
				return getViewerList().get(i);
			}
		}
		return null;
	}
	
	Boolean removeViewer(int ID)
	{
		Viewer v = getViewer(ID);
		if(v == null)
		{
			return false;
		}
		setViewerCount(getViewerCount() - 1);
		return getViewerList().remove(v);
	}
	
	public void display()
	{
		for(int i=0;i<getViewerCount();i++)
		{
			System.out.print(getViewerList().get(i).getUsername());
			System.out.println("\t");
			System.out.print(getViewerList().get(i).getEmail());
			System.out.println("\n");
		}
	}

	public static int getViewerCount() {
		return viewerCount;
	}

	public static void setViewerCount(int viewerCount) {
		ViewerStorage.viewerCount = viewerCount;
	}

	public static ArrayList<Viewer> getViewerList() {
		return viewerList;
	}

	public static void setViewerList(ArrayList<Viewer> viewerList) {
		ViewerStorage.viewerList = viewerList;
	}
}
