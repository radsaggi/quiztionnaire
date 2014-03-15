package Server.RunTime;

import Structure.QuizTeam;

import intesoc.Couple;
import Utilities.IDManager;

public class ServerCouple extends Couple{
	private QuizTeam team;
	private int points;
	
	public ServerCouple (ClientListener cl,IDManager id){
		super(new ServerSender(),new ServerReceiver(cl,id),id);
		
		points=0;
	}
	
	public IDManager getID(){
		return (IDManager)getIdentifier();
	}
	
	public ServerSender getSender(){
		return (ServerSender)super.getSender();
	}
	
	public ServerReceiver getReceiver(){
		return (ServerReceiver)super.getReceiver();
	}
	
	public void loginTeam(QuizTeam t){
		team=t;
		t.setLoggedIn(true);
	}
	
	public QuizTeam getLoggedInTeam(){
		return team;
	}
	
	public boolean isLoggedIn(){
		return team!=null;
	}
	
	public void logOutTeam(){
		team.setLoggedIn(false);
		team=null;
	}
	
	public void  addPoints(int addValue){
		points+=addValue;
	}
	
	public void reducePoints(int reduceValue){
		points-=reduceValue;
	}
	
	public int getPoints(){
		return points;
	}
}