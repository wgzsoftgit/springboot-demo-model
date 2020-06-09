package com.demomodel.query.Asyncquery;

import java.util.Random;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TaskComponentImpl implements TaskComponent {
	public static Random random = new Random();

	@Override
	@Async
	public void test1() throws InterruptedException {
		System.out.println("开始做任务一");
		long start = System.currentTimeMillis();
		Thread.sleep(random.nextInt(10000));
		long end = System.currentTimeMillis();
		System.out.println("完成任务一，耗时：" + (end - start) + "毫秒");

	}

	@Override
	@Async
	public void test2() throws InterruptedException {
		System.out.println("开始做任务二");
		long start = System.currentTimeMillis();
		Thread.sleep(random.nextInt(10000));
		long end = System.currentTimeMillis();
		System.out.println("完成任务二，耗时：" + (end - start) + "毫秒");

	}

	@Override
	@Async
	public void test3() throws InterruptedException {
		System.out.println("开始做任务三");
		long start = System.currentTimeMillis();
		Thread.sleep(random.nextInt(30000));
		long end = System.currentTimeMillis();
		System.out.println("完成任务三，耗时：" + (end - start) + "毫秒");

	}

}