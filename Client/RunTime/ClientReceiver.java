package Client.RunTime;

import intesoc.*;

import Client.GUI.*;
import Structure.PrelimInfo;
import Structure.TeamReport;
import Structure.Interfaces.AbstractQuestion;

public class ClientReceiver implements Receiver {
	private QuizDisplayRegulator quizRegulator;
	private LoginRegulator loginRegulator;
	
	public ClientReceiver(LoginRegulator lr){
		if (lr==null)	throw new NullPointerException();
		loginRegulator=lr;
	}
	
	public void displayTimedOut(String message){
		System.out.println ("displayTimedOut;");
		quizRegulator.displayTimedOut(message);
	}
	public void getResponseForQuestion(AbstractQuestion quest){
		System.out.println ("getResponseFor;");
		
		quizRegulator.displayFullQuestion(quest);
	}
	public void setDisplayQuestionNumber(int number){
		System.out.println ("setDisplayQuestionNumber;");
		quizRegulator.setQuestionNumber(number);
	}
	public void setDisplayMaxQuestionNumber(int n){
		System.out.println ("setDisplayMaxQuestionNumber;");
		quizRegulator.setMaxQuestionNumber(n);
	}
	public void updatePoints(int newValue){
		System.out.println ("updatePoints;");
		quizRegulator.setPoints(newValue);
	}
	public void displayAnswerResult(boolean isCorrect,String correctAnswer,String answerDetails){
		System.out.println ("displayAnswerResult;");
		quizRegulator.displayAnswer(isCorrect,correctAnswer,answerDetails);
	}
	
	
	public void displayQuestionOnly(AbstractQuestion question){
		System.out.println ("displayQuestionOnly;");
		quizRegulator.displayQuestionOnly(question);
	}
	public void displayAnswerReceived(String teamName,String correctAnswer,String answerDetails){
		System.out.println ("displayAnswerReceived;");
		quizRegulator.answerReceived(teamName,correctAnswer,answerDetails);
	}
	
	
	public void setDisplayRoundNumber(Integer number){
		System.out.println ("setDisplayRoundNumber;");
		quizRegulator.setRoundNumber(number);
	}
	public void loginComplete(PrelimInfo info){
		MainFrame.getInstance().createQuizRegulator(info);
		
		//this is better
		//quizRegulator.init(info);
		quizRegulator=MainFrame.getInstance().getQuizRegulator();
		quizRegulator.setMaxRoundNumber(info.getRounds());
	}
	public void displayQuizComplete(TeamReport tr){
		System.out.println ("displayQuizComplete;");
		quizRegulator.displayResult(tr);
	}	
	public void showWrongLogin(String msg){
		loginRegulator.showError(msg);
	}	
}