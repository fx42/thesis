package provider;

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
	private static SystemInfo sysinf = new SystemInfo();
	private static HardwareAbstractionLayer hw = sysinf.getHardware();
	private static CentralProcessor processor = hw.getProcessor();
	private static GlobalMemory ram = hw.getMemory();

	public static List< Observable< Double > > getCPUusage()
	{
		List< Observable< Double > > usage;
		List< Double > tmp;
		double[] test;

		Observable< Object > obser = Observable.interval( 1, TimeUnit.SECONDS ).subscribeOn( Schedulers.computation() )
				.map( tick -> processor.getProcessorCpuLoadBetweenTicks() );

		return null;
	}
}
