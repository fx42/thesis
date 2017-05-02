package provider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class NewProvider
{
	private static HardwareAbstractionLayer hw = new SystemInfo().getHardware();
	private static CentralProcessor processor = hw.getProcessor();
	private static GlobalMemory ram = hw.getMemory();
	private static Observable< Long > tick = Observable
			.interval( 1_000_000 / 60, TimeUnit.MICROSECONDS, Schedulers.computation() ).share();

	public static List< Observable< Double > > fetchingCPUusage()
	{
		List< Observable< Double > > list = new ArrayList<>();
		list.add( tick.subscribeOn( Schedulers.computation() ).flatMap( it -> cpu0( 0 ) ) );
		list.add( tick.subscribeOn( Schedulers.computation() ).flatMap( it -> cpu0( 1 ) ) );
		list.add( tick.subscribeOn( Schedulers.computation() ).flatMap( it -> cpu0( 2 ) ) );
		list.add( tick.subscribeOn( Schedulers.computation() ).flatMap( it -> cpu0( 3 ) ) );
		return list;
	}

	public static Observable< Double > cpu0( int cpu )
	{
		return Observable.just( test().get( cpu ) );
	}

	public static List< Double > test()
	{
		List< Double > result = new ArrayList<>();
		for ( int i = 0; i < processor.getLogicalProcessorCount(); i++ )
		{
			result.add( processor.getProcessorCpuLoadBetweenTicks()[ i ] );
		}

		return result;

	}
}
