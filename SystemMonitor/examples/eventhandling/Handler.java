package eventhandling;

import javafx.event.Event;
import javafx.event.EventHandler;

public class Handler implements EventHandler< Event >
{
	@Override
	public void handle( Event arg0 )
	{
		System.out.println( "Event of type: " + arg0.getEventType() );
		arg0.consume();
	}
}
