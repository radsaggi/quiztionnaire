package Client.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

import ResourceTools.*;
import Client.RunTime.*;

public class LoginRegulator extends JPanel implements ActionListener{	
	private CardLayout cardLO;
	private JPanel panel;
	private JTextField userNameTF;
	private JPasswordField passwordPF;
	private JButton loginB;
	private JLabel processingL;
	
	private String processingPrompt;
	
	private static final String PROCESS="Process",ENTER="Enter";
	
	public LoginRegulator(Dimension full){
		
		String title=null,userNamePrompt=null,passwordPrompt=null,loadingPrompt=null,imagePath="data\\back.jpg";
		int width=0,height=0;
		int entryR=0,entryB=0,entryG=0;
		int backAnimePause=1000;
		float lower=0.2f,upper=0.7f,diff=0.01f;
		try {
			Resources r=Resources.createInstance(new java.io.File("graphDetails//client//loginGUI.dat"),"-");
			title=(String)r.get("title");
			userNamePrompt=(String)r.get("username");
			passwordPrompt=(String)r.get("password");
			width=(Integer)r.get("width");
			height=(Integer)r.get("height");
			entryR=(Integer)r.get("entry red");
			entryB=(Integer)r.get("entry blue");
			entryG=(Integer)r.get("entry green");
			processingPrompt=(String)r.get("processing");
			imagePath=(String)r.get("background image");
			lower=(Float)r.get("lower animation value");
			upper=(Float)r.get("upper animation value");
			diff=(Float)r.get("difference animation value");
			backAnimePause=(Integer)r.get("background image pause");
			loadingPrompt=(String)r.get("loading");
		} catch (Exception e){
			System.err.println("Error loding from file :\"\\graphDetails\\client\\loginGUI.dat\" "+e.getMessage()+". Using defaults.");
			if (title==null) 			title="Enter login details:";
			if (userNamePrompt==null) 	userNamePrompt="Username : ";
			if (passwordPrompt==null) 	passwordPrompt="Password : ";
			if (processingPrompt==null)	processingPrompt="Processing. Please Wait.";
			if (width==0)				width=400;
			if (height==0)				height=200;
			if (loadingPrompt==null)	loadingPrompt="Connectiong to server. Please wait";
		}
		
		setBackground(java.awt.Color.black);
		setLayout(new GridLayout(1,1));
		JLabel loadedImage=loadBackgroundImage(imagePath,backAnimePause,lower,upper,diff);
		
		panel=new JPanel(cardLO=new CardLayout());		
		Rectangle r=getCenterOfScreen(full,new Dimension(width,height));
		JPanel entryPanel=createEntryPanel(title,userNamePrompt,passwordPrompt,
											r,	entryR,entryG,entryB);
		JPanel processingPanel = createProcessingPanel(r,  loadingPrompt,
											entryR,entryG,entryB);
		panel.add(entryPanel,ENTER);
		panel.add(processingPanel,PROCESS);
		
		JLayeredPane layered=new JLayeredPane();
		layered.setLayout(null);
		layered.setOpaque(false);
		loadedImage.setBounds(0,0,(int)full.getWidth(),(int)full.getHeight());
		layered.add(loadedImage,JLayeredPane.DEFAULT_LAYER);
		panel.setBounds(r);
		layered.add(panel,JLayeredPane.DRAG_LAYER);
		
		this.add(layered);
		
		showProcessing();
	}
	
	private static void createFullScreen(JFrame frame){
		System.out.println ("Creating full screen window");
		GraphicsDevice gd=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (GraphicsEnvironment.isHeadless()||!gd.isFullScreenSupported()){
			System.err.println("Full Screen not supported.\nUsing Windowed mode only.");
			frame.setSize(640,480);
			frame.setLocationRelativeTo(null);
			return;
		}
		gd.setFullScreenWindow(frame);
		frame.setVisible(false);
		System.out.println ("Full screen created");
	}
	
	private Rectangle getCenterOfScreen(Dimension full,Dimension dim){
		Dimension d=full;
		int fullWidth=(int)d.getWidth();
		int fullHeight=(int)d.getHeight();
		int x=(fullWidth-(int)dim.getWidth())/2;
		int y=(fullHeight-(int)dim.getHeight())/2;
		return new Rectangle(new Point(x,y),dim);
	}
	
