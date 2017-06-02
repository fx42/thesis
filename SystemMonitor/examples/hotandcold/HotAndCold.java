package hotandcold;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class HotAndCold 
{
	public static void main(String[] args) throws InterruptedException
	{	
		System.out.println("--------Cold Observable---------------------");
		Observable<Long> obs = Observable.interval(1, TimeUnit.SECONDS);
		Disposable d1 = obs.map(tick -> tick*2).subscribe(s -> System.out.println(s));
		Thread.sleep(1000);
		Disposable d2 = obs.map(tick -> tick*20).subscribe(s -> System.out.println(s));
		Thread.sleep(5000);
		d1.dispose();
		d2.dispose();
		System.out.println("--------Hot Observable---------------------");
		Observable<Long> obs2 = Observable.interval(1, TimeUnit.SECONDS).publish().autoConnect();
		obs2.map(tick -> tick*2).subscribe(s -> System.out.println(s));
		Thread.sleep(1000);
		obs2.map(tick -> tick*20).subscribe(s -> System.out.println(s));
		Thread.sleep(5000);
	}
}
