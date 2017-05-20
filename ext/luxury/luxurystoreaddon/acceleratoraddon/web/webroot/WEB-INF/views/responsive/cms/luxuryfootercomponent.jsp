<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="container footer-bottom">
		<div class="row footer-inner-wrapper">
			
			<c:forEach items="${component.navigationNodes}" var="node">
				<div class="col-sm-2 col-md-2 col-lg-2 accordion">
					<h3 class="">${node.title}<span class="sprite sp-minus"></span></h3>
					<ul class="accordion-content">
						<c:forEach items="${node.links}" var="childlink">
							<cms:component component="${childlink}"
								evaluateRestriction="true" element="li" />
						</c:forEach>
					</ul>
				</div>
			</c:forEach>
		
			<div class="col-sm-4 col-md-4 col-lg-4 app-info">
				<h3 class="">EXEPERIANCE LUXURY APP ON ONLINE<span class="sprite sp-minus"></span></h3>
				<div class="">
				<ul>
					<li><a href="javascript:;"><img src="../images/appstore.png" alt="Appstore"></a></li>
					<li><a href="javascript:;"><img src="../images/googleplay.png" alt="Googleplay"></a></li>
				</ul>
				<div class="get-in-touch">
					<p>(OR)</p>
					<p>Give a missed call to <span class="tel">1800-419-3500</span> and get links to download Luxury app</p>
					<h3>KEEP IN TOUCH</h3>
					<p class="soc-links">
						<a class="fb-link" href="javascript:;"></a>
						<a class="twtr-link" href="javascript:;"></a>
						<a class="utube-link" href="javascript:;"></a>
						<a class="instgrm-link" href="javascript:;"></a>
					</p>
				</div>
				</div>
			</div>
			<div class="col-sm-4 col-md-4 col-lg-4 about-product">
				<ul>
					<li><span>100% ORIGINAL </span>gaurantee for all products at luxury.com</li>
					<li><span>Return within 30days </span>of placing your order</li>
					<li><span>Get free delivery </span>for every order above Rs. 1,999</li>
				</ul>
			</div>
			
		</div>
	</div>
