<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>
	
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />

<div class="account preferences">
	<template:page pageTitle="${pageTitle}">
		<c:url var="mainUrl" value="/my-account/marketplace-preference"></c:url>
		<c:if test="${not empty result && result eq 'success'}">
			<div class="alert alert-info alert-dismissible">
				<a href="#nogo" onclick="changeUrl('${mainUrl}')" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    		<spring:theme code="text.account.preference.save.success" text="Your preferences has been updated"/>
  			</div>
		</c:if>
		<c:if test="${not empty result && result eq 'unsubscribed'}">
			<div class="alert alert-danger">
				<a href="#nogo" onclick="changeUrl('${mainUrl}')" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    		<spring:theme code="text.account.preference.save.unsubscribed" text="Aww snap, you're leaving. Is it something we said?"/>
  			</div>
		</c:if>
		<c:if test="${not empty result && result eq 'failure'}">
			<div class="alert alert-danger">
				<a href="#nogo" onclick="changeUrl('${mainUrl}')" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    		<spring:theme code="text.account.preference.save.failure" text="Your preferences has not been updated"/>
  			</div>
		</c:if>
		
		<h1 class="account-header">
			<spring:theme code="text.account.headerTitle" text="My MarketPlace" />
			<select class="menu-select" onchange="window.location=this.options[this.selectedIndex].value;">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value=/store/mpl/en/my-account/ data-href="/store/mpl/en/my-account/"><spring:theme code="header.flyout.overview" /></option>
                  <option value=/store/mpl/en/my-account/marketplace-preference data-href="/store/mpl/en/my-account/marketplace-preference" selected><spring:theme code="header.flyout.marketplacepreferences" /></option>
                  <option value=/store/mpl/en/my-account/update-profile data-href="account-info.php"><spring:theme code="header.flyout.Personal" /> </option>
                  <option value=/store/mpl/en/my-account/orders data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value=/store/mpl/en/my-account/payment-details data-href="account-cards.php"><spring:theme code="header.flyout.cards" /></option>
                  <option value=/store/mpl/en/my-account/address-book data-href="account-addresses.php"><spring:theme code="header.flyout.address" /></option>
          </optgroup>
         
          <optgroup label="Share">
                  <option value=/store/mpl/en/my-account/friendsInvite data-href="account-invite.php"><spring:theme code="header.flyout.invite" /></option>
          </optgroup>
      </select>
		</h1>

		<!----- Left Navigation Starts --------->
		<div class="wrapper">
			<%-- <div class="left-nav">
				<h1>
					<spring:theme code="text.account.headerTitle" text="My MarketPlace" />
				</h1>
				<ul>
					<li><h3>
							<spring:theme code="header.flyout.myaccount" />
						</h3></li>
					<li><a href="<c:url value="/my-account/"/>"><spring:theme
								code="header.flyout.overview" /></a></li>
					<li><a class="active"
						href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
								code="header.flyout.marketplacepreferences" /></a></li>
					<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
								code="header.flyout.Personal" /></a></li>
					<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
								code="header.flyout.orders" /></a></li>
					<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
								code="header.flyout.cards" /></a></li>
					<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
								code="header.flyout.address" /></a></li>
								<li><a href="<c:url value="/my-account/reviews"/>"><spring:theme
										code="header.flyout.review" /></a></li>
								<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
								code="header.flyout.recommendations" /></a></li>
				</ul>
				<ul>
				<li class="header-SignInShare"><h3><spring:theme
									code="header.flyout.credits" /></h3></li>
						<li><a href="<c:url value="/my-account/coupons"/>"><spring:theme
									code="header.flyout.coupons" /></a></li>
				</ul>
				<ul>
					<li><h3>
							<spring:theme code="header.flyout.share" />
						</h3></li>
					<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
								code="header.flyout.invite" /></a></li>

				</ul>

			</div> --%>
			<user:accountLeftNav pageName="mplPref"/>
			<!----- Left Navigation ENDS --------->



			<!----- RIGHT Navigation STARTS --------->

			<div class="right-account">
				<div>
					<h1>
						<spring:theme code="text.account.MarketplacePreferences"
							text="Marketplace Preferences" />
					</h1>
					<p>
						<spring:theme code="account.marketplacePreferences.text"
							text="The pit stop to tell us all about your favourite brands, categories and likes" />
					</p>
				</div>


				<div class="email-pref">

					<h2>
						<spring:theme code="text.marketplacePreferences.subTitle"
							text="Newsletter: #StayQued" />
					</h2>
					<p>
						<spring:theme code="text.marketplacePreferences.subTitleInfo"
							text="Keep in touch to hear from us again. Select your likes and hit on Save Changes. And you are good to go! " />
					</p>
					<form>
					<input type="hidden" id="selectedInterest" value="${selectedInterest}">
						<div class="emails">
							<c:forEach items="${interestList}" var="interest"
								varStatus="status">
								<input type="radio" name="interest"
									id="radioInterest${status.index}" value="${interest}"
									<c:if test="${not empty selectedInterest && interest eq selectedInterest}">
									checked="checked"
									</c:if>>
								<label for="radioInterest${status.index}"> <span>${interest}</span>
								</label>
							</c:forEach>
						</div>
						<fieldset>
							<p>
								<spring:theme code="text.marketplacePreferences.categories"
									text="Choose wisely. Tell us your preferences. Choose wisely. Tell us your preferences. Choose wisely. Tell us your preferences. " />
							</p>
							
							<div class="mplPref-category">
								<c:if test="${not empty selectedCategoryList}">
									<c:forEach items="${categoryList}" var="category"
										varStatus="status">
										<input type="checkbox" name="categoryData" id="categoryData-${status.index}" value="${category.code}"
											<c:forEach items="${selectedCategoryList}" var="selectedCategory">
												<c:if test="${not empty selectedCategory.code && category.code eq selectedCategory.code}">
												checked="checked"
												</c:if>
											</c:forEach>>
										<label for="categoryData-${status.index}">
											<span>${category.name}</span>
										</label>
									</c:forEach>
								</c:if>
								<c:if test="${empty selectedCategoryList}">
										<c:forEach items="${categoryList}" var="category"
											varStatus="status">
											<input type="checkbox" name="categoryData" id="categoryData-${status.index}" value="${category.code}" 
												<c:if test="${is_already_subscribed eq FALSE || is_already_subscribed eq false}">
												checked="checked"
												</c:if>>
											<label for="categoryData-${status.index}">
												<span>${category.name}</span>
											</label>
										</c:forEach>
								</c:if>
							</div>
						</fieldset>
						<fieldset>
							<p>
								<spring:theme code="text.marketplacePreferences.brand"
									text="Tell us about the brands you love." />
							</p>
							<div class="mplPref-category">
								<c:if test="${not empty selectedBrandList}">
									<c:forEach items="${brandList}" var="brand"
										varStatus="status">
										<input type="checkbox" name="brandData" id="brandData-${status.index}" value="${brand.code}"
											<c:forEach items="${selectedBrandList}" var="selectedBrand">
												<c:if test="${not empty selectedBrand.code && brand.code eq selectedBrand.code}">
												checked="checked"
												</c:if>
											</c:forEach>>
										<label for="brandData-${status.index}">
											<span>${brand.name}</span>
										</label>
									</c:forEach>
								</c:if>
								<c:if test="${empty selectedBrandList}">
									<c:forEach items="${brandList}" var="brand"
										varStatus="status">
										<input type="checkbox" name="brandData" id="brandData-${status.index}" value="${brand.code}" 
											<c:if test="${is_already_subscribed eq FALSE || is_already_subscribed eq false}">
												checked="checked"
											</c:if>>
										<label for="brandData-${status.index}">
											<span>${brand.name}</span>
										</label>
									</c:forEach>
								</c:if>
							</div>
						</fieldset>
						<fieldset class="frequent">
							<p>
								<spring:theme code="text.marketplacePreferences.mailfrequency"
									text="How often would you like to hear from us?" />
							</p>
							<div class="freq">
								<c:forEach items="${frequencyList}" var="frequency" varStatus="status">
									<input type="radio" name="frequency"
										id="radioFrequency${status.index}" value="${frequency}"
										<c:if test="${not empty selectedFreq && frequency eq selectedFreq}">
									checked="checked"
									</c:if>>
									<label for="radioFrequency${status.index}">
											<span>${frequency}</span>
										</label>
								</c:forEach>
							</div>
						</fieldset>
						<fieldset class="feedback">
							<p>
								<cms:pageSlot position="Section1" var="feature">
									<cms:component component="${feature}" />
								</cms:pageSlot>
							</p>
								<c:if test="${not empty selectedFeedbackList}">
									<c:forEach items="${feedbackAreaList}" var="feedbackArea"
										varStatus="status">
										<input type="checkbox" name="feedbackArea" id="feedbackArea-${status.index}" value="${feedbackArea}"
											<c:forEach items="${selectedFeedbackList}" var="selectedfeedbackArea">
												<c:if test="${not empty selectedfeedbackArea && feedbackArea eq selectedfeedbackArea}">
												checked="checked"
												</c:if>
											</c:forEach>>
										<label for="feedbackArea-${status.index}">
											<span>${feedbackArea}</span>
										</label>
									</c:forEach>
								</c:if>
								<c:if test="${empty selectedFeedbackList}">
									<c:forEach items="${feedbackAreaList}" var="feedbackArea"
										varStatus="status">
										<input type="checkbox" name="feedbackArea" id="feedbackArea-${status.index}" value="${feedbackArea}"
											<c:if test="${is_already_subscribed eq FALSE || is_already_subscribed eq false}">
												checked="checked"
											</c:if>>
										<label for="feedbackArea-${status.index}">
											<span>${feedbackArea}</span>
										</label>
									</c:forEach>
								</c:if>
						</fieldset>
						<div class="button-set">
							<input type="hidden" id="isUnsubcribed">
							
							<button class="blue" id="saveMarketPrefButton" type="button">
								<spring:theme code="cart.modal.save.changes" text="SAVE CHANGES" />
							</button>
							<a href="#nogo" id="unsubcribe-link">
								<spring:theme code="text.marketplacePreferences.unsubcribe.all"
									text="Unsubscribe from All" />
							</a>

						</div>
					</form>
				</div>
			</div>
		</div>
	</template:page>
</div>

<%-- <script type="text/javascript" src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript" src="${commonResourcePath}/js/acc.accountaddress.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script> --%>
