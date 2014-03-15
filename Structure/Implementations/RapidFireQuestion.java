package Structure.Implementations;

import Structure.Interfaces.AbstractQuestion;

public class RapidFireQuestion extends AbstractQuestion {
	private int time;
	
	public RapidFireQuestion(Object question,int time){
		super(question);
		this.time=time;
	}
	
	public int getTimerValue(){
		return time;
	}
}