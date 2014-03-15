package Client.Debug;

import java.awt.BorderLayout;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.*;

public class ErrorOutputPanel extends JPanel {
	private static class RedirectedOutputStream extends OutputStream{
		private JTextArea ta;
		
		public RedirectedOutputStream (JTextArea ta){
			this.ta=ta;
		}
		
		public void write (int n){
			ta.append((char)n+"");
		}
	}
	private static JTextArea ta;
	static {
		ta=new JTextArea();
		System.setErr(new PrintStream(new RedirectedOutputStream(ta)));
	}
	
	public ErrorOutputPanel (){
		super();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Error Stream:"));
		
		ta.setFocusable(false);
		ta.setEditable(false);
		JScrollPane sp=new JScrollPane(ta);
		this.add(sp,BorderLayout.CENTER);		
	}
}