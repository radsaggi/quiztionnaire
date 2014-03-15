package Client.Debug;

import javax.swing.*;

import Utilities.JMultiPanelPane;

public class DebugFrame extends JFrame {
	private ErrorOutputPanel err;
	private StandardOutputPanel out;
	
	public DebugFrame(){
		super("Debugger");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setSize(640,480);
		setLocationRelativeTo(null);
		
		JMultiPanelPane pane=new JMultiPanelPane(JMultiPanelPane.NORTH,new JMultiPanelPane.JToggleButtonRenderer());
		pane.addPanel(out=new StandardOutputPanel(),"Standard Output");
		pane.addPanel(err=new ErrorOutputPanel(),"Standard Error");
		
		setLayout(new java.awt.GridLayout(1,1));
		this.add(pane);
	}
}