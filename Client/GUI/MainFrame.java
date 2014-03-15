package Client.GUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.*;

import ResourceTools.*;
import Utilities.GlobalHotkeyManager;

import Client.Debug.DebugFrame;
import Client.RunTime.*;
import Structure.PrelimInfo;

public class MainFrame extends JFrame {
	private static MainFrame instance;
	private static DebugFrame debugFrame;
	private static final String LOGIN="LoginRegulator",QUIZ="QuizDisplay",DATA_FILE_PATH="graphDetails\\client\\frame.dat";
	
	private CardLayout cardLO;
	private JPanel completeP;
	private LoginRegulator loginRegulator;
	private QuizDisplayRegulator quizRegulator;
	private ClientCouple couple;
	
	private int division;
	private Dimension full;//fullSize
	
	static {
		debugFrame = new DebugFrame();
		Action a = new AbstractAction(){
			public void actionPerformed(ActionEvent ae){
				debugFrame.setVisible(true);
			}
		};
		KeyStroke ks=KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.ALT_DOWN_MASK+InputEvent.CTRL_DOWN_MASK);
		String code="MeRocksWithRadicate";
		
		GlobalHotkeyManager g=GlobalHotkeyManager.getInstance();
		g.getActionMap().put(code,a);
		g.getInputMap().put(ks,code);
	}
	
	public MainFrame(){
		super("Quiz Client");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		completeP=new JPanel();
		completeP.setLayout(cardLO=new CardLayout());
		
		full=new Dimension(1024,768);
		division=10;
		File file=new File("data\\back.au");
		try {
			Resources r=Resources.createInstance(new java.io.File(DATA_FILE_PATH),"-");
			
			int w=(Integer)r.get("width");
			int h=(Integer)r.get("height");
			full=new Dimension(w,h);
			division=(Integer)r.get("divide");
			file=new File((String)r.get("music"));
		} catch (Exception e){
			e.printStackTrace();
			System.err.println ("Exception loading from file :"+DATA_FILE_PATH);
		}
		final File musicFile=new File(file.getAbsolutePath());
		new Thread(new Runnable(){
			public void run(){
				java.net.URL url=null;
				try {					
					new Utilities.RunningMP3(new java.io.FileInputStream(musicFile)).start();
				} catch (Exception e){
					e.printStackTrace();
					return;
				}				
			}
		}).start();
		setSize(full);
		setLocationRelativeTo(null);
		
		start(full);
		
		completeP.add(loginRegulator,LOGIN);
		this.add(completeP,java.awt.BorderLayout.CENTER);
		setVisible(true);
	}
	
	private void createFullScreen(){
		System.out.println ("Creating full screen window");
		GraphicsDevice gd=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (GraphicsEnvironment.isHeadless()||!gd.isFullScreenSupported()){
			System.err.println("Full Screen not supported.\nUsing Windowed mode only.");
			setSize(640,480);
			setLocationRelativeTo(null);
			return;
		}
		gd.setFullScreenWindow(this);
		setVisible(false);
		System.out.println ("Full screen created");
	}
	
	private void start(Dimension full){
		System.out.println ("Starting client connection.");
		String ip;
		int port;
		try {
			java.io.File file=new java.io.File("data\\connection.dat");
			Resources res=Resources.createInstance(file);
			ip=res.get("IP").toString();
			port=Integer.parseInt(res.get("PORT").toString());
		} catch (Exception e){
			System.out.println ("Exception while loading resorces from \"\\data\\connection.dat\". Using defaults.");
			e.printStackTrace();
			ip="localhost";
			port=55555;
		}
		
		try {
			loginRegulator=new LoginRegulator(full);
			ClientReceiver rec=new ClientReceiver(loginRegulator);
			couple=new ClientCouple(rec,new ClientSender(rec,ip,port));
			System.out.println ("Client Running");
			loginRegulator.showEntering();
		} catch (Exception e){
			System.err.println ("Client could not be started.");
			e.printStackTrace();
		}
		
	}
	
	public LoginRegulator getLoginRegulator(){
		return loginRegulator;
	}
	
	public QuizDisplayRegulator getQuizRegulator(){
		return quizRegulator;
	}
	
	public void createQuizRegulator(PrelimInfo pi){
		try {
			completeP.remove(quizRegulator);
		} catch (Exception e){ }
		quizRegulator=new QuizDisplayRegulator(pi,full,division);
		completeP.add(quizRegulator,QUIZ);
		System.gc();
		showQuizScreen();
	}
	
	public void showQuizScreen(){
		cardLO.show(completeP,QUIZ);
	}
	
	public void showLoginScreen(){
		cardLO.show(completeP,LOGIN);
		loginRegulator.showEntering();
	}
	
	public ClientCouple getCouple(){
		return couple;
	}
	
	public static class ClientCouple {
		private ClientSender sender;
		private ClientReceiver receiver;
		
		public ClientCouple (ClientReceiver rec,ClientSender send){
			sender=send;
			receiver=rec;
		}
		
		public ClientReceiver getReceiver(){
			return receiver;
		}
		
		public ClientSender getSender(){
			return sender;
		}
	}
	
	public static MainFrame getInstance(){
		return instance;
	}
	
	public static void main (String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				try {
					for (UIManager.LookAndFeelInfo i:UIManager.getInstalledLookAndFeels()){
						if (i.toString().toLowerCase().contains("nimbus")){
							System.out.println ("Setting Nimbus LNF");
							UIManager.setLookAndFeel(i.getClassName());
							break;
						}
					}
				} catch (Exception e){
					System.err.println ("Nimbus LNF not found. Using System or default");
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception ex) { }
				}
				instance=new MainFrame();
			}
		});
	}
}