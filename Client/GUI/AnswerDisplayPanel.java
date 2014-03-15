package Client.GUI;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.*;

import ResourceTools.*;

public class AnswerDisplayPanel extends JXPanel {
	private JLabel imageL, answerL;
	private JTextArea detailsTA;
	private ImageIcon correctImage,wrongImage;
	
	private org.jdesktop.swingx.painter.Painter correct,wrong;
	
	private static final  String DATA_FILE_LOCATION="graphDetails\\client\\answerGUI.dat";
		
	public AnswerDisplayPanel(){
		
		File f=new File(DATA_FILE_LOCATION);
		correctImage=new ImageIcon();
		wrongImage=new ImageIcon();
		try {
			Resources res=Resources.createInstance(f,"-");
			
			int size=(Integer)res.get("size");
			
			String correctPath=(String)res.get("correct");
			BufferedImage correctBI=javax.imageio.ImageIO.read(new File(correctPath));
			correctBI=resize(correctBI,size,size);
			correctImage=new ImageIcon(correctBI);
			
			String wrongPath=(String)res.get("wrong");
			BufferedImage wrongBI=javax.imageio.ImageIO.read(new File(wrongPath));
			wrongBI=resize(wrongBI,size,size);
			wrongImage=new ImageIcon(wrongBI);
			
		} catch (Exception e){
			System.err.println ("Exception while loading resources from: "+f.getAbsolutePath());
		}
		
		setOpaque(true);
		
		Color green1=new Color(153,225,100),green2=new Color(91,172,32),
					red1=new Color(252,45,7),red2=new Color(253,78,47);
		
		MattePainter mp=new MattePainter(green1);		
		PinstripePainter pp=new PinstripePainter(green2,0,8,8);;
		correct=new CompoundPainter(mp,pp,new GlossPainter());
		
		mp=new MattePainter(red1);		
		pp=new PinstripePainter(red2,0,8,8);;
		wrong=new CompoundPainter(mp,pp,new GlossPainter());
		
		setLayout(new BorderLayout());
		
		add(createNorthPanel(),BorderLayout.NORTH);
		add(createCenterPanel(),BorderLayout.CENTER);
	}
	
	private JPanel createNorthPanel(){
		JPanel retP=new JPanel(new BorderLayout());	
		retP.setOpaque(false);
		imageL=new JLabel();
		imageL.setHorizontalAlignment(SwingConstants.CENTER);
		imageL.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		answerL=new JLabel("",SwingConstants.CENTER);
		Font font=answerL.getFont();
		font=new Font(font.getName(),Font.BOLD,26);
		answerL.setFont(font);
		retP.add(imageL,BorderLayout.CENTER);
		retP.add(answerL,BorderLayout.SOUTH);
		return retP;
	}
	
	private JPanel createCenterPanel(){
		JPanel retP=new JPanel(new BorderLayout());
		retP.setOpaque(false);
		retP.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(20,20,20,20),
				BorderFactory.createLineBorder(Color.black,2,true)));
		
		JXPanel p=new JXPanel(new BorderLayout());
		p.setOpaque(false);
		AlphaPainter ap=new AlphaPainter();
		ap.setAlpha(0.5f);
		ap.setPainters(new MattePainter(Color.white));
		p.setBackgroundPainter(ap);
		
		detailsTA=new JTextArea();
		detailsTA.setOpaque(false);
		Font font=detailsTA.getFont();
		font=font.deriveFont(14f);
		detailsTA.setFont(font);
		detailsTA.setForeground(Color.black);
		detailsTA.setEditable(false);
		
		p.add(detailsTA,BorderLayout.CENTER);
		retP.add(p,BorderLayout.CENTER);
		
		return retP;
	}
	
	public void displayAnswer(boolean isCorrect,String correctAnswer,String answerDetails){
		imageL.setIcon(isCorrect?correctImage:wrongImage);
		setBackgroundPainter(isCorrect?correct:wrong);
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
				AnswerDisplayPanel adp=new AnswerDisplayPanel();
				adp.displayAnswer(false,"Ashutosh","Ashutosh is a hero!!");
				frame.add(adp,BorderLayout.CENTER);
				//createFullScreen(frame);
				frame.setSize(1024,768);
				frame.setVisible(true);
   }
}