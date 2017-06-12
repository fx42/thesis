package scheduling;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SchedulerExample
{
	public static void main( String[] args ) throws InterruptedException
	{
		Observable< String > obs0 = Observable.just( "Java", "Kotlin", "Scala" );
		// Aufruf 1
		obs0.subscribeOn( Schedulers.newThread() ).map( foo ).subscribe( s -> {
			System.out.println( s + ":	" + Thread.currentThread().getName() );
		} );
		// Aufruf 2
		obs0.subscribeOn( Schedulers.newThread() ).map( foo ).subscribeOn( Schedulers.newThread() ).subscribe( s -> {
			System.out.println( s + ":	" + Thread.currentThread().getName() );
		} );

		// Aufruf 3
		obs0.map( foo ).observeOn( Schedulers.newThread() ).subscribe( s -> {
			System.out.println( s + ":	" + Thread.currentThread().getName() );
		} );
		// Aufruf 4
		obs0.observeOn( Schedulers.newThread() ).map( foo ).subscribe( s -> {
			System.out.println( s + ":	" + Thread.currentThread().getName() );
		} );
		// Aufruf 5
		obs0.observeOn( Schedulers.newThread() ).map( foo ).observeOn( Schedulers.newThread() ).subscribe( s -> {
			System.out.println( s + ":	" + Thread.currentThread().getName() );
		} );

		Thread.sleep( 5000 );
	}

	static Function< String, String > foo = new Function< String, String >()
	{

		@Override
		public String apply( String t ) throws Exception
		{
			System.out.println( "mapping:  " + Thread.currentThread().getName() );
			return t.toLowerCase();
		}
	};
}
