<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="luxregister" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/user"%>
<c:url value="/login/register" var="registerActionUrl" />
<luxregister:register actionNameKey="lux.register.submit"
		action="${registerActionUrl}"/>