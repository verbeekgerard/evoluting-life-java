package eu.luminis.ui;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import eu.luminis.general.Event;
import eu.luminis.general.EventType;
import eu.luminis.general.General;
import eu.luminis.general.Options;

public class StatsPanel extends JPanel implements ChangeListener, Observer, ActionListener {
	
	private static final long serialVersionUID = 1L;

	private static final int FPS_MIN = 0;
	private static final int FPS_MAX = 100;
    private static final int FPS_INIT = (int) Options.mainLoopSleep.get(); 
    private static final String FILE_EXTENSION = "json";
    
    private StatsCollector statsCollector;
    private JLabel bestLbl;
    private General general;
    private JFileChooser fileChooser;
    private JButton exportBtn;
    private JButton importBtn;
    
	public StatsPanel(StatsCollector statsCollector, General general) {
		this.statsCollector = statsCollector;
		this.general = general;
		
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("*."+FILE_EXTENSION, FILE_EXTENSION));
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JFrame frame = new JFrame("Evoluting-life-java");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	    frame.add(this);
	    frame.setSize(300, 500);
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
        
        bestLbl = new JLabel("Best: ", JLabel.LEFT);
        add(bestLbl);
        
        exportBtn = new JButton("Export");
        exportBtn.addActionListener(this);
        add(exportBtn);
        
        importBtn = new JButton("Import");
        importBtn.addActionListener(this);
        add(importBtn);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
		if (event.type.equals(EventType.CYCLE_END)) {
			if (statsCollector.getStats() != null) {
				bestLbl.setText("Best: " + statsCollector.getStats().getAverageBestFitness());
			}
		}
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            double sleep = source.getValue();
            Options.mainLoopSleep.set(sleep);
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(importBtn)) {

			int returnVal = fileChooser.showOpenDialog(StatsPanel.this);
			 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                doImport(file);
            }
			
		}
		else if (e.getSource().equals(exportBtn)) {
			int returnVal = fileChooser.showSaveDialog(StatsPanel.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(FILE_EXTENSION)) {
                    file = new File(file.toString() + "." + FILE_EXTENSION);
                }
                doExport(file);
            }
		}
	}
	
	private void doImport(File file) {
		try {
			String json = new String(readAllBytes(get(file.getPath())));
			general.importPopulation(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void doExport(File file) {
		String json = general.exportPopulation();
		PrintWriter out = null;
		try {
			out = new PrintWriter(file);
			out.println(json);
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
}
