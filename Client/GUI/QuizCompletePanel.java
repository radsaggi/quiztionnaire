package Client.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import Client.RunTime.ClientSender;
import Structure.TeamReport;

public class QuizCompletePanel extends javax.swing.JPanel {
	private JLabel rankL,pointsL;
	private final String rankPrefix="Your rank is ",pointsPrefix="Your score ";
	
	public QuizCompletePanel(){
		setOpaque(false);
		setLayout(new BorderLayout());
		JPanel subP=new JPanel(new GridLayout(2,1));
		subP.setOpaque(false);
		rankL=new JLabel("");
		rankL.setHorizontalAlignment(SwingConstants.CENTER);
		rankL.setVerticalAlignment(SwingConstants.BOTTOM);
		{	Font f=rankL.getFont();
			f=new Font(f.getName(),Font.BOLD,32);
			rankL.setFont(f);
		}
		pointsL=new JLabel("");
		{	Font f=pointsL.getFont();
			f=new Font(f.getName(),Font.BOLD,20);
			pointsL.setFont(f);
		}
		pointsL.setHorizontalAlignment(SwingConstants.CENTER);
		pointsL.setVerticalAlignment(SwingConstants.TOP);
		
		JButton logoutB=new JButton("Log Out");
		logoutB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				logoutAction();
			}
		});
		
		{	JPanel panel=new JPanel(new FlowLayout(FlowLayout.CENTER));
			panel.setOpaque(false);
			panel.add(logoutB);
			this.add(panel,BorderLayout.SOUTH);
		}
		
		subP.add(rankL);
		subP.add(pointsL);
		this.add(subP,BorderLayout.CENTER);
	}
	
	public void refresh(TeamReport tr){
		rankL.setText(rankPrefix+tr.getRank());
		pointsL.setText(pointsPrefix+tr.getScore());
	}
	
	private void logoutAction(){
		MainFrame.getInstance().getCouple().getSender().logout();
		MainFrame.getInstance().showLoginScreen();
	}
}