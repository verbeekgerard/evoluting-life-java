package eu.luminis.genetics;

import eu.luminis.util.Option;
import eu.luminis.util.Range;

class EvolvingValue {
    private final Option mutationRate;
    private final Option replacementRate;
    private final Option mutationFraction;
    private final Range range;

    EvolvingValue(Option mutationRate, Option replacementRate, Option mutationFraction, Range range) {
        this.mutationRate = mutationRate;
        this.replacementRate = replacementRate;
        this.mutationFraction = mutationFraction;
        this.range = range;
    }

    double getNewValue() {
        return getNewValue(0.0);
    }

    double getNewValue(double offset) {
        return range.gaussian() + offset;
    }

    double mutateValue(double value) {
        return mutateValue(value, 0.0);
    }

    double mutateValue(double value, double offset) {
        if (Math.random() <= replacementRate.get()) {
            return getNewValue(offset);
        }

        if (Math.random() <= mutationRate.get()) {
            value += range.gaussianMutation(mutationFraction.get());
        }

        return value;
    }

    double mutateValueWithBounds(double value) {
        return mutateValueWithBounds(value, 0.0);
    }

    double mutateValueWithBounds(double value, double offset) {
        double mutatedValue = mutateValue(value);
        return range.assureBounds(mutatedValue) + offset;
    }

    double mutateValueWithBounds(double value, double lower, double upper) {
        double mutatedValue = mutateValue(value);
        return new Range(lower, upper).assureBounds(mutatedValue);
    }

    double mutateValueWithLowerBound(double value) {
        double mutatedValue = mutateValue(value);
        return range.assureLowerBound(mutatedValue);
    }

    double mutateValueWithLowerBound(double value, double lower) {
        double mutatedValue = mutateValue(value);
        return new Range(lower, range.getUpperBound()).assureLowerBound(mutatedValue);
    }

    double mutateValueWithUpperBound(double value) {
        double mutatedValue = mutateValue(value);
        return range.assureUpperBound(mutatedValue);
    }

    double mutateValueWithUpperBound(double value, double upper) {
        double mutatedValue = mutateValue(value);
        return new Range(range.getLowerBound(), upper).assureUpperBound(mutatedValue);
    }
}
