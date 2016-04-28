<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>

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
  <h1 class="account-header"><spring:theme code="text.account.headerTitle" text="My Tata CLiQ" />
  		<user:accountMobileViewMenuDropdown pageNameDropdown="savedCards"/>
 <%--  <select class="menu-select" onchange="window.location=this.options[this.selectedIndex].value;">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value=/store/mpl/en/my-account/ data-href="/store/mpl/en/my-account/"><spring:theme code="header.flyout.overview" /></option>
                  <option value=/store/mpl/en/my-account/marketplace-preference data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme code="header.flyout.marketplacepreferences" /></option>
                  <option value=/store/mpl/en/my-account/update-profile data-href="account-info.php"><spring:theme code="header.flyout.Personal" /> </option>
                  <option value=/store/mpl/en/my-account/orders data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value=/store/mpl/en/my-account/payment-details data-href="account-cards.php"  selected><spring:theme code="header.flyout.cards" /></option>
                  <option value=/store/mpl/en/my-account/address-book data-href="account-addresses.php"><spring:theme code="header.flyout.address" /></option>
              </optgroup>
         
          <optgroup label="Share">
                  <option value=/store/mpl/en/my-account/friendsInvite data-href="account-invite.php"><spring:theme code="header.flyout.invite" /></option>
              </optgroup>
      </select> --%>
