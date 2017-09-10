package creatures.omi;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import creatures.omi.mainpanel.MainPanel;
import creatures.omi.mainpanel.MainPanelCtrl;

@SuppressWarnings("serial")
public class PoolFenetre extends JFrame
{
	private JPanel container = new JPanel();
	private MainPanel sim;
	
	private int speedUp = 0;
	
	
	public PoolFenetre()
	{
		setVisible(true);		
		setTitle("Shells");
		
		setLocationRelativeTo(null);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int choice = 0;
		
		String[] userChoice = {"from scratch", "Resume previous run", "Load intelligent ones"};
		choice = JOptionPane.showOptionDialog(null, "Start Condition","Shells Launcher", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, userChoice,userChoice[2]);
		System.out.println(choice);
		
		container.setLayout(new BorderLayout());
		sim = new MainPanelCtrl(choice);
		container.add(sim.getView(), BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu simMenu = new JMenu("Simulation");
		
		JMenu windowMenu = new JMenu("Window");
		
		JMenuItem brainWDButton = new JMenuItem("Open Brain View");		
		brainWDButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("AHA");
				sim.openBrainView();
			}
		});
		
		JRadioButtonMenuItem speedUpMenuItem1 = new JRadioButtonMenuItem("SpeedUp x1");
		speedUpMenuItem1.setSelected(true);
		speedUpMenuItem1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(speedUpMenuItem1.isSelected()) speedUp = 0;
			}
		});
		
		JRadioButtonMenuItem speedUpMenuItem100 = new JRadioButtonMenuItem("SpeedUp x5");
		speedUpMenuItem100.setSelected(false);
		speedUpMenuItem100.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(speedUpMenuItem100.isSelected()) speedUp = 5;
			}
		});
		
		JRadioButtonMenuItem speedUpMenuItem1000 = new JRadioButtonMenuItem("SpeedUp x10");
		speedUpMenuItem1000.setSelected(false);
		speedUpMenuItem1000.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(speedUpMenuItem1000.isSelected()) speedUp = 10;
			}
		});
		
		JRadioButtonMenuItem speedUpMenuItem10000 = new JRadioButtonMenuItem("SpeedUp x100");
		speedUpMenuItem10000.setSelected(false);
		speedUpMenuItem10000.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(speedUpMenuItem10000.isSelected()) speedUp = 100;
			}
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(speedUpMenuItem1);
		group.add(speedUpMenuItem100);
		group.add(speedUpMenuItem1000);
		group.add(speedUpMenuItem10000);

		menuBar.add(simMenu);
		simMenu.add(speedUpMenuItem1);
		simMenu.add(speedUpMenuItem100);
		simMenu.add(speedUpMenuItem1000);
		simMenu.add(speedUpMenuItem10000);
		
		menuBar.add(windowMenu);
		windowMenu.add(brainWDButton);
		
		this.setJMenuBar(menuBar);
		
		setSize(1000,600);

		this.setContentPane(container);
		
		addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		         sim.save();
		         System.out.println("saved");
		    }
		});
		
		setVisible(true);
		this.setLocationRelativeTo(null);		
		
		System.out.println("lancement !");
		go();
	}
	
	private void go()
	{
		int index = 0;
		while(true)
		{
			sim.playRound();
			
			if(speedUp == 0)
			{
				sim.repaint();
			
				try{
					Thread.sleep(5);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			else
			{
				if(index++ > speedUp)
				{
					sim.repaint();
					index = 0;
				}
			}
		}
	}
}
