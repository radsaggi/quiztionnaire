package Client.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.*;

import Structure.*;
import Structure.Interfaces.AbstractQuestion;

public class QuestionOnlyPanel extends JPanel {
	private JTextPane questionTP;
	private StyledDocument doc;
	
	private int documentRemoveFrom;
	
	public QuestionOnlyPanel(){
		setOpaque(false);
		setLayout(new BorderLayout());	
		add(createCenterPanel(),BorderLayout.CENTER);
	}
	
	private JPanel createCenterPanel(){
		JPanel retP=new JPanel(new GridLayout(2,1));
		retP.setOpaque(false);
		
		retP.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(20,20,20,20),
			BorderFactory.createLoweredSoftBevelBorder()));
		
		
		questionTP = new JTextPane();
		questionTP.setEditable(false);
		doc=questionTP.getStyledDocument();
		
		//initialise the stylising
		Style def=StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setFontFamily(def,"SansSerif");
		
		Style s=doc.addStyle("regular",def);
		StyleConstants.setFontSize(s,16);
		StyleConstants.setFirstLineIndent(s,100.0f);
		
		s=doc.addStyle("question",def);
		StyleConstants.setAlignment(s,StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(s,true);
		StyleConstants.setFontSize(s,26);
		StyleConstants.setSpaceBelow(s,5.0f);
		
		try {
			doc.insertString(doc.getLength(),"Question\n",doc.getStyle("question"));
		} catch (Exception e){ }
		documentRemoveFrom=doc.getLength();
		
		{	JPanel temp=new JPanel(new BorderLayout());
			temp.setOpaque(false);
			temp.add(new JScrollPane(questionTP),BorderLayout.CENTER);
			temp.add(new JSeparator(JSeparator.HORIZONTAL),BorderLayout.SOUTH);
			retP.add(temp);
		}
		JLabel label=new JLabel("This question is being passed. You will be asked to answer only upon your turn!",SwingConstants.CENTER);
		Font f=label.getFont();
		f=new Font(f.getName(),f.getStyle(),16);
		label.setFont(f);
		retP.add(label);
		return retP;
	}
	
	public void showQuestion(AbstractQuestion quest){
		try {
			doc.remove(documentRemoveFrom,doc.getLength()-documentRemoveFrom);
		} catch (Exception e){ }
		try {
			doc.insertString(doc.getLength(),(String)quest.getQuestion(),doc.getStyle("regular"));
		} catch (Exception e){ }
		
	}
}