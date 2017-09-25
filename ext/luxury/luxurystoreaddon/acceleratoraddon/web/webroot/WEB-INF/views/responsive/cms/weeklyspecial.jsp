<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<h3 class="section-title">${title}</h3>
<div class="brand-slider circle-pager">
	<c:forEach items="${weeklySpecialBanners}" var="component">
			<cms:component component="${component}" evaluateRestriction="true"
				navigationType="offcanvas" />
	</c:forEach>
</div>
		
	
	
