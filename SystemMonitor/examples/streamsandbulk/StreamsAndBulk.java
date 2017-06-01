package streamsandbulk;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamsAndBulk
{
	public static void main( String[] args )
	{
		List< String > asCollection = Arrays.asList( "Alpha", "Beta", "Gamma", "Delta" );
		Stream< String > asStream = asCollection.stream();
		asStream.map( s -> s.toUpperCase() ).filter( s -> s.contains( "L" ) ).forEach( s -> System.out.println( s ) );
	}
}
