package com.yujun.calculate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.yujun.calculate.HoldingStockCal;
import com.yujun.calculate.TradeOrder;
import com.yujun.domain.OnlinePriceDO;
import com.yujun.domain.PriceDO;
import com.yujun.domain.StockDO;
import com.yujun.util.DevitionUtil;
import com.yujun.util.Money;
import com.yujun.util.TdxResultUtil;

/**
 * �����������������㷨
 * @author yujun
 *
 */
public class HighAndLowPriceCal implements TradeOrder{
	Logger log = Logger.getLogger(this.getClass());
	HoldingStockCal holdingStockCal = new HoldingStockCal();
	public Map<String,StockDO> calculate(StockDO initStockDO, StockDO hoding, OnlinePriceDO online ) {
		long[][] priceRegion 	= holdingStockCal.calStockRegion(initStockDO);
		Map<String,StockDO> result = new HashMap<String,StockDO>();
		StockDO[] byAmont = priceRegionByAmont(priceRegion,hoding);
		StockDO[] byPrice = priceRegionByPrice(priceRegion,online);
		
		StockDO low = null;
		StockDO high = null;
		
		low  = byAmont[0];
		high = byAmont[1];
		
		// ��������������ͼ۴��� ����������߼�
		// ���������ͼ��Լ������ĳɽ���������߼�
		if(low !=null && byPrice!=null&&low.getAvaPrice().getCent() > byPrice[1].getAvaPrice().getCent()) { 
			low  = byPrice[1];
		}
		
		// ��������������߼�С���ڽ���������ͼ�
		// ����������߼��Լ������ĳɽ���������ͼ�  
		if(high !=null && byPrice!=null &&high.getAvaPrice().getCent() < byPrice[0].getAvaPrice().getCent()) {	
			high = byPrice[0];
		}
		
		result.put("low", low);
		result.put("high", high);
		if(high!=null) {
			log.info("���ռ۸�����������" + high.getAvaPrice() +":" + high.getAmount());
		} else {
			log.info("���ռ۸��������޳������㷶Χ�����ֶ�����");
		}
		
		log.info("���ռ۸�����������" + low.getAvaPrice()+":" + low.getAmount());
		
		
		/*if(online.getNowPrice().greaterThan(hoding.getAvaPrice())){
			log.info("��ǰ�۸񳬹��ɱ��߼۸� ��������");
		}*/
		return result;
	}
	
	public StockDO[] priceRegionByAmont(long[][] priceRegion , StockDO hoding) {
		StockDO low = null;
		StockDO high = null;
		long highest=0;
		long lowest =0;
		long standard = rangeOfPrice(hoding.getZqCode());	
		
		int  index =-1;
		log.info("���յ�ǰ�ֹ�����" + hoding.getAmount());
		for(int i = 0 ; i<priceRegion.length  ;i++){
			if(hoding.getAmount() <= priceRegion[i][1]) {
				index = i ;
				log.info("ѡ�񵽵�������" + priceRegion[i][0] +":" +priceRegion[i][1] );
				break;
			}
		}
		
		if(index !=-1) {
			highest = priceRegion[index][0] + standard;
			lowest =  priceRegion[index][0] - standard;
			for(int i = 0 ; i<priceRegion.length  ;i++){
				if(highest > priceRegion[0][0]) {
					break;
				}
				if(highest >= priceRegion[i][0]) {
					if(i>1 && priceRegion[index-2][0] >= highest) {
						high = new StockDO(null,priceRegion[index-2][1],new Money(priceRegion[index-2][0]));
					} else if(i>=3 && priceRegion[index-4][0] <= highest) {
						high = new StockDO(null,priceRegion[index-4][1],new Money(priceRegion[index-4][0]));
					} else {
						high = new StockDO(null,priceRegion[i][1],new Money(highest));
					}
					log.info("���ֹ���������ļ۸�����������" + high.getAvaPrice() +":" + high.getAmount());
					break;
				}
			}
			
			if(high==null) {
				log.info("�������㷶Χ����߼۸���" + highest);
			} 
			
			for (int i = priceRegion.length-1; i > 0; i--) {
				if(lowest <= priceRegion[i][0]) {
					if (priceRegion[index+2][0] <= lowest) {
						low = new StockDO(null,priceRegion[index+2][1] ,new Money(priceRegion[index+2][0]));
					} else if(lowest <priceRegion[index+3][0]){
						low = new StockDO(null,priceRegion[index+4][1] ,new Money(priceRegion[index+4][0]));
					} else {
						low = new StockDO(null,priceRegion[i][1] ,new Money(lowest));
					}
					log.info("���ֹ���������ļ۸�����������" + low.getAvaPrice()+":" + low.getAmount());
					break;
				}
			}
		}
		
		StockDO[] result = new StockDO[2];
		result[0] = low;
		result[1] = high;
		
		return result;
	}
	
