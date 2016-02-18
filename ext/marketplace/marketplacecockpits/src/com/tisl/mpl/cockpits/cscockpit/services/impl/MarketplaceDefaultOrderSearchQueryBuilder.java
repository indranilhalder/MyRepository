package com.tisl.mpl.cockpits.cscockpit.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.services.search.generic.query.AbstractCsFlexibleSearchQueryBuilder;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

public class MarketplaceDefaultOrderSearchQueryBuilder extends
		AbstractCsFlexibleSearchQueryBuilder<DefaultCsTextSearchCommand> {

	private static final Logger LOG = Logger
			.getLogger(MarketplaceDefaultOrderSearchQueryBuilder.class);
	private Map<String, String> channelCodeMap;
	private Map<String, String> deliveryCodeMap;

	private static final String EMPTY_STRING_CONSTANT = "Empty";

	@Autowired
	private ConfigurationService configurationService;

	private static final String JOIN_CUSTOMER_TABLE_PART_QUERY = " LEFT JOIN Customer AS c ON {c:pk} = {o:user}";

	public enum TextField {
		OrderId, CustomerFirstName, CustomerLastName, CustomerMobile, CustomerEmail, OrderDateInit, OrderDateEnd, SearchableOrderStatuses, DeliveryMode, SalesApplication;
	}

	public static enum OrderStatuses {
		Empty, AWB_ASSIGNED, CANCELLATION_INITIATED, CLOSED_ON_CANCELLATION, CLOSED_ON_RETURN_TO_ORIGIN, COD_CLOSED_WITHOUT_REFUND, DELIVERED, HOTC, INVOICE_GENERATED, LOGISTIC_PARTNER_SWITCH, LOST_IN_TRANSIT, MANIFESTO_PENDING, ORDER_ACCEPTED, ORDER_ALLOCATED, ORDER_CANCELLED, ORDER_REJECTED, OUT_FOR_DELIVERY, PACKED, PAYMENT_SUCCESSFUL, PENDING_SELLER_ASSIGNMENT, PICK_CONFIRMED, PICK_HOLD, PICK_IN_PROGRESS, PICK_LIST_GENERATED, PRINT_INVOICE, PRINT_SHIPPING_LABEL, QC_FAILED, REDISPATCH_INITIATED, REACHED_NEAREST_HUB, READY_TO_SHIP, REFUND_IN_PROGRESS, REFUND_INITIATED, RETURN_CANCELLED, RETURN_CLOSED, RETURN_COMPLETED, RETURN_INITIATED, RETURN_RECEIVED, RETURN_REJECTED, RETURN_TO_ORIGIN, REVERSE_AWB_ASSIGNED, RMS_VERIFICATION_FAILED, RMS_VERIFICATION_PENDING, UNDELIVERED

	}

	public static enum DeliveryModes {
		Empty, HD, ED, CC;

	}

	public static enum SalesApplications {
		Empty, WEB, /*WEBMOBILE,*/ CALLCENTER,MOBILE;

	}

	protected FlexibleSearchQuery buildFlexibleSearchQuery(
			DefaultCsTextSearchCommand command) {
		String orderId = command.getText(TextField.OrderId);
		String customerFirstName = command.getText(TextField.CustomerFirstName);
		String customerLastName = command.getText(TextField.CustomerLastName);
		final String orderDateInit = command.getText(TextField.OrderDateInit);
		final String orderDateEnd = command.getText(TextField.OrderDateEnd);
		final String mobileNumber = command.getText(TextField.CustomerMobile);
		final String emailId = command.getText(TextField.CustomerEmail);
		final String orderStatus = command
				.getText(TextField.SearchableOrderStatuses) != null ? command
				.getText(TextField.SearchableOrderStatuses) : "";
		final String deliveryMode = deliveryCodeMap.get(command
				.getText(TextField.DeliveryMode));
		final String salesApplication = channelCodeMap.get(command
				.getText(TextField.SalesApplication));

		int thresholddays = configurationService.getConfiguration().getInt(
				"cscockpit.order.search.date.threshold")
				* -1;

		boolean searchOrderId = StringUtils.isNotEmpty(orderId);
		boolean searchCustomerFirstName = StringUtils
				.isNotEmpty(customerFirstName);
		boolean searchCustomerLastName = StringUtils
				.isNotEmpty(customerLastName);
		final boolean searchOrderDateInit = StringUtils
				.isNotEmpty(orderDateInit);
		final boolean searchOrderDateEnd = StringUtils.isNotEmpty(orderDateEnd);
		final boolean searchOrderStatus = StringUtils.isNotEmpty(orderStatus)
				&& !StringUtils.equalsIgnoreCase(orderStatus,
						EMPTY_STRING_CONSTANT);
		final boolean searchdeliveryMode = StringUtils.isNotEmpty(deliveryMode)
				&& !StringUtils.equalsIgnoreCase(deliveryMode,
						EMPTY_STRING_CONSTANT);
		final boolean searchSalesApplication = StringUtils
				.isNotEmpty(salesApplication) && !StringUtils.equalsIgnoreCase(salesApplication,
						EMPTY_STRING_CONSTANT);
		final boolean searchMobileNumer = StringUtils.isNotEmpty(mobileNumber);
		final boolean searchEmailId = StringUtils.isNotEmpty(emailId);
		final boolean searchThreshold = thresholddays != 0;
		StringBuilder query = new StringBuilder();
		StringBuilder query2=new StringBuilder();
		
		if (!(searchOrderId || searchCustomerFirstName || searchCustomerLastName
				|| searchOrderDateInit || searchOrderDateEnd
				|| searchOrderStatus || searchdeliveryMode
				|| searchSalesApplication || searchMobileNumer || searchEmailId)) {
			return null;
		}
		
		

		if (searchMobileNumer || searchOrderId || searchCustomerFirstName
				|| searchCustomerLastName || searchOrderDateEnd
				&& searchOrderDateInit || searchOrderStatus
				|| searchdeliveryMode || searchSalesApplication
				|| searchEmailId) {

			query.append("SELECT DISTINCT OrderAlias.pk, OrderAlias.creationtime FROM ( {{ ");

			query.append("SELECT DISTINCT {o:pk} as pk ,{o:creationtime} as creationtime FROM {Order AS o");
			query2.append("SELECT DISTINCT {o:pk} as pk ,{o:creationtime} as creationtime FROM {Order AS o");
			if (searchCustomerFirstName || searchCustomerLastName
					|| searchMobileNumer || searchEmailId) {
				query.append(JOIN_CUSTOMER_TABLE_PART_QUERY);
				query2.append(JOIN_CUSTOMER_TABLE_PART_QUERY);
			}

			if (searchdeliveryMode) {
				query.append(" LEFT JOIN OrderEntry AS d ON {d:order}={o:pk} JOIN MplZoneDeliveryModeValue as delv ON {delv:pk}={d:mpldeliverymode} JOIN ZoneDeliveryMode as mod ON {mod:pk}={delv:deliveryMode} ");
				query2.append(" LEFT JOIN OrderEntry AS d ON {d:order}={o:pk} JOIN MplZoneDeliveryModeValue as delv ON {delv:pk}={d:mpldeliverymode} JOIN ZoneDeliveryMode as mod ON {mod:pk}={delv:deliveryMode} ");
			}

			if (searchSalesApplication) {
				query.append(" LEFT JOIN  SalesApplication AS s ON {s:pk}={o:salesapplication}");
				query2.append(" LEFT JOIN  SalesApplication AS s ON {s:pk}={o:salesapplication}");
			}

			if (searchOrderStatus) {
				query.append(" LEFT JOIN CONSIGNMENT as cos ON {cos:order}={o:pk} JOIN CONSIGNMENTSTATUS as cs ON {cs:pk}={cos:status}");
				query2.append(" JOIN OrderStatus as stat on {stat:pk}={o:status}");
			}


			query.append("} WHERE {o:originalVersion} IS NULL ");
			query2.append("} WHERE {o:originalVersion} IS NULL ");

			if (searchOrderId) {
				query.append(" AND {o:code} LIKE ?orderId ");
				query2.append(" AND {o:code} LIKE ?orderId ");
			}

			if (searchdeliveryMode) {
				query.append(" AND {mod:code}=?deliveryMode ");
				query2.append(" AND {mod:code}=?deliveryMode ");
			}
			if (searchOrderDateInit) {
				query.append(" AND {o:" + OrderModel.CREATIONTIME
						+ "} >= ?orderDateInit");
				query2.append(" AND {o:" + OrderModel.CREATIONTIME
						+ "} >= ?orderDateInit");
			}
			if (searchOrderDateEnd) {
				query.append(" AND {o:" + OrderModel.CREATIONTIME
						+ "} <= ?orderDateEnd");
				query2.append(" AND {o:" + OrderModel.CREATIONTIME
						+ "} <= ?orderDateEnd");
			}
			if (searchOrderStatus) {
				query.append(" AND {cs:code} = ?orderStatus");
			}

			if (searchSalesApplication) {
				query.append(" AND LOWER({s:code})=?salesApplictionCscockpit");
				query2.append(" AND LOWER({s:code})=?salesApplictionCscockpit");
			}

			if (searchCustomerFirstName) {
				query.append(" AND LOWER({c:firstName}) LIKE ?customerFirstName ");
				query2.append(" AND LOWER({c:firstName}) LIKE ?customerFirstName ");
			}

			if (searchCustomerLastName) {
				query.append(" AND LOWER({c:lastName}) LIKE ?customerLastName ");
				query2.append(" AND LOWER({c:lastName}) LIKE ?customerLastName ");
			}
			if (searchEmailId) {
				query.append(" AND LOWER({c:originalUid}) LIKE ?emailId");
				query2.append(" AND LOWER({c:originalUid}) LIKE ?emailId");
			}
			if (searchMobileNumer) {
				query.append(" AND {c:mobileNumber} = ?mobileNumber");
				query2.append(" AND {c:mobileNumber} = ?mobileNumber");
			}

			if (searchThreshold) {
				query.append(" AND {o:creationtime} >=?thresholdDays");
				query2.append(" AND {o:creationtime} >=?thresholdDays");
			}

			query.append(" }}");
			

			if (searchOrderStatus) {
				query2.append(" AND {stat:code}=?orderStatus }}");
				query.append(" UNION {{ ");
				query.append(query2.toString());
			}

			query.append(" ) OrderAlias ORDER BY OrderAlias.creationtime DESC ");

			FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(
					query.toString());

			if (searchOrderId) {
				searchQuery.addQueryParameter("orderId", "%" + orderId.trim()
						+ "%");
			}
			if (searchCustomerFirstName) {
				searchQuery.addQueryParameter("customerFirstName", "%"
						+ customerFirstName.trim().toLowerCase() + "%");
			}

			if (searchCustomerLastName) {
				searchQuery.addQueryParameter("customerLastName", "%"
						+ customerLastName.trim().toLowerCase() + "%");
			}
			try {
				DateFormat dateFormat = DateFormat.getInstance();
				if (searchOrderDateInit) {
					Date startDate = dateFormat.parse(orderDateInit);
					searchQuery.addQueryParameter("orderDateInit", startDate);
				}
				if (searchOrderDateEnd) {
					Date endDate = dateFormat.parse(orderDateEnd);
					Calendar cal=Calendar.getInstance();
					cal.setTime(endDate);
					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 59);
					cal.set(Calendar.SECOND, 59);
					searchQuery.addQueryParameter("orderDateEnd", cal.getTime());
				}
			} catch (ParseException e) {
				LOG.error(e);
			}

			if (searchOrderStatus) {
				searchQuery.addQueryParameter("orderStatus", orderStatus);
			}

			if (searchdeliveryMode) {
				searchQuery.addQueryParameter("deliveryMode", deliveryMode);
			}

			if (searchSalesApplication) {
				searchQuery.addQueryParameter("salesApplictionCscockpit",
						salesApplication);
			}
			if (searchMobileNumer) {
				searchQuery.addQueryParameter("mobileNumber", mobileNumber);
			}
			if (searchEmailId) {
				searchQuery.addQueryParameter("emailId", "%"+emailId.trim().toLowerCase()+"%");
			}
			if (searchThreshold) {
				Calendar currentDate = Calendar.getInstance();
				currentDate.add(Calendar.DAY_OF_YEAR, thresholddays);
				searchQuery.addQueryParameter("thresholdDays",
						currentDate.getTime());
			}
			LOG.debug("Query for Order Search " + searchQuery.getQuery());
			return searchQuery;
		}
		return null;
	}

	public Map<String, String> getChannelCodeMap() {
		return channelCodeMap;
	}

	public void setChannelCodeMap(Map<String, String> channelCodeMap) {
		this.channelCodeMap = channelCodeMap;
	}

	public Map<String, String> getDeliveryCodeMap() {
		return deliveryCodeMap;
	}

	public void setDeliveryCodeMap(Map<String, String> deliveryCodeMap) {
		this.deliveryCodeMap = deliveryCodeMap;
	}

}
