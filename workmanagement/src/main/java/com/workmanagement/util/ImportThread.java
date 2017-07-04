package com.workmanagement.util;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

//更简单的方法:使用java.util.concurrent.CountDownLatch代替MyCountDown,用await()方法代替while(true){...}
//ImportThread类 
public class ImportThread extends Thread {

	private MyCountDown c;

	public ImportThread(MyCountDown c) {
		this.c = c;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + "开始...");// 打印开始标记
		
		c.countDown();// 计时器减1
		System.out.println(Thread.currentThread().getName() + "结束. 还有" + c.getCount() + " 个线程");// 打印结束标记
	}

	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName() + "开始");// 打印开始标记

		MyCountDown c = new MyCountDown(5);// 初始化countDown
		for (int ii = 0; ii < 5; ii++) {// 开threadNum个线程
			Thread t = new ImportThread(c);
			t.start();
		}
		while (true) {// 等待所有子线程执行完
			if (!c.hasNext())
				break;
		}
		System.out.println(Thread.currentThread().getName() + "结束.");// 打印结束标记
	}
}
