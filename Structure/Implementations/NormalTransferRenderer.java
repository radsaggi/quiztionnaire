package Structure.Implementations;

import java.util.Collection;

import Structure.Answer;
import Structure.Interfaces.*;
import Utilities.SearchableList;

public class NormalTransferRenderer implements TransferRenderer<NormalQuestion,NormalClientInstructor>,Runnable{
	private int currentQuestion;
	private int passedHolder,originalHolder;
	private Thread waitingThread;
	private SearchableList<NormalQuestion> questions;
	private SearchableList<Answer> answers;
	private SearchableList<NormalClientInstructor> connections;
	
	public void run(){
		try {
			System.out.println ("Starting timer");
			int counter=questions.get(currentQuestion).getTimerValue();
			while (counter!=-1){
				counter--;
				System.out.println ("Timer:"+counter);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e){
					return;
				}
			}
			
			System.out.println ("time up");
			connections.get(passedHolder).displayTimedOut("Time UP!!");
			try {
				Thread.sleep(5000);
			} catch (Exception e){ }
			passToNextConnection();
		} catch (Exception e){	}
	}
	
	//The answers and questions should be in the same order
	public void init(Collection<NormalQuestion> quests,Collection<Answer> answ,NormalClientInstructor[] clients){
		currentQuestion=0;
		originalHolder=0;
		passedHolder=0;
		
		connections=new SearchableList<NormalClientInstructor>(clients);
		this.questions=new SearchableList<NormalQuestion>(quests);
		this.answers=new SearchableList<Answer>(answ);
		
		NormalQuestion qq=questions.get(currentQuestion);
		connections.get(passedHolder).getResponseForQuestion( qq );
		waitingThread = new Thread(this);
		waitingThread.start();
		
		connections.get(0).setDisplayQuestionNumber(currentQuestion+1);			
		
		for (int i=1;i<connections.size();i++){
			connections.get(i).displayQuestionOnly(qq);
			connections.get(i).setDisplayQuestionNumber(currentQuestion+1);
		}
	}
	
	//true if round over
	public boolean transfer(NormalClientInstructor client,String answer){
		
		//System.out.println (couple.getLoggedInTeam().getUserName()+" sent answer "+answer);
		if (!client.equals(connections.get(passedHolder)))return false;
		
		waitingThread.interrupt();
		Answer currQuest=answers.get(currentQuestion);
		boolean z=currQuest.checkAnswer(answer);
		System.out.println ("Answer result :"+z);
		NormalClientInstructor cpl=connections.get(passedHolder);
		
		if (z){
			for (NormalClientInstructor s:connections){
				if (!s.equals(cpl))	{
					s.displayAnswerReceived("Some one",
															currQuest.getAnswer(),currQuest.getDisplayAnswer());
					//System.out.println (s.getLoggedInTeam().getUserName()+" notfied of answer");
				} else {
					//System.out.println (s.getLoggedInTeam().getUserName()+" not notfied of answer");
				}
			}
			cpl.displayAnswerResult(z,currQuest.getAnswer(),currQuest.getDisplayAnswer());
			//cpl.addPoints(passedHolder==originalHolder?round.getCorrectAnswerPoints():round.getSkippedAnswerPoints());
			//cpl.updatePoints(cpl.getPoints());
			try {
				Thread.sleep(5000);
			} catch (Exception e){ }
			return passToNextQuestion();
		} else {
			cpl.displayAnswerResult(z,"WRONG ANSWER","");
			//cpl.reducePoints(round.getWrongAnswerPoints());
			//cpl.updatePoints(cpl.getPoints());
			try {
				Thread.sleep(5000);
			} catch (Exception e){ }
			passToNextConnection();
		}
		
		
		return false;
	}
	
	private boolean passToNextQuestion(){
		originalHolder++;
		if (originalHolder==connections.size())
			originalHolder=0;
		passedHolder=originalHolder;
		currentQuestion++;
		
		if (currentQuestion==questions.size()){
			return true;
		}
		
		for (NormalClientInstructor s:connections){
			s.setDisplayQuestionNumber(currentQuestion+1);
		}
		
		NormalQuestion qq=questions.get(currentQuestion);
		connections.get(passedHolder).getResponseForQuestion(qq);
		for (int i=0;i<connections.size();i++){
			if (connections.get(i)!=connections.get(originalHolder))
				connections.get(i).displayQuestionOnly(qq);
		}
		waitingThread = new Thread(this);
		waitingThread.start();
		return false;
	}
	
	private void passToNextConnection(){
		passedHolder++;
		if (passedHolder==connections.size()){
			passedHolder=0;
		}
		if (passedHolder==originalHolder){
			for (NormalClientInstructor sc:connections){
				
				sc.displayAnswerReceived("NO ONE",
						answers.get(currentQuestion).getAnswer(),
						answers.get(currentQuestion).getDisplayAnswer());
			}
			try {
				Thread.sleep(5000);
			} catch (Exception e){ }
			passToNextQuestion();
			return;
		}
		
		NormalQuestion qq=questions.get(currentQuestion);
		connections.get(passedHolder).getResponseForQuestion(qq);
		waitingThread = new Thread(this);
		waitingThread.start();
	}
}