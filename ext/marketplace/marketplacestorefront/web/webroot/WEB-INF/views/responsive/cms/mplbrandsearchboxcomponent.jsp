<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<c:url value="/**/c-${brandCode}" var="searchUrl" />
<div class="ui-front active ">
	<form id="search_form_brand" name="search_form_brand" method="get"
		action="${searchUrl}">
		<button id="brandSearchButton">Search</button>
		<spring:theme code="search.placeholder" var="searchPlaceholder" />
		<ycommerce:testId code="header_search_input">
			<input type="text" id="js-site-micrositesearch-input"
				class="form-control js-site-micrositesearch-input" name="q" value=""
				maxlength="100" placeholder="Search in ${categoryName}">
		</ycommerce:testId>
	</form>
</div>
