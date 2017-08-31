<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="false" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="groupData" required="true" type="de.hybris.platform.commercefacades.order.data.OrderEntryGroupData" %>
<%@ attribute name="index" required="true" type="java.lang.Integer" %>
<%@ attribute name="showPotentialPromotions" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showHead" required="false" type="java.lang.Boolean" %>
<!-- TPR-629 orderData added to tag parameters -->
<%@ attribute name="orderData" required="false" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ attribute name="isCart" required="false" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/product" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<style>
	.cncOrderInfo{
		list-style: none;
		clear: both;
		margin-left: 10px;
	}
	.thumb {
		width: 30%;
		float: left;
	}
	
	.allDetails {
		width: 70%;
		float: right;
	}
	
	
	.thumb a img {
		width: 95%;
	}
	.price {
		padding-top: 10px;
		font-size: 14px !important;
		margin-bottom: 5px;
	}
	.title {
		font-weight: 600;
	}
	.cart.wrapper .right-block .block .cncOrderInfo p, .confirmation .right-block .block .cncOrderInfo p {
		margin-bottom: 0px !important;
	}
	
	.cncOrderInfo .product-brand-name a {
		margin: 0;
	    padding: 0;
	    border: 0;
	    font-size: 100%;
	    font-size: 16px !important;
	    margin-bottom: 5px;
	    vertical-align: baseline;
	}
	
	.cncOrderInfo .product-brand-name {
		margin-bottom: 5px;
    }
    
    .cncOrderInfo .product-name {
    	font-size: 15px !important;
    	color: #fff;
    	font-weight: bold;
    	margin-bottom: 8px;
    }
    
    .cncOrderInfo .details .method h3 {
    	margin-top: 15px;
    	font-weight: bold;
    	font-size: 12px !important;
    }
    
    .cncOrderInfo .details .method {
    	margin-bottom: 30px;
    }
	
</style>
<c:if test="${showHead}">
	<li class="section cncOrderInfo">
		<div class="title"><p><spring:theme code="checkout.multi.items.to.pickup" text="Store Address:"/></p></div>
		<div class="address">
			<address>
				${groupData.deliveryPointOfService.displayName}
				<br>
				<c:if test="${ not empty groupData.deliveryPointOfService.address.line1 }">
							${fn:escapeXml(groupData.deliveryPointOfService.address.line1)},&nbsp;<br>
				</c:if>
				<c:if test="${ not empty groupData.deliveryPointOfService.address.line2 }">
					${fn:escapeXml(groupData.deliveryPointOfService.address.line2)},&nbsp;<br>
				</c:if>
				<c:if test="${not empty groupData.deliveryPointOfService.address.town }">
					${fn:escapeXml(groupData.deliveryPointOfService.address.town)},&nbsp;<br>
				</c:if>
				<c:if test="${ not empty groupData.deliveryPointOfService.address.region.name }">
					${fn:escapeXml(groupData.deliveryPointOfService.address.region.name)},&nbsp;<br>
				</c:if>
				<c:if test="${ not empty groupData.deliveryPointOfService.address.postalCode }">
					${fn:escapeXml(groupData.deliveryPointOfService.address.postalCode)},&nbsp;<br>
				</c:if>
				<c:if test="${ not empty groupData.deliveryPointOfService.address.country.name }">
					${fn:escapeXml(groupData.deliveryPointOfService.address.country.name)}
				</c:if>
			</address>
		</div>
	</li>
</c:if>
<c:forEach items="${groupData.entries}" var="entry">		
	<c:url value="${entry.product.url}" var="productUrl"/>
		<li class="cncOrderInfo">
			<div class="thumb">
				<a href="${productUrl}">
					<product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
				</a>
			</div>
			<div class="allDetails">
				<p class="company"></p>
	            <h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3>
	            <h3 class="product-name"><a href="${productUrl}">${entry.product.name}</a></h3>
	            <p class="qty"><spring:theme code="basket.page.qty"/>&nbsp;${entry.quantity}</p>
	              <c:if test="${not empty entry.product.size}">
						<p class="size"><spring:theme code="text.size"/>&nbsp;${entry.product.size}</p>
				  </c:if>
				<div class="price"><format:price priceData="${entry.basePrice}" displayFreeForZero="true"/></div>
				<div class="details">
					<%-- <div class="name"><a href="${productUrl}">${entry.product.name}</a></div>
					<div class="qty"><spring:theme code="basket.page.qty"/>&nbsp;${entry.quantity}</div> --%>
					<div class="variants">
						<c:forEach items="${entry.product.baseOptions}" var="option">
							<c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
								<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
									<div>${selectedOption.name}: ${selectedOption.value}</div>
								</c:forEach>
							</c:if>
						</c:forEach>
					</div>
					<div class="method">
	                   <h3> <spring:theme code="checkout.multi.shipmentMethod"/></h3>
	                   <p class="delivery-method-description"><c:out value="${entry.mplDeliveryMode.name}"></c:out>&nbsp;-&nbsp;<c:if test="${entry.currDelCharge.value.unscaledValue() == 0}"><c:out value="FREE"></c:out></c:if><c:if test="${entry.currDelCharge.value.unscaledValue() != 0}"><c:out value="${entry.currDelCharge.formattedValue}"></c:out></c:if></p>
	                  <p class="delivery-method-description delivery-method-description-time"><c:out value="${entry.mplDeliveryMode.description}"></c:out></p>
	                </div>
	                <c:choose>
	                <c:when test="${isCart eq true}">
					<c:if test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber) && showPotentialPromotions && (entry.isBOGOapplied || entry.giveAway)}">
						<ul>
							<c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
								<c:set var="displayed" value="false"/>
								<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
									<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true"/>
										<span>${promotion.description}</span>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>
					<c:if test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber) && (entry.isBOGOapplied || entry.giveAway)}">
						<ul>
							<c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
								<c:set var="displayed" value="false"/>
								<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
									<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true"/>
										<span>${promotion.description}</span>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>
					</c:when>
					<c:otherwise>
					<c:if test="${ycommerce:doesAppliedPromoExistForOrderEntry(orderData, entry.entryNumber) && (entry.isBOGOapplied || entry.giveAway)}">
						<ul>
							<c:forEach items="${orderData.appliedProductPromotions}" var="promotion">
								<c:set var="displayed" value="false"/>
								<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
									<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true"/>
										<span>${promotion.description}</span>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>
					</c:otherwise>
					</c:choose>
				</div>
				
			</div>
		
			<!--  <div class="stock-status">PickUpInStore Availability
				Notification</div> -->
		
		</li>
</c:forEach>
