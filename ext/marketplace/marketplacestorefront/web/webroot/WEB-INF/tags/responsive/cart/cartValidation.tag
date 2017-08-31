<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>

<c:if test="${not empty validationData}">
	<c:set var="productLinkValidationTextDecoration" value="style=\"text-decoration: underline\""/>
	<c:forEach items="${validationData}" var="modification">
		<div class="alert neutral">
			
				<c:url value="${modification.entry.product.url}" var="entryUrl"/>
				<spring:theme code="basket.validation.${modification.statusCode}"
					arguments="${modification.entry.product.name}###${entryUrl}###${modification.quantity}###
							${modification.quantityAdded}###${productLinkValidationTextDecoration}" argumentSeparator="###"/><br>
			
		</div>
	</c:forEach>
</c:if>
<div class="global-alerts" id="exCartAlert" style="display:none">

				<div class="alert alert-warning alert-dismissable">
					<button class="close" aria-hidden="true" data-dismiss="alert" type="button">&times;</button>
					<span style="margin-left: 23px;">Exchange is Not Serviceable for some of the Products in your Cart </span> <!-- TISUATSE-84 -->
				</div>
			
		</div>