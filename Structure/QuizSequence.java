package Structure;

public class QuizSequence implements java.io.Serializable {
	private String name;
	private RoundsList mainRound;
	private TeamsList teams;
	
	public QuizSequence(String name,RoundsList r,TeamsList c){
		this.name=name;
		mainRound=r;
		teams=c;
	}
	
	public String getName(){
		return name;
	}
	
	public RoundsList getMainRound(){
		return mainRound;
	}
	
	public TeamsList getCandidateTeams(){
		return teams;
	}
	
	public static String getExtension(){
		return ".qsq";
	}
}