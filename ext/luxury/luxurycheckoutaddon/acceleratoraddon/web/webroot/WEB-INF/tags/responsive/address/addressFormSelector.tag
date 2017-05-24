<%@ attribute name="supportedCountries" required="true" type="java.util.List"%>
<%@ attribute name="regions" required="true" type="java.util.List"%>
<%@ attribute name="country" required="false" type="java.lang.String"%>
<%@ attribute name="cancelUrl" required="false" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/address"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<c:if test="${not empty deliveryAddresses}">
	<%-- <button type="button" class="btn btn-default btn-block js-address-book" id="viewAddressBook" >
		<spring:theme code="checkout.checkout.multi.deliveryAddress.viewAddressBook" text="View Address Book"/>
	</button> --%>
</c:if>


<form:form method="post" commandName="addressForm"  >
	<form:hidden path="addressId" class="add_edit_delivery_address_id" 
				status="${not empty suggestedAddresses ? 'hasSuggestedAddresses' : ''}"/>
	<input type="hidden" name="bill_state" id="address.billstate"/>
	<div id="countrySelector" data-address-code="${addressData.id}" data-country-iso-code="${addressData.country.isocode}" class="clearfix" >
	
	
		  	                         	                   
	<!-- <div>
	<strong>Country</strong>        
	<select id="country" name="name" class="form-control" disabled="disabled">
    	<option value="IN" selected="selected">India</option>
	</select>
	</div> -->                           
	<br>                         
	</div>
	<div class="i18nAddressForm address-form" id="i18nAddressForm">
	  <address:addressFormElements regions="${regions}" country="IN"/>
	</div> 
<%-- <formElement:formSelectBox idKey="country"
		                           labelKey="address.country"
		                           path="countryIso"
		                           mandatory="true"
		                           skipBlank="false"
		                           skipBlankMessageKey="address.selectCountry"
		                           items="${supportedCountries}"
		                           itemValue="isocode"
		                           selectedValue="${addressForm.countryIso}"
		  	                       selectCSSClass="form-control"
		  	                      /> --%>
	
	<%-- <div class="i18nAddressForm"> 
		<c:if test="${not empty country}">
			<address:addressFormElements regions="${regions}"
			                             country="${country}"/>
		</c:if>	
	</div --%>
	
	
	<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
	<div class="form-additionals">
		<c:choose>
			<c:when test="${showSaveToAddressBook}">
				<%-- <formElement:formCheckbox idKey="saveAddressInMyAddressBook" 
						labelKey="checkout.summary.deliveryAddress.saveAddressInMyAddressBook" 
						path="saveInAddressBook" 
						labelCSS="add-address-left-label" mandatory="false"/> --%>
						<input type="checkbox" id="saveAddressInMyAddressBook" checked="checked" name="saveInAddressBook" value="true"/>
												<label for="saveAddressInMyAddressBook">SAVE THIS ADDRESS FOR LATER</label>
			</c:when>
			<c:when test="${not addressBookEmpty && not isDefaultAddress}">
				<%-- <formElement:formCheckbox idKey="defaultAddress" 
						labelKey="address.default" path="defaultAddress"
						        labelCSS="add-address-left-label" mandatory="false"/> --%>	
						        <input type="checkbox" id="defaultAddress" name="defaultAddress" value="true"/>
												<label for="defaultAddress">SAVE THIS ADDRESS FOR LATER</label>
			</c:when>
		</c:choose>
	</div>
	</sec:authorize>


	<div id="addressform_button_panel" class="form-actions">
				<c:choose>
					<c:when test="${edit eq true}">
						<ycommerce:testId code="multicheckout_saveAddress_button">
							<button  id="editAddressButton"  class="btn btn-primary btn-block" type="submit">
								<spring:theme code="checkout.multi.saveAddress" text="Save address"/>
							</button>
						</ycommerce:testId>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${accountPageAddress eq true}">
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonAccount" class=" btn btn-primary btn-block" type="submit">
										<spring:theme code="mobile.checkout.continue.button" text="Continue"/>
									</button>
								</ycommerce:testId>
							</c:when>
							<c:otherwise>
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButton" class="button" type="button">
										<spring:theme code="mobile.checkout.continue.save" text="Continue"/>
									</button>
								</ycommerce:testId>
							</c:otherwise>
						</c:choose>						
					</c:otherwise>
				</c:choose>
				
			
			
				<%-- <c:if test="${not noAddress}">
					<ycommerce:testId code="multicheckout_cancel_button">
						<c:url value="${cancelUrl}" var="cancel"/>
							<a class="button delivery-cancelButton" href="${cancel}"><spring:theme code="checkout.multi.cancel" text="Cancel"/></a>
					</ycommerce:testId>
				</c:if> --%>	
	</div>
</form:form>


	<%-- <div id="addressform_button_panel" class="form-actions">
		<c:choose>
			<c:when test="${edit eq true}">
				<ycommerce:testId code="multicheckout_saveAddress_button">
					<button class="positive right change_address_button show_processing_message" type="submit">
						<spring:theme code="checkout.multi.saveAddress"
							text="Save address" />
					</button>
				</ycommerce:testId>
			</c:when>
		</c:choose>
	</div>
</form:form> --%>
