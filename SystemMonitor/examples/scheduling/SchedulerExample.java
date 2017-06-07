package scheduling;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class SchedulerExample
{
	public static void main(String[] args) throws InterruptedException
	{
		Observable<String> obs0 = Observable.just("Java", "Kotlin", "Scala");
		Predicate<String> p = new Predicate<String>()
		{

			@Override
			public boolean test(String arg0) throws Exception
			{
				return arg0.contains("a");
			}
		};
		obs0.subscribeOn(Schedulers.computation()).map(s -> s.toLowerCase()).filter(p).subscribe(s -> {System.out.println(s);});
		Thread.sleep(1000);
	}
}
