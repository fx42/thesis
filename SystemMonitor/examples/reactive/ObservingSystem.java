package reactive;
// package examples;
//
// import java.util.concurrent.TimeUnit;
//
// import io.reactivex.Observable;
// import io.reactivex.rxjavafx.observables.JavaFxObservable;
// import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
// import javafx.application.Application;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.scene.Scene;
// import javafx.scene.chart.BarChart;
// import javafx.scene.chart.CategoryAxis;
// import javafx.scene.chart.NumberAxis;
// import javafx.scene.chart.XYChart;
// import javafx.scene.chart.XYChart.Data;
// import javafx.scene.chart.XYChart.Series;
// import javafx.scene.control.Label;
// import javafx.scene.layout.VBox;
// import javafx.stage.Stage;
// import oshi.SystemInfo;
// import oshi.hardware.HardwareAbstractionLayer;
//
// public class ObservingSystem extends Application
// {
// private static SystemInfo sysinf = new SystemInfo();
// private static HardwareAbstractionLayer hw = sysinf.getHardware();
//
// @SuppressWarnings( "unchecked" )
// @Override
// public void start( Stage primaryStage ) throws Exception
// {
// VBox root = new VBox();
// Label label = new Label( "Verf�gbarer Arbeitspeicher" );
// Label ram = new Label();
//
// final CategoryAxis xAxis = new CategoryAxis();
// final NumberAxis yAxis = new NumberAxis( 0, 100, 1 );
// final BarChart< String, Number > bc = new BarChart< String, Number >( xAxis,
// yAxis );
// bc.setTitle( "CPU Overview" );
// xAxis.setLabel( "CPU" );
// yAxis.setLabel( "Usage" );
// yAxis.setAutoRanging( false );
// Series< String, Number > seriesCPU0 = new XYChart.Series<>();
// Data< String, Number > dataCPU0 = new Data< String, Number >( "CPU0", 0 );
// seriesCPU0.setName( "CPU 0" );
//
// Series< String, Number > seriesCPU1 = new XYChart.Series<>();
// Data< String, Number > dataCPU1 = new Data< String, Number >( "CPU1", 0 );
// seriesCPU1.setName( "CPU 1" );
//
// Series< String, Number > seriesCPU2 = new XYChart.Series<>();
// Data< String, Number > dataCPU2 = new Data< String, Number >( "CPU2", 0 );
// seriesCPU2.setName( "CPU 2" );
//
// Series< String, Number > seriesCPU3 = new XYChart.Series<>();
// Data< String, Number > dataCPU3 = new Data< String, Number >( "CPU3", 0 );
// seriesCPU3.setName( "CPU 3" );
//
// ObservableList< Data< String, Number > > dataList =
// FXCollections.observableArrayList( dataCPU0, dataCPU1,
// dataCPU2, dataCPU3 );
//
// Observable.interval( 4, TimeUnit.MILLISECONDS, JavaFxScheduler.platform() )
// .map( i -> hw.getProcessor().getProcessorCpuLoadBetweenTicks()
// ).retry().subscribe( s -> {
// dataCPU0.setYValue( s[ 0 ] * 100 );
// dataCPU1.setYValue( s[ 1 ] * 100 );
// dataCPU2.setYValue( s[ 2 ] * 100 );
// dataCPU3.setYValue( s[ 3 ] * 100 );
// } );
//
// JavaFxObservable.emitOnChanged( dataList ).retry().subscribe( s -> {
// seriesCPU0.getData().add( dataCPU0 );
// seriesCPU0.getData().add( dataCPU1 );
// seriesCPU0.getData().add( dataCPU2 );
// seriesCPU0.getData().add( dataCPU3 );
// // seriesCPU1.getData().add( dataCPU1 );
// // seriesCPU2.getData().add( dataCPU2 );
// // seriesCPU3.getData().add( dataCPU3 );
// } );
//
// bc.setCategoryGap( 10 );
// bc.getData().addAll( seriesCPU0, seriesCPU1, seriesCPU2, seriesCPU3 );
// root.getChildren().setAll( label, ram, bc );
// primaryStage.setScene( new Scene( root, 800, 600 ) );
// primaryStage.show();
//
// }
// }
