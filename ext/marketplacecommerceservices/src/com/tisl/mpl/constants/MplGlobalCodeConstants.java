/**
 *
 */
package com.tisl.mpl.constants;

import de.hybris.platform.core.enums.OrderStatus;

import java.util.HashMap;
import java.util.Map;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class MplGlobalCodeConstants
{
	public static final Map<String, String> GLOBALCONSTANTSMAP = new HashMap<String, String>();



	static
	{
		//Global Channel constants
		GLOBALCONSTANTSMAP.put("WEB", "01");
		GLOBALCONSTANTSMAP.put("MOBILE", "02");
		GLOBALCONSTANTSMAP.put("KIOSK", "03");
		GLOBALCONSTANTSMAP.put("CALL CENTER", "04");

		//Global Transport mode constants
		GLOBALCONSTANTSMAP.put("AIR", "AIR");
		GLOBALCONSTANTSMAP.put("SURFACE-GROUND", "SUR");
		GLOBALCONSTANTSMAP.put("SURFACE", "SUR");

		//Global Address
		GLOBALCONSTANTSMAP.put("DELIVERY ADDRESS", "DA");
		GLOBALCONSTANTSMAP.put("BILLING ADDRESS", "BA");

		//Payment Status
		GLOBALCONSTANTSMAP.put("SUCCESS", "S");
		GLOBALCONSTANTSMAP.put("FAILURE", "F");
		GLOBALCONSTANTSMAP.put("RMS VERIFICATION PENDING", "VP");
		GLOBALCONSTANTSMAP.put("RMS VERIFICATION SUCCESS", "VS");

		//Order type
		GLOBALCONSTANTSMAP.put("NEW", "01");
		GLOBALCONSTANTSMAP.put("RETURN", "02");
		GLOBALCONSTANTSMAP.put("EXCHANGE", "03");
		GLOBALCONSTANTSMAP.put("REPLACEMENT", "04");
		GLOBALCONSTANTSMAP.put("CANCELLATION", "05");
		GLOBALCONSTANTSMAP.put("GOOD WILL", "06");

		//Payment code
		GLOBALCONSTANTSMAP.put(OrderStatus.PAYMENT_SUCCESSFUL.getCode().toUpperCase(), "PYMTSCSS");
		GLOBALCONSTANTSMAP.put("PYMTSCSS", "PYMTSCSS");
		GLOBALCONSTANTSMAP.put(OrderStatus.RMS_VERIFICATION_PENDING.getCode().toUpperCase(), "PYMTVRFN");
		GLOBALCONSTANTSMAP.put("PYMTVRFN", "PYMTVRFN");


		//Fulfilment type
		GLOBALCONSTANTSMAP.put("TSHIP", "TSHIP");
		GLOBALCONSTANTSMAP.put("SSHIP", "SSHIP");

		//Delivery Mode
		GLOBALCONSTANTSMAP.put("HOME-DELIVERY", "HD");
		GLOBALCONSTANTSMAP.put("EXPRESS-DELIVERY", "ED");

		//Payment Mode
		GLOBALCONSTANTSMAP.put("CREDIT CARD", "CC");
		GLOBALCONSTANTSMAP.put("DEBIT CARD", "DC");
		GLOBALCONSTANTSMAP.put("EMI", "EM");
		GLOBALCONSTANTSMAP.put("NETBANKING", "NB");
		GLOBALCONSTANTSMAP.put("COD", "CD");
		GLOBALCONSTANTSMAP.put("WALLET", "CB");
	}
}
