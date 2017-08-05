<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ attribute name="checkoutSteps" required="true" type="java.util.List"%>
<%@ attribute name="progressBarId" required="true"
	type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ attribute name="isCart" required="false" type="java.lang.Boolean" %>	<!-- TPR-629 -->

<ycommerce:testId code="checkoutSteps">

<%-- <div class="payment progress-barcheck ${progressBarClass}  ${paymentPage}">
		 <div class="step-1"><a href="/checkout/multi/checkoutlogin/login" class="step-head js-checkout-step">Delivery Method<i class="arrow"></i></a><span class="paymentStepDone"></span></div>
				   <div class="step-2"><a href="/checkout/multi/delivery-method/choose" class="step-head js-checkout-step ">Delivery Address<i class="arrow"></i></a><span class="paymentStepDone"></span></div>
				  <div class="step-3"><a href="/checkout/multi/checkoutlogin/login" class="step-head js-checkout-step">Payment<i class="arrow"></i></a><span class="paymentStepDone"></span></div>
				</div> --%>
				
	<div class="top checkout-top">
		<div>
		<!-- <nav class="checkout-header login">
  			<div class="checkout-container">
    		<h1>Login / Sign Up</h1>
  		</div>
		</nav> -->
		<div class="container-address nav">
		  	   <c:set var="progressBarClass" value="${progressBarClass}" />
               <c:set var="paymentPage" value="${paymentPage}" />
			<div class="progress-barcheck  ${progressBarClass}  ${paymentPage}">
      <%-- <div class="progress-barg"><span class="step${checkoutStep.stepNumber}"></span></div> --%>
     <!--  <div class="step-1 finish"><a href="checkout-delivery.html">Sign In <i class="fa fa-caret-right fa-caret"></i></a><span></span></div>
      <div class="step-2 inprogress active"><a href="checkout-combination.html">Delivery&nbsp;<i class="fa fa-caret-right fa-caret"></i></a><span></span></div>
      <div class="step-3 finalstep "><a href="checkout-payment.html">Payment&nbsp;<i class="fa fa-caret-right fa-caret"></i></a><span></span></div> -->
            
		
				<div class="progress-barg">
				<c:forEach items="${checkoutSteps}" var="checkoutStep"
					varStatus="status">
					<c:if test="${checkoutStep.progressBarId eq progressBarId}">
					<c:set scope="page" var="activeCheckoutBarStepNumber" value="${checkoutStep.stepNumber}" />
					</c:if>
					</c:forEach>
					<span class="step"></span>
				</div> 
			
				<c:forEach items="${checkoutSteps}" var="checkoutStep"
					varStatus="status">
				
					<c:url value="${checkoutStep.url}" var="stepUrl" />
					<c:choose>
						<c:when test="${progressBarId eq checkoutStep.progressBarId}">
							<c:set scope="page" var="activeCheckoutStepNumber"
								value="${checkoutStep.stepNumber}" />
							<%-- <li class="sign-in active">
								<a href="${stepUrl}" class="step-head js-checkout-step active">
								<span>${checkoutStep.stepNumber}.</span>&nbsp;<spring:theme
									code="checkout.multi.${checkoutStep.progressBarId}" /> <!-- 	</a> -->

							</li> 
							 <div class="step-${checkoutStep.stepNumber}" ><a href="${stepUrl}" class="step-head js-checkout-step "><spring:theme
									code="checkout.multi.${checkoutStep.progressBarId}" /><i class="fa fa-caret-right fa-caret"></i></a><span class="paymentStepDone"></span></div>--%>
						</c:when>
						<c:when
							test="${checkoutStep.stepNumber > activeCheckoutStepNumber}">
							<%-- <li class="delivery">
									<a href="${stepUrl}" class="step-head js-checkout-step ">
								<span>${checkoutStep.stepNumber}.</span>&nbsp;<spring:theme
									code="checkout.multi.${checkoutStep.progressBarId}" /> <!-- 	</a> -->

							</li>
							 <div class="step-${checkoutStep.stepNumber}"><a href="${stepUrl}" class="step-head js-checkout-step"><spring:theme
									code="checkout.multi.${checkoutStep.progressBarId}" /><i class="fa fa-caret-right fa-caret"></i></a><span class="paymentStepDone"></span></div> --%>
						</c:when>
						<c:otherwise>
							<!--  TISCR-304 start -->
							<%-- <li class="payments step-done"><c:choose>


									<c:when test="${checkoutStep.stepNumber >1}">
									<c:if test="${isCart eq true}">		<!-- TPR-629 -->
										<a href="${stepUrl}" class="step-head js-checkout-step ">
											<span>${checkoutStep.stepNumber}.</span> <spring:theme
												code="checkout.multi.${checkoutStep.progressBarId}" />
										</a>
									</c:if>
									<c:if test="${isCart eq false}">
											<span>${checkoutStep.stepNumber}.</span> <spring:theme
												code="checkout.multi.${checkoutStep.progressBarId}" />
									</c:if>
									</c:when>
									<c:otherwise>
										<span>${checkoutStep.stepNumber}.</span>
										<spring:theme
											code="checkout.multi.${checkoutStep.progressBarId}" />
									</c:otherwise>

								</c:choose></li> 
								<div class="step-${checkoutStep.stepNumber}"><a href="${stepUrl}" class="step-head js-checkout-step"><spring:theme
									code="checkout.multi.${checkoutStep.progressBarId}" /><i class="fa fa-caret-right fa-caret"></i></a><span class="paymentStepDone"></span></div>--%>
									
									
							<!--  TISCR-304 end -->
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</div> 
			  </div>
		</div>
	</div>
	<c:forEach items="${checkoutSteps}" var="checkoutStep"
		varStatus="status">
		<c:url value="${checkoutStep.url}" var="stepUrl" />
		<c:choose>
			<c:when test="${progressBarId eq checkoutStep.progressBarId}">
				<c:set scope="page" var="activeCheckoutStepNumber"
					value="${checkoutStep.stepNumber}" />

				<div class="step-body"><jsp:doBody /></div>

			</c:when>
		</c:choose>
	</c:forEach>

</ycommerce:testId>




