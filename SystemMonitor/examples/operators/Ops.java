package operators;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class Ops
{
	public static void main( String[] args ) throws InterruptedException
	{
		System.out.println( "--------Filter-------" );
		Observable< Integer > obs0 = Observable.just( 2, 30, 22, 5, 60, 1 );
		Predicate< Integer > p = new Predicate< Integer >()
		{
			@Override
			public boolean test( Integer arg0 ) throws Exception
			{
				return ( arg0 > 10 );
			}
		};
		obs0.filter( p ).subscribe( x -> System.out.println( x ) );

		System.out.println( "--------Map-------" );
		Observable< Integer > obs1 = Observable.just( 1, 2, 3 );
		obs1.map( x -> x * 10 ).subscribe( x -> System.out.println( x ) );

		System.out.println( "--------Zip-------" );
		Observable< Integer > obs4 = Observable.just( 1, 2, 3, 4, 5 );
		Observable< String > obs5 = Observable.just( "B", "A", "C", "D" );
		Observable.zip( obs4, obs5, ( x, y ) -> y + x.intValue() ).subscribe( z -> System.out.println( z ) );

		System.out.println( "--------Merge-------" );
		Observable< Integer > obs2 = Observable.just( 20, 40, 60, 80, 100 );
		Observable< Integer > obs3 = Observable.interval( 10, TimeUnit.MICROSECONDS ).map( i -> 1 );
		Observable.merge( obs2, obs3 ).subscribe( x -> System.out.println( x ) );
	}

}
