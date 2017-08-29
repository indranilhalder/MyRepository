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
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<input type="hidden" id="deliveryAddorderCode"
	value="${orderDetails.code}" />
<div class="modal-dialog changeAdddd" >
	<div class="modal-content">
		<form:form method="GET" id="deliveryAddressForm"
			action="${request.contextPath}/my-account/changeDeliveryAddress"
			commandName="addressForm">
			<div class="modal-body">
				<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
				<div class="row" id="onTop">
					<div class="col-md-5">
					<div class="error_text serverError"></div>
						<h4><spring:theme code="text.order.deliveryAddress.editAddress"/></h4>
					</div>
					<div class="col-md-2 or">
						<h4><spring:theme code="text.order.deliveryAddress.or"/></h4>
					</div>
					<div class="col-md-5">
						<h4><spring:theme code="text.order.changeAddTo"/></h4>
					</div>
				</div>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-6 NOP" >
						<div class="row">
							<div class="col-md-6 form-group">
								<label for="firstName"><spring:theme code="text.order.returns.firstname"/></label>
								<!-- PRDI-124 start-->
								<form:input path="firstName" onkeypress="return ValidateAlpha(event)"
									class="form-control textInputChangeAddress" id="firstName"
									value="${orderDetails.deliveryAddress.firstName}" placeholder="First Name" maxlength="140"/>
								<div class="error_text firstNameError"></div>
							</div>
							<div class="col-md-6 form-group">
								<label for="lastName"><spring:theme code="text.order.returns.lastname"/></label>

								<form:input path="lastName" onkeypress="return ValidateAlpha(event)"
									class="form-control textInputChangeAddress" id="lastName"
									value="${orderDetails.deliveryAddress.lastName}" placeholder="Last Name" maxlength="140"/>
								<div class="error_text lastNameError"></div>
							</div>
						</div>
						
						<div class="row" style="clear: both">
<%-- 							<div class="col-md-6 form-group">
								<label for="phonenumber"><spring:theme code="text.order.returns.phonenumber"/></label>
								<form:input path="mobileNo" onkeypress="return isNumber(event)"
									class="form-control textInputChangeAddress" id="mobileNo" maxlength="10"
									value="${orderDetails.deliveryAddress.phone}" placeholder="Mobile Number" />
								<div class="error_text mobileNumberError"></div>
							</div>		<!-- TISPRDT-1213 reverting back --> --%>
							<div class="col-md-6">
								
									<label for="pincode"><spring:theme code="text.order.returns.pincode"/></label>
									<form:input path="postcode" class="address_postcode form-control" onkeypress="return isNumber(event)"
										id="pincode" maxlength="6"
										value="${orderDetails.deliveryAddress.postalCode}"  placeholder="Pincode" />
									
							
							</div>
							<div  class="col-md-12 error_text pincodeNoError"></div>
						</div>
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="addressLine1"><spring:theme code="text.order.returns.addressline1"/></label>

								<form:textarea path="line1"
									class="form-control textInputChangeAddress addressline1" id="addressLine1"
									value="${orderDetails.deliveryAddress.line1}" placeholder="Address Line" maxlength="120"/>
								<div class="error_text address1Error"></div>
							</div>
						</div>
						
						
						<div class="hide">
						
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="addressLine2"><spring:theme code="text.order.returns.addressline2"/></label>
								<form:input path="line2"
									value="${orderDetails.deliveryAddress.line2}" placeholder="Address Line 2"
									class="form-control textInputChangeAddress" id="addressLine2" maxlength="40"/>
								<div class="error_text address2Error"></div>
								</div>
							</div>
						</div>
						
						<div class="hide">
						<div class="row">
							<div class="col-md-12 form-group">
								<label for="addressLine2"><spring:theme code="text.order.returns.addressline3"/></label>
								<form:input path="line3" id="addressLine3"
									class="form-control textInputChangeAddress"
									value="${orderDetails.deliveryAddress.line3}" placeholder="Address Line 3" maxlength="40"/>
								<div class="error_text address3Error"></div>
							</div>
						</div>
						</div>
						
						
				
						<div class="row">
							<div class="optionsLandmark">
								<div class="col-md-12">
									<label for="landmark"><spring:theme code="text.order.returns.landmark"/></label>
									<%-- <c:if test="${orderDetails.deliveryAddress.landmark==}"></c:if> --%>
									<form:select path="landmark" 
										class="form-control textInputChangeAddress address_landmarks slected_value" id="landmark"
										 />
									<div class="error_text landMarkError"></div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="address_landmarkOtherDiv" data-value="${orderDetails.deliveryAddress.landmark}">
							<input type="hidden" id="otherLandmarkInitial" value="${orderDetails.deliveryAddress.landmark}"/>
								<div class="col-md-12">
									<label><spring:theme code="text.order.returns.landmarkother"/></label>
										<form:input class="otherLandMark address_landmarkOther form-control" placeholder="Other Landmark" path="otherLandmark"/>	
										<div class="error_text otherLandMarkError"></div>									
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12" style="margin-top: 10px;">
								<div class="form-group">
									<label for="city"><spring:theme code="text.order.returns.city"/></label>
									<form:input path="townCity" class="address_townCity form-control" id="city"
										value="${orderDetails.deliveryAddress.town}" placeholder="City" maxlength="40"/>
										<!-- PRDI-124 END-->
									<div class="error_text cityError"></div>

								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 form-group">
							
