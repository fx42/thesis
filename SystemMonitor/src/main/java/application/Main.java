package application;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import provider.CPUprovider;

public class Main extends Application
{

	@Override
	public void start( Stage primaryStage ) throws Exception
	{
		VBox root = new VBox();
		Label label = new Label( "Verfügbarer Arbeitspeicher" );
		Label ram = new Label();

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis( 0, 100, 1 );
		final BarChart< String, Number > bc = new BarChart< String, Number >( xAxis, yAxis );
		bc.setTitle( "CPU Overview" );
		xAxis.setLabel( "CPU" );
		yAxis.setLabel( "Usage" );
		yAxis.setAutoRanging( false );

		Series< String, Number > seriesCPU0 = new XYChart.Series<>();
		Data< String, Number > dataCPU0 = new Data< String, Number >( "CPU0", 0 );

		Observable.interval( 4, TimeUnit.MILLISECONDS, JavaFxScheduler.platform() ).retry()
				.map( i -> CPUprovider.getCPUusage() );

		JavaFxObservable.emitOnChanged( CPUprovider.valuesAsPercent ).map( s -> s.entrySet() ).retry().subscribe( s -> {
			seriesCPU0.getData()
					.add( new Data< String, Number >( s.iterator().next().getKey(), s.iterator().next().getValue() ) );
		} );

		bc.setCategoryGap( 10 );
		bc.getData().addAll( seriesCPU0 );
		root.getChildren().setAll( label, ram, bc );
		primaryStage.setScene( new Scene( root, 800, 600 ) );
		primaryStage.show();

	}

}
