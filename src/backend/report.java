package backend;

public class report {
	private video videoID = null;
	private String reporterID=" ";
	private String deets=" ";
	public String reportID;
	static int c;
	
	public report()
	{
		
	}
	
	public report(video v, String r, String d)
	{
		setVideoID(v);
		setReporterID(r);
		setDeets(d);
		reportID = "Report " + c;
		c++;
	}

	public String getReporterID() {
		return reporterID;
	}

	public void setReporterID(String reporterID) {
		this.reporterID = reporterID;
	}

	public String getDeets() {
		return deets;
	}

	public void setDeets(String deets) {
		this.deets = deets;
	}

	public video getVideoID() {
		return videoID;
	}

	public void setVideoID(video videoID) {
		this.videoID = videoID;
	}

}
