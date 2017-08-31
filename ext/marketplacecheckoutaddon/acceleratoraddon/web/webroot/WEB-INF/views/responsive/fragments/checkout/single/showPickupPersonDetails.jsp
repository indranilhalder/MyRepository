<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<div class="container" id='pickup'>
	<div class="panel">
		<div class="pickUpPersonAjax"></div>
	</div>
	<div class="panel panel-default pickuppersonWidth">
		<h2 class="panel-defaultHeading"><spring:theme	code="checkout.single.pickup.person.personalinfo" /></h2>
		<p class="retail-bag"></p>
		<div class="retail-detail"><spring:theme code="checkout.single.pickup.person.providedetail.msg" /></div>
		<div class="panel panel-body">
			<div class="pickupDetails error_txt" style="display:none;">
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
				<div class="form-group">
					<label class="control-label" for="pickupPersonName"><spring:theme code="checkout.single.pickup.person.name" /></label>
					<input type="text" id="pickupPersonName" name="pickupPersonName"
						maxlength="30" class="inputname form-control" placeholder="Enter Full Name*"
						value="" onkeyup="ACC.singlePageCheckout.validatePickupPersonNameOnKeyUp()"/>
					<div class="error_txt pickupPersonNameError"></div>
						</div>
				</div>
				<div class="mobileSubPanel">
				<div class="form-group">
					<label class="control-label" for="pickupPersonMobile"><spring:theme	code="checkout.single.pickup.person.mobileNumber" /></label>
					<input type="text" id="pickupPersonMobile" class="inputmobile"
						maxlength="10" placeholder="Enter Mobile Number*"
						value="" onkeyup="ACC.singlePageCheckout.validatePickupPersonMobileOnKeyUp()" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"/><br />
					<div class="error_txt pickupPersonMobileError"></div>
					</div>
				</div>
				<div class="line"></div>
				<div class="submitSubPanel">
					<button type="button" class="savenewid"
						id="savePickupPersondDetails" onclick="ACC.singlePageCheckout.savePickupPersonDetails(this);">
						<spring:theme code="checkout.single.cnc.pickup.details.submit" />
					</button>
					<div>
						<a href="javascript:void(0);" class="cancel_delslot" onclick="$('#singlePagePickupPersonPopup').modal('hide');"><spring:theme code="checkout.single.pickup.person.cancel" /></a>
					</div>
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