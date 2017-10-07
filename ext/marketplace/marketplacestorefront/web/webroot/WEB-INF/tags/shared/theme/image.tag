<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="code" required="true" type="java.lang.String" %>
<%@ attribute name="alt" required="false" type="java.lang.String" %>
<%@ attribute name="title" required="false" type="java.lang.String" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="lazyLoad" required="false" type="java.lang.Boolean" %>

<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="staticHost"/>
<spring:theme code="${code}" text="/" var="imagePath"/>
<c:choose>
	<c:when test="${originalContextPath ne null}">
		<c:url value="${imagePath}" var="imageUrl" context="${originalContextPath}"/>
	</c:when>
	<c:otherwise>
		<c:url value="//${staticHost}${imagePath}" var="imageUrl" />
	</c:otherwise>
</c:choose>
<c:choose>
<c:when test="${lazyLoad eq false}">
<img class="picZoomer-pic" src="${imageUrl}" alt="${alt}" title="${title}" />
</c:when>
<c:otherwise>
<img class="picZoomer-pic lazy" data-original="${imageUrl}" src="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAASABIAAD/2wCEAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAf/CABEIAXYA/AMBEQACEQEDEQH/xAAZAAEAAwEBAAAAAAAAAAAAAAAAAwQIBwr/2gAIAQEAAAAA95AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAzVpVzR0tmrSoAAAKltElVLYAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD//EABQBAQAAAAAAAAAAAAAAAAAAAAD/2gAIAQIQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/xAAUAQEAAAAAAAAAAAAAAAAAAAAA/9oACAEDEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/8QAKRAAAQIFAwQBBQEAAAAAAAAAAwECBBESExQFISIQFRYxBgAHQ1GQQP/aAAgBAQABPwD+dnmHyzyC5lxeR3Cx2ipcWd+1g4lNuf4K5ZE+d27z6/cvWNW0rT4BummNCji4gw4qLAqsKy2NjggaVEVwlNUV9TKSKkPJHoytHfbTWNW1XT49upGNFDhIgI4WLOqvK+4N7jAcVURxVDSJ9T6iIkRJXqyhG9PMPlnkFzLi8juFjtFS4s79rBxKbc/wVyyJ87t3n/pwIHKzcKEzZSy8YOVKVMsi3dlTxlX629cep4cEUJwIkIogL9nhONhRPRFmiOGRrmOku+7V3/UkX6BDghRNBDBFDhZswIBsEJiKs1Roxtaxs132am/7mq9cCBys3ChM2UsvGDlSlTLIt3ZU8ZV+tvXH+Zv/xAAUEQEAAAAAAAAAAAAAAAAAAACg/9oACAECAQE/ADwf/8QAFBEBAAAAAAAAAAAAAAAAAAAAoP/aAAgBAwEBPwA8H//Z" alt="${alt}" title="${title}" />
</c:otherwise>
</c:choose>