	public StockDO[] priceRegionByPrice(long[][] priceRegion ,OnlinePriceDO online) {
		if(online.getNowPrice().getCent() <=0) {	
			//��û����
			return null;
		}
		StockDO low = null;
		StockDO high = null;
		for(int i = priceRegion.length-2 ; i>=0  ;i--){
			if(online.getNowPrice().getCent() <= priceRegion[i][0] ) {
				high 	= new StockDO(null,priceRegion[i][1],new Money(priceRegion[i][0]));
				low 	= new StockDO(null,priceRegion[i+1][1],new Money(priceRegion[i+1][0]));
				break;
			};
		}
		
		if(low!=null && high!=null) {
			log.info("���յ�ǰ��Ʊ�۸�" + online.getNowPrice());
			log.info("���۸����ļ۸�����������" + high.getAvaPrice() +":" + high.getAmount());
			log.info("���۸����ļ۸�����������" + low.getAvaPrice()+":" + low.getAmount());
			
			StockDO[] result = new StockDO[2];
			result[0] = low;
			result[1] = high;
			return result;
		} 
		return null;
	}
	
	public long rangeOfPrice(String zqCode) {
		try {
			List<PriceDO> list = null;
			/*if(daylineMap.containsKey(zqCode)) {
			    list = daylineMap.get(zqCode);
			} else {*/
				list = TdxResultUtil.parseDayline(zqCode);
				//daylineMap.put(zqCode, list);
			//}
			int[] price = new int[7];
			String priceStr ="";
			for (int i = 1; i <= price.length; i++) {
				PriceDO beYestday = list.get(list.size() - 1 - i);
				PriceDO yestday = list.get(list.size() - i);
				price[i-1] = (int)(Math.max(Math.abs(yestday.getHighestPrice().getCent()-beYestday.getClosingPrice().getCent()),Math.abs(yestday.getLowestPrice().getCent()-beYestday.getClosingPrice().getCent())));
				priceStr +="[" +yestday.getDateStr() +"," +price[i-1]+"]";
			}
			
			/*for (int i = 0; i < list.size(); i++) {
				if(date.equals(list.get(i+1).getDate())) {
					for (int j = 0; j < price.length; j++) {
						OfflinePriceDO beYestday = list.get(i-j-1);
						OfflinePriceDO yestday = list.get(i-j );
						price[j] = (int)(Math.max(Math.abs(yestday.getHighestPrice().getCent()-beYestday.getClosingPrice().getCent()),Math.abs(yestday.getLowestPrice().getCent()-beYestday.getClosingPrice().getCent())));
						priceStr +=price[j]+",";
					}
					break;
				}
			}*/
			
			double average = DevitionUtil.getAverageOther(price);
			double standard = DevitionUtil.getStandardDevition(price);
			log.info("����ļ���ļ۸񲨶���Χ��" + priceStr);
			log.info("������Ȩ����ƽ��ֵ��" + average);
			log.info("��Ʊ������Χ��׼����" + standard);
			
			for (int i = 0; i < price.length; i++) {
				PriceDO of1 = list.get(list.size() - 1 - i);
				PriceDO of2 = list.get(list.size() - 2 - i);
				price[i] = (int)(of1.getClosingPrice().getCent()-of2.getClosingPrice().getCent());
				priceStr +=price[i]+",";
			}
			double priceStandard = DevitionUtil.getStandardDevition(price);
			log.info("��Ʊ�۸��׼����" + priceStandard);
			
			return (long)(average);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		 ThreadFactory  factory = Executors.defaultThreadFactory(); 
		 factory.newThread(new TT()).start();
		 TimeUnit.SECONDS.sleep(1);
		 factory.newThread(new TT()).start();
		 TimeUnit.SECONDS.sleep(1);
		 factory.newThread(new TT()).start();
	}
	
	 public static class TT implements Runnable {
		public void run() {
			System.out.println(this.hashCode() +":"+ lock.tryLock());
			System.out.println(this.hashCode() +":"+ lock.getHoldCount());
			System.out.println(this.hashCode() +":"+ lock.tryLock());
			System.out.println(this.hashCode() +":"+ lock.getHoldCount());
		}
	}
	 
	 static ReentrantLock lock = new ReentrantLock();
}
