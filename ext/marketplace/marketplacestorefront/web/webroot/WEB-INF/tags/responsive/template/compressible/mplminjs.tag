<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${empty buildNumber}">
	<c:set var="buildNumber" value="100000" />
</c:if>

<!-- UF-439 -->
	<c:if test="${!fn:contains(themeResourcePath,'theme-luxury')}">
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
					<!-- Best pics loaded as part of tmpmain script load -->
					document.body.appendChild(ia);
					 if (!$('#bestPicks').attr('loaded')) {
			             //not in ajax.success due to multiple sroll events
			             $('#bestPicks').attr('loaded', true);
			             if ($('#bestPicks').children().length == 0 && $('#pageTemplateId').val() ==
			                 'LandingPage2Template') {
			                 getBestPicksAjaxCall();
			             }
			         }
					 <!-- Gigya called there after -->
					 callGigya();
				}
				
				ia.onload = function(){
					<!-- Banner loaded after IA plugin loaded  -->
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
					//TISSPTEN-134
					_autoload();
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
</c:if>
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

/*--- Start of  Mobile view Left menu Sign In toggle---- */
$(window).on("load resize",function(e){
	//alert('here1');
	if($('#pageTemplateId').val() =='LandingPage2Template'){	
		//alert('here2');
		var style=null;
		if($(window).width() < 773) {
			  $(document).off("click","span#mobile-menu-toggle").on("click","span#mobile-menu-toggle",function() {
				$("a#tracklink").mouseover();
				$(this).parent('li').siblings().children('#mobile-menu-toggle').removeClass("menu-dropdown-arrow");
				$(this).parent('li').siblings().find('#mobile-menu-toggle + ul').slideUp();
				$(this).next().slideToggle();
				$(this).toggleClass("menu-dropdown-arrow");
			});
			  $(document).on("click","ul.words span#mobile-menu-toggle",function() {
				var id = $(this).parents('ul.words').siblings("div.departmenthover").attr("id"), ind = $(this).parent('li.short.words').index("."+id+" .short.words")
					$(".long.words").hide();
					div_container=$(this).parent(".short.words");
					if($(this).hasClass('menu-dropdown-arrow')){
						for(var i=1;i<$(".long.words").length;i++){
							if(div_container.next().hasClass("long")){
								div_container.next().show();
								div_container=div_container.next();
							}
							else
								break;
							}		
					} else {
						$(".long.words").hide();
					}
					longStyleContainer=$(this).parent(".short.words").next(".long.words").attr("style");
					$(this).parents("li.level1").nextAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
					$(this).parents("li.level1").prevAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
			  });
			/*--- Mobile view shop by brand and department ---*/ 
		}
		else {
			$("#mobile-menu-toggle").next().attr("style",style);
			$("li.short.words,li.long.words").next().attr("style",style); 
		}
		
		div_container=$(".productGrid-menu nav #mobile-menu-toggle.mainli.menu-dropdown-arrow").parent(".short.words");
		for(var i=1;i<$(".long.words").length;i++){
			if(div_container.next().hasClass("long")){
				div_container.next().attr("style",longStyleContainer);
				div_container=div_container.next();
			}
			else
				break;
			}
	}
	});
</script>
</c:if>