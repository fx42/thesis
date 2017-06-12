package cold;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class ColdObs
{
	public static void main( String[] args ) throws InterruptedException
	{
		System.out.println( "--------Cold Observable--------" );
		Observable< Long > obs = Observable.interval( 1, TimeUnit.SECONDS );
		obs.subscribe( s -> System.out.println( s ) );
		Thread.sleep( 1000 );
		obs.subscribe( s -> System.out.println( s ) );
		Thread.sleep( 5000 );
	}
}
