package com.yujun.util;


public class MathUtil {
	/* **
	 * �õ���Ҫ����Ĺ�Ʊ����
	 */
	public static int getCount(Money price, Money total) {
		//�������룬��100��һ����λ���
		return  Math.round((total.getCent()/(float)(price.getCent() * 100)))*100;
	}
}
