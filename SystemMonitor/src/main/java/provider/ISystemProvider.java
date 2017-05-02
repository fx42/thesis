package provider;

import java.util.Map;

import io.reactivex.Observable;

public interface ISystemProvider
{
	Map< String, Observable< Double > > getCpuUsage();

	int getCpuAmount();

	Observable< Long > getAvailableMemory();

	long getTotalMemory();

}
