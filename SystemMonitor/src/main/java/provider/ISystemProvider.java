package provider;

import java.util.List;

import io.reactivex.Observable;

public interface ISystemProvider
{
	List< Observable< Double > > fetchCpuValues();

	int getCpuAmount();

	Observable< Long > getAvailableMemory();

	long getTotalMemory();

	Observable< List< String > > getProcesses();
}
