package subject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class SubjectExample
{
	public static void main(String[] args)
	{
		Observable<String> obs0 = Observable.just("Java", "Kotlin", "Scala");
		Subject<String> subj = PublishSubject.create();
		subj.map(s -> s.toUpperCase()).subscribe(s -> {System.out.println(s);});
		obs0.subscribe(subj);
	}
}
