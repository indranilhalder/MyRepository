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

<div class="tab-details return-dtls">
	<ycommerce:testId code="productRet_content_label">
		<ul>
			<li id="defaultRetLi"><spring:theme code="know.more.second"/>&nbsp;<span id="returnWindowRet"></span>&nbsp;<spring:theme code="know.more.third"/></li>
			<!-- Added for UF-98 -->
			<li id="defaultRetLi4" style="display:none"><spring:theme code="know.more.sixth"/>&nbsp;</li>
		</ul>
	</ycommerce:testId>
</div>
