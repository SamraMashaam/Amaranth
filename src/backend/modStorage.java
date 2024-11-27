package backend;
import java.util.ArrayList;

import java.sql.*;

public class modStorage {
	 static ArrayList<mod> modList;
	 static int modCount;
	 static int modID;
	
	 public modStorage()
		{
			modList = new ArrayList<>(20);
			modCount = 0;
			modID = 0;
		}
	 
	 public void init() {
			try {
				String query = "select * from Amaranth.moderator";
				String driver = "com.mysql.cj.jdbc.Driver";
				Class.forName(driver);
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Amaranth?useSSL=false","root","mumtaz sana");
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery(query);
				while(rs.next()) {
					mod temp = new mod(rs.getInt(1),rs.getString(2),rs.getString(3)); 
					modList.add(temp);
					modCount++;
				}
				conn.close();
			}
			catch(Exception e) {
		        e.printStackTrace();
		    }
		}
	

}
