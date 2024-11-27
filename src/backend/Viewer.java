package backend;


public class Viewer 
{
	private int viewerID;
	private String username;
	private String email;
	private String DOB;
	private video watchHistory[];
	private int count = 0;
	private Boolean kidsAcc;
	private String password;
	private Boolean isCreator;
	private Boolean isSubscriber;
	
	public Viewer(int viewerCount, String username2, String email2, String dOB2, Boolean kids, String pass)
	{
		viewerID = viewerCount;
		username = username2;
		email = email2;
		DOB = dOB2;
		setWatchHistory(new video[20]);
		kidsAcc = kids;
		setPassword(pass);
		setIsCreator(false);
		setIsSubscriber(false);
	}
	
	public void watchvideo(video v)
	{
		getWatchHistory()[getCount()] = v;
		setCount(getCount() + 1);
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dOB) {
		DOB = dOB;
	}
	public Boolean getKidsAcc() {
		return kidsAcc;
	}
	public void setKidsAcc(Boolean kidsAcc) {
		this.kidsAcc = kidsAcc;
	}
	public int getViewerID() {
		return viewerID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsCreator() {
		return isCreator;
	}

	public void setIsCreator(Boolean isCreator) {
		this.isCreator = isCreator;
	}

	public Boolean getIsSubscriber() {
		return isSubscriber;
	}

	public void setIsSubscriber(Boolean isSubscriber) {
		this.isSubscriber = isSubscriber;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public video[] getWatchHistory() {
		return watchHistory;
	}

	public void setWatchHistory(video watchHistory[]) {
		this.watchHistory = watchHistory;
	}
	
}
