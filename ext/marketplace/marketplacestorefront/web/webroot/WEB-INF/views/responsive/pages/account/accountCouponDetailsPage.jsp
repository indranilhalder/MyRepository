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
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>

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
					<option value=/store/mpl/en/my-account/ data-href="/store/mpl/en/my-account/"><spring:theme
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

			</div> --%>
			
			<user:accountLeftNav pageName="coupons"/>
			
			<!----- Left Navigation ENDS --------->
			<!----- RIGHT Navigation STARTS --------->
			<div class="right-account rewards">



				<!-- for showing all type of  coupons without semiclosed -start-->


				<div class="your-activity coupon-listing">
					<h2><spring:theme code="text.account.coupons.title" /></h2>
					<p><spring:theme code="text.account.coupons.display.description" /></p>
					<p class="mobile"><spring:theme code="text.account.coupons.display.description.mobile" /></p>
					<ul class="coupon-container">
						<c:choose>
							<c:when test="${empty closedCouponList}">
								<div>
								<h2>
										<spring:theme code="text.account.coupons.nocouponavailable" />
									</h2>
								</div>
							</c:when>
							<c:otherwise>
								<c:forEach items="${closedCouponList}" var="closedVoucherDisplay" varStatus="vlstatus">
									<li class="coupon-box starred">
									<h2>${closedVoucherDisplay.voucherDescription}</h2> <c:if
										test="${not empty closedVoucherDisplay.reedemCouponCount}">
										<div align="center">
											<c:choose>
												<c:when
													test="${closedVoucherDisplay.reedemCouponCount eq '1'}">
													<p class="coupon_count">Single</p>
												</c:when>
												<c:otherwise>
													<p class="coupon_count">Multiple</p>
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
					      </c:otherwise>
						</c:choose>
						
					</ul>
					<!--  pagination for upper section  -->
					<div class="bottom btn-placement">
						<%-- <c:if test="${not empty closedCouponList}">
							<p>${startIndexCoupon}-${endIndexCoupon}
								of ${couponListSize} &nbsp;
								<spring:theme code="text.account.coupons.coupons" />
							</p>
						</c:if> --%>
						<c:if test="${not empty searchPageData.results}">
						<!-- TISSRT-630 ---- Set values in hidden filed for lazy loading pagination -->
							<input type="hidden" id="pageIndexC" value="${pageIndex}" />
							<input type="hidden" id="pagableSizeC" value="${pageSize}" />
							<input type="hidden" id="totalNumberOfResultsC"
								value="${searchPageData.pagination.totalNumberOfResults}" />
							<div id="displayPaginationCountUpCoupon"></div>
						</c:if>
						<nav:mpl-pagination top="true" supportShowPaged="${isShowPageAllowed}"
							supportShowAll="${isShowAllAllowed}"
							searchPageData="${searchPageData}"
							searchUrl="/my-account/coupons?sort=${searchPageData.pagination.sort}"
							numberPagesShown="${numberPagesShown}" />
							
							
							
							
							<c:if test="${totalPagesCoupon ne 1 }">
								<ul class="pagination">
									<!-- Previous link addition -->
									<c:if
										test="${param.pageVoucher != 1 and not empty param.pageVoucher and not empty closedCouponList}">
										<li class="prev" id="voucherPrev"><a href="#nogo"><spring:theme
													code="text.account.coupons.prev" /> <span
												class="lookbook-only"></span></a></li>
									</c:if>

									<c:forEach begin="1" end="${totalPagesCoupon}" var="i">
										<c:choose>
											<c:when test="${param.pageVoucher eq i}">
												<li class="number first active"><a href="?pageVoucher=${i}&pageFor=voucher">${i}</a></li>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${param.pageVoucher eq null and i eq 1}">
														<li class="number first active"><a href="?pageVoucher=${i}&pageFor=voucher">${i}</a></li>
													</c:when>
													<c:otherwise>
														<li class="number first"><a href="?pageVoucher=${i}&pageFor=voucher">${i}</a></li>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${param.pageVoucher eq null}">
											<c:set var="page" value="1"></c:set>
										</c:when>
										<c:otherwise>
											<c:set var="page" value="${param.pageVoucher}"></c:set>
										</c:otherwise>
									</c:choose>
									<!-- Next link addition -->
									
									<c:if test="${totalPagesCoupon gt 1 and param.pageVoucher ne totalPagesCoupon}">
										<li class="next" id="voucherNext"><a href="#nogo"><spring:theme
													code="text.account.coupons.next" /> <span
												class="lookbook-only"></span></a></li>
									</c:if>

								</ul>
							</c:if>
					
					</div>
				</div>


				<!-- for showing all type of  coupons without semiclosed-end -->


				<!-- for showing  coupons history-start -->
				<div class="your-activity coupon-history">

					<c:choose>
						<c:when test="${not empty couponOrderDataDTOList}">

							<h2><a id="transactionHistory" name="transactionHistory" style="position:absolute; top:100px;"></a>
								<spring:theme code="text.account.coupons.couponHistory" />
							</h2>

							<p>
								<spring:theme code="text.account.coupons.youhvused" />
								<span> ${couponsRedeemedCount}&nbsp;<c:if
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
							<c:if test="${not empty couponHistListSize}">
								<c:forEach begin="1" end="${totalPagesCouponHist}" var="i">
									<c:choose>
										<c:when test="${param.pageHistory eq i}">
										</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</c:forEach>
								<div class="bottom btn-placement">
									<c:if test="${not empty couponOrderDataDTOList}">
										<p>${startIndexHist}-${endIndexHist}
											of ${couponHistListSize} &nbsp;
											<spring:theme code="text.account.coupons.transactions" />
										</p>
									</c:if>
										<c:if test="${totalPagesCouponHist ne 1 }">
											<ul class="pagination">
												<!-- Previous link addition -->
												<c:if
													test="${param.pageHistory != 1 and not empty param.pageHistory and not empty couponOrderDataDTOList}">
													<li class="prev" id="historyPrev"><a href="#" ><spring:theme
																code="text.account.coupons.prev" /> <span
															class="lookbook-only"></span></a></li>
												</c:if>
												<c:forEach begin="1" end="${totalPagesCouponHist}" var="i">
													<c:choose>
														<c:when test="${param.pageHistory eq i}">
															<li class="number first active"><a href="?pageHistory=${i}&pageFor=history#transactionHistory">${i}</a></li>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${param.pageHistory eq null and i eq 1}">
																	<li class="number first active"><a
																		href="?pageHistory=${i}&pageFor=history#transactionHistory">${i}</a></li>
																</c:when>
																<c:otherwise>
																	<li class="number first"><a href="?pageHistory=${i}&pageFor=history#transactionHistory">${i}</a></li>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												</c:forEach>
												<c:choose>
													<c:when test="${param.page eq null}">
														<c:set var="pageHistory" value="1"></c:set>
													</c:when>
													<c:otherwise>
														<c:set var="pageHistory" value="${param.pageHistory}"></c:set>
													</c:otherwise>
												</c:choose>
												<!-- Next link addition -->
												<c:if test="${totalPagesCouponHist gt 1 and totalPagesCouponHist gt pageHistory and param.pageHistory ne totalPagesCouponHist}">
													<li class="next" id="historyNext"><a href="#"><spring:theme
																code="text.account.coupons.next" /> <span
															class="lookbook-only"></span></a></li>
												</c:if>

											</ul>
										</c:if>
									
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

						</c:when>
						<c:otherwise>
							<c:if test="${empty couponOrderDataDTOList}">
								<div>
									<h2>
										<spring:theme code="text.account.coupons.nocouponhistory" />
									</h2>
								</div>
							</c:if>
						</c:otherwise>

					</c:choose>

					<div class="bottom btn-placement">
						<c:if test="${not empty couponOrderDataDTOList}">
										<p>${startIndexHist}-${endIndexHist}
											of ${couponHistListSize} &nbsp;
											<spring:theme code="text.account.coupons.transactions" />
										</p>
						</c:if>
						
							<%-- <c:if test="${totalPagesCouponHist ne 1 }">
								<ul class="pagination">
									<!-- Previous link addition -->
									<c:if
										test="${param.pageHistory != 1 and not empty param.pageHistory and not empty couponOrderDataDTOList}">
										<li class="prev" id="historyPrevBtm"><a href="#nogo"><spring:theme
													code="text.account.coupons.prev" /> <span
												class="lookbook-only"></span></a></li>
									</c:if>

									<c:forEach begin="1" end="${totalPagesCouponHist}" var="i">
										<c:choose>
											<c:when test="${param.pageHistory eq i}">
												<li class="number first active"><a href="?pageHistory=${i}&pageFor=history#transactionHistory">${i}</a></li>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${param.pageHistory eq null and i eq 1}">
														<li class="number first active"><a href="?pageHistory=${i}&pageFor=history#transactionHistory" >${i}</a></li>
													</c:when>
													<c:otherwise>
														<li class="number first"><a href="?pageHistory=${i}&pageFor=history#transactionHistory">${i}</a></li>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${param.pageHistory eq null}">
											<c:set var="pageHistory" value="1"></c:set>
										</c:when>
										<c:otherwise>
											<c:set var="pageHistory" value="${param.pageHistory}"></c:set>
										</c:otherwise>
									</c:choose>
									<!-- Next link addition -->
									<c:if test="${totalPagesCouponHist gt 1 and param.pageHistory ne totalPagesCouponHist}">
										<li class="next" id="historyNextBtm"><a href="#nogo"><spring:theme
													code="text.account.coupons.next" /> <span
												class="lookbook-only"></span></a></li>
									</c:if>

								</ul>
							</c:if> --%>
					
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
		$(".page-coupons .totalResults").hide();
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
		var x = $(".coupon-container li").length;
		console.log(x);
		if (x == 0) {
			$("#transactionHistory").css("top", "10px");
		}
		else if (0 < x && x < 4 ) {
			$("#transactionHistory").css("top", "230px");
		}
		else if (3 < x && x < 7 ) {
			$("#transactionHistory").css("top", "370px");
		}
		else if (6 < x && x < 10 ) {
			$("#transactionHistory").css("top", "515px");
		}
		else  {
			$("#transactionHistory").css("top", "640px");
		}
	});

	//voucher list 
	$("#voucherNext").click(function(){
		var pageNo = $(this).closest(".pagination").find("li.active a").text();
		if(pageNo != ""){
			pageNo = parseInt(pageNo);
		}else{
			pageNo = 1;
		}
		pageNo = pageNo+1;
		var totalPages = '${totalPagesCoupon}';
		if(totalPages!="" && pageNo <= totalPages)
			{
			window.location.href="?pageVoucher="+pageNo+"&pageFor=voucher";
			}
	});
	
	$("#voucherPrev").click(function(){
		var pageNo = $(this).closest(".pagination").find("li.active a").text();
		pageNo = parseInt(pageNo);
		pageNo = pageNo-1;
		var totalPages = '${totalPagesCoupon}';
		if(pageNo!=0 && totalPages!="" && pageNo <= totalPages)
			{
			window.location.href="?pageVoucher="+pageNo+"&pageFor=voucher";
			}
	});
	
	

	//hitory list 
	$("#historyNext,#historyNextBtm").click(function(){
		var pageNo = $(this).closest(".pagination").find("li.active a").text();
		if(pageNo != ""){
			pageNo = parseInt(pageNo);
		}else{
			pageNo = 1;
		}
		pageNo = pageNo+1;
		var totalPages = '${totalPagesCouponHist}';
		if(totalPages!="" && pageNo <= totalPages)
			{
			window.location.href="?pageHistory="+pageNo+"&pageFor=history#transactionHistory";
			}
	});
	
	$("#historyPrev,#historyPrevBtm").click(function(){
		var pageNo = $(this).closest(".pagination").find("li.active a").text();
		pageNo = parseInt(pageNo);
		pageNo = pageNo-1;
		var totalPages = '${totalPagesCouponHist}';
		if(pageNo!=0 && totalPages!="" && pageNo <= totalPages)
			{
			window.location.href="?pageHistory="+pageNo+"&pageFor=history#transactionHistory";
			}
	});
	
</script>
<c:if test="${param.pageHistory ne null or param.pageVoucher ne null}">
	<script>
		$(document).ready(function(){
			$("#couponHistory").click();	
		});
		
	</script>
</c:if>