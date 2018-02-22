package eu.luminis.ui;

import eu.luminis.events.Event;
import eu.luminis.events.EventType;
import eu.luminis.Simulation;
import eu.luminis.Options;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

public class MainPanel extends JPanel implements ChangeListener, Observer {
    private static final long serialVersionUID = 1L;
    private DecimalFormat decimalFormat = new DecimalFormat("0.000");
    private DecimalFormat numberFormat = new DecimalFormat("#,###,###,###");

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
    private JLabel cycleCostLbl;
    private JLabel collideCostLbl;
    private JLabel cycleTimeLbl;

    private long startTime = System.nanoTime();

    public MainPanel(StatsCollector statsCollector, Simulation simulation) {
		this.statsCollector = statsCollector;

        this.genesFileReader = new GenesFileReader(this, simulation);
        this.genesFileWriter = new GenesFileWriter(this, simulation);

		setLayout(null);

		JFrame frame = new JFrame("Evoluting-life-java");
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	    frame.add(this);
	    frame.setSize(310, 370);
	    frame.setVisible(true);
	    frame.setResizable(false);

        JPanel delayPanel = setupDelaySlider();
        JPanel statsPanel = setupStatisticsPanel();
        JPanel actionsPanel = setupActionsPanel();
        JPanel optionsPanel = setupOptionsPanel();

        actionsPanel.setBounds(5, 0, 300, 90);
        add(actionsPanel);

        statsPanel.setBounds(5, 90, 300, 110);
        add(statsPanel);

        delayPanel.setBounds(5, 200, 300, 80);
        add(delayPanel);

        optionsPanel.setBounds(5, 280, 300, 60);
        add(optionsPanel);
    }

    @Override
    public void update(Observable o, Object arg) {
        Event event = (Event) arg;
        if (event.type.equals(EventType.CYCLE_END) && this.isShowing()) {
            int iterationCount = (int)event.value;
            if (iterationCount > 500 && iterationCount % 500 == 0) {
                updateStatsLabels();
                updateOptionsLabels();
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

        cycleTimeLbl = new JLabel("", JLabel.LEFT);
        statsPanel.add(cycleTimeLbl);

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

    private JPanel setupOptionsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.PAGE_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        cycleCostLbl = new JLabel("", JLabel.LEFT);
        statsPanel.add(cycleCostLbl);

        collideCostLbl = new JLabel("", JLabel.LEFT);
        statsPanel.add(collideCostLbl);

        return statsPanel;
    }

    private void updateStatsLabels() {
        Stats stats = this.statsCollector.getStats();
        if(stats == null) {
            return;
        }

        bestFitnessLbl.setText("Best fitness: " + decimalFormat.format(stats.getAverageBestFitness()));
        avgFitnessLbl.setText("Avg health: " + decimalFormat.format(stats.getAverageHealth()));
        avgAgeLbl.setText("Avg age: " + decimalFormat.format(stats.getAverageAge()));
        avgDistanceLbl.setText("Avg distance: " + decimalFormat.format(stats.getAverageDistance()));
        cycleCostLbl.setText("Cycle cost: " + decimalFormat.format(Options.cycleCostFactor.get()));
        collideCostLbl.setText("Collide cost: " + numberFormat.format(Options.collideCostFactor.get()));
        cycleTimeLbl.setText("Cycle time: " + numberFormat.format(System.nanoTime() - startTime));

        startTime = System.nanoTime();
    }

    private void updateOptionsLabels() {
        cycleCostLbl.setText("Cycle cost: " + decimalFormat.format(Options.cycleCostFactor.get()));
        collideCostLbl.setText("Collide cost: " + numberFormat.format(Options.collideCostFactor.get()));
    }
}
