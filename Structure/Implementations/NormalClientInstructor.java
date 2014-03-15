package Structure.Implementations;

public interface NormalClientInstructor extends Structure.Interfaces.ClientInstructor{
	public void displayQuestionOnly (Structure.Interfaces.AbstractQuestion question);
	public void displayAnswerReceived(String teamName,String correctAnswer,String answerDetails);
}