package com.yujun.util;

import java.util.List;

public class DevitionUtil {
	
	 //�@ȡƽ��ֵ
    public static double getAverage(int[] price){
        int sum = 0;
        for(int i = 0;i < price.length;i++){
            sum += price[i];
        }
        return (double)(sum / (price.length));
    }
    
    //ɾ��������ֵ��
    public static double getAverageOther(int[] price){
        int sum = 0;
        int lowest = price[0];
        int highest = price[0];
        
        for(int i = 0;i < price.length;i++){
        	if(lowest > price[i]) lowest = price[i];
        	if(highest < price[i]) highest = price[i];
            sum += price[i];
        }
        return (double)((sum - lowest - highest)/ (price.length-2));
    }
    
    //ĩ�ռ�Ȩƽ����
    public static double endWeightedAverage(int[] price){
        int sum = 0;
        for(int i = 1 ;i <price.length;i++){
            sum += price[i];
        }
        sum += 2*price[0];
        return (double)(sum / (price.length+1));
    }
    
    //���ݼ�Ȩƽ����
    public static double jieTiAverage(int[] price){
        int sum = 0;
        int count = 0;
        for(int i = 0 ;i <price.length;i++){
            sum += price[i] * (price.length-i);
            count+=price.length-i;
        }
        return (sum+0.0d) /count;
    }
   
    //�@ȡ�˜ʲ�
    public static double getStandardDevition(int[] price){
        double sum = 0;
        for(int i = 0;i < price.length;i++){
            sum += Math.sqrt(((double)price[i] -getAverage(price)) * (price[i] -getAverage(price)));
        }
        return (sum / (price.length - 1));
    }

    public static void main(String[] args) throws InterruptedException {
    	int[] price =  {7,17,22,10,2};
    	System.out.print(jieTiAverage(price));
	}
    
    
}
