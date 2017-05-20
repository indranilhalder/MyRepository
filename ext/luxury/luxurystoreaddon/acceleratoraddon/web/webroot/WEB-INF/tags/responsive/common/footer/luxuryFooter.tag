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
						<p class="email-tit">We send awesome emails.</p>
						<p>Be first to know about new arrivals, curated collections, and exclusive sales.</p>
					</div>
					<div class="col-sm-6 signup-email">
						<input type="email" class="col-sm-6 email" placeholder="Enter your email address">
						<input type="text" class="col-sm-4 gender" placeholder="Gender">
						<button class="btn col-sm-2 text-left">Sign Up</button>

					</div>
				</div>
			</div>
		</div>
	
		<cms:pageSlot position="Footer" var="feature"
					element="div" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		<!-- <script type="text/javascript" src="/_ui/responsive/common/js/uicombined-min.js"></script> -->
	</footer>
</section>

