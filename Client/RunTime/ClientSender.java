package Client.RunTime;

//import javax.swing.JOptionPane;

import intesoc.*;

import Structure.*;
import Structure.Interfaces.*;

public class ClientSender extends Sender implements ExceptionHandler{
	
	public ClientSender (Receiver r, String host, int port)throws Exception{
		CommunicationSystem.startClient(new Couple(this,r,this),
												CommunicationSystem.DEFAULT_EXCEPTION_HANDLER,
												host,port);
	}
	
	public void login(String username,char[] password){
		invokeMethod(new MethodDetails(Boolean.class,"login",
				new Class[]{String.class,char[].class},	username,password));
	}
	
	public void sendAnswer(String answer){
		invokeMethod(new MethodDetails(Void.class,"sendAnswer",
				new Class[]{String.class},answer));
	}
	
	public void logout(){
		invokeMethod(new MethodDetails(Void.class,"logout",
				new Class[]{}));
	}
	
	public void exceptionEncountered(Throwable t,CommunicationPlug cp){
		t.printStackTrace();
	}
		
	public void clientConnected(){
		System.out.println ("Connected to server!!");
		while (true){
			try {
				Client.GUI.MainFrame.getInstance().getLoginRegulator().showEntering();
				break;
			} catch (NullPointerException e){ }
		}
	}
}