</h1>

		<div class="wrapper">
		
				<!----- Left Navigation Starts --------->
			<user:accountLeftNav pageName="savedCards"/>
			<!----- Left Navigation ENDS --------->
			<!----- Right Navigation ENDS --------->
			<div class="right-account">
				<div class="info card-list">
					<h1>
						<spring:theme code="text.account.SavedCards" text="Saved Cards" /><span><spring:theme code="text.account.SavedCards.details" text="Add, delete and manage your credit/debit card details here." /></span>
					</h1>
					<ul class="saved-cards">
					
						<c:choose>
							<c:when test="${not empty creditCards || not empty debitCards}">
								<c:forEach items="${creditCards}" var="creditCard" varStatus="creditStatus">
									<li>
										<!--  CARD DETAILS -->
										<div class="number paymentItem">
											<c:if
												test="${fn:escapeXml(creditCard.value.cardBrand) eq 'VISA'}">
												<img src="${commonResourcePath}/images/Visa.png">
											</c:if>
											<c:if
												test="${fn:escapeXml(creditCard.value.cardBrand) eq 'MASTERCARD'}">
												<img src="${commonResourcePath}/images/Master_Card.png">
											</c:if>
											<c:if
												test="${fn:escapeXml(creditCard.value.cardIssuer) eq 'AMEX'}">
												<img src="${commonResourcePath}/images/American_Express.png">
											</c:if>
											<a href="#nogo" class="view-details">View Card Details</a>

											<h3>${fn:escapeXml(creditCard.value.cardIssuer)}</h3>
											<c:if test="${not empty creditCard.value.cardType}">
												<h3>${fn:escapeXml(creditCard.value.cardType)}&nbsp;CARD</h3>
											</c:if>
											<p>${fn:escapeXml(creditCard.value.cardBrand)}&nbsp;ending in&nbsp;${creditCard.value.cardEndingDigits}</p>
											<p>
												<spring:theme code="text.expires" text="Expires" />
												${fn:escapeXml(creditCard.value.expiryMonth)}/<c:set var="expiryYearMasked" value=" ${fn:escapeXml(creditCard.value.expiryYear)}" />
												<c:set var="expiryYearLength" value="${fn:length(expiryYearMasked)}" /> 
												<c:set var="expiryYearNumEnd" value="${fn:substring(expiryYearMasked, expiryYearLength-4, expiryYearLength)}" />${fn:substring(expiryYearMasked, expiryYearLength-4, expiryYearLength)}
											</p>
										</div> <!--  CARD DETAILS -->
										
										<!-- Billing Address -->
										<div class="details">
											<ul>
												<li>
													<h3><spring:theme code="text.name.on.card"/></h3>
													<p>${creditCard.value.nameOnCard}</p>
												</li>
												<li>
													<h3><spring:theme code="text.billing.address"/></h3>
													<address>
															${creditCard.value.firstName}&nbsp;${creditCard.value.lastName}
														<br>${creditCard.value.addressLine1}
																&nbsp;${creditCard.value.addressLine2}
																&nbsp;${creditCard.value.addressLine3},
														<br> ${creditCard.value.city},
																&nbsp;${creditCard.value.state},
																&nbsp;${creditCard.value.pincode}
																&nbsp;${creditCard.value.country}
														<!-- <br> 91 (044) 3351 1500 // Not required-->
													</address>
												</li>
												<c:url value="/my-account/remove-payment-method?paymentInfoId=${creditCard.value.cardToken}"
												var="removePaymentActionUrl" />
												<%-- <input type="hidden" name="paymentInfoId"
													value="${paymentInfo.cardToken}" /> --%>
												<%-- <li><a href="${removePaymentActionUrl}" class="delete">Delete Card</a></li>	 --%>
												<li><a href="#nogo" data-toggle="modal" class="delete"
														data-target="#delete-card_${creditCard.value.cardToken}_${creditStatus.index}"
														data-mylist="<spring:theme code="text.help" />"
														data-dismiss="modal">
														<spring:theme code="text.remove" text="Delete Card" />
													</a>
												</li>
											</ul>
										</div>
										<!-- Billing Address -->
									</li>
									<div class="mpl-addressPopup delete-address-Popup modal cancellation-request fade" 
												id="delete-card_${creditCard.value.cardToken}_${creditStatus.index}">
												<div class="content">
													<button class="close" data-dismiss="modal"></button>
													<div id="popup_confirm_address_removal_${creditCard.value.cardToken}">
														<div class="addressItem">
													<div class="number paymentItem">
														<c:if
															test="${fn:escapeXml(creditCard.value.cardBrand) eq 'VISA'}">
															<img src="${commonResourcePath}/images/Visa.png">
														</c:if>
														<c:if
															test="${fn:escapeXml(creditCard.value.cardBrand) eq 'MASTERCARD'}">
															<img src="${commonResourcePath}/images/Master_Card.png">
														</c:if>
														<c:if
															test="${fn:escapeXml(creditCard.value.cardIssuer) eq 'AMEX'}">
															<img
																src="${commonResourcePath}/images/American_Express.png">
														</c:if>
														<br><br>
														<h3>${fn:escapeXml(creditCard.value.cardIssuer)}</h3>
														<c:if test="${not empty creditCard.value.cardType}">
															<h3>${fn:escapeXml(creditCard.value.cardType)}&nbsp;CARD</h3>
														</c:if>
														<p>${fn:escapeXml(creditCard.value.cardBrand)}&nbsp;ending
															in&nbsp;${creditCard.value.cardEndingDigits}</p>
														<p>
															<spring:theme code="text.expires" text="Expires" />
															${fn:escapeXml(creditCard.value.expiryMonth)}/
															<c:set var="expiryYearMasked"
																value=" ${fn:escapeXml(creditCard.value.expiryYear)}" />
															<c:set var="expiryYearLength"
																value="${fn:length(expiryYearMasked)}" />
															<c:set var="expiryYearNumEnd"
																value="${fn:substring(expiryYearMasked, expiryYearLength-4, expiryYearLength)}" />${fn:substring(expiryYearMasked, expiryYearLength-4, expiryYearLength)}
														</p>
													</div>
													<br>
													<p Class="address-deleteConfirmation">
																<spring:theme code="text.adress.remove.confirmation"
																	text="Are you sure you would like to delete this card?" />
															</p>

															<div class="buttons">
																<c:url value="/my-account/remove-payment-method?paymentInfoId=${creditCard.value.cardToken}"
																var="removePaymentActionUrl" />
																<a class="btn confirm-deletionButton btn-block"
																	<%-- data-address-id="${address.id}" --%>
																href="${removePaymentActionUrl}">
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
								</c:forEach>
								<c:forEach items="${debitCards}" var="debitCard" varStatus="debitStatus">
									<li>
										<!--  CARD DETAILS -->
										<div class="number paymentItem">
											<c:choose>
												<c:when test="${fn:escapeXml(debitCard.value.cardBrand) eq 'VISA'}">
													<img src="${commonResourcePath}/images/Visa.png">
												</c:when>
												<c:when test="${fn:escapeXml(debitCard.value.cardBrand) eq 'MASTERCARD'}">
													<img src="${commonResourcePath}/images/Master_Card.png">
												</c:when>
												<c:otherwise>
												<img class="credit-cards-sprite sprite-mastercard"
														src="${commonResourcePath}/images/Maestro.png">
												</c:otherwise>
											</c:choose>		
											<a href="#nogo" class="view-details">View Card Details</a>
							
											<h3>${fn:escapeXml(debitCard.value.cardIssuer)}</h3>
											<c:if test="${not empty debitCard.value.cardType}">
												<h3>${fn:escapeXml(debitCard.value.cardType)}&nbsp;CARD</h3>
											</c:if>
											<p>${fn:escapeXml(debitCard.value.cardBrand)}&nbsp;ending in&nbsp;${debitCard.value.cardEndingDigits}</p>
											<p>
												<spring:theme code="text.expires" text="Expires" />
												${fn:escapeXml(debitCard.value.expiryMonth)}/<c:set var="expiryYearMasked" value=" ${fn:escapeXml(debitCard.value.expiryYear)}" />
												<c:set var="expiryYearLength" value="${fn:length(expiryYearMasked)}" /> 
												<c:set var="expiryYearNumEnd" value="${fn:substring(expiryYearMasked, expiryYearLength-4, expiryYearLength)}" />${fn:substring(expiryYearMasked, expiryYearLength-4, expiryYearLength)}
											</p>
										</div> <!--  CARD DETAILS -->
										
										<!-- Billing Address -->
										<div class="details">
											<ul>
												<li>
													<h3><spring:theme code="text.name.on.card"/></h3>
													<p>${debitCard.value.nameOnCard}</p>
												</li>
												<li>
													&nbsp;
												</li>
												<c:url value="/my-account/remove-payment-method?paymentInfoId=${debitCard.value.cardToken}"
												var="removePaymentActionUrl" />
												<%-- <input type="hidden" name="paymentInfoId"
													value="${paymentInfo.cardToken}" /> --%>
												<%-- <li><a href="${removePaymentActionUrl}" class="delete">Delete Card</a></li> --%>
												<li><a href="#nogo" data-toggle="modal" class="delete"
														data-target="#delete-card_${debitCard.value.cardToken}_${debitStatus.index}"
														data-mylist="<spring:theme code="text.help" />"
														data-dismiss="modal">
														<spring:theme code="text.remove" text="Delete Address" />
													</a>
												</li>
	
											</ul>
										</div>
										<!-- Billing Address -->
									</li>
									<div class="mpl-addressPopup delete-address-Popup modal cancellation-request fade" 
												id="delete-card_${debitCard.value.cardToken}_${debitStatus.index}">
												<div class="content">
													<button class="close" data-dismiss="modal"></button>
													<div id="popup_confirm_address_removal_${debitCard.value.cardToken}">
														<div class="addressItem">
													<div class="number paymentItem">
														<c:choose>
															<c:when
																test="${fn:escapeXml(debitCard.value.cardBrand) eq 'VISA'}">
																<img src="${commonResourcePath}/images/Visa.png">
															</c:when>
															<c:when
																test="${fn:escapeXml(debitCard.value.cardBrand) eq 'MASTERCARD'}">
																<img src="${commonResourcePath}/images/Master_Card.png">
															</c:when>
															<c:otherwise>
																<img class="credit-cards-sprite sprite-mastercard"
																	src="${commonResourcePath}/images/Maestro.png">
															</c:otherwise>
														</c:choose>
														<br><br>
														<h3>${fn:escapeXml(debitCard.value.cardIssuer)}</h3>
														<c:if test="${not empty debitCard.value.cardType}">
															<h3>${fn:escapeXml(debitCard.value.cardType)}&nbsp;CARD</h3>
														</c:if>
														<p>${fn:escapeXml(debitCard.value.cardBrand)}&nbsp;ending
															in&nbsp;${debitCard.value.cardEndingDigits}</p>
														<p>
															<spring:theme code="text.expires" text="Expires" />
															${fn:escapeXml(debitCard.value.expiryMonth)}/
															<c:set var="expiryYearMasked"
																value=" ${fn:escapeXml(debitCard.value.expiryYear)}" />
															<c:set var="expiryYearLength"
																value="${fn:length(expiryYearMasked)}" />
															<c:set var="expiryYearNumEnd"
																value="${fn:substring(expiryYearMasked, expiryYearLength-4, expiryYearLength)}" />${fn:substring(expiryYearMasked, expiryYearLength-4, expiryYearLength)}
														</p>
													</div>
													<br>
													<p Class="address-deleteConfirmation">
																<spring:theme code="text.adress.remove.confirmation"
																	text="Are you sure you would like to delete this card?" />
															</p>

															<div class="buttons">
																<c:url value="/my-account/remove-payment-method?paymentInfoId=${debitCard.value.cardToken}"
																var="removePaymentActionUrl" />
																<a class="btn confirm-deletionButton btn-block"
																	<%-- data-address-id="${address.id}" --%>
																href="${removePaymentActionUrl}">
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
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li>
									<p class="emptyMessage">
										<div class="account-emptyOrderMessage"><spring:theme
											code="text.account.paymentDetails.noPaymentInformation"
											text="No Saved Payment Details" /></div>
									</p>
								</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			</div>
			<!----- Right Navigation ENDS --------->
		</div>
		</div>
	</template:page>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script> --%>
