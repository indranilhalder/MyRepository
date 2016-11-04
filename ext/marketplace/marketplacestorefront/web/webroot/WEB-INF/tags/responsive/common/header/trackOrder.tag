<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

 <div class="removeModalAfterLoad" id="showTrackOrder">
           <div class="modal-dialog">
               <div class="modal-content">
                <form class="form-horizontal" id="trackOrderForm">
                    <div class="modal-body">
                    	<button type="button" onclick="closeTrackOrder()" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    	<h4><spring:theme code="trackorder.track.text" text="Track Your Order"/></h4>                    	
                    	<div class="row">
                    		<div class="col-md-6 col-sm-6">
	                    	<div><label for="email"><spring:theme code="trackorder.track.email.text" text="Enter Your Email ID*"/></label>
   							</div>
	                    		<input type="text" class="form-control trackEmail" id="TrackEmailId" placeholder="Enter your email Id">
   								<label for="email"><spring:theme code="trackorder.track.order.text" text="Enter Your Order ID*"/></label>
   								<input type="text" class="form-control orderEmail" id="TrackOrderdId" placeholder="Enter your order Id">
                    		</div>
                    		<div class="col-md-6 col-sm-6">
                    			<label for="email"><spring:theme code="trackorder.track.provenotrobo.text" text="Prove you're not a robot*"/></label>
   								
   								<div id="recaptchaWidgetForTracking">
									<%--  <div class="g-recaptcha" data-sitekey="${recaptchaKey}"></div>  --%>
								</div>
								
                    		</div>
                    	</div>
                    	<div class="row">
                    		<div class="col-md-12 col-sm-12">
                    			<div class="trackPopupText"><spring:theme code="trackorder.track.check.order.text" text="To know your Order ID, Please check your order confirmation mail or sms."/></div>
                    		</div>
                    	</div>
                    	<div class="row">
                    		<div class="col-md-7 col-sm-12">
                    			
                    		</div>
                    		<div class="col-md-5">
                    			<button class="viewOrderButton" onclick="return viewOrderStatus(event)"><spring:theme code="trackorder.vieworder.button.text" text="View Order Status"/></button>
                    		</div>
                    	</div>
                    </div>
                     <p style="clear: both;"></p>
                     <div class="row">
                    		<div class="col-md-12 col-sm-12" style="padding-left: 15px;">
			                     <span class="error_text emailError"></span>
			                     <span class="error_text orderError"></span>
			                     <span class="error_text trackCaptchaError"></span>
			                     <span class="error_text main_error"></span>
			                </div>
			         </div>
                </form>
           </div>
       </div>
   </div>
	
<div class="wrapBG" style="background-color: rgba(0, 0, 0, 0.5); width: 100%; height: 600px; position: fixed; top: 0px; left: 0px; z-index: 99999; display: none;"></div>
	
