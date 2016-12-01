package com.yujun;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.yujun.domain.FundPoolDO;
import com.yujun.domain.OnlinePriceDO;
import com.yujun.domain.OrderDO;
import com.yujun.domain.StockDO;
import com.yujun.util.Money;
import com.yujun.util.TdxResultUtil;

public class TdxClient {
	Logger log = Logger.getLogger(this.getClass());
	public interface TdxLibrary extends Library 
	{
        //�����溯��
		/**
		 * ��ͨ����ʵ��
		 */
	    public  void OpenTdx();
	    /**
	     * ��ͨ����ʵ��
	     */
	    public  void CloseTdx();
	    
	    /**
	     * 
	     * @param IP	ȯ�̽��׷�����IP
	     * @param Port	ȯ�̽��׷������˿�
	     * @param Version	����ͨ���ſͻ��˵İ汾��
	     * @param YybID		Ӫҵ�����룬�뵽��ַ http://www.chaoguwaigua.com/downloads/qszl.htm ��ѯ
	     * @param AccountNo	�����ĵ�¼�˺ţ�ȯ��һ��ʹ���ʽ��ʻ���ͻ���
	     * @param TradeAccount	�����˺ţ�һ�����¼�ʺ���ͬ. ���¼ȯ��ͨ�����������ѯ�ɶ��б��ɶ��б��ڵ��ʽ��ʺž��ǽ����ʺ�, �����ѯ���������վ���ȵ��ʴ���Ŀ
	     * @param JyPassword	��������
	     * @param TxPassword	ͨѶ����
	     * @param ErrInfo	��APIִ�з��غ�������������˴�����Ϣ˵����һ��Ҫ����256�ֽڵĿռ䡣û����ʱΪ���ַ�����
	     * @return
	     */
	    public  int Logon(String IP, short  Port, String Version, short  YybID, String AccountNo, String TradeAccount, String JyPassword, String TxPassword, byte[] ErrInfo);
	    public  void Logoff(int ClientID);
	    /**
	     * ��ѯ���ֽ�������
	     * @param ClientID 	�ͻ���ID
	     * @param Category	��ʾ��ѯ��Ϣ�����࣬0�ʽ�  1�ɷ�   2����ί��  3���ճɽ�     4�ɳ���   5�ɶ�����  6�������   7��ȯ���  8����֤ȯ
	     * @param Result	��APIִ�з��غ�Result�ڱ����˷��صĲ�ѯ����, ��ʽΪ������ݣ�������֮��ͨ��\n�ַ��ָ������֮��ͨ��\t�ָ���һ��Ҫ����1024*1024�ֽڵĿռ䡣����ʱΪ���ַ�����
	     * @param ErrInfo	ͬLogon������ErrInfo˵��
	     */
	    public  void QueryData(int ClientID, int Category,  byte[] Result,  byte[] ErrInfo);
	    /**
	     * ��ί�н���֤ȯ
	     * @param ClientID	�ͻ���ID
	     * @param Category	��ʾί�е����࣬0���� 1����  2��������  3��ȯ����   4��ȯ��ȯ   5��ȯ����  6��ȯ��ȯ
	     * @param PriceType	��ʾ���۷�ʽ 0�Ϻ��޼�ί�� �����޼�ί�� 1(�м�ί��)���ڶԷ����ż۸�  2(�м�ί��)���ڱ������ż۸�  3(�м�ί��)���ڼ�ʱ�ɽ�ʣ�೷��  4(�м�ί��)�Ϻ��嵵����ʣ�� �����嵵����ʣ�� 5(�м�ί��)����ȫ��ɽ����� 6(�м�ί��)�Ϻ��嵵����ת�޼�
	     * @param Gddm	�ɶ�����, �����Ϻ���Ʊ���Ϻ��Ĺɶ����룻�������ڵĹ�Ʊ�������ڵĹɶ�����
	     * @param Zqdm	֤ȯ����
	     * @param Price	ί�м۸�
	     * @param Quantity	ί������
	     * @param Result	ͬ��,���к���ί�б������
	     * @param ErrInfo	ͬ��
	     */
	    public  void SendOrder(int ClientID, int Category, int PriceType, String Gddm, String Zqdm, float Price, int Quantity, byte[] Result,  byte[] ErrInfo);
	    
	    /**
	     *  ��ί��
	     * @param ClientID	�ͻ���ID
	     * @param ExchangeID	������ID�� �Ϻ�1������0(����֤ȯ��ͨ�˻�������2)
	     * @param hth	��ʾҪ����Ŀ��ί�еı��
	     * @param Result	ͬ��
	     * @param ErrInfo	ͬ��
	     */
	    public  void CancelOrder(int ClientID, String ExchangeID, String hth,  byte[] Result,  byte[] ErrInfo);
	    /**
	     * ��ȡ֤ȯ��ʵʱ�嵵����
	     * @param ClientID 	�ͻ���ID
	     * @param Zqdm		֤ȯ����
	     * @param Result	ͬ��
	     * @param ErrInfo	ͬ��
	     */
	    public  void GetQuote(int ClientID, String Zqdm,  byte[] Result,  byte[] ErrInfo);
	    public  void Repay(int ClientID, String Amount, byte[] Result,  byte[] ErrInfo);
	}
	

