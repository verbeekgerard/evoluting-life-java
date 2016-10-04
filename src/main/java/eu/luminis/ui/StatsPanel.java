package eu.luminis.ui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.luminis.general.Options;

public class StatsPanel extends JPanel implements ChangeListener {
	
	private static final long serialVersionUID = 1L;

	static final int FPS_MIN = 0;
    static final int FPS_MAX = 100;
    static final int FPS_INIT = (int) Options.mainLoopSleep.get(); 
	
	public StatsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JFrame frame = new JFrame("Evoluting-life-java");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	    frame.add(this);
	    frame.setSize(300, 110);
	    frame.setVisible(true);
	    frame.setResizable(false);
	    
	 	//Create the label.
        JLabel sliderLabel = new JLabel("Delay milliseconds", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        //Create the slider.
        JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);
        
        framesPerSecond.addChangeListener(this);
        
        //Turn on labels at major tick marks.
        framesPerSecond.setMajorTickSpacing(10);
        framesPerSecond.setMinorTickSpacing(1);
        framesPerSecond.setPaintTicks(true);
        framesPerSecond.setPaintLabels(true);
        framesPerSecond.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        Font font = new Font("Serif", Font.ITALIC, 15);
        framesPerSecond.setFont(font);
 
        //Put everything together.
        add(sliderLabel);
        add(framesPerSecond);

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        
//        JLabel label = new JLabel("Best: ", JLabel.LEFT);
//        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        sliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        add(label);
        
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            double sleep = source.getValue();
            Options.mainLoopSleep.set(sleep);
        }
	}
	
}
