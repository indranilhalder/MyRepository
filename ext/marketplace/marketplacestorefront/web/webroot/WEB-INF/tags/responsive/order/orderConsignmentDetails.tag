<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="consignment" required="true" type="de.hybris.platform.commercefacades.order.data.ConsignmentData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
function callCancel()
{
 var action="Cancel";
	$("#action").val(action);
}

function callReturn()
{
 var action="Return";
	$("#action").val(action);
}

function callExchange()
{
 var action="Exchange";
	$("#action").val(action);
}
</script>

<div class="item_container_holder">
	<table id="your_order">
		<thead>
			<tr id="header0">
				<th id="header_status">${consignment.statusDisplay}" : "${consignment.statusDate}</th>
				<th id="header_trackingId" class="right"><spring:theme  code="text.order.consignment.trackingID" text="Tracking : " ></spring:theme>${consignment.trackingID}</th>
			</tr>
			<tr>
				<th id="header1"><span class="hidden"><spring:theme code="text.product" text="Product"/></span></th>
				<th id="header2"><span class="hidden"><spring:theme code="text.productDetails" text="Product Details"/></span></th>
				<th id="header4"><spring:theme code="text.quantity" text="Quantity"/></th>
				<th id="header5"><spring:theme code="text.itemPrice" text="Item Price"/></th>
				<th id="header6"><spring:theme code="text.total" text="Total"/></th>
			</tr>
		</thead>
		<tbody id="consignment_entries">
			<c:forEach items="${consignment.entries}" var="entry">
				<c:url value="${entry.orderEntry.product.url}" var="productUrl"/>
				<tr class="consignment_entries">
					<td headers="header1" class="product_image">
						<a href="${productUrl}">
							<product:productPrimaryImage product="${entry.orderEntry.product}" format="thumbnail"/>
						</a>
					</td>
					<td headers="header2" class="product_details">
						<h2>
							<ycommerce:testId code="orderDetails_productName_link">
								<a href="${entry.orderEntry.product.purchasable ? productUrl : ''}">${entry.orderEntry.product.name}</a>
							</ycommerce:testId>
						</h2>
						<c:forEach items="${entry.orderEntry.product.baseOptions}" var="option">
							<c:if test="${not empty option.selected and option.selected.url eq entry.orderEntry.product.url}">
								<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
									<dl>
										<dt>${selectedOption.name}:</dt>
										<dd>${selectedOption.value}</dd>
									</dl>
								</c:forEach>
							</c:if>
						</c:forEach>							
					</td>
					<td headers="header4" class="quantity">
						<ycommerce:testId code="orderDetails_productQuantity_label">${entry.quantity}</ycommerce:testId>
					</td>
					<td headers="header5">
						<ycommerce:testId code="orderDetails_productItemPrice_label"><format:price priceData="${entry.orderEntry.basePrice}" displayFreeForZero="true"/></ycommerce:testId>
					</td>
					<td headers="header6" class="total">
						<ycommerce:testId code="orderDetails_productTotalPrice_label"><format:price priceData="${entry.orderEntry.totalPrice}" displayFreeForZero="true"/></ycommerce:testId>
					</td>
				</tr>
				<tr class="item">
					<td colspan="5">
					<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
						<c:url var="pageUrl" value="my-account" /> 	
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
						<c:url var="pageUrl" value="order_tracking" /> 	
					</sec:authorize>
						<form:form action="${request.contextPath}/${pageUrl}/order/?orderCode=${order.code}" method="post">
						<input type="hidden" name="action" id="action" value=""/>
						<%-- <form:input type="hidden" id="myInput" path="code" value="${order.code}"/>
						<c:set var="orderCode" value="${order.code}" scope="request"/> --%>
						
						<c:if test="${order.status.code eq 'PLACED' or order.status.code eq 'CONFIRMED' or order.status.code eq 'PACKED' or order.status.code eq 'DISPATCHED_FROM_WAREHOUSE'}">
							<button type="submit" class="btn btn-primary" onclick="callCancel();"><spring:theme code="text.account.profile.cancelOrder" text="Cancel"/></button>		
						</c:if>
						<c:if test="${order.status.code ne 'PLACED' and order.status.code ne 'CONFIRMED' and order.status.code ne 'PACKED' and order.status.code ne 'DISPATCHED_FROM_WAREHOUSE'}">
							<button type="submit" class="btn btn-primary" onclick="callReturn();"><spring:theme code="text.account.profile.returnOrder" text="Return"/></button>		
							<button type="submit" class="btn btn-primary" onclick="callExchange();"><spring:theme code="text.account.profile.exchangeOrder" text="Exchange"/></button>		
						</c:if>	
						</form:form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr id="show_hide_entries">	
				<th class="center"><a><spring:theme  code="text.order.consignment.hideItemization" text="hide Itemization" ></a></spring:theme></th>
			</tr>
		</tfoot>
	</table>
</div>
