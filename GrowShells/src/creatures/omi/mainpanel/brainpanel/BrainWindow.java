package creatures.omi.mainpanel.brainpanel;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import creatures.model.Brain;

@SuppressWarnings("serial")
public class BrainWindow extends JFrame{
	
	protected BrainPanel brainCtrl;

	public BrainWindow() {		
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		brainCtrl = new BrainPanelCtrl();
		
		JPanel center = brainCtrl.getView();
		center.setLayout(new BorderLayout());
		
		frame.setContentPane(center);
		frame.setVisible(true);
		frame.setSize(250, 250);
	}
	
	public void setBrainToDraw(final Brain toDraw)
	{
		brainCtrl.setBrainToDraw(toDraw);
	}

	public void repaintContent() 
	{
		brainCtrl.repaint();
	}
}
