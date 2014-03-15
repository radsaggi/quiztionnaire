package Structure.Implementations;

import java.util.Collection;

import Structure.*;
import Structure.Interfaces.*;
import Utilities.SearchableList;

public class RapidFireTransferRenderer implements TransferRenderer<RapidFireQuestion,RapidFireClientInstructor>,Runnable {
	private Thread t;
	private boolean isPaused;
	private int timerValue;
	
	private int currQuestion,currClient;
	private SearchableList<RapidFireQuestion> questions;
	private SearchableList<Answer> answers;
	private SearchableList<RapidFireClientInstructor> connections;
	
	public void init(Collection<RapidFireQuestion> quests,Collection<Answer> answ,RapidFireClientInstructor[] clients){
		currClient=currQuestion=0;
		
		connections=new SearchableList<RapidFireClientInstructor>(clients);
		this.questions=new SearchableList<RapidFireQuestion>(quests);
		this.answers=new SearchableList<Answer>(answ);
		
		timerValue=0;
		for (RapidFireQuestion q:quests){
			timerValue+=q.getTimerValue();
		}
		
		connections.get(currClient).getResponseForQuestion(this.questions.get(currQuestion));
		t=new Thread(this);
		t.start();
		
		for (int i=0;i<connections.size();i++){
			if (i!=currClient)
				connections.get(i).displayTimedOut("Waiting for turn.");
		}
	}
	
	public boolean transfer(RapidFireClientInstructor client,String answer){
		Answer ans=answers.get(currQuestion);
		pauseTimer();
		connections.get(currClient).displayAnswerResult(ans.checkAnswer(answer),ans.getAnswer(),ans.getDisplayAnswer());
				
		try {
			Thread.sleep(5000);
		} catch (Exception e){ }
		
		currQuestion++;
		if (currQuestion==questions.size()){
			connections.get(currClient).displayTimedOut("Rapid fire Over!!");
			currClient++;
			if (currClient==connections.size())
				return true;
			
			if (t.isAlive()) t.interrupt();
			try {
				Thread.sleep(10);
			} catch (Exception e){ }
			connections.get(currClient).getResponseForQuestion(this.questions.get(currQuestion));
			t=new Thread(this);
			t.start();
		} else {
			connections.get(currClient).getResponseForQuestion(this.questions.get(currQuestion));
			resumeTimer();
		}		
		return false;
	}
	
	public void run(){
		isPaused=false;
		try {
			int iter=timerValue*10;
			int i=0;
			while(i<iter){
				while(isPaused) Thread.sleep(10);
				Thread.sleep(100);
				i++;
			}
		} catch (InterruptedException e){
			System.out.println ("RapidFire complete!");
			return;
		}
		
		Answer ans=answers.get(currQuestion);
		connections.get(currClient).displayAnswerResult(ans.checkAnswer(""),ans.getAnswer(),ans.getDisplayAnswer());		
		
		try {
			Thread.sleep(5000);
		} catch (Exception e){ }
		connections.get(currClient).displayTimedOut("Rapid fire Over!!");
	}
	
	private void pauseTimer(){
		isPaused=true;
	}
	
	private void resumeTimer(){
		isPaused=false;
	}
}