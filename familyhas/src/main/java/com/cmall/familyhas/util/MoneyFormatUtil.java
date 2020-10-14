package com.cmall.familyhas.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyFormatUtil {
	
	public static BigDecimal moneyFormat(String orderMoney){  
		return new BigDecimal(orderMoney).setScale(2,BigDecimal.ROUND_HALF_UP); 
	}
	
}