<!-- 								<label for="state">State*</label> -->
								<div class="mainDrop">
								<formElement:formSelectBox idKey="state"
									selectCSSClass="l textInputChangeAddress address_states form-control" 
									labelKey="address.states"
									path="state" mandatory="true" skipBlank="false"
									skipBlankMessageKey="${orderDetails.deliveryAddress.state}"
									items="${stateDataList}" selectedValue="${addressForm.state}"
									itemValue="name" />
								<div class="error_text stateError"></div>
								</div>
								<div class="dupDisplay">
								<label>State *</label>
								<div class="stateInput"></div>
									<div class="help-block has-error" id="stateError"
								style="display: none;">
									</div>
									</div>
							</div>
							<div class="col-md-6 form-group">
								<label for="country"><spring:theme code="text.order.returns.country"/></label> <input type="text"
									id="country"
									value='${orderDetails.deliveryAddress.country.name}' readonly class="form-control"/>
								<form:input path="countryIso" type="hidden" id="country"
									name="countryIso"
									value="${orderDetails.deliveryAddress.country.isocode}"  class="form-control"/>
							</div>
						</div>
						
					<!-- 	<div class="hide"> -->
						<div class="row">
							<div class="col-md-8 form-group">
								<label for="phonenumber"><spring:theme code="text.order.returns.phonenumber"/></label>
								<form:input path="mobileNo" onkeypress="return isNumber(event)"
									class="form-control textInputChangeAddress" id="mobileNo" maxlength="10"
									value="${orderDetails.deliveryAddress.phone}" placeholder="Mobile Number" />
								<div class="error_text mobileNumberError"></div>
							</div>
						</div>
					<!-- 	</div>	 --><!-- TISPRDT-1213 reverting back -->
					</div>

					<form:input type="hidden" path="addressId"
						value="${orderDetails.deliveryAddress.id}" id="id" />
						
					<form:input type="hidden" path="addressType" name="addressType"
						id="new-address-option-1" value="${orderDetails.deliveryAddress.addressType}" />
					<div class="col-md-6 addressListPop">

						<!-- varStatus="i" -->
						<c:if test="${not empty orderDetails.deliveryAddressList}">
							<c:forEach items="${orderDetails.deliveryAddressList}"
								var="orderDeliveryAddressList" varStatus="status">
								<c:set var="addressCount" value="${addressCount+1}" />
								<div class="row">
									<div class="col-md-2">
										<input type="radio" class="addAddressToForm changeAddCheck radio${status.count}"
											data-item="changeAddress${status.count}"
											name="select_address"/>
									</div>
									<div
										class="col-md-9 addressTextChange addressSpace changeAddress${status.count}">
										<b><c:choose>
                                               <c:when test="${orderDeliveryAddressList.addressType eq 'Home'}">
                                                Residential
                                               </c:when>    
                                               <c:otherwise>
                                               Commercial
                                               </c:otherwise>
                                            </c:choose>
										     Address ${addressCount} <c:if test="${orderDeliveryAddressList.defaultAddress}">&nbsp;&nbsp;-Default Address</c:if></b> <br /> 
									
										<span class="addressType addressFont">${orderDeliveryAddressList.addressType}</span>
										<span class="firstName addressFont">${orderDeliveryAddressList.firstName}</span>
										<span class="lastName addressFont">${orderDeliveryAddressList.lastName}</span><br>
										<!-- TISUATSE-135 start -->
										<c:if test="${not empty orderDeliveryAddressList.line1 && empty orderDeliveryAddressList.line2 && empty orderDeliveryAddressList.line3}">
											<span class="addressline1 addressFont">${orderDeliveryAddressList.line1}</span>,&nbsp;
										</c:if>
										<c:if test="${empty orderDeliveryAddressList.line2 && not empty orderDeliveryAddressList.line3}">
											<span class="addressline1 addressFont">${orderDeliveryAddressList.line1}${orderDeliveryAddressList.line3}</span>,&nbsp;
										</c:if>
										<c:if test="${not empty orderDeliveryAddressList.line2 && empty orderDeliveryAddressList.line3}">
											<span class="addressline1 addressFont">${orderDeliveryAddressList.line1}${orderDeliveryAddressList.line2}</span>,&nbsp;
										</c:if>
										<c:if test="${not empty orderDeliveryAddressList.line2 && not empty orderDeliveryAddressList.line3}">
											<span class="addressline1 addressFont">${orderDeliveryAddressList.line1}${orderDeliveryAddressList.line2}${orderDeliveryAddressList.line3}</span>,&nbsp;
										</c:if>
										<c:if test="${not empty orderDeliveryAddressList.landmark}">
												<span class="landmark addressFont">${orderDeliveryAddressList.landmark}</span>,&nbsp;
										</c:if>
										<br>
										<!-- TISUATSE-135 end -->
										<%-- <c:if test="${not empty orderDeliveryAddressList.line1}">
											<span class="addressLine1 addressFont">${orderDeliveryAddressList.line1}</span>,&nbsp;
									    </c:if>
										<c:if test="${not empty orderDeliveryAddressList.line2}">
											<span class="addressLine2 addressFont">${orderDeliveryAddressList.line2}</span>,
										</c:if>
										<c:if test="${not empty orderDeliveryAddressList.line3}">
											<span class="addressLine3 addressFont">${orderDeliveryAddressList.line3}</span>,
										</c:if>
										<c:if test="${not empty orderDeliveryAddressList.landmark}">
												&nbsp;<span class="landmark addressFont">${orderDeliveryAddressList.landmark}</span>,
										</c:if> --%>
										
										<c:if test="${not empty orderDeliveryAddressList.town}">
											<span class="town addressFont">${orderDeliveryAddressList.town}</span>&nbsp;
									    </c:if>

										<c:if test="${not empty orderDeliveryAddressList.state}">
											<span class="state addressFont">${orderDeliveryAddressList.state}</span>,&nbsp;
								        </c:if>
										<span class="postalCode addressFont">${orderDeliveryAddressList.postalCode}</span>&nbsp;
										<span class="isocode addressFont">${orderDeliveryAddressList.country.isocode}</span>
										<br>
										<c:if test="${not empty orderDeliveryAddressList.phone}">
									     	91&nbsp;<span class="phone addressFont">${orderDeliveryAddressList.phone}</span>
										</c:if>
										<br>

									</div>
								</div>
								<p style="clear: both;"></p>
							</c:forEach>
						</c:if>
					  <div class="col-md-12"><a href="#onTop" id="addNewAddressPOP">Add a New Address</a></div>
					</div>
				</div>

				<c:if test="${isEDOrder eq true}">
					<div class="row">
						<div class="col-md-12 form-group formMsg">
							<spring:theme code="text.order.CDA.ED" />
						</div>
					</div>
				</c:if>
			</div>
				<p style="clear: both;"></p>
			<div class="modal-footer">
				<div class="error_text main_error"></div>
				<button type="button" onclick="checkPopupDataOrderHistory()" class="btn btn-primary saveBlockData"><spring:theme code="text.order.returns.savebutton"/></button>
			</div>
		</form:form>
		<p style="clear: both;"></p>
	</div>
	<!-- /.modal-content -->
</div>

<style>
#deliveryAddressForm .changeAddCheck{
	padding: 5px;
height: 17px;
display: block !important;
cursor: pointer;
border-radius: 50% !important;
width: 17px;
}
#deliveryAddressForm input[type="text"]{
	height: 35px;
}
#deliveryAddressForm input[type="radio"]{
	border:1px solid #CCC;
	position: static;
	overflow: auto;
	outline: none;
}
#deliveryAddressForm .addressSpace{
	 margin-bottom: 15px;
}
.addressListPop {
    overflow-y: auto;
    height: 400px;
}
#changeAddressPopup .changeAdddd {
    height: inherit;
    overflow-y: inherit;
}
#changeAddressPopup .modal-dialog{
	max-height: calc(100vh - 100px);
	overflow: auto;
    overflow-x: hidden;
}
#changeAddressPopup select {
    padding-right: 15px;
}
#changeAddressPopup label {
    padding-bottom: 5px;
}
.addressFont{
font-size: 10px;
}
#addNewAddressPOP{
	color: #A9143C;
text-decoration: underline;
}

.formMsg{
    font-size: 14px;
    color: red;
}
</style>
<!-- /.modal-dialog -->