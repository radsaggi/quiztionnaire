package Client.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import Structure.*;
import Structure.Interfaces.*;

public class QuizDisplayRegulator extends JPanel {
	private CardLayout cardLO;
	private JPanel panel;
	private String names;
	
	private static final String FULL_QUESTION="FullQuestion",
								TIMED_OUT="TimedOut",
								QUESTION_ONLY="QuestionOnly",
								ANSWER_DISPLAY="AnswerDisplay",
								ANSWER_RECEIVED="AnswerReceived",
								QUIZ_COMPLETE="QuizComplete";
	
	private FullQuestionPanel fullQuestionPanel;
	private TimedOutPanel timedOutPanel;
	private QuestionOnlyPanel questionOnlyPanel;
	private AnswerDisplayPanel answerDisplayPanel;
	private AnswerReceivedPanel answerReceivedPanel;
	private QuizCompletePanel quizCompletePanel;
	
	private JLabel pointsL,questionsAttemptedL,roundsClearedL;
	private PointsAnime pointsAnimation;
	private int totalRounds;
	private int totalQuestions;
	private int timerInit;
	
	private JLabel imageL;
	private ImageIcon questionII,infoII,resultII;
	private ImageAnime imageAnimation;
	private static final String DATA_FILE_PATH="graphDetails\\client\\images.dat";
	
	private JLayeredPane layered;
	private final Integer UPPER_LAYER=new Integer(5),MIDDLE_LAYER=new Integer(3),LOWER_LAYER=new Integer(1),BACKGROUND_LAYER=new Integer(0);
	
	public QuizDisplayRegulator(PrelimInfo t,Dimension full,int division){
		names=t.getDisplayName();
		Rectangle fullR=new Rectangle(new Point(0,0),full);
		
		Color redC=Color.red;
		Color greenC=Color.green;
		int pause=55;
		float startBlur=0.0f,stopBlur=0.4f,diffBlur=0.01f;
		ImageIcon backII=new ImageIcon(createImageFromColor(Color.black,full));
		try {
			ResourceTools.Resources res=ResourceTools.Resources.createInstance(new java.io.File(DATA_FILE_PATH),"-");
			int r=(Integer)res.get("answer red red");
			int g=(Integer)res.get("answer red green");
			int b=(Integer)res.get("answer red blue");
			redC=new Color(r,g,b);
			
			r=(Integer)res.get("answer green red");
			g=(Integer)res.get("answer green green");
			b=(Integer)res.get("answer green blue");
			greenC=new Color(r,g,b);
			
			pause=(Integer)res.get("answer pause");
			startBlur=(Float)res.get("answer start blur");
			stopBlur=(Float)res.get("answer stop blur");
			diffBlur=(Float)res.get("answer diff blur");
			
			Float transp=(Float)res.get("transparency");
			
			BufferedImage bi=javax.imageio.ImageIO.read(new java.io.File((String)res.get("question")));
			if (!(new Dimension(bi.getWidth(),bi.getHeight()).equals(full)))
				bi=resize(bi,full);
			bi=loadTranslucentImage(bi,0.5f);
			questionII=new ImageIcon(bi);
			
			bi=javax.imageio.ImageIO.read(new java.io.File((String)res.get("info")));
			if (!(new Dimension(bi.getWidth(),bi.getHeight()).equals(full)))
				bi=resize(bi,full);
			bi=loadTranslucentImage(bi,0.5f);
			infoII=new ImageIcon(bi);
			
			bi=javax.imageio.ImageIO.read(new java.io.File((String)res.get("result")));
			if (!(new Dimension(bi.getWidth(),bi.getHeight()).equals(full)))
				bi=resize(bi,full);
			bi=loadTranslucentImage(bi,0.5f);
			resultII=new ImageIcon(bi);
			
			r=(Integer)res.get("back red");
			g=(Integer)res.get("back green");
			b=(Integer)res.get("back blue");
			backII=new ImageIcon(createImageFromColor(new Color(r,g,b),full));
						
		} catch (Exception e){
			System.err.println ("Error loading from file :"+DATA_FILE_PATH);
			e.printStackTrace();
		}
		
		this.setLayout(new GridLayout(1,1));
		layered=new JLayeredPane();
		layered.setLayout(null);
		
		JPanel back=createGUI();
		back.setOpaque(false);
		back.setBounds(fullR);
		layered.add(back,MIDDLE_LAYER);
		
		imageL=new JLabel();
		imageAnimation=new ImageAnime(pause,createImageFromColor(greenC,full),createImageFromColor(redC,full),
				imageL,startBlur,stopBlur,diffBlur);
		imageL.setBounds(fullR);
		layered.add(imageL,LOWER_LAYER);
		
		JLabel backL=new JLabel(backII);
		backL.setBounds(fullR);
		layered.add(backL,BACKGROUND_LAYER);
		
		this.add(layered);
		
		displayTimedOut("Waiting for response from SERVER");
		
		setRoundNumber(0);
		setPoints(0);
		setQuestionNumber(0);
	}
	
