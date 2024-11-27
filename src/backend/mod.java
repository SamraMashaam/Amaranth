package backend;

import java.util.ArrayList;

public class mod {
	
	int ID;
	String username;
	String password;
	video watchHistory[];
	 public static ArrayList<report> reportList= new ArrayList<report>();
	int count = 0;
	private static int rcount = 0;
	
	public mod(int i, String u){
		ID = i;
		username = u;
		watchHistory = new video[20];
		//setReportList(new report[20]);
	}
	
	public mod(int i, String u, String p){
		ID = i;
		username = u;
		password = p;
		watchHistory = new video[20];
		//setReportList(new report[20]);
	}
	
	public void watchvideo(video v)
	{
		watchHistory[count] = v;
		count++;
	}
	
	public void flagcontent(int i)	//called from controller, has video ID/object as parameter
	{
		watchHistory[i].flag = true;
		String reason = "inappropriate";
		//take input from dialog box;
		//watchHistory[i].c.flagform(reason);
	}
	
	public void bancontent(int i)	//called from controller, has video ID/object as parameter
	{
		watchHistory[i].setBan(true);
		String reason = "inappropriate content for this site";
		//take input from dialog box;
		//watchHistory[i].c.flagform(reason);
	}
	
	
	
	public void changerating(int i, String c) {
		System.out.println("Current rating: ");
		System.out.println(watchHistory[i].getRating());
		watchHistory[i].setRating(c);
		String reason = "Incorrect rating regarding subject matter of video";
		System.out.println("New rating: ");
		System.out.println(watchHistory[i].getRating());
		//watchHistory[i].c.flagform(reason);
	}
	
	public void handlereport(int i) {
		//System.out.println(getReportList()[i].getDeets());
		//depending on choice, call flag, changerating, or ban functions
	}

	public static ArrayList<report> getReportList() {
		return reportList;
	}

	public static void setReportList(ArrayList<report> reportList) {
		mod.reportList = reportList;
	}

	public static int getRcount() {
		return rcount;
	}
	
	public static void display() {
		for(int i =0;i<reportList.size();i++) {
			System.out.println(reportList.get(i).reportID);
			System.out.println(reportList.get(i).getDeets());
		}
	}

	public static void setRcount(int rcount) {
		mod.rcount = rcount;
	}
}
