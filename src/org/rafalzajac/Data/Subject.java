package org.rafalzajac.Data;

import java.util.List;

/**
 * Class describing subject data. It has one constructor getting as input only three fields (name, CopX, CopY)
 * for which data are obtained after files are loaded to the program. All remaining fields are calculated later
 * and added to subject via their corresponding setter methods. Getters are used to obtain data for exporting
 * result file in .txt format
 * @author Rafal Zajac
 */
public class Subject {

    private String name;
    double rangeCOPap;
    double stdCOPap;
    double rmsCOPap;
    double lenCOPap;
    double vCOPap;
    double rangeCOPml;
    double stdCOPml;
    double rmsCOPml;
    double lenCOPml;
    double vCOPml;
    double lenCOPtotal;
    double vCOPtotal;
    List<Double> CopX;
    List<Double> CopY;


    /**
     * Constructor used to create subject which will be updated later.
     * @param name subject/file name
     * @param copX list of center of pressure in ML direction data points.
     * @param copY list of center of pressure in AP direction data points.
     */
    public Subject(String name, List<Double> copX, List<Double> copY) {
        this.name = name;
        CopX = copX;
        CopY = copY;
    }

    public String getName() {
        return name;
    }

    public double getRangeCOPap() {
        return rangeCOPap;
    }

    public double getStdCOPap() {
        return stdCOPap;
    }

    public double getRmsCOPap() {
        return rmsCOPap;
    }

    public double getLenCOPap() {
        return lenCOPap;
    }

    public double getvCOPap() {
        return vCOPap;
    }

    public double getRangeCOPml() {
        return rangeCOPml;
    }

    public double getStdCOPml() {
        return stdCOPml;
    }

    public double getRmsCOPml() {
        return rmsCOPml;
    }

    public double getLenCOPml() {
        return lenCOPml;
    }

    public double getvCOPml() {
        return vCOPml;
    }

    public double getLenCOPtotal() {
        return lenCOPtotal;
    }

    public double getvCOPtotal() {
        return vCOPtotal;
    }

    public List<Double> getCopX() {
        return CopX;
    }

    public List<Double> getCopY() {
        return CopY;
    }

    public void setRangeCOPap(double rangeCOPap) {
        this.rangeCOPap = rangeCOPap;
    }

    public void setStdCOPap(double stdCOPap) {
        this.stdCOPap = stdCOPap;
    }

    public void setRmsCOPap(double rmsCOPap) {
        this.rmsCOPap = rmsCOPap;
    }

    public void setLenCOPap(double lenCOPap) {
        this.lenCOPap = lenCOPap;
    }

    public void setvCOPap(double vCOPap) {
        this.vCOPap = vCOPap;
    }

    public void setRangeCOPml(double rangeCOPml) {
        this.rangeCOPml = rangeCOPml;
    }

    public void setStdCOPml(double stdCOPml) {
        this.stdCOPml = stdCOPml;
    }

    public void setRmsCOPml(double rmsCOPml) {
        this.rmsCOPml = rmsCOPml;
    }

    public void setLenCOPml(double lenCOPml) {
        this.lenCOPml = lenCOPml;
    }

    public void setvCOPml(double vCOPml) {
        this.vCOPml = vCOPml;
    }

    public void setLenCOPtotal(double lenCOPtotal) {
        this.lenCOPtotal = lenCOPtotal;
    }

    public void setvCOPtotal(double vCOPtotal) {
        this.vCOPtotal = vCOPtotal;
    }

    public void setCopX(List<Double> copX) {
        CopX = copX;
    }

    public void setCopY(List<Double> copY) {
        CopY = copY;
    }

    /**
     * This toString method is used only to print subjects name in listView instead of object information
     * @return subjects of to be precise file name
     */
    @Override
    public String toString() {
        return name;
    }
}






