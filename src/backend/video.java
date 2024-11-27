package backend;

import java.util.ArrayList;

public class video {
	int ID;
	String title;
	 Boolean sponsored;
	 int viewCount;
	 int likeCount;
	 Boolean kidsContent;
	 ArrayList<String> comments;
	boolean flag;
	private boolean ban;
	String path;
	int creatorID;
	private String rating; 
	private boolean liked;
	
	
	public video(int id, String t, Boolean spons, Boolean kids,int cr, String r, String p)
	{
		title = t;
		ID = id;
		sponsored = spons;
		kidsContent = kids;
		viewCount = 0;
		likeCount = 0;
		creatorID = cr;
		path = p;
		flag = false;
		setBan(false);
		setRating(r);
		comments = new ArrayList<>();
	}
	
	public video(int id, String t, Boolean spons, Boolean kids,int cr, String r, String p,boolean f, boolean b, int l, int v)
	{
		title = t;
		ID = id;
		sponsored = spons;
		kidsContent = kids;
		viewCount = v;
		likeCount = l;
		creatorID = cr;
		flag = f;
		path = p;
		setBan(b);
		setRating(r);
		comments = new ArrayList<>();
	}
	
	public video(){
		ID =-10;
		creatorID = -10;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getID() {
		return ID;
	}
	public Boolean getSponsored() {
		return sponsored;
	}
	public void setSponsored(Boolean sponsored) {
		this.sponsored = sponsored;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void incrementViews() {
		++viewCount;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void incrementLikes() {
		++likeCount;
	}
	public ArrayList<String> getComments() {
		return comments;
	}	
	public Boolean addComment(String comm)
	{
		return comments.add(comm);
	}	
	public Boolean getFlag() {
		return flag;
	}
	public void flag(Boolean flag) {
		this.flag = flag;
	}
	public Boolean getKidsContent() {
		return kidsContent;
	}
	public void changeRating(Boolean kidsContent) {
		this.kidsContent = kidsContent;
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return path;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public boolean isBan() {
		return ban;
	}

	public void setBan(boolean ban) {
		this.ban = ban;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}


}
