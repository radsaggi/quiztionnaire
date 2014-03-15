package Client.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
//import javax.swing.text.*;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.*;

import Structure.*;
import Structure.Interfaces.AbstractQuestion;

import ResourceTools.*;

public class FullQuestionPanel extends JPanel implements ActionListener{
	//private JTextPane questionTP;
	//private StyledDocument doc;
	private JXPanel questionPanel;
	private TimerThread tt;
	private AnswerPanel answerPanel;
	
	private int documentRemoveFrom;
	public FullQuestionPanel(){
		
		setLayout(new BorderLayout());
		setOpaque(false);
		add(createCenterPanel(),BorderLayout.CENTER);
		add(createSouthPanel(),BorderLayout.SOUTH);
	}
	
	private JPanel createSouthPanel(){
		JPanel retP=new JPanel (new BorderLayout());
		retP.setOpaque(false);
		retP.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));
		
		JLabel timerL=new JLabel("",SwingConstants.CENTER);
		tt=new TimerThread(timerL);
		
		JButton acceptB=new JButton("Accept");
		acceptB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		acceptB.addActionListener(this);
		
		retP.add(timerL,BorderLayout.WEST);
		{	JPanel temp=new JPanel(new FlowLayout(FlowLayout.CENTER));
			temp.setOpaque(false);
			temp.add(acceptB);
			retP.add(temp,BorderLayout.EAST);
		}
		
		return retP;
	}
	
	private JPanel createCenterPanel(){
		JPanel retP=new JPanel(new GridLayout(2,1));
		retP.setOpaque(false);
		
		retP.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(20,20,20,20),
			BorderFactory.createLoweredSoftBevelBorder()));
		
		/*questionTP = new JTextPane();
		questionTP.setEditable(false);
		doc=questionTP.getStyledDocument();
		
		//initialise the stylising
		Style def=StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setFontFamily(def,"SansSerif");
		
		Style s=doc.addStyle("regular",def);
		StyleConstants.setFirstLineIndent(s,100.0f);
		StyleConstants.setFontSize(s,16);
		
		s=doc.addStyle("question",def);
		StyleConstants.setAlignment(s,StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(s,true);
		StyleConstants.setFontSize(s,28);
		StyleConstants.setSpaceBelow(s,5.0f);
		
		try {
			doc.insertString(doc.getLength(),"Question\n",doc.getStyle("question"));
		} catch (Exception e){ }
		documentRemoveFrom=doc.getLength();*/
		
		JLabel label=new JLabel("QUESTION");
		label.setForeground(Color.black);
		Font f=label.getFont();
		f=f.deriveFont(28f);
		label.setFont(f);
		label.setHorizontalAlignment(SwingUtilities.LEFT);
		
		questionPanel=new JXPanel();
		questionPanel.setOpaque(false);
		AlphaPainter ap=new AlphaPainter();
		ap.setAlpha(0.5f);
		ap.setPainters(new MattePainter(Color.white));
		questionPanel.setBackgroundPainter(ap);
		
		{	JPanel temp=new JPanel(new BorderLayout());
			temp.setOpaque(false);
			temp.setBorder(BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
			temp.add(questionPanel,BorderLayout.CENTER);
			temp.add(label,BorderLayout.NORTH);
			retP.add(temp);
		}
		retP.add(answerPanel=new AnswerPanel(this,null));
		return retP;
	}
	
	public void showQuestion(AbstractQuestion quest){
		//assuming that the question is a string
		/*try {
			doc.remove(documentRemoveFrom,doc.getLength()-documentRemoveFrom);
		} catch (Exception e){ }
		try {
			doc.insertString(doc.getLength(),(String)quest.getQuestion(),doc.getStyle("regular"));
		} catch (Exception e){ }*/
		
		questionPanel.removeAll();
		
		Object o=quest.getQuestion();
		if (o instanceof Component)
			questionPanel.add((Component)o);
		else {
			String str=o.toString();
			String[] arr=str.split("\n");
			for (String s:arr){
				JLabel  label=new JLabel(s);
				Font f=label.getFont();
				f=f.deriveFont(16f);
				label.setFont(f);
				questionPanel.add(label);
			}
		}
		
		answerPanel.setQuestion(quest);
		
		//Redo the timer
		//tt.initTimer(ansType.getTimerValue());
		//tt.start();
	}
	
	public void actionPerformed(ActionEvent ae){
		System.out.println ("Answer received : " + answerPanel.getAnswer());
		String answer=answerPanel.getAnswer();
		MainFrame.getInstance().getCouple().getSender().sendAnswer(answer);
	}
	
	private static class TimerThread extends SwingWorker<Void,Void>{
		private JLabel modifyL;
		private int givenTime;
		
		public TimerThread (JLabel l){
			modifyL=l;
		}
		
		public void initTimer(int value){
			givenTime=value;
		}
		
		public void start()throws IllegalStateException{
			System.out.println ("Starting");
			if (!cancel(true))
				throw new IllegalStateException();
			
			execute();
		}
		
		public boolean stop(){
			return cancel(true);
		}
		
		public Void doInBackground(){
			int counter=givenTime;
			while (counter!=-1){
				modifyL.setText("Timer : "+counter--);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e){ 
					return null;
				}
			}
			return null;
		}
		
		public void done(){
			System.out.println ("Timed Out");
		}
	}	
	
	private static class AnswerPanel extends JPanel{
		private CardLayout cardLO;
		private String visible;
		private JPanel oneWordP,multipleChoiceP;		
		private JRadioButton buttons[];
		private ButtonGroup bg;
		private JTextField inputTF;
		private ActionListener listener;
		
		private static final String ONE_WORD="OneWord",MULTIPLE_CHOICE="MultipleChoice";
		
		public AnswerPanel(ActionListener listener,AbstractQuestion aq){
			super();
			setOpaque(false);
			setBorder(BorderFactory.createTitledBorder("Your answer here"));
			setLayout(cardLO = new CardLayout());
			visible=null;
			this.listener=listener;
			initOneWordAnswerPanel();
			initMultipleChoiceAnswerPanel(aq);
			this.add(oneWordP,ONE_WORD);
			this.add(multipleChoiceP,MULTIPLE_CHOICE);
		}
		
		private void initOneWordAnswerPanel(){
			if (oneWordP==null)		oneWordP=new JPanel(new FlowLayout(FlowLayout.CENTER));
			oneWordP.setOpaque(false);
			oneWordP.removeAll();
			oneWordP.add(inputTF=new JTextField(30));
			inputTF.addActionListener(listener);
		}
		
		private void initMultipleChoiceAnswerPanel(AbstractQuestion ans){
			if (multipleChoiceP==null) multipleChoiceP=new JPanel();
			multipleChoiceP.setOpaque(false);
			multipleChoiceP.removeAll();
			buttons=null;
			bg=null;
			System.gc();
			try {
				int n=ans.getOptions().length;
				multipleChoiceP.setLayout(new GridLayout((n+1)/2,2));
				buttons=new JRadioButton[n];
				bg=new ButtonGroup();
				
				for (int i=0;i<n;i++){
					buttons[i]=new JRadioButton(ans.getOptions()[i]);
					buttons[i].setActionCommand(ans.getOptions()[i]);
					bg.add(buttons[i]);
					multipleChoiceP.add(buttons[i]);
				}
				buttons[0].setSelected(true);
			} catch(ClassCastException e){
				return;				
			} catch (NullPointerException e){
				return;
			}		
		}
		
		public void setQuestion(AbstractQuestion ans){
			initOneWordAnswerPanel();
			initMultipleChoiceAnswerPanel(ans);
			visible=ans.areOptionsAvailable()?MULTIPLE_CHOICE:ONE_WORD;
			System.out.println ("Visible : "+visible);
			cardLO.show(this,visible);
		}
		
		public String getAnswer(){
			if (visible.equals(ONE_WORD))
				return inputTF.getText();
			else 
				return bg.getSelection().getActionCommand();
		}
	}
	
	public static void main (String[] args) {
   	  try {
					/*for (UIManager.LookAndFeelInfo i:UIManager.getInstalledLookAndFeels()){
						if (i.toString().toLowerCase().contains("nimbus")){
							System.out.println ("Setting Nimbus LNF");
							UIManager.setLookAndFeel(i.getClassName());
							break;
						}
					}*/
				} catch (Exception e){
					System.err.println ("Nimbus LNF not found. Using System or default");
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception ex) { }
				}
				
				JFrame frame=new JFrame();
				frame.setLayout(new BorderLayout());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				FullQuestionPanel adp=new FullQuestionPanel();
				adp.showQuestion(new Structure.Implementations.NormalQuestion("How are you??",555));
				frame.add(adp,BorderLayout.CENTER);
				//createFullScreen(frame);
				frame.setSize(1024,768);
				frame.setVisible(true);
   }
}