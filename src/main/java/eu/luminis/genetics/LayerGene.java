package eu.luminis.genetics;

public class LayerGene extends Gene {
    private static final LayerGeneEvolver evolver = new LayerGeneEvolver();

    private double[][] weights;
    private double[] biases;
    private double[][] stateWeights;
    private double[] gains;

    private int rowDelta;
    private int columnDelta;

    public LayerGene(int rows, int columns) {
        weights = new double[rows][columns];
        biases = new double[rows];
        stateWeights = new double[rows][rows];
        gains = new double[rows];
        
        for (int i = 0; i < rows; i++) {
            biases[i] = evolver.Bias.getNewValue();
            gains[i] = evolver.Gain.getNewValue();

            for (int j = 0; j < columns; j++) {
                weights[i][j] = evolver.Weight.getNewValue();
            }
            for (int j = 0; j < rows; j++) {
                stateWeights[i][j] = evolver.StateWeight.getNewValue();
            }
        }
    }

    public LayerGene(double[][] weights, double[] biases, double[][] stateWeights, double[] gains) {
        this.weights = weights;
        this.biases = biases;
        this.stateWeights = stateWeights;
        this.gains = gains;
    }

    public double[][] getWeights() {
        return weights;
    }

    public double[] getBiases() {
        return biases;
    }

    public double[][] getStateWeights() {
        return stateWeights;
    }

    public double[] getGains() {
        return gains;
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
            gains[i] = evolver.Gain.mutateValue(gains[i]);

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
        
        double[] initiateProperties = new double[initiateRows * (initiateColumns + 1 + initiateRows + 1)];

        int k=0;
        k = initiateProperties(k, initiateProperties, weights, rowDelta, columnDelta);
        k = initiateProperties(k, initiateProperties, biases, rowDelta);
        k = initiateProperties(k, initiateProperties, stateWeights, rowDelta, rowDelta);
        k = initiateProperties(k, initiateProperties, gains, rowDelta);

        return initiateProperties;
    }

    @Override
    public LayerGene initiate(double[] properties) {
        int k=0;

        double[][] initiateWeights = new double[this.getRows()][this.getColumns()];
        k = initiate(k, properties, initiateWeights, rowDelta, columnDelta);

        double[] initiateBiases = new double[this.getRows()];
        k = initiate(k, properties, initiateBiases, rowDelta);

        double[][] initiateStateWeights = new double[this.getRows()][this.getRows()];
        k = initiate(k, properties, initiateStateWeights, rowDelta, rowDelta);

        double[] initiateGains = new double[this.getRows()];
        k = initiate(k, properties, initiateGains, rowDelta);

        return new LayerGene(initiateWeights, initiateBiases, initiateStateWeights, initiateGains);
    }

    @Override
    public LayerGene[] newArray(int size) {
        return new LayerGene[size];
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
