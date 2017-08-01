<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${empty buildNumber}">
	<c:set var="buildNumber" value="100000" />
</c:if>

<!-- UF-439 -->
<c:choose>
	<c:when test="${fn:contains(pageBodyCssClasses, 'homepage')}">
		<script type="text/javascript">
			var plugins = document.createElement("script");
			plugins.src = "${commonResourcePath}/js/minified/plugins.min.js?v=${buildNumber}";

			var tmpmain = document.createElement("script");
			tmpmain.src = "${commonResourcePath}/js/minified/tmpmain.min.js?v=${buildNumber}";

			var ia = document.createElement("script");
			ia.src = "${commonResourcePath}/js/minified/ia.min.js?v=${buildNumber}";

			function downloadJSAtOnload() {
				// add the first script element
				document.body.appendChild(plugins);

				plugins.onload = function() {
					document.body.appendChild(tmpmain);
				}

				tmpmain.onload = function() {
					document.body.appendChild(ia);
					 if (!$('#bestPicks').attr('loaded')) {
			             //not in ajax.success due to multiple sroll events
			             $('#bestPicks').attr('loaded', true);
			             if ($('#bestPicks').children().length == 0 && $('#pageTemplateId').val() ==
			                 'LandingPage2Template') {
			                 getBestPicksAjaxCall();
			             }
			         }
					 callGigya();
				}
				
				ia.onload = function(){
					
					$(".electronic-rotatingImage").owlCarousel({
						items:1,
			    		//loop: true,
						loop: $("#rotatingImage img").length == 1 ? false : true,
			    		nav:true,
			    		dots:false,
			    		navText:[]
					});
					/*TPR-268*/
				 
					//Desktop view
					/*TISSQAEE-403 Start*/
					if(typeof homePageBannerTimeout!== "undefined"){
						var timeout = parseInt(homePageBannerTimeout) * 1000 ;
						var loop = $(".homepage-banner #rotatingImageTimeout img").length > 1 ? true :false;
						var dots = $(".homepage-banner #rotatingImageTimeout img").length > 1 ? true :false; 
						//$(".home-rotatingImage").owlCarousel({
						$("#rotatingImageTimeout").owlCarousel({
							items:1,
							nav:false,
							dots:dots,
							loop: loop,
					        autoplay: true,
					        //autoHeight : true, //UF-365
					        autoplayTimeout: timeout
					    });
						//UF-291 starts here ---- UF-365 starts here 
						if ($(window).width() > 773) {
							$("#rotatingImageTimeout img").each(function() {
							    if ($(this).attr("data-src")) {
									$(this).attr("src",$(this).attr("data-src"));
									$(this).removeAttr("data-src");
									$(this).load(function(){
										$(this).css("display", "block");
									});
								}	
							});
						}
						//UF-291 ends here
					}
						//Mobile View
						if(typeof homePageBannerTimeout!== "undefined"){
							var timeout = parseInt(homePageBannerTimeout) * 1000 ;
							var loop = $(".homepage-banner #rotatingImageTimeoutMobile img").length > 1 ? true :false;
							var dots = $(".homepage-banner #rotatingImageTimeoutMobile img").length > 1 ? true :false;
							
							$("#rotatingImageTimeoutMobile").owlCarousel({
								
								items:1,
								nav:false,
								dots:dots,
								loop: loop,
						        autoplay: true,
						        //autoHeight : true, //UF-365
						        autoplayTimeout: timeout
						    });
							//UF-291 starts here ---- UF-365 starts here
							if ($(window).width() <= 773) {	
								$("#rotatingImageTimeoutMobile img").each(function() {
								    if ($(this).attr("data-src")) {
										$(this).attr("src",$(this).attr("data-src"));
										$(this).removeAttr("data-src");
										$(this).load(function(){
											$(this).css("display", "block");
										});
									}	
								});
							}
							//UF-291 ends here
						
					}
				}
			}

			if (window.addEventListener)
				window.addEventListener("load", downloadJSAtOnload, false);
			else if (window.attachEvent)
				window.attachEvent("onload", downloadJSAtOnload);
			else
				window.onload = downloadJSAtOnload;

		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript"
			src="${commonResourcePath}/js/minified/plugins.min.js?v=${buildNumber}"></script>
		<script type="text/javascript"
			src="${commonResourcePath}/js/minified/tmpmain.min.js?v=${buildNumber}"></script>
		<c:if test="${isIAEnabled}">
			<script type="text/javascript"
				src="${commonResourcePath}/js/minified/ia.min.js?v=${buildNumber}"></script>
		</c:if>
	</c:otherwise>
</c:choose>

<!--[if lt IE 9]>
<script type="text/javascript" src="${commonResourcePath}/js/minified/ie9.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script type="text/javascript" src="${commonResourcePath}/js/minified/ie10.min.js"></script>
<![endif]-->

<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
	<script type="text/javascript"
		src="${addOnJavaScript}?v=${buildNumber}"></script>
