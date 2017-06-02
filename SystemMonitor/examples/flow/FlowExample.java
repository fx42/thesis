package flow;

import io.reactivex.Flowable;

public class FlowExample
{
public static void main(String[] args)
{
	Flowable<String> flowing = Flowable.just("Java", "Kotlin", "Scala");
	flowing.onBackpressureBuffer(1).subscribe(s -> {System.out.println(s);},
			t -> {t.printStackTrace();},
			() -> {System.out.println("finish");} );
}
}
