<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>
<%@ attribute name="showOnlySiteLogo" required="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>



<div>
<header:header/>
<header class="brand-header">
<div class="content">
<div class="bottom">

<div class="marketplace">
        <div class="mobile-bag bag">
           <a href="${cartUrl}"><span>(0)</span></a>
        </div>

      </div>
      
      <div class="brand-logo">
                  <cms:pageSlot position="MicroSiteLogo" var="component">
										<cms:component component="${component}" />
									</cms:pageSlot>
			</div>
			<nav>
					<ul>
						<c:if test="${empty showOnlySiteLogo }">
						
									<cms:pageSlot position="MicrositeNavigationBar" var="component">
										<cms:component component="${component}" />
									</cms:pageSlot>
								
							</c:if>
						

					</ul>
				</nav>

				<div class="search">
					<c:if test="${empty showOnlySiteLogo }">
						<cms:pageSlot position="MicrositeSearchBox" var="component">
							<cms:component component="${component}" />
						</cms:pageSlot>
					</c:if>
				</div>

				<!--   changes for Sticky Header in MyBag -->
				<div class="bag">
      				  <!-- <a href="/store/mpl/en/cart" class="mini-cart-link myBag-sticky"
					data-mini-cart-url="/store/mpl/en/cart/rollover/MiniCart"
					data-mini-cart-refresh-url="/store/mpl/en/cart/miniCart/SUBTOTAL"
					data-mini-cart-name="Cart" data-mini-cart-empty-name="Empty Cart"
					style="position: static;"> -->
					<a href="/cart" class="mini-cart-link myBag-sticky"
					data-mini-cart-url="/cart/rollover/MiniCart"
					data-mini-cart-refresh-url="/cart/miniCart/SUBTOTAL"
					data-mini-cart-name="Cart" data-mini-cart-empty-name="Empty Cart"
					style="position: static;"><spring:theme
							code="minicart.mybag" />&nbsp;(<span
					class="js-mini-cart-count-hover"></span>)
				</a>
    		    </div>
				</div>
		</div>						
	</header>							
						
</div>