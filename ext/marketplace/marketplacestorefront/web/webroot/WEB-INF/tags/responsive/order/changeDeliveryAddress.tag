<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="orderDetails" required="true"
	type="de.hybris.platform.commercefacades.order.data.OrderData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<input type="hidden" id="deliveryAddorderCode"
	value="${orderDetails.code}" />
<div class="modal-dialog">
	<div class="modal-content">

		<form:form method="GET" id="deliveryAddressForm"
			action="${request.contextPath}/my-account/changeDeliveryAddress"
			commandName="addressForm">
			<div class="modal-header">
				<div class="row">
					<div class="col-md-6">
						<h4>Edit Shipping Address</h4>
					</div>
					<div class="col-md-1">
						<h4>OR</h4>
					</div>
					<div class="col-md-4">
						<h4>Change Address To</h4>
					</div>
					<div class="col-md-1">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
				</div>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-7 NOP">
						<div class="row">
							<div class="col-md-6 form-group">
								<label for="firstName">First Name*</label>
								<form:input path="firstName"
									class="form-control textInputChangeAddress" id="firstName"
									placeholder="${orderDetails.deliveryAddress.firstName}" />
								<div class="error_text firstNameError"></div>
							</div>
							<div class="col-md-6 form-group">
								<label for="lastName">Last Name*</label>

								<form:input path="lastName"
									class="form-control textInputChangeAddress" id="lastName"
									placeholder="${orderDetails.deliveryAddress.lastName}" />
								<div class="error_text lastNameError"></div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="addressLine1">Address Line 1*</label>

								<form:input path="line1"
									class="form-control textInputChangeAddress" id="addressLine1"
									placeholder="${orderDetails.deliveryAddress.line1}" />
								<div class="error_text address1Error"></div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="addressLine2">Address Line 2*</label>
								<form:input path="line2"
									placeholder="${orderDetails.deliveryAddress.line2}"
									class="form-control textInputChangeAddress" id="addressLine2" />
								<div class="error_text address2Error"></div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="addressLine2">Address Line 3*</label>
								<form:input path="line3" id="addressLine3"
									class="form-control textInputChangeAddress"
									placeholder="${orderDetails.deliveryAddress.line3}" />
								<div class="error_text address3Error"></div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label for="pincode">Pincode*</label>
									<form:input path="postcode" class="address_postcode"
										id="pincode"
										placeholder="${orderDetails.deliveryAddress.postalCode}" />
									<div class="error_text pincodeNoError"></div>
								</div>
							</div>
							<div class="col-md-6"></div>
						</div>
						<div class="row">
							<div class="col-md-12">

								<label for="landmark">Landmark</label>
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</div>
								<form:input path="landmark"
									class="form-control textInputChangeAddress" id="landmark"
									placeholder="${orderDetails.deliveryAddress.landmark}" />
								<div class="error_text landMarkError"></div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12" style="margin-top: 10px;">
								<div class="form-group">
									<label for="city">City*</label>
									<form:input path="townCity" class="address_townCity" id="city"
										placeholder="${orderDetails.deliveryAddress.town}" />
									<div class="error_text cityError"></div>

								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 form-group">
								<label for="state">State*</label>
								<form:input path="state"
									class="form-control textInputChangeAddress" id="state"
									placeholder="${orderDetails.deliveryAddress.state}" />
								<div class="error_text stateError"></div>
							</div>
							<div class="col-md-6 form-group">
								<label for="country">Country*</label> 
								<input type="text"
									id="country"
									value='${orderDetails.deliveryAddress.country.name}' readonly />
								<form:input path="countryIso" type="hidden" id="country"
									name="countryIso"
									value="${orderDetails.deliveryAddress.country.isocode}"/>
							</div>
						</div>

						<div class="row">
							<div class="col-md-8 form-group">
								<label for="phonenumber">Phone Number*</label>
								<form:input path="mobileNo"
									class="form-control textInputChangeAddress" id="mobileNo"
									placeholder="${orderDetails.deliveryAddress.phone}" />
								<div class="error_text mobileNumberError"></div>
							</div>
						</div>
					</div>


					<form:input type="hidden" path="addressId"
						value="${orderDetails.deliveryAddress.id}" id="id" />
					<form:input type="hidden" path="addressType" name="addressType"
						id="new-address-option-1" value="Home" />
					<div class="col-md-5">
						<div class="row">
							<div class="col-md-2">
								<input type="radio" name="select_address"
									style="width: 15px; height: 15px; display: block; cursor: pointer;" />
							</div>
							<div class="col-md-9 addressTextChange">
								<b>Residential Address 1 - Default</b> <br />
								${orderDetails.deliveryAddress.firstName}<br />
								${orderDetails.deliveryAddress.lastName}<br>
								${orderDetails.deliveryAddress.line1},&nbsp;
								${orderDetails.deliveryAddress.line2},
								<c:if test="${not empty orderDetails.deliveryAddress.line3}">
												&nbsp;${orderDetails.deliveryAddress.line3},
							   </c:if>
								<br> ${orderDetails.deliveryAddress.town},&nbsp;
								<c:if test="${not empty orderDetails.deliveryAddress.state}">
												${orderDetails.deliveryAddress.state},&nbsp;
								</c:if>
								${orderDetails.deliveryAddress.postalCode}&nbsp;
								${orderDetails.deliveryAddress.country.isocode} <br>
								91&nbsp;${orderDetails.deliveryAddress.phone} <br>

							</div>
						</div>
						<p style="clear: both;"></p>


					</div>
				</div>
			</div>

			<p style="clear: both;"></p>
			<div class="modal-footer">
				<button type="submit" id="saveBlockData" class="btn btn-primary">Save</button>
			</div>
		</form:form>
		<p style="clear: both;"></p>
	</div>
	<!-- /.modal-content -->
</div>
<!-- /.modal-dialog -->


<div class="wrapBG1"
	style="background-color: rgba(0, 0, 0, 0.5); width: 100%; height: 600px; position: fixed; top: 0px; left: 0px; z-index: 99999; display: none;"></div>
