package Structure;

import java.util.ArrayList;
import Structure.Interfaces.*;

public class QuizRound<AQ extends AbstractQuestion,C extends ClientInstructor> implements java.io.Serializable{
	private ArrayList<AQ> questions;
	private ArrayList<Answer> answers;
	private TransferRenderer<AQ,C> transfer;
	private int correctAns,wrongAns,passedAns;
	
	public QuizRound (TransferRenderer<AQ,C> tr){
		questions=new ArrayList<AQ>();
		answers=new ArrayList<Answer>();
		transfer=tr;
	}
	
	public void initTransferRenderer(C[] arr){
		transfer.init(questions,answers,arr);
	}
	
	public TransferRenderer<AQ,C> getTransferRenderer(){
		return transfer;
	}
	
	public int getQuestionCount(){
		return questions.size();
	}
	
	public void addQuestion(AQ qc,Answer ans){
		questions.add(qc);
		answers.add(ans);
	}
	
	public void setCorrectAnswerPoints(int value){
		correctAns=value;
	}
	
	public void setWrongAnswerPoints(int value){
		wrongAns=value;
	}
	
	public void setPassedAnswerPoints(int value){
		passedAns=value;
	}
	
	public int getCorrectAnswerPoints(){
		return correctAns;
	}
	
	public int getWrongAnswerPoints(){
		return wrongAns;
	}
	
	public int getPassedAnswerPoints(){
		return passedAns;
	}
	
	public ArrayList<AQ> getQuestions(){
		return questions;
	}
	
	public ArrayList<Answer> getAnswers(){
		return answers;
	}
}