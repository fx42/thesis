package processor;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;


public class ProcessorExample
{
	public static void main(String[] args)
	{
		Flowable<String> flow = Flowable.just("Java", "Kotlin", "Scala");
		PublishProcessor<String> proc = PublishProcessor.create();
		proc.onBackpressureBuffer(1).map(s -> s.toUpperCase()).subscribe(s -> {System.out.println(s);});
		flow.subscribe(proc);		
	}
}
