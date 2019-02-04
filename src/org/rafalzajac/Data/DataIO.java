package org.rafalzajac.Data;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uk.me.berndporr.iirj.Butterworth;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;


/**
 * DataIO class covers all data manipulations for this program. To begin with it allows you to import data
 * from text files (force plate recordings), next it filters aquired data with butterworth filter (4th order with
 * cutoff frequency set to 7hz. Filter designed by https://github.com/berndporr/iirj). Because filter always starts
 * from 0 value it takes few data samples to 'get' to desired stata. Thats why I decided to cut first 50 rows to
 * remove filter lag (cut filter lag method). Lastly saveFile calculates remaining variables describing motion
 * of COP (Center of Pressure), like range, velocity, path lenght etc. The resulting file consists result from all
 * data files opened, row by row.
 * @author Rafal Zajac
 */
public class DataIO {


    /**
     * All subjects data are stored in this Subjects list. ProcessData method first populates fileList with files
     * selected by fileChooser. Then based on this list pathList gets appropriate paths to files which are then used
     * by buffered reader to read selected text files, save fileName as subject name and calculate COP data in both
     * planes (CopX is medio-lateral (ML) plane, and CopY is anterior-posterior plane (AP) )
     */
    private ObservableList<Subject> Subjects;
    private static List<File> fileList;
    private static List<Path> pathList = new LinkedList<>();


    /**
     * Both fields are instances of Statistics class. Both are initialized later getting appropriate COP data as input
     * for further calculations.
     */
    private Statistics statisticsAp;
    private Statistics statisticsMl;


    /**
     * Getter for Subjects list. Provides all calculated data to the Controller in order to visualize data
     * @return Subjects stored in ObesrvableList.
     */
    public ObservableList<Subject> getSubjects() {
        return Subjects;
    }


    /**
     * Method obtaining .txt files from selected location via fileChooser, and calculating some basic data.
     * In finally block it builds Subject using its parametrized constructor. Other Subjects data are added later
     * through setter methods. At the end of the method there is a call for another method filtering data.
     * @throws IOException if you decide not to open any files.
     */
    public void processData() throws IOException {

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(".txt", "*.txt"),
                    new FileChooser.ExtensionFilter("All files", "*.*")
            );

            fileList = fileChooser.showOpenMultipleDialog(new Stage());

            // == If there are files opened this part will get their paths to pathList == //
            if (fileList.size()>0){
                for (int i = 0; i< fileList.size(); i++){
                    pathList.add(Paths.get(fileList.get(i).getPath()));
                }
            }

            // == Initializations of Subjects as Collection == //
            Subjects = FXCollections.observableArrayList();

            // == Initialization of table storing file names which are also used as Subject name == //
            String name [] = new String[fileList.size()];

            // ==  Looping through files/subjects == //
            for (int i = 0; i< fileList.size(); i++){

                name[i] = fileList.get(i).getName();
                List<Double> CopX = new LinkedList<>();
                List<Double> CopY = new LinkedList<>();


                BufferedReader br = Files.newBufferedReader(pathList.get(i));

                // == First line of text files is always empty, this part is only to avoid reading this line == //
                br.readLine();
                String input;

                try {
                    while ((input = br.readLine()) != null){
                        String[] split = input.split(", ");
                        CopX.add( (-((Double.parseDouble(split[4]))/ Double.parseDouble(split[2]) ) *100 ));
                        CopY.add( (((-Double.parseDouble(split[3]))/ Double.parseDouble(split[2]) ) *100 ));
                    }
                }finally {
                    if (br!=null){
                        Subjects.add(new Subject(name[i], CopX, CopY));
                        br.close();
                    }

                }
            }

