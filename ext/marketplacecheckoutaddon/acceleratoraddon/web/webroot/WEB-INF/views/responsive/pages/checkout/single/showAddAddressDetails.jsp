<%@ page trimDirectiveWhitespaces="true"%>
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

<script>
	//TISST-13010
	$(document)
			.ready(
					function() {
						showPromotionTag();
						if ($(".choose-address .acc_content").children(
								".address-list").length == 0) {
							$(".add-address").css({
								//margin : "0px auto",
								float : "none"
							});
							$(".checkout-shipping.addNewAddress .formaddress")
									.css({
										//margin : "0px auto",
										float : "none",
										width : "80%",
										overflow : "hidden"
									});
							//$(".choose-address .acc_head").css("text-align","center");
						}
						$(".cancelBtn").click(function() {
							//alert('here');

							$(".editnewAddresPage, .formaddress").slideUp();
							$(".add-address").slideDown();
						});
						$('.checkout.wrapper .formaddress select[name="state"]')
								.on("change", function() {
									$(this).css("color", "#000");
								});
					});
/* 	var timeoutID;
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
	} */
	//TPR-1214
	$("#newAddressButton,#newAddressButtonUp").click(function() {
						ACC.singlePageCheckout.postAddAddress(this);
						return false;
					});
</script>
<%-- <ycommerce:testId code="checkoutStepTwo"> --%>
	<div class="checkout-shipping addNewAddress">
		<c:choose>
			<c:when test="${edit eq true}">
				<ycommerce:testId code="multicheckout_saveAddress_button">
					<button id="editAddressButtonUp" class="btn btn-primary btn-block"
						type="submit">
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
							<%-- <button id="newAddressButtonUp" class="button" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue" text="Continue"/>
									</button> --%>
						</ycommerce:testId>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		<div class="formaddress" style="display: block;">
			<div class="heading-form">
				<h3>Enter a new shipping address</h3>
				<!-- <input type="button" value="cancel" class="cancelBtn"> -->
			</div>


			<div class="checkout-indent left-block address-form">

				<ul class="product-block addresses new-form account-section">

					<li
						class="item account-section-content	 account-section-content-small ">
						<address:addressFormSelector supportedCountries="${countries}"
							regions="${regions}" cancelUrl="${currentStepUrl}" />
					</li>
				</ul>

			</div>

		</div>
	</div>

<%-- </ycommerce:testId> --%>
