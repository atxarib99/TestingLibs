 package testinglibs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
 import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
 import org.jfree.chart.ChartPanel;
 import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
 import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetUtilities;
 import org.jfree.data.xy.XYSeries;
 import org.jfree.data.xy.XYSeriesCollection;
 import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
 import org.jfree.ui.RefineryUtilities;


public class XYSeriesDemo extends ApplicationFrame implements ChartMouseListener {
    
    //chart panel
    ChartPanel chartPanel;
    
    //x and y crosshairs
    Crosshair xCrosshair;
    Crosshair yCrosshair;
    
    //x and y vals
    public double xCor = 0;
    public double yCor = 0;
    
    //this object is a static boolean that another JFrame uses to kill itself when the app is closing to essentially link them together
    public static boolean isRunning = false;

    /**
     * A demonstration application showing an XY series containing a null value.
     *
     * @param title  the frame title.
     */
    public XYSeriesDemo(final String title) {

        //call the constructor for the Application frame.
        super(title);
        
        //This is the object that will show the dialog box on your cursor when you hover over a point
        CrosshairOverlay overlay = new CrosshairOverlay();
        
        //create the global object crosshairs
        this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(true);
        this.yCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.yCrosshair.setLabelVisible(true); 
        
        //add the crosshairs to the overlay
        overlay.addDomainCrosshair(xCrosshair);
        overlay.addRangeCrosshair(yCrosshair);
        
        //One data series
        final XYSeries series = new XYSeries("Lap 1");
        series.add(1.0, 500.2);
        series.add(5.0, 694.1);
        series.add(4.0, 100.0);
        series.add(12.5, 734.4);
        //another data series
        final XYSeries series2 = new XYSeries("Lap 2");
        series2.add(1.0, 400.2);
        series2.add(5.0, 670.1);
        series2.add(4.0, 200.0);
        series2.add(12.5, 734.4);
        
        
        //A collection of series.
        final XYSeriesCollection data = new XYSeriesCollection();
        //add the series to the collection
        data.addSeries(series);
        data.addSeries(series2);
        
        //create a JFreeChart from the Factory, given parameters (Chart Title, Domain name, Range name, series collection, PlotOrientation, show legend, show tooltips, show url)
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "XY Series Demo",
            "Distance (miles)", 
            "Speed", 
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        //instantiate chart panel object from the object created from ChartFactory
        chartPanel = new ChartPanel(chart);
        //set the size of the panel
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        //add a mouse listener to the chart
        chartPanel.addChartMouseListener(this);
        //set the content of the pane to be the chartPanel
        setContentPane(chartPanel);

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        //when the class is run create this class's object
        final XYSeriesDemo demo = new XYSeriesDemo("XY Series Demo");
        //pack the class, I'm not exactly sure what this does.
        demo.pack();
        //center the class on the frame.
        RefineryUtilities.centerFrameOnScreen(demo);
        //make it visible
        demo.setVisible(true);
        
        //add a listener to the window for when its opened closed or modified.
        demo.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                //do nothing
            }

            //When the window is closing set the isRunning boolean to false.
            @Override
            public void windowClosing(WindowEvent e) {
                isRunning = false;
            }

            //When the window is fully closed set the isRunning boolean to false.
            @Override
            public void windowClosed(WindowEvent e) {
                isRunning = false;
            }

            @Override
            public void windowIconified(WindowEvent e) {
                //do nothing
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                //do nothing
            }

            @Override
            public void windowActivated(WindowEvent e) {
                //do nothing
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                //do nothing
            }
        });
        
        //set the isRunning boolean to true
        isRunning = true;
        //create subwindow that holds x and y parameters.
        //create the subwindow that shows the values.
        new TextForm().setVisible(true);

    }

    // When the chart is clicked
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {
        //create a static cursor that isnt cleared every time
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) {
        
        //the data area of where the chart is.
        Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
        //get the chart from the chart mouse event
        JFreeChart chart = cme.getChart();
        //Get the xy plot object from the chart
        XYPlot plot = (XYPlot) chart.getPlot();
        //clear all markers
        //this will be a problem for static markers we want to create
        plot.clearDomainMarkers();
        //get the xAxis
        ValueAxis xAxis = plot.getDomainAxis();
        //get the xCordinate from the xPositon of the mouse
        xCor = xAxis.java2DToValue(cme.getTrigger().getX(), dataArea, 
                RectangleEdge.BOTTOM);
        //find the y cordinate from the plots data set given a x cordinate
        yCor = DatasetUtilities.findYValue(plot.getDataset(), 0, xCor);
        //create a marker at the x Coordinate with black paint
        ValueMarker marker = new ValueMarker(xCor);
        marker.setPaint(Color.BLACK);
        //add a marker on the x axis given a marker. This essentially makes the marker verticle
        plot.addDomainMarker(marker);
        
        //string object that holds values for all the series on the plot.
        String yCordss = "";
        //repeat the loop for each series in the plot
        for(int i = 0; i < plot.getDataset().getSeriesCount(); i++) {
            //get the collection from the plots data set
            XYSeriesCollection col = (XYSeriesCollection) plot.getDataset();
            //get the plots name from the series's object
            String plotName = plot.getDataset().getSeriesKey(i).toString();
            //create a new collection 
            XYSeriesCollection col2 = new XYSeriesCollection();
            //add the series with the name we found to the other collection
            //we do this because the findYValue() method takes a collection
            col2.addSeries(col.getSeries(plotName));
            //get the y value for the current series.
            double val = DatasetUtilities.findYValue(col2, 0, xCor);
            //add the name and value to the string
            yCordss += plotName + "\n" + val + "\n\n\n";
        }
        //set the Sub forms x and y data to what we have found
        TextForm.xCor = xCor;
        TextForm.yCor = yCordss;
        
        //set this objects crosshair data to the value we have
        this.xCrosshair.setValue(xCor);
        this.yCrosshair.setValue(yCor);
    }

}