            // == Call to filtering data method == //
            filterData();

        }catch (NullPointerException e){
            System.out.println("You didn't choose anything");
        }
    }


    /**
     * Method filtering data. It uses Butterworth filter (designed by https://github.com/berndporr/iirj). It does
     * not get any input but it manipulates Subjects data. Filtered data takes place of data calculated in previous
     * method. There are two for loops because filter can only get one data set at the time and needs to be restarted
     * after it is finished that is why data for both planes (AP and ML) are calculated separately
     */
    public void filterData() {

        // == Filter instance and its definition == //
        Butterworth butterworth = new Butterworth();
        butterworth.lowPass(4, 100, 7);

        // == Loop filtering COP data in ML (Medio-lateral) direction == //
        for (int i = 0; i < Subjects.size(); i++){
            List<Double> FilteredCopX = new LinkedList<>();
            for (int j =0; j<Subjects.get(i).getCopX().size(); j++){
                FilteredCopX.add(butterworth.filter(Subjects.get(i).getCopX().get(j)));
            }
            // == Using setter method to replace data with filtered version of it == //
            Subjects.get(i).setCopX(FilteredCopX);
            // == Resetting filter in order to use it again later == //
            butterworth.reset();
        }

        // == Loop filtering COP data in AP (Anterior-posterior) direction == //
        for (int i = 0; i < Subjects.size(); i++){
            List<Double> FilteredCopY = new LinkedList<>();
            for (int j =0; j<Subjects.get(i).getCopX().size(); j++){
                FilteredCopY.add(butterworth.filter(Subjects.get(i).getCopY().get(j)));
            }
            // == Using setter method to replace data with filtered version of it == //
            Subjects.get(i).setCopY(FilteredCopY);
            // == Resetting filter in order to use it again later == //
            butterworth.reset();
        }
        // == Call for method cutting filter lag at the beginning of data set == //
        cutFilterLag();
    }


    /**
     * Just as filterData, this method does not take any input but manipulates Subjects data. This time by cutting
     * first 50 lines of data to get rid of filter lag. It might be enough to cut 30 lines but I chose 50 to be sure
     */
    public void cutFilterLag() {
        for (int i = 0; i < Subjects.size(); i++){
            for (int j =0; j<50; j++){
                Subjects.get(i).getCopX().remove(0);
                Subjects.get(i).getCopY().remove(0);
            }
        }
    }


    /**
     * Method fulfills all subject data. After all calculations data are added to Subjects list via their corresponding
     * setter methods. After calling this method all data are ready to save and export to .txt result file
     */
    public void calculateCopData(){

        for (int i=0; i<Subjects.size(); i++) {
            double copMaxAp = -1000;
            double copMinAp = 1000;
            double copMaxMl = -1000;
            double copMinMl = 1000;
            double rmsCOPap = 0;
            double rmsCOPml = 0;

            // == Initialization of Statistics class with suitable data == //
            statisticsAp = new Statistics(Subjects.get(i).CopY);
            statisticsMl = new Statistics(Subjects.get(i).CopX);

            // == First loop only calculates Min, Max, and RMS (only sum of squares now) values it both planes == //
            for (int j=0 ; j<Subjects.get(i).getCopX().size(); j++) {
                if(Subjects.get(i).getCopY().get(j) > copMaxAp) {
                    copMaxAp = Subjects.get(i).getCopY().get(j);
                }else if (Subjects.get(i).getCopX().get(j) > copMaxMl)
                    copMaxMl = Subjects.get(i).getCopX().get(j);

                if(Subjects.get(i).getCopY().get(j)< copMinAp) {
                    copMinAp = Subjects.get(i).getCopY().get(j);
                }else if (Subjects.get(i).getCopX().get(j)< copMinMl)
                    copMinMl = Subjects.get(i).getCopX().get(j);

                rmsCOPap += (Subjects.get(i).getCopY().get(j) * Subjects.get(i).getCopY().get(j));
                rmsCOPml += (Subjects.get(i).getCopX().get(j) * Subjects.get(i).getCopX().get(j));

            }

            // == Ranges of COP and RMS are calculated and set for selected subject in AP direction== //
            Subjects.get(i).setRangeCOPap(copMaxAp-copMinAp);
            Subjects.get(i).setStdCOPap(statisticsAp.getStdDev());
            Subjects.get(i).setRmsCOPap(Math.sqrt(rmsCOPap/Subjects.get(i).getCopY().size()));

            // == Ranges of COP and RMS are calculated and set for selected subject in ML direction== //
            Subjects.get(i).setRangeCOPml(copMaxMl-copMinMl);
            Subjects.get(i).setStdCOPml(statisticsMl.getStdDev());
            Subjects.get(i).setRmsCOPml(Math.sqrt(rmsCOPml/Subjects.get(i).getCopX().size()));

        }

            //== Lenghts are calculated by subtracting current data point from n+1 point. That is why another loop == //
            for(int i=0; i<Subjects.size(); i++) {
                double lenCOPap = 0;
                double lenCOPml = 0;
                double lenCOPtotal = 0;

                for (int j=0; j<Subjects.get(i).getCopX().size()-1; j++){
                    lenCOPap = lenCOPap + Math.abs(Subjects.get(i).getCopY().get(j + 1) - Subjects.get(i).getCopY().get(j));
                    lenCOPml = lenCOPml + Math.abs(Subjects.get(i).getCopX().get(j + 1) - Subjects.get(i).getCopX().get(j));
                    lenCOPtotal += Math.sqrt( (Math.abs(Subjects.get(i).getCopY().get(j + 1) - Subjects.get(i).getCopY().get(j)) * Math.abs(Subjects.get(i).getCopY().get(j + 1) - Subjects.get(i).getCopY().get(j))) +
                                                (Math.abs(Subjects.get(i).getCopX().get(j + 1) - Subjects.get(i).getCopX().get(j)) * Math.abs(Subjects.get(i).getCopX().get(j + 1) - Subjects.get(i).getCopX().get(j))));
                }

                // == All lengths and velocities calculated and added to subject == //
                Subjects.get(i).setLenCOPap(lenCOPap);
                Subjects.get(i).setvCOPap(lenCOPap/(Subjects.get(i).getCopY().size()/100));
                Subjects.get(i).setLenCOPml(lenCOPml);
                Subjects.get(i).setvCOPml(lenCOPml/(Subjects.get(i).getCopX().size()/100));
                Subjects.get(i).setLenCOPtotal(lenCOPtotal);
                Subjects.get(i).setvCOPtotal(lenCOPtotal/(Subjects.get(i).getCopY().size()/100));
            }
    }


    /**
     * Method saving all calculated data to .txt file in row order. First line consist of data headers, next rows
     * are each subject and their results. All data are tab separated
     */
    public void saveToFile() {

        try {
            // == Call for method calculating data described before == //
            calculateCopData();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".txt", "*.txt"));
            File file = fileChooser.showSaveDialog(new Stage());
            if (file !=null) {
                try (FileWriter myFile = new FileWriter(file)) {
                    myFile.write("Name" + "\t" + "raCOPap" + "\t" + "stdCOPap" + "\t" + "rmsCOPap" + "\t" + "lenCOPap" + "\t" +
                            "vCOPap" + "\t" + "raCOPml" + "\t" + "stdCOPml" + "\t" + "rmsCOPml" + "\t" + "lenCOPml" + "\t" + "vCOPml"+ "\t" +
                            "lenCOPtotal" + "\t" + "vCOPtotal" + "\t" + "\n");

                    for (int i = 0; i<Subjects.size(); i++) {
                        myFile.write(Subjects.get(i).getName() + "\t" + Subjects.get(i).getRangeCOPap() + "\t" + Subjects.get(i).getStdCOPap() + "\t" +
                                Subjects.get(i).getRmsCOPap() + "\t" + Subjects.get(i).getLenCOPap() + "\t" + Subjects.get(i).getvCOPap() + "\t" +
                                Subjects.get(i).getRangeCOPml() + "\t" + Subjects.get(i).getStdCOPml() + "\t" + Subjects.get(i).getRmsCOPml() + "\t" +
                                Subjects.get(i).getLenCOPml() + "\t" + Subjects.get(i).getvCOPml() + "\t" + Subjects.get(i).getLenCOPtotal() + "\t" +
                                Subjects.get(i).getvCOPtotal() + "\n");
                    }

                } catch (IOException e) {
                    System.out.println("Something went wrong, data not saved");
                }
            }
        } catch (NullPointerException e){
            System.out.println("Nothing to save, probably you didn't select any data");
        }
    }
}


