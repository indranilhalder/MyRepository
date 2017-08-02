<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${empty buildNumber}">
<c:set var="buildNumber" value= "100000"/>
</c:if>
<c:choose>
	<c:when test="${fn:contains(pageBodyCssClasses, 'homepage')}">
	<noscript id="deferred-styles">
      <link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/tmpmain.min.css?v=${buildNumber}" />
</noscript>

<script>
      var loadDeferredStyles = function() {
        var addStylesNode = document.getElementById("deferred-styles");
        var replacement = document.createElement("div");
        replacement.innerHTML = addStylesNode.textContent;
        document.body.appendChild(replacement)
        addStylesNode.parentElement.removeChild(addStylesNode);
      };
      var raf = requestAnimationFrame || mozRequestAnimationFrame ||
          webkitRequestAnimationFrame || msRequestAnimationFrame;
      if (raf) raf(function() { window.setTimeout(loadDeferredStyles, 0); });
      else window.addEventListener('load', loadDeferredStyles);
</script>
	</c:when>
	<c:otherwise>
	 <link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/tmpmain.min.css?v=${buildNumber}" />
	</c:otherwise>
</c:choose>
<!-- UF-439 -->


<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/main-ie8-min.css"/>
<![endif]-->
<!--[if gte IE 9]>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/main-ie9-min.css"/>
<![endif]-->

