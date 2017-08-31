<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/formElement"%>
<%@ taglib prefix="multi-checkout"
	tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/common"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/address"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/cart"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/checkout/multi" %>

<style>

.checkTab .address-list.hideItem {
display: none;
}

</style>

<input type="hidden" id="currentPageName" value="${progressBarClass}">
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">
<cart:tealiumCartParameters/>
	<!-- <div class="checkout-headline">
		<spring:theme code="checkout.multi.secure.checkout" text="Secure Checkout"></spring:theme>
	</div> -->
	
	
	<div class="checkout-content cart checkout delivery row">
	<!-- store url fix -->
	<script type="text/javascript" src="/_ui/responsive/common/js/jquery-2.1.1.min.js"></script>
	 <div class="main"><div class="col-md-8">
		<c:if test="${showDeliveryMethod eq true}">
				<script>
					//window.onload =	function(){
					//resetConvChargeElsewhere(); Commented for TIS 400
					//}
					$(document).ready(function() {
						selectDefaultDeliveryMethod();
						
						
					});
					var timeoutID;
					function setup() {
						this.addEventListener("mousemove", resetTimer, false);
						this.addEventListener("mousedown", resetTimer, false);
						this.addEventListener("keypress", resetTimer, false);
						this.addEventListener("DOMMouseScroll", resetTimer,
								false);
						this.addEventListener("mousewheel", resetTimer, false);
						this.addEventListener("touchmove", resetTimer, false);
						this.addEventListener("MSPointerMove", resetTimer,
								false);
						startTimer();
					}
					setup();

					function startTimer() {
						// wait 2 seconds before calling goInactive
						timeoutID = window.setTimeout(goInactive, '${timeout}');
					}

					function resetTimer(e) {
						window.clearTimeout(timeoutID);

						goActive();
					}

					function goInactive() {
						window.location = '${request.contextPath}/cart';
					}

					function goActive() {
						startTimer();
					}
				</script>
				   <c:set var="progressBarClass" value="${progressBarClass}" />
               <c:set var="paymentPage" value="${paymentPage}" />
               
					<div class="delivery-address progress-barcheck  ${progressBarClass}  ${paymentPage}">
					   <div class="step-1 active"><a href="/checkout/multi/checkoutlogin/login" class="step-head js-checkout-step">Delivery Method<i class="arrow"></i></a><span class="paymentStepDone">Showing delivery options for ${defaultPincode}</span>
					   </div>
					</div>
					<div class="deliverymethod clearfix">
						<ycommerce:testId code="checkoutStepTwo">
						
						<form:form id="selectDeliveryMethodForm"
									action="${request.contextPath}/checkout/multi/delivery-method/check"
									method="post" commandName="deliveryMethodForm">
								<!-- TISCR-305 starts -->
								<!-- TISPRO-625 starts -->
								<input type="hidden" id="isExpressCheckoutSelected"
												value="${isExpressCheckoutSelected}" />
									
								<!-- TISPRO-625 ends -->
								<!-- TISCR-305 ends -->
									<div class="checkout-shipping left-block">
									
										<div class="checkout-indent">
											<%-- <form:form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/multi/delivery-method/select" method="post" commandName="deliveryMethodForm"> --%>
													<multi-checkout:shipmentItems cartData="${cartData}"
														defaultPincode="${defaultPincode}" showDeliveryAddress="true" />
									
										</div>
										
										
										
									</div>
									
									<c:choose>
										<c:when test="${isExpressCheckoutSelected}">
												<button class="button proceed-button" id="deliveryMethodSubmitUp" type="submit"
														class="checkout-next">
														<spring:theme
															code="checkout.multi.deliveryMethod.expresscheckout.continue"
															text="Continue" />
													</button>
										</c:when>
										<c:otherwise>
													<button class="button proceed-button" id="deliveryMethodSubmitUp"
														type="submit" class="checkout-next">
														<spring:theme code="checkout.multi.deliveryMethod.continue.lux"
															text="Continue" />
													</button>
										</c:otherwise>
									</c:choose>
									
							</form:form>
							
						</ycommerce:testId>
					</div>
					
		</c:if>
		

