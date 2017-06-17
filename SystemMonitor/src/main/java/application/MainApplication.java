package application;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sun.javafx.robot.impl.FXRobotHelper.FXRobotSceneAccessor;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
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
		hBox.setPadding( new Insets( 7, 7, 7, 7 ) );
		hBox.setAlignment( Pos.BASELINE_LEFT );
		hBox.getChildren().setAll( createBarChartV2() );

		root.setLeft( hBox );
		primaryStage.setTitle( "System Monitor" );
		primaryStage.setResizable( true );
		primaryStage.setScene( new Scene( root, 1000, 400 ) );
		primaryStage.show();

	}

	private static Node createBarChart()
	{
		SystemProvider provider = SystemProvider.getInstance();

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis( 0, 100, 1 );
		final BarChart< String, Number > bc = new BarChart< String, Number >( xAxis, yAxis );
		bc.setTitle( "CPU Overview" );
		xAxis.setLabel( "CPU" );
		yAxis.setLabel( "Usage" );
		yAxis.setAutoRanging( false );
		bc.setLegendVisible( false );
		bc.setCategoryGap( 10 );

		ObservableList< XYChart.Data< String, Number > > listOfData = FXCollections.observableArrayList();

		for ( int i = 0; i < provider.getCpuAmount(); i++ )
		{
			listOfData.add( new XYChart.Data< String, Number >( "CPU" + i, 0 ) );

		}

		List< Observable< Double > > list = provider.fetchCpuValues();
		Series< String, Number > chartSeries = new XYChart.Series<>();

		for ( int i = 0; i < list.size(); i++ )
		{
			changeOnList( listOfData, list.get( i ), i );
			System.err.println( "list: " + listOfData.toString() + ", listitem: " + list.get( i ) + "index: " + i );
			chartSeries.getData().add( listOfData.get( i ) );
		}

		bc.getData().add( chartSeries );
		return bc;

	}
	
	private static Node createBarChartV2()
	{
		SystemProvider provider = SystemProvider.getInstance();

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis( 0, 100, 1 );
		final BarChart< String, Number > bc = new BarChart< String, Number >( xAxis, yAxis );
		bc.setTitle( "CPU Overview" );
		xAxis.setLabel( "CPU" );
		yAxis.setLabel( "Usage" );
		yAxis.setAutoRanging( false );
		bc.setLegendVisible( false );
		bc.setCategoryGap( 10 );

		List< Observable< Double > > list = provider.fetchCpuValues();
		Series< String, Number > chartSeries = new XYChart.Series<>();

		for ( int i = 0; i < provider.getCpuAmount(); i++ )
		{
			int j = i;
			list.get( j ).map( x -> x.doubleValue() * 100 ).map( x -> new XYChart.Data<String, Number>( "CPU " + j, x )).observeOn( JavaFxScheduler.platform() ).subscribe(data -> {System.out.println( data ); chartSeries.getData().add( data );});
		}
		bc.getData().add( chartSeries );
		return bc;
	}

	private static void changeOnList( ObservableList< XYChart.Data< String, Number > > list, Observable< Double > value,
			int index )
	{
		Observer< Double > observer = new Observer< Double >()
		{

			@Override
			public void onSubscribe( Disposable d )
			{
				// TODO not needed

			}

			@Override
			public void onNext( Double t )
			{
				list.get( index ).setYValue( t * 100 );
				System.out.println( index + " " + t * 100 + "\n --------------------------" );
			}

			@Override
			public void onError( Throwable e )
			{
				e.printStackTrace();

			}

			@Override
			public void onComplete()
			{
				// Never reached

			}
		};
		value.observeOn( Schedulers.computation() ).retry().subscribe( observer );
	}

	private static Node createPieChart()
	{
		SystemProvider provider = SystemProvider.getInstance();
		// Piechart for RAM usage must be created. Fetching of data get started
		PieChart pieChart = new PieChart();
		pieChart.setLegendVisible( true );
		pieChart.setLegendSide( Side.BOTTOM );
		pieChart.setTitle( "RAM Overview" );
		pieChart.setLabelsVisible( false );

		ObservableList< Data > valueList = FXCollections.observableArrayList();
		Data memoryInUse = new Data( "Memory in use", 0 );
		Data memoryAvailable = new Data( "available Memory", 0 );

		Observer< Long > sub = new Observer< Long >()
		{

			@Override
			public void onNext( Long t )
			{
				System.out.println( "set memory values value" );
				valueList.get( 0 ).setPieValue( t.doubleValue() );
				valueList.get( 1 ).setPieValue( provider.getTotalMemory() - t );
			}

			@Override
			public void onError( Throwable t )
			{
				t.printStackTrace();

			}

			@Override
			public void onComplete()
			{
				// Should never complete

			}

			@Override
			public void onSubscribe( Disposable d )
			{
				// Not necessary

			}
		};

		valueList.add( memoryAvailable );
		valueList.add( memoryInUse );
		provider.getAvailableMemory().observeOn( Schedulers.computation() ).retry().subscribe( sub );
		JavaFxObservable.emitOnChanged( valueList ).observeOn( JavaFxScheduler.platform() ).retry().map( s -> {
			System.out.println( s );
			return s;
		} ).subscribe( s -> pieChart.setData( s ) );
		return pieChart;
	}

	private static Node createProcessList()
	{
		SystemProvider provider = SystemProvider.getInstance();
		ListView< String > listview = new ListView<>();

		provider.getProcesses().observeOn( JavaFxScheduler.platform() ).retry()
				.subscribe( x -> listview.setItems( FXCollections.observableArrayList( x ) ) );

		return listview;
	}
}
