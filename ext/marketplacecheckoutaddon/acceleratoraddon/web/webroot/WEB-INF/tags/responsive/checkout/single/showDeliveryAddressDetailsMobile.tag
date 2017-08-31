<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="multi-checkout"
	tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ attribute name="deliveryAddresses" required="true"
	type="java.util.List"%>
<%@ attribute name="selectedAddress" required="false" type="java.lang.String"%>

<script>
	//TISST-13010
	$(document).ready(function() {
		showPromotionTag();
	});
</script>
<!-- Div to display ajax failure messages -->
<div id="selectedAddressMessageMobile" style=""></div>
<div class="checkout-shipping checkTab">

	<c:if test="${not empty deliveryAddresses}">	

		<form id="selectAddressFormMobile"
			action="/checkout/single/select-address-responsive"
			method="get">
			<!-- change here for modified checkout page starts -->

						<span id="emptyAddressMobile" style="display:none">Please select a delivery method to proceed.</span>

<div class="addressList_wrapper mobile_list">
						<c:set var="defaultAddressPresent" value="false"></c:set>
							<c:forEach items="${deliveryAddresses}" var="deliveryAddress"
								varStatus="status">

								<c:if test="${status.last}">
									<c:set var="deliveryAddressCount" value="${status.index}"></c:set>
								</c:if>


								<c:choose>
									<c:when
										test="${status.index eq 0 ||  status.index eq 1 ||  status.index eq 2 ||  status.index eq 3}">
										<c:set var="showItem" value="showItem"></c:set>
									</c:when>
									<c:otherwise>

										<c:set var="showItem" value="hideItem"></c:set>
									</c:otherwise>
								</c:choose>


								<div class="address-list ${showItem} <c:if test="${not deliveryAddress.defaultAddress}">mobileNotDefaultDelAddress</c:if>">
									<span class="edit"> 
										<a href="javascript:void(0)"
										onclick="event.stopPropagation();ACC.singlePageCheckout.getMobileEditAddress('${deliveryAddress.postalCode}','${deliveryAddress.id}');"></a>
									</span>
									 <div class="clickableDivMobile" style="cursor:pointer;" onclick="ACC.singlePageCheckout.checkPincodeServiceabilityForRespoinsive('${deliveryAddress.postalCode}','${deliveryAddress.id}',false,false);">
										<c:choose>
											<c:when test="${deliveryAddress.defaultAddress}">
												<input type="radio" class="radio1" name="selectedAddressCode"
													value="${deliveryAddress.id}"
													id="radio_mobile_${deliveryAddress.id}" checked="checked"  onclick="event.stopPropagation();"/>
												<label for="radio_mobile_${deliveryAddress.id}"></label>
												<span id="defaultAddressPincode" style="display:none;">${deliveryAddress.postalCode}</span>
												<span id="defaultAddressId" style="display:none;">${deliveryAddress.id}</span>
												<c:set var="defaultAddressPresent" value="true"></c:set>
											</c:when>
											<c:otherwise>
												<input type="radio" class="radio1" name="selectedAddressCode"
													value="${deliveryAddress.id}"
													id="radio_mobile_${deliveryAddress.id}"  onclick="event.stopPropagation();"/>
												<label for="radio_mobile_${deliveryAddress.id}"></label>
											</c:otherwise>
										</c:choose>	
										<p class="address">	
											<c:set var="myline2" value="${fn:trim(deliveryAddress.line2)}" />
											<c:set var="myline3" value="${fn:trim(deliveryAddress.line3)}" />
	
											<c:if test="${empty myline2  && empty myline3}">
												<span style="padding-bottom: 0px;">
	
													${fn:escapeXml(deliveryAddress.title)}</span>
												<span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
												<span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;</span>
												<span>${fn:escapeXml(deliveryAddress.state)},&nbsp;
													${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
													${fn:escapeXml(deliveryAddress.country.isocode)}
												</span>
												<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
												</c:if>
												<span> <spring:theme code="checkout.phone.no"
														text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}
													<br></span>
	
											</c:if>
	
											<c:if test="${not empty myline2  && empty myline3}">
												<span style="padding-bottom: 0px;">
													${fn:escapeXml(deliveryAddress.title)}</span>
												<span class="name">
													${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
												<span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line2)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;</span>
												<span>${fn:escapeXml(deliveryAddress.state)},&nbsp;
													${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
													${fn:escapeXml(deliveryAddress.country.isocode)}
												</span>
												<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
												</c:if>
												<span> <spring:theme code="checkout.phone.no"
														text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br></span>
	
											</c:if>
											<c:if test="${ empty myline2  && not empty myline3}">
	
												<span style="padding-bottom: 0px;">${fn:escapeXml(deliveryAddress.title)}</span>
												<span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
												<span>
													${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line3)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;</span>
												<span>${fn:escapeXml(deliveryAddress.state)},&nbsp;
													${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
													${fn:escapeXml(deliveryAddress.country.isocode)}
												</span>
												<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>
														${fn:escapeXml(deliveryAddress.region.name)}</span>
												</c:if>
												<span> <spring:theme code="checkout.phone.no"
														text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br></span>
	
	
											</c:if>
	
											<c:if test="${not empty myline2  && not empty myline3}">
												<span style="padding-bottom: 0px;">
													${fn:escapeXml(deliveryAddress.title)}</span>
												<span class="name">
													${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
												<span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line2)},&nbsp;${fn:escapeXml(deliveryAddress.line3)},&nbsp;
													${fn:escapeXml(deliveryAddress.town)},&nbsp;</span>
												<span>${fn:escapeXml(deliveryAddress.state)},&nbsp;${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
													${fn:escapeXml(deliveryAddress.country.isocode)}
												</span>
												<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>
														${fn:escapeXml(deliveryAddress.region.name)}</span>
												</c:if>
												<span> <spring:theme code="checkout.phone.no"
														text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br></span>
	
											</c:if>
	
										</p>	
									 </div> 
								</div>
								
							</c:forEach>
							<span id="defaultAddressPresent" style="display:none;">${defaultAddressPresent}</span>
						</div>


						<div class="addNew_wrapper" id="addNewAddressForResponsive">
							<div class="mobile_add_address mobileNotDefaultDelAddress" onclick="ACC.singlePageCheckout.getMobileAddAddress();">
							<span class="mobile_add_address_radio"></span>
							<spring:theme code="checkout.multi.deliveryAddress.useNewAddress.mobile" text="Use New Address"></spring:theme>
							</div>
							<span id="newAddressMobileErrorMessage"></span>
							<div id="newAddressFormMobile" class="new-address-form-mobile" data-loaded="false"></div>
						</div>
						<div class="addNew_wrapper" id="editAddressForResponsive">
							<div class="mobile_add_address mobileNotDefaultDelAddress">
							<span class="mobile_add_address_radio"></span>
							<spring:theme code="checkout.single.deliveryAddress.editAddress.mobile" text="Edit Address"></spring:theme>
							</div>
							<span id="editAddressMobileErrorMessage"></span>
							<div id="editAddressFormMobile" class="new-address-form-mobile" data-loaded="false"></div>
						</div>



			<!-- change here for modified checkout page ends -->

		</form>

	</c:if>

	<!--  If no address is present -->
	<c:if test="${empty deliveryAddresses}">
		<script>
		$(document).ready(function(){
			if(ACC.singlePageCheckout.getIsResponsive())
			{
				ACC.singlePageCheckout.getMobileAddAddress();
				$("#address-change-link").hide();
			}
		})
		</script>
		<div class="new-address" id="addNewAddressForResponsive" style="display: block;">
			<%-- <p id="" onclick="ACC.singlePageCheckout.getMobileAddAddress();">
				<span class="addsign pincode-button"> </span> 
				<a
					class="pincode-button"> <spring:theme
						code="checkout.multi.deliveryAddress.useNewAddress"
						text="Use New Address"></spring:theme>
				</a>
			</p> --%>
			<div class="mobile_add_address" onclick="ACC.singlePageCheckout.getMobileAddAddress();">
			<span class="mobile_add_address_radio"></span>
			<spring:theme code="checkout.multi.deliveryAddress.useNewAddress.mobile" text="Use New Address"></spring:theme>
			</div>
			<span id="newAddressMobileErrorMessage"></span>
			<div id="newAddressFormMobile" class="new-address-form-mobile" data-loaded="false"></div>
		</div>
	</c:if>


<script>
//$(".mobileNotDefaultDelAddress").hide();
</script>
</div>
