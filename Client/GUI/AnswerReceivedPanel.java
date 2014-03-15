package Client.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

import ResourceTools.*;

public class AnswerReceivedPanel extends JPanel {
	private JLabel imageL, answerL, receivedL;
	private JTextArea detailsTA;
	
	private static final  String DATA_FILE_LOCATION="graphDetails\\client\\answerGUI.dat";
		
	public AnswerReceivedPanel(){
		setOpaque(false);
		setLayout(new BorderLayout());
		
		add(createNorthPanel(),BorderLayout.NORTH);
		add(createCenterPanel(),BorderLayout.CENTER);
	}
	
	private JPanel createNorthPanel(){
		JPanel retP=new JPanel(new BorderLayout());
		retP.setOpaque(false);
		
		File f=new File(DATA_FILE_LOCATION);
		ImageIcon ii=new ImageIcon();
		try {
			Resources r=Resources.createInstance(f,"-");
			
			int size=(Integer)r.get("size");
			
			String correctPath=(String)r.get("info");
			BufferedImage correctBI=javax.imageio.ImageIO.read(new File(correctPath));
			correctBI=resize(correctBI,size,size);
			ii=new ImageIcon(correctBI);
		} catch (Exception e){
			System.err.println ("Exception while loading resources from: "+f.getAbsolutePath());
		}
		
		imageL=new JLabel(ii);
		imageL.setHorizontalAlignment(SwingConstants.CENTER);
		imageL.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		receivedL=new JLabel("",SwingConstants.CENTER);
		Font font=receivedL.getFont();
		font=new Font(font.getName(),Font.BOLD,26);
		receivedL.setFont(font);
		
		retP.add(imageL,BorderLayout.CENTER);
		retP.add(receivedL,BorderLayout.SOUTH);
		return retP;
	}
	
	private JPanel createCenterPanel(){
		JPanel retP=new JPanel(new BorderLayout());
		retP.setOpaque(false);
		
		answerL=new JLabel("",SwingConstants.CENTER);
		Font font=answerL.getFont();
		font=new Font(font.getName(),Font.BOLD,30);
		answerL.setFont(font);
		
		detailsTA=new JTextArea();
		font=detailsTA.getFont();
		font=new Font(font.getName(),font.getStyle(),16);
		detailsTA.setFont(font);
		detailsTA.setEditable(false);
		
		JScrollPane sp=new JScrollPane(detailsTA);
		sp.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(20,20,20,20),
				BorderFactory.createRaisedSoftBevelBorder()),
			sp.getBorder()));
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		retP.add(answerL,BorderLayout.NORTH);
		retP.add(sp,BorderLayout.CENTER);
		
		return retP;
	}
	
	public void displayAnswer(String from,String correctAnswer,String answerDetails){
		receivedL.setText("Answer recieved from - "+from);
		answerL.setText(correctAnswer);
		detailsTA.setText(answerDetails);
	}
	
	private static BufferedImage resize(BufferedImage image, int width, int height){
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
				AnswerReceivedPanel adp=new AnswerReceivedPanel();
				adp.displayAnswer("HII","Ashutosh","Ashutosh is great");
				frame.add(adp,BorderLayout.CENTER);
				//createFullScreen(frame);
				frame.setSize(1024,768);
				frame.setVisible(true);
   }
}