<div class="deliver-method progress-barcheck ${progressBarClass}  ${paymentPage}">
 <div class="step-1"><a href="/checkout/multi/checkoutlogin/login" class="step-head js-checkout-step">Delivery Method<i class="arrow"></i></a><span class="paymentStepDone"></span></div>
				   <div class="step-2"><a href="/checkout/multi/delivery-method/select" class="step-head js-checkout-step ">Delivery Address<i class="arrow"></i></a><span class="paymentStepDone"></span></div>
				</div>
				<div class="deliveryaddress ${progressBarClass}">

		<c:if test="${showAddress eq true}">
			<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}"
				progressBarId="${progressBarId}">
				<jsp:body>
			<script>
				//TISST-13010
				$(document).ready(function() {
					showPromotionTag();
				});

				var timeoutID;
				function setup() {
					this.addEventListener("mousemove", resetTimer, false);
					this.addEventListener("mousedown", resetTimer, false);
					this.addEventListener("keypress", resetTimer, false);
					this.addEventListener("DOMMouseScroll", resetTimer, false);
					this.addEventListener("mousewheel", resetTimer, false);
					this.addEventListener("touchmove", resetTimer, false);
					this.addEventListener("MSPointerMove", resetTimer, false);
					startTimer();
				}
				setup();

				function startTimer() {
					// wait 2 seconds before calling goInactive
					timeoutID = window.setTimeout(goInactive, '${timeout}');
				}

				function resetTimer(e) {
					window.clearTimeout(timeoutID);

					goActive();
				}

				function goInactive() {
					window.location = '${request.contextPath}/cart';
				}

				function goActive() {
					startTimer();
				}
			</script>
				<ycommerce:testId code="checkoutStepTwo"> 
					<div class="checkout-shipping checkTab"> 
								<c:if test="${not empty deliveryAddresses}"> 
					
										<form id="selectAddressForm"
											action="${request.contextPath}/checkout/multi/delivery-method/select-address"
												method="get">
											<c:set var='countWork' value='1' />
											<c:set var='countHome' value='1' />
							
						<!-- change here for modified checkout page starts -->
									
									<div class="acc_with_icon">
 									<div class="choose-address accordion_in acc_active">
    
      								<div class="acc_head">
												<div class="acc_icon_expand"></div>
												<h2>Choose Address</h2>
												<p class="cart-items">
													<spring:theme code="checkout.multi.shipment.items"
														arguments="${cartData.deliveryItemsQuantity}"
														text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme>
												</p>
									</div>
      
	  	
	  								<div class="acc_content" style="display:block">
		
      					<form>
								                <div class="addressList_wrapper">
								                <c:forEach items="${deliveryAddresses}" var="deliveryAddress"
																						varStatus="status">
																						
										<c:if test="${status.last}">
												<c:set var="deliveryAddressCount" value="${status.index}"></c:set>
										</c:if>	
										
												
										<c:choose>
											<c:when test="${status.index eq 0 ||  status.index eq 1 ||  status.index eq 2 ||  status.index eq 3}"> 
													 <c:set var="showItem" value="showItem"></c:set>
													</c:when>
											<c:otherwise>
																					
											 <c:set var="showItem" value="hideItem"></c:set>
											</c:otherwise>
										</c:choose>	
																								
								        <div class="address-list ${showItem}" id="${singleAddress}">
								          <span class="edit">
										  		<a	href="${request.contextPath}/checkout/multi/delivery-method/edit-address/${deliveryAddress.id}"
																								class="edit_address" id="link_${deliveryAddress.id}"><i class="fa fa-pencil"></i></a>
										  </span>
                                          <div class="address-details">
								        	<div class="addresslist-left le-radio">
								        	<c:choose>
												<c:when test="${deliveryAddress.defaultAddress}">
									       	
									          <input type="radio" class="radio1" name="selectedAddressCode"
																											value="${deliveryAddress.id}"
																											id="radio_${deliveryAddress.id}" checked="checked" />
									          <label for="radio_${deliveryAddress.id}"></label>
									           </c:when>
									           <c:otherwise>
									           	<input type="radio" class="radio1"
																											name="selectedAddressCode"
																											value="${deliveryAddress.id}"
																											id="radio_${deliveryAddress.id}" />
									          	<label for="radio_${deliveryAddress.id}"></label>
									           </c:otherwise>
								           
								           </c:choose>
								           </div>
								           <div class="addresslist-right">
								          
								          <p class="address"> 
								          <c:choose>
								          <c:when test="${deliveryAddress.addressType eq 'Work'}">
								          <span class="name commercial"
																											for="radio_${deliveryAddress.id}">
								          <spring:theme
																												code="checkout.multi.deliveryAddress.commercialAddress"
																												text="Commercial Addresses" />
								          <c:if test="${deliveryAddress.defaultAddress}">
								          - <spring:theme
																													code="checkout.multi.deliveryAddress.defaultAddress"
																													text="Default Addresses" /> <br />
								          </c:if>
								          </span> 
								          
								          <c:set var='countWork' value='${countWork+1}' />
								          </c:when>
								          <c:otherwise>
								           <span class="name residential"
																											for="radio_${deliveryAddress.id}" ${countHome}>
								           <spring:theme
																												code="checkout.multi.deliveryAddress.residentialAddress"
																												text="Residential Addresses" />
								           <c:out value="${countHome}"></c:out>
								           <c:if test="${deliveryAddress.defaultAddress}">
								           - <spring:theme
																													code="checkout.multi.deliveryAddress.defaultAddress"
																													text="Default Addresses" /> <br />
								           </c:if>
								           </span>
								           <c:set var='countHome'  value='${countHome+1}' />
								          </c:otherwise>
								          
								          </c:choose>
								          
								          <c:set var="myline2" value="${fn:trim(deliveryAddress.line2)}" />
										  <c:set var="myline3" value="${fn:trim(deliveryAddress.line3)}" />
										  
										  <c:if test="${empty myline2  && empty myline3}">
										  <span style="padding-bottom: 0px;">
										  
										  ${fn:escapeXml(deliveryAddress.title)}</span>	
										  <span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
										   <span>${fn:escapeXml(fn:replace(deliveryAddress.line1, '%20', ' '))},&nbsp;${fn:escapeXml(deliveryAddress.landmark)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;</span><span>${fn:escapeXml(deliveryAddress.state)},&nbsp;
										 ${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
										 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																									<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
																									</c:if>
										 <span> <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br></span>
										 
										  </c:if>
										  
										  <c:if test="${not empty myline2  && empty myline3}">
										   <span style="padding-bottom: 0px;">
										   ${fn:escapeXml(deliveryAddress.title)}</span>
										  <span class="name"> ${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
										 <span>${fn:escapeXml(fn:replace(deliveryAddress.line1, '%20', ' '))},&nbsp;${fn:escapeXml(fn:replace(deliveryAddress.line2, '%20', ' '))},&nbsp;${fn:escapeXml(deliveryAddress.landmark)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;</span> <span>${fn:escapeXml(deliveryAddress.state)},&nbsp;
										${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
										 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																									<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
																									</c:if>
										  <span> <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br></span>
										 
										  </c:if>
										  <c:if test="${ empty myline2  && not empty myline3}">
										  
										  <span style="padding-bottom: 0px;">${fn:escapeXml(deliveryAddress.title)}</span>
										 <span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
										 <span> ${fn:escapeXml(fn:replace(deliveryAddress.line1, '%20', ' '))},&nbsp;${fn:escapeXml(fn:replace(deliveryAddress.line3, '%20', ' '))},&nbsp;${fn:escapeXml(deliveryAddress.landmark)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;</span><span>${fn:escapeXml(deliveryAddress.state)},&nbsp; 
										 ${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
										 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																									<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span> ${fn:escapeXml(deliveryAddress.region.name)}</span>
																									</c:if>
										 <span> <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br></span>
										
										  
										  </c:if>
										  
										  <c:if test="${not empty myline2  && not empty myline3}">
										  <span style="padding-bottom: 0px;">
										  ${fn:escapeXml(deliveryAddress.title)}</span>
																									<span class="name"> ${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
										  <span>${fn:escapeXml(fn:replace(deliveryAddress.line1, '%20', ' '))},&nbsp;${fn:escapeXml(fn:replace(deliveryAddress.line2, '%20', ' '))},&nbsp;${fn:escapeXml(fn:replace(deliveryAddress.line3, '%20', ' '))},&nbsp;${fn:escapeXml(deliveryAddress.landmark)},&nbsp;
										${fn:escapeXml(deliveryAddress.town)},&nbsp;</span><span>${fn:escapeXml(deliveryAddress.state)},&nbsp;${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
										 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																									<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span> ${fn:escapeXml(deliveryAddress.region.name)}</span>
																									</c:if>
										 <span> <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br></span>
										  
										  </c:if>
										  
								 	<c:choose>
								 	<c:when test="${deliveryAddress.defaultAddress}">
<%-- 								 		<span class="edit">
										  <a	href="${request.contextPath}/checkout/multi/delivery-method/edit-address/${deliveryAddress.id}"
																								class="edit_address" id="link_${deliveryAddress.id}">Edit</a>
										  </span> --%>
								 		<%-- <span class="default default-selected">
								 	  <input type="radio" value="Make this my default address"
								 																class="regular-radio" name="default"
								 																id="radio-default2_${deliveryAddress.id}"
								 																data-address-id="${deliveryAddress.id}">				      
								 	  <label class="radio-checked" for="radio-default2_${deliveryAddress.id}">Make this my default address</label>
								 	  </span> --%>
								 	</c:when>
								 		</c:choose>
								
								
										<%--   <span class="default">
										  <input type="radio" value="Make this my default address"
																									class="regular-radio" name="default"
																									id="radio-default2_${deliveryAddress.id}"
																									data-address-id="${deliveryAddress.id}">				      
										  <label for="radio-default2_${deliveryAddress.id}">Make this my default address</label>
										  </span> --%>
										  </p>

										  
										    <c:set var="adressid" value="${deliveryAddress.id}" />
										  
										  <div class="editnewAddresPage" id="${adressid}"></div>
								           </div></div>
								           </div>
								           </c:forEach>
										</div> 
								 <div class="btn-section"><button id="deliveryAddressSubmitUp" type="submit" class="button checkout-next" >Continue</button></div>
	  <%-- <div class="formaddress" style="display: none;">
		<div class="heading-form">
														<h3>Add New Address</h3>
														<input type="button" value="cancel" class="cancelBtn">
													</div>
	  
	  
	   <div class="checkout-indent left-block address-form">
								
									<ul class="product-block addresses new-form account-section">
									  	
									  	<li
																class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector
																	supportedCountries="${countries}" regions="${regions}"
																	cancelUrl="${currentStepUrl}" />
									  	</li>
									  	</ul>
									  	
									</div>
	  
	  </div> --%>
	  <c:choose>
	  <c:when test="${deliveryAddressCount eq 0}">
				<c:set var="deliveryAddressClass" value="single_address"></c:set>
		</c:when>
		<c:when test="${deliveryAddressCount eq 1}">
				<c:set var="deliveryAddressClass" value="two_address"></c:set>
		</c:when>
		<c:when test="${deliveryAddressCount eq 2}">
				<c:set var="deliveryAddressClass" value="three_address"></c:set>
		</c:when>
		<c:when test="${deliveryAddressCount eq 3}">
				<c:set var="deliveryAddressClass" value="four_address"></c:set>
		</c:when>
		
		<%-- <c:when test="${deliveryAddressCount gt 3 && deliveryAddressCount % 2 == 0}">
				<c:set var="deliveryAddressClass" value="moreEvens_address"></c:set>
		</c:when> --%>
		</c:choose>
		
	  
							 <div class="addNew_wrapper ${deliveryAddressClass}">	  
							  
							     <c:if test="${deliveryAddressCount gt 3}">
							    <!--  <li style="float:left;width:500px;"><a href="#" class="viewMore">View More</a></li> -->	
							    <div class="add-address_viewMore viewMoreContainer">
						        <p>
																				<span class="addsign viewMoreSign">
						        
						        </span>
						        <a class="viewMore"> 
								View More Address</a>
						        </p>
								
						      </div>
							  </c:if>
							  <div class="addnewAddresPage"></div>
						     <div class="add-address" style="display: block;">
						        <p id="address-form">
																				<span class="addsign pincode-button"> <i class="fa fa-plus" aria-hidden="true"></i>
						        
						        </span>
						        <a class="pincode-button"> 
								<spring:theme code="checkout.multi.deliveryAddress.useNewAddress"
																						text="Use New Address"></spring:theme>
								</a>
						        </p>
								
						      </div>
						      
						    </div>
						    </form>
	  
	  </div> 
   
  </div>
 
</div>
				
				
									
									<!-- change here for modified checkout page ends -->
						
						</form>
						
									</c:if>
									
						<!--  If no address is present -->
						<c:if test="${empty deliveryAddresses}"> 
						<div id="emptyAddress" style="color:red;display:none;">Please select a delivery address</div>		
										<form id="selectAddressForm"
									action="${request.contextPath}/checkout/multi/delivery-method/select-address"
									method="get">
											<c:set var='countWork' value='1' />
											<c:set var='countHome' value='1' />
							
						<!-- change here for modified checkout page starts -->
									
									<div class="acc_with_icon">
  <div class="choose-address accordion_in acc_active">
    
      <div class="acc_head newAddressCenter">
												<div class="acc_icon_expand"></div>
												<h2>Add New Address</h2>
												<p class="cart-items">
													<spring:theme code="checkout.multi.shipment.items"
														arguments="${cartData.deliveryItemsQuantity}"
														text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme>
												</p>
											</div>
      
	  	<div class="acc_content" style="display: block;">
		
      <form>
        
          <div class="addressList_wrapper">
          <c:forEach items="${deliveryAddresses}" var="deliveryAddress"
														varStatus="status">
        <div class="address-list ${singleAddress}">
        	<c:choose>
			<c:when test="${deliveryAddress.defaultAddress}">
          <input type="radio" class="radio1" name="selectedAddressCode"
																		value="${deliveryAddress.id}"
																		id="radio_${deliveryAddress.id}" checked="checked" />
          <label for="radio_${deliveryAddress.id}"></label>
           </c:when>
           <c:otherwise>
           	<input type="radio" class="radio1"
																		name="selectedAddressCode"
																		value="${deliveryAddress.id}"
																		id="radio_${deliveryAddress.id}" />
          	<label for="radio_${deliveryAddress.id}"></label>
           </c:otherwise>
           
           </c:choose>
           
          
          <p class="address"> 
          <c:choose>
          <c:when test="${deliveryAddress.addressType eq 'Work'}">
          <span class="name commercial"
																			for="radio_${deliveryAddress.id}">
          <spring:theme
																				code="checkout.multi.deliveryAddress.commercialAddress"
																				text="Commercial Addresses" />
          <c:if test="${deliveryAddress.defaultAddress}">
          - <spring:theme
																					code="checkout.multi.deliveryAddress.defaultAddress"
																					text="Default Addresses" /> <br />
          </c:if>
          </span> 
          
          <c:set var='countWork' value='${countWork+1}' />
          </c:when>
          <c:otherwise>
           <span class="name residential"
																			for="radio_${deliveryAddress.id}">
           <spring:theme
																				code="checkout.multi.deliveryAddress.residentialAddress"
																				text="Residential Addresses" />
           <c:out value="${countHome}"></c:out>
           <c:if test="${deliveryAddress.defaultAddress}">
           - <spring:theme
																					code="checkout.multi.deliveryAddress.defaultAddress"
																					text="Default Addresses" /> <br />
           </c:if>
           </span>
           <c:set var='countHome'  value='${countHome+1}' />
          </c:otherwise>
          
          </c:choose>
          
          <c:set var="myline2" value="${fn:trim(deliveryAddress.line2)}" />
		  <c:set var="myline3" value="${fn:trim(deliveryAddress.line3)}" />
		  
		  <c:if test="${empty myline2  && empty myline3}">
		  <span style="padding-bottom: 0px;">
		  
		  ${fn:escapeXml(deliveryAddress.title)}</span>	
		  <span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		  <span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.landmark)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.state)},&nbsp;
		 ${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		  <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br>
		  
		  </c:if>
		  
		  <c:if test="${not empty myline2  && empty myline3}">
		   <span style="padding-bottom: 0px;">
		   ${fn:escapeXml(deliveryAddress.title)}</span>
		  <span class="name"> ${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		   <span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line2)},&nbsp;${fn:escapeXml(deliveryAddress.landmark)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.state)},&nbsp;
		${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		   <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br>
		  
		  </c:if>
		  <c:if test="${ empty myline2  && not empty myline3}">
		  
		  <span style="padding-bottom: 0px;">${fn:escapeXml(deliveryAddress.title)}</span>
		 <span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		 <span> ${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line3)},&nbsp;${fn:escapeXml(deliveryAddress.landmark)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.state)},&nbsp; 
		 ${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span> ${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		  <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br>
		
		  </c:if>
		  
		  <c:if test="${not empty myline2  && not empty myline3}">
		  <span style="padding-bottom: 0px;">
		  ${fn:escapeXml(deliveryAddress.title)}</span>
																	<span class="name"> ${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		  <span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line2)},&nbsp;${fn:escapeXml(deliveryAddress.line3)},&nbsp;${fn:escapeXml(deliveryAddress.landmark)},&nbsp;
		${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.state)},&nbsp;${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span> ${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		  <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br>
		  
		  </c:if>
		  
		  <span class="default">
		  <input type="radio" value="Make this my default address"
																	class="regular-radio" name="default"
																	id="radio-default2_${deliveryAddress.id}"
																	data-address-id="${deliveryAddress.id}">				      
		  <label for="radio-default2_${deliveryAddress.id}">Make this my default address</label>
		  </span>
		  </p>
		  <span class="edit">
		  <a href="${request.contextPath}/checkout/multi/delivery-method/edit-address/${deliveryAddress.id}"
																class="edit_address" id="link_${deliveryAddress.id}"></a>
		  </span>
		  
		    <c:set var="adressid" value="${deliveryAddress.id}" />
		  
		  <div class="editnewAddresPage" id="${adressid}"></div>
           </div>
           </c:forEach>
		</div> 
      </form>
	  <%-- <div class="formaddress" style="display: none;">
		<div class="heading-form">
														<h3>Add New Address</h3>
														<input type="button" value="cancel" class="cancelBtn">
													</div>
	  
	  
	   <div class="checkout-indent left-block address-form">
								
									<ul class="product-block addresses new-form account-section">
									  	
									  	<li
																class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector
																	supportedCountries="${countries}" regions="${regions}"
																	cancelUrl="${currentStepUrl}" />
									  	</li>
									  	</ul>
									  	<!-- <input type="submit" value="save" class="saveBtn"> -->
									</div>
	  
	  </div> --%>
	  <div class="addNew_wrapper no_address">
	   <div class="addnewAddresPage"></div>
     <div class="add-address" style="display: block;">
        <p id="address-form">
														<span class="addsign pincode-button">
        
        </span>
        <a class="pincode-button"> 
		<spring:theme code="checkout.multi.deliveryAddress.useNewAddress"
																text="Use New Address"></spring:theme>
		</a>
        </p>
		
      </div>
      </div>
	  
	  </div> 
   
  </div>
 
</div>
				
				
									
									<!-- change here for modified checkout page ends -->
						
						</form>
						
									</c:if>								
									
									
									
									
									
					</div>
					
	</div>
	</ycommerce:testId>
	</jsp:body>
	</multi-checkout:checkoutSteps>
	</c:if>



	<c:if test="${showEditAddress eq true}">
<%-- 		<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}"
			progressBarId="${progressBarId}">
			<jsp:body> --%>
					<script>
						//TISST-13010
						$(document).ready(function() {
							showPromotionTag();
						});
						var timeoutID;
						function setup() {
							this.addEventListener("mousemove", resetTimer,
									false);
							this.addEventListener("mousedown", resetTimer,
									false);
							this
									.addEventListener("keypress", resetTimer,
											false);
							this.addEventListener("DOMMouseScroll", resetTimer,
									false);
							this.addEventListener("mousewheel", resetTimer,
									false);
							this.addEventListener("touchmove", resetTimer,
									false);
							this.addEventListener("MSPointerMove", resetTimer,
									false);
							startTimer();
						}
						setup();

						function startTimer() {
							// wait 2 seconds before calling goInactive
							timeoutID = window.setTimeout(goInactive,
									'${timeout}');
						}

						function resetTimer(e) {
							window.clearTimeout(timeoutID);

							goActive();
						}

						function goInactive() {
							window.location = '${request.contextPath}/cart';
						}

						function goActive() {
							startTimer();
						}
					</script>
					<ycommerce:testId code="checkoutStepTwo">
						<div class="checkout-shipping">
						<c:choose>
					<c:when test="${edit eq true}">
						<ycommerce:testId code="multicheckout_saveAddress_button">
							<button id="editAddressButtonUp"
										class="btn btn-primary btn-block" type="submit">
								<spring:theme code="checkout.multi.saveAddress"
											text="Save address" />
							</button>
						</ycommerce:testId>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${accountPageAddress eq true}">
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonAccountUp"
												class=" btn btn-primary btn-block" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue"
													text="Continue" />
									</button>
								</ycommerce:testId>
							</c:when>
							<c:otherwise>
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonUp" class="button" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue"
													text="Continue" />
									</button>
								</ycommerce:testId>
							</c:otherwise>
						</c:choose>						
					</c:otherwise>
				</c:choose> 
							<div class="checkout-indent left-block address-form">
								<h1>
									<spring:theme code="checkout.summary.shippingAddress"
									text="Shipping Address"></spring:theme>
								</h1>
								<div class="checkout-shipping-items-header">
								<spring:theme code="checkout.multi.shipment.items"
									arguments="${cartData.deliveryItemsQuantity}"
									text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme>
							</div>
								<ul class="product-block addresses new-form account-section">
									  	<li class="header">
									  		<ul class="account-section-header">
									        <li><spring:theme
												code="text.account.addressBook.addressDetails" /></li>
									        <li class="pincode-button"><a
											href="${request.contextPath}/checkout/multi/delivery-method/selectaddress">
									       
									       
									        <c:if test="${addressFlag eq 'T'}"> 
										<spring:theme
														code="checkout.multi.deliveryAddress.useSavedAddress"
														text="Use a Saved Address"></spring:theme>	
										</c:if>																
									</a></li>
									      </ul>
									  	</li>
									  	<li
									class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector
										supportedCountries="${countries}" regions="${regions}"
										cancelUrl="${currentStepUrl}" country="${country}" />
									  	</li>
									  	</ul>
							</div>
						</div>
					</ycommerce:testId>
			<%--	</jsp:body>
		 </multi-checkout:checkoutSteps> --%>
	</c:if>


	<c:if test="${addAddress eq true}">

		<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}"
			progressBarId="${progressBarId}">
			<jsp:body>
					<script>
						//TISST-13010
						$(document).ready(function() {
							showPromotionTag();
						});
						var timeoutID;
						function setup() {
							this.addEventListener("mousemove", resetTimer,
									false);
							this.addEventListener("mousedown", resetTimer,
									false);
							this
									.addEventListener("keypress", resetTimer,
											false);
							this.addEventListener("DOMMouseScroll", resetTimer,
									false);
							this.addEventListener("mousewheel", resetTimer,
									false);
							this.addEventListener("touchmove", resetTimer,
									false);
							this.addEventListener("MSPointerMove", resetTimer,
									false);
							startTimer();
						}
						setup();

						function startTimer() {
							// wait 2 seconds before calling goInactive
							timeoutID = window.setTimeout(goInactive,
									'${timeout}');
						}

						function resetTimer(e) {
							window.clearTimeout(timeoutID);

							goActive();
						}

						function goInactive() {
							window.location = '${request.contextPath}/cart';
						}

						function goActive() {
							startTimer();
						}
					</script>
					<ycommerce:testId code="checkoutStepTwo">
						<div class="checkout-shipping">
					<c:choose>
					<c:when test="${edit eq true}">
						<ycommerce:testId code="multicheckout_saveAddress_button">
							<button id="editAddressButtonUp"
										class="btn btn-primary btn-block" type="submit">
								<spring:theme code="checkout.multi.saveAddress"
											text="Save address" />
							</button>
						</ycommerce:testId>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${accountPageAddress eq true}">
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonAccountUp"
												class=" btn btn-primary btn-block" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue"
													text="Continue" />
									</button>
								</ycommerce:testId>
							</c:when>
							<c:otherwise>
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonUp" class="button" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue"
													text="Continue" />
									</button>
								</ycommerce:testId>
							</c:otherwise>
						</c:choose>						
					</c:otherwise>
				</c:choose> 
							<div class="checkout-indent left-block address-form">
								<h1>
									<spring:theme code="checkout.summary.shippingAddress"
									text="Shipping Address"></spring:theme>
								</h1>
								<div class="checkout-shipping-items-header">
								<spring:theme code="checkout.multi.shipment.items"
									arguments="${cartData.deliveryItemsQuantity}"
									text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme>
							</div>
									<ul class="product-block addresses new-form account-section">
									  	<li class="header">
									  		<ul class="account-section-header">
									        <li><spring:theme
												code="checkout.multi.deliveryAddress.newAddress"
												text="New Address"> </spring:theme> </li>																  			 			 							        
									        <li class="pincode-button"><a
											href="${request.contextPath}/checkout/multi/delivery-method/selectaddress">
									        
									         <c:if test="${addressFlag eq 'T'}"> 
											<spring:theme
														code="checkout.multi.deliveryAddress.useSavedAddress"
														text="Use a Saved Address"></spring:theme>
											 </c:if>
										</a></li>
										
									      </ul>
									  	</li>
									  	<li
									class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector
										supportedCountries="${countries}" regions="${regions}"
										cancelUrl="${currentStepUrl}" />
									  	</li>
									  	</ul>
									</div>
							</div>
						
					</ycommerce:testId>
				</jsp:body>
			</multi-checkout:checkoutSteps>			
				
		</c:if>
		
		<div class="payment progress-barcheck ${progressBarClass}  ${paymentPage}">
		 <div class="step-1"><a href="/checkout/multi/checkoutlogin/login" class="step-head js-checkout-step">Delivery Method<i class="arrow"></i></a><span class="paymentStepDone"></span></div>
				   <div class="step-2"><a href="/checkout/multi/delivery-method/choose" class="step-head js-checkout-step ">Delivery Address<i class="arrow"></i></a><span class="paymentStepDone"></span></div>
				  <div class="step-3"><a href="/checkout/multi/checkoutlogin/login" class="step-head js-checkout-step">Payment<i class="arrow"></i></a><span class="paymentStepDone"></span></div>
				</div>
	</div></div>
		
	<div class="right-block shipping col-md-4">
			<div class="orderSummary">
			<div class="order-summary-header"><h2>Summary</h2></div>
				<multi-checkout:orderTotals cartData="${cartData}"
					showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" />
				<c:if test="${showDeliveryMethod eq true}">
		
		</c:if>
		</div>
		<div class="coupon-code  mt-20"><multi-checkout:coupons isCart="${isCart}"/></div>
	
	<div class="helpLine mt-20 mb-20">
		<p class="text-center grayTxt normalSizeRegularTxt">
			Need Help With Placing Your Order?
			<strong class="mediumSizeTxt mt-10 mb-10">Call us at 91-901 924 5000</strong>
			<small class="smallSizeRegularTxt">Lines open from 8:00 AM to 11:00 PM</small>
		</p>
	</div>
	
	<div class="card-details mt-20 mb-20">
		<ul>
		 <li class="ssl"></li>
		 <li class="use-card"></li>
		</ul>
	</div>
	</div>	
						
</div>
</template:page>