	public TdxClient(int type) {
		if(type==1) {
			login("62124349","122541","A238440910,0129170967");
		} else {
			login("62278700","324877","A317676125,0187394552");
		}
		
	}
	
	private TdxLibrary tdxLibrary;
	private byte[] result = new byte[1024 * 1024];
	private byte[] errInfo = new byte[256];
	private int clientID =0;
	private String[] gddm =null;

	private void cleanResult(){
		result = new byte[1024 * 1024];
		errInfo = new byte[256];
	}
	/**
	 * ��ѯĿǰ���ʽ����
	 * @return
	 */
	public FundPoolDO queryFundDO() {
		FundPoolDO fundPoolDO = null;
		tdxLibrary.QueryData(clientID, 0, result, errInfo);
		String res = Native.toString(result, "GBK");
		cleanResult();
		if(res !=null) {
			String[][] items =TdxResultUtil.parseStr(res);
			fundPoolDO= new FundPoolDO();
			fundPoolDO.setAvailableFunds(new Money(items[1][2]));
			fundPoolDO.setTotalValue(new Money(items[1][5]));
			fundPoolDO.setStockValue(new Money(items[1][6]));
		}
		log.info(fundPoolDO);
		return fundPoolDO;
	}
	/**
	 * ��ѯĿǰ���й�Ʊ���
	 * @return
	 */
	public StockDO queryStockDO(String zqdm) {
		StockDO stockDO = null;
		tdxLibrary.QueryData(clientID, 1, result, errInfo);
		String res = Native.toString(result, "GBK");
		cleanResult();
		if(res !=null) {
			String[][] items =TdxResultUtil.parseStr(res);
			for(int i=1 ;i<items.length; i++) {
				if(zqdm.equals(items[i][0])) {
					stockDO = new StockDO();
					stockDO.setZqCode(items[i][0]);
					stockDO.setZqName(items[i][1]);
					Float amout = Float.parseFloat(items[i][2]);
					stockDO.setAmount(amout.longValue());
					stockDO.setAvaPrice(new Money(items[i][4]));
					log.info("���й�Ʊ�����" + stockDO);
					return stockDO;
				}
			}
		}
		log.info(stockDO);
		return stockDO;
	}
	/**
	 * �Ƿ���ί�е�
	 * @param isBuy
	 * @param zqdm
	 * @return
	 */
	public OrderDO haveDelegate(boolean isBuy, String zqdm){
		List<OrderDO> list = queryDelegate();
		for(OrderDO orderDO: list) {
			if(orderDO.isBuy() == isBuy && orderDO.getZqCode().equals(zqdm) && (orderDO.getStatus() == OrderDO.WAITING || orderDO.getStatus() == OrderDO.PART_SUCCESS)) {
				log.info("����"+ (isBuy ?"��":"��")+"ί�е���ί�е��ǣ� " + orderDO);
				return orderDO;
			}
		}
		log.info(zqdm +"Ŀǰ������ "+(isBuy ?"��":"��")+ "ί�е�");
		return null;
	}
	
	
	/**
	 * ��ѯί�����
	 * @return
	 */
	public List<OrderDO> queryDelegate() {
		List list = new ArrayList<OrderDO>();
		tdxLibrary.QueryData(clientID, 2, result, errInfo);
		String res = Native.toString(result, "GBK");
		cleanResult();
		if(res !=null) {
			String[][] items =TdxResultUtil.parseStr(res);
			for(int i=1 ;i<items.length; i++) {
				OrderDO orderDO = new OrderDO();
				orderDO.setZqCode(items[i][1]);
				orderDO.setZqName(items[i][2]);
				orderDO.setDate(items[i][0]);
				orderDO.setPrice(new Money(items[i][6]));
				orderDO.setOrderId(items[i][12]);
				Float amout = Float.parseFloat(items[i][7]);
				orderDO.setAmount(amout.longValue());
				
				if("����".equals(items[i][4])) {
					orderDO.setBuy(true);
				}
				
				if("�ѳ�".equals(items[i][5])|| "�ϵ�".equals(items[i][5])) {
					orderDO.setStatus(OrderDO.SUCCESS);
				} else if("�ѳ�".equals(items[i][5])) {
					orderDO.setStatus(OrderDO.CANCLE);
				} else if("����".equals(items[i][5])) {
					orderDO.setStatus(OrderDO.PART_SUCCESS);
				} else if("����".equals(items[i][5])) {
					orderDO.setStatus(OrderDO.PART_CANCLE);
				} else {
					orderDO.setStatus(OrderDO.WAITING);
				}
				list.add(orderDO);
			}
		}
		return list;
	}
	
