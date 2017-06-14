package provider;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class SystemProviderV2
{
	private static SystemProvider instance;

	private HardwareAbstractionLayer hw;
	private CentralProcessor processor;
	private GlobalMemory memory;
	private int cpuAmount;

	private void test()
	{
		SystemInfo s = new SystemInfo();

	}
}
