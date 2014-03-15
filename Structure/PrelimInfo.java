package Structure;

public class PrelimInfo implements java.io.Serializable {
	private String displayName;
	private int rounds;
	
	public PrelimInfo (String displayName,int rounds){
		this.displayName=displayName;
		this.rounds=rounds;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public int getRounds(){
		return rounds;
	}
}