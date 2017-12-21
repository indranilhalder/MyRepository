<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html amp>
<header:ampheader/>
<body on="tap:AMP.setState({visible: false})" role="menu" tabindex="0">
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('amp.analytics.host.adobe')" var="host"/>
<c:set var="base" value="https://${host}"/>
<!-- <amp-install-serviceworker src="/cliq-service-worker.js" layout="nodisplay"></amp-install-serviceworker> -->
	<div class="top-header">
	    <a href="#">Marketplace</a>
	    <a href="#">Luxury</a>
	  </div>
	<header>
		<button class="header-icon-1 mobile-item" on='tap:sidebar.open'><i class="fa fa-navicon"></i></button>
		<!-- <a href="index.php" class="header-logo"><img src="./images/logo.png" /></a> -->

		<section class="col-xs-12 header-search-section">
			<section class="header-search-left">
				<section class="logo-container header-search-left-child">
					<amp-img class="logo-image" width="50" height="30" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/9906406817822.png"></amp-img>
				</section>
			</section>
      <section class="header-search-center desktop-item" [class]="visible ? 'header-search-center mobile-item' : 'header-search-center desktop-item'">
					<!-- <input class="header-search-input" autocomplete placeholder="Search in Marketplace" /> -->
					<section class="department-menu desktop-item">
						<span><span [text]="category">All</span> <i class="fa fa-angle-down"></i></span>
						<ul id="department_menu_list" class="department-menu-items">
							<li tabindex="1" role="button" on='tap:AMP.setState({category: "All", categoryId: "all"})'>All</li>
							<li tabindex="2" role="button" on='tap:AMP.setState({category: "Women&#39;s Clothing", categoryId: "MSH10"})'>Women's Clothing</li>
							<li tabindex="3" role="button" on='tap:AMP.setState({category: "Men&#39;s Clothing", categoryId: "MSH11"})'>Men's Clothing</li>
							<li tabindex="4" role="button" on='tap:AMP.setState({category: "Electronics", categoryId: "MSH12"})'>Electronics</li>
							<li tabindex="5" role="button" on='tap:AMP.setState({category: "Footwear", categoryId: "MSH13"})'>Footwear</li>
							<li tabindex="6" role="button" on='tap:AMP.setState({category: "Watches", categoryId: "MSH15"})'>Watches</li>
							<li tabindex="7" role="button" on='tap:AMP.setState({category: "Accessories", categoryId: "MSH16"})'>Accessories</li>
						</ul>
					</section>
					<input name="search" id="search_autocomplete"
          type="text"
					placeholder="Search in Marketplace"
          class="header-search-input"
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
          [value]="term || ''"
          value=""
          required
          autocomplete="off" />
				<button class="header-search-btn"><i class="fa fa-search"></i></button>
				<div class="suggest">
					<div class="autosuggest-container hidden"
						[class]="(showDropdown && term) ?
							'autosuggest-container' :
							'autosuggest-container hidden'">
						<amp-list class="autosuggest-box"
							layout="fixed-height"
							height="140"
							src="https://www.tatacliq.com/search/autocomplete/MplEnhancedSearchBox?term="
							[src]="term.length>2 ?
								autosuggest.endpoint :
								autosuggest.emptyAndInitialTemplateJson"
							id="autosuggest-list">
							<template type="amp-mustache">
								<amp-selector id="autosuggest-selector"
									keyboard-select-mode="focus"
									layout="container"
									on="
										select:
											AMP.setState({
												term: event.targetOption,
												showDropdown: false
											}),
											autosuggest-list.hide">
                  <strong>{{searchTerm}}</strong>
                  {{#brands}}
									<div class="select-option no-outline"
										role="option"
										tabindex="0"
										on="tap:autosuggest-list.hide"
										option="{{name}}">
                    <a href="https://www.tatacliq.com/search/page-1?q={{searchTerm}}%3Arelevance%3Abrand%3A{{code}}%3AisLuxuryProduct%3Afalse&text={{searchTerm}}&searchCategory=all">in {{name}}</a>
                  </div>
									{{/brands}} {{^brands}}
									{{/brands}}
                  <strong>{{searchTerm}}</strong>
                  {{#categories}}
									<div class="select-option no-outline"
										role="option"
										tabindex="0"
										on="tap:autosuggest-list.hide"
										option="{{name}}">
                    <a href="https://www.tatacliq.com/search/page-1?q={{searchTerm}}%3Arelevance%3Acategory%3A{{code}}%3AisLuxuryProduct%3Afalse&text={{searchTerm}}&searchCategory=all">in {{name}}</a>
                  </div>
									{{/categories}} {{^categories}}
									{{/categories}}
								</amp-selector>
							</template>
						</amp-list>
					</div>
				</div>
				<amp-state id="autosuggest">
					<script type="application/json">
						{
							"category": "",
							"endpoint": "/json/searchdata.json",
							"emptyAndInitialTemplateJson": {"items": [{
								"suggestions": [],
								"products": [],
                "categories": [],
                "searchTerm": "",
                "brands": []
							}]},
              "searchInBrand": "https://www.tatacliq.com/search/page-1?q="
						}
					</script>
				</amp-state>
      </section>
		</section>
		<p class="header-icon-2 mobile-item"><a href="/cart"><span class="responsive-bag">0</span></a></p>
		<p class="header-icon-3 mobile-item"><i tabindex="1" role="main" on="tap:AMP.setState({visible: !visible}), search_autocomplete.focus" class="">&nbsp;</i></p>
	</header>

	<amp-sidebar id="sidebar" layout="nodisplay" side="left">
		<amp-accordion class="sidebar-menu l1-accordian">
			<section>
				<h4 class="l1-options">Department<i class="fa fa-angle-right"></i></h4>
        <amp-accordion class="sidebar-menu l2-accordian">
          <section>
            <h4 class="l2-options">Women<i class="fa fa-angle-right"></i></h4>
            <amp-accordion class="sidebar-menu l3-accordian">
              <section>
                <h4 class="l3-options">Ethnic wear<i class="fa fa-angle-right"></i></h4>
                <ul>
                  <li><a href="#">Kurtis And Kurtas</a></li>
                  <li><a href="#">Suit Sets</a></li>
                  <li><a href="#">Fusion Wear</a></li>
                </ul>
              </section>
              <section>
                <h4 class="l3-options">Inner & Nightwear<i class="fa fa-angle-right"></i></h4>
                <ul>
                  <li><a href="#">Bras</a></li>
                  <li><a href="#">Panties</a></li>
                  <li><a href="#">Lingerie Sets</a></li>
                </ul>
              </section>
              <section>
                <h4 class="l3-options">Western Wear<i class="fa fa-angle-right"></i></h4>
                <ul>
                  <li><a href="#">Tops & Tees</a></li>
                  <li><a href="#">Dresses</a></li>
                  <li><a href="#">Shirts</a></li>
                </ul>
              </section>
            </amp-accordion>
          </section>
        </amp-accordion>
			</section>
      <section>
				<h4 class="l1-options">Brand<i class="fa fa-angle-right"></i></h4>
        <amp-accordion class="sidebar-menu l2-accordian">
        <c:forEach items="${shopByBrandDataList}" var="shopByBrands">
        <c:choose>
        	<c:when test="${shopByBrands.masterBrandName eq 'A-Z Brands'}">
        	          <section>
            <h4 class="l2-options">A-Z List<i class="fa fa-angle-right"></i></h4>
            <div class="a2z-section">
              <amp-selector role="tablist"
        				layout="container"
        				class="a2zTabContainer">
        				<div role="tab"
        					class="a2zTabButton"
        					selected
        					option="a">A-E</div>
        				<div role="tabpanel"
        					class="a2zTabContent">
        					<div>
                    <ul class="a-z-ul">
                				<h3>A</h3>
                				<li class="a-z-li"><a href="/apple/c-mbh12e00008">Apple</a></li>
                				<li class="a-z-li"><a href="/asics/c-mbh13f00013">Asics</a></li>
                				<li class="a-z-li"><a href="/asus/c-mbh12e00093">Asus</a></li>
                				<li class="a-z-li"><a href="/and/c-mbh11a00015">AND</a></li>
                				<li class="a-z-li"><a href="/allen-solly/c-mbh11a00023">Allen Solly</a></li>
                				<li class="a-z-li"><a href="/ascot/c-mbh11a00248">Ascot</a></li>
                				<li class="a-z-li"><a href="/aurelia/c-mbh11a00005">Aurelia</a></li>
                				<li class="a-z-li"><a href="/avirat/c-mbh11a00272">Aviraté</a></li>
                				<li class="a-z-li"><a href="/amante/c-mbh11a00038">Amante</a></li>
                		</ul>
                		<ul class="a-z-ul">
                				<h3>B</h3>
                				<li class="a-z-li"><a href="/bajaj/c-mbh12e00055">Bajaj</a></li>
                				<li class="a-z-li"><a href="/blue-star/c-mbh12e00068">Blue Star</a></li>
                				<li class="a-z-li"><a href="/basics/c-mbh11a00103">Basics</a></li>
                				<li class="a-z-li"><a href="/bang-olufsen/c-mbh12e00494">Bang &amp; Olufsen</a></li>
                				<li class="a-z-li"><a href="/bissell/c-mbh12e00261">Bissell</a></li>
                				<li class="a-z-li"><a href="/bombay-paisley/c-mbh11a00174">Bombay Paisley</a></li>
                				<li class="a-z-li"><a href="/bosch/c-mbh12e00026">Bosch</a></li>
                				<li class="a-z-li"><a href="/braun/c-mbh12e00041">Braun</a></li>
                				<li class="a-z-li"><a href="/buckaroo/c-mbh13f00020">Buckaroo</a></li>
                				<li class="a-z-li"><a href="/breaking-bad/c-mbh11a00310">Breaking Bad</a></li>
                				<li class="a-z-li"><a href="/bodybasics/c-mbh11a00273">Bodybasics</a></li>
                				</ul>
                		<ul class="a-z-ul">
                				<h3>C</h3>
                				<li class="a-z-li"><a href="/croma/c-mbh12e00048">Croma</a></li>
                				<li class="a-z-li"><a href="/colorplus/c-mbh11a00094">ColorPlus</a></li>
                				<li class="a-z-li"><a href="/converse/c-mbh13a00142">Converse</a></li>
                				<li class="a-z-li"><a href="/cottonworld/c-mbh11a00008">Cottonworld</a></li>
                				<li class="a-z-li"><a href="/crocs/c-mbh13f00032">Crocs</a></li>
                				<li class="a-z-li"><a href="/calvin-klein/c-mbh11a00059">Calvin Klein</a></li>
                				<li class="a-z-li"><a href="/chef-pro/c-mbh12e00257">Chef Pro</a></li>
                				<li class="a-z-li"><a href="/canon/c-mbh12e00010">Canon</a></li>
                				<li class="a-z-li"><a href="/circle/c-mbh12e00081">Circle</a></li>
                				<li class="a-z-li"><a href="/corioliss/c-mbh12e00857">Corioliss</a></li>
                				</ul>
                		<ul class="a-z-ul">
                				<h3>D</h3>
                				<li class="a-z-li"><a href="/dell/c-mbh12e00011">Dell</a></li>
                				<li class="a-z-li"><a href="/dlink/c-mbh12e00074">Dlink</a></li>
                				<li class="a-z-li"><a href="/dr-morepen/c-mbh12e00364">Dr Morepen</a></li>
                				<li class="a-z-li"><a href="/disney/c-mbh11a00145">Disney</a></li>
                				<li class="a-z-li"><a href="/da-vinchi/c-mbh13f00091">Da Vinchi</a></li>
                				<li class="a-z-li"><a href="/duracell/c-mbh12e00590">Duracell</a></li>
                				<li class="a-z-li"><a href="/digisol/c-mbh12e00127">DigiSol</a></li>
            				</ul>
                		<ul class="a-z-ul">
                				<h3>E</h3>
                				<li class="a-z-li"><a href="/eureka-forbes/c-mbh12e00014">Eureka Forbes</a></li>
                				<li class="a-z-li"><a href="/easies/c-mbh11a00190">Easies</a></li>
                				<li class="a-z-li"><a href="/eta/c-mbh11a00175">ETA</a></li>
                				<li class="a-z-li"><a href="/electrolux/c-mbh12e00100">Electrolux</a></li>
                				<li class="a-z-li"><a href="/eveready/c-mbh12e00286">Eveready</a></li>
                				<li class="a-z-li"><a href="/energizer/c-mbh12e00382">Energizer</a></li>
                				<li class="a-z-li"><a href="/elac/c-mbh12e01528">Elac</a></li>
                				<li class="a-z-li"><a href="/envent/c-mbh12e01152">Envent</a></li>
            				</ul>
                  </div>
        				</div>
        				<div role="tab"
        					class="a2zTabButton"
        					option="b">F-J</div>
                <div role="tabpanel"
        					class="a2zTabContent">
        					F to J
          			</div>
        				<div role="tab"
        					class="a2zTabButton"
        					option="c">K-O</div>
                <div role="tabpanel"
        					class="a2zTabContent">
        					K to O
        				</div>
                <div role="tab"
        					class="a2zTabButton"
        					option="d">P-T</div>
                <div role="tabpanel"
        					class="a2zTabContent">
        					P to T
        				</div>
                <div role="tab"
        					class="a2zTabButton"
        					option="e">U-Z</div>
                <div role="tabpanel"
        					class="a2zTabContent">
        					U to Z
        				</div>
        			</amp-selector>
            </div>
          </section>
        	
        	</c:when>
        <c:otherwise>
          <section>
            <h4 class="l2-options">${shopByBrands.masterBrandName}<i class="fa fa-angle-right"></i></h4>
            <ul>
            <c:forEach items="${shopByBrands.subBrandList}" var="subShopByBrand">
              <li><a href="${subShopByBrand.subBrandUrl}">${subShopByBrand.subBrandName}</a></li>
             </c:forEach>
            </ul>
          </section>        
        </c:otherwise>
        </c:choose>

          </c:forEach>
        </amp-accordion>
			</section>
		</amp-accordion>
    <p class="sidebar-divider-item"><a href="#"><i class="fa fa-user"></i>Sign In/Sign Up</a></p>
    <p class="sidebar-divider-item"><a href="#"><i class="fa fa-heart"></i>My Wishlists</a></p>
    <p class="sidebar-divider-item"><a href="#">Track Order</a></p>
    <p class="sidebar-divider-item"><a href="#"><i class="fa fa-mobile-phone"></i>Download App</a></p>
    <p class="sidebar-divider-item"><a href="#">Enter Your Pincode</a></p>
	</amp-sidebar>

	<div class="sliders main-content">
		<amp-list src="/pwamp/getHomePageBanners?version=Online" height="370" layout="fixed-height">
			<template type="amp-mustache">
				<amp-carousel class="slider" width="400" height="400" layout="responsive" type="slides" controls autoplay loop delay="3000">
				  {{#moblileBanners}}
				    <div>
  						<amp-img class="responsive-img" src="{{url}}" layout="fill"></amp-img>
  					</div>
					{{/moblileBanners}}
				</amp-carousel>
			</template>
		</amp-list>
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
	<div id="topDealsComp">
		<div class="topDealsTopSection">
			<h2 class="homeViewHeading">Top Deals <small class="homeViewAllBtn"><a href="#">View All</a></small></h2>
		</div>
		<div>
			<div id="topDealsCompCarousel">
			<amp-list src="/pwamp/getBestPicks?version=Online" height="360" layout="fixed-height">
			<template type="amp-mustache">
				<amp-carousel height="360" layout="fixed-height" type="carousel">
				{{#subItems}}
					<div class="topDealsItem">
						<div class="topDealsItemImg">
							<a href="{{url}}"><amp-img class="responsive-img" layout="fill" src="{{imageUrl}}" alt="Brand Image"></amp-img></a>
						</div>
					</div>
				{{/subItems}}
				</amp-carousel>
				</template>
				</amp-list>
			</div>
		</div>
    <div class="brandStudioBottom">
      <amp-list src="/json/bestpicks.json" height="40" layout="fixed-height">
        <template type="amp-mustache">
    			<a href="{{buttonLink}}"><button class="brandStudioViewAllBtn">{{buttonText}}</button></a>
        </template>
      </amp-list>
		</div>
	</div>

	<!-- Brands You Love -->
	<div id="brandsYouLoveMobileComp" class="">
		<div class="brandStudioTop">
			<h2 class="homeViewHeading">Brand Studio</h2>
		</div>
		<div>
			<div id="brandsYouLoveMobileCompCarousel">
			<amp-list src="/pwamp/getBrandsYouLove?version=Online" height="320" layout="fixed-height">
				<template type="amp-mustache">
			    <amp-carousel height="320" layout="fixed-height" type="carousel">
			    	{{#subComponents}}
					<div class="brandsCarouselItem">
						<div class="brandStudioImg">
							<a href="{{bannerUrl}}"><amp-img class="responsive-img" layout="fill" src="{{bannerImageUrl}}" alt="{{brandLogoAltText}}"></amp-img></a>
						</div>
						<div class="brandStudioDesc">
							<p class="brandStudioDescHeading">{{bannerText}}</p>
							<p class="brandStudioDescInfo">{{text}}</p>
							<p class="brandStudioVisitStore"><a href="{{bannerUrl}}">Visit Store</a></p>
						</div>
					</div>
					{{/subComponents}}
				  </amp-carousel>
				  </template>
				  </amp-list>
			</div>
		</div>
		<!-- <div class="brandStudioBottom">
			<button class="brandStudioViewAllBtn">View All Brands</button>
		</div> -->
	</div>

	<!-- What To Buy Now -->
	<div id="whatToBuyComp">
		<div class="whatToBuyTopSection">
			<h2 class="homeViewHeading">What To Buy Now</h2>
		</div>
		<div>
			<div id="whatToBuyCompCarousel">

				<amp-list src="/pwamp/getProductsYouCare?version=Online" height="400" layout="fixed-height">
				<template type="amp-mustache">
				<amp-carousel height="400" layout="fixed-height" type="carousel">
				{{#categories}}
					<div class="whatToBuyItem">
						<div class="whatToBuyItemImg">
							<a href="{{imageURL}}"><amp-img class="responsive-img" layout="fill" src="{{mediaURL}}" alt="Brand Image"></amp-img></a>
						</div>
					</div>
				{{/categories}}
				</amp-carousel>
					</template>
				</amp-list>

			</div>
		</div>
	</div>

  <br />

  <div class="compContainer">
    <!-- Stay Qued -->
    <div id="stayQuedComp">
      <div class="stayQuedTopSection">
        <h2 class="homeViewHeading">Stay Qued</h2>
      </div>
      <amp-list src="/pwamp/getStayQuedHomepage?version=Online" height="240" layout="fixed-height">
      <template type="amp-mustache">
      {{#allBannerJsonObject}}
      <div class="stayQuedCenter">
        <div class="stayTwo">
          <div>
            <amp-img class="responsive-img" width="auto" height="200" layout="flex-item" src="{{bannerImage}}" alt="{{bannerAltText}}"></amp-img>
          </div>
        </div>
        <div class="stayOne">
          <div>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p class="h2 stayQuedHeading">{{promoText1}}</p>
            <p>{{promoText2}}</p>
            <div class="stayQuedBottom">
              <a href="{{bannerUrlLink}}"><button class="stayQuedViewAllBtn">Read The Story</button></a>
            </div>
          </div>
        </div>
      </div>
      {{/allBannerJsonObject}}
      </template>
      </amp-list>
    </div>

    <!-- New In -->
  	<div id="newInComp">
  		<div class="newInTopSection">
  			<h2 class="homeViewHeading">New In <small class="homeViewAllBtn"><a href="#">View All</a></small></h2>
  		</div>
  		<div>
  			<div id="newInCompCarousel">
  			<amp-list src="/pwamp/getNewAndExclusive?version=Online" height="280" layout="fixed-height">
  				<template type="amp-mustache">
  				<amp-carousel height="280" layout="fixed-height" type="carousel">
  				{{#newAndExclusiveProducts}}
  					<a href="{{productUrl}}"><div class="newInItem">
  						<div class="newInItemImg">
  							<amp-img width="120" height="180" layout="responsive" src="{{productImageUrl}}" alt="Brand Image"></amp-img>
  						</div>
  						<div class="newInDesc">
  							<p class="newInDescName">{{productTitle}}</p>
  							{{#productPrice}}
  							<p class="newInDescPrice">{{dispPrice}}<span class="price-right">{{strikePrice}}</span></p>
  							{{/productPrice}}
  						</div>
  					</div></a>
  					{{/newAndExclusiveProducts}}
  				</amp-carousel>
  				</template>
  				</amp-list>
  			</div>
  		</div>
  		<div class="brandStudioBottom">
  			<button class="brandStudioViewAllBtn">View All</button>
  		</div>
  	</div>
  </div>

  <br />
	<!-- Hot Now -->
	<!--  <div id="hotNowComp">
		<div class="hotNowTopSection">
			<h2 class="homeViewHeading">Hot Now</h2>
		</div>
		<div>
			<div id="hotNowCompCarousel">
			<amp-list src="/pwamp/getBestPicks?version=Online" height="320" layout="fixed-height">
				<template type="amp-mustache">
				<amp-list src="/pwamp/getProductsYouCare?version=Online" height="320" layout="fixed-height">
				<template type="amp-mustache">
				<amp-carousel height="320" layout="fixed-height" type="carousel">
				{{#subItems}}
					<div class="hotNowItem">
						<div class="hotNowItemImg">
							<amp-img class="responsive-img" layout="fill" src="{{imageUrl}}" alt="Brand Image"></amp-img>
						</div>
						<div class="hotNowDesc">
							<p class="hotNowTitle">Vivo</p>
							<p class="hotNowDescName">{{text}}</p>

						</div>
					</div>
					{{/subItems}}
				</amp-carousel>
				</template>
				</amp-list>
				</template>
				</amp-list>

			</div>
		</div>
		<div class="brandStudioBottom">
			<button class="brandStudioViewAllBtn">Shop the Hot List</button>
		</div>
	</div>-->

	<!-- Inspire Me -->
  <div id="inspireMeMobileComp">
    <div class="inspireMeMobileTopSection">
      <h2 class="homeViewHeading">Inspire Me</h2>
    </div>
    <amp-list src="/pwamp/getCollectionShowcase?version=Online" height="240" layout="fixed-height">
      <template type="amp-mustache">
        <amp-carousel height="240" layout="fixed-height" type="slides" controls autoplay loop delay="8000">
          {{#subComponents}}
          <div class="inspireMeCenter">
            <div class="stayTwo">
              <div>
                <amp-img class="responsive-img" width="auto" height="200" layout="flex-item" src="{{details.bannerImageUrl}}" alt="{{bannerAltText}}"></amp-img>
              </div>
            </div>
            <div class="stayOne">
              <div>
                <p class="h2 stayQuedHeading">{{headerText}}</p>
                <p>{{text}}</p>
                <p>These accessories simply add just the right amount of style to any outfit.</p>
                <div class="stayQuedBottom">
                  <a href="{{details.bannerUrl}}"><button class="stayQuedViewAllBtn">Read The Story</button></a>
                </div>
                <div class="stayQuedBottom">
                  <a href="https://www.tatacliq.com/que/"><button class="stayQuedViewAllBtn">Read More</button></a>
                </div>
              </div>
            </div>
          </div>
          {{/subComponents}}
        </amp-carousel>
      </template>
    </amp-list>
  </div>
<footer:ampfooter/>
	<!-- <DTM Amp-Analytics starts> -->
<!--  <amp-analytics type="adobeanalytics_nativeConfig">
	<script type="application/json">
	{
		"requests": {
			"base": "${base}",           
			"iframeMessage": "${base}/stats.html?pageURL=${ampdocUrl}&ref=${documentReferrer}"  
		},
		"vars": {
			"host": "${host}"           
		},
		"extraUrlParams": {
			"pageName": "HomePage",        
			"page_type": "home",
            "user_login_type" : "${userLoginType}",
            "site_currency "  : "INR"
		}
	}
	</script>
</amp-analytics> -->
</body>
</html>
