package org.rafalzajac.Data;

import java.util.List;

/**
 * Class allowing for simple statistical evaluation of data sets stored as lists of doubles.
 * Methods allows for calculation of mean, variance and standard deviation of given data.
 * @author Some Guy on StackOverflow (Sorry I couldn't find it once more...)
 */
public class Statistics {
    List<Double> data;
    int size;

    /**
     * Constructor of statistics class
     * @param data - accepts a list of type double for further evaluation
     */
    public Statistics(List<Double> data) {
        this.data = data;
        size = data.size();
    }

    /**
     * Method calculating mean value of a given dataset
     * @return mean ie. Sum of all elements divided by their number
     */
    double getMean() {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/size;
    }

    /**
     * Method calculationg variance of a given dataset.
     * @return variance of a given data set
     */
    double getVariance() {
        double mean = getMean();
        double temp = 0;
        for(double a :data)
            temp += (a-mean)*(a-mean);
        return temp/(size-1);
    }

    /**
     * Method calculating standard deviation of a given dataset
     * @return standard deviation of given dataset
     */
    double getStdDev() {
        return Math.sqrt(getVariance());
    }
}
