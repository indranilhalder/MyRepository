<%@ taglib prefix="footer"
	tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="header"
	tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!doctype html>
<html amp>
<header:ampheader />
<body on="tap:AMP.setState({visible: false})" role="menu" tabindex="0">
	<spring:eval
		expression="T(de.hybris.platform.util.Config).getParameter('amp.analytics.utility.host')"
		var="host" />
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('luxury.resource.host')" var="luxuryHost"/>
	<c:set var="base" value="https://${host}" />
	<!-- <amp-install-serviceworker src="/cliq-service-worker.js" layout="nodisplay"></amp-install-serviceworker> -->
	<div class="top-header">
		<a href="/">Marketplace</a> <a href="${luxuryHost}" target="_blank">Luxury</a>
	</div>
	<header>
		<button class="header-icon-1 mobile-item" on='tap:sidebar.open'>
			<i class="fa fa-navicon"></i>
		</button>
		<section class="col-xs-12 header-search-section">
			<section class="header-search-left">
				<section class="logo-container header-search-left-child">
					<a href="/"><amp-img class="logo-image" width="50" height="30"
						layout="flex-item"
						src="//assets.tatacliq.com/medias/sys_master/images/9906406817822.png"></amp-img></a>
				</section>
			</section>
			<section class="header-search-center desktop-item"
				[class]="visible ? 'header-search-center mobile-item' : 'header-search-center desktop-item'">
				<!-- <input class="header-search-input" autocomplete placeholder="Search in Marketplace" /> -->
				<section class="department-menu desktop-item">
					<span><span [text]="category">All</span> <i
						class="fa fa-angle-down"></i></span>
					<ul id="department_menu_list" class="department-menu-items">
						<li tabindex="1" role="button"
							on='tap:AMP.setState({category: "All", categoryId: "all"})'>All</li>
						<li tabindex="2" role="button"
							on='tap:AMP.setState({category: "Women&#39;s Clothing", categoryId: "MSH10"})'>Women's
							Clothing</li>
						<li tabindex="3" role="button"
							on='tap:AMP.setState({category: "Men&#39;s Clothing", categoryId: "MSH11"})'>Men's
							Clothing</li>
						<li tabindex="4" role="button"
							on='tap:AMP.setState({category: "Electronics", categoryId: "MSH12"})'>Electronics</li>
						<li tabindex="5" role="button"
							on='tap:AMP.setState({category: "Footwear", categoryId: "MSH13"})'>Footwear</li>
						<li tabindex="6" role="button"
							on='tap:AMP.setState({category: "Watches", categoryId: "MSH15"})'>Watches</li>
						<li tabindex="7" role="button"
							on='tap:AMP.setState({category: "Accessories", categoryId: "MSH16"})'>Accessories</li>
					</ul>
				</section>
				<input name="search" id="search_autocomplete" type="text"
					placeholder="Search in Marketplace" class="header-search-input"
					on="
            input-debounced:
              AMP.setState({
                term: event.value,
                showDropdown: event.value,
								categoryId: categoryId == null ? 'all' : categoryId
              }),
              autosuggest-list.show;
            tap:
              AMP.setState({
                term: term == null ? '' : term,
								category: category == null ? 'All' : category,
                showDropdown: 'true'
              }),
              autosuggest-list.show"
					[value]="term || ''" value="" required autocomplete="off" />
				<button class="header-search-btn">
		          <a href="/search/?searchCategory=all&text=" [href]="'/search/?searchCategory=all&text='+term"><i class="fa fa-search"></i></a>
		        </button>
				<div class="suggest">
					<div class="autosuggest-container hidden"
						[class]="(showDropdown && term) ?
							'autosuggest-container' :
							'autosuggest-container hidden'">
						<amp-list class="autosuggest-box" layout="fixed-height"
							height="140"
							src="/pwamp/autocomplete/MplEnhancedSearchBox?term="
							[src]="term.length>2 ?
								autosuggest.endpoint + term + '&category=' + categoryId :
								autosuggest.emptyAndInitialTemplateJson"
							id="autosuggest-list"> <template type="amp-mustache">
						<amp-selector id="autosuggest-selector"
							keyboard-select-mode="focus" layout="container"
							on="
										select:
											AMP.setState({
												term: event.targetOption,
												showDropdown: false
											}),
											autosuggest-list.hide">
						<strong>{{searchTerm}}</strong> {{#brands}}
						<div class="select-option no-outline" role="option" tabindex="0"
							on="tap:autosuggest-list.hide" option="{{name}}">
							<a
								href="/search/page-1?q={{searchTerm}}%3Arelevance%3Abrand%3A{{code}}%3AisLuxuryProduct%3Afalse&text={{searchTerm}}&searchCategory=all">in
								{{name}}</a>
						</div>
						{{/brands}} {{^brands}} {{/brands}} <strong>{{searchTerm}}</strong>
						{{#categories}}
						<div class="select-option no-outline" role="option" tabindex="0"
							on="tap:autosuggest-list.hide" option="{{name}}">
							<a
								href="/search/page-1?q={{searchTerm}}%3Arelevance%3Acategory%3A{{code}}%3AisLuxuryProduct%3Afalse&text={{searchTerm}}&searchCategory=all">in
								{{name}}</a>
						</div>
						{{/categories}} {{^categories}} {{/categories}} </amp-selector> </template> </amp-list>
					</div>
				</div>
				<amp-state id="autosuggest"> <script
					type="application/json">
						{
							"category": "",
							"endpoint": "/pwamp/autocomplete/MplEnhancedSearchBox?term=",
							"emptyAndInitialTemplateJson": {"items": [{
								"suggestions": [],
								"products": [],
                				"categories": [],
                				"searchTerm": "",
                				"brands": []
							}]},
              				"searchInBrand": "/search/page-1?q="
						}
					</script> </amp-state>
			</section>
		</section>
		<p class="header-icon-2 mobile-item">
			<a href="/cart"><span class="responsive-bag">0</span></a>
		</p>
		<p class="header-icon-3 mobile-item">
			<i tabindex="1" role="main"
				on="tap:AMP.setState({visible: !visible}), search_autocomplete.focus"
				class="">&nbsp;</i>
		</p>
	</header>

	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.homepage.amp.menu.radio')" var="isMenuRadio"/>
<c:choose>
<c:when test="${isMenuRadio}"><c:set var="leftMenuStyle" value="radio"></c:set></c:when>
<c:otherwise><c:set var="leftMenuStyle" value="checkbox"></c:set></c:otherwise>
</c:choose>
	
	<amp-sidebar id="sidebar" class="" [class]="pinCodeVisible ? 'sidebar-zindex' : ''" on="sidebarOpen:AMP.setState({showCloseBtn : true}),closeLeftMenu.show;sidebarClose:AMP.setState({showCloseBtn : false})" layout="nodisplay" side="left">
	<ul class="left-accordion-menu">
		<li>
			<input type="${leftMenuStyle}" name="l1-group" id="l1-department" />
        	<label for="l1-department"><span>Department</span><i class="fa fa-angle-right"></i></label>
        	<ul class="l1-menu-section">
        		<c:forEach items="${component.components}" var="component" varStatus="i">	
        			<li>
        				<input type="${leftMenuStyle}" name="l2-dept-group" id="l2-dept-${component.navigationNode.uid}" />
            			<label for="l2-dept-${component.navigationNode.uid}"><span>${component.navigationNode.title}</span><i class="fa fa-angle-right"></i></label>
        				<c:if test="${not empty component.navigationNode.children}">
	        				<ul class="l2-menu-section">
	        					<c:forEach items="${component.navigationNode.children}" var="child1">
	        						<c:forEach items="${child1.children}" var="child">
	        							<c:if test="${child.visible}">
	        								<li>
								                <input type="${leftMenuStyle}" name="l3-dept-group" id="l3-dept-${child.uid}" />
								                <label for="l3-dept-${child.uid}"><span>${child.title}</span><i class="fa fa-angle-right"></i></label>
								                <ul class="l3-menu-section">
													<c:forEach items="${child.links}" step="${component.wrapAfter}" var="childlink" varStatus="i">
														<c:forEach items="${child.links}" var="childlink" begin="${i.index+1}" end="${i.index + component.wrapAfter - 1}">
															<li><a href="${childlink.url}">${childlink.linkName}</a></li>
														</c:forEach>
													</c:forEach>
												</ul>
								              </li>
	        							</c:if>
	        						</c:forEach>
	        					</c:forEach>
	        				</ul>
        				</c:if>
        			</li>
        		</c:forEach>
        	</ul>
		</li>
		<li>
	        <input type="${leftMenuStyle}" name="l1-group" id="l1-brand" />
	        <label for="l1-brand"><span>Brand</span><i class="fa fa-angle-right"></i></label>
        	<ul class="l1-menu-section">
        		<c:forEach items="${shopByBrandDataList}" var="shopByBrands">
        			<c:choose>
						<c:when test="${shopByBrands.masterBrandName eq 'A-Z Brands'}">
							<li>
					            <input type="${leftMenuStyle}" name="l2-brand-group" id="l2-brand-AtoZ" />
					            <label for="l2-brand-AtoZ"><span>A-Z List</span><i class="fa fa-angle-right"></i></label>
					            <ul class="l2-menu-section">
					              <li>
					              	<div class="a2z-section">
									<amp-selector role="tablist" layout="container"
										class="a2zTabContainer">
									<c:forEach items="${groupedAlphabets}" var="entry">	
									<div role="tab" class="a2zTabButton" selected option="${entry.key}">${entry.key}-${entry.value}</div>
									</c:forEach>
									<c:forEach items="${AToEBrands}" var="entry" varStatus="i">	
									<div role="tab" class="a2zTabButton" selected option="A-E">A-E</div>
									<div role="tabpanel" class="a2zTabContent" data="${i.index}">
										<div>
											<ul class="a-z-ul">
												<h3>${entry.key}</h3>
												<c:set var="values" value="${entry.value}" />
												<c:forEach items="${values}" var="item">
												<c:set var="catName" value="${fn:split(item.name, '||')}" />
												<c:url var="brandlistUrl" value="/${catName[1]}/c-${fn:toLowerCase(item.code)}"></c:url>
												<li class="a-z-li"><a href="${brandlistUrl}">${catName[0]}</a></li>
												</c:forEach>
											</ul>
										</div>
									</div>
									</c:forEach>
									<c:forEach items="${FToJBrands}" var="entry" varStatus="i">	
									<div role="tab" class="a2zTabButton" selected option="F-J">F-J</div>
									<div role="tabpanel" class="a2zTabContent" data="${i.index}">
										<div>
											<ul class="a-z-ul">
												<h3>${entry.key}</h3>
												<c:set var="values" value="${entry.value}" />
												<c:forEach items="${values}" var="item">
												<c:set var="catName" value="${fn:split(item.name, '||')}" />
												<c:url var="brandlistUrl" value="/${catName[1]}/c-${fn:toLowerCase(item.code)}"></c:url>
												<li class="a-z-li"><a href="${brandlistUrl}">${catName[0]}</a></li>
												</c:forEach>
											</ul>
										</div>
									</div>
									</c:forEach>
									<c:forEach items="${KToOBrands}" var="entry" varStatus="i">	
									<div role="tab" class="a2zTabButton" selected option="K-O">K-O</div>
									<div role="tabpanel" class="a2zTabContent" data="${i.index}">
										<div>
											<ul class="a-z-ul">
												<h3>${entry.key}</h3>
												<c:set var="values" value="${entry.value}" />
												<c:forEach items="${values}" var="item">
												<c:set var="catName" value="${fn:split(item.name, '||')}" />
												<c:url var="brandlistUrl" value="/${catName[1]}/c-${fn:toLowerCase(item.code)}"></c:url>
												<li class="a-z-li"><a href="${brandlistUrl}">${catName[0]}</a></li>
												</c:forEach>
											</ul>
										</div>
									</div>
									</c:forEach>
									<c:forEach items="${PToTBrands}" var="entry" varStatus="i">	
									<div role="tab" class="a2zTabButton" selected option="P-T">P-T</div>
									<div role="tabpanel" class="a2zTabContent" data="${i.index}">
										<div>
											<ul class="a-z-ul">
												<h3>${entry.key}</h3>
												<c:set var="values" value="${entry.value}" />
												<c:forEach items="${values}" var="item">
												<c:set var="catName" value="${fn:split(item.name, '||')}" />
												<c:url var="brandlistUrl" value="/${catName[1]}/c-${fn:toLowerCase(item.code)}"></c:url>
												<li class="a-z-li"><a href="${brandlistUrl}">${catName[0]}</a></li>
												</c:forEach>
											</ul>
										</div>
									</div>
									</c:forEach>
									<c:forEach items="${UToZBrands}" var="entry" varStatus="i">	
									<div role="tab" class="a2zTabButton" selected option="U-Z">U-Z</div>
									<div role="tabpanel" class="a2zTabContent" data="${i.index}">
										<div>
											<ul class="a-z-ul">
												<h3>${entry.key}</h3>
												<c:set var="values" value="${entry.value}" />
												<c:forEach items="${values}" var="item">
												<c:set var="catName" value="${fn:split(item.name, '||')}" />
												<c:url var="brandlistUrl" value="/${catName[1]}/c-${fn:toLowerCase(item.code)}"></c:url>
												<li class="a-z-li"><a href="${brandlistUrl}">${catName[0]}</a></li>
												</c:forEach>
											</ul>
										</div>
									</div>
									</c:forEach>
									</amp-selector>
								</div>
								
					              </li>
					            </ul>
					          </li>
						</c:when>
						<c:otherwise>
						
							<li>
					            <input type="${leftMenuStyle}" name="l2-brand-group" id="l2-brand-Electronics" />
					            <label for="l2-brand-Electronics"><span>${shopByBrands.masterBrandName}</span><i class="fa fa-angle-right"></i></label>
					            <ul class="l2-menu-section">
									<c:forEach items="${shopByBrands.subBrandList}"
										var="subShopByBrand">
										<li><a href="${subShopByBrand.subBrandUrl}">${subShopByBrand.subBrandName}</a></li>
									</c:forEach>
								</ul>
					          </li>
						</c:otherwise>
					</c:choose>
        		</c:forEach>
        	</ul>
        </li>
	</ul>
	
	<c:if test="${empty hideHeaderLinks}">
			<c:if test="${uiExperienceOverride}">
				<li class="backToMobileLink"><c:url
						value="/_s/ui-experience?level=" var="backToMobileStoreUrl" /> <a
					href="${backToMobileStoreUrl}"> <spring:theme
							code="text.backToMobileStore" />
				</a></li>
			</c:if>

			<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
			
				<amp-accordion class="sidebar-menu l1-accordian user-information" disable-session-states>
					<section>
						<h4 class="l1-options">
							<c:set var="userName" value="${fname}"/>
							<a href="<c:url value="/my-account"/>" class="fa fa-user l1-my-account"> <spring:theme code="header.hi" arguments="${userName}" htmlEscape="true" />!</a>
						<i class="fa fa-angle-right"></i></h4>
						<div>
							<c:if test="${not empty userName && !fn:contains(userName, 'Anonymous')}">
		        				<ul>
			        				<li class="header-myAccount"></li>
									<li><a href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
												code="header.flyout.marketplacepreferences" /></a></li>   <!-- UF-249 -->
									<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
												code="header.flyout.Personal" /></a></li>
									<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
												code="header.flyout.orders" /></a></li>
									<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
												code="header.flyout.cards" /></a></li>
									<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
												code="header.flyout.address" /></a></li>
	                       <c:if test="${isVoucherToBeDisplayed eq true }">
							    <li><a href="<c:url value="/my-account/coupons"/>"><spring:theme
										code="header.flyout.coupons" /></a></li>
							</c:if>
								<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
											code="header.flyout.invite" /></a></li>
											
								<li><ycommerce:testId code="header_signOut">
										<u><a href="<c:url value='/logout'/>"  class="header-myAccountSignOut"> <spring:theme
												code="header.link.logout" />
										</a></u>
									</ycommerce:testId>
								</li>
								</ul>
						</c:if>
						</div>
					</section>
				</amp-accordion>
			</sec:authorize>

			<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
					<p class="sidebar-divider-item">
					<a href="<c:url value="/login"/>"><i class="fa fa-user"></i>Sign In/Sign Up</a>
					</p>
			</sec:authorize>
		</c:if>
	
	<p class="sidebar-divider-item">
		<a href="/my-account/wishList"><i class="fa fa-heart"></i>My Wishlists</a>
	</p>
	<p class="sidebar-divider-item">
		<a href="<c:url value="/my-account/orders"/>"><spring:theme code="header.trackorder" /></a>
	</p>
	<p class="sidebar-divider-item">
		<a href="${request.contextPath}/apps"><i class="fa fa-mobile-phone"></i>Download App</a>
	</p>
	<p class="sidebar-divider-item" role="popup" tabindex="0" on="tap:AMP.setState({pinCodeVisible: true, submitPincode: false})">Enter Your Pincode</p>
	</amp-sidebar>

	<button id="closeLeftMenu" on="tap:sidebar.close, closeLeftMenu.hide" class="close-menubar hidden" [class]="showCloseBtn ? 'close-menubar display-visible' : 'close-menubar hidden'">&times;</button>
	<section class="pincode-check hidden" [class]="pinCodeVisible ? 'pincode-check display-visible' : 'pincode-check hidden'">
    <section>
      <!--<form method="GET" action-xhr="/homePincode" target="_top">
        <input class="pincode_text" id="home_pincode" name="pin" placeholder="Pincode" maxlength="6" />
        <input type="submit" value="SUBMIT" class="pincode_submit">
      </form> -->
      <h3>Enter Pincode <span role="popup-close" tabindex="2" on="tap:AMP.setState({pinCodeVisible: false})">&times;</span></h3>
      <input class="pincode_text" id="home_pincode" on="input-debounced:AMP.setState({homePincode: event.value})" placeholder="Pincode" maxlength="6" />
      <button class="pincode_submit" on="tap:AMP.setState({submitPincode: true, pinCodeVisible: false})">Submit</button>
    </section>
    <amp-list src="/homePincode="
      [src]="submitPincode ? '/homePincode='+homePincode : ''"
      height="10" layout="fixed-height">

    </amp-list>
  </section>
  <section class="background-layer hidden" tabindex="0" role="popup-close"
    [class]="pinCodeVisible ? 'background-layer display-visible' : 'background-layer hidden'"
      on="tap:AMP.setState({pinCodeVisible: false})">
  </section>

	<div class="sliders main-content">
		<amp-list src="/pwamp/getHomePageBanners?version=Online" height="370"
			layout="fixed-height"> <template type="amp-mustache">
		<amp-carousel class="slider" width="400" height="400"
			layout="responsive" type="slides" controls autoplay loop delay="3000">
		{{#moblileBanners}}
		<a href="{{href}}?icid={{pk}}"><div>
			<amp-img class="responsive-img" src="{{url}}" layout="fill"></amp-img>
		</div></a>
		{{/moblileBanners}} </amp-carousel> </template> </amp-list>
	</div>

	<!-- <ul>
		<amp-iframe width="500"
		  title="Netflix House of Cards branding: The Stack"
		  height="281"
		  layout="responsive"
		  sandbox="allow-scripts allow-same-origin allow-popups"
		  allowfullscreen
		  frameborder="0"
		  src="http://localhost:8887/pages/shopbydepartment.html">
		</amp-iframe>
	</ul> -->
	
	<!-- Top Deals -->
	<amp-list src="/pwamp/getBestPicks?version=Online" height="340" layout="fixed-height"> 
	<template type="amp-mustache">
	<div id="topDealsComp">
		<div class="topDealsTopSection">
			<h2 class="homeViewHeading">
				{{title}} <small class="homeViewAllBtn">
				<a href="{{buttonLink}}">{{buttonText}}</a></small>
			</h2>
		</div>
		<div>
			<div id="topDealsCompCarousel">
				
				
				<amp-carousel height="340" layout="fixed-height" type="carousel">
				{{#subItems}}
				<div class="topDealsItem">
					<div class="topDealsItemImg">
						<a href="{{url}}"><amp-img class="responsive-img"
								layout="fill" src="{{imageUrl}}" alt="Brand Image"></amp-img></a>
					</div>
				</div>
				{{/subItems}} 
				</amp-carousel> 
				
				
			</div>
		</div>
<!-- 		<div class="brandStudioBottom">
			<amp-list src="/json/bestpicks.json" height="40"
				layout="fixed-height"> <template type="amp-mustache">
			<a href="{{buttonLink}}"><button class="brandStudioViewAllBtn">{{buttonText}}</button></a>
			</template> </amp-list>
		</div> -->
	</div>
	</template> 
	</amp-list>

	<!-- Brands You Love -->
	<div id="brandsYouLoveMobileComp" class="">
		<amp-list src="/pwamp/getBrandsYouLove?version=Online" height="320"
					layout="fixed-height"> <template type="amp-mustache">
		<div class="brandStudioTop">
			<h2 class="homeViewHeading">{{title}}</h2>
		</div>
		<div>
			<div id="brandsYouLoveMobileCompCarousel">
				
				<amp-carousel height="320" layout="fixed-height" type="carousel">
				{{#subComponents}}
				<div class="brandsCarouselItem">
					<div class="brandStudioImg">
						<a href="{{bannerUrl}}"><amp-img class="responsive-img"
								layout="fill" src="{{bannerImageUrl}}"
								alt="{{brandLogoAltText}}"></amp-img></a>
					</div>
					<div class="brandStudioDesc">
						<p class="brandStudioDescHeading">{{bannerText}}</p>
						<p class="brandStudioDescInfo">{{text}}</p>
						<p class="brandStudioVisitStore">
							<a href="{{bannerUrl}}">Visit Store</a>
						</p>
					</div>
				</div>
				{{/subComponents}} </amp-carousel>
			</div>
		</div>
		 </template> 
		</amp-list>
		<!-- <div class="brandStudioBottom">
			<button class="brandStudioViewAllBtn">View All Brands</button>
		</div> -->
	</div>

	<!-- What To Buy Now -->
	<amp-list src="/pwamp/getProductsYouCare?version=Online" height="340" layout="fixed-height"> 
	<template type="amp-mustache"> 
	<div id="whatToBuyComp">
		<div class="whatToBuyTopSection">
			<h2 class="homeViewHeading">{{title}}</h2>
		</div>
		<div>
			<div id="whatToBuyCompCarousel">
				<amp-carousel height="340" layout="fixed-height" type="carousel">
				{{#categories}}
				<div class="whatToBuyItem">
					<div class="whatToBuyItemImg">
						<a href="{{imageURL}}"><amp-img class="responsive-img"
								layout="fill" src="{{mediaURL}}" alt="{{imageName}}"></amp-img></a>
					</div>
				</div>
				{{/categories}} 
				</amp-carousel> 
			</div>
		</div>
	</div>
	</template> 
	</amp-list>
	<br />

	<div class="compContainer">
		<!-- Stay Qued -->
		<div id="stayQuedComp">
			<div class="stayQuedTopSection">
				<h2 class="homeViewHeading">Stay Qued</h2>
			</div>
			<amp-list src="/pwamp/getStayQuedHomepage?version=Online"
				height="240" layout="fixed-height"> 
				<template type="amp-mustache"> 
			<div class="stayQuedCenter">
				<div class="stayTwo">
					<div>
						<amp-img class="responsive-img" width="auto" height="200"
							layout="flex-item" src="{{bannerImage}}" alt="{{bannerAltText}}"></amp-img>
					</div>
				</div>
				<div class="stayOne">
					<div>
						<p class="h2 stayQuedHeading">{{promoText1}}</p>
						<p>{{promoText2}}</p>
						<div class="stayQuedBottom">
							<a href="{{bannerUrlLink}}"><button
									class="stayQuedViewAllBtn">Read The Story</button></a>
						</div>
					</div>
				</div>
			</div>
			</template> 
			</amp-list>
		</div>
		
		<!-- New In -->
		<div id="newInComp">
			<amp-list src="/pwamp/getNewAndExclusive?version=Online" height="320" layout="fixed-height"> 
		<template type="amp-mustache">
		<div class="newInTopSection">
				<h2 class="homeViewHeading">
					{{title}} <small class="homeViewAllBtn"><a href="/search/viewOnlineProducts">View
							All</a></small>
				</h2>
			</div>
			<div>
				<div id="newInCompCarousel">
					 
						<amp-carousel height="320"
						layout="fixed-height" type="carousel">
						{{#newAndExclusiveProducts}} <a href="{{productUrl}}"><div
							class="newInItem">
							<div class="newInItemImg">
								<amp-img width="120" height="180" layout="responsive"
									src="{{productImageUrl}}" alt="Brand Image"></amp-img>
							</div>
							<div class="newInDesc">
								<p class="newInDescName">{{productTitle}}</p>
								{{#productPrice}}
								<p class="newInDescPrice">
									{{dispPrice}}<span class="price-right">{{strikePrice}}</span>
								</p>
								{{/productPrice}}
							</div>
						</div></a> {{/newAndExclusiveProducts}} </amp-carousel> 
				</div>
			</div>
			<div class="brandStudioBottom">
				<button class="brandStudioViewAllBtn">View All</button>
			</div>
		</template> 
	</amp-list>
		</div>
	</div>


	<!-- Inspire Me -->
	<div id="inspireMeMobileComp">
		<amp-list src="/pwamp/getCollectionShowcase?version=Online" height="240" layout="fixed-height"> 
		<template type="amp-mustache"> 
		<div class="inspireMeMobileTopSection">
			<h2 class="homeViewHeading">{{title}}</h2>
		</div>
			<amp-carousel height="240" layout="fixed-height" type="slides" controls autoplay loop delay="8000"> 
			{{#subComponents}}
		<div class="inspireMeCenter">
			<div class="stayTwo">
				<div>
					<amp-img class="responsive-img" width="auto" height="200"
						layout="flex-item" src="{{details.bannerImageUrl}}"
						alt="{{bannerAltText}}"></amp-img>
				</div>
			</div>
			<div class="stayOne">
				<div>
					<p class="h2 stayQuedHeading">{{headerText}}</p>
					<p>{{text}}</p>
					<div class="stayQuedBottom">
						<a href="{{details.bannerUrl}}"><button
								class="stayQuedViewAllBtn">Read The Story</button></a>
					</div>
					<div class="stayQuedBottom">
						<a href="/que/"><button
								class="stayQuedViewAllBtn">Read More</button></a>
					</div>
				</div>
			</div>
		</div>
		{{/subComponents}} 
		</amp-carousel> 
		</template> 
		</amp-list>
	</div>
	
	<div class="amp-tealium" style="position:absolute;top:75%;">
		<c:set var="site_region" value="en"/>
		<c:set var="user_type" value="${user_type}"/>
		<c:set var="user_login_type" value="${userLoginType}"/>
		<c:set var="user_id" value="${user_id}"/>
		<c:set var="page_type" value="home"/>
		<c:set var="page_name" value="homepage"/>
		<c:set var="product_category" value="null"/>
		<c:set var="page_subcategory_name" value="null"/>
		<c:set var="page_subcategory_name_L3" value="null"/>
		<c:set var="session_id" value="${sessionId}"/>
		<c:set var="visitor_ip" value="${visitorIp}"/>
		<c:set var="site_currency" value="INR"/>
		<c:set var="site_section" value="home"/>
		<c:set var="IA_company" value="${pageContext.request.serverName}"/>
		<c:set var="fb_content_type" value="null"/>
		<c:set var="product_sku_quick_view" value="null"/>
		<c:set var="page_subcategory_L1" value="null"/>
		<c:set var="page_subcategory_L2" value="null"/>
		<c:set var="page_subcategory_L3" value="null"/>
		<c:set var="product_mrp" value="null"/>
		<c:set var="post_category" value="null"/>
		<c:set var="post_title" value="null"/>
		<c:set var="post_author" value="null"/>
		
		<amp-iframe height="1" width="1"
		 src="${base}/iframeUtag_homepage.html?site_region=${site_region}&user_type=${user_type}&user_login_type=${user_login_type}&user_id=${user_id}&page_type=${page_type}&page_name=${page_name}&product_category=${product_category}&page_subcategory_name=${page_subcategory_name}&page_subcategory_name_L3=${page_subcategory_name_L3}&session_id=${session_id}&visitor_ip=${visitor_ip}&site_currency=${site_currency}&site_section=${site_section}&IA_company=${IA_company}&fb_content_type=${fb_content_type}&product_sku_quick_view=${product_sku_quick_view}&page_subcategory_L1=${page_subcategory_L1}&page_subcategory_L2=${page_subcategory_L2}&page_subcategory_L3=${page_subcategory_L3}&product_mrp=${product_mrp}&post_category=${post_category}&post_title=${post_title}&post_author=${post_author}" 
		 sandbox="allow-scripts allow-same-origin"></amp-iframe>
	</div>
	
	<footer:ampfooter />
	
</body>
</html>
