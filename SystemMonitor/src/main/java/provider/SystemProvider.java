package provider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem.ProcessSort;

public class SystemProvider implements ISystemProvider
{
	private static SystemProvider instance;

	private final SystemInfo sysinfo;
	private final HardwareAbstractionLayer hw;
	private final CentralProcessor processor;
	private final GlobalMemory memory;
	private final int cpuAmount;

	private SystemProvider()
	{
		sysinfo = new SystemInfo();
		hw = sysinfo.getHardware();
		processor = hw.getProcessor();
		memory = hw.getMemory();
		cpuAmount = processor.getLogicalProcessorCount();
	}

	public static SystemProvider getInstance()
	{
		if ( instance == null )
		{
			instance = new SystemProvider();
		}
		return instance;
	}

	@Override
	public List< Observable< Double > > fetchCpuValues()
	{
		List< Observable< Double > > resultList = new ArrayList<>();
		for ( int i = 0; i < cpuAmount; i++ )
		{
			int j = i;
			resultList.add( Observable.interval( 1, TimeUnit.SECONDS, Schedulers.computation() )
					.map( tmp -> processor.getProcessorCpuLoadBetweenTicks()[ j ] ) );
		}
		return resultList;
	}

	@Override
	public int getCpuAmount()
	{
		return this.cpuAmount;
	}

	@Override
	public Observable< Long > getAvailableMemory()
	{
		return Observable.interval( 1, TimeUnit.SECONDS, Schedulers.computation() ).map( i -> memory.getAvailable() );
	}

	@Override
	public long getTotalMemory()
	{
		return this.memory.getTotal();
	}

	@Override
	public Observable< List< String > > getProcesses()
	{

		Function< OSProcess[], List< String > > func = new Function< OSProcess[], List< String > >()
		{
			@Override
			public List< String > apply( OSProcess[] t ) throws Exception
			{
				List< String > list = new ArrayList<>();

				for ( int i = 0; i < t.length; i++ )
				{
					list.add( t[ i ].getProcessID() + " : " + t[ i ].getName() );
				}
				return list;
			}
		};
		return Observable.interval( 1, TimeUnit.SECONDS, Schedulers.computation() )
				.map( x -> sysinfo.getOperatingSystem().getProcesses( 16, ProcessSort.CPU ) ).map( func );
	}
}
