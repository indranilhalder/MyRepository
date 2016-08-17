<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ attribute name="checkoutSteps" required="true" type="java.util.List"%>
<%@ attribute name="progressBarId" required="true"
	type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ attribute name="isCart" required="false" type="java.lang.Boolean" %>

<ycommerce:testId code="checkoutSteps">
	<div class="top checkout-top">
		<div class="content">
			<ul class="nav">
				<c:forEach items="${checkoutSteps}" var="checkoutStep"
					varStatus="status">
					<c:url value="${checkoutStep.url}" var="stepUrl" />
					<c:choose>
						<c:when test="${progressBarId eq checkoutStep.progressBarId}">
							<c:set scope="page" var="activeCheckoutStepNumber"
								value="${checkoutStep.stepNumber}" />
							<li class="sign-in active">
								<%-- <a href="${stepUrl}" class="step-head js-checkout-step active"> --%>
								<span>${checkoutStep.stepNumber}.</span>&nbsp;<spring:theme
									code="checkout.multi.${checkoutStep.progressBarId}" /> <!-- 	</a> -->

							</li>
						</c:when>
						<c:when
							test="${checkoutStep.stepNumber > activeCheckoutStepNumber}">
							<li class="delivery">
								<%-- 	<a href="${stepUrl}" class="step-head js-checkout-step "> --%>
								<span>${checkoutStep.stepNumber}.</span>&nbsp;<spring:theme
									code="checkout.multi.${checkoutStep.progressBarId}" /> <!-- 	</a> -->

							</li>
						</c:when>
						<c:otherwise>
							<!--  TISCR-304 start -->
							<li class="payments step-done"><c:choose>


									<c:when test="${checkoutStep.stepNumber >1}">
									<c:if test="${isCart eq true}">
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
							<!--  TISCR-304 end -->
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
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




