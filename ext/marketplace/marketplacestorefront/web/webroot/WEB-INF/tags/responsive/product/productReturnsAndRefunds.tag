<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<!-- PDP changes start -->
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<!-- Details of style notes tab -->
<div class="tab-details return-refnd-dtls">
<!-- /* CLASS return-refnd-dtls ADDED TO REDUCE WIDTH OF CONTAINER */ -->
	<%-- <ycommerce:testId code="productDetails_content_label"> --%>
	<ycommerce:testId code="productRetRef_content_label">
	<ul>
		<li id="defaultRetRefLi"><spring:theme code="know.more.second"/>&nbsp;<span id="returnWindowRefRet"></span>&nbsp;<spring:theme code="know.more.third"/></li>
		<!-- Added for UF-98 -->
		<li id="defaultRetRefLi4" style="display:none"><spring:theme code="know.more.sixth"/>&nbsp;</li>

		<c:if test="${productCategoryType eq 'FineJewellery'}">
			<c:if test="${not empty RetRefTab}">
				<c:forEach items="${RetRefTab}" var="retRefTab">
					<li>${retRefTab}</li>
				</c:forEach>
			</c:if>
		</c:if>
	</ul>
	</ycommerce:testId>
	
</div> 

<!-- PDP changes end -->

