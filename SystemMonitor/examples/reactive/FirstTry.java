package reactive;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class FirstTry extends Application
{

	// public static void main(String[] args)
	// {
	// //Prints all emissions -> counting from 1 to 5
	// Observable<Integer> source = Observable.just(1,2,3,4,5);
	// source.subscribe(i -> System.out.println(i),e -> e.printStackTrace(), ()
	// -> System.out.println("Done"));
	//
	// //Maps all emissions to upper case, filters if emission contains a
	// capital A and prints the result
	// Observable<String> source2 =
	// Observable.just("Alpha","Beta","Gamma","Delta", "Epsilon");
	// source2.map(s -> s.toUpperCase()).filter(s ->
	// s.contains("A")).subscribe(s -> System.out.println(s) );
	//
	// }

	// // Hot Observable. With pressing a number in range from 0-9 the number
	// will
	// // geht highlighted
	// @Override
	// public void start(Stage primaryStage) throws Exception
	// {
	// VBox vBox = new VBox();
	//
	// ListView<String> listView = new ListView<>();
	//
	// for (int i = 0; i <= 9; i++)
	// {
	// listView.getItems().add(String.valueOf(i));
	// }
	//
	// JavaFxObservable.eventsOf(listView,
	// KeyEvent.KEY_TYPED).map(KeyEvent::getCharacter)
	// .filter(s -> s.matches("[0-9]")).subscribe(s ->
	// listView.getSelectionModel().select(s));
	//
	// vBox.getChildren().add(listView);
	// primaryStage.setScene(new Scene(vBox));
	// primaryStage.show();
	//
	// }

	// Get events from mouse movement. When the mouse is moved within the red
	// rectangle the coursor position is represented by the label
	// @Override
	// public void start( Stage stage ) throws Exception
	// {
	//
	// VBox vBox = new VBox();
	//
	// Label positionLabel = new Label();
	// Rectangle rectangle = new Rectangle( 200, 200 );
	// rectangle.setFill( Color.RED );
	//
	// JavaFxObservable.eventsOf( rectangle, MouseEvent.MOUSE_MOVED ).map( me ->
	// me.getX() + "-" + me.getY() )
	// .subscribe( positionLabel::setText );
	//
	// Label testlabel = new Label();
	// testlabel.setText( "rp rocks" );
	// testlabel.setTextFill( Color.GREEN );
	// vBox.getChildren().addAll( testlabel, positionLabel, rectangle );
	// stage.setScene( new Scene( vBox ) );
	// stage.show();
	// }

	// // Button and label. If the button gets clicked, the label changes the
	// // amount of clicks
	// @Override
	// public void start( Stage primaryStage ) throws Exception
	// {
	// VBox vBox = new VBox();
	// vBox.setMinWidth( 180 );
	// vBox.setMinHeight( 50 );
	// Button button1 = new Button( "push it!" );
	// Label label = new Label();
	//
	// JavaFxObservable.actionEventsOf( button1 ).map( ae -> 1 ).scan( 0, ( x, y
	// ) -> x + y )
	// .subscribe( count -> label.setText( "Has been clicked " + count + "
	// times" ) );
	// vBox.getChildren().setAll( label, button1 );
	// primaryStage.setScene( new Scene( vBox ) );
	// primaryStage.show();
	// }

	// @Override
	// public void start( Stage primaryStage ) throws Exception
	// {
	// HBox hBox = new HBox();
	// ComboBox< String > comboBox = new ComboBox<>();
	// comboBox.getItems().setAll( "Alpha", "Beta", "Gamma", "Delta", "Epsilon"
	// );
	// Label testlabel = new Label();
	//
	// JavaFxObservable.valuesOf( comboBox.valueProperty() )
	// .subscribe( value -> System.out.println( value + " was selected!" ) );
	//
	// SystemInfo info = new SystemInfo();
	// HardwareAbstractionLayer hwlayer = info.getHardware();
	// hwlayer.getComputerSystem().getManufacturer();
	//
	// testlabel.setText(
	// hwlayer.getComputerSystem().getManufacturer() + " " +
	// hwlayer.getProcessor().getSystemCpuLoad() );
	//
	// hBox.getChildren().setAll( comboBox, testlabel );
	// primaryStage.setScene( new Scene( hBox ) );
	// primaryStage.show();
	// }

	// @Override
	// public void start( Stage primaryStage ) throws Exception
	// {
	// VBox vBox = new VBox();
	// Button button = new Button( "Add" );
	// Label totalLabel = new Label();
	// TextField input = new TextField();
	//
	// // JavaFxObservable.actionEventsOf( button ).map( ae -> Integer.valueOf(
	// // input.getText() ) )
	// // .scan( 0, ( x, y ) -> x + y )
	// // .doOnError( e -> new Alert( Alert.AlertType.ERROR, e.getMessage()
	// // ).show() ).retry().subscribe( i -> {
	// // totalLabel.setText( i.toString() );
	// // input.clear();
	// // } );
	//
	// JavaFxObservable.actionEventsOf( button ).map( ae -> input.getText()
	// ).filter( s -> s.matches( "[0-9]+" ) )
	// .map( Integer::valueOf ).scan( 0, ( x, y ) -> x + y ).subscribe( i -> {
	// totalLabel.setText( i.toString() );
	// input.clear();
	// }, e -> new Alert( Alert.AlertType.ERROR, e.getMessage() ).show() );
	//
	// vBox.getChildren().setAll( button, totalLabel, input );
	// primaryStage.setScene( new Scene( vBox ) );
	// primaryStage.show();
	// }

	@Override
	public void start( Stage primaryStage ) throws Exception
	{
		VBox vBox = new VBox();
		Button buttonUp = new Button( "Up" );
		Button buttonDown = new Button( "Down" );
		Label labelCount = new Label( "0" );

		// JavaFxObservable.actionEventsOf( button ).map( ae -> Integer.valueOf(
		// input.getText() ) )
		// .scan( 0, ( x, y ) -> x + y )
		// .doOnError( e -> new Alert( Alert.AlertType.ERROR, e.getMessage()
		// ).show() ).retry().subscribe( i -> {
		// totalLabel.setText( i.toString() );
		// input.clear();
		// } );

		Observable
				.merge( JavaFxObservable.actionEventsOf( buttonUp ).map( ae -> 1 ),
						JavaFxObservable.actionEventsOf( buttonDown ).map( ae -> -1 ) )
				.scan( 0, ( x, y ) -> x + y ).subscribe( i -> labelCount.setText( i.toString() ) );

		vBox.getChildren().setAll( buttonUp, labelCount, buttonDown );
		primaryStage.setScene( new Scene( vBox ) );
		primaryStage.show();
	}
}
