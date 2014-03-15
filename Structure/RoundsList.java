package Structure;

import java.util.ArrayList;

public class RoundsList implements java.io.Serializable{
	private ArrayList<QuizRound<?,?>> subComponents;
	
	public RoundsList (){
		subComponents=new ArrayList<QuizRound<?,?>>();
	}
	
	public int getNumberOfRounds(){
		return subComponents.size();
	}
	
	public void addQuizRound(QuizRound<?,?> qc){
		subComponents.add(qc);
	}
	
	public ArrayList<QuizRound<?,?>> getRounds(){
		return subComponents;
	}
}