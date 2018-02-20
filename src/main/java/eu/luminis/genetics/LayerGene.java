package eu.luminis.genetics;

public class LayerGene extends Gene {
    private static final LayerGeneEvolver evolver = new LayerGeneEvolver();

    private double[][] weights;
    private double[] biases;
    private double[][] stateWeights;

    private int rowDelta;
    private int columnDelta;

    public LayerGene(int rows, int columns) {
        weights = new double[rows][columns];
        biases = new double[rows];
        stateWeights = new double[rows][rows];
        
        for (int i = 0; i < rows; i++) {
            biases[i] = evolver.Bias.getNewValue();

            for (int j = 0; j < columns; j++) {
                weights[i][j] = evolver.Weight.getNewValue();
            }
            for (int j = 0; j < rows; j++) {
                stateWeights[i][j] = evolver.StateWeight.getNewValue();
            }
        }
    }

    public LayerGene(double[][] weights, double[] biases, double[][] stateWeights) {
        this.weights = weights;
        this.biases = biases;
        this.stateWeights = stateWeights;
    }

    public double[][] getWeights() {
        return weights;
    }

    public double[] getBiases() {
        return biases;
    }

    public double[][] getStateWeights() {
        if (this.stateWeights == null) {
            stateWeights = new double[biases.length][biases.length];
        }
        
        return stateWeights;
    }

    public int getRows() {
        return weights.length;
    }

    public int getColumns() {
        return weights[0].length;
    }

    public void mutate() {
        for (int i = 0; i < this.getRows(); i++) {
            biases[i] = evolver.Bias.mutateValue(biases[i]);

            for (int j = 0; j < this.getColumns(); j++) {
                weights[i][j] = evolver.Weight.mutateValue(weights[i][j]);
            }
            for (int j = 0; j < this.getRows(); j++) {
                stateWeights[i][j] = evolver.StateWeight.mutateValue(stateWeights[i][j]);
            }
        }
    }

    public LayerGene[] mate(LayerGene partner) {
        this.setSizeDeltas(partner.getRows(), partner.getColumns());
        partner.setSizeDeltas(this.getRows(), this.getColumns());

        return evolver.mate(this, partner);
    }

    @Override
    public double[] getInitiateProperties() {
        int initiateRows = this.getRows() + rowDelta;
        int initiateColumns = this.getColumns() + columnDelta;
        
        double[] initiateProperties = new double[initiateRows * (initiateColumns + 1 + initiateRows)];

        int k=0;
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                initiateProperties[k++] = weights[i][j];
            }
            k += columnDelta; // Fill up
        }
        k += rowDelta * (this.getColumns() + columnDelta); // Fill up

        for (int i = 0; i < this.getRows(); i++) {
            initiateProperties[k++] = biases[i];
        }
        k += rowDelta; // Fill up

        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getRows(); j++) {
                initiateProperties[k++] = stateWeights[i][j];
            }
            k += rowDelta; // Fill up
        }

        return initiateProperties;
    }

    @Override
    public LayerGene initiate(double[] properties) {
        int k=0;

        double[][] initiateWeights = new double[this.getRows()][this.getColumns()];
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                initiateWeights[i][j] = properties[k++];
            }
            k += columnDelta; // Skip filler
        }
        k += rowDelta * (this.getColumns() + columnDelta); // Skip filler

        double[] initiateBiases = new double[this.getRows()];
        for (int i = 0; i < this.getRows(); i++) {
            initiateBiases[i] = properties[k++];
        }
        k += rowDelta; // Skip filler

        double[][] initiateStateWeights = new double[this.getRows()][this.getRows()];
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getRows(); j++) {
                initiateStateWeights[i][j] = properties[k++];
            }
            k += rowDelta; // Skip filler
        }

        return new LayerGene(initiateWeights, initiateBiases, initiateStateWeights);
    }

    @Override
    public LayerGene[] newArray(int size) {
        return new LayerGene[size];
    }

    private void setSizeDeltas(int partnerRows, int partnerColumns) {
        rowDelta = partnerRows > this.getRows() ? partnerRows - this.getRows() : 0;
        columnDelta = partnerColumns > this.getColumns() ? partnerColumns - this.getColumns() : 0;
    }
}
