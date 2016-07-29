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
<div class="modal-dialog changeAdddd">
	<div class="modal-content">
		<form:form method="GET" id="deliveryAddressForm"
			action="${request.contextPath}/my-account/changeDeliveryAddress"
			commandName="addressForm">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-6">
						<h4>Edit Shipping Address</h4>
					</div>
					<div class="col-md-1">
						<h4>OR</h4>
					</div>
					<div class="col-md-4">
						<h4>Change Address To </h4>
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
									value="${orderDetails.deliveryAddress.firstName}" placeholder="First Name" />
								<div class="error_text firstNameError"></div>
							</div>
							<div class="col-md-6 form-group">
								<label for="lastName">Last Name*</label>

								<form:input path="lastName"
									class="form-control textInputChangeAddress" id="lastName"
									value="${orderDetails.deliveryAddress.lastName}" placeholder="Last Name" />
								<div class="error_text lastNameError"></div>
							</div>
						</div>
						
						<div class="row" style="clear: both">
							<div class="col-md-6">
								<div class="form-group">
									<label for="pincode">Pincode*</label>
									<form:input path="postcode" class="address_postcode"
										id="pincode"
										value="${orderDetails.deliveryAddress.postalCode}"  placeholder="Pincode" />
									<div class="error_text pincodeNoError"></div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="addressLine1">Address Line 1*</label>

								<form:input path="line1"
									class="form-control textInputChangeAddress" id="addressLine1"
									value="${orderDetails.deliveryAddress.line1}" placeholder="Address Line 1" />
								<div class="error_text address1Error"></div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="addressLine2">Address Line 2*</label>
								<form:input path="line2"
									value="${orderDetails.deliveryAddress.line2}" placeholder="Address Line 2"
									class="form-control textInputChangeAddress" id="addressLine2" />
								<div class="error_text address2Error"></div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="addressLine2">Address Line 3*</label>
								<form:input path="line3" id="addressLine3"
									class="form-control textInputChangeAddress"
									value="${orderDetails.deliveryAddress.line3}" placeholder="Address Line 3" />
								<div class="error_text address3Error"></div>
							</div>
						</div>
				
						<div class="row">
							<div class="optionsLandmark">
								<div class="col-md-12">
									<label for="landmark">Landmark</label>
									<form:select path="landmark" 
										class="form-control textInputChangeAddress address_landmarks" id="landmark"
										placeholder="select Landmark" />
									<div class="error_text landMarkError"></div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="address_landmarkOtherDiv">
								<div class="col-md-12">
									<label>Landmark</label>
										<form:input class="otherLandMark" placeholder="Other Landmark" path="otherLandmark"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12" style="margin-top: 10px;">
								<div class="form-group">
									<label for="city">City*</label>
									<form:input path="townCity" class="address_townCity" id="city"
										value="${orderDetails.deliveryAddress.town}" placeholder="City" />
									<div class="error_text cityError"></div>

								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 form-group">
								<label for="state">State*</label>
								<form:input path="state"
									class="form-control textInputChangeAddress address_states" id="state"
									value="${orderDetails.deliveryAddress.state}" placeholder="State" />
								<div class="error_text stateError"></div>
							</div>
							<div class="col-md-6 form-group">
								<label for="country">Country*</label> <input type="text"
									id="country"
									value='${orderDetails.deliveryAddress.country.name}' readonly />
								<form:input path="countryIso" type="hidden" id="country"
									name="countryIso"
									value="${orderDetails.deliveryAddress.country.isocode}" />
							</div>
						</div>

						<div class="row">
							<div class="col-md-8 form-group">
								<label for="phonenumber">Phone Number*</label>
								<form:input path="mobileNo"
									class="form-control textInputChangeAddress" id="mobileNo"
									value="${orderDetails.deliveryAddress.phone}" placeholder="Mobile Number" />
								<div class="error_text mobileNumberError"></div>
							</div>
						</div>
					</div>


					<form:input type="hidden" path="addressId"
						value="${orderDetails.deliveryAddress.id}" id="id" />
					<form:input type="hidden" path="addressType" name="addressType"
						id="new-address-option-1" value="Home" />
					<div class="col-md-5 addressListPop">

						<!-- varStatus="i" -->
					<c:if test="${not empty orderDetails.deliveryAddressList}">
							<c:forEach items="${orderDetails.deliveryAddressList}"
								var="orderDeliveryAddressList" varStatus="status">
								<c:set var="addressCount" value="${addressCount+1}"/>
								<div class="row">
									<div class="col-md-2">
										<input type="radio" class="addAddressToForm"
											data-item="changeAddress${status.count}" name="select_address"
											style="width: 15px; height: 15px; display: block; cursor: pointer;" />
									</div>
									<div class="col-md-9 addressTextChange changeAddress${status.count}">
										<b>Residential Address ${addressCount} - Default</b> <br /> <span
											class="firstName">${orderDeliveryAddressList.firstName}</span><br />
										<span class="lastName">${orderDeliveryAddressList.lastName}</span><br>
										<c:if test="${not empty orderDeliveryAddressList.line2}">
										  <span class="addressLine1">${orderDeliveryAddressList.line1}</span>,&nbsp;
									    </c:if>
									    
										<c:if test="${not empty orderDeliveryAddressList.line2}">
											<span class="addressLine2">${orderDeliveryAddressList.line2},</span>
									    </c:if>
										<c:if test="${not empty orderDeliveryAddressList.line3}">
											<span class="addressLine3">${orderDeliveryAddressList.line3}</span>
										</c:if> 
										<c:if test="${not empty orderDeliveryAddressList.landmark}">
												&nbsp;  <span class="landmark">,${orderDeliveryAddressList.landmark},</span>
									    </c:if> 
										<br> 
										<c:if test="${not empty orderDeliveryAddressList.town}">
												  <span class="town">${orderDeliveryAddresssList.town},</span>&nbsp;
									    </c:if> 
									  
										<c:if test="${not empty orderDeliveryAddressList.state}">
											<span class="state">${orderDeliveryAddressList.state},</span>&nbsp;
								        </c:if>
										<span class="postalCode">${orderDeliveryAddressList.postalCode}&nbsp;</span>
										<span class="isocode">${orderDeliveryAddressList.country.isocode}</span>
										<br>
										<c:if test="${not empty orderDeliveryAddressList.phone}">
									     	91&nbsp;<span class="phone">${orderDeliveryAddressList.phone}</span> 
								        </c:if>
									    <br>
										
									</div>
								</div>
								<p style="clear: both;"></p>
							</c:forEach>
						</c:if>
					</div>
				</div>
			</div>
			<p style="clear: both;"></p>
			<div class="modal-footer">
				<div class="error_text main_error"></div>
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