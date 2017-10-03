
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
					 //UF-6399
					var forceLoginUser = ($.cookie("mpl-user") == "anonymous") && window.location.search.indexOf("boxed-login") >= 1 ? "Y" : "N";
					var isMobile = screen.width < 460 ? "true" : "false" ;
					if(forceLoginUser == "Y"){
					if(isMobile == "true"){
					setTimeout(function(){
					window.location.href="/login";
					},10000);
					}else{
					$.ajax({
					url: "/login?frame=true&box-login",
					type: "GET",
					responseType: "text/html",
					success: function(response){
						$("#login-modal").find(".content").html('<button id="close-login" type="button" class="close"></button>'+response);
					},
					fail: function(response){
						alert(response);
					}
					});
					setTimeout(function(){
					$("#login-modal").modal({
						 backdrop: 'static',
						 keyboard: false
					 });
					},2000);
					}
					}
					//TPR-6654
					var pageTypeVal = $("#pageType").val();
					if(pageTypeVal == "homepage" && ($.cookie("mpl-user") == "anonymous")){
						$(".enter-pincode").show();
				}
				}
				
				ia.onload = function(){
					<!-- Banner loaded after IA plugin loaded  -->
					if(typeof homePageBannerTimeout!== "undefined"){
						$(".electronic-rotatingImage").owlCarousel({
							items:1,
				    		//loop: true,
							loop: $("#rotatingImage img").length == 1 ? false : true,
				    		nav:true,
				    		dots:false,
				    		navText:[]
						});
					}
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
					
					if($('#pageTemplateId').val() =='LandingPage2Template'){
						setTimeout(function(){$(".timeout-slider").removeAttr("style")},1500);
					}
					 $(window).on('scroll', function() {
						 
				         var wH = $(window).height(),
				         wS = $(this).scrollTop();
				         
					     // brandsYouLove ID 
					    // var hT = $('.lazy-reached-brandsYouLove').offset().top, 
					    var ht1 = $('.lazy-reached-brandsYouLove');
					    if(ht1.length)
					    	{
					    		var hT = ht1.offset().top;
						        var hH = $('.lazy-reached-brandsYouLove').outerHeight();
							     
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
					    	}
					     
				      // bestOffers ID 
					    // var hT = $('.lazy-reached-bestOffers').offset().top,
					    var ht1 = $('.lazy-reached-bestOffers');
					    if(ht1.length)
					    	{
					    		var hT = ht1.offset().top;
						        var hH = $('.lazy-reached-bestOffers').outerHeight();
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
					    	}
					    
					     
					     // promobannerhomepage ID
					     //var hT = $('.lazy-reached-promobannerhomepage').offset().top,
					     var ht1 = $('.lazy-reached-promobannerhomepage');
					     if(ht1.length)
					    	{
					    		var hT = ht1.offset().top;
						        var hH = $('.lazy-reached-promobannerhomepage').outerHeight();
							     
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
					    	}
					     
					     
					     //productYouCare ID
					    // var hT = $('.lazy-reached-productYouCare').offset().top,
					    var ht1 = $('.lazy-reached-productYouCare');
					    if(ht1.length)
					    	{
					    		var hT = ht1.offset().top;
						        var hH = $('.lazy-reached-productYouCare').outerHeight();
							     
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
					    	}
					     
					     //stayQued ID
					    // var hT = $('.lazy-reached-stayQued').offset().top,
					    var ht1 = $('.lazy-reached-stayQued');
					    if(ht1.length)
					    	{
					    		var hT = ht1.offset().top;
						        var hH = $('.lazy-reached-stayQued').outerHeight();
							     
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
					    	}
					     
					     //newAndExclusive ID
					     //var hT = $('.lazy-reached-newAndExclusive').offset().top,
					     var ht1 = $('.lazy-reached-newAndExclusive');
					     if(ht1.length)
					    	{
					    		var hT = ht1.offset().top;
						        var hH = $('.lazy-reached-newAndExclusive').outerHeight();
							     
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
					    	}
					     
					    //showcase ID
					     //var hT = $('.lazy-reached-showcase').offset().top,
					     var ht1 = $('.lazy-reached-showcase');
					     if(ht1.length)
					    	{
					    		var hT = ht1.offset().top;
						        var hH = $('.lazy-reached-showcase').outerHeight();
							     
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
					    	}
					     
					   //showcaseMobile ID
					   //var hT = $('.lazy-reached-showcaseMobile').offset().top,
					   var ht1 = $('.lazy-reached-showcaseMobile');
					    if(ht1.length)
					    	{
					    		var hT = ht1.offset().top;
						        var hH = $('.lazy-reached-showcaseMobile').outerHeight();
							    
							     if (wS > (hT + hH - wH)) {
							    	 if (!$('#showcaseMobile').attr('loaded')) {
							             //not in ajax.success due to multiple sroll events
							             $('#showcaseMobile').attr('loaded', true);
		
							             if ($('#showcaseMobile').children().length == 0 && $('#pageTemplateId').val() == 'LandingPage2Template') {
							                 showMobileShowCase();
							             }
							         }
							     }
					    	}
					 });
					 <!-- PDP changes -->
					 	if($('.productImagePrimaryLink').length){
					 		$('.productImagePrimaryLink').picZoomer();
					 	}
						if ($(window).width() > 789) {
							$(".picZoomer-pic-wp img").attr('data-zoom-image',$(".product-image-container .productImageGallery .imageList .active img").attr("data-zoomimagesrc"));
							$('.picZoomer-pic-wp .picZoomer-pic').elevateZoom({
								zoomType : "window",
								cursor : "crosshair",
								zoomWindowFadeIn : 500,
								zoomWindowFadeOut : 750
							});
						}
						
					 	$("img.lazy").lazyload();
					 	
					 	//SERP display hierarchy 
						var inputArray = escape(["|MSH10:Women's Clothing:L1:true:0:1|MSH1010:What's New!:L2:false:0:1|MSH1010100:Apparel:L3:false:0:1","|MSH10:Women's Clothing:L1:true:0:1|MSH1011:Apparel:L2:false:0:1|MSH1011101:Tops and T-shirts:L3:false:0:1","|MSH10:Women's Clothing:L1:true:0:1|MSH1011:Apparel:L2:false:0:1|MSH1011102:Shirts:L3:false:0:1","|MSH10:Women's Clothing:L1:true:0:2|MSH1015:Shop by Look:L2:false:0:2|MSH1015102:Work Wear:L3:false:0:2","|MSH10:Women's Clothing:L1:true:0:1|MSH1015:Shop by Look:L2:false:0:1|MSH1015103:Casual Day Out:L3:false:0:1","|MSH10:Women's Clothing:L1:true:0:2|MSH1016:Casual Wear:L2:false:0:2|MSH1016103:Shirts:L3:false:0:2","|MSH10:Women's Clothing:L1:true:0:1|MSH1016:Casual Wear:L2:false:0:1|MSH1016110:Sweaters:L3:false:0:1","|MSH10:Women's Clothing:L1:true:0:2|MSH1016:Casual Wear:L2:false:0:2|MSH1016111:Work wear:L3:false:0:2","|MSH11:Men's Clothing:L1:true:0:55|MSH1110:What's New!:L2:false:0:55|MSH1110100:Apparel:L3:false:0:55","|MSH11:Men's Clothing:L1:true:0:5|MSH1111:Apparel:L2:false:0:5|MSH1111101:Casual and Evening Wear Shirts:L3:false:0:5","|MSH11:Men's Clothing:L1:true:0:1|MSH1112:Ethnic Wear:L2:false:0:1|MSH1112107:Accessories:L3:false:0:1","|MSH11:Men's Clothing:L1:true:0:23|MSH1115:Shop by Look:L2:false:0:23|MSH1115102:Work Wear:L3:false:0:23","|MSH11:Men's Clothing:L1:true:0:11|MSH1115:Shop by Look:L2:false:0:11|MSH1115103:Casual Day Out:L3:false:0:11","|MSH11:Men's Clothing:L1:true:0:20|MSH1115:Shop by Look:L2:false:0:20|MSH1115104:College Look:L3:false:0:20","|MSH11:Men's Clothing:L1:true:0:20|MSH1116:Casual Wear:L2:false:0:20|MSH1116100:T-shirts & Polos:L3:false:0:20","|MSH11:Men's Clothing:L1:true:0:10|MSH1116:Casual Wear:L2:false:0:10|MSH1116101:Shirts:L3:false:0:10","|MSH11:Men's Clothing:L1:true:0:23|MSH1117:Formal Wear:L2:false:0:23|MSH1117100:Shirts:L3:false:0:23","|MSH12:Electronics:L1:true:0:1|MSH1210:Mobile Phones:L2:false:0:1|MSH1210100:Smartphones:L3:false:0:1","|MSH12:Electronics:L1:true:0:1|MSH1210:Mobile Phones:L2:false:0:1|MSH1210101:Smartphone accessories:L3:false:0:1","|MSH12:Electronics:L1:true:0:1|MSH1215:Small and Kitchen Applicances:L2:false:0:1|MSH1215104:Kettles\/ Coffee Makers:L3:false:0:1","|MSH12:Electronics:L1:true:0:1|MSH1220:Camera:L2:false:0:1|MSH1220101:Camera accessories:L3:false:0:1","|MSH21:Kids:L1:true:0:33|MSH2112:Infants:L2:false:0:33|MSH2112100:Tops & T-shirts:L3:false:0:33","|MSH21:Kids:L1:true:0:11|MSH2112:Infants:L2:false:0:11|MSH2112100:Tops & T-shirts:L3:false:0:11|MSH2112100100:T-Shirts:L4:false:0:11","|MSH21:Kids:L1:true:0:22|MSH2112:Infants:L2:false:0:22|MSH2112100:Tops & T-shirts:L3:false:0:22|MSH2112100101:Tops:L4:false:0:22","|MSH21:Kids:L1:true:0:6|MSH2112:Infants:L2:false:0:6|MSH2112106:Shirts:L3:false:0:6"]);
						if(inputArray != "" || inputArray != []){
						constructDepartmentHierarchy(inputArray);
						}
						//TISSPTEN-134
						_autoload();
						//UF-409 -> added for ajax complete events to auto lazy load
						$( document ).ajaxComplete(function( event, xhr, settings ) {
							//if($('#pageType').val() == "productsearch" || $('#pageType').val() == "product" || $('#pageType').val() == "category" || $('input[name=customSku]').length){
							if($('#pageType').val() == "productsearch" || $('#pageType').val() == "category" || $('input[name=customSku]').length){	
								$("img.lazy").lazyload();
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
