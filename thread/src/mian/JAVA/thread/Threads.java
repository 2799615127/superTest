package thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Threads implements Runnable{
	private long start;
	private long end;
	/***
	 * 1、volatile关键词：用来对共享变量的访问进行同步，上一次写入操作的结果对下一次读取操作是肯定可见的。（在写入volatile变量值之后，CPU缓存中的内容会被写回内存；在读取volatile变量时，CPU缓存中的对应内容会被置为失效，重新从主存
	 * 中进行读取），volatile不
	 * 使用锁，性能优于synchronized
	 * 关键词。
	 */
	/***
	 * final关键词声明的域的值只能被初始化一次，
	 * 一般在构造方法中初始化。。（在多线程开发中，
	 * final域通常用来实现不可变对象）
	当对象中的共享变量的值不可能发生变化时，在多线程中
	也就不需要同步机制来进行处理，故在多线程开发中应尽可
	能使用不可变对象。
	另外，在代码执行时，final域的值可以被保存在寄存器中
	，而不用从主存中频繁重新读取。
	 */
	private volatile static int i;
	private SimpleDateFormat sm= 
			new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private Threads(){
		
	}
	public Threads(String start,String end){
		try {
			this.start=sm.parse(start).getTime();
			this.end=sm.parse(end).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public  boolean add(){
		 i++;
		 synchronized (Threads.class) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("插入数据库300数据"+i);
			start+=1000;
			System.out.println(sm.format(new Date(start)));
			//System.out.println(Thread.currentThread().getPriority());
			if(start>=end){
				return true;
			}
			return false;
			
		 }
	}
	
	@Override
	public void run() {
		//Thread.currentThread().setPriority(i);
		while(true){
			try {
				Thread.sleep(1000);
				boolean exit= add();
				if(exit){
					System.out.println("结束了");
					return;
				}
			} catch (InterruptedException e) {
				System.out.println("线程发生异常停止");
				return;
			}
		}
	}
	public void setI(int i){
		this.i=i;
		
	}
	
	public static void main(String[] args) {
		ExecutorService fixedThreadPool =
				Executors.newFixedThreadPool(1000);
		for(int i=1;i<=1000;i++){
			Threads threads=new Threads("2018-05-27 12:09:00","2018-05-27 12:09:02");
			fixedThreadPool.execute(threads);
		}
		fixedThreadPool.shutdown();
	  }
	
}
