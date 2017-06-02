package single;

import io.reactivex.Single;

public class SingleExample
{
	public static void main(String[] args)
	{
		Single<String> single = Single.just("Covfefe");
		single.map(s -> s.length()).subscribe(
				element -> {System.out.println("Length: " + element);},
				error -> {error.printStackTrace();}
				);
	}	
}
