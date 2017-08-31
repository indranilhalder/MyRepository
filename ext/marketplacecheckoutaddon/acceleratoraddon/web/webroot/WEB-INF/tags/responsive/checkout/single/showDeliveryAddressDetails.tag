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
<div class="checkout-shipping checkTab">

	<c:if test="${not empty deliveryAddresses}">
	
	<form id="selectAddressForm"
			action="/checkout/single/select-address"
			method="get">
			<c:set var='countWork' value='1' />
			<c:set var='countHome' value='1' />
			<c:set var='countWorkMobile' value='1' />
			<c:set var='countHomeMobile' value='1' />
			<input type="hidden" id="contExchnage" name="contExchnage" value="">
			<!-- change here for modified checkout page starts -->
						<!-- Div to display ajax failure messages -->
						<div id="selectedAddressMessage" style=""></div>
						
						<span id="emptyAddress" style="display:none"><spring:theme code="checkout.single.deliveryAddress.emptyAddress"
														text="Epmty Address" /></span>

						<div class="addressList_wrapper owl-carousel" id="address_carousel">
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


								<div class="address-list ${showItem}">
									<span class="edit"> <a
										href="/checkout/single/edit-address/${deliveryAddress.id}"
										class=""
										onclick="ACC.singlePageCheckout.getEditAddress(this,event);"
										id="link_${deliveryAddress.id}"></a>
									</span>
									<div  style="cursor:pointer;" onclick="ACC.singlePageCheckout.proceedOnAddressSelection(this,'${deliveryAddress.id}');">	
										<c:choose>
											<c:when test="${deliveryAddress.defaultAddress}">
												<input type="radio" class="radio1" name="selectedAddressCode"
													value="${deliveryAddress.id}"
													id="radio_${deliveryAddress.id}" checked="checked" />
												<label for="radio_${deliveryAddress.id}"></label>
											</c:when>
											<c:otherwise>
												<input type="radio" class="radio1" name="selectedAddressCode"
													value="${deliveryAddress.id}"
													id="radio_${deliveryAddress.id}" />
												<label for="radio_${deliveryAddress.id}"></label>
											</c:otherwise>
										</c:choose>
										<p class="address">
											<c:choose>
												<c:when test="${deliveryAddress.addressType eq 'Work'}">
													<span class="name commercial" for="radio_${deliveryAddress.id}"> <spring:theme
															code="checkout.multi.deliveryAddress.commercialAddress"
															text="Commercial Addresses" />
															<c:out
															value="${countWork}"></c:out>
													</span>
	
													<c:set var='countWork' value='${countWork+1}' />
												</c:when>
												<c:otherwise>
													<span class="name residential" for="radio_${deliveryAddress.id}" ${countHome}> <spring:theme
															code="checkout.multi.deliveryAddress.residentialAddress"
															text="Residential Addresses" /> <c:out
															value="${countHome}"></c:out> 
															<c:if
															test="${deliveryAddress.defaultAddress}">
															<br />
														</c:if>
													</span>
													<c:set var='countHome' value='${countHome+1}' />
												</c:otherwise>
	
											</c:choose>
	
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
	
											<c:choose>
												<c:when test="${deliveryAddress.defaultAddress}">
													<span class="default default-selected"> <input
														type="radio" value="Make this my default address"
														class="regular-radio" name="default"
														id="radio-default2_${deliveryAddress.id}"
														data-address-id="${deliveryAddress.id}"> <label
														class="radio-checked"
														for="radio-default2_${deliveryAddress.id}">Make
															this my default address</label>
													</span>
												</c:when>
											</c:choose>
	
											<span class="default"> <input type="radio"
												value="Make this my default address" class="regular-radio"
												name="default" id="radio-default2_${deliveryAddress.id}"
												data-address-id="${deliveryAddress.id}"> <label
												for="radio-default2_${deliveryAddress.id}">Make this
													my default address</label>
											</span>
										</p>	
											<div class="editnewAddresPage" id="${adressid}"></div>
										<button type="button"><spring:theme code="checkout.single.deliveryMethod.deliverHere.continue" text="DELIVER HERE"></spring:theme></button>
									</div>
								</div>
								
							</c:forEach>
						</div>
						<p class="page_count"></p>

						<div class="addNew_wrapper">

							<!-- <div class="addnewAddresPage"></div> -->
							<div class="add_address_button" style="display: block;">
								<p id="" onclick="ACC.singlePageCheckout.getAddAddress();">
									<span class="addsign pincode-button"> </span> <a
										class="pincode-button"> <spring:theme
											code="checkout.multi.deliveryAddress.useNewAddress"
											text="Use New Address"></spring:theme>
									</a>
								</p>
							</div>
						</div>



			<!-- change here for modified checkout page ends -->

		</form>

	</c:if>

	<!--  If no address is present -->
	<c:if test="${empty deliveryAddresses}">
		<script>
		$(document).ready(function(){
			if(!ACC.singlePageCheckout.getIsResponsive())
			{
				ACC.singlePageCheckout.getAddAddress();
			}
		})
		</script>
		<div class="new-address" style="display: block;">
			<p id="" onclick="ACC.singlePageCheckout.getAddAddress();">
				<span class="addsign pincode-button"> </span> 
				<a
					class="pincode-button"> <spring:theme
						code="checkout.multi.deliveryAddress.useNewAddress"
						text="Use New Address"></spring:theme>
				</a>
			</p>
			<div class="mobile_add_address" onclick="ACC.singlePageCheckout.getAddAddress();">
			<span class="mobile_add_address_radio"></span>
			<spring:theme code="checkout.multi.deliveryAddress.useNewAddress.mobile" text="Use New Address"></spring:theme>
			</div>
			<div class="new-address-form-mobile"></div>
		</div>
	</c:if>
</div>
