<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:if test="${requestScope.captcaEnabledForCurrentStore}">
	
<script src="https://www.google.com/recaptcha/api.js"></script>
<div class="g-recaptcha" data-sitekey="${requestScope.recaptchaPublicKey}"></div> 

</c:if>
