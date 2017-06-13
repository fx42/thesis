package scheduling;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SchedulerExample
{
	public static void main( String[] args ) throws InterruptedException
	{
		Observable< String > observable = Observable.just( "Java", "Kotlin", "Scala" );
		// Aufruf 1
		observable.subscribeOn( Schedulers.newThread() ).map( mapping ).subscribe( s -> {
			System.out.println( s + ":	" + Thread.currentThread().getName() );
		} );
		// Aufruf 2
		observable.subscribeOn( Schedulers.newThread() ).map( mapping ).subscribeOn( Schedulers.newThread() )
				.subscribe( s -> {
					System.out.println( s + ":	" + Thread.currentThread().getName() );
				} );

		// Aufruf 3
		observable.map( mapping ).observeOn( Schedulers.newThread() ).subscribe( s -> {
			System.out.println( s + ":	" + Thread.currentThread().getName() );
		} );
		// Aufruf 4
		observable.observeOn( Schedulers.newThread() ).map( mapping ).subscribe( s -> {
			System.out.println( s + ":	" + Thread.currentThread().getName() );
		} );
		// Aufruf 5
		observable.observeOn( Schedulers.newThread() ).map( mapping ).observeOn( Schedulers.newThread() ).subscribe( s -> {
			System.out.println( s + ":	" + Thread.currentThread().getName() );
		} );

		Thread.sleep( 5000 );
	}

	static Function< String, String > mapping = new Function< String, String >()
	{

		@Override
		public String apply( String t ) throws Exception
		{
			System.out.println( "mapping:  " + Thread.currentThread().getName() );
			return t.toLowerCase();
		}
	};
}
