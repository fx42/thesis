package provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class SystemProvider implements ISystemProvider
{
	private static SystemProvider instance;

	private HardwareAbstractionLayer hw;
	private CentralProcessor processor;
	private GlobalMemory memory;
	private int cpuAmount;

	private SystemProvider()
	{
		hw = new SystemInfo().getHardware();
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
	public Map< String, Observable< Double > > getCpuUsage()
	{
		Map< String, Observable< Double > > resultMap = new HashMap<>();
		for ( int i = 0; i < cpuAmount; i++ )
		{
			int j = i;
			resultMap.put( "CPU" + i, Observable.interval( 1, TimeUnit.SECONDS, Schedulers.computation() )
					.map( tmp -> processor.getProcessorCpuLoadBetweenTicks()[ j ] ) );

		}
		return resultMap;
	}

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

}
