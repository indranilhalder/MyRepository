<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>

<script>
	//TISST-13010
	$(document).ready(function() {
		showPromotionTag();
	}); //TPR-1214
	$("#newAddressButton,#newAddressButtonUp").click(function() {
		ACC.singlePageCheckout.postEditAddress(this);
		return false;
	});
</script>
<div class="formaddress">
	<div class="heading-form">
		<h3>Edit Address</h3>
	</div>
	<div class="checkout-indent left-block address-form ">
		<ul class="product-block addresses new-form account-section">
			<li
				class="item account-section-content	 account-section-content-small ">
				<address:addressFormSelector supportedCountries="${countries}"
					regions="${regions}" cancelUrl="${currentStepUrl}"
					country="${country}" />
			</li>
		</ul>
	</div>
</div>