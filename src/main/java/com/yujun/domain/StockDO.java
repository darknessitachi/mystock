package com.yujun.domain;

import com.yujun.util.Money;

public class StockDO {
	private String zqCode;	//֤ȯ����
	private String zqName;	//֤ȯ����
	private long amount; // ��Ʊ����
	private Money avaPrice; // �ɱ��۸�
	public StockDO() {
		
	}
	public StockDO(String zqCode,long amount,Money avaPrice) {
		this.zqCode 	= zqCode;
		this.amount 	= amount;
		this.avaPrice 	= avaPrice;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public Money getTotalValue() {
		return avaPrice.multiply(amount);
	}
	public Money getAvaPrice() {
		return avaPrice;
	}
	public void setAvaPrice(Money avaPrice) {
		this.avaPrice = avaPrice;
	}
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
	public String toString() {
		return "zqCode : " + zqCode + ",zqName : " + zqName + ",amount : " + amount
				+ ",avaPrice : " + avaPrice;
	}
}
