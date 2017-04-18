package application;

import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import provider.Provider;

public class Main extends Application
{

	@SuppressWarnings( "unchecked" )
	@Override
	public void start( Stage primaryStage ) throws Exception
	{
		// Create root element
		HBox root = new HBox();

		// create barchart and set Information
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis( 0, 100, 1 );
		final BarChart< String, Number > bc = new BarChart< String, Number >( xAxis, yAxis );
		bc.setTitle( "CPU Overview" );
		xAxis.setLabel( "CPU" );
		yAxis.setLabel( "Usage" );
		yAxis.setAutoRanging( false );
		bc.setLegendVisible( false );
		bc.setCategoryGap( 10 );

		// Create series and start fetching CPU data. Emit on changed of
		// valueList from the provider
		Series< String, Number > chartSeries = new XYChart.Series<>();
		Provider.fetchCPUdata();
		JavaFxObservable.emitOnChanged( Provider.cpuUsage ).subscribe( s -> chartSeries.setData( s ) );
		bc.getData().addAll( chartSeries );

		// Create Listview with all processes
		ListView< String > processList = new ListView<>();
		Provider.fetchActiveProcesses();
		JavaFxObservable.emitOnChanged( Provider.activeProcesses ).subscribe( s -> processList.setItems( s ) );

		// Create Piechart and start fetching data
		PieChart pieChart = new PieChart();
		pieChart.setLegendVisible( false );
		Provider.fetchRAMdata();
		JavaFxObservable.emitOnChanged( Provider.ramUsage ).subscribe( s -> pieChart.setData( s ) );

		// Add series to barchart, add barchart to root element and show scene
		root.getChildren().setAll( bc, processList, pieChart );
		primaryStage.setScene( new Scene( root, 800, 600 ) );
		primaryStage.show();

	}

}
