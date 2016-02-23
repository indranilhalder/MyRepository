<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="multi"
	tagdir="/WEB-INF/tags/addons/b2ccheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<template:page pageTitle="${pageTitle}" >

<!--- START: INSERTED for MSD --->
<c:if test="${isMSDEnabled}">
<input type="hidden" value="${isMSDEnabled}" name="isMSDEnabled" />
	<c:forEach items="${orderData.entries}" var="entryMSD">
		<c:if test="${entryMSD.product.rootCategory eq 'Clothing'}">
		        <c:set var="includeMSDJS" scope="request" value="true"/>
		        <input type="hidden" value="true" name="isApparelExist"/>
		</c:if>
	</c:forEach>	
	<c:if test="${includeMSDJS eq 'true'}">
		<script type="text/javascript"	src="${msdjsURL}"></script>
	</c:if> 
</c:if>
<!--- END:MSD --->

	<c:url value="${continueUrl}" var="continueShoppingUrl" scope="session" />

	<cms:pageSlot position="TopContent" var="feature" element="div">
		<cms:component component="${feature}" />
	</cms:pageSlot>

	<!-- <div class="container"> -->
	<div class="checkout-success confirmation">
		<%-- <div class="checkout-success-headline">
				<span class="glyphicon glyphicon-lock"></span> <spring:theme code="checkout.orderConfirmation.checkoutSuccessful" />
			</div> --%>
		<!-- START OF TOP COMPONENT -->
		<div class="checkout-success-body thanks">
			<div class="wrapper">
				<h3 class="checkout-success-body-headline">
					<spring:theme code="checkout.orderConfirmation.thankYouForOrder" />
				</h3>
				<p>
					<p>
						${orderStatusMsg}
					</p>
				</p>
				<p>
					<strong><spring:theme
							code="text.account.order.orderNumber" text="Order Number: #{0}"
							arguments="${orderData.code}" /></strong>
				</p>
				<p>
					<strong>
					<fmt:formatDate value="${orderData.created}" pattern="MMMM " var="month" />
					<fmt:formatDate value="${orderData.created}" pattern=", yyyy " var="year" />
					<spring:theme
							code="text.account.order.orderPlaced" text="Order Placed: "/>&nbsp;${month} ${date} ${year} </strong>
				</p>
				

				<%-- <p>
				**************************************
				Blocked for Defect : TISPRD-57
					<spring:theme code="checkout.orderConfirmation.copySentTo"
						arguments="${orderData.customerData.displayUid}" />
				****************************************
				</p> --%>
				
				
				<p>
					<spring:theme code="checkout.orderConfirmation.checkOrderStatus" />
				</p>

				<%-- <p><multi:pickupGroups2 orderData="${orderData}"/></p> --%>
				<%--                 <p><spring:theme code="text.account.order.orderNumber" text="Order number is {0}" arguments="${orderData.code}"/></p>
 --%>
				<%-- <c:if test="${not empty orderData.statusDisplay}">
					<spring:theme
						code="text.account.order.status.display.${orderData.statusDisplay}"
						var="orderStatus" />
					<p>
						<spring:theme code="text.account.order.orderStatus"
							text="The order is {0}" arguments="${orderStatus}" />
					</p>
				</c:if> --%>
			</div>
		</div>
		<!-- END OF TOP COMPONENT -->

		<div class="wrapper">

			<!-- START OF RIGHT BLOCK COMPONENT -->
			<div class="right-block billing">
				<div class="orderTotalsThankyou top block">
					<div class="span-16">
						<%-- <order:receivedPromotions order="${orderData}" /> --%>
					</div>
					<div class="span-8 right last">
						<order:orderTotalsItem order="${orderData}"
							containerCSS="positive" />
					</div>
				</div>
				<div class="orderBoxes clearfix bottom block">
					<order:paymentMethodItem order="${orderData}" />
				</div>
			</div>
			<!-- END OF RIGHT BLOCK COMPONENT -->

			<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
				<div class="span-24 delivery_stages-guest last">
					<user:guestRegister actionNameKey="guest.register.submit" />
				</div>
			</sec:authorize>
			<!-- <hr> -->

			<!-- START OF LEFT BLOCK COMPONENT -->
			<div class="left-block">
			
			<div class="orderBoxes clearfix addressThankyou">
                 	<c:set var="sellerOrder"  value="${orderData}" />
					<order:deliveryAddressItem order="${sellerOrder}" />					
					<%-- 	<p>"${totalCount}" ITEMS SHIPPING TO THE ADDRESS</p> --%>
					<%-- 	<order:deliveryMethodItem order="${orderData}"/> --%>

				</div>
					<ul class="product-block orderDetailsThankyou">
				<li class="header">
			<ul>
				<li id="header2"><spring:theme code="text.productDetails" text="Product Information"/></li>
				<%-- <li id="header4"><spring:theme code="text.quantity" text="Quantity"/></li>
				<li id="header5"><spring:theme code="text.itemPrice" text="Item Price"/></li> --%>
				<li class="shipping"><spring:theme code="text.deliveryMethod" text="Shipping Method:"/></li>
				<li id="header6" class="price"><spring:theme code="text.total" text="Price"/></li>
			</ul>
		</li>
				
			 <c:forEach items="${orderData.sellerOrderList}" var="sellerOrder">  
			 
			
					<%-- <p style="font-weight:500;font-size:16px;"><spring:theme
							code="text.account.order.orderNumber" text="Order number is {0}"
							arguments="${sellerOrder.code}" /></p> --%>
				
				
			
					   <b style="margin-left: 15px"><spring:theme code="seller.order.code"/>${sellerOrder.code}</b> 
					   
					<c:forEach items="${sellerOrder.deliveryOrderGroups}"
						var="orderGroup">
						<order:orderDetailsItem order="${sellerOrder}"
							orderGroup="${orderGroup}" parentOrder="${orderData}" />				
					</c:forEach>
					<c:forEach items="${sellerOrder.pickupOrderGroups}" var="orderGroup">
						<order:orderPickupDetailsItem order="${sellerOrder}"
							orderGroup="${orderGroup}"   parentOrder="${orderData}"/>
					</c:forEach>

					<%-- <div>
			    <button class="btn btn-default continueShoppingButton" data-continue-shopping-url="${continueShoppingUrl}"><spring:theme code="checkout.orderConfirmation.continueShopping"/></button>
            </div> --%>
         </c:forEach> 
				</ul>


				<cms:pageSlot position="SideContent" var="feature" element="div"
					class="span-24 side-content-slot cms_disp-img_slot">
					<cms:component component="${feature}" />
				</cms:pageSlot>
				
			</div>
			<!-- END OF RIGHT BLOCK COMPONENT -->
		</div>
	</div>



	<cms:pageSlot position="SideContent" var="feature" element="div">
		<cms:component component="${feature}" />
	</cms:pageSlot>
