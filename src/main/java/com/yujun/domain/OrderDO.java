package com.yujun.domain;

import com.yujun.util.Money;

/**
 * ί��
 * @author yujun
 *
 */
public class OrderDO {
	private String zqCode;		//֤ȯ����
	private String zqName;		//֤ȯ����
	private String date;		//ί��ʱ��
	private boolean isBuy;		//����
	private Money price;		//ί�м۸�
	private	long amount;		//ί������
	private String orderId;		// ����id
	private int status;			// ״̬
	public static int SUCCESS =1;	// �ѳ�
	public static int CANCLE  =2;	// �ѳ�
	public static int WAITING =3;	// �ѱ�
	public static int PART_SUCCESS =4;	// ����
	public static int PART_CANCLE =5;	// ����
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isBuy() {
		return isBuy;
	}
	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}
	public Money getPrice() {
		return price;
	}
	public void setPrice(Money price) {
		this.price = price;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String toString() {
		return "zqCode : " + zqCode + ",zqName : " + zqName + ",date : " + date
				+ ",isBuy : " + isBuy + ",status : " + status + ",price : " + price + ",amount : "
				+ amount + ",orderId : " + orderId;
	}
}
