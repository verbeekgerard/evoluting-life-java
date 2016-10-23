package eu.luminis.ui;

import eu.luminis.events.Event;
import eu.luminis.events.EventType;
import eu.luminis.Simulation;
import eu.luminis.Options;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class MainPanel extends JPanel implements ChangeListener, Observer {
    private static final long serialVersionUID = 1L;

	private static final int FPS_MIN = 0;
	private static final int FPS_MAX = 100;
    private static final int FPS_INIT = (int) Options.mainLoopSleep.get();

    private StatsCollector statsCollector;
    private GenesFileReader genesFileReader;
    private GenesFileWriter genesFileWriter;

    private JLabel bestFitnessLbl;
    private JLabel avgFitnessLbl;
    private JLabel avgAgeLbl;
    private JLabel avgDistanceLbl;

    public MainPanel(StatsCollector statsCollector, Simulation simulation) {
		this.statsCollector = statsCollector;

        this.genesFileReader = new GenesFileReader(this, simulation);
        this.genesFileWriter = new GenesFileWriter(this, simulation);

		setLayout(null);

		JFrame frame = new JFrame("Evoluting-life-java");
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	    frame.add(this);
	    frame.setSize(300, 330);
	    frame.setVisible(true);
	    frame.setResizable(false);

        JPanel delayPanel = setupDelaySlider();
        JPanel statsPanel = setupStatisticsPanel();
        JPanel actionsPanel = setupActionsPanel();

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
        if (event.type.equals(EventType.CYCLE_END) && this.isShowing()) {
            if ((int)event.value % 500 == 0) {
                updateStatsLabels();
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

    private JPanel setupActionsPanel() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.PAGE_AXIS));

        JButton exportBtn = new JButton("Export");
        exportBtn.addActionListener(genesFileWriter);
        actionsPanel.add(exportBtn);

        JButton importBtn = new JButton("Import");
        importBtn.addActionListener(genesFileReader);
        actionsPanel.add(importBtn);

        return actionsPanel;
    }

    private JPanel setupStatisticsPanel() {
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

        return statsPanel;
    }

    private JPanel setupDelaySlider() {
        JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);
        framesPerSecond.addChangeListener(this);
        framesPerSecond.setMajorTickSpacing(10);
        framesPerSecond.setMinorTickSpacing(1);
        framesPerSecond.setPaintTicks(true);
        framesPerSecond.setPaintLabels(true);
        framesPerSecond.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        Font font = new Font("Serif", Font.ITALIC, 15);
        framesPerSecond.setFont(font);

        //Put the panel together.
        JPanel delayPanel = new JPanel();
        delayPanel.setLayout(new BoxLayout(delayPanel, BoxLayout.PAGE_AXIS));
        delayPanel.setBorder(BorderFactory.createTitledBorder("Delay"));
        delayPanel.add(framesPerSecond);

        return delayPanel;
    }

    private void updateStatsLabels() {
        Stats stats = this.statsCollector.getStats();
        bestFitnessLbl.setText("Best fitness: " + stats.getAverageBestFitness());
        avgFitnessLbl.setText("Avg fitness: " + stats.getAverageHealth());
        avgAgeLbl.setText("Avg age: " + stats.getAverageAge());
        avgDistanceLbl.setText("Avg distance: " + stats.getAverageDistance());
    }
}