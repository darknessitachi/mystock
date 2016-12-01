package com.yujun.util;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
/*
 * Taobao.com Inc.
 * Copyright (c) 2000-2004 All Rights Reserved.
 */


/**
 * �����ֻ����࣬����������������ֺ�ȡ����
 *
 * <p>
 * �������з�װ�˻��ҽ��ͱ��֡�Ŀǰ������ڲ���long���ͱ�ʾ��
 * ��λ���������ֵ���С���ҵ�λ����������Ƿ֣���
 *
 * <p>
 * Ŀǰ������ʵ����������Ҫ���ܣ�<br>
 * <ul>
 *   <li>֧�ֻ��Ҷ�����double(float)/long(int)/String/BigDecimal֮���໥ת����
 *   <li>���������������ṩ��JDK�е�BigDecimal���Ƶ�����ӿڣ�
 *       BigDecimal������ӿ�֧������ָ�����ȵ����㹦�ܣ��ܹ�֧�ָ���
 *       ���ܵĲ������
 *   <li>��������������Ҳ�ṩһ�������ӿڣ�ʹ����������ӿڣ�����
 *       ���ȴ�����ʹ��ȱʡ�Ĵ������
 *   <li>�Ƽ�ʹ��Money��������ֱ��ʹ��BigDecimal��ԭ��֮һ���ڣ�
 *       ʹ��BigDecimal��ͬ�����ͱ��ֵĻ���ʹ��BigDecimal���ڶ��ֿ���
 *       �ı�ʾ�����磺new BigDecimal("10.5")��new BigDecimal("10.50")
 *       ����ȣ���Ϊscale���ȡ�ʹ��Money�࣬ͬ�����ͱ��ֵĻ���ֻ��
 *       һ�ֱ�ʾ��ʽ��new Money("10.5")��new Money("10.50")Ӧ������ȵġ�
 *   <li>���Ƽ�ֱ��ʹ��BigDecimal����һԭ�����ڣ� BigDecimal��Immutable��
 *       һ�������Ͳ��ɸ��ģ���BigDecimal�����������㶼������һ���µ�
 *       BigDecimal������˶��ڴ�����ͳ�Ƶ����ܲ������⡣Money����
 *       mutable�ģ��Դ�����ͳ���ṩ�Ϻõ�֧�֡�
 *   <li>�ṩ�����ĸ�ʽ�����ܡ�
 *   <li>Money���в�������ҵ����ص�ͳ�ƹ��ܺ͸�ʽ�����ܡ�ҵ����صĹ���
 *       ����ʹ��utility����ʵ�֡�
 *   <li>Money��ʵ����Serializable�ӿڣ�֧����ΪԶ�̵��õĲ����ͷ���ֵ��
 *   <li>Money��ʵ����equals��hashCode������
 * </ul>
 * @author Cheng Li
 *
 * @version $Id: Money.java 21751 2007-04-26 07:48:16Z yuanying $
 */
@SuppressWarnings("unchecked")
public class Money implements Serializable, Comparable {
    private static final long serialVersionUID = 3761410806910104373L;

    /**
     * ȱʡ�ı��ִ��룬ΪCNY������ң���
     */
    public static final String DEFAULT_CURRENCY_CODE = "CNY";

    /**
     * ȱʡ��ȡ��ģʽ��Ϊ<code>BigDecimal.ROUND_HALF_EVEN
     * ���������룬��С��Ϊ0.5ʱ����ȡ�����ż������
     */
    public static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

    /**
     * һ����ܵ�Ԫ/�ֻ��������
     *
     * <p>
     * �˴������֡���ָ���ҵ���С��λ����Ԫ���ǻ��ҵ���õ�λ��
     * ��ͬ�ı����в�ͬ��Ԫ/�ֻ�����������������100������ԪΪ1��
     */
    private static final int[] centFactors = new int[] { 1, 10, 100, 1000 };

