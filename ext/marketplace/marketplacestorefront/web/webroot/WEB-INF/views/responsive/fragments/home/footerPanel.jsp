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
			<spring:theme code="text.newsletter.placeholder" text="Enter your email to Sign up for our newsletter" var="newsletterPlaceholder" />
			<input type="text" name="email"  id="mailtext"  placeholder="${newsletterPlaceholder}" />
			<button id ="submit" type="submit" onclick="return emailvalidate();"><spring:theme code="footer.submit"/></button>
			<div>&emsp;</div>
			<div id="error_message" style="display:block;font-size:12px;"></div>
	</div>
	
<!-- This is used for social media images in footer -->
	
	 <div class="social share">
		<h3><spring:theme code="text.stay.connected"/></h3>
		<div class="links">
			<%-- <c:forEach items="${component.footerImageList}" var="banner">
					<a href="${banner.urlLink}" ><span class="spriteImg"></span></a>
			</c:forEach> --%>
			<a href="https://plus.google.com/107413929814020009505" class="gplus"><span class="spriteImg"></span></a>
			<a href="https://www.facebook.com/TataCLiQ/" class="facebook"><span class="spriteImg"></span></a>
			<a href="https://twitter.com/tatacliq" class="twitter"><span class="spriteImg"></span></a>
			<a href="https://www.instagram.com/tatacliq/" class="insta"><span class="spriteImg"></span></a>
			<a href="https://www.youtube.com/channel/UCUwkaWqIcl9dYQccKkM0VRA" class="youtube"><span class="spriteImg"></span></a>
		</div>
	</div>
	


<!-- This is used for app download images in footer -->
	
	 <div class="social app-download">
		<h3><spring:theme code="text.download.app"/></h3>
		<div class="links">
			<%-- <c:forEach items="${footerAppImageList}" var="banner">
					<a href="${banner.urlLink}" class="appios"><span class="spriteImg"></span></a>
			</c:forEach> --%>
			<a href="/apps" class="appios"><span class="spriteImg"></span></a>
			<a href="/apps" class="appios"><span class="spriteImg"></span></a>
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
						<!-- <h3 class="toggle">${node.title}</h3> -->
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
</ul>

<!-- The script is used for validating email in news letter sign up Section -->
<script>
	function emailvalidate() {
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
			            document.getElementById("error_message").innerHTML = "<font color='#60a119'>Yay! We can't wait to be pen-pals with you.</font>";
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
<!-- This is used for displaying text in footer -->
<div class="footer-text">
	${footerText}
</div>
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

<!-- Added for Need help section -->


	<div id="up" onclick="openNeedHelpSec()">
		<spring:theme code="needhelp.needhelptext" />

		<div id="h">
			<span class="cclose-ico"></span>
			<div class="chat">
				<h5>
					<span class="cicon icon-comment"></span>
					<a href="${request.contextPath}/clickto/chat" id="chatMe"
					data-title="">
					<spring:theme code="needhelp.chatwithus" />
					<!-- &nbsp;<span class="bubble">1</span> -->
					</a>
				</h5>
					<!-- Commented as part of the chairman demo feedback -->
			<%-- 	<a href="${request.contextPath}/clickto/chat" id="chatMe"
					data-title="">
					<spring:theme code="needhelp.availablenow" />
				</a> --%>

			</div>
			<div class="call">
				<h5>
					<span class="cicon icon-earphone"></span>
					<a href="${request.contextPath}/clickto/call" id="callMe"
					data-title="">${contactNumber}
					<%-- <spring:theme code="needhelp.callus" /> --%>
					</a>
				</h5>
				
				<!--  Post chairman demo Changes -->
				<%-- <a href="${request.contextPath}/clickto/call" id="callMe">${contactNumber}</a> --%>
			</div>
		</div>
	</div>
	<%-- <div class="feedback-links">
		<a id="feedback" href="#nogo"><spring:theme code="text.feedback.survey"/></a>
		<input type="hidden" id="feedbackUrlPath" value="${feebBackSurveyUrl}" />
		</div> --%>
	<script>
		$(document).ready(function() {
			$('.glyphicon-remove').click(function() {
				helpMe();
			});
			/* $('#up').click(function() {
					$(this).removeClass("minimize");
					$("#h").toggle();
			}); */
			$(document).on("blur",".input-box input",function(){	
				if( $(this).val() != ""){
					$(this).addClass("used");
				}
				else {
					$(this).removeClass("used");
				}
			});
			$(document).on("blur",".input-box select",function(){	
				if( $(this).val() != ""){
					$(this).addClass("used");
				}
				else {
					$(this).removeClass("used");
				}
			});

		});
	</script>
