package algorithm;

import java.util.*;
import java.io.*;

public class Node {
    public double Output;
    // Output signal from current node

    public double Weight[];
    // Vector of weights from previous nodes to current node

    public double Threshold;
    // algorithm.Node Threshold /Bias

    public double WeightDiff[];
    // Weight difference between the nth and the (n-1) iteration

    public double ThresholdDiff;
    // Threshold difference between the nth and the (n-1) iteration

    public double SignalError;
    // Output signal error

    // InitialiseWeights function assigns a randomly
    // generated number, between -1 and 1, to the
    // Threshold and Weights to the current node
    private void InitialiseWeights() {
        Threshold = -1 + 2 * Math.random();
        // Initialise threshold nodes with a random
        // number between -1 and 1

        ThresholdDiff = 0;
        // Initially, ThresholdDiff is assigned to 0 so
        // that the Momentum term can work during the 1st
        // iteration

        for (int i = 0; i < Weight.length; i++) {
            Weight[i] = -1 + 2 * Math.random();
            // Initialise all weight inputs with a
            // random number between -1 and 1

            WeightDiff[i] = 0;
            // Initially, WeightDiff is assigned to 0
            // so that the Momentum term can work during
            // the 1st iteration
        }
    }

    public Node(int NumberOfNodes) {
        Weight = new double[NumberOfNodes];
        // Create an array of Weight with the same
        // size as the vector of inputs to the node

        WeightDiff = new double[NumberOfNodes];
        // Create an array of weightDiff with the same
        // size as the vector of inputs to the node

        InitialiseWeights();
        // Initialise the Weights and Thresholds to the node
    }

    public double[] get_weights() {
        return Weight;
    }

    public double get_output() {
        return Output;
    }
};