    /**
     * Ĭ�J�ĵ؅^
     */
    private static final String DEFAULT_LOCALE = "zh_CN";
    /**
     * ������ҵ�λ��map��������ʾ���ҵ�λ����Ԫ����Ԫ��
     */
    protected static Map<String, Map<String, String>> CURRENCY_DISPLAY_UNIT_MAP = new HashMap<String, Map<String, String>>();
    //���г�ʼ������
    static {
        //���w���ĵČ����l
        Map<String, String> zhCNMap = new HashMap<String, String>();
        zhCNMap.put("CNY", "Ԫ");
        zhCNMap.put("POINT", "����");
        CURRENCY_DISPLAY_UNIT_MAP.put("zh_CN", zhCNMap);
        
        //���w�Č����l
        Map<String, String> zhHKMap = new HashMap<String, String>();
        zhHKMap.put("CNY", "Ԫ");
        zhHKMap.put("POINT", "�e��");
        CURRENCY_DISPLAY_UNIT_MAP.put("zh_HK", zhHKMap);
    }
    
    /**
     * ���붨���ں���(����ǰ��ľ�̬����)
     */
    public static final Money ZERO = new Money(0);
    
    /**
     * ���Է�Ϊ��λ��
     */
    private long cent;

    /**
     * ���֡�
     */
    private Currency currency;

    // ������ ====================================================