	/**
	 * ��ѯĿ���Ʊ�۸�
	 * @param zqdm
	 * @return
	 */
	public OnlinePriceDO queryMarket(String zqdm) {
		tdxLibrary.GetQuote(clientID, zqdm, result, errInfo);
		String res = Native.toString(result, "GBK");
		cleanResult();
		OnlinePriceDO market = null;
		if(res !=null) {
			String[][] items =TdxResultUtil.parseStr(res);
			market = new OnlinePriceDO();
			market.setZqCode(items[1][0]);
			market.setZqName(items[1][1]);
			market.setyPrice(new Money(items[1][2]));
			market.setNsPrice(new Money(items[1][3]));
			market.setNowPrice(new Money(items[1][5]));
		}
		log.info("��Ʊ:" +market.getZqName()+ " ����ǰ�ļ۸���" + market);
		return market;
	}
	/**
	 * ��������
	 * @param zqdm
	 */
	public void cancleOrder(OrderDO orderDO) {
		log.info("����һ���򶩵�" + orderDO);
		if(TdxResultUtil.isSHCode(orderDO.getZqCode())) {	// ��֤
			tdxLibrary.CancelOrder(clientID, "1", orderDO.getOrderId(), result, errInfo);
		} else {
			tdxLibrary.CancelOrder(clientID, "0",  orderDO.getOrderId(), result, errInfo);
		}
		cleanResult();
	}
	
	/**
	 * �Ƿ�����µ�
	 * @param isBuy
	 * @param zqdm
	 * @return
	 */
	private boolean canOrder(boolean isBuy, String zqdm){
		int sum =0;
		List<OrderDO> list = queryDelegate();
		for(OrderDO orderDO: list) {
			if(orderDO.getZqCode().equals(zqdm) && orderDO.getStatus() == OrderDO.SUCCESS) {
				if(orderDO.isBuy()) {
					sum ++;
				} else {
					sum --;
				}
			}
		}
		
		sum += isBuy? 1:-1;
		
		return Math.abs(sum) <=2;
	}
	
	/**
	 * �µ�
	 * @param zqdm
	 * @param money
	 * @param amount
	 * @param isBuy
	 */
	public void crateOrder(OrderDO orderDO) {
		if (!canOrder(orderDO.isBuy(), orderDO.getZqCode())) {
			log.info("��������͵��Ѿ��ɽ��������Σ������µ�" +orderDO);
			return ;
		}
		if (orderDO.getAmount()*orderDO.getPrice().getCent() <20000*100) { 
			log.info("�������С��2w ����������" +orderDO);
			return ;
		}
		
		log.info("�µ���" + orderDO);
		tdxLibrary.SendOrder(clientID, orderDO.isBuy() ? 0 : 1, 0, TdxResultUtil.isSHCode(orderDO.getZqCode()) ? gddm[0]:gddm[1] , orderDO.getZqCode(), Float.parseFloat(orderDO.getPrice().toString()), (int)orderDO.getAmount(), result, errInfo);
		String res = Native.toString(errInfo, "GBK");
		cleanResult();
	}
	
	private boolean login(String userId,String passWord,String gddm) {
		// DLL��32λ��,��˱���ʹ��jdk32λ����,���ܵ���DLL;
		// �����Trade.dll��4��DLL���Ƶ�java����Ŀ¼��;
		// java���̱���������� jna.jar, �� https://github.com/twall/jna ���� jna.jar
		// ������ʲô���Ա�̣���������ϸ�Ķ�VC���ڵĹ���DLL���������Ĺ��ܺͲ�������˵��������ϸ�Ķ���������������ʱ�侫�����ޣ�ˡ�����
		tdxLibrary = (TdxLibrary) Native.loadLibrary("trade", TdxLibrary.class);
		tdxLibrary.OpenTdx();
		this.gddm = gddm.split(",");
		// ��¼
		clientID = tdxLibrary.Logon("140.207.225.74", (short) 7708, "7.02", (short) 1, userId,
				userId, passWord, "", errInfo);
		if (clientID == -1) {
			System.out.println(Native.toString(errInfo, "GBK"));
			return false;
		} 
		log.error("AccountNo "+userId +"login sucess");
		cleanResult();
		return true;
	}
	
	public void close() {
		tdxLibrary.Logoff(clientID);
		tdxLibrary.CloseTdx();
	}
	
	public static void main(String[] args) {
		TdxClient tdxClient2 = new TdxClient(2);
		OrderDO orderDO = new OrderDO();
		orderDO.setBuy(false);
		orderDO.setZqCode("600196");
		
		tdxClient2.crateOrder(orderDO);
	}

}