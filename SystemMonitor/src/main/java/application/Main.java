package application;

import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import provider.Provider;

public class Main extends Application
{

	@Override
	public void start( Stage primaryStage ) throws Exception
	{
		// We create a borderpane as the root element. On the Top there will be
		// the "start fetching" button. To show the system resources The hbox
		// will contain the three elements, cpu usage graph, ram usage graph and
		// the process list
		BorderPane root = new BorderPane();
		Label label = new Label( "A start button will be placed here" );
		HBox hBox = new HBox( 10 );
		hBox.setPadding( new Insets( 7, 7, 7, 7 ) );
		hBox.setAlignment( Pos.BASELINE_LEFT );
		hBox.getChildren().setAll( createBarChart(), createPieChart() );

		root.setTop( label );
		BorderPane.setAlignment( label, Pos.TOP_CENTER );
		root.setLeft( hBox );
		primaryStage.setTitle( "System Monitor" );
		primaryStage.setResizable( false );
		primaryStage.setScene( new Scene( root, 1200, 400 ) );
		primaryStage.show();

	}

	private static Node createListView()
	{
		// Create Listview with all processes
		ListView< String > processList = new ListView<>();
		processList.setMinWidth( 200 );
		ObservableList< String > procs = FXCollections.observableArrayList();
		Provider.fetchActiveProcesses();
		JavaFxObservable.emitOnChanged( Provider.activeProcesses ).observeOn( JavaFxScheduler.platform() )
				.subscribe( s -> s.forEach( p -> procs.add( p.getName() ) ) );

		JavaFxObservable.emitOnChanged( procs ).observeOn( JavaFxScheduler.platform() )
				.subscribe( s -> processList.setItems( s ) );

		return processList;
	}

	@SuppressWarnings( "unchecked" )
	private static Node createBarChart()
	{
		// Barchart for cpu usage must be created
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
		JavaFxObservable.emitOnChanged( Provider.cpuUsage ).observeOn( JavaFxScheduler.platform() ).retry()
				.subscribe( s -> chartSeries.setData( s ) );
		bc.getData().addAll( chartSeries );

		return bc;
	}

	private static Node createPieChart()
	{
		// Piechart for RAM usage must be created. Fetching of data get started
		PieChart pieChart = new PieChart();
		pieChart.setLegendVisible( true );
		pieChart.setLegendSide( Side.BOTTOM );
		pieChart.setTitle( "RAM Overview" );
		pieChart.setLabelsVisible( false );
		Provider.fetchRAMdata();
		JavaFxObservable.emitOnChanged( Provider.ramUsage ).observeOn( JavaFxScheduler.platform() ).retry()
				.subscribe( s -> pieChart.setData( s ) );
		return pieChart;
	}
}
