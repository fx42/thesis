package scheduling;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SchedulerExample
{
	public static void main( String[] args ) throws InterruptedException
	{
		Observable< String > obs0 = Observable.just( "Java", "Kotlin", "Scala" );
		obs0.subscribeOn( Schedulers.computation() ).map( s -> s.toLowerCase() ).subscribe( s -> {
			System.out.println( s );
		} );
		Thread.sleep( 1000 );
	}
}
