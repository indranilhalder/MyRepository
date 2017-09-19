<!-- PDP changes start -->
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="tab-details">
	<%-- <ycommerce:testId code="productDetails_content_label"> --%>
	<ycommerce:testId code="productTataPromise_content_label">
		<ul>
		<li><spring:theme code="lux.know.more.first"/></li>
		<!-- 	TISCR-414 - Chairmans demo feedback 10thMay CR -->
		<c:if test="${productCategoryType ne 'FineJewellery' && productCategoryType ne 'FashionJewellery'}">
			<li id="defaultKnowMoreLi"><spring:theme code="lux.know.more.second"/>&nbsp;</li>
			<li id="defaultKnowMoreLi2"><spring:theme code="lux.know.more.third"/></li>
			
		</c:if>
		<li id="lingerieKnowMoreLi1" style="display:none"><spring:theme code="know.more.second.lingerie1"/></li>
		<li id="lingerieKnowMoreLi2" style="display:none"><spring:theme code="know.more.second.lingerie2"/></li>
		<!-- Added for UF-98 -->
		<li id="defaultKnowMoreLi4" style="display:none"><spring:theme code="know.more.sixth"/>&nbsp;</li>
		<li><spring:theme code="lux.know.more.fourth"/></li>
		
		<c:if test="${productCategoryType eq 'FineJewellery' || productCategoryType eq 'FashionJewellery'}">
			<c:forEach items="${Warranty}" var="warranty">
				<li>${warranty}</li>
			</c:forEach>
		</c:if>
	</ul>
	</ycommerce:testId>
</div>
<!-- PDP changes end -->
