package Server.RunTime;

import Structure.*;
import Structure.Interfaces.*;
import Structure.Implementations.*;
import intesoc.*;

import Utilities.IDManager;

public class ServerSender extends Sender implements ClientInstructor,NormalClientInstructor{
	
	//Implementing ClientInstructor
	public void displayTimedOut(String message){
		invokeMethod(new MethodDetails(Void.class,"displayTimedOut",
				new Class<?>[]{String.class},message));
	}
	public void getResponseForQuestion(AbstractQuestion quest){
		invokeMethod(new MethodDetails(Void.class,"getResponseForQuestion",
				new Class<?>[]{AbstractQuestion.class},quest));
	}
	public void setDisplayQuestionNumber(int number){
		invokeMethod(new MethodDetails(Void.class,"setDisplayQuestionNumber",
				new Class<?>[]{int.class},number));
	}
	public void setDisplayMaxQuestionNumber(int n){
		invokeMethod(new MethodDetails(Void.class,"setDisplayMaxQuestionNumber",
				new Class<?>[]{int.class},n));
	}
	public void updatePoints(int newValue){
		invokeMethod(new MethodDetails(Void.class,"updatePoints",
				new Class<?>[]{int.class},newValue));
	}
	public void displayAnswerResult(boolean isCorrect,String correctAnswer,String answerDetails){
		invokeMethod(new MethodDetails(Void.class,"displayAnswerResult",
				new Class<?>[]{boolean.class,String.class,String.class},isCorrect,correctAnswer,answerDetails));
	}
	
	//Implementing NormalClientListener 
	public void displayQuestionOnly(AbstractQuestion quest){
		invokeMethod(new MethodDetails(Void.class,"displayQuestionOnly",
				new Class<?>[]{AbstractQuestion.class},quest));
	}
	public void displayAnswerReceived(String teamName,String correctAnswer,String answerDetails){
		invokeMethod(new MethodDetails(Void.class,"displayAnswerReceived",
				new Class<?>[]{String.class,String.class,String.class},teamName,correctAnswer,answerDetails));
	}
	
	//Other methods not visible to any transfer handler but required by the quiz master
	public void setDisplayRoundNumber(Integer number){
		invokeMethod(new MethodDetails(Void.class,"setDisplayRoundNumber",
				new Class<?>[]{Integer.class},number));
	}
	public void loginComplete(PrelimInfo pre){
		invokeMethod(new MethodDetails(Void.class,"loginComplete",
				new Class<?>[]{PrelimInfo.class},pre));
	}
	public void displayQuizComplete (TeamReport rep){
		invokeMethod(new MethodDetails(Void.class,"displayQuizComplete",
				new Class<?>[]{TeamReport.class},rep));
	}	
	public void showWrongLogin(String message){
		invokeMethod(new MethodDetails(Void.class,"showWrongLogin",
				new Class<?>[]{String.class},message));
	}
}
