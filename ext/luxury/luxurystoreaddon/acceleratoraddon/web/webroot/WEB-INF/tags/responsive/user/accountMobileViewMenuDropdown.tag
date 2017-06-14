<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageNameDropdown" required="true" type="java.lang.String"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="pageNameDropdown" value="${pageNameDropdown}">

<!-- TISLUX-89 -->
<c:set var="isLuxVal" value="${not empty param.isLux?param.isLux:false}"/>
<select class="menu-select" id="menuPageSelect"
	onchange="window.location=this.options[this.selectedIndex].value;">
	<optgroup label='<spring:theme code="header.flyout.myaccount" />'>
		<option value=/store/mpl/en/my-account?isLux=${isLuxVal}
			 data-href="/store/mpl/en/my-account/"><spring:theme
				code="header.flyout.overview" />
		</option>
		<option value=/store/mpl/en/my-account/marketplace-preference?isLux=${isLuxVal}
			data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme
				code="header.flyout.marketplacepreferences" />
		</option>
		<option value=/store/mpl/en/my-account/update-profile?isLux=${isLuxVal}
			data-href="account-info.php"><spring:theme
				code="header.flyout.Personal" />
		</option>
		<option value=/store/mpl/en/my-account/orders?isLux=${isLuxVal}
			data-href="order-history.php">
			<spring:theme code="header.flyout.orders" />
		</option>
		<option value=/store/mpl/en/my-account/payment-details?isLux=${isLuxVal}
			data-href="account-cards.php"><spring:theme
				code="header.flyout.cards" />
		</option>
		<option value=/store/mpl/en/my-account/address-book?isLux=${isLuxVal}
			data-href="account-addresses.php"><spring:theme
				code="header.flyout.address" />
		</option>
		<option value=/store/mpl/en/my-account/reviews?isLux=${isLuxVal}
						data-href="account-addresses.php"><spring:theme
							code="header.flyout.review" />
		</option>
		<option value=/store/mpl/en/my-account/myInterest?isLux=${isLuxVal}
			data-href="account-addresses.php"><spring:theme
				code="header.flyout.recommendations" />
		</option>
	</optgroup>

	<optgroup label="Credit">
		<option value=/store/mpl/en/my-account/coupons?isLux=${isLuxVal}
			data-href="account-invite.php"><spring:theme
				code="header.flyout.coupons" />
		</option>
	</optgroup>
	<optgroup label="Share">
		<option value=/store/mpl/en/my-account/friendsInvite?isLux=${isLuxVal}
			data-href="account-invite.php"><spring:theme
				code="header.flyout.invite" />
		</option>
	</optgroup>
</select>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>
