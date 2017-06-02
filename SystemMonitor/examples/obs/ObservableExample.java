package obs;

import io.reactivex.Observable;

public class ObservableExample
{
	public static void main(String[] args)
	{
		Observable<String> obs0 = Observable.just("Java", "Kotlin", "Scala");
		obs0.subscribe(s -> {System.out.println(s);},
				t -> {t.printStackTrace();},
				() -> {System.out.println("finish");} );
	}
	
}
