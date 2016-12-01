package com.yujun.domain;

import com.yujun.util.Money;


/**
 * �ʽ��
 * @author yujun
 *
 */
public class FundPoolDO {
	/**
	 * ����ֵ
	 */
	private Money totalValue;
	
	/**
	 * ��Ʊ��ֵ
	 */
	private Money stockValue;
	
	/**
	 * �������
	 */
	private Money availableFunds;

	public Money getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Money totalValue) {
		this.totalValue = totalValue;
	}

	public Money getStockValue() {
		return stockValue;
	}

	public void setStockValue(Money stockValue) {
		this.stockValue = stockValue;
	}

	public Money getAvailableFunds() {
		return availableFunds;
	}

	public void setAvailableFunds(Money availableFunds) {
		this.availableFunds = availableFunds;
	}

	public String toString() {
		return "totalValue : " + totalValue + ",stockValue : " + stockValue + ",availableFunds : " + availableFunds;
	}
	
}
