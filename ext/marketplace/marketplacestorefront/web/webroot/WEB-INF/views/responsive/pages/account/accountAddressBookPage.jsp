<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />



<template:page pageTitle="${pageTitle}">
<div class="account">
  <h1 class="account-header"><spring:theme code="text.account.headerTitle" text="My Marketplace" />
  <select class="menu-select" onchange="window.location=this.options[this.selectedIndex].value;">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value=/store/mpl/en/my-account/ data-href="/store/mpl/en/my-account/"><spring:theme code="header.flyout.overview" /></option>
                  <option value=/store/mpl/en/my-account/marketplace-preference data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme code="header.flyout.marketplacepreferences" /></option>
                  <option value=/store/mpl/en/my-account/update-profile data-href="account-info.php"><spring:theme code="header.flyout.Personal" /> </option>
                  <option value=/store/mpl/en/my-account/orders data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value=/store/mpl/en/my-account/payment-details data-href="account-cards.php"><spring:theme code="header.flyout.cards" /></option>
                  <option value=/store/mpl/en/my-account/address-book data-href="account-addresses.php" selected><spring:theme code="header.flyout.address" /></option>
              </optgroup>
         
          <optgroup label="Share">
                  <option value=/store/mpl/en/my-account/friendsInvite data-href="account-invite.php"><spring:theme code="header.flyout.invite" /></option>
              </optgroup>
      </select>
