package Structure;

public class TeamReport implements java.io.Serializable{
	private int rank,score;
	
	public TeamReport(int rank,int score){
		this.rank=rank;
		this.score=score;
	}
	
	public int getRank(){
		return rank;
	}
	
	public int getScore(){
		return score;
	}
}