<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="multi-checkout"
	tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ attribute name="deliveryAddresses" required="true"
	type="java.util.List"%>
<%@ attribute name="selectedAddress" required="false" type="java.lang.String"%>

<script>
	//TISST-13010
	$(document).ready(function() {
		showPromotionTag();
	});

	/* 				var timeoutID;
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
	 */
</script>
<div class="checkout-shipping checkTab">

	<c:if test="${not empty deliveryAddresses}">

		<form id="selectAddressForm"
			action="${request.contextPath}/checkout/single/select-address"
			method="get">
			<c:set var='countWork' value='1' />
			<c:set var='countHome' value='1' />
			<c:set var='countWorkMobile' value='1' />
			<c:set var='countHomeMobile' value='1' />

			<!-- change here for modified checkout page starts -->

						<%-- <h2>Choose Address</h2>
						<p class="cart-items">
							<spring:theme code="checkout.multi.shipment.items"
								arguments="${cartData.deliveryItemsQuantity}"
								text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme>
						</p> --%>
						
						<div id="selectedAddressMessage" style=""></div>

						<%-- <form> --%>
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

								<c:set var="selectedAddressVar" value="${selectedAddress}"/>
								

								<div class="address-list ${showItem}">
									<span class="edit"> <a
										href="${request.contextPath}/checkout/single/edit-address/${deliveryAddress.id}"
										class=""
										onclick="ACC.singlePageCheckout.getEditAddress(this,event);"
										id="link_${deliveryAddress.id}"></a>
									</span>
									<div style="cursor:pointer;" onclick="ACC.singlePageCheckout.proceedOnAddressSelection(this,'${deliveryAddress.id}');">
										<%-- <c:choose>
											<c:when test="${deliveryAddress.defaultAddress && empty selectedAddressVar}">
												<input type="radio" class="radio1" name="selectedAddressCode"
													value="${deliveryAddress.id}"
													id="radio_${deliveryAddress.id}" checked="checked" />
												<label for="radio_${deliveryAddress.id}"></label>
											</c:when>
											<c:when test="${not empty selectedAddressVar && deliveryAddress.id eq selectedAddressVar}">
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
	
										</c:choose> --%>
	
	
										<p class="address">
											<c:choose>
												<c:when test="${deliveryAddress.addressType eq 'Work'}">
													<span class="name commercial" for="radio_${deliveryAddress.id}"> <spring:theme
															code="checkout.multi.deliveryAddress.commercialAddress"
															text="Commercial Addresses" /> 
															<%-- <c:if
															test="${deliveryAddress.defaultAddress}">
															<spring:theme
																code="checkout.multi.deliveryAddress.defaultAddress"
																text="Default Addresses" />
															<br />
														</c:if> --%>
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
															<%-- <spring:theme
																code="checkout.multi.deliveryAddress.defaultAddress"
																text="Default Addresses" /> --%>
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
	
										<%-- <c:set var="adressid" value="${deliveryAddress.id}" />
	
											<div class="editnewAddresPage" id="${adressid}"></div> --%>
										<button type="button"><spring:theme code="checkout.single.deliveryMethod.deliverHere.continue" text="DELIVER HERE"></spring:theme></button>
									</div>
								</div>
								
							</c:forEach>
						</div>
						<p class="page_count"></p>
						<%-- </form> --%>
						<%-- <c:choose>
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

							<c:when test="${deliveryAddressCount gt 3 && deliveryAddressCount % 2 == 0}">
				<c:set var="deliveryAddressClass" value="moreEvens_address"></c:set>
		</c:when>
						</c:choose> --%>






<div class="addressList_wrapper mobile_list">
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

								<c:set var="selectedAddressVar" value="${selectedAddress}"/>
								

								<div class="address-list ${showItem} <c:if test="${not deliveryAddress.defaultAddress}">mobileNotDefaultDelAddress</c:if>">
									<%-- <span class="edit"> <a
										href="${request.contextPath}/checkout/single/edit-address/${deliveryAddress.id}"
										class=""
										onclick="ACC.singlePageCheckout.getEditAddress(this,event);"
										id="link_${deliveryAddress.id}"></a>
									</span> --%>
									 <div style="cursor:pointer;" onclick="ACC.singlePageCheckout.proceedOnAddressSelection(this,'${deliveryAddress.id}');">
										<c:choose>
											<c:when test="${deliveryAddress.defaultAddress && empty selectedAddressVar}">
												<input type="radio" class="radio1" name="selectedAddressCode"
													value="${deliveryAddress.id}"
													id="radio_${deliveryAddress.id}" checked="checked" />
												<label for="radio_${deliveryAddress.id}"></label>
											</c:when>
											<c:when test="${not empty selectedAddressVar && deliveryAddress.id eq selectedAddressVar}">
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
											<%-- <c:choose>
												<c:when test="${deliveryAddress.addressType eq 'Work'}">
													<span class="name commercial" for="radio_${deliveryAddress.id}"> <spring:theme
															code="checkout.multi.deliveryAddress.commercialAddress"
															text="Commercial Addresses" /> 
															<c:if
															test="${deliveryAddress.defaultAddress}">
															<spring:theme
																code="checkout.multi.deliveryAddress.defaultAddress"
																text="Default Addresses" />
															<br />
														</c:if>
													</span>
	
													<c:set var='countWorkMobile' value='${countWorkMobile+1}' />
												</c:when>
												<c:otherwise>
													<span class="name residential" for="radio_${deliveryAddress.id}" ${countHomeMobile}> <spring:theme
															code="checkout.multi.deliveryAddress.residentialAddress"
															text="Residential Addresses" /> <c:out
															value="${countHomeMobile}"></c:out> 
															<c:if
															test="${deliveryAddress.defaultAddress}">
															<spring:theme
																code="checkout.multi.deliveryAddress.defaultAddress"
																text="Default Addresses" />
															<br />
														</c:if>
													</span>
													<c:set var='countHomeMobile' value='${countHomeMobile+1}' />
												</c:otherwise>
	
											</c:choose> --%>
	
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
	
											<%-- <c:choose>
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
											</c:choose> --%>
	
											<%-- <span class="default"> <input type="radio"
												value="Make this my default address" class="regular-radio"
												name="default" id="radio-default2_${deliveryAddress.id}"
												data-address-id="${deliveryAddress.id}"> <label
												for="radio-default2_${deliveryAddress.id}">Make this
													my default address</label>
											</span> --%>
										</p>
	
										<%-- <c:set var="adressid" value="${deliveryAddress.id}" />
	
											<div class="editnewAddresPage" id="${adressid}"></div> --%>
										<%-- <button type="button"><spring:theme code="checkout.single.deliveryMethod.deliverHere.continue" text="DELIVER HERE"></spring:theme></button> --%>
									 </div> 
								</div>
								
							</c:forEach>
						</div>








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
			ACC.singlePageCheckout.getAddAddress();
			/* setTimeout(function(){ACC.singlePageCheckout.getAddAddress();},1); */
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
		</div>
	</c:if>


<script>
$(".mobileNotDefaultDelAddress").hide();
</script>


</div>
