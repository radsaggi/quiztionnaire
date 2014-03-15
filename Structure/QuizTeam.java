package Structure;

public class QuizTeam implements java.io.Serializable{
	private String password,userName;
	private String[] students;
	private boolean loggedIn;
	
	public QuizTeam(String pass,String user,String...candidates){
		password=pass;
		userName=user;
		students=candidates;
		loggedIn=false;
	}
	
	public boolean check(String user,String pass){
		return user.equalsIgnoreCase(userName)&&pass.equals(password);
	}
	
	public boolean equals(Object o){
		if (!(o instanceof QuizTeam))	return false;
		return ((QuizTeam)o).userName.equals(userName);
	}
	
	public boolean isLoggedIn(){
		return loggedIn;
	}
	
	public void setLoggedIn(boolean z){
		loggedIn=z;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public String getNamesString(){
		String str=java.util.Arrays.toString(students);
		
		return str.substring(1,str.length()-1);
	}
	
	public String toString(){
		return getUserName();
	}
}