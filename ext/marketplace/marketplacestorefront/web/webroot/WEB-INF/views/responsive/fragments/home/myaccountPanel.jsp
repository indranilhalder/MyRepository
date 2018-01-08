<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('myAcc.voucher.display.flag')" var="isVoucherToBeDisplayed"/>

<c:if test="${empty userName}">
	
							<li class="header-myAccount"><%-- <spring:theme
									code="header.flyout.myaccount" /> --%></li>				<!-- UF-249 -->
	
							<%-- <li><a href="<c:url value="/my-account/"/>"><spring:theme
										code="header.flyout.overview" /></a></li> --%> 	<!-- UF-249 -->
							
							<li><a href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
										code="header.flyout.marketplacepreferences" /></a></li>  <!-- UF-249 -->
	
							<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
										code="header.flyout.Personal" /></a></li>
	
							<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
										code="header.flyout.orders" /></a></li>
	
							<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
										code="header.flyout.cards" /></a></li>
	
							<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
										code="header.flyout.address" /></a></li>
	<!-- Release 2 changes -->
				<%-- 		<li><a href="<c:url value="/my-account/reviews"/>"><spring:theme
										code="header.flyout.review" /></a></li>  --%> <!-- Closed as per SPRINT 13 requirement -->   
										
						<%-- 	<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
										code="header.flyout.myInterest" /></a></li> --%> <!--  UF-249 link hide -->
							
							
							 <li class="header-SignInShare"><%-- <spring:theme
									code="header.flyout.credits" /> --%></li>				<!-- UF-249 -->

                         <c:if test="${isVoucherToBeDisplayed eq true }">
							<li><a href="<c:url value="/my-account/coupons"/>"><spring:theme
									code="header.flyout.coupons" /></a></li>  
                         </c:if>
							 
							<%-- <li class="header-SignInShare"><spring:theme
									code="header.flyout.share" /></li> --%>			<!-- UF-249 -->
	
							<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
										code="header.flyout.invite" /></a></li>
											<!-- For Infinite Analytics Start -->
												<li><div class="ia_cat_recent" id="ia_categories_recent"></div></li>
											<!-- For Infinite Analytics End -->
							<li><ycommerce:testId code="header_signOut">
									<u><a href="<c:url value='/logout'/>"  class="header-myAccountSignOut"> <spring:theme
											code="header.link.logout" />
									</a></u>
								</ycommerce:testId>
							</li>
</c:if>

<c:if
	test="${not empty userName && !fn:contains(userName, 'Anonymous')}">
	
							<li class="header-myAccount"><%-- <spring:theme
									code="header.flyout.myaccount" /> --%></li>				<!-- UF-249 -->
	
							<%-- <li><a href="<c:url value="/my-account/"/>"><spring:theme
										code="header.flyout.overview" /></a></li> --%>
							
							<li><a href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
										code="header.flyout.marketplacepreferences" /></a></li>   <!-- UF-249 -->
	
							<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
										code="header.flyout.Personal" /></a></li>
	
							<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
										code="header.flyout.orders" /></a></li>
	
							<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
										code="header.flyout.cards" /></a></li>
	
							<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
										code="header.flyout.address" /></a></li>
	
					<%-- 	<li><a href="<c:url value="/my-account/reviews"/>"><spring:theme
										code="header.flyout.review" /></a></li>  --%> <!-- commented as per sprint 13 requirement -->

										
							<%-- <li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
										code="header.flyout.myInterest" /></a></li> --%>	<!--  UF-249 link hide -->
							
											<li class="header-SignInShare"><%-- <spring:theme
									code="header.flyout.credits" /> --%></li>				<!-- UF-249 -->
                       <c:if test="${isVoucherToBeDisplayed eq true }">
						    <li><a href="<c:url value="/my-account/coupons"/>"><spring:theme
									code="header.flyout.coupons" /></a></li>
						</c:if>
									
							<%-- <li class="header-SignInShare"><spring:theme
									code="header.flyout.share" /></li> --%>		<!-- UF-249 -->
	
							<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
										code="header.flyout.invite" /></a></li>
										
							<li><ycommerce:testId code="header_signOut">
									<u><a href="<c:url value='/logout'/>"  class="header-myAccountSignOut"> <spring:theme
											code="header.link.logout" />
									</a></u>
								</ycommerce:testId>
							</li>
</c:if>