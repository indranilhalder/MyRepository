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
            <p><input class="footer-last-input" placeholder="Your Email Id" /><button class="footer-last-btn">SUBSCRIBE</button></p>
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
              <a href="#"><i class="fa fa-android"></i></a>
        			<a href="#"><i class="fa fa-apple"></i></a>
            </p>
        </section>
        
        
        	
        	
        	
        
        <section class="footer-child">
          <amp-accordion>
          
          <c:forEach items="${navigationNodes}" var="node">
        	<c:if test="${node.visible}">
          		<c:forEach items="${node.links}"
							step="${wrapAfter}" varStatus="i">
							<section>
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
							</section>
						</c:forEach>
			              
			              </c:if>
        			</c:forEach>
        			
        			<section>
	      					<h4>KNOW MORE<i class="fa fa-angle-down"></i></h4>
			              <div class="footer-section-content">
			                <div>
			                  <p><b>Tata CLiQ: Online shopping in India at the most trusted destination</b></p>
			                  <p>Shop online whenever you want to - yes, that 24x7 experience is so convenient now, isn't it?! And you get genuine products delivered to your doorstep from your favourite brands with free shipping. Our clean, immersive design allows for easy navigation across categories and brand stores so you can find the best products from a wide range of smartphones, mobile phones, laptops, women’s clothing, men’s clothing, kids wear, shoes, watches and accessories online. You can also check our great offers or deal of the day section and get the best prices on various products across lifestyle, fashion, electronics, appliances & more.</p>
			                </div>
			                <div>
			                  <p><b>Online Shopping: Fast & convenient with the click of a button</b></p>
			                  <p>The upside of online shopping at Tata CLiQ is that you'll save on time and (money). It's as simple as comparing products and prices online before making the right buy. What’s more, you also have the option to pay for your favourite brands and products using our easy EMI options. Of course, you can buy and try - in the convenience of your home. Returns are easy too: We'll pick up your returns for free or you can also drop off the goods at the nearest brand store. The downside? There is no downside. Still have a question? email us at hello@tatacliq.com.</p>
			                </div>
			                <div>
			                  <p><b>Tata CLiQ Shopping App: Everything you want with just a few clicks on Android & iOS</b></p>
			                  <p>Shopping on the Tata CLiQ App is an absolute delight. Download the Tata CLiQ Android app from the Play Store or the iOS App from Apple App Store and get set to enjoy a range of benefits. Apart from the best deals, amazing offers and the latest styles online, the app also gives you the flexibility to shop at your convenience. Shop during a commute, in the middle of a holiday or at any time you want to. You can also use the easy share options to share your shopping with your friends and family to ensure you’re buying something that’s perfect. With constant updates and a host of new features being introduced constantly, you also get to enjoy a shopping experience that you’ll absolutely love. Download the Tata CLiQ App today and prepare to rediscover the way you shop.</p>
			                </div>
			                <div>
			                  <p><b>Popular Searches</b></p>
			                  <p>Samsung Mobile	| LYF Mobile	| Oppo Mobile	| Vivo Mobile	| Gionee Mobile	| Micromax Mobile	| Vivo Smartphone	| Samsung Smartphone	| Oppo Smartphone	| Apple iPhone | iPhone 8 | iPhone X | Infocus Mobile | Xolo Mobile | Intex Mobiles| Oppo F5 | Mi Max 2 | Mi Power Bank | Ambrane Power Bank
			                  LED TV	| Sony TV	| Samsung TV	| LG TV	| Samsung LED TV	| Sony LED TV	| Micromax TV	| Panasonic LED TV	| LG LED TV	| JBL Speakers	| JBL Headphones
			                  Dell Laptops	| HP Laptops	| Lenovo Laptops	| Acer Laptops	| Asus Laptop	| Apple Laptop	| Canon Camera	| Nikon Camera	| Sony Camera	| DSLR Camera	| Bluetooth Speakers | TP Link Router	| D-Link Router | Mi Router 3C
			                  LG Washing Machine	| IFB Washing Machine	| Samsung Washing Machine	| Whirlpool Washing Machine	| LG Refrigerator	| Samsung Refrigerator	| Whirlpool Refrigerator	| Godrej Refrigerator	| Philips Trimmer | Air Purifiers | Vacuum Cleaner | Printers | Philips | LG | Carrier AC | IFB
			                  Mufti Shirts | Jack & Jones | Pepe Jeans | Levi's India | Levis Jeans | Allen Solly | FabIndia | Spykar Jeans | W Kurtis | Vero Moda | Only | W For Women | Aurelia | Jockey Bras | Zivame Bras | Clovia | Amante | Aurelia Kurtis | Titan Watches | Fastrack Watches | Fossil Watches | Casio Watches | Sonata Watches | Guess Watches | Timex Watches | Tommy Hilfiger Watches | Maxima Watches | Bata Shoes | Mochi Shoes | Metro Shoes | Aldo Shoes | Catwalk | Crocs India | Lotto Shoes | Woodland Shoes | Red Tape Shoes | Clarks | Nike Shoes | Reebok Shoes | Ruosh Shoes | Adidas Shoes | Alberto Torresi | Puma Shoes | Caprese Bags | Lavie Bags | Fastrack Bags | Skybags | Lilliput | Nauti Nati | Saree | Dresses For Girls | Tops For Girls | Mens Watches | Salwar Suit | Ladies Watches | Wallet | Sports Shoes | Shoes For Girls | Jackets For Men | Shirts For Men | Jumpsuit | Ladies Dresses | Mens Casual Shoes | Sneakers | Panties | Sling Bag | Backpack | Skirt | Jackets For Women | Boys Shoes | Kurta For Men | Formal Shirts For Men | The Souled Store
			                  </p>
			                </div>
			                </div>
			              </section>
      		
          </amp-accordion>
          <p class="footer-copyright"> 2017 Tata CLiQ | All rights reserved</p>
        </section>
      </section>
    </section>
  </footer>