    /**
     * ȱʡ��������
     *
     * <p>
     * ����һ������ȱʡ��0����ȱʡ���ֵĻ��Ҷ���
     */
    public Money() {
        this(0l);
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>yuan</code>Ԫ<code>cent</cent>�ֺ�ȱʡ���ֵĻ��Ҷ���
     *
     * @param yuan ���Ԫ����
     * @param cent ��������
     */
    public Money(long yuan, int cent) {
        this(yuan, cent, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>yuan</code>Ԫ<code>cent</cent>�ֺ�ȱʡ���ֵĻ��Ҷ���
     *
     * @param cent ��������
     */
    public Money(long cent) {
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
        this.cent = cent;
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>yuan</code>Ԫ<code>cent</code>�ֺ�ָ�����ֵĻ��Ҷ���
     *
     * @param yuan ���Ԫ����
     * @param cent ��������
     * @param currency 
     */
    public Money(long yuan, int cent, Currency currency) {
        this.currency     = currency;

        this.cent = (yuan * getCentFactor()) + (cent % getCentFactor());
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>amount</code>Ԫ��ȱʡ���ֵĻ��Ҷ���
     *
     * @param amount ����ԪΪ��λ��
     */
    public Money(String amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>amount</code>Ԫ��ָ������<code>currency</code>�Ļ��Ҷ���
     *
     * @param amount ����ԪΪ��λ��
     * @param currency ���֡�
     */
    public Money(String amount, Currency currency) {
        this(new BigDecimal(amount), currency);
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>amount</code>Ԫ��ָ������<code>currency</code>�Ļ��Ҷ���
     * �������ת��Ϊ�����֣���ʹ��ָ����ȡ��ģʽ<code>roundingMode</code>ȡ����
     *
     * @param amount ����ԪΪ��λ��
     * @param currency ���֡�
     * @param roundingMode ȡ��ģʽ��
     */
    public Money(String amount, Currency currency, int roundingMode) {
        this(new BigDecimal(amount), currency, roundingMode);
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����в���<code>amount</code>ָ������ȱʡ���ֵĻ��Ҷ���
     * �������ת��Ϊ�����֣���ʹ���������뷽ʽȡ����
     *
     * <p>
     * ע�⣺����double���������д�����ʹ���������뷽ʽȡ����
     * �������ȷ������ˣ�Ӧ��������ʹ��double���ʹ����������͡�
     * ����
     * <code>
     * assertEquals(999, Math.round(9.995 * 100));
     * assertEquals(1000, Math.round(999.5));
     * money = new Money((9.995));
     * assertEquals(999, money.getCent());
     * money = new Money(10.005);
     * assertEquals(1001, money.getCent());
     * </code>
     *
     * @param amount ����ԪΪ��λ��
     *
     */
    public Money(double amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>amount</code>��ָ�����ֵĻ��Ҷ���
     * �������ת��Ϊ�����֣���ʹ���������뷽ʽȡ����
     *
     * <p>
     * ע�⣺����double���������д�����ʹ���������뷽ʽȡ����
     * �������ȷ������ˣ�Ӧ��������ʹ��double���ʹ����������͡�
     * ����
     * <code>
     * assertEquals(999, Math.round(9.995 * 100));
     * assertEquals(1000, Math.round(999.5));
     * money = new Money((9.995));
     * assertEquals(999, money.getCent());
     * money = new Money(10.005);
     * assertEquals(1001, money.getCent());
     * </code>
     *
     * @param amount ����ԪΪ��λ��
     * @param currency ���֡�
     */
    public Money(double amount, Currency currency) {
        this.currency     = currency;
        this.cent         = Math.round(amount * getCentFactor());
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>amount</code>��ȱʡ���ֵĻ��Ҷ���
     * �������ת��Ϊ�����֣���ʹ��ȱʡȡ��ģʽ<code>DEFAULT_ROUNDING_MODE</code>ȡ����
     *
     * @param amount ����ԪΪ��λ��
     */
    public Money(BigDecimal amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����в���<code>amount</code>ָ������ȱʡ���ֵĻ��Ҷ���
     * �������ת��Ϊ�����֣���ʹ��ָ����ȡ��ģʽ<code>roundingMode</code>ȡ����
     *
     * @param amount ����ԪΪ��λ��
     * @param roundingMode ȡ��ģʽ
     *
     */
    public Money(BigDecimal amount, int roundingMode) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE), roundingMode);
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>amount</code>��ָ�����ֵĻ��Ҷ���
     * �������ת��Ϊ�����֣���ʹ��ȱʡ��ȡ��ģʽ<code>DEFAULT_ROUNDING_MODE</code>����ȡ����
     *
     * @param amount ����ԪΪ��λ��
     * @param currency ����
     */
    public Money(BigDecimal amount, Currency currency) {
        this(amount, currency, DEFAULT_ROUNDING_MODE);
    }

    /**
     * ��������
     *
     * <p>
     * ����һ�����н��<code>amount</code>��ָ�����ֵĻ��Ҷ���
     * �������ת��Ϊ�����֣���ʹ��ָ����ȡ��ģʽ<code>roundingMode</code>ȡ����
     *
     * @param amount ����ԪΪ��λ��
     * @param currency ���֡�
     * @param roundingMode ȡ��ģʽ��
     */
    public Money(BigDecimal amount, Currency currency, int roundingMode) {
        this.currency     = currency;
        this.cent         = rounding(amount.movePointRight(currency.getDefaultFractionDigits()),
                roundingMode);
    }

    // Bean���� ====================================================

    /**
     * ��ȡ�����Ҷ������Ľ������
     *
     * @return ���������ԪΪ��λ��
     */
    public BigDecimal getAmount() {
        return BigDecimal.valueOf(this.cent, this.currency.getDefaultFractionDigits());
    }
    
    /**
     * ���ñ����Ҷ������Ľ������
     * 
     * @param amount ���������ԪΪ��λ��
     */
    public void setAmount(BigDecimal amount) {
    	if (amount != null) {
    		this.cent = rounding(amount.movePointRight(2), BigDecimal.ROUND_HALF_EVEN);
    	}
    }

    /**
     * ��ȡ�����Ҷ������Ľ������
     *
     * @return ��������Է�Ϊ��λ��
     */
    public long getCent() {
        return this.cent;
    }

    /**
     * ��ȡ�����Ҷ������ı��֡�
     *
     * @return �����Ҷ���������ı��֡�
     * @deprecated use getCurrencyCode()
     */
    public Currency getCurrency() {
        return this.currency;
    }
    
    /**
     * ��ȡ�����Ҷ������ı��ִ���
     * 
     * @return ���ִ���
     */
    public String getCurrencyCode() {
        return this.currency.getCurrencyCode();
    }

    /**
     * ��ȡ�����ұ��ֵ�Ԫ/�ֻ�����ʡ�
     *
     * @return �����ұ��ֵ�Ԫ/�ֻ�����ʡ�
     */
    public int getCentFactor() {
        return centFactors[this.currency.getDefaultFractionDigits()];
    }

    // �������󷽷� ===================================================

    /**
     * �жϱ����Ҷ�������һ�����Ƿ���ȡ�
     *
     * <p>
     * �����Ҷ�������һ������ȵĳ�ֱ�Ҫ�����ǣ�<br>
     *  <ul>
     *   <li>��һ����Ҳ�����Ҷ����ࡣ
     *   <li>�����ͬ��
     *   <li>������ͬ��
     *  </ul>
     *
     * @param other ���Ƚϵ���һ����
     * @return <code>true</code>��ʾ��ȣ�<code>false</code>��ʾ����ȡ�
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        return (other instanceof Money) && equals((Money) other);
    }

    /**
     * �жϱ����Ҷ�������һ���Ҷ����Ƿ���ȡ�
     *
     * <p>
     * �����Ҷ�������һ���Ҷ�����ȵĳ�ֱ�Ҫ�����ǣ�<br>
     *  <ul>
     *   <li>�����ͬ��
     *   <li>������ͬ��
     *  </ul>
     *
     * @param other ���Ƚϵ���һ���Ҷ���
     * @return <code>true</code>��ʾ��ȣ�<code>false</code>��ʾ����ȡ�
     */
    public boolean equals(Money other) {
        return this.currency.equals(other.currency) && (this.cent == other.cent);
    }

    /**
     * ���㱾���Ҷ�����Ӵ�ֵ��
     *
     * @return �����Ҷ�����Ӵ�ֵ��
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (int) (this.cent ^ (this.cent >>> 32));
    }

    // Comparable�ӿ� ========================================

    /**
     * ����Ƚϡ�
     *
     * <p>
     * �Ƚϱ���������һ����Ĵ�С��
     * ������ȽϵĶ�������Ͳ���<code>Money</code>�����׳�<code>java.lang.ClassCastException</code>��
     * ������Ƚϵ��������Ҷ���ı��ֲ�ͬ�����׳�<code>java.lang.IllegalArgumentException</code>��
     * ��������Ҷ���Ľ�����ڴ��Ƚϻ��Ҷ����򷵻�-1��
     * ��������Ҷ���Ľ����ڴ��Ƚϻ��Ҷ����򷵻�0��
     * ��������Ҷ���Ľ����ڴ��Ƚϻ��Ҷ����򷵻�1��
     *
     * @param other ��һ����
     * @return -1��ʾС�ڣ�0��ʾ���ڣ�1��ʾ���ڡ�
     *
     * @exception ClassCastException ���Ƚϻ��Ҷ�����<code>Money</code>��
     *            IllegalArgumentException ���Ƚϻ��Ҷ����뱾���Ҷ���ı��ֲ�ͬ��
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object other) {
        return compareTo((Money) other);
    }

    /**
     * ���ұȽϡ�
     *
     * <p>
     * �Ƚϱ����Ҷ�������һ���Ҷ���Ĵ�С��
     * ������Ƚϵ��������Ҷ���ı��ֲ�ͬ�����׳�<code>java.lang.IllegalArgumentException</code>��
     * ��������Ҷ���Ľ�����ڴ��Ƚϻ��Ҷ����򷵻�-1��
     * ��������Ҷ���Ľ����ڴ��Ƚϻ��Ҷ����򷵻�0��
     * ��������Ҷ���Ľ����ڴ��Ƚϻ��Ҷ����򷵻�1��
     *
     * @param other ��һ����
     * @return -1��ʾС�ڣ�0��ʾ���ڣ�1��ʾ���ڡ�
     *
     * @exception IllegalArgumentException ���Ƚϻ��Ҷ����뱾���Ҷ���ı��ֲ�ͬ��
     */
    public int compareTo(Money other) {
        assertSameCurrencyAs(other);

        if (this.cent < other.cent) {
            return -1;
        } else if (this.cent == other.cent) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * ���ұȽϡ�
     *
     * <p>
     * �жϱ����Ҷ����Ƿ������һ���Ҷ���
     * ������Ƚϵ��������Ҷ���ı��ֲ�ͬ�����׳�<code>java.lang.IllegalArgumentException</code>��
     * ��������Ҷ���Ľ����ڴ��Ƚϻ��Ҷ����򷵻�true�����򷵻�false��
     *
     * @param other ��һ����
     * @return true��ʾ���ڣ�false��ʾ�����ڣ�С�ڵ��ڣ���
     *
     * @exception IllegalArgumentException ���Ƚϻ��Ҷ����뱾���Ҷ���ı��ֲ�ͬ��
     */
    public boolean greaterThan(Money other) {
        return compareTo(other) > 0;
    }

    // �������� ==========================================

    /**
     * ���Ҽӷ���
     *
     * <p>
     * ��������ұ�����ͬ���򷵻�һ���µ���ͬ���ֵĻ��Ҷ�������Ϊ
     * �����Ҷ�����֮�ͣ������Ҷ����ֵ���䡣
     * ��������Ҷ�����ֲ�ͬ���׳�<code>java.lang.IllegalArgumentException</code>��
     *
     * @param other ��Ϊ�����Ļ��Ҷ���
     *
     * @exception IllegalArgumentException ��������Ҷ�������һ���Ҷ�����ֲ�ͬ��
     *
     * @return ��Ӻ�Ľ����
     */
    public Money add(Money other) {
        assertSameCurrencyAs(other);

        return newMoneyWithSameCurrency(this.cent + other.cent);
    }

    /**
     * �����ۼӡ�
     *
     * <p>
     * ��������ұ�����ͬ���򱾻��Ҷ���Ľ����������Ҷ�����֮�ͣ������ر����Ҷ�������á�
     * ��������Ҷ�����ֲ�ͬ���׳�<code>java.lang.IllegalArgumentException</code>��
     *
     * @param other ��Ϊ�����Ļ��Ҷ���
     *
     * @exception IllegalArgumentException ��������Ҷ�������һ���Ҷ�����ֲ�ͬ��
     *
     * @return �ۼӺ�ı����Ҷ���
     */
    public Money addTo(Money other) {
        assertSameCurrencyAs(other);

        this.cent += other.cent;

        return this;
    }

    /**
     * ���Ҽ�����
     *
     * <p>
     * ��������ұ�����ͬ���򷵻�һ���µ���ͬ���ֵĻ��Ҷ�������Ϊ
     * �����Ҷ���Ľ���ȥ�������Ҷ���Ľ������Ҷ����ֵ���䡣
     * ��������ұ��ֲ�ͬ���׳�<code>java.lang.IllegalArgumentException</code>��
     *
     * @param other ��Ϊ�����Ļ��Ҷ���
     *
     * @exception IllegalArgumentException ��������Ҷ�������һ���Ҷ�����ֲ�ͬ��
     *
     * @return �����Ľ����
     */
    public Money subtract(Money other) {
        assertSameCurrencyAs(other);

        return newMoneyWithSameCurrency(this.cent - other.cent);
    }

    /**
     * �����ۼ���
     *
     * <p>
     * ��������ұ�����ͬ���򱾻��Ҷ���Ľ����������Ҷ�����֮������ر����Ҷ�������á�
     * ��������ұ��ֲ�ͬ���׳�<code>java.lang.IllegalArgumentException</code>��
     *
     * @param other ��Ϊ�����Ļ��Ҷ���
     *
     * @exception IllegalArgumentException ��������Ҷ�������һ���Ҷ�����ֲ�ͬ��
     *
     * @return �ۼ���ı����Ҷ���
     */
    public Money subtractFrom(Money other) {
        assertSameCurrencyAs(other);

        this.cent -= other.cent;

        return this;
    }

    /**
     * ���ҳ˷���
     *
     * <p>
     * ����һ���µĻ��Ҷ��󣬱����뱾���Ҷ�����ͬ�����Ϊ�����Ҷ���Ľ����Գ�����
     * �����Ҷ����ֵ���䡣
     *
     * @param val ����
     *
     * @return �˷���Ľ����
     */
    public Money multiply(long val) {
        return newMoneyWithSameCurrency(this.cent * val);
    }

    /**
     * �����۳ˡ�
     *
     * <p>
     * �����Ҷ�������Գ����������ر����Ҷ���
     *
     * @param val ����
     *
     * @return �۳˺�ı����Ҷ���
     */
    public Money multiplyBy(long val) {
        this.cent *= val;

        return this;
    }

    /**
     * ���ҳ˷���
     *
     * <p>
     * ����һ���µĻ��Ҷ��󣬱����뱾���Ҷ�����ͬ�����Ϊ�����Ҷ���Ľ����Գ�����
     * �����Ҷ����ֵ���䡣�����˺�Ľ���ת��Ϊ�����֣����������롣
     *
     * @param val ����
     *
     * @return ��˺�Ľ����
     */
    public Money multiply(double val) {
        return newMoneyWithSameCurrency(Math.round(this.cent * val));
    }

    /**
     * �����۳ˡ�
     *
     * <p>
     * �����Ҷ�������Գ����������ر����Ҷ���
     * �����˺�Ľ���ת��Ϊ�����֣���ʹ���������롣
     *
     * @param val ����
     *
     * @return �۳˺�ı����Ҷ���
     */
    public Money multiplyBy(double val) {
        this.cent = Math.round(this.cent * val);

        return this;
    }

    /**
     * ���ҳ˷���
     *
     * <p>
     * ����һ���µĻ��Ҷ��󣬱����뱾���Ҷ�����ͬ�����Ϊ�����Ҷ���Ľ����Գ�����
     * �����Ҷ����ֵ���䡣�����˺�Ľ���ת��Ϊ�����֣�ʹ��ȱʡ��ȡ��ģʽ
     * <code>DEFUALT_ROUNDING_MODE</code>����ȡ����
     *
     * @param val ����
     *
     * @return ��˺�Ľ����
     */
    public Money multiply(BigDecimal val) {
        return multiply(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * �����۳ˡ�
     *
     * <p>
     * �����Ҷ�������Գ����������ر����Ҷ���
     * �����˺�Ľ���ת��Ϊ�����֣�ʹ��ȱʡ��ȡ����ʽ
     * <code>DEFUALT_ROUNDING_MODE</code>����ȡ����
     *
     * @param val ����
     *
     * @return �۳˺�Ľ����
     */
    public Money multiplyBy(BigDecimal val) {
        return multiplyBy(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * ���ҳ˷���
     *
     * <p>
     * ����һ���µĻ��Ҷ��󣬱����뱾���Ҷ�����ͬ�����Ϊ�����Ҷ���Ľ����Գ�����
     * �����Ҷ����ֵ���䡣�����˺�Ľ���ת��Ϊ�����֣�ʹ��ָ����ȡ����ʽ
     * <code>roundingMode</code>����ȡ����
     *
     * @param val ����
     * @param roundingMode ȡ����ʽ
     *
     * @return ��˺�Ľ����
     */
    public Money multiply(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).multiply(val);

        return newMoneyWithSameCurrency(rounding(newCent, roundingMode));
    }

    /**
     * �����۳ˡ�
     *
     * <p>
     * �����Ҷ�������Գ����������ر����Ҷ���
     * �����˺�Ľ���ת��Ϊ�����֣�ʹ��ָ����ȡ����ʽ
     * <code>roundingMode</code>����ȡ����
     *
     * @param val ����
     * @param roundingMode ȡ����ʽ
     *
     * @return �۳˺�Ľ����
     */
    public Money multiplyBy(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).multiply(val);

        this.cent = rounding(newCent, roundingMode);

        return this;
    }

    /**
     * ���ҳ�����
     *
     * <p>
     * ����һ���µĻ��Ҷ��󣬱����뱾���Ҷ�����ͬ�����Ϊ�����Ҷ���Ľ����Գ�����
     * �����Ҷ����ֵ���䡣��������Ľ���ת��Ϊ�����֣�ʹ���������뷽ʽȡ����
     *
     * @param val ����
     *
     * @return �����Ľ����
     */
    public Money divide(double val) {
        return newMoneyWithSameCurrency(Math.round(this.cent / val));
    }

    /**
     * �����۳���
     *
     * <p>
     * �����Ҷ�������Գ����������ر����Ҷ���
     * ��������Ľ���ת��Ϊ�����֣�ʹ���������뷽ʽȡ����
     *
     * @param val ����
     *
     * @return �۳���Ľ����
     */
    public Money divideBy(double val) {
        this.cent = Math.round(this.cent / val);

        return this;
    }

    /**
     * ���ҳ�����
     *
     * <p>
     * ����һ���µĻ��Ҷ��󣬱����뱾���Ҷ�����ͬ�����Ϊ�����Ҷ���Ľ����Գ�����
     * �����Ҷ����ֵ���䡣��������Ľ���ת��Ϊ�����֣�ʹ��ȱʡ��ȡ��ģʽ
     * <code>DEFAULT_ROUNDING_MODE</code>����ȡ����
     *
     * @param val ����
     *
     * @return �����Ľ����
     */
    public Money divide(BigDecimal val) {
        return divide(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * ���ҳ�����
     *
     * <p>
     * ����һ���µĻ��Ҷ��󣬱����뱾���Ҷ�����ͬ�����Ϊ�����Ҷ���Ľ����Գ�����
     * �����Ҷ����ֵ���䡣��������Ľ���ת��Ϊ�����֣�ʹ��ָ����ȡ��ģʽ
     * <code>roundingMode</code>����ȡ����
     *
     * @param val ����
     * @param roundingMode ȡ��
     *
     * @return �����Ľ����
     */
    public Money divide(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).divide(val, roundingMode);

        return newMoneyWithSameCurrency(newCent.longValue());
    }

    /**
     * �����۳���
     *
     * <p>
     * �����Ҷ�������Գ����������ر����Ҷ���
     * ��������Ľ���ת��Ϊ�����֣�ʹ��ȱʡ��ȡ��ģʽ
     * <code>DEFAULT_ROUNDING_MODE</code>����ȡ����
     *
     * @param val ����
     *
     * @return �۳���Ľ����
     */
    public Money divideBy(BigDecimal val) {
        return divideBy(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * �����۳���
     *
     * <p>
     * �����Ҷ�������Գ����������ر����Ҷ���
     * ��������Ľ���ת��Ϊ�����֣�ʹ��ָ����ȡ��ģʽ
     * <code>roundingMode</code>����ȡ����
     *
     * @param val ����
     * @param roundingMode 
     *
     * @return �۳���Ľ����
     */
    public Money divideBy(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).divide(val, roundingMode);

        this.cent = newCent.longValue();

        return this;
    }

    /**
     * ���ҷ��䡣
     *
     * <p>
     * �������Ҷ��󾡿���ƽ�������<code>targets</code>�ݡ�
     * �������ƽ�����価������ͷ�ŵ���ʼ�����ɷ��С�����
     * �����ܹ�ȷ�����ᶪʧ�����ͷ��
     *
     * @param targets ������ķ���
     *
     * @return ���Ҷ������飬����ĳ�������������ͬ������Ԫ��
     *         �Ӵ�С���У����л��Ҷ���Ľ�����ֻ���1�֡�
     */
    public Money[] allocate(int targets) {
        Money[] results = new Money[targets];

        Money   lowResult  = newMoneyWithSameCurrency(this.cent / targets);
        Money   highResult = newMoneyWithSameCurrency(lowResult.cent + 1);

        int     remainder = (int) this.cent % targets;

        for (int i = 0; i < remainder; i++) {
            results[i] = highResult;
        }

        for (int i = remainder; i < targets; i++) {
            results[i] = lowResult;
        }

        return results;
    }

    /**
     * ���ҷ��䡣
     *
     * <p>
     * �������Ҷ����չ涨�ı�����������ɷݡ�������ʣ����ͷ
     * �ӵ�һ�ݿ�ʼ˳����䡣��������ȷ�����ᶪʧ�����ͷ��
     *
     * @param ratios ����������飬ÿһ��������һ�������ͣ�����
     *               ������������������
     *
     * @return ���Ҷ������飬����ĳ���������������ĳ�����ͬ��
     */
    public Money[] allocate(long[] ratios) {
        Money[] results = new Money[ratios.length];

        long    total = 0;

        for (int i = 0; i < ratios.length; i++) {
            total += ratios[i];
        }

        long remainder = this.cent;

        for (int i = 0; i < results.length; i++) {
            results[i] = newMoneyWithSameCurrency((this.cent * ratios[i]) / total);
            remainder -= results[i].cent;
        }

        for (int i = 0; i < remainder; i++) {
            results[i].cent++;
        }

        return results;
    }

    // ��ʽ������ =================================================

    /**
     * ���ɱ������ȱʡ�ַ�����ʾ
     * @return string
     */
    public String toString() {
    	return getAmount().toString();
    	
//        StringBuffer sb = new StringBuffer();
//
//        // sb.append(currency.getSymbol());
//
//        // sb.append(getAmount().toString());
//        sb.append(cent / getCentFactor());
//
//        if (getCentFactor() != 1) {
//            sb.append(".");
//
//            String remainder = String.valueOf(getCentFactor() + Math.abs(cent % getCentFactor()));
//
//            sb.append(remainder.substring(1));
//        }
//
//        return sb.toString();
    }

    // �ڲ����� ===================================================

    /**
     * ���Ա����Ҷ�������һ���Ҷ����Ƿ������ͬ�ı��֡�
     *
     * <p>
     * ��������Ҷ�������һ���Ҷ��������ͬ�ı��֣��򷽷����ء�
     * �����׳�����ʱ�쳣<code>java.lang.IllegalArgumentException</code>��
     *
     * @param other ��һ���Ҷ���
     *
     * @exception IllegalArgumentException ��������Ҷ�������һ���Ҷ�����ֲ�ͬ��
     */
    protected void assertSameCurrencyAs(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money math currency mismatch.");
        }
    }

    /**
     * ��BigDecimal�͵�ֵ��ָ��ȡ����ʽȡ����
     *
     * @param val ��ȡ����BigDecimalֵ
     * @param roundingMode ȡ����ʽ
     *
     * @return ȡ�����long��ֵ
     */
    protected long rounding(BigDecimal val, int roundingMode) {
        return val.setScale(0, roundingMode).longValue();
    }

    /**
     * ����һ��������ͬ������ָ�����Ļ��Ҷ���
     *
     * @param cent1 ���Է�Ϊ��λ
     *
     * @return һ���½��ı�����ͬ������ָ�����Ļ��Ҷ���
     */
    protected Money newMoneyWithSameCurrency(long cent1) {
        Money money = new Money(0, this.currency);

        money.cent = cent1;

        return money;
    }

    // ���Է�ʽ ==================================================

    /**
     * ���ɱ������ڲ��������ַ�����ʾ�����ڵ��ԡ�
     *
     * @return �������ڲ��������ַ�����ʾ��
     */
    public String dump() {
        String       lineSeparator = System.getProperty("line.separator");

        StringBuffer sb = new StringBuffer();

        sb.append("cent = ").append(this.cent).append(lineSeparator);
        sb.append("currency = ").append(this.currency);

        return sb.toString();
    }
    
    
    /**
	 * @param cent The cent to set.
	 */
	public void setCent(long cent) {
		this.cent = cent;
	}

	
}