</h1>
		
		<div class="wrapper">
				<!----- Left Navigation Starts --------->
			<div class="left-nav">
				<%-- <h1>
					<spring:theme code="text.account.headerTitle" text="My MarketPlace" />
				</h1> --%>
				<ul>
					<li><h3>
							<spring:theme code="header.flyout.myaccount" />
						</h3></li>
					<li><a href="<c:url value="/my-account/"/>"><spring:theme
								code="header.flyout.overview" /></a></li>
					<li><a
						href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
								code="header.flyout.marketplacepreferences" /></a></li>
					<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
								code="header.flyout.Personal" /></a></li>
					<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
								code="header.flyout.orders" /></a></li>
					<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
								code="header.flyout.cards" /></a></li>
					<li><a class="active" href="<c:url value="/my-account/address-book"/>"><spring:theme
								code="header.flyout.address" /></a></li>
								<li><a href="<c:url value="/my-account/reviews"/>"><spring:theme
						code="header.flyout.review" /></a></li>
					<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
								code="header.flyout.recommendations" /></a></li>
				</ul>
				<ul>
					<li><h3>
							<spring:theme code="header.flyout.share" />
						</h3></li>
					<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
								code="header.flyout.invite" /></a></li>
								
				</ul>
				
			</div>
			<!----- Left Navigation ENDS --------->
			<!----- RIGHT Navigation STARTS --------->
			<div class="right-account">
				<div id="address_item" class="info card-list">
					<!-- Heading for saved Cards -->

					<h1>
						<c:if test="${fn:length(addressData)>10}">
						<ul class="pagination address_pagination">
						 <li id="prev"></li></ul>
						</c:if>
						<spring:theme code="text.account.addressBook.savedAddress"
							text="My Address" />
							<span><spring:theme code="text.account.addressBook.savedAddress.details"
							text="Where in the world are you located? Edit, delete or change your address here." /></span>
					</h1>
					
					
					
					

					<!-- Heading for saved Cards -->
					
					<!-- address pagination	 -->
					<%-- <c:if test="${fn:length(addressData)>10}">
						<div class="address_pagination">
							<div id="prev"><a href="#nogo" onClick="prevAcc()"><spring:theme code="text.account.addressBook.pagination.prev" /></a></div>&emsp;|&emsp;
							<div id="next"><a href="#nogo" onClick="nextAcc()"><spring:theme code="text.account.addressBook.pagination.next" /></a></div>
						</div>
					</c:if> --%>

					<ul
						class="saved-cards" id="item_ul">
						<c:choose>
							<c:when test="${not empty addressData}">
								<c:choose>
									<c:when test="${not empty addressData}">
									<input type="hidden" id="accountAddressCount" value="${fn:length(addressData)}"/>
										<c:forEach items="${addressData}" var="address"
											varStatus="status">
		<!-- pagination	-->	
											<li class="mpl-addressBook">
												<div class="address default addressItem">
													<ycommerce:testId code="addressBook_address_label">

														<ul>
															<li>
																<c:if test="${address.addressType eq 'Home' || address.addressType eq 'HOME'}">
																	<h3>
																		<spring:theme code="text.addressBook.residential" text="Residential" />
																		<c:if test="${address.defaultAddress}">&nbsp;-&nbsp;Default Address</c:if>
																	</h3>
																</c:if>
																<c:if test="${address.addressType eq 'Work' || address.addressType eq 'WORK'}">
																	<h3>
																		<spring:theme code="text.addressBook.commercial" text="Commercial" />
																		<c:if test="${address.defaultAddress}">&nbsp;-&nbsp;Default Address</c:if>
																	</h3>
																</c:if>	
																

																<address>
																	${fn:escapeXml(address.firstName)}&nbsp;${fn:escapeXml(address.lastName)}<br>
																	${fn:escapeXml(address.line1)},&nbsp;${fn:escapeXml(address.line2)},
																	${fn:escapeXml(address.line3)},&nbsp;<br>
																	${fn:escapeXml(address.town)},&nbsp;${fn:escapeXml(address.state)},&nbsp;${fn:escapeXml(address.postalCode)}
																	&nbsp;IN <br> ${fn:escapeXml(address.region.name)}
																	 91&nbsp;${fn:escapeXml(address.phone)} <br>
																</address></li>
															<li><ycommerce:testId
																	code="addressBook_addressOptions_label">
																	<c:if test="${not address.defaultAddress}">
																		<ycommerce:testId code="addressBook_isDefault_button">
																			<a href="set-default-address/${address.id}"
																				class="make-default"> <spring:theme
																					code="text.makeDefaultAddress" text="MAKE DEFAULT ADDRESS" />
																			</a>
																		</ycommerce:testId>
																	</c:if>

																	<div class="actions">
																		
																		<ycommerce:testId
																			code="addressBook_removeAddress_button">
																			<a href="#nogo" data-toggle="modal"
																				data-target="#delete-address_${address.id}_${status.index}"
																				data-mylist="<spring:theme code="text.help" />"
																				data-dismiss="modal">
																				<spring:theme
																					code="text.remove" text="Delete Address" />
																			</a>

																		</ycommerce:testId>
																		
																		<ycommerce:testId
																			code="addressBook_editAddress_button">
																			<a href="#editaddress" class="edit" 
																				onclick="editAddress(${address.id})"> <spring:theme
																					code="text.edit" text="Edit Address" />
																			</a>
																		</ycommerce:testId>
																	</div>
																</ycommerce:testId>
															</li>
														</ul>
													</ycommerce:testId>
												</div>
											</li>

											<div class="mpl-addressPopup delete-address-Popup modal cancellation-request fade" 
												id="delete-address_${address.id}_${status.index}">
												<div class="content">
													<button class="close" data-dismiss="modal"></button>
													<div id="popup_confirm_address_removal_${address.id}">
														<div class="addressItem">
															<ul>
																<c:if test="${address.addressType eq 'Home' || address.addressType eq 'HOME'}">
																	<p class="account-addressBooktitle">
																		<spring:theme code="text.addressBook.residential" text="Residential" />
																		<c:if test="${address.defaultAddress}">&nbsp;-&nbsp;Default Address</c:if>
																		<c:if test="${not address.defaultAddress}">&nbsp;Address</c:if>
																	</p>
																</c:if>
																<c:if test="${address.addressType eq 'Work' || address.addressType eq 'WORK'}">
																	<p class="account-addressBooktitle">
																		<spring:theme code="text.addressBook.commercial" text="Commercial" />
																		<c:if test="${address.defaultAddress}">&nbsp;-&nbsp;Default Address</c:if>
																		<c:if test="${not address.defaultAddress}">&nbsp;Address</c:if>
																	</p>
																</c:if>	
																
																<li>${fn:escapeXml(address.firstName)}
																	&nbsp;${fn:escapeXml(address.lastName)}</li>
																<li>${fn:escapeXml(address.line1)},
																	&nbsp;${fn:escapeXml(address.line2)},</li>
																<li>${fn:escapeXml(address.line3)},</li>
																<li>${fn:escapeXml(address.town)},
																	&nbsp;${fn:escapeXml(address.state)},
																	&nbsp;${fn:escapeXml(address.postalCode)} &emsp;IN</li>
																<li>${fn:escapeXml(address.region.name)}</li>
																<li>91&nbsp;${fn:escapeXml(address.phone)}</li>
															</ul>

															<p Class="address-deleteConfirmation">
																<spring:theme code="text.adress.remove.confirmation"
																	text="Are you sure you want to delete this address?" />
															</p>

															<div class="buttons">

																<a class="btn confirm-deletionButton btn-block"
																	<%-- data-address-id="${address.id}" --%>
																href="remove-address/${address.id}">
																	<spring:theme code="text.yes" text="Yes" />
																</a> <a class="btn closeColorBox btn-block"
																	<%-- data-address-id="${address.id}" --%> data-dismiss="modal">
																	<spring:theme code="text.no" text="No" />
																</a>
															</div>
														</div>
													</div>
													<!-- <button class="close" data-dismiss="modal"></button> -->
												</div>
												<div class="overlay" data-dismiss="modal"></div>
											</div>

											<%-- <div class="mpl-addressPopup delete-address-Popup modal cancellation-request fade" id="saved-addressdeletePopup">
												<div class="content">
												<button class="close" data-dismiss="modal"></button>
												<div id="popup_confirm_address_removal_${address.id}">
													<div class="addressItem">
														<ul>
															<c:if test="${address.defaultAddress}">
																<p class="account-addressBooktitle">${address.addressType}&nbsp;-&nbsp;Default
																	Address</p>
															</c:if>
															<c:if test="${not address.defaultAddress}">
																<strong>${address.addressType}</strong>
															</c:if>
															<li>${fn:escapeXml(address.firstName)}
																&nbsp;${fn:escapeXml(address.lastName)}</li>
															<li>${fn:escapeXml(address.line1)},
																&nbsp;${fn:escapeXml(address.line2)},</li>
															<li>${fn:escapeXml(address.line3)},</li>
															<li>${fn:escapeXml(address.town)},
																&nbsp;${fn:escapeXml(address.state)},
																&nbsp;${fn:escapeXml(address.postalCode)} &emsp;IN</li>
															<li>${fn:escapeXml(address.region.name)}</li>
															<li>91&nbsp;${fn:escapeXml(address.phone)}</li>
														</ul>

														<p Class="address-deleteConfirmation"><spring:theme code="text.adress.remove.confirmation"
															text="Are you sure you would like to delete this address?" /></p>

														<div class="buttons">
															
															<a class="btn confirm-deletionButton btn-block"
																data-address-id="${address.id}"
																href="remove-address/${address.id}"> <spring:theme
																	code="text.yes" text="Yes" />
															</a> <a class="btn closeColorBox btn-block"
																data-address-id="${address.id}" data-dismiss="modal"> <spring:theme
																	code="text.no" text="No" /></a>
														</div>
													</div>
												</div>
												</div>
												<div class="overlay" data-dismiss="modal"></div>
											</div> --%>											
											<%-- </c:if> --%>
										</c:forEach>
									</c:when>
								</c:choose>
								<br>
							</c:when>
							<c:otherwise>
								<div class="account-emptyOrderMessage">
									<spring:theme code="text.account.addressBook.noSavedAddresses" />
								</div>
							</c:otherwise>
						</c:choose>
					</ul>
					<!-- address pagination	 -->
					<%-- <c:if test="${fn:length(addressData)>1}">
						<ul class="pagination address_pagination">
						 <li id="prev"></li></ul>
					</c:if> --%>
				</div>
				
				<%-- <!-- View More-->
				<c:if test="${fn:length(addressData)>2}">
					<div class="account-section-content	 account-section-content-small"
						align="right">
						<button class="mpl-moreLink" id="moreLink">View More >>></button>
					</div>
				</c:if>
				<c:if test="${fn:length(addressData)>2}">
					<div class="account-section-content	 account-section-content-small"
						align="right">
						<button class="mpl-lessLink" id="lessLink">View Less >>></button>
					</div>
				</c:if> --%>

				<!-- Add address Fields -->
				<!-- <div><a name="editaddress">&emsp;</a></div> -->
				<div class="new-address" id="editaddress">
					<h2 class="account-only" id="headerAdd"><spring:theme code="text.addressBook.newaddress" text="Add a New Address" /></h2>
					<h2 class="account-only" id="headerEdit"><spring:theme code="text.addressBook.editaddress" text="Edit Address" /></h2>
					<ul class="product-block addresses new-form">
						<li class="item"><form:form action="addNewAddress"
								method="post" commandName="addressForm" name="addressForm"
								onsubmit="return validateAccountAddress();">
								<!-- Static data for radio button -->
									
										<input type="radio" name="addressRadioType"
											id="new-address-option-1" value="Residential"
											onChange="onSelectRadio()" />
										<label class="residential" for="new-address-option-1"><spring:theme code="text.addressBook.Residentialaddress" text="Residential Address" />
											</label>
										<input type="radio" name="addressRadioType"
											id="new-address-option-2" value="Commercial"
											onChange="onSelectRadio()" />
										<label class="commercial" for="new-address-option-2"><spring:theme code="text.addressBook.commercialaddress" text="Commercial Address" />
											</label>
										 <div class="errorMessage">
											<div id="errtype"></div>
										</div> 
								

								<fieldset>

									
									<form:input type="hidden" path="addressId" id="addressId" />
									

									<!-- TISEE-4696 -->
									<div class="half no-display">
									<label><spring:theme code="text.addressBook.firstName" text="First name *" /></label>
										<form:input path="firstName" id="firstName"
											onkeyup="kpressaddressfn()" maxlength="40" />
									<div class="errorMessage"><div id="erraddressfn"></div></div>
									</div>
									
								

									
									<div class="half no-display last-name">
									<label><spring:theme code="text.addressBook.lastName" text="Last name *" /></label>
										<form:input path="lastName" id="lastName"
											onkeyup="kpressaddressln()" maxlength="40" />
										<div class="errorMessage"><div id="erraddressln"></div></div>
									</div>
									

									<!-- TISUAT-4696 -->
									<div class="half" style="clear:both;">
									<label><spring:theme code="text.addressBook.addressline1" text="Address Line 1 *" /></label>
										<form:input path="line1" id="line1" onkeyup="kpressaddressln1()"
											maxlength="30" />
											<div class="errorMessage"><div id="erraddressline1"></div></div>
									</div>
									

									<!-- TISUAT-4696 -->
									<div class="half">
									<label><spring:theme code="text.addressBook.addressline2" text="Address Line 2 *" /></label>
										<form:input path="line2" id="line2" onkeyup="kpressaddressln2()"
											maxlength="30" />
											<div class="errorMessage"><div id="erraddressline2">   </div></div>
									</div>
							

									<!-- TISUAT-4696 -->
									<div class="half">
									<label><spring:theme code="text.addressBook.landmark" text="Landmark *" /></label>
										<form:input path="line3" id="line3" onkeyup="kpressaddressln3()"
											maxlength="30" />
											 <div class="errorMessage"><div id="erraddressline3">   </div></div> 
									</div>
								
									
									<!-- TISUAT-4696 -->
									<div class="half">
									<label><spring:theme code="text.addressBook.City" text="City *" /></label>
										<form:input path="townCity" id="townCity"
											onkeyup="kpressaddresscity()" maxlength="30" />
											<div class="errorMessage"><div id="erraddressCity">  </div></div>
									</div>
									


									<div class="half no-display">
										<label><spring:theme code="text.addressBook.State" text="State *" /></label>
										<form:select name="stateList" id="stateListBox" path="state"
											 onChange="onAddressSelectValidate()">
											<c:forEach items="${stateDataList}" var="state"
												varStatus="stateStatus">
												<option value="${state.name}">${state.name}</option>
											</c:forEach>
										</form:select>
										<div class="errorMessage"><div id="erraddressState"></div></div> 
									</div>
									 

									<div class="half no-display country" id="countryListBox">
										<label><spring:theme code="text.addressBook.Country" text="Country *" /></label> 
										<%-- <select name="countryList"
											disabled="disabled">
											<c:forEach items="${countryData}" var="country"
												varStatus="countryStatus">
													<option value="${country.name}">${country.name}</option>
												<option value="IN">India</option>
											</c:forEach>
										</select> --%>
										
										<input type="text" name="CountryList" value="India" disabled="">
										
									</div>
									
									<div class="half" style="clear:both;">
									<label><spring:theme code="text.addressBook.PinCode" text="PinCode *" /></label>
										<form:input path="postcode" id="postcode"
											onkeyup="kpressaddresspost()" maxlength="6" />
											<div class="errorMessage"><div id="erraddressPost">   </div></div> 
									</div>
									
									<div class="half phone">
										<label><spring:theme code="text.addressBook.Phone"
												text="Mobile Number*" /></label>
										<%-- <form:input type="text" value="+91" id="myInput" inputCSS="form-text" path="MobileNo" disabled="true"/> --%>
										<select name="countryList"
											disabled="disabled">
											<c:forEach items="${countryData}" var="country"
												varStatus="countryStatus">
												<%-- 	<option value="${country.name}">${country.name}</option> --%>
												<option value="IN">+91</option>
											</c:forEach>
										</select>
											<form:input type="text" id="mobileNo" inputCSS="form-text"
												path="mobileNo" 
												onkeyup="kpressaddressmob()" maxlength="10" />
										
										<div class="errorMessage"><div id="erraddressMob"></div></div>
									</div>
								</fieldset>
								
								<%-- <input type="checkbox" name="mark-address-default" id="mark-address-default" class="account-only">
								<label for="mark-address-default" class="account-only"><spring:theme code="text.addressBook.makedefaultAddress" text="Mark as Default Address" /></label> --%>
								 <div id="checkBox">
										<%-- 
											<form:checkbox id="checkBox1" path="defaultAddress"
												name="checkBox" value="checkBox" /> --%>
												<input type="checkbox" id="checkBox1" name="defaultAddressCheckbox" value="true"/>
												<label for="checkBox1">Mark as default address</label>
									</div> 
								<div class="actions">
								<button type="submit" class="blue" id="addNewAddress"><spring:theme code="text.account.addressBook.addAddress" text="ADD NEW ADDRESS" />
									</button>
								
								<button type="submit" class="blue account-addressEditButton" id="edit" ><spring:theme code="text.addressBook.editAddress" text="SAVE ADDRESS" />
									</button>
									</div>
							</form:form></li>
					</ul>
				</div>
			</div>
		</div>
		</div>
	</template:page>

