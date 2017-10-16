<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- TPR-667 START -->
<!-- <script type='text/javascript'>(function(a,b,c,d){a='//tags.tiqcdn.com/utag/tataunistore/main/prod/utag.js';b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);})();</script>
<script type='text/javascript'>(function(a,b,c,d){a='//tags.tiqcdn.com/utag/tataunistore/main/dev/utag.js';b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);})();</script> -->
<!-- TPR-667 END -->

<!-- This is used for NewsLetter SignUp Section in footer -->
<div class="right">
	<div class="newsletter">
			<h3><spring:theme code="text.newsletter.signup"/></h3>
			<spring:theme code="text.newsletter.placeholder" text="Enter your email to Sign up for our newsletter" var="newsletterPlaceholder" />
			<input type="text" name="email"  id="mailtext"  placeholder="${newsletterPlaceholder}" />
			<button id ="submit" type="submit" onclick="return emailvalidate();"><spring:theme code="footer.submit"/></button>
			<!-- <div>&emsp;</div> --> <!-- commented as part of UF-249 -->
			<div id="error_message" style="display:block;font-size:12px;"></div>
	</div>
	
<!-- This is used for social media images in footer -->
	
	 <div class="social share">
		<h3><spring:theme code="text.stay.connected"/></h3>
		<div class="links">
			<%-- <c:forEach items="${component.footerImageList}" var="banner">
					<a href="${banner.urlLink}" ><span class="spriteImg"></span></a>
			</c:forEach> --%>
			<a href="https://plus.google.com/107413929814020009505" class="gplus" target="_blank"><span class="spriteImg"></span></a>
			<a href="https://www.facebook.com/TataCLiQ/" class="facebook" target="_blank"><span class="spriteImg"></span></a>
			<a href="https://twitter.com/tatacliq" class="twitter" target="_blank"><span class="spriteImg"></span></a>
			<a href="https://www.instagram.com/tatacliq/" class="insta" target="_blank"><span class="spriteImg"></span></a>
			<a href="https://www.youtube.com/channel/UCUwkaWqIcl9dYQccKkM0VRA" class="youtube" target="_blank"><span class="spriteImg"></span></a>
		</div>
	</div>
	


<!-- This is used for app download images in footer -->
	
	 <div class="social app-download">
		<h3><spring:theme code="text.download.app"/></h3>
		<div class="links">
			<c:forEach items="${footerAppImageList}" var="banner">
					<a href="${banner.urlLink}" class="appios" target="_blank"><span class="spriteImg"></span></a>
			</c:forEach>
		</div>
	</div>
</div>

<!-- This is used for navigation nodes in footer  -->

<ul>
	<c:forEach items="${navigationNodes}" var="node">
		<c:if test="${node.visible}">
			<li><c:forEach items="${node.links}"
					step="${wrapAfter}" varStatus="i">
					<c:if test="${wrapAfter > i.index}">
						<c:choose>
							<c:when test="${empty node.media}">
							 <h3 class="toggle">${node.title}</h3>
						    </c:when>    
						    <c:otherwise>
						        <h3 class="toggle"><img src="${node.media.url}" alt="${node.media.altText}" /></h3>
						    </c:otherwise>
						</c:choose>
						<!-- V <h3 class="toggle">${node.title}</h3> -->
					</c:if>
					<ul class="">
						<c:forEach items="${node.links}" var="childlink"
							begin="${i.index}" end="${i.index + wrapAfter - 1}">
							<cms:component component="${childlink}"
								evaluateRestriction="true" element="li" />
						</c:forEach>
					</ul>
				</c:forEach></li>
		</c:if>
	</c:forEach>
	<li class="footer-text-mobile">
		<h3 class="toggle">Know More</h3>
		<ul>
			<li>
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
			</li>
		</ul>
	</li>
</ul>
<c:if test="${fn:length(footerLinkList) gt 0}">
	<div id="footerLink">
		<c:set var="rowcount" value="-1"></c:set>
		<c:forEach items="${footerLinkList}" var="footerLinkRow">
			<div class="column">
				<ul>
					<c:forEach items="${footerLinkRow.value}" var="footerlinkColumnObj">
						<c:choose>
							<c:when test="${footerlinkColumnObj.key eq 0}">
								<li class="header"><a href="${footerlinkColumnObj.value.footerLinkURL}"><b>${footerlinkColumnObj.value.footerLinkName}</b></a></li>
							</c:when>
							<c:otherwise>
								<li class="node"><a href="${footerlinkColumnObj.value.footerLinkURL}">${footerlinkColumnObj.value.footerLinkName}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
			</div>
		</c:forEach>
	</div>
</c:if>

<!-- The script is used for validating email in news letter sign up Section -->
<script>
	function emailvalidate() {
		<!-- TPR - 667--->
		/*utag.link({
		"link_obj": this, "link_text": "newsletter_subscription" , "event_type" : "newsletter_subscription" 
	}); */
		//TISSTRT-1527
       if(typeof(utag) != "undefined"){
	       	utag.link({
		      "link_text": "newsletter_subscription" , "event_type" : "newsletter_subscription" 
	        }); 
     }
		
		if(typeof _satellite !="undefined"){
			   _satellite.track('news_letter_sign_up');
			 }
		<!-- TPR - 667 ends --->
		var mail = $("#mailtext").val();

		if (mail == "") {

			$("#error_message").css({"display":"block"});
            document.getElementById("error_message").innerHTML = "<font color='#ff1c47'>Please enter a valid email ID</font>";
			return false;

		} else {
			
			 var regex =/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
			 if(!regex.test(mail))
			 {

				$("#error_message").css({"display":"block"});
	            document.getElementById("error_message").innerHTML = "<font color='#ff1c47'>Please enter a valid email ID</font>";
	            return false;
			}

			$.ajax({
				url : ACC.config.encodedContextPath
						+ "/newsLetterSubscriptionEmail?email=" + mail,
				type : "GET",
				dataType : "JSON",
				success : function(data) {

					if (data == "fail") {
						
						$("#error_message").css({"display":"block"});
			            document.getElementById("error_message").innerHTML = "<font color='#ff1c47'>You are already subscribed to NewsLetter!</font>";
						return false;

					} else if (data == "success") {
						
						$("#error_message").css({"display":"block"});
			            document.getElementById("error_message").innerHTML = "<font color='#60a119'>Thanks for signing up. We'll keep you updated with our newsletters.</font>";//TISSTRT-1599 message changed
			          //TPR-667 START
			            /* utag.link({
							"link_obj": this, "link_text": "newsletter_subscription" , "event_type" : "newsletter_subscription" 
						}); */
			          	//TPR-667 END
						return true;
					}
					
					else if(data == "mailFormatError"){
						$("#error_message").css({"display":"block"});
			            document.getElementById("error_message").innerHTML = "<font color='#ff1c47'>Please enter a valid email ID</font>";
						return false;
					}
				},
				error : function(data) {
					alert("Oops something went wrong!!!");
				}
			});
		}

		//$("#success_emailid").css('display','block');
		//$("#blank_emailid").css('display', 'none') ;
		//$("#valid_emailid").css('display', 'none') ;
		//return true;
	}
</script>
