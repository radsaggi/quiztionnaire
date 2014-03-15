package Server.RunTime;

import Utilities.IDManager;

public interface ClientListener {
	public void login(String userName,char[] password,IDManager id);
	public void logout(IDManager id);
	public void checkAnswer (String answer,IDManager id);
}