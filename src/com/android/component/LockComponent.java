package com.android.component;

import java.util.concurrent.locks.ReentrantLock;

import com.googlecode.androidannotations.annotations.EBean;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
@EBean
public class LockComponent {

	public static final ReentrantLock LOCKER = new ReentrantLock();

}
