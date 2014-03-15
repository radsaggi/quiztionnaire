package Server.RunTime;

import intesoc.*;
import Utilities.IDManager;

public class ServerReceiver implements intesoc.Receiver {
	private IDManager id;
	private ClientListener cl;
	
	public ServerReceiver (ClientListener cl,IDManager i){
		this.cl=cl;
		this.id=i.clone();
	}
	
	public void login(String username,char[] password){	
		cl.login(username,password,id);
	}
	
	public void logout(){
		cl.logout(id);
	}
	
	public void sendAnswer(final String answer){
		System.out.println ("receiveing answer "+answer);
		new Thread(new Runnable(){
			public void run(){
				cl.checkAnswer(answer,id);
			}
		}).start();
	}
}