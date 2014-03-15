package Structure;

public  class Answer {
	private String answer;
	private String answerInfo;	
	
	public Answer (String answer){
		this.answer=answer;
	}
	
	public final boolean checkAnswer (String s){
		return answer.equalsIgnoreCase(s);
	}
	
	public String getAnswer(){
		return answer;
	}
	
	public void setAdditionalInfo(String str){
		if (!str.isEmpty())//do not entertain empty strings
			answerInfo=str;
	}
	
	public String getDisplayAnswer(){
		if (answerInfo!=null)
			return answerInfo;
		else return "";
	}
}