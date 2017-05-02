package application;

import java.util.Map;
import java.util.Map.Entry;

import io.reactivex.Observable;
import io.reactivex.Observer;
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
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import provider.SystemProvider;

public class Main2 extends Application
{

	@Override
	public void start( Stage primaryStage ) throws Exception
	{
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
		primaryStage.setResizable( true );
		primaryStage.setScene( new Scene( root, 1200, 400 ) );
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

		Map< String, Observable< Double > > resultMap = provider.getCpuUsage();
		ObservableList< XYChart.Data< String, Number > > listOfData = FXCollections.observableArrayList();
		for ( Entry< String, Observable< Double > > entry : resultMap.entrySet() )
		{
			listOfData.add( new XYChart.Data< String, Number >( entry.getKey(),
					entry.getValue().blockingNext().iterator().next() ) );

		}

		Series< String, Number > chartSeries = new XYChart.Series<>();
		JavaFxObservable.emitOnChanged( listOfData ).observeOn( JavaFxScheduler.platform() ).retry().map( s -> {
			System.out.println( s );
			return s;
		} ).subscribe( s -> chartSeries.setData( s ) );
		bc.getData().add( chartSeries );
		return bc;

	}

	private static Observer< Double > getCpuObserver( int index )
	{
		SystemProvider provider = SystemProvider.getInstance();
		return new Observer< Double >()
		{
			@Override
			public void onSubscribe( Disposable d )
			{
				// Not necessary

			}

			@Override
			public void onNext( Double t )
			{
				provider.getCpuUsage().get( index );
			}

			@Override
			public void onError( Throwable e )
			{
				e.printStackTrace();

			}

			@Override
			public void onComplete()
			{
				// Should never complete

			}
		};
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
}
