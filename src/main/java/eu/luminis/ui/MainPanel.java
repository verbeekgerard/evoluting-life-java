package eu.luminis.ui;

import eu.luminis.general.Event;
import eu.luminis.general.EventType;
import eu.luminis.general.General;
import eu.luminis.general.Options;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class MainPanel extends JPanel implements ChangeListener, Observer, ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int FPS_MIN = 0;
	private static final int FPS_MAX = 100;
    private static final int FPS_INIT = (int) Options.mainLoopSleep.get(); 
    private static final String FILE_EXTENSION = "json";
    
    private StatsCollector statsCollector;
    private JLabel bestFitnessLbl;
    private JLabel avgFitnessLbl;
    private JLabel avgAgeLbl;
    private JLabel avgDistanceLbl;
    
    private General general;
    private JFileChooser fileChooser;
    private JButton exportBtn;
    private JButton importBtn;
    
	public MainPanel(StatsCollector statsCollector, General general) {
		this.statsCollector = statsCollector;
		this.general = general;
		
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("*."+FILE_EXTENSION, FILE_EXTENSION));

		setLayout(null);
		
		JFrame frame = new JFrame("Evoluting-life-java");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	    frame.add(this);
	    frame.setSize(300, 330);
	    frame.setVisible(true);
	    frame.setResizable(false);
	    
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
        JPanel delayPanel = new JPanel();
        delayPanel.setLayout(new BoxLayout(delayPanel, BoxLayout.PAGE_AXIS));
        delayPanel.setBorder(BorderFactory.createTitledBorder("Delay"));
        delayPanel.add(framesPerSecond);
        
        // Labels
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.PAGE_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats"));
        
        bestFitnessLbl = new JLabel("", JLabel.LEFT);
        statsPanel.add(bestFitnessLbl);
        
        avgFitnessLbl = new JLabel("", JLabel.LEFT);
        statsPanel.add(avgFitnessLbl);
        
        avgAgeLbl = new JLabel("", JLabel.LEFT);
        statsPanel.add(avgAgeLbl);
        
        avgDistanceLbl = new JLabel("", JLabel.LEFT);
        statsPanel.add(avgDistanceLbl);
        
        // Buttons
        JPanel actionsPanel = new JPanel();
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.PAGE_AXIS));
        
        exportBtn = new JButton("Export");
        exportBtn.addActionListener(this);
        actionsPanel.add(exportBtn);
        
        importBtn = new JButton("Import");
        importBtn.addActionListener(this);
        actionsPanel.add(importBtn);
        
        actionsPanel.setBounds(0, 0, 300, 100);
        add(actionsPanel);
        statsPanel.setBounds(0, 100, 300, 100);
        add(statsPanel);
        
        delayPanel.setBounds(0, 200, 300, 100);
        add(delayPanel);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
		if (event.type.equals(EventType.CYCLE_END)) {
            if ((int)event.value % 500 == 0) {
                Stats stats = this.statsCollector.getStats();
				bestFitnessLbl.setText("Best fitness: " + stats.getAverageBestFitness());
				avgFitnessLbl.setText("Avg fitness: " + stats.getAverageHealth());
				avgAgeLbl.setText("Avg age: " + stats.getAverageAge());
				avgDistanceLbl.setText("Avg distance: " + stats.getAverageDistance());
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

			int returnVal = fileChooser.showOpenDialog(MainPanel.this);
			 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                doImport(file);
            }
			
		}
		else if (e.getSource().equals(exportBtn)) {
			int returnVal = fileChooser.showSaveDialog(MainPanel.this);
			
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
