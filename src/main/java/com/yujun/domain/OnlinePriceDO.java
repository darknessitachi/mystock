package com.yujun.domain;

import com.yujun.util.Money;

/**
 * ��Ʊʵʱ����
 * @author yujun
 *
 */
public class OnlinePriceDO {
	/**
	 * ֤ȯ����
	 */
	private String zqCode;
	/**
	 * ֤ȯ����
	 */
	private String zqName;
	
	/**
	 * �������̼۸�
	 */
	private Money yPrice;
	
	/**
	 * ���տ��̼۸�
	 */
	private Money nsPrice;
	
	/**
	 * ��ǰ�۸�
	 */
	private Money nowPrice;
	
	/**
	 * 
	 * @return ʱ��
	 */
	private String date;
	
	public String getZqCode() {
		return zqCode;
	}

	public void setZqCode(String zqCode) {
		this.zqCode = zqCode;
	}

	public String getZqName() {
		return zqName;
	}

	public void setZqName(String zqName) {
		this.zqName = zqName;
	}

	public Money getyPrice() {
		return yPrice;
	}

	public void setyPrice(Money yPrice) {
		this.yPrice = yPrice;
	}

	public Money getNsPrice() {
		return nsPrice;
	}

	public void setNsPrice(Money nsPrice) {
		this.nsPrice = nsPrice;
	}

	public Money getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(Money nowPrice) {
		this.nowPrice = nowPrice;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String toString() {
		return "date"+ date + "zqCode : " + zqCode + ",zqName : " + zqName + ",yPrice : " + yPrice
				+ ",nsPrice : " + nsPrice + ",nowPrice : " + nowPrice;
	}
	
}
