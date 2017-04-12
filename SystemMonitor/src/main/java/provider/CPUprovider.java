package provider;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class CPUprovider
{
	private static SystemInfo sysinf = new SystemInfo();
	private static HardwareAbstractionLayer hw = sysinf.getHardware();
	private static CentralProcessor processor = hw.getProcessor();
	private static GlobalMemory ram = hw.getMemory();

	public static ObservableMap< String, Double > valuesAsPercent = FXCollections.observableHashMap();

	public static ObservableMap< String, Double > getCPUusage()
	{
		for ( int i = 0; i < processor.getProcessorCpuLoadBetweenTicks().length; i++ )
		{
			valuesAsPercent.put( "CPU " + i, processor.getProcessorCpuLoadBetweenTicks()[ i ] * 100 );
		}
		return valuesAsPercent;
	}
}
