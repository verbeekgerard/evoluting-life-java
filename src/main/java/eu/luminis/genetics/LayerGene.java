package eu.luminis.genetics;

public class LayerGene extends Gene {
    private static final LayerGeneEvolver evolver = new LayerGeneEvolver();

    private double[][] weights;
    private double[] biases;

    private int rowDelta;
    private int columnDelta;

    public LayerGene(int rows, int columns) {
        weights = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                weights[i][j] = evolver.Weight.getNewValue();
            }
        }

        biases = new double[rows];
        for (int i = 0; i < rows; i++) {
            biases[i] = evolver.Bias.getNewValue();
        }
    }

    public LayerGene(double[][] weights, double[] biases) {
        this.weights = weights;
        this.biases = biases;
    }

    public double[][] getWeights() {
        return weights;
    }

    public double[] getBiases() {
        return biases;
    }

    public int getRows() {
        return weights.length;
    }

    public int getColumns() {
        return weights[0].length;
    }

    public void mutate() {
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                weights[i][j] = evolver.Weight.mutateValue(weights[i][j]);
            }
        }

        for (int i = 0; i < this.getRows(); i++) {
            biases[i] = evolver.Bias.mutateValue(biases[i]);
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
        
        double[] initiateProperties = new double[initiateRows * (initiateColumns + 1)];

        int k=0;
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                initiateProperties[k++] = weights[i][j];
            }

            // Fill up
            for (int j=0; j<columnDelta; j++) {
                initiateProperties[k++] = 0;
            }
        }

        // Fill up
        int maxColumns = this.getColumns() + columnDelta;
        for (int i=0; i<rowDelta; i++) {
            for (int j=0; j<maxColumns; j++) {
                initiateProperties[k++] = 0;
            }
        }

        for (int i = 0; i < this.getRows(); i++) {
            initiateProperties[k++] = biases[i];
        }

        // Fill up
        for (int i=0; i<rowDelta; i++) {
            initiateProperties[k++] = 0;
        }

        return initiateProperties;
    }

    @Override
    public LayerGene initiate(double[] properties) {
        double[][] initiateWeights = new double[this.getRows()][this.getColumns()];

        int k=0;
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
        
        return new LayerGene(initiateWeights, initiateBiases);
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