	private JPanel createProcessingPanel(Rectangle d,String processingPrompt,
											int r,int g,int b){
		JPanel retP=new JPanel(new GridLayout(1,1));
		retP.setOpaque(true);
		
		JProgressBar pb=new JProgressBar();
		pb.setIndeterminate(true);
		
		JLabel label=new JLabel(processingPrompt,SwingConstants.CENTER);
		processingL=label;
		Font pre=label.getFont();
		Font nxt=new Font(pre.getName(),Font.BOLD,pre.getSize()+5);
		label.setFont(nxt);
		JPanel panel=new JPanel(new GridLayout(2,1));
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		panel.setOpaque(true);
		panel.setBackground(new Color(r,b,g));
		panel.setBounds(d);
		panel.add(label);
		{	JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER));
			p.setOpaque(false);
			p.add(pb);
			panel.add(p);
		}		
		
		retP.add(panel);
		return retP;
	}
	
	private JPanel createEntryPanel(String title,String userNamePrompt,String passwordPrompt,
									Rectangle d,int r,int g,int b){
		JPanel retP=new JPanel();
		retP.setOpaque(true);
		retP.setBackground(new Color(r,g,b));
		retP.setLayout(new GridLayout(4,1));
		retP.setSize(400,200);
		retP.setBorder(BorderFactory.createLoweredBevelBorder());		
		
		JLabel titleL= new JLabel(title,SwingConstants.CENTER);
		Font f=titleL.getFont();
		f=new Font(f.getName(),Font.BOLD,f.getSize()+2);
		titleL.setFont(f);
		JLabel userNameL=new JLabel (userNamePrompt);
		JLabel passwordL=new JLabel (passwordPrompt) ;
		
		userNameTF= new JTextField(30);
		userNameTF.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				passwordPF.requestFocusInWindow();
			}
		});
		userNameL.setLabelFor( userNameTF);
		userNameL.setDisplayedMnemonic(userNamePrompt.charAt(0));
		passwordPF= new JPasswordField(30);
		passwordPF.addActionListener(this);
		passwordL.setLabelFor(passwordPF);
		passwordL.setDisplayedMnemonic(passwordPrompt.charAt(0));
		
		loginB = new JButton ("Login");
		loginB.addActionListener(this);
		
		JButton closeB = new JButton("Exit");
		closeB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				System.exit(0);
			}
		});
		
		retP.add(titleL);
		JPanel temp=null;
		{	temp=new JPanel (new FlowLayout(FlowLayout.CENTER));
			temp.setOpaque(false);
			temp.add(userNameL);
			temp.add(userNameTF);
			retP.add(temp);
		}
		{	temp=new JPanel (new FlowLayout(FlowLayout.CENTER));
			temp.setOpaque(false);
			temp.add(passwordL);
			temp.add(passwordPF);
			retP.add(temp);
		}
		{	temp=new JPanel (new FlowLayout(FlowLayout.CENTER));
			temp.setOpaque(false);
			temp.add(loginB);
			temp.add(closeB);
			retP.add(temp);
		}
		
 		JPanel panel=new JPanel();
		panel.setOpaque(false);
		panel.setLayout(null);
		panel.add(retP);
		return panel;
	}
	
	public void showProcessing(){
		cardLO.show(panel,PROCESS);
	}
	
	public void showEntering(){
		cardLO.show(panel,ENTER);
	}
	
	public void showError(String error){
		userNameTF.setText("");
		passwordPF.setText("");
		showEntering();
		JOptionPane.showMessageDialog(this,error,"Wrong Login!!",JOptionPane.ERROR_MESSAGE);
	}
	
	public void actionPerformed(ActionEvent ae){
		final ClientSender sender=MainFrame.getInstance().getCouple().getSender();
		final String userName=userNameTF.getText();
		userNameTF.setText("");
		final char[] password= passwordPF.getPassword();
		passwordPF.setText("");
		
		processingL.setText(processingPrompt);
		showProcessing();
		
		new Thread(new Runnable (){
			public void run(){
				sender.login(userName,password);
			}
		}).start();
	}
	
	private JLabel loadBackgroundImage(String path,int pause,float lower,float upper,float diff){
    	JLabel ret=new JLabel ();
    	try {
    		ret.setHorizontalAlignment(SwingConstants.CENTER);
    		BufferedImage image=javax.imageio.ImageIO.read(new File(path));
    		new ImageAnime(pause,image,ret,lower,upper,diff).execute();
    		ret.setIcon(new ImageIcon(image));
    		return ret;
    	}catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
    private static class ImageAnime extends SwingWorker<Void,Void>{
    	private int pause;
    	private BufferedImage image;
    	private JLabel label;
    	private float lower,upper,diff;
    	
    	public ImageAnime(int pause,BufferedImage image,JLabel label,float lower,float upper,float diff){
    		this.pause=pause;
    		this.image=image;
    		this.label=label;
    		this.lower=lower;
    		this.upper=upper;
    		this.diff=diff;
    	}
    	
    	public Void doInBackground(){
    		float value=upper;
    		float dif=-diff;
    		
    		while(true){
    			BufferedImage img=loadTranslucentImage(image,value);
    			label.setIcon(new ImageIcon(img));
    			//label.repaint();
    			value+=dif;
    			if (value<lower)dif=diff;
    			if (value>upper)dif=-diff;
    			try {
    				Thread.sleep(pause);
 					System.gc();
    			} catch (Exception e){ }
    		}
    	}
    	
    	private BufferedImage loadTranslucentImage(BufferedImage loaded, float transperancy) {  
	        // Create the image using the   
   			BufferedImage aimg = new BufferedImage(loaded.getWidth(), loaded.getHeight(), BufferedImage.TRANSLUCENT);  
        	// Get the images graphics  
        	Graphics2D g = aimg.createGraphics();  
        	// Set the Graphics composite to Alpha  
        	g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));  
        	// Draw the LOADED img into the prepared reciver image  
        	g.drawImage(loaded, null, 0, 0);  
        	// let go of all system resources in this Graphics  
        	g.dispose();  
	        // Return the image
   	    	return aimg;  
   	 	}
    	
    	public void done(){
    		throw new IllegalStateException("The animation thread terminated unexpectedly!!!!");
    	}
    }  
    	
    public static void main (String[] args) {
    	SwingUtilities.invokeLater(new Runnable(){
    		public void run(){
    			
    			JFrame frame=new JFrame();
				frame.setLayout(new BorderLayout());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				LoginRegulator lr=new LoginRegulator(new Dimension(1024,768));
				lr.showProcessing();
				//adp.displayAnswer(true,"Ashutosh","Ashutosh is a hero!!");
				frame.add(lr,BorderLayout.CENTER);
				//createFullScreen(frame);
				frame.setSize(1024,768);
				frame.setVisible(true);
    			
    			
    		}
    	});
	}
    
}