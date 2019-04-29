package org.rafalzajac;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.rafalzajac.Data.Subject;
import org.rafalzajac.Data.DataIO;
import java.io.IOException;

/**
 * Class controlling behaviour of application and interaction with GUI elements.
 * Imports data to the program (Load files), displays file names on left panel in listView
 * Visualize data on line chart and allows you to switch which data should be plotted (Plot options).
 * Lastly it allows for saving calculated data into .txt file (Save results) and exit application
 * @author Rafal Zajac
 */
public class Controller {

    @FXML
    private ListView<Subject> listView;
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private CheckMenuItem COPx;
    @FXML
    private CheckMenuItem COPy;

    // == Instance of a dataIO class == //
    private DataIO data = new DataIO();


    /**
     * Class calls method processing data from DataIO after clicking "Load files" option from menu item list in main
     * window. Method populates list view with subject names and handles changes (mouse click) to displayed chosen
     * subjects data. It allows you also to change which data should be displayed (Plot option in menu item).
     * Data are displayed on a line chart.
     * @throws IOException when no data is selected in file chooser.
     */
    public void chooseFile() throws IOException {

        // == Call to method processing data == //
       data.processData();

       // == Change listener allowing to view selected subject's data == //
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {

                Subject subject = listView.getSelectionModel().getSelectedItem();

                // ==  Defining chart series == //
                XYChart.Series<Number, Number> series = new XYChart.Series();
                XYChart.Series<Number, Number> series2 = new XYChart.Series();
                lineChart.setCreateSymbols(false);
                lineChart.setLegendVisible(false);

                // == Plot option displaying only COPx chart == //
                if(COPx.isSelected() && !COPy.isSelected()){

                    // == Chart is always clear first so old data doesn't overlay with new one == //
                    lineChart.getData().clear();

                    // == Populating series with chosen data == //
                    for (int i = 0; i<subject.getCopX().size(); i++){
                        series.getData().add(new XYChart.Data<>(i, subject.getCopX().get(i)));
                    }
                    lineChart.getData().add(series);
                }

                // == Plot option displaying only COPy chart == //
                if (COPy.isSelected() && !COPx.isSelected()){

                    // == Chart is always clear first so old data doesn't overlay with new one == //
                    lineChart.getData().clear();

                    // == Populating series with chosen data == //
                    for (int i = 0; i<subject.getCopY().size(); i++){
                        series.getData().add(new XYChart.Data<>(i, subject.getCopY().get(i)));
                    }
                    lineChart.getData().add(series);
                }

                // == Plot option displaying both COPx and COPy chart == //
                if (COPy.isSelected() && COPx.isSelected()){

                    // == Chart is always clear first so old data doesn't overlay with new one == //
                    lineChart.getData().clear();

                    // == Populating series with chosen data == //
                    for (int i = 0; i<subject.getCopY().size(); i++){
                        series.getData().add(new XYChart.Data<>(i, subject.getCopX().get(i)));
                        series2.getData().add(new XYChart.Data<>(i, subject.getCopY().get(i)));
                    }
                    lineChart.getData().addAll(series, series2);
                }
            }
        });

        // == Setting subjects to list view and definition of selection model == //
        listView.setItems(data.getSubjects());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectFirst();
    }


    /**
     * Method invoked by clicking "Save results" from menu item list in main window. Performs calculations of all data
     * imported to program and saves results in location pointed by the user
     */
    public void saveFile () {
        data.saveToFile();
    }

    /**
     * Method invoked by clicking "Exit" from menu item list in main window. Allows to close program when it's work
     * is done
     */
    public void closeProgram () {
        System.exit(0);
    }

}
