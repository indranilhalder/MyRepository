<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer"
	tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<footer>
    <section>
      <section class="footer-top-content">
        <ul class="footer-top-child">
          <li><p>TATA TRUST</p></li>

          <li><p>CLIQ AND PIQ</p></li>

          <li><p>AUTHENTIC BRANDS</p></li>

          <li><p>EASY RETURNS</p></li>
        </ul>
      </section>
      <section class="footer-main-content">
        <section class="footer-child-last mobile-item">
            <p>#NEWSLETTER</p>
            <form method="get"
            	action="/newsLetterSubscriptionEmail"
			  	action-xhr="/newsLetterSubscriptionEmail"
			  target="_top">
            <div><input class="footer-last-input" placeholder="Your Email Id" name="email" />
            <input class="footer-last-btn" type="submit" value="SUBSCRIBE" /></div>
            <div submit-success>
			    <template type="amp-mustache">
			      Thanks for signing up. We'll keep you updated with our newsletters.
			    </template>
			  </div>
            </form>
            
            <!-- <p><input class="footer-last-input" placeholder="Your Email Id" on="input-debounced:AMP.setState({subscribeEmail: event.value, subscribeNewsletter: 'no-subscribe'})" />
            <button class="footer-last-btn" on="tap:AMP.setState({subscribeNewsletter: 'subscribe'})">SUBSCRIBE</button></p>
            <amp-list src="https://www.tatacliq.com/newsLetterSubscriptionEmail?email="
		      [src]="subscribeNewsletter == 'subscribe' ? 'https://www.tatacliq.com/newsLetterSubscriptionEmail?email='+subscribeEmail : 'https://www.tatacliq.com/newsLetterSubscriptionEmail?email='" height="20" layout="fixed-height">
				<template type="amp-mustache">
					<span>{{response == 'success' ? 'Thanks for signing up. We will keep you updated with our newsletters.' : 'None'}}</span>
				</template>
		    </amp-list> -->
		    
            <p>THE SOCIAL NETWORK</p>
            <p>
              <a href="https://plus.google.com/107413929814020009505"><i class="fa fa-google-plus"></i></a>
        			<a href="https://www.facebook.com/TataCLiQ/"><i class="fa fa-facebook"></i></a>
        			<a href="https://twitter.com/tatacliq"><i class="fa fa-twitter"></i></a>
        			<a href="https://www.instagram.com/tatacliq/"><i class="fa fa-instagram"></i></a>
        			<a href="https://www.youtube.com/channel/UCUwkaWqIcl9dYQccKkM0VRA"><i class="fa fa-youtube"></i></a>
        			
            </p>
            <p><spring:theme code="text.download.app" /></p>
            <p>
              <c:forEach items="${footerAppImageList}" var="banner">
					<a href="${banner.urlLink}" class="appios" target="_blank"><span class="spriteImg"></span></a>
			</c:forEach>
              <!-- <a href="#"><i class="fa fa-android"></i></a>
        	  <a href="#"><i class="fa fa-apple"></i></a> -->
            </p>
        </section>
        <section class="footer-child">
          <amp-accordion>
          
          <c:forEach items="${navigationNodes}" var="node">
        	<c:if test="${node.visible}">
          		<section>
          			<c:forEach items="${node.links}"
							step="${wrapAfter}" varStatus="i">
								<c:if test="${wrapAfter > i.index}">
									<c:choose>
										<c:when test="${empty node.media}">
											<h4><span class="tata-title">Tata</span> MARKETPLACE<i class="fa fa-angle-down"></i></h4>
											<!-- TEXT NOT SUITABLE -->
										</c:when>
										<c:otherwise>
											<h4>
												<amp-img src="${node.media.url}"layout="fixed-height" height="40" alt="${node.media.altText}"></amp-img>
											</h4>
											<!-- TEXT NOT SUITABLE -->
										</c:otherwise>
									</c:choose>
								</c:if>
								<ul>
									<c:forEach items="${node.links}" var="childlink"
										begin="${i.index}" end="${i.index + wrapAfter - 1}">
										<cms:component component="${childlink}" evaluateRestriction="true" element="li" />
									</c:forEach>
								</ul>
							
						</c:forEach>
	              	</section>
	              </c:if>
      			</c:forEach>
        			
        			<section>
	      					<h4>KNOW MORE<i class="fa fa-angle-down"></i></h4>
			              <div class="footer-section-content">
			               ${footerText}
			                </div>
			              </section>
      		
          </amp-accordion>
          <p class="footer-copyright"> ${notice}</p>
        </section>
      </section>
    </section>
  </footer>
