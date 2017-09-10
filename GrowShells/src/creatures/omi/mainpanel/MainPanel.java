package creatures.omi.mainpanel;

import javax.swing.JPanel;

public interface MainPanel {
	public JPanel getView();
	public void save();
	
	public void playRound();
	public void openBrainView();
	public void repaint();
}
