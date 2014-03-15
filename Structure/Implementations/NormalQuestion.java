package Structure.Implementations;

import Structure.Interfaces.AbstractQuestion;

public class NormalQuestion extends AbstractQuestion{
	private int time;
	
	public NormalQuestion(Object question,int time){
		super(question);
		this.time=time;
	}
	
	public int getTimerValue(){
		return time;
	}
}