package Client.GUI;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimedOutPanel extends JPanel {
	private JLabel waitL,messageL;
	
	public TimedOutPanel(){
		setOpaque(false);
		setLayout(new GridLayout(2,1));
		setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(20,20,20,20),
			BorderFactory.createLineBorder(Color.white)));
		waitL=new JLabel("You are requested to wait",javax.swing.SwingConstants.CENTER);
		Font f=waitL.getFont();
		f=new Font(f.getName(),Font.BOLD,42);
		waitL.setFont(f);
		messageL=new JLabel("",javax.swing.SwingConstants.CENTER);
		f=messageL.getFont();
		f=new Font(f.getName(),f.getStyle(),26);
		messageL.setFont(f);
		
		this.add(waitL);
		{	JPanel panel=new JPanel(new FlowLayout(FlowLayout.CENTER));
			panel.setOpaque(false);
			panel.add(messageL);
			this.add(panel);
		}
	}
	
	public void setMessage(String message){
		messageL.setText(message);
	}
}