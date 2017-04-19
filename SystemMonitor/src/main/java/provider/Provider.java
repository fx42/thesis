package provider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart.Data;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class Provider
{
	private static SystemInfo sysinf = new SystemInfo();
	private static HardwareAbstractionLayer hw = sysinf.getHardware();
	private static CentralProcessor processor = hw.getProcessor();
	private static GlobalMemory ram = hw.getMemory();

	public static ObservableList< Data< String, Number > > cpuUsage = FXCollections.observableArrayList();
	public static ObservableList< PieChart.Data > ramUsage = FXCollections.observableArrayList();
	public static ObservableList< String > activeProcesses = FXCollections.observableArrayList();
	public static Observable< Long > tick = Observable.interval( 10, TimeUnit.MILLISECONDS )
			.subscribeOn( Schedulers.computation() );

	public static void fetchCPUdata()
	{
		for ( int i = 0; i < processor.getLogicalProcessorCount(); i++ )
		{
			cpuUsage.add( new Data<>( "CPU" + i, 0 ) );
		}

		Observable.interval( 10, TimeUnit.MILLISECONDS ).subscribeOn( Schedulers.computation() )
				.map( i -> processor.getProcessorCpuLoadBetweenTicks() ).retry().observeOn( JavaFxScheduler.platform() )
				.subscribe( s -> {
					for ( int i = 0; i < cpuUsage.size(); i++ )
					{
						cpuUsage.get( i ).setYValue( s[ i ] * 100 );
					}
				} );
	}

	public static void fetchRAMdata()
	{
		ramUsage.add( new PieChart.Data( "available", 0 ) );
		ramUsage.add( new PieChart.Data( "in use", 0 ) );

		Observable.interval( 10, TimeUnit.MILLISECONDS ).subscribeOn( Schedulers.computation() ).retry()
				.observeOn( JavaFxScheduler.platform() ).subscribe( s -> {
					ramUsage.get( 0 ).setPieValue( ram.getAvailable() );
					ramUsage.get( 1 ).setPieValue( ram.getTotal() - ram.getAvailable() );
				} );
	}

	// TODO
	public static void fetchActiveProcesses()
	{
		// Observable.interval( 10, TimeUnit.MILLISECONDS,
		// JavaFxScheduler.platform() )
		// .map( s -> Arrays.asList( sysinf.getOperatingSystem().getProcesses(
		// 32, ProcessSort.CPU ) ).stream() ).subscribe(s -> s.)

	}

	public static void fetchCPUdata_2()
	{
		for ( int i = 0; i < processor.getLogicalProcessorCount(); i++ )
		{
			cpuUsage.add( new Data<>( "CPU" + i, 0 ) );
		}

		// Observable.interval( 10, TimeUnit.MILLISECONDS ).subscribeOn(
		// Schedulers.computation() )
		// .map( i -> processor.getProcessorCpuLoadBetweenTicks() ).flatMap(
		// Observable::fromArray );

	}
}
