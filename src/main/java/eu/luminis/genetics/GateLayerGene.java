package eu.luminis.genetics;

public class GateLayerGene extends Gene {
    private static final LayerGeneEvolver evolver = new LayerGeneEvolver();

    private double[][] weights;
    private double[][] stateWeights;
    private double[] biases;

    private int rowDelta;
    private int columnDelta;

    public GateLayerGene(int rows, int columns) {
        weights = new double[rows][columns];
        stateWeights = new double[rows][rows];
        biases = new double[rows];
        
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

    public GateLayerGene(double[][] weights, double[][] stateWeights, double[] biases) {
        this.weights = weights;
        this.stateWeights = stateWeights;
        this.biases = biases;
    }

    public double[][] getWeights() {
        return weights;
    }

    public double[][] getStateWeights() {
        return stateWeights;
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
            biases[i] = evolver.Bias.mutateValue(biases[i]);

            for (int j = 0; j < this.getColumns(); j++) {
                weights[i][j] = evolver.Weight.mutateValue(weights[i][j]);
            }
            for (int j = 0; j < this.getRows(); j++) {
                stateWeights[i][j] = evolver.StateWeight.mutateValue(stateWeights[i][j]);
            }
        }
    }

    public GateLayerGene[] mate(GateLayerGene partner) {
        this.setSizeDeltas(partner.getRows(), partner.getColumns());
        partner.setSizeDeltas(this.getRows(), this.getColumns());

        return evolver.mate(this, partner);
    }

    @Override
    public double[] getInitiateProperties() {
        int initiateRows = this.getRows() + rowDelta;
        int initiateColumns = this.getColumns() + columnDelta;
        
        double[] initiateProperties = new double[initiateRows * (initiateColumns + initiateRows + 1)];

        int k=0;
        k = initiateProperties(k, initiateProperties, weights, rowDelta, columnDelta);
        k = initiateProperties(k, initiateProperties, stateWeights, rowDelta, rowDelta);
        k = initiateProperties(k, initiateProperties, biases, rowDelta);

        return initiateProperties;
    }

    @Override
    public GateLayerGene initiate(double[] properties) {
        int k=0;

        double[][] initiateWeights = new double[this.getRows()][this.getColumns()];
        k = initiate(k, properties, initiateWeights, rowDelta, columnDelta);

        double[][] initiateStateWeights = new double[this.getRows()][this.getRows()];
        k = initiate(k, properties, initiateStateWeights, rowDelta, rowDelta);

        double[] initiateBiases = new double[this.getRows()];
        k = initiate(k, properties, initiateBiases, rowDelta);

        return new GateLayerGene(initiateWeights, initiateStateWeights, initiateBiases);
    }

    @Override
    public GateLayerGene[] newArray(int size) {
        return new GateLayerGene[size];
    }

    private void setSizeDeltas(int partnerRows, int partnerColumns) {
        rowDelta = partnerRows > this.getRows() ? partnerRows - this.getRows() : 0;
        columnDelta = partnerColumns > this.getColumns() ? partnerColumns - this.getColumns() : 0;
    }

    private static int initiateProperties(int k, double[] initiateProperties, double[] vector, int rowsDelta) {
        for (int i = 0; i < vector.length; i++) {
            initiateProperties[k++] = vector[i];
        }
        k += rowsDelta; // Fill up

        return k;
    }

    private static int initiateProperties(
        int k, double[] initiateProperties, double[][] matrix,
        int rowsDelta, int columnsDelta) {
            int columns = matrix[0].length;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < columns; j++) {
                    initiateProperties[k++] = matrix[i][j];
                }
                k += columnsDelta; // Fill up
            }
            k += rowsDelta * (columns + columnsDelta); // Fill up

            return k;
    }

    private static int initiate(int k, double[] properties, double[] vector, int rowsDelta) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] = properties[k++];
        }
        k += rowsDelta; // Skip filler

        return k;
    }
    
    private static int initiate(
        int k, double[] properties, double[][] matrix,
        int rowsDelta, int columnsDelta) {
            int columns = matrix[0].length;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < columns; j++) {
                    matrix[i][j] = properties[k++];
                }
                k += columnsDelta; // Skip filler
            }
            k += rowsDelta * (columns + columnsDelta); // Skip filler
                
            return k;
    }
}
