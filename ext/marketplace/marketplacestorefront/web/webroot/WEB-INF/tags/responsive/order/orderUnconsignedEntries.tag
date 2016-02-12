<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
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
<div class="orderList">
	<div class="headline"><spring:theme code="order.orderItems" text="Order Items"/></div>
	
	<table class="orderListTable">
		<thead>
			<tr>
				<th id="header2" colspan="2"><spring:theme code="text.productDetails" text="Product Details"/></th>
				<th id="header4"><spring:theme code="text.quantity" text="Quantity"/></th>
				<th id="header5"><spring:theme code="text.itemPrice" text="Item Price"/></th>
				<th id="header6"><spring:theme code="text.total" text="Total"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${order.unconsignedEntries}" var="entry">
			<div class="account-orderdetail-item-section-body">
				<c:url value="${entry.product.url}" var="productUrl"/>
				<tr class="item">
					<td headers="header2" class="thumb">
						<a href="${productUrl}">
							<product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
						</a>
					</td>
					<td headers="header2" class="details">
					
							<ycommerce:testId code="orderDetails_productName_link">
								<div class="itemName"><a href="${entry.product.purchasable ? productUrl : ''}">${entry.product.name}</a></div>
							</ycommerce:testId>
						
						<c:forEach items="${entry.product.baseOptions}" var="option">
						<div class="thumb">
							<c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
								<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
									<dl>
										<dt>${selectedOption.name}:</dt>
										<dd>${selectedOption.value}</dd>
									</dl>
								</c:forEach>
							</c:if>
							</div>
						</c:forEach>
						<c:if test="${not empty order.appliedProductPromotions}">
							<ul>
								<c:forEach items="${order.appliedProductPromotions}" var="promotion">
									<c:set var="displayed" value="false"/>
									<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
										<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
											<c:set var="displayed" value="true"/>
											<li><span>${promotion.description}</span></li>
										</c:if>
									</c:forEach>
								</c:forEach>
							</ul>
						</c:if>
					</td>
				
					<td headers="header4" class="quantity" width="30%">
						<ycommerce:testId code="orderDetails_productQuantity_label">${entry.quantity}</ycommerce:testId>
					</td>
				
					<td headers="header5" width="30%">
						<ycommerce:testId code="orderDetails_productItemPrice_label"><format:price priceData="${entry.basePrice}" displayFreeForZero="true"/></ycommerce:testId>
					</td>
					<td headers="header6" class="total" >
						<ycommerce:testId code="orderDetails_productTotalPrice_label"><format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/></ycommerce:testId>
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
				</div>
			</c:forEach>
		</tbody>
	</table>
	
	
	
	
</div>
