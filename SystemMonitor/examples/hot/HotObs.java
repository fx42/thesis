package hot;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

public class HotObs
{
	public static void main( String[] args ) throws InterruptedException
	{
		System.out.println( "--------Hot Observable--------" );
		Observable< Long > observable = Observable.interval( 1, TimeUnit.SECONDS );
		ConnectableObservable< Long > connectable = observable.publish();
		connectable.connect();
		Thread.sleep( 5000 );
		connectable.subscribe( s -> System.out.println( s ) );
		Thread.sleep( 5000 );
		connectable.subscribe( s -> System.out.println( s ) );
		Thread.sleep( 10000 );
	}
}
