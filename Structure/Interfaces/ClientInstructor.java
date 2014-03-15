package Structure.Interfaces;

public interface ClientInstructor {
	public void displayTimedOut(String message);
	public void getResponseForQuestion(AbstractQuestion question);
	public void setDisplayQuestionNumber(int number);
	public void setDisplayMaxQuestionNumber(int value);
	public void updatePoints(int newValue);
	public void displayAnswerResult(boolean isCorrect,String correctAnswer,String answerDetails);
}

/*
 *setDisplayRoundNumber(int)
 *loginComplete(PrelimInfo)
 *displayQuizComplete(TeamReport)
 *showWrongLogin(String message)
 *
 *these methods will pbe present in the Sender but not in the interfaces to ensure that they are only accessible to the QuizMaster
 */