package flow;

import io.reactivex.Flowable;
import io.reactivex.subscribers.DefaultSubscriber;

public class FlowExample
{
	public static void main( String[] args )
	{
		Flowable< String > flowing = Flowable.just( "Java", "Kotlin", "Scala" );
		flowing.onBackpressureBuffer( 3 ).subscribe( s -> {
			System.out.println( s );
		}, t -> {
			t.printStackTrace();
		}, () -> {
			System.out.println( "finish" );
		} );

		DefaultSubscriber< String > sub = new DefaultSubscriber< String >()
		{
			@Override
			public void onStart()
			{
				request( 1 );
			}

			@Override
			public void onNext( String t )
			{
				System.out.println( t );
				request( 1 );
			}

			@Override
			public void onError( Throwable t )
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete()
			{
				// TODO Auto-generated method stub

			}
		};

		flowing.subscribe( sub );
	}
}
