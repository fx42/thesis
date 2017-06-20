package application;

import java.util.List;

import io.reactivex.Observable;
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
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import provider.ISystemProvider;
import provider.SystemProvider;

public class MainApplication extends Application
{

	public static void main( String[] args )
	{
		Application.launch( args );
	}

	@Override
	public void start( Stage primaryStage ) throws Exception
	{
		BorderPane root = new BorderPane();
		HBox hBox = new HBox( 10 );
		hBox.setPadding( new Insets( 8, 5, 5, 8 ) );
		hBox.setAlignment( Pos.BASELINE_CENTER );
		hBox.getChildren().setAll( createBarChart(), createProcessList(), createPieChart() );

		root.setLeft( hBox );
		primaryStage.setTitle( "System Monitor" );
		primaryStage.setResizable( false );
		primaryStage.setScene( new Scene( root, 1200, 400 ) );
		primaryStage.show();
	}

	private static Node createBarChart()
	{
		ISystemProvider provider = SystemProvider.getInstance();

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis( 0, 100, 1 );
		final BarChart< String, Number > barChart = new BarChart< String, Number >( xAxis, yAxis );
		barChart.setTitle( "CPU Overview" );
		xAxis.setLabel( "CPU" );
		yAxis.setLabel( "Usage" );
		yAxis.setAutoRanging( false );
		barChart.setLegendVisible( false );
		barChart.setAnimated( true );
		barChart.setCategoryGap( 8 );

		List< Observable< Double > > cpuLoadList = provider.fetchCpuValues();
		ObservableList< Series< String, Number > > seriesList = FXCollections.observableArrayList();

		for ( int i = 0; i < cpuLoadList.size(); i++ )
		{
			Series< String, Number > chartSeries = setObservableChartData( cpuLoadList.get( i ), i );
			seriesList.add( chartSeries );
		}
		barChart.setData( seriesList );

		return barChart;
	}

	private static Series< String, Number > setObservableChartData( Observable< Double > data, int index )
	{
		XYChart.Series< String, Number > chartSeries = new XYChart.Series<>();
		chartSeries.getData().add( new XYChart.Data< String, Number >( "CPU  " + index, 0 ) );
		data.map( x -> x * 100 ).observeOn( JavaFxScheduler.platform() ).subscribe( d -> {
			System.out.println( "CPU  " + index + "  " + +d );
			chartSeries.getData().get( 0 ).setYValue( d );
		} );
		return chartSeries;
	}

	private static Node createPieChart()
	{
		ISystemProvider provider = SystemProvider.getInstance();
		PieChart pieChart = new PieChart();
		pieChart.setLegendVisible( true );
		pieChart.setLegendSide( Side.BOTTOM );
		pieChart.setTitle( "RAM Overview" );
		pieChart.setLabelsVisible( false );

		ObservableList< Data > valueList = FXCollections.observableArrayList();
		Data memoryInUse = new Data( "Memory in use", 0 );
		Data memoryAvailable = new Data( "available Memory", 0 );
		valueList.add( memoryAvailable );
		valueList.add( memoryInUse );

		provider.getAvailableMemory().observeOn( JavaFxScheduler.platform() ).subscribe( x -> {
			memoryInUse.setPieValue( provider.getTotalMemory() - x );
			memoryAvailable.setPieValue( x );
		} );

		pieChart.setData( valueList );

		return pieChart;
	}

	private static Node createProcessList()
	{
		ISystemProvider provider = SystemProvider.getInstance();
		ListView< String > listview = new ListView<>();
		provider.getProcesses().observeOn( JavaFxScheduler.platform() )
				.subscribe( x -> listview.setItems( FXCollections.observableArrayList( x ) ) );

		return listview;
	}
}
