<<<<<<< HEAD
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<cms:component component="${banner}" evaluateRestriction="true" />

=======
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<input type="hidden" id="bannersCount" value="${fn:length(banners)}" />

<c:forEach items="${banners}" var="banner" varStatus="status">
	<cms:component component="${banner}" evaluateRestriction="true" />
</c:forEach>

>>>>>>> BRANCH_TCS-HYCOMM-R1PS-BN-38
