package creatures.omi.mainpanel.brainpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import creatures.model.Brain;
import utils.Matrix;

@SuppressWarnings("serial")
public class BrainPanelView extends JPanel{
	
	public final int XSIZE = 200;
	public final int YSIZE = 200;
	
	protected Brain drawnBrain = null;
	
	public BrainPanelView() {
		super();
		this.setSize(new Dimension(XSIZE, YSIZE));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		
		g.setColor(Color.white);
		g.fillRect(0, 0, XSIZE, YSIZE);
		
		if(drawnBrain == null)return;
		
		g.setColor(Color.black);

		ArrayList<Matrix> states = drawnBrain.getStates();
		//ArrayList<Matrix> weights = drawnBrain.getWeights();
		
		for(int mi = 0; mi < states.size(); mi++)
		{
			Matrix m = states.get(mi);
			for(int xi = 0; xi < m.getSize()[1]; xi++)
			{
				int xCenter = (xi+1) * XSIZE / (m.getSize()[1] +1);
				int yCenter = (mi+1) * YSIZE / (states.size() +1);
				if(m.get(0, xi) > 0)
				{
					g.setColor(Color.black);
				}
				else
				{
					g.setColor(Color.red);
				}

				int diameter = 5 + (int)(Math.abs(20 * m.get(0, xi)));
				
				g.drawOval(xCenter - diameter/2, yCenter - diameter/2, diameter, diameter);
			}			
		}
		
		g.setColor(Color.RED);
		g.drawRect(0, 0, XSIZE, YSIZE);
	}
}