</c:forEach>
<c:if test="${fn:contains(pageBodyCssClasses, 'homepage')}">
<script>
//TISPT-290
$(window).load(function(){
	
	if($('#pageTemplateId').val() =='LandingPage2Template'){
		setTimeout(function(){$(".timeout-slider").removeAttr("style")},1500);
	}
	 $(window).on('scroll', function() {
		 
         var wH = $(window).height(),
         wS = $(this).scrollTop();
         
	     // brandsYouLove ID 
	     var hT = $('.lazy-reached-brandsYouLove').offset().top,
         hH = $('.lazy-reached-brandsYouLove').outerHeight();
	     
         if (wS > (hT + hH - wH)) {
        	 if (!$('#brandsYouLove').attr('loaded')) {
                 $('#brandsYouLove').attr('loaded', true);
                 
                 if ($('#brandsYouLove').children().length == 0 && $('#pageTemplateId').val() ==
                     'LandingPage2Template') {
                     if (window.localStorage) {
                         for (var key in localStorage) {
                             if (key.indexOf("brandContent") >= 0) {
                                 window.localStorage.removeItem(key);
                                 //console.log("Deleting.." + key);
                             }
                         }
                     }
                     getBrandsYouLoveAjaxCall();
                 }
             }
	     }
	     
      // bestOffers ID 
	     var hT = $('.lazy-reached-bestOffers').offset().top,
         hH = $('.lazy-reached-bestOffers').outerHeight();
	     if (wS > (hT + hH - wH)) {
	    	 
	    	 if (!$('#bestOffers').attr('loaded')) {
		            //not in ajax.success due to multiple sroll events
		            $('#bestOffers').attr('loaded', true);

		            //ajax goes here
		            //by theory, this code still may be called several times
		            if ($('#bestOffers').children().length == 0 && $('#pageTemplateId').val() ==
		                'LandingPage2Template') {
		                getBestOffersAjaxCall();
		            }
		        }
	     }
	    
	     
	     // promobannerhomepage ID
	     var hT = $('.lazy-reached-promobannerhomepage').offset().top,
         hH = $('.lazy-reached-promobannerhomepage').outerHeight();
	     
	     if (wS > (hT + hH - wH)) {
	    	 if (!$('#promobannerhomepage').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#promobannerhomepage').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#promobannerhomepage').children().length == 0 && $('#pageTemplateId').val() ==
	                'LandingPage2Template') {
	                getPromoBannerHomepage();
	            }
	        }
	     }
	     
	     
	     //productYouCare ID
	     var hT = $('.lazy-reached-productYouCare').offset().top,
         hH = $('.lazy-reached-productYouCare').outerHeight();
	     
	     if (wS > (hT + hH - wH)) {
	    	 if (!$('#productYouCare').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#productYouCare').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#productYouCare').children().length == 0 && $('#pageTemplateId').val() ==
	                'LandingPage2Template') {
	                getProductsYouCareAjaxCall();
	            }
	        }
	     }
	     
	     //stayQued ID
	     var hT = $('.lazy-reached-stayQued').offset().top,
         hH = $('.lazy-reached-stayQued').outerHeight();
	     
	     if (wS > (hT + hH - wH)) {
	    	  if (!$('#stayQued').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#stayQued').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#stayQued').children().length == 0 && $('#pageTemplateId').val() ==
	                'LandingPage2Template') {
	                getStayQuedHomepage();
	            }
	        }
	     }
	     
	     //newAndExclusive ID
	     var hT = $('.lazy-reached-newAndExclusive').offset().top,
         hH = $('.lazy-reached-newAndExclusive').outerHeight();
	     
	     if (wS > (hT + hH - wH)) {
	    	 if (!$('#newAndExclusive').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#newAndExclusive').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#newAndExclusive').children().length == 0 && $('#pageTemplateId').val() ==
	                'LandingPage2Template') {
	                getNewAndExclusiveAjaxCall();
	            }
	        }
	     }
	     
	    //showcase ID
	     var hT = $('.lazy-reached-showcase').offset().top,
         hH = $('.lazy-reached-showcase').outerHeight();
	     
	     if (wS > (hT + hH - wH)) {
	    	  if (!$('#showcase').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#showcase').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#showcase').children().length == 0 && $('#pageTemplateId').val() ==
	                'LandingPage2Template') {
	                if (window.localStorage) {
	                    for (var key in localStorage) {
	                        if (key.indexOf("showcaseContent") >= 0) {
	                            window.localStorage.removeItem(key);
	                            //console.log("Deleting.." + key);
	                        }
	                    }
	                }
	                getShowCaseAjaxCall();
	            }
	        }
	     }
	     
	   //showcaseMobile ID
	     var hT = $('.lazy-reached-showcaseMobile').offset().top,
         hH = $('.lazy-reached-showcaseMobile').outerHeight();
	    
	     if (wS > (hT + hH - wH)) {
	    	 if (!$('#showcaseMobile').attr('loaded')) {
	             //not in ajax.success due to multiple sroll events
	             $('#showcaseMobile').attr('loaded', true);

	             if ($('#showcaseMobile').children().length == 0 && $('#pageTemplateId').val() == 'LandingPage2Template') {
	                 showMobileShowCase();
	             }
	         }
	     }
	 });
});
</script>
</c:if>