<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- This is used for NewsLetter SignUp Section in footer -->
<div class="right">
	<div class="newsletter">
			<h3><spring:theme code="text.newsletter.signup"/></h3>
			<input type="text" name="email"  id="mailtext"  placeholder="Enter your email" />
			<button id ="submit" type="submit" onclick="return emailvalidate();"><spring:theme code="footer.submit"/></button>
			<div>&emsp;</div>
			<div id="error_message" style="display:block;font-size:12px;"></div>
	</div>
	
<!-- This is used for social media images in footer -->
	
	 <div class="social">
		<h3><spring:theme code="text.stay.connected"/></h3>
		<div class="links">
			<c:forEach items="${component.footerImageList}" var="banner">
					<a href="${banner.urlLink}"><img src="${banner.media.URL}" ></a>
			</c:forEach>
		</div>
	</div>
</div>

<!-- This is used for navigation nodes in footer  -->

<ul>
	<c:forEach items="${navigationNodes}" var="node">
		<c:if test="${node.visible}">
			<li><c:forEach items="${node.links}"
					step="${component.wrapAfter}" varStatus="i">
					<c:if test="${component.wrapAfter > i.index}">
						<h3 class="toggle">${node.title}</h3>
					</c:if>
					<ul class="">
						<c:forEach items="${node.links}" var="childlink"
							begin="${i.index}" end="${i.index + component.wrapAfter - 1}">
							<cms:component component="${childlink}"
								evaluateRestriction="true" element="li" />
						</c:forEach>
					</ul>
				</c:forEach></li>
		</c:if>
	</c:forEach>
</ul>

<!-- The script is used for validating email in news letter sign up Section -->
<script>
	function emailvalidate() {
		var mail = $("#mailtext").val();

		if (mail == "") {

			$("#error_message").css({"display":"block"});
            document.getElementById("error_message").innerHTML = "<font color='red'><b>Please enter a valid email ID</b></font>";
			return false;

		} else {
			
			 var regex =/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
			 if(!regex.test(mail))
			 {

				$("#error_message").css({"display":"block"});
	            document.getElementById("error_message").innerHTML = "<font color='red'><b>Please enter a valid email ID</b></font>";
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
			            document.getElementById("error_message").innerHTML = "<font color='red'><b>You are already subscribed to NewsLetter!</b></font>";
						return false;

					} else if (data == "success") {
						
						$("#error_message").css({"display":"block"});
			            document.getElementById("error_message").innerHTML = "<font color='#00cbe9'><b>Yay! We can't wait to be pen-pals with you.</b></font>";
						return true;
					}
					
					else if(data == "mailFormatError"){
						$("#error_message").css({"display":"block"});
			            document.getElementById("error_message").innerHTML = "<font color='red'><b>Please enter a valid email ID</b></font>";
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

<!-- This is used for displaying copyright in footer -->
<div class="banner">
	<span>${notice}</span>
</div>


<div class="modal size-guide fade" id="popUpModal" style="z-index:1000000000;" tabindex="-1" role="modal" aria-labelledby="popUpModalLabel" aria-hidden="true">
	<div class="overlay" data-dismiss="modal"></div>
		<div class="modal-content content" style="width:90%; max-width:90%;">
			
		</div>
		<!-- /.modal-content -->
	
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->
