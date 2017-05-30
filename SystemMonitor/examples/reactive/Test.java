package reactive;

import io.reactivex.Observable;

public class Test
{

	public static void main( String[] args )
	{
		Observable< Integer > o = Observable.create( s -> {
			s.onNext( 1 );
			s.onNext( 2 );
			s.onNext( 3 );
			s.onComplete();
		} );

		o.map( i -> "Number" + i ).subscribe( s -> System.out.println( s ) );

	}

}
