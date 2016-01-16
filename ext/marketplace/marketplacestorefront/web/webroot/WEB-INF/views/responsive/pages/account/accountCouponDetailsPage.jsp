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
<spring:url value="/my-account/coupons" var="couponsUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />
<spring:url value="/my-account/coupons/coupon-history"
	var="couponHistoryDetailsUrl" />


<template:page pageTitle="${pageTitle}">
	<div class="account">
		<h1 class="account-header">
			<spring:theme code="text.account.headerTitle" text="My Marketplace" />
			<select class="menu-select"
				onchange="window.location=this.options[this.selectedIndex].value;">
				<optgroup label="<spring:theme code="header.flyout.myaccount" />">
					<option value=/store/mpl/en/my-account
						/ data-href="/store/mpl/en/my-account/"><spring:theme
							code="header.flyout.overview" /></option>
					<option value=/store/mpl/en/my-account/marketplace-preference
						data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme
							code="header.flyout.marketplacepreferences" /></option>
					<option value=/store/mpl/en/my-account/update-profile
						data-href="account-info.php"><spring:theme
							code="header.flyout.Personal" />
					</option>
					<option value=/store/mpl/en/my-account/orders
						data-href="order-history.php">
						<spring:theme code="header.flyout.orders" /></option>
					<option value=/store/mpl/en/my-account/payment-details
						data-href="account-cards.php"><spring:theme
							code="header.flyout.cards" /></option>
					<option value=/store/mpl/en/my-account/address-book
						data-href="account-addresses.php"><spring:theme
							code="header.flyout.address" /></option>
					<option value=/store/mpl/en/my-account/reviews
						data-href="account-addresses.php"><spring:theme
							code="header.flyout.review" /></option>
					<option value=/store/mpl/en/my-account/myInterest
						data-href="account-addresses.php"><spring:theme
							code="header.flyout.recommendations" /></option>
				</optgroup>

				<optgroup label="Credits">
					<option value=/store/mpl/en/my-account/coupons
						data-href="account-coupon.php" selected><spring:theme
							code="header.flyout.coupons" /></option>
				</optgroup>
				<optgroup label="Share">
					<option value=/store/mpl/en/my-account/friendsInvite
						data-href="account-invite.php"><spring:theme
							code="header.flyout.invite" /></option>
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
					<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
								code="header.flyout.address" /></a></li>
					<li><a href="<c:url value="/my-account/reviews"/>"><spring:theme
								code="header.flyout.review" /></a></li>
					<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
								code="header.flyout.recommendations" /></a></li>
				</ul>
				<ul>
					<li><h3>
							<spring:theme code="header.flyout.credits" />
						</h3></li>
					<li><a class="active"
						href="<c:url value="/my-account/coupons"/>"><spring:theme
								code="header.flyout.coupons" /></a></li>

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
			<div class="right-account rewards">



				<!-- for showing all type of  coupons without semiclosed -start-->


				<div class="your-activity coupon-listing">
					<h2>Coupons</h2>
					<p>Listing of offers &amp; discounts with coupon codes, which
						helps you to save money and smart shopping!</p>
					<p class="mobile">Edit the coupon code in your cart to receive
						your discount.</p>
					<ul class="coupon-container">
						<c:forEach items="${closedCouponList}" var="closedVoucherDisplay"
							varStatus="vlstatus">
							<li class="coupon-box starred">

								<h2>${closedVoucherDisplay.voucherDescription}</h2>
								<c:if test="${not empty closedVoucherDisplay.reedemCouponCount}">
								<div align="center">
									<c:choose>
										<c:when
											test="${closedVoucherDisplay.reedemCouponCount eq '1'}">
											<p>Single</p>
										</c:when>
										<c:otherwise>
											<p>Multiple</p>
										</c:otherwise>
									</c:choose>
									</div>
								</c:if>
								<div class="left">
									<p>Coupon Code</p>
									${closedVoucherDisplay.voucherCode}
								</div>
								<div class="right">
									<p>Expires on</p>
									<p>${closedVoucherDisplay.voucherExpiryDate}</p>
								</div>
							</li>
						</c:forEach>


					</ul>
					<div class="bottom">
						<p>1-5 of 12 Transactions</p>

						<ul class="pagination">
							<li class="number first active"><a href="?page=1">1</a></li>
							<li class="number last "><a href="?page=2">2</a></li>
							<li class="next"><a href="?page=2">Next</a></li>
						</ul>
					</div>
				</div>


				<!-- for showing all type of  coupons without semiclosed-end -->


				<!-- for showing  coupons history-start -->
				<div class="your-activity coupon-history">
					<c:if test="${not empty couponOrderDataDTOList}">
					<h2><spring:theme code="text.account.coupons.couponHistory"/></h2>
						<p>
							<spring:theme code="text.account.coupons.youhvused" />
							<span>${couponsRedeemedCount}&nbsp;<c:if
									test="${couponsRedeemedCount < 2}">
									<spring:theme code="text.account.coupons.coupon1" />
								</c:if> <c:if test="${couponsRedeemedCount > 1}">
									<spring:theme code="text.account.coupons.coupons" />
								</c:if>
							</span>&nbsp;
							<spring:theme code="text.account.coupons.sofarsaved" />
							<span>Rs. ${totalSavedSum}</span>
							<spring:theme code="text.account.coupons.onpurchase" />
						</p>
						<c:if test="${not empty commentsListSize}">
							<c:forEach begin="1" end="${totalPages}" var="i">
								<c:choose>
									<c:when test="${param.page eq i}">
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
							</c:forEach>
							<div class="bottom">
								<c:if test="${not empty commentsListSize}">
									<p>${startIndex}-${endIndex}
										of ${commentsListSize} &nbsp;
										<spring:theme code="text.account.coupons.transactions" />
									</p>
								</c:if>
								<div class="btn-placement bottom">
									<c:if test="${totalPages ne 1 }">
										<ul class="pagination">
											<!-- Previous link addition -->
											<c:if
												test="${param.page != 1 and not empty param.page and not empty couponOrderDataDTOList}">
												<li class="prev"><a href="#nogo"><spring:theme
															code="text.account.coupons.prev" /></a></li>
											</c:if>
											<c:forEach begin="1" end="${totalPages}" var="i">
												<c:choose>
													<c:when test="${param.page eq i}">
														<li class="number first active"><a href="?page=${i}">${i}</a></li>
													</c:when>
													<c:otherwise>
														<li class="number first"><a href="?page=${i}">${i}</a></li>
													</c:otherwise>
												</c:choose>
											</c:forEach>
											<c:choose>
												<c:when test="${param.page eq null}">
													<c:set var="page" value="1"></c:set>
												</c:when>
												<c:otherwise>
													<c:set var="page" value="${param.page}"></c:set>
												</c:otherwise>
											</c:choose>
											<!-- Next link addition -->
											<c:if test="${totalPages gt 1 and totalPages gt page}">
												<li class="next"><a href="#nogo"><spring:theme
															code="text.account.coupons.next" /></a></li>
											</c:if>

										</ul>
									</c:if>
								</div>
							</div>
						</c:if>

						<ul>
							<li class="header">
								<p class="coupon">
									<spring:theme code="text.account.coupons.coupon" />
								</p>
								<p class="description">
									<spring:theme code="text.account.coupons.decription" />
								</p>
								<p class="order">
									<spring:theme code="text.account.coupons.appliedorder" />
								</p>
								<p class="date">
									<spring:theme code="text.account.coupons.date" />
								</p>
							</li>
							<c:forEach items="${couponOrderDataDTOList}"
								var="couponHistoryDetailDTO">
								<li class="cashback-row ">
									<p class="coupon">
										<span>${couponHistoryDetailDTO.couponCode}</span>
									</p>
									<p class="description">
										<span>${couponHistoryDetailDTO.couponDescription}</span>
									</p> <c:if test="${couponHistoryDetailDTO ne null}">
										<p class="order">
											#<span>${couponHistoryDetailDTO.orderCode}</span>
										</p>
										<p class="date">
											<span>${couponHistoryDetailDTO.redeemedDate}</span>
										</p>
									</c:if>
								</li>
							</c:forEach>
						</ul>

					</c:if>

					<div class="bottom">
						<c:if test="${not empty commentsListSize}">
							<p>${startIndex}-${endIndex}
								of ${commentsListSize} &nbsp;
								<spring:theme code="text.account.coupons.transactions" />
							</p>
						</c:if>
						<div class="btn-placement bottom">
							<c:if test="${totalPages ne 1 }">
								<ul class="pagination">
									<!-- Previous link addition -->
									<c:if
										test="${param.page != 1 and not empty param.page and not empty couponOrderDataDTOList}">
										<li class="prev"><a href="#nogo"><spring:theme
													code="text.account.coupons.prev" /></a></li>
									</c:if>
									<c:forEach begin="1" end="${totalPages}" var="i">
										<c:choose>
											<c:when test="${param.page eq i}">
												<li class="number first active"><a href="?page=${i}">${i}</a></li>
											</c:when>
											<c:otherwise>
												<li class="number first"><a href="?page=${i}">${i}</a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${param.page eq null}">
											<c:set var="page" value="1"></c:set>
										</c:when>
										<c:otherwise>
											<c:set var="page" value="${param.page}"></c:set>
										</c:otherwise>
									</c:choose>
									<!-- Next link addition -->
									<c:if test="${totalPages gt 1 and totalPages gt page}">
										<li class="next"><a href="#nogo"><spring:theme
													code="text.account.coupons.next" /> </a></li>
									</c:if>

								</ul>
							</c:if>
						</div>
					</div>
				</div>
				<div class="couponHistoryLinkDiv">
					<a href="#nogo" id="couponHistory" class="couponHistoryLink"> <spring:theme
							code="text.account.coupons.usage.history"
							text="Coupon transaction history" /></a> <a href="#nogo"
						id="couponHistoryHide" class="couponHistoryLink"> <spring:theme
							code="text.account.coupons.back.history"
							text="Back to Coupon Details" /></a>
				</div>


				<!-- for showing  coupons history-end -->

				<div class="customer-service steps">
					<h2>
						<spring:theme code="text.account.coupons.work" />
					</h2>
					<ul>
						<li class="step"><span>1</span>
							<p>
								<spring:theme code="text.account.coupons.work.step1" />
							</p></li>
						<li class="step"><span>2</span>
							<p>
								<spring:theme code="text.account.coupons.work.step2" />
							</p></li>
						<li class="step"><span>3</span>
							<p>
								<spring:theme code="text.account.coupons.work.step3" />
							</p></li>
						<li class="step"><span>4</span>
							<p>
								<spring:theme code="text.account.coupons.work.step4" />
							</p></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</template:page>

<script type="text/javascript">
	$(document).ready(function() {
		$("#couponHistory").click(function() {
			$(".your-activity.coupon-history").slideDown();
			$(this).hide();
			$("#couponHistoryHide").show();
		});
		$("#couponHistoryHide").click(function() {
			$(".your-activity.coupon-history").slideUp();
			$(this).hide();
			$("#couponHistory").show();
		});
	});

	$(".next a").click(function() {
		var pageNo = $(this).closest(".pagination").find("li.active a").text();
		if (pageNo != "") {
			pageNo = parseInt(pageNo);
		} else {
			pageNo = 1;
		}
		pageNo = pageNo + 1;
		var totalPages = $
		{
			totalPages
		}
		;
		if (totalPages != "" && pageNo <= totalPages) {
			window.location.href = "?page=" + pageNo;
		}
	});

	$(".prev a").click(function() {
		var pageNo = $(this).closest(".pagination").find("li.active a").text();
		pageNo = parseInt(pageNo);
		pageNo = pageNo - 1;
		var totalPages = $
		{
			totalPages
		}
		;
		if (pageNo != 0 && totalPages != "" && pageNo <= totalPages) {
			window.location.href = "?page=" + pageNo;
		}
	});
</script>