	private JPanel createGUI(){
		JPanel retP=new JPanel();
		retP.setOpaque(false);
		retP.setLayout(new BorderLayout());
		
		panel=new JPanel(cardLO=new CardLayout());
		panel.setOpaque(false);
		fullQuestionPanel=new FullQuestionPanel();
		timedOutPanel=new TimedOutPanel();
		timedOutPanel.setOpaque(false);
		questionOnlyPanel=new QuestionOnlyPanel();
		answerDisplayPanel=new AnswerDisplayPanel();
		answerReceivedPanel=new AnswerReceivedPanel();
		quizCompletePanel=new QuizCompletePanel();
		panel.add( fullQuestionPanel  ,FULL_QUESTION);
		panel.add( timedOutPanel 	  ,TIMED_OUT);
		panel.add( questionOnlyPanel  ,QUESTION_ONLY);
		panel.add( answerDisplayPanel ,ANSWER_DISPLAY);
		panel.add( answerReceivedPanel,ANSWER_RECEIVED);
		panel.add( quizCompletePanel  ,QUIZ_COMPLETE);
		
		JPanel northP=createNorthPanel();
		retP.add(northP,BorderLayout.NORTH);
		retP.add(panel,BorderLayout.CENTER);
		return retP;
	}
	
	private JPanel createNorthPanel(){
		JPanel retP=new JPanel(new BorderLayout());
		retP.setOpaque(false);
		retP.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));
		
		JLabel helloL=new JLabel("Hello, "+names,SwingConstants.CENTER);
		Font f=helloL.getFont();
		f=new Font(f.getName(),Font.BOLD,16);
		helloL.setFont(f);
		
		questionsAttemptedL=new JLabel("");
		roundsClearedL=new JLabel ("");
		pointsL=new JLabel("");
		pointsAnimation=new PointsAnime(pointsL);
		
		retP.add(helloL,BorderLayout.WEST);
		{	JPanel temp=new JPanel (new GridLayout(3,1));
			temp.setOpaque(false);
			temp.add(pointsL);
			temp.add(roundsClearedL);
			temp.add(questionsAttemptedL);
			retP.add(temp,BorderLayout.EAST);
		}
		
		return retP;
	}	
	
	public void setPoints(int value){
		pointsL.setText("Your score :  "+value);
		new Thread(pointsAnimation).start();
	}
	
	public void setMaxRoundNumber(int value){
		totalRounds=value;
		setRoundNumber(0);
	}
	
	public void setMaxQuestionNumber(int value){
		totalQuestions=value;
		setQuestionNumber(0);
	}
	
	public void setRoundNumber(int value){
		roundsClearedL.setText("Round :  "+value+"/"+totalRounds);
	}
	
	public void setQuestionNumber(int value){
		questionsAttemptedL.setText("Questions :  "+value+"/"+totalQuestions);
	}
	
	public void displayTimedOut(String str){
		timedOutPanel.setMessage(str);
		imageL.setIcon(infoII);
		layered.setLayer(imageL,LOWER_LAYER);
		cardLO.show(panel,TIMED_OUT);
	}
	
	public void displayAnswer(boolean isCorrect,String answer,String ansDetails){
		answerDisplayPanel.displayAnswer(isCorrect,answer,ansDetails);
		layered.setLayer(imageL,UPPER_LAYER);
		imageAnimation.setCorrectOrWrong(isCorrect);
		new Thread(imageAnimation).start();
		cardLO.show(panel,ANSWER_DISPLAY);
	}
	
	public void answerReceived(String qt,String answer,String answerDetails){
		answerReceivedPanel.displayAnswer(qt,answer,answerDetails);
		imageL.setIcon(infoII);
		layered.setLayer(imageL,LOWER_LAYER);
		cardLO.show(panel,ANSWER_RECEIVED);
	}
	
	public void displayQuestionOnly(AbstractQuestion str){
		questionOnlyPanel.showQuestion(str);
		layered.setLayer(imageL,LOWER_LAYER);
		imageL.setIcon(questionII);
		cardLO.show(panel,QUESTION_ONLY);
	}
	
	public void displayFullQuestion(AbstractQuestion quest){
		fullQuestionPanel.showQuestion(quest);
		layered.setLayer(imageL,LOWER_LAYER);
		imageL.setIcon(questionII);
		cardLO.show(panel,FULL_QUESTION);
	}
	
	public void displayResult(TeamReport tr){
		quizCompletePanel.refresh(tr);
		imageL.setIcon(resultII);
		layered.setLayer(imageL,LOWER_LAYER);
		cardLO.show(panel,QUIZ_COMPLETE);
	}
	
	private BufferedImage createImageFromColor(Color c,Dimension d){
		int w=(int)d.getWidth();
		int h=(int)d.getHeight();
		BufferedImage image = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g=image.createGraphics();
		g.setColor(c);
		g.fillRect(0,0,w,h);
		g.dispose();
		return image;
	}
	
	public static BufferedImage loadTranslucentImage(BufferedImage loaded, float transperancy) {  
        BufferedImage aimg = new BufferedImage(loaded.getWidth(), loaded.getHeight(), BufferedImage.TRANSLUCENT);  
        Graphics2D g = aimg.createGraphics();  
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));  
        g.drawImage(loaded, null, 0, 0);  
        g.dispose();  
        return aimg;  
    }
    
    private static BufferedImage resize(BufferedImage image, Dimension d){
      int width=(int)d.getWidth();
      int height=(int)d.getHeight();
      int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
      BufferedImage resizedImage = new BufferedImage(width, height, type);
      Graphics2D g = resizedImage.createGraphics();
      g.setComposite(AlphaComposite.Src);
 
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
 
      g.setRenderingHint(RenderingHints.KEY_RENDERING,
      RenderingHints.VALUE_RENDER_QUALITY);
 
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
 
      g.drawImage(image, 0, 0, width, height, null);
      g.dispose();
      return resizedImage;
	}
	
	private static class PointsAnime implements Runnable {
		private JLabel label;
		
		public PointsAnime(JLabel label){
			this.label=label;
		}
		
		public void run(){
			int increased=16;
			int original=original=label.getFont().getSize();
			Font font=label.getFont();
			while(increased>=original){
				Font f=label.getFont();
				f=new Font(f.getName(),Font.BOLD,increased);
				label.setFont(f);
				try {
					Thread.sleep(500);
				} catch (Exception e){ }
			}
			label.setFont(font);
		}
	}
	
	private static class ImageAnime extends SwingWorker<Void,Void>{
    	private int pause;
    	private BufferedImage correctI,wrongI;
    	private boolean which;
    	private JLabel label;
    	private float lower,upper,diff;
    	
    	public ImageAnime(int pause,BufferedImage correctI,BufferedImage wrongI,JLabel label,float lower,float upper,float diff){
    		this.pause=pause;
    		this.correctI=correctI;
    		this.wrongI=wrongI;
    		this.label=label;
    		this.lower=lower;
    		this.upper=upper;
    		this.diff=diff;
    		which = false;
    	}
    	
    	public void setCorrectOrWrong(boolean z){
    		which=z;
    	}
    	
    	public Void doInBackground(){
    		float value=lower;
    		float dif=diff;
    		
    		BufferedImage image=which?correctI:wrongI;
    		
    		while(true){
    			BufferedImage img=loadTranslucentImage(image,value);
    			label.setIcon(new ImageIcon(img));
    			//label.repaint();
    			value+=dif;
    			if (value>upper)break;
    			try {
    				Thread.sleep(pause);
    			} catch (Exception e){ }
    		}
    		return null;
    	}
    }
}