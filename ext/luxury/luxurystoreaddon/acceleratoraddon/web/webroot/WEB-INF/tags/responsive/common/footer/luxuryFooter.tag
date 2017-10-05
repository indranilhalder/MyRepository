<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section>
	<footer>
		
		<div class="container footer-top text-center">
			<cms:pageSlot position="WhyShopOnLuxury" var="feature"
					element="div" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			
		</div>
		
		<div class="container-fluid footer-middle">
			<div class="layout-fluid">
				<div class="row signup-email-wrapper">
					<div class="col-sm-6 send-awesome-email">
						<p class="email-tit">Sign up to our newsletters</p>
						<p>Be the first to know about new products, exclusive collections, latest trends, stories and more.</p>
					</div>
					<div class="col-sm-6 signup-email nopadding">
						<div class="col-sm-6 nopadding">
							<input type="email" name="email" id="emailId" class="email col-sm-12" placeholder="Enter your email address">
						</div>
						<div class="col-sm-3 nopadding">
							<select id="gender" class="gender">
								<option value="Male">Male</option>
								<option value="Female">Female</option>
							</select>
						</div>
						<div class="col-sm-3 nopadding">
							<button class="btn text-left" onclick="return newsletterSubscribe();">Sign Up</button>
						</div>
						<div id="error_message" style="display:block;font-size:12px;"></div>
					</div>
				</div>
			</div>
		</div>
	
		<cms:pageSlot position="Footer" var="feature"
					element="div" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
	</footer>
</section>
<script>
	function newsletterSubscribe() {
		console.log("please verify the email and subscribe");
		var mail = $("#emailId").val();
		var gender = $("#gender").val();
		console.log(mail);
		console.log(gender);
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
						+ "/newsLetterSubscriptionEmail?email=" + mail+"&gender="+gender+"&isLuxury="+true,
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
	}
</script>