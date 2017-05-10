<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<div class="container" id='pickup'>
	<div class="panel">
		<div class="pickUpPersonAjax"></div>
	</div>
	<div class="panel panel-default pickuppersonWidth">
		<h2 class="panel-defaultHeading">Collect in store: Personal Info</h2>
		<p class="retail-bag"></p>
		<div class="retail-detail">Please provide us with your name and
			phone number so we can assist you quickly and easily</div>
		<div class="panel panel-body">
			<div class="pickupDetails error_txt">
				<spring:theme
					code="checkout.multi.cnc.pickup.details.validation.msg" />
			</div>
			<form name="pickupPersonDetails" action="#">
				<div style="display: none;">
					<span class="pickupperson"><h5 id="pickup">
							<spring:theme code="checkout.multi.cnc.pickup.person.name" />
						</h5></span>
				</div>
				<div class="nameFullSubPanel">
					<input type="text" id="pickupPersonName" name="pickupPersonName"
						maxlength="30" class="inputname" placeholder="Enter Full Name*"
						value="" onkeyup="ACC.singlePageCheckout.validatePickupPersonNameOnKeyUp()"/>
					<div class="error_txt pickupPersonNameError"></div>
				</div>
				<!-- <div class="nameBlankSubPanel">
					<input type="text" name="pickupPersonName" maxlength="30"
						class="inputname" />
					<div class="error_txt pickupPersonNameError"></div>
				</div> -->
				<div class="mobileSubPanel">
					<input type="text" id="pickupPersonMobile" class="inputmobile"
						maxlength="10" placeholder="Enter Mobile Number*"
						value="" onkeyup="ACC.singlePageCheckout.validatePickupPersonMobileOnKeyUp()"/><br />
					<div class="error_txt pickupPersonMobileError"></div>
				</div>
				<div class="submitSubPanel">
					<button type="button" class="savenewid"
						id="savePickupPersondDetails" onclick="ACC.singlePageCheckout.savePickupPersonDetails(this);">
						<spring:theme code="checkout.multi.cnc.pickup.details.submit" />
					</button>
					<!-- <div id="pickupPersonSubmit"></div>
					<div class="error_txt pickupPersonSubmitError"></div> -->
				</div>
				<div id="pickUpDetailsMsg" style="padding-top: 10px; display: none;">
					<spring:theme code="checkout.multi.cnc.pickup.details.below.msg" />
				</div>
				<p style="clear: both;"></p>
				&nbsp;
			</form>

		</div>
	</div>
</div>