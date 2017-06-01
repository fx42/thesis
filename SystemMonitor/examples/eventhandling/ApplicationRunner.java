package eventhandling;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ApplicationRunner extends Application
{
	public static void main( String[] args )
	{
		Application.launch( args );
	}

	@Override
	public void start( Stage arg0 ) throws Exception
	{
		HBox root = new HBox();
		Button button = new Button( "hi, I'm button! Hover over me!" );

		button.addEventHandler( MouseEvent.ANY, new Handler() );

		root.getChildren().setAll( button );
		arg0.setScene( new Scene( root, 180, 40 ) );
		arg0.show();
	}
}
