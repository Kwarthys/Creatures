package creatures.omi.mainpanel.brainpanel;

import javax.swing.JPanel;

import creatures.model.Brain;

public class BrainPanelCtrl implements BrainPanel{
	
	private BrainPanelView view;
	private BrainPanelModel model;
	
	public BrainPanelCtrl() {
		view = new BrainPanelView();
		model = new BrainPanelModel();
	}

	@Override
	public JPanel getView() {
		return view;
	}

	@Override
	public void setBrainToDraw(final Brain brain) {
		model.drawnBrain = brain;
		view.drawnBrain = brain;
	}

	@Override
	public void repaint() {
		view.repaint();
	}

}
