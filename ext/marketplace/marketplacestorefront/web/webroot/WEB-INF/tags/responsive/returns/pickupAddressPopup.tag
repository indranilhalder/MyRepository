<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="removeModalAfterLoad" id="changeAddressPopup">
	<div class="modal-dialog changeAdddd">
		<div class="modal-content" id="popupFields">
			<form:form
				action="${request.contextPath}/my-account/returns/editReturnAddress"
				method="POST" commandName="addressForm" name="addressForm"
				class="form-horizontal" id="addAddressForm">

				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" onclick="hideRspShowRss();">
						<span aria-hidden="true">&times;</span>
					</button>


					<fieldset>
						<form:input type="hidden" path="addressId" id="addressId" />
						<div class="row">
							<div class="col-md-12 col-sm-12 NOP">
								
									<input type="hidden" id="temp" />
									<h4 class="editAddress">
										<spring:theme code="text.order.returns.editshippingaddress" />
									</h4>
									<h4 class="newAddress">New Shipping Address</h4>
									<div class="row">
										<div class="col-md-6 col-sm-6 col-xs-6 padd">
											<input type="radio" name="addressRadioType"
												id="new-address-option-1" value="Residential"
												onChange="onSelectRadio()" /> <label class="residential"
												for="new-address-option-1"><spring:theme
													code="text.addressBook.Residentialaddress"
													text="Residential Address" /> </label>
										</div>
										<div class="col-md-6 col-sm-6 col-xs-6 padd">
											<input type="radio" name="addressRadioType"
												id="new-address-option-2" value="Commercial"
												onChange="onSelectRadio()" /> <label class="commercial"
												for="new-address-option-2"><spring:theme
													code="text.addressBook.commercialaddress"
													text="Commercial Address" /> </label>
										</div>
									</div>

									<div class="errorMessage">
										<div id="errtype"></div>
									</div>
									<div class="row">
									<div class="col-md-6 form-group">
										<label for="firstName"><spring:theme
												code="text.order.returns.firstname" /></label>

										<form:input path="firstName" maxlength="40"
											class="form-control textInputChangeAddress" id="firstName"
											placeholder="First Name"
											onkeyup="this.value=this.value.replace(/\s\s+/g,'');" />
											<div class="errorText"></div>
									</div>
									<div class="col-md-6 form-group">
										<label for="lastName"><spring:theme
												code="text.order.returns.lastname" /></label>
										<form:input path="lastName" maxlength="40"
											class="form-control textInputChangeAddress" id="lastName"
											placeholder="Last Name"
											onkeyup="this.value=this.value.replace(/\s\s+/g,'');" />
											<div class="errorText"></div>
									</div>
									</div>
								<div class="clearfix"></div>

								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label for="pincode"><spring:theme
													code="text.order.returns.pincode" /></label>
											<form:input path="postcode"
												class="form-control textInputChangeAddress address_postcode"
												id="pincode" placeholder="Pincode" maxlength="6" />
												<div class="errorText"></div>
										</div>
									</div>
									<div class="col-md-6 form-group">
										<label for="phonenumber"><spring:theme
												code="text.order.returns.phonenumber" /></label>
										<form:input path="mobileNo"
											class="form-control textInputChangeAddress" id="mobileNo"
											placeholder="9876543210" maxlength="10" />
											<div class="errorText"></div>
									</div>
								</div>

							<div class="clearfix"></div>
								<div class="row">
									<div class="col-md-12 form-group">
										<label for="addressLine1"><spring:theme
												code="text.order.returns.addressline1" /></label>
										<form:textarea path="line1" maxlength="120"
											class="form-control textInputChangeAddress" id="addressLine1"
											placeholder="Address Line"
											onkeyup="this.value=this.value.replace(/\s\s+/g,'');" />
											<div class="errorText"></div>
									</div>
								</div>
								<!-- <div class="clearfix"></div> -->
								<div class="hide">
								<!-- <div class="row"> -->
									<div class="col-md-12 form-group">
										<label for="addressLine2"><spring:theme
												code="text.order.returns.addressline2" /></label>
										<form:input path="line2" maxlength="40"
											class="form-control textInputChangeAddress" id="addressLine2"
											placeholder="Address Line 2"
											onkeyup="this.value=this.value.replace(/\s\s+/g,'');" />
											<div class="errorText"></div>
									</div>
								</div>
								<!-- <div class="clearfix"></div> -->
								<!-- <div class="row"> -->
								<div class="hide">
									<div class="col-md-12 form-group">
										<label for="addressLine3"><spring:theme
												code="text.order.returns.addressline3" /></label>
										<form:input path="line3" maxlength="40"
											class="form-control textInputChangeAddress" id="addressLine3"
											placeholder="Address Line 3"
											onkeyup="this.value=this.value.replace(/\s\s+/g,'');" />
											<div class="errorText"></div>
									</div>
								</div>

	<div class="clearfix"></div>
								<div class="row">
									<div class="col-md-12 optionsLandmark">
										<label><spring:theme
												code="text.order.returns.landmark" /></label>
										<form:select style="height: 40px;" path="landmark"
											id="landmark" value="${addressForm.landmark}"
											class="address_landmarks" maxlength="30"></form:select>
											<div class="errorText"></div>
									</div>
									<div class="col-md-12 address_landmarkOtherDiv" data-value="${addressForm.landmark}">
										<label for="landmark"> <spring:theme
												code="text.order.returns.landmarkother" /></label>
										
										<form:input path="otherLandmark"
											class="form-control textInputChangeAddress address_landmarkOther"
											id="otherLandmark" placeholder="Enter your nearest landmark"
											onkeyup="this.value=this.value.replace(/\s\s+/g,'');" />
											<div class="errorText"></div>
									</div>
								</div>
									<div class="clearfix"></div>
								<div class="row">
									<div class="col-md-12" style="margin-top: 10px;">
										<div class="form-group">
											<label for="city"><spring:theme
													code="text.order.returns.city" /></label>
											<form:input path="townCity" onkeypress="return ValidateAlpha(event)"
												class="form-control textInputChangeAddress address_townCity"
												id="city" placeholder="City"
												onkeyup="this.value=this.value.replace(/\s\s+/g,'');" />
												<div class="errorText"></div>
										</div>
									</div>
								</div>
									<div class="clearfix"></div>
								<div class="row">
									<div class="col-md-6 form-group">
									
									
									<div class="mainDrop">
										<label for="state"><spring:theme
												code="text.order.returns.state" /></label>
										<form:select
											cssClass="form-control textInputChangeAddress address_states addressRead"
											name="stateList" id="stateListBox" path="state"
											onChange="onAddressSelectValidate()">
											<c:forEach items="${stateDataList}" var="state"
												varStatus="stateStatus">
												<option value="${state.name}">${state.name}</option>
											</c:forEach>
										</form:select>
										<div class="errorText"></div>
									</div>
									
									<div class="dupDisplay">
										<label>State *</label>
										<div class="stateInput"></div>
											<div class="help-block has-error" id="stateError"
										style="display: none;"></div>
								</div>
									</div>
										
										
									</div>
										<div class="clearfix"></div>
									<div class="col-md-6 form-group">
										<label for="country"> <spring:theme
												code="text.order.returns.country" /></label> <input
											class="form-control textInputChangeAddress textDisabled"
											id="country" value="India" placeholder="Country"
											disabled="disabled"
											onkeyup="this.value=this.value.replace(/\s\s+/g,'');" />
									</div>
								</div>
								<div class="clearfix"></div>
								<div class="row">
									<div class="col-md-12 defaultAddCheck">
										<div id="checkBox">
											<%-- 
											<form:checkbox id="checkBox1" path="defaultAddress"
												name="checkBox" value="checkBox" /> --%>
											<input type="checkbox" id="checkBox1"
												name="defaultAddressCheckbox" value="true" /> <label
												for="checkBox1">Mark as default address</label>
										</div>
									</div>
								</div>
								

							</div>
						</fieldset>
						</div>
					
					 <div class="row errorCodemessage"></div> 
					<p style="clear: both;"></p>
					<div class="modal-footer">
						<button type="submit" id="saveAddress"
							class="btn btn-primary light-blue">
							<spring:theme code="text.order.returns.savebutton" />
						</button>
					</div>
					<form:hidden path="orderCode" value="${orderCode}" />

					<%-- <p style="clear: both;"></p>
				<div class="modal-footer">
					<button type="submit" id="saveAddress" class="btn btn-primary light-blue">	
					<spring:theme code="text.order.returns.savebutton"/></button>
				</div> --%>

				</div>
			</form:form>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->



<div class="wrapBG"
	style="background-color: rgba(0, 0, 0, 0.5); width: 100%; height: 600px; position: fixed; top: 0px; left: 0px; display: none;"></div>
<style>

@media(min-width:791px) {
   
    #new-address-option-1+label,
    #new-address-option-2+label {
        width:100%
    }
    
}
.modal-body {
	position: relative;
	padding: 15px;
	max-height: calc(100vh - 210px);
	overflow-y: auto;
}

</style>