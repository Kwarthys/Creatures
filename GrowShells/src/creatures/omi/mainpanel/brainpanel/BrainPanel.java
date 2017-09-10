package creatures.omi.mainpanel.brainpanel;

import javax.swing.JPanel;

import creatures.model.Brain;

public interface BrainPanel {
	public JPanel getView();
	public void setBrainToDraw(Brain brain);
	public void repaint();
}
