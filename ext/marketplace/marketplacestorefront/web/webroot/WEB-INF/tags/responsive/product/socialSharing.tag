<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<script type="text/javascript" src="http://apis.google.com/js/plusone.js"></script>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<span id="sharepretext" style="display:none"><spring:theme code="share.pretext"/></span>
<span id="shareposttext" style="display:none"><spring:theme code="share.posttext"/></span>
<span id="productUrl" style="display:none">${request.contextPath}${product.url}</span>
<span id="googleClientid" style="display:none">${googleClientid}</span>
<span id="facebookAppid" style="display:none">${facebookAppid}</span>
<script>

			$(".g-interactivepost").attr("data-contenturl",window.location.host+$('#productUrl').text());
			$(".g-interactivepost").attr("data-calltoactionurl",window.location.host+$('#productUrl').text());
			//$(".wish-share .share a.tw").attr("href","https://twitter.com/intent/tweet?text=Wow! I found this amazing product - check it out here"+window.location+". Like or comment to tell me what you guys think. Hit share to spread the love. ");
			
			var popUpWidth=500;
			var popUpHeight=450;
				 var PopUpLeftPosition = screen.width/2 - popUpWidth/2;
				    var PopUpTopPosition= screen.height/2 - popUpHeight/2;
			function openPopup(url) {
				    window.open(url, 'popup_id','scrollbars,resizable,height='+popUpHeight+',width='+ popUpWidth +',left='+ PopUpLeftPosition +',top='+ PopUpTopPosition);
			      return false;
			    }
</script>


<div class="share">
<span><spring:theme code="text.share.friends"/></span>
	<ul>
		<li>
			<a class="tw" onclick="return openPopup('https://twitter.com/intent/tweet?text='+ $('#sharepretext').text() + ' ' +window.location.host+ $('#productUrl').text() + ' ' + $('#shareposttext').text())"></a>
		</li>
		<li>
			<a class="fb" onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location.host+ $('#productUrl').text() + '&amp;app_id=' + $('#facebookAppid').text() + '&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=http://www.facebook.com/')"></a> 
			<!-- <a class="fb" onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location + '&amp;app_id=145634995501895&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=https://developers.facebook.com/tools/explorer')"></a>  -->				
		</li>
		<li>
			<button class="g-interactivepost"
	        data-contenturl=""
	        data-clientid='${googleClientid}'
	        data-cookiepolicy="single_host_origin"
	        data-prefilltext="<spring:theme code="share.pretext"/><spring:theme code="share.posttext"/>"
	        data-calltoactionlabel="OPEN"
	        data-calltoactionurl="">
	        <a class="gp"></a>
	        </button>
			<!-- <a class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=https://www.dev.tataunistore.com&amp;clientid=888304528479-qdh1rp8r9o5fvh3dlabr7ebdbr02se6e.apps.googleusercontent.com&amp;prefilltext=helooo&amp;calltoactionurl=https://www.google.com')"></a>  -->
			<!-- <a href="" class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=' + window.location)"></a> -->
		</li>	
		<li><%-- <a class="mail" data-target="#popUpModal" data-url="${SHARED_PATH}"></a> --%>
			<a class="mail mailproduct" role="button" data-toggle="popover" data-placement="bottom"></a>
		</li>
	</ul>
</div>

<div id="emailLoggedInId" style="display: none"><spring:theme code="product.emailNonLoggedIn"/></div>
<div id="emailSend" class="emailSend">
	
				<div class="click2chat-container send-email-container" id="myModalLabel">
					<h1><spring:theme code="email.mail"/></h1>
					
					<form>
				
				
				<label for="forgotPassword_email"><spring:theme code="email.mailto"/></label>
				<input type="text" id="emailId" placeholder="abc@xyz.com,abc@xyz.com">
				<input type="hidden" id="pId" value="${product.code}"/>
				
				<!-- <button type="button" id="sendEmail" class="blue" onClick="sendmail()">Send Mail</button> -->
				<div class="actions">
				
				<button id="sendEmail"  onClick="return sendmail()" type="button">
				<spring:theme code="email.send"/>
				</button>
				<span id="email" style="display:none"></span>
				<span id="emailError" style="display:none;color:#ff1c47"><spring:theme code="email.emailError"/></span>
				<span id="emailSuccess" style="display:none;color:#00CBE9"><spring:theme code="email.emailSuccess"/></span>
				<span id="emailUnSuccess" style="display:none;color:#ff1c47"><spring:theme code="email.emailUnSuccess"/></span>
				<span id="emailEmpty" style="display:none;color:#ff1c47"><spring:theme code="email.emailEmpty"/></span>
				<span id="validateemail" style="display:none;color:#ff1c47"><spring:theme code="email.validate"/></span>

				</div>
				</form>
				</div>
	
</div>