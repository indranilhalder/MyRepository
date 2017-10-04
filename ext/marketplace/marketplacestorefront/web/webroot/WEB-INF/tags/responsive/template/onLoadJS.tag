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
			
			
			// SERP PLP HIERARCHICAL data
			function constructDepartmentHierarchy(inputArray) {		
				var output = [];
					if(inputArray!=""){
					for (var i = 0; i < inputArray.length; i++) {				
						var categoryArray = inputArray[i].split("|");			
						var currentNode = output;
						//Construct 'All' tree node initially for search page
						if(i==0 && $('#isCategoryPage').val() == '') {
							output[0] = {label: "All", children: [], categoryCode: "", categoryType: "All", categoryName: ""};
						}
						//Other tree nodes are constructed here
						for (var j = 0; j < categoryArray.length; j++) {				
							if(categoryArray[j] != null && categoryArray[j].length > 0){
								var categoryDetails = categoryArray[j].split(":");
								var categoryCode = categoryDetails[0];
								var categoryName = categoryDetails[1];
								var facetCount = 0;
								
								if(categoryDetails[2] == "L3" || categoryDetails[2] == "L4")
								{
									//categoryName += "  (" +categoryDetails[5] + ")";
									facetCount = "  (" +categoryDetails[5] + ")";
															
								}
								
								var categoryType = "category";
								if(categoryDetails[3] == 'true') {
									categoryType = "department"
								}
								var lastNode = currentNode;
								for (var k = 0; k < currentNode.length; k++) {
									if (currentNode[k].categoryName == categoryName) {							
										currentNode = currentNode[k].children;								
										break;
									}						
								}
								if (lastNode == currentNode) {
									var newNode = currentNode[k] = {label: categoryName, children: [], categoryCode: categoryCode, categoryType: categoryType, categoryName: categoryName, facetCount: facetCount};
									currentNode = newNode.children;						
								}
							}
						}
					}
					}
				var expandTree = false;
				
				//TISCF-4 Start
				//The Department Hierarchy Tree should always remain Closed for Both PLP and SERP
//				if(output.length == 2) {
//					expandTree = true;
//				}
				//TISCF-4 End
				
				//TISPT-304 starts
				
				$( ".serpProduct" ).each(function( index ) {
					var product=$(this).closest('span').find('#productCode').val();
					 // console.log("prod"+product);
					  var categoryTypeValue=$(this).closest('span').find('#categoryType').val()
					 //  console.log("categoryTypeValue"+categoryTypeValue);
					  var productUrl=$(this).closest('span').find('#productUrl').val();
					 //console.log("productUrl"+productUrl);
					  var productPrice=$(this).closest('span').find('#productPrice').val();
					//  console.log("productPrice"+productPrice);
					  var list=$(this).closest('span').find('#list').val();
					//  console.log("list"+list);
					  var mrpPriceValue=$(this).closest('span').find('#mrpPriceValue').val();
					//  console.log("mrpPriceValue"+mrpPriceValue);
					  var sizeStockLevel=$(this).closest('span').find('#sizeStockLevel').val();
					 // console.log("sizeStockLevel"+sizeStockLevel);
					  var productPromotion=$(this).closest('span').find('#productPromotion').val();
					 // console.log("productPromotion"+productPromotion);
					  populateFacet();
					  if(typeof(serpSizeList)!= "undefined"){
						modifySERPDetailsByFilters(serpSizeList,product,categoryTypeValue,list,productUrl,productPrice,mrpPriceValue,sizeStockLevel,productPromotion);
					 } 
					});
				
				//TISPT-304 ends
				
				
				if($('#isCategoryPage').val() == 'true'){	
					// Assign tree object to category page
					$("#categoryPageDeptHierTree").tree({
						data: output,
						 openedIcon:'',
						 openedIcon: '',
						//TISCF-4 Start
						//autoOpen: true
						//The Department Hierarchy Tree should always remain Closed for Both PLP and SERP
						autoOpen: true
						//TISCF-4 End
				
					});
				}else {
					// Assign tree object to search page
					$("#searchPageDeptHierTree").tree({
						data: output,
						 closedIcon:'',
						 openedIcon:'',
						autoOpen: true
				
					});
					
					// persist search text in search text box
					 var isConceirge = $('#isConceirge').val();
						if(isConceirge!='true') {
						ACC.autocomplete.bindSearchText($('#text').val());
						}
				}
				
				$('#categoryPageDeptHierTree').bind(
						'tree.click',
						function(event) {
							var node = event.node;
							if(node.categoryType != 'All') {
								var actionText = ACC.config.contextPath;
								actionText = (actionText + '/Categories/' + node.name + '/c-' + node.categoryCode);
								$('#categoryPageDeptHierTreeForm').attr('action',actionText);
								
								$('#categoryPageDeptHierTreeForm').submit();
							}
						}
				);
				
				$('#searchPageDeptHierTree').bind(
						'tree.click',
						function(event) {
							var node = event.node;
							var searchQuery = document.getElementById("q").value;				
							if(node.categoryType == 'All') {
								$('#q').val($('#text').val() + ":relevance");
								$('#searchCategoryTree').val("all");
							}
							else{
								//Changes Added for TOR-488
								//$('#q').val($('#text').val() + ":relevance:category:" + node.categoryCode);
								//$('#searchCategoryTree').val(node.categoryCode);
								// alert($('#q').val());
								 //TISQAEE-14
								 if($('#q').val().indexOf(node.categoryCode)==-1){
									//INC_11754 start
									 if(node.categoryCode.indexOf($('#searchCategory').val())==-1){
										 $('#q').val(searchQuery +":category:" + node.categoryCode);
									 }else{			
									 	 $('#q').val($('#text').val() + ":relevance:category:" + node.categoryCode);
									 }
									 //INC_11754 end
								 }
								 $('#searchCategoryTree').val(node.categoryCode);
								
							} 
							
							$('#searchPageDeptHierTreeForm').submit();
							
						}
				);
					
				}
		</script>