<!-- For Infinite Analytics Start -->
	<div class="trending"  id="ia_products"></div>
<!-- For Infinite Analytics End -->

<!-- for MSD -->
<script type="text/javascript">

$( document ).ready(function() {
    console.log( "MSD Tracking!" );
    var isMSDEnabled =  $("input[name=isMSDEnabled]").val();
    var isApparelExist  = $("input[name=isApparelExist]").val();
    
    if(typeof isMSDEnabled === 'undefined')
	{
		isMSDEnabled = false;						
	}
    
    if(typeof isApparelExist === 'undefined')
	{
		isApparelExist = false;						
	}
    
	if(Boolean(isMSDEnabled) && Boolean(isApparelExist))
    	{
		
		var orderDataForMSD = [
				<c:forEach items="${orderData.entries}" var="entryMSD" varStatus="status">
				
					<c:if test="${entryMSD.product.rootCategory eq 'Clothing'}">
					{
						"ProductId": "${entryMSD.product.code}",
						<c:if test="${not empty entryMSD.product.categories}">
						"CategoryId": "${entryMSD.product.categories[0].code}",
						</c:if>
						"Price":	"${entryMSD.basePrice.value}",
						"Currency":"${entryMSD.basePrice.currencyIso}"
					}
					</c:if>
					<c:if test="${not status.last}">,</c:if>
				</c:forEach> 
			  ];
		console.log(orderDataForMSD);
		try
		{			 
		  track(['buy',JSON.stringify(orderDataForMSD)]);		 
		}
		catch(err)
		{
			console.log('Error Adding trackers in order confirmation: '+err.message);	
		}
    	
    	}
});

</script>
<!-- end MSD -->


</template:page>