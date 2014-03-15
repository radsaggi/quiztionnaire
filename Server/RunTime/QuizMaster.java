package Server.RunTime;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.TreeMap;

import Structure.*;
import Structure.Implementations.*;
import Structure.Interfaces.*;

import intesoc.*;
import ResourceTools.*;
import Utilities.IDManager;
import Utilities.SearchableList;

public class QuizMaster extends ServerConnector implements ClientListener{
	private static final int PORT;
	
	static {
		int port=5050;
		try {
			Resources r=Resources.createInstance(new java.io.File("data\\connection.dat"),"%^");
			port=(Integer)r.get("PORT");
		} catch (Exception e){
			System.err.println("Error loding from file :\"data\\connection.dat\" "+e+". Using defaults.");
		}
		PORT=port;
	}
	
	private QuizSequence qsq;
	private TreeMap<IDManager,ServerCouple> mappedConnections;
	private boolean quizRunning;
	
	//The running quiz condition
	private int currentRound;
	
	public QuizMaster(QuizSequence qsq){
		this.qsq=qsq;
		mappedConnections=new TreeMap<IDManager,ServerCouple>();
		quizRunning=false;
	}
	
	public void startServer(){
		try {
			System.out.println ("Attempting to start the server");
			CommunicationSystem.startServer(this,PORT);
			System.out.println ("Server started");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void startQuiz(){
		quizRunning=true;
		System.out.println ("Starting quiz");
		//MainFrame.getInstance().quizStarted();
		currentRound=-1;
		new Thread(new Runnable(){
			public void run(){
				changeRound();		
			}
		}).start();
	}
	
	private void changeRound(){
		currentRound++;
		if (currentRound==qsq.getMainRound().getNumberOfRounds()){
			quizComplete();
			try {
				Thread.sleep(1000);
			} catch (Exception e){ }
			System.gc();
			return;
		}
		
		QuizRound<?,?> qr=qsq.getMainRound().getRounds().get(currentRound);
		
		SearchableList<ServerCouple> couples=new SearchableList<ServerCouple>(mappedConnections.values());
		Iterator<ServerCouple> itr=couples.iterator();
		while(itr.hasNext()){
			ServerCouple s=itr.next();
			if (!s.isLoggedIn()){
				itr.remove();
				continue;
			}
			s.getSender().setDisplayRoundNumber(currentRound+1);
			s.getSender().setDisplayMaxQuestionNumber(qr.getQuestionCount());
		}
		
		SearchableList<ServerSender> ss=couples.getAll(new SearchableList.ListFilter<ServerCouple,ServerSender>(){
			public ServerSender accept(ServerCouple sc){
				return sc.getSender();
			}
		});
		
		qr.initTransferRenderer(ss.toArray(new ServerSender[ss.size()]));
	}
	
	private void quizComplete(){
		System.out.println ("Quiz Complete !!");
	}
	
	//Implementing the abstract methods of ServerConnector
	@Override
	public Couple generateNewCouple(InetAddress ia){
		System.out.println ("Add request from address-"+ia.toString());
		IDManager id=IDManager.createNewID();
		ServerCouple sc=new ServerCouple(this,id);
		mappedConnections.put(id,sc);
		return sc;
	}
	@Override
	public void lastAdded(){
		System.out.println ("Adding the couple complete!!!");
	}
	@Override
	public void exceptionEncountered(Throwable e){
		System.err.println ("\nException in ServerConnector");
		e.printStackTrace();
	}
	@Override
	public void exceptionEncountered(Throwable t, CommunicationPlug plug){
		System.err.println ("\nException in CommunicationPlug connected with "+plug.getDetails());
		t.printStackTrace();
		IDManager id=(IDManager)plug.getIdentifier();
		logout(id);
	}
	
	//implementing the methods of ClientListener
	@Override
	public void login(final String userName,final char[] password,IDManager id){
		if (quizRunning){
			mappedConnections.get(id).getSender().showWrongLogin("Cannot login when the quiz is running.");
			return;
		}
		
		TeamsList ct=qsq.getCandidateTeams();
		QuizTeam qt=ct.getFirst(new SearchableList.ListFilter<QuizTeam,QuizTeam>(){
			public QuizTeam accept(QuizTeam qt){
				if (qt.check(userName,new String(password)))
					return qt;
				return null;
			}
		});
		if (qt==null||qt.isLoggedIn()){
			ServerCouple couple=mappedConnections.get(id);
			String message = qt==null? "Illegal username or password":"You are already logged into another PC.";
			couple.getSender().showWrongLogin(message);
			return;
		}
		
		qt.setLoggedIn(true);
		ServerCouple couple=mappedConnections.get(id);
		couple.loginTeam(qt);
		couple.getSender().loginComplete(new PrelimInfo(qt.getNamesString(),qsq.getMainRound().getNumberOfRounds()));
	}
	@Override
	public void logout(IDManager id){
		ServerCouple sc=mappedConnections.get(id);
		if (sc.isLoggedIn()){
			sc.logOutTeam();
			//MainFrame.getInstance().getConnectionsPanel().removeLoggedIn(	 sc.getLoggedInTeam().getUserName()  );
		}
	}
	@Override
	public void checkAnswer (final String answer,IDManager id){
		final ServerCouple sc=mappedConnections.get(id);
		Runnable r= new Runnable(){
			public void run(){
				QuizRound qr=qsq.getMainRound().getRounds().get(currentRound);
				boolean z=qr.getTransferRenderer().transfer(sc.getSender(),answer);
				if (z)	changeRound();
			}
		};
		
		new Thread(r).start();		
	}
	
	
	public static void main (String[] args)throws Exception {
		NormalQuestion q1=new NormalQuestion("Who is the PM of India?",20);
		Answer a1=new Answer("Dr Manmohan Singh");
		
		NormalQuestion q2=new NormalQuestion("Who is next after Munni?",10);
		Answer a2=new Answer("Shiela");
		
		QuizRound<NormalQuestion,NormalClientInstructor> qr=new QuizRound<NormalQuestion,NormalClientInstructor>(new NormalTransferRenderer());
		qr.addQuestion(q1,a1);
		qr.addQuestion(q2,a2);
		RoundsList rl=new RoundsList();
		rl.addQuizRound(qr);
		
		QuizTeam t1=new QuizTeam("Agarwal","Ashutosh","Ashutosh");
		QuizTeam t2=new QuizTeam("Singh","Satwik","satwik");
		QuizTeam t3=new QuizTeam("Kapoor","Ranbir","Ranbir");
		TeamsList tl=new TeamsList();
		tl.add(t1);
		tl.add(t2);
		tl.add(t3);
		
		QuizSequence qsq=new QuizSequence("Ashutosh",rl,tl);
		
		QuizMaster qm=new QuizMaster(qsq);
		qm.startServer();
		
		BufferedReader ob=new BufferedReader (new InputStreamReader (System.in));
		ob.readLine();
		
		qm.startQuiz();
	}
}