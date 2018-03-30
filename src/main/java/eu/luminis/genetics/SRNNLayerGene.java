package eu.luminis.genetics;

public class SRNNLayerGene extends Evolvable {
    private static final LayerGeneEvolver evolver = new LayerGeneEvolver();

    private final double[][] weights;
    private final double[][] stateWeights;
    private final double[] gains;
    private final double[] biases;

    private final double gainOffset;
    private final double biasOffset;

    private int rowDelta;
    private int columnDelta;

    public SRNNLayerGene(int rows, int columns) {
        this(rows, columns, 0.0);
    }

    public SRNNLayerGene(int rows, int columns, double initialBiasOffset) {
        this.biasOffset = initialBiasOffset;
        this.gainOffset = 0.0;

        this.weights = new double[rows][columns];
        this.stateWeights = new double[rows][rows];
        this.gains = new double[rows];
        this.biases = new double[rows];
        
        for (int i = 0; i < rows; i++) {
            this.gains[i] = evolver.Gain.getNewValue();
            this.biases[i] = evolver.Bias.getNewValue(this.biasOffset);

            for (int j = 0; j < columns; j++) {
                this.weights[i][j] = evolver.Weight.getNewValue();
            }
            for (int j = 0; j < rows; j++) {
                this.stateWeights[i][j] = evolver.StateWeight.getNewValue();
            }
        }
    }

    public SRNNLayerGene(double[][] weights, double[][] stateWeights, double[] gains, double[] biases, double initialBiasOffset) {
        this.weights = weights;
        this.stateWeights = stateWeights;
        this.gains = gains;
        this.biases = biases;

        this.gainOffset = calculateAverage(gains) / 10.0; // Somewhat closer to zero
        
        double biasAverage = calculateAverage(biases);
        this.biasOffset = initialBiasOffset + (biasAverage - initialBiasOffset) / 100.0; // Move slow towards the average
    }

    public SRNNLayerGene Clone() {
        return new SRNNLayerGene(
            weights.clone(),
            stateWeights.clone(),
            gains.clone(),
            biases.clone(),
            biasOffset);
    }

    public double[][] getWeights() {
        return weights;
    }

    public double[][] getStateWeights() {
        return stateWeights;
    }

    public double[] getGains() {
        return gains;
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
        for (int i = 0; i < getRows(); i++) {
            gains[i] = evolver.Gain.mutateValue(gains[i], gainOffset);
            biases[i] = evolver.Bias.mutateValue(biases[i], biasOffset);

            for (int j = 0; j < getColumns(); j++) {
                weights[i][j] = evolver.Weight.mutateValue(weights[i][j]);
            }
            for (int j = 0; j < getRows(); j++) {
                stateWeights[i][j] = evolver.StateWeight.mutateValue(stateWeights[i][j]);
            }
        }
    }

    public SRNNLayerGene[] mate(SRNNLayerGene partner) {
        setSizeDeltas(partner.getRows(), partner.getColumns());
        partner.setSizeDeltas(getRows(), getColumns());

        return evolver.mate(this, partner);
    }

    @Override
    public double[] getInitiateProperties() {
        int initiateRows = getRows() + rowDelta;
        int initiateColumns = getColumns() + columnDelta;
        
        double[] properties = new double[initiateRows * (initiateColumns + initiateRows + 2)];

        int k=0;
        k = copyMatrixToProperties(k, properties, weights, rowDelta, columnDelta);
        k = copyMatrixToProperties(k, properties, stateWeights, rowDelta, rowDelta);
        k = copyVectorToProperties(k, properties, gains, rowDelta, gainOffset);
        k = copyVectorToProperties(k, properties, biases, rowDelta, biasOffset);

        return properties;
    }

    @Override
    public SRNNLayerGene initiate(double[] properties) {
        int k=0;

        double[][] initiateWeights = new double[getRows()][getColumns()];
        k = copyPropertiesToMatrix(k, properties, initiateWeights, rowDelta, columnDelta);

        double[][] initiateStateWeights = new double[getRows()][getRows()];
        k = copyPropertiesToMatrix(k, properties, initiateStateWeights, rowDelta, rowDelta);

        double[] initiateGains = new double[getRows()];
        k = copyPropertiesToVector(k, properties, initiateGains, rowDelta);

        double[] initiateBiases = new double[getRows()];
        k = copyPropertiesToVector(k, properties, initiateBiases, rowDelta);

        return new SRNNLayerGene(initiateWeights, initiateStateWeights, initiateGains, initiateBiases, biasOffset);
    }

    @Override
    public SRNNLayerGene[] newArray(int size) {
        return new SRNNLayerGene[size];
    }

    private double calculateAverage(double[] values) {
        double sum = 0.0;
        for(int i=0; i < values.length ; i++) {
            sum += values[i];
        }
        return sum / values.length;        
    }

    private void setSizeDeltas(int partnerRows, int partnerColumns) {
        rowDelta = partnerRows > getRows() ? partnerRows - getRows() : 0;
        columnDelta = partnerColumns > getColumns() ? partnerColumns - getColumns() : 0;
    }

    private static int copyVectorToProperties(int k, double[] properties, double[] vector, int rowsDelta) {
        return copyVectorToProperties(k, properties, vector, rowsDelta, 0.0);
    }

    private static int copyVectorToProperties(int k, double[] properties, double[] vector, int rowsDelta, double initValue) {
        for (int i = 0; i < vector.length; i++) {
            properties[k++] = vector[i];
        }

        if (initValue == 0.0) {
            return k + rowsDelta;
        }

        for (int i = vector.length; i < vector.length + rowsDelta; i++) {
            properties[k++] = initValue;
        }

        return k;
    }

    private static int copyMatrixToProperties(
        int k, double[] properties, double[][] matrix,
        int rowsDelta, int columnsDelta) {
            int columns = matrix[0].length;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < columns; j++) {
                    properties[k++] = matrix[i][j];
                }
                k += columnsDelta; // Fill up
            }
            k += rowsDelta * (columns + columnsDelta); // Fill up

            return k;
    }

    private static int copyPropertiesToVector(int k, double[] properties, double[] vector, int rowsDelta) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] = properties[k++];
        }
        k += rowsDelta; // Skip filler

        return k;
    }
    
    private static int copyPropertiesToMatrix(
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
