package eu.luminis.genetics;

public class NeuronGene extends Gene {
    private static final NeuronGeneEvolver evolver = new NeuronGeneEvolver();

    private double threshold;
    private double relaxation;
    private final AxonGene[] axons;

    public NeuronGene(int maxOutputs) {
        threshold = evolver.Threshold.getNewValue();
        relaxation = evolver.Relaxation.getNewValue();

        axons = new AxonGene[maxOutputs];
        for (int i = 0; i < maxOutputs; i++) {
            this.axons[i] = new AxonGene();
        }
    }

    public NeuronGene(double threshold, double relaxation, int axonCount) {
        this.threshold = threshold;
        this.relaxation = relaxation;
        this.axons = new AxonGene[axonCount];
    }

    public NeuronGene(double threshold, double relaxation, AxonGene[] axons) {
        this.threshold = threshold;
        this.relaxation = relaxation;

        this.axons = new AxonGene[axons.length];
        for (int i=0; i<axons.length; i++) {
            AxonGene axon = axons[i];
            this.axons[i] = new AxonGene(axon.getStrength(), axon.getStrengthening(), axon.getWeakening());
        }
    }

    public double getThreshold() {
        return threshold;
    }

    public double getRelaxation() {
        return relaxation;
    }

    public AxonGene[] getAxons() {
        return axons;
    }

    public void mutate() {
        threshold = evolver.Threshold.mutateValueWithLowerBound(threshold);
        relaxation = evolver.Relaxation.mutateValueWithBounds(relaxation);
        mutateAxons();
    }

    public NeuronGene[] mate(NeuronGene partner) {
        NeuronGene[] children = evolver.mate(this, partner);

        for (int i = 0; i < this.axons.length; i++) {
            AxonGene[] childAxons = this.axons[i].mate(partner.axons[i]);

            for (int j = 0; j < children.length; j++) {
                children[j].axons[i] = childAxons[j];
            }
        }

        return children;
    }

    @Override
    public double[] getInitiateProperties() {
        return new double[] {
                this.threshold,
                this.relaxation
        };
    }

    @Override
    public NeuronGene initiate(double[] properties) {
        return new NeuronGene(properties[0], properties[1], this.axons.length);
    }

    @Override
    public NeuronGene[] newArray(int size) {
        return new NeuronGene[size];
    }

    private void mutateAxons() {
        for (AxonGene axon : this.axons) {
            axon.mutate();
        }
    }
}
