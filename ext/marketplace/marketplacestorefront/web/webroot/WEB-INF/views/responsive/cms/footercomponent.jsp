<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

 
<div class="footer-text">
	<%-- ${footerText} --%>
	<c:choose>
		<c:when test="${not empty categoryFooterTxt}">
			${categoryFooterTxt}
		</c:when>
		<c:otherwise>
		<c:if test="${!fn:containsIgnoreCase(cmsPage.name, 'Cart Page')}">
			${footerText}
			</c:if>
		</c:otherwise>
	</c:choose>

</div>
<div class="banner">
	<span>${notice}</span>
</div>
<div class="modal size-guide fade" id="popUpModal" style="z-index:1000000000;" tabindex="-1" role="modal" aria-labelledby="popUpModalLabel" aria-hidden="true">
	<div class="overlay" data-dismiss="modal"></div>
		<div class="modal-content content" style="width:90%; max-width:90%;">
			
		</div>
</div>
