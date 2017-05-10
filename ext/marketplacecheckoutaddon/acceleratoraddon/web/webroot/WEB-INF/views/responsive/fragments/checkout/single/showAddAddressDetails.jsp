<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>

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
	//TPR-1214
	$("#newAddressButton,#newAddressButtonUp").click(function() {
						ACC.singlePageCheckout.postAddAddress(this);
						return false;
					});
</script>
	<div class="checkout-shipping addNewAddress">
		<div class="formaddress" style="display: block;">
			<div class="heading-form">
				<h3>Enter a new shipping address</h3>
			</div>
			<div class="checkout-indent left-block address-form">
				<ul class="product-block addresses new-form account-section">
					<li	class="item account-section-content	 account-section-content-small ">
						<address:addressFormSelector supportedCountries="${countries}"
							regions="${regions}" cancelUrl="${currentStepUrl}" />
					</li>
				</ul>
			</div>
		</div>
	</div>