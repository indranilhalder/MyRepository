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
					 //Gigya Call Closed due to new Social Login
// 					 callGigya();
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
						$(".homepage-banner #rotatingImageTimeout").owlCarousel({
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
							$(".homepage-banner #rotatingImageTimeout img").each(function() {
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
							
							$(".homepage-banner #rotatingImageTimeoutMobile").owlCarousel({
								
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
								$(".homepage-banner #rotatingImageTimeoutMobile img").each(function() {
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
			
			
			//SERP added for SNS and Dept hierarchy 
			function constructDepartmentHierarchy(e){var t=[];if(""!=e)for(var n=0;n<e.length;n++){var o=e[n].split("|"),r=t;0==n&&""==$("#isCategoryPage").val()&&(t[0]={label:"All",children:[],categoryCode:"",categoryType:"All",categoryName:""});for(var i=0;i<o.length;i++)if(null!=o[i]&&o[i].length>0){var s=o[i].split(":"),a=s[0],d=s[1],l=0;("L3"==s[2]||"L4"==s[2])&&(l="  ("+s[5]+")");var u="category";"true"==s[3]&&(u="department");for(var h=r,p=0;p<r.length;p++)if(r[p].categoryName==d){r=r[p].children;break}if(h==r){var c=r[p]={label:d,children:[],categoryCode:a,categoryType:u,categoryName:d,facetCount:l};r=c.children}}}if($(".serpProduct").each(function(e){var t=$(this).closest("span").find("#productCode").val(),n=$(this).closest("span").find("#categoryType").val(),o=$(this).closest("span").find("#productUrl").val(),r=$(this).closest("span").find("#productPrice").val(),i=$(this).closest("span").find("#list").val(),s=$(this).closest("span").find("#mrpPriceValue").val(),a=$(this).closest("span").find("#sizeStockLevel").val(),d=$(this).closest("span").find("#productPromotion").val();populateFacet(),"undefined"!=typeof serpSizeList&&modifySERPDetailsByFilters(serpSizeList,t,n,i,o,r,s,a,d)}),"true"==$("#isCategoryPage").val())$("#categoryPageDeptHierTree").tree({data:t,openedIcon:"",openedIcon:"",autoOpen:!0});else{$("#searchPageDeptHierTree").tree({data:t,closedIcon:"",openedIcon:"",autoOpen:!0});var _=$("#isConceirge").val();"true"!=_&&autosearch($("#text").val())}$("#categoryPageDeptHierTree").bind("tree.click",function(e){var t=e.node;if("All"!=t.categoryType){var n=ACC.config.contextPath;n=n+"/Categories/"+t.name+"/c-"+t.categoryCode,$("#categoryPageDeptHierTreeForm").attr("action",n),$("#categoryPageDeptHierTreeForm").submit()}}),$("#searchPageDeptHierTree").bind("tree.click",function(e){var t=e.node,n=document.getElementById("q").value;"All"==t.categoryType?($("#q").val($("#text").val()+":relevance"),$("#searchCategoryTree").val("all")):(-1==$("#q").val().indexOf(t.categoryCode)&&(-1==t.categoryCode.indexOf($("#searchCategory").val())?$("#q").val(n+":category:"+t.categoryCode):$("#q").val($("#text").val()+":relevance:category:"+t.categoryCode)),$("#searchCategoryTree").val(t.categoryCode)),$("#searchPageDeptHierTreeForm").submit()})}function populateFacet(){var e=-1,t=0,n=$("#sizeCountForAppliedFilter").val(),o=$("#searchQueryForAppliedFilter").val();if("undefined"!=typeof o)for(var r=o.split(":"),i=0;i<r.length;i++)if("size"==r[i])t++;else if(t>=1&&(serpSizeList[++e]=r[i],t==n))break}function modifySERPDetailsByFilters(e,t,n,o,r,s,a,d,l,u){if(("Apparel"==n||"Footwear"==n)&&""!=e){var p=checkSizeCount(o,e),c=s.replace("[[",""),_=c.replace("]]",""),f=new Array;f=_.split(",");var g=[],m=-1,v="",y="";for(k=0;k<f.length;k++)if(""!=f[k]){var N=JSON.parse(f[k]);if(""!=p)v=p,void 0!=N[p]&&(y=N[p]);else for(h=0;h<e.length;h++){var w=e[h];void 0!=N[w]&&(v=w,g[++m]=parseInt(N[w]))}}""==p&&(""!=g||g!=[])&&(g.sort(function(e,t){return e-t}),y=g[0],v=findSizeBasedOnMinPrice(y,f)),void 0!=y&&($("#price_"+t).html(""),$("#price_"+t).html("&#8377;"+y)),updateProductMrp(a,p,e,v,y,t),updateProductStock(d,p,e,v,t),"undefined"!=typeof l&&""!=l&&findOnSaleBasedOnMinPrice(l,o,e,t);var S=r.replace("[[",""),C=S.replace("]]",""),F=new Array;for(F=C.split(","),i=0;i<F.length;i++)if(""!=F[i]){var N=JSON.parse(F[i]);if(""!=p)void 0!=N[p]&&($(".thumb_"+t).attr("href",ACC.config.encodedContextPath+"/p"+N[p]),$(".name_"+t).attr("href",ACC.config.encodedContextPath+"/p"+N[p]),$("#quickview_"+t).attr("href",ACC.config.encodedContextPath+"/p"+N[p]+"/quickView"));else for(j=0;j<e.length;j++){var D=e[j];void 0!=N[D]&&v==e[j]&&($(".thumb_"+t).attr("href",ACC.config.encodedContextPath+"/p"+N[v]),$(".name_"+t).attr("href",ACC.config.encodedContextPath+"/p"+N[v]),$("#quickview_"+t).attr("href",ACC.config.encodedContextPath+"/p"+N[p]))}}}}function autosearch(e){var t=e.match(/,/g),n=document.getElementById("spellingSearchterm").value;(null==t||t.length<=2)&&(""==n||null==n?$("#js-site-search-input").val(e):""!=n||null!=n&&e!=n?$("#js-site-search-input").val(n):$("#js-site-search-input").val(e))}var serpSizeList=[];!function e(t,n,o){function r(s,a){if(!n[s]){if(!t[s]){var d="function"==typeof require&&require;if(!a&&d)return d(s,!0);if(i)return i(s,!0);var l=new Error("Cannot find module '"+s+"'");throw l.code="MODULE_NOT_FOUND",l}var u=n[s]={exports:{}};t[s][0].call(u.exports,function(e){var n=t[s][1][e];return r(n?n:e)},u,u.exports,e,t,n,o)}return n[s].exports}for(var i="function"==typeof require&&require,s=0;s<o.length;s++)r(o[s]);return r}({1:[function(e,t,n){var o,r,i,s,a,d,l=function(e,t){function n(){this.constructor=e}for(var o in t)u.call(t,o)&&(e[o]=t[o]);return n.prototype=t.prototype,e.prototype=new n,e.__super__=t.prototype,e},u={}.hasOwnProperty;d=e("./node"),s=d.Position,o=function(){function e(e){this.tree_widget=e,this.hovered_area=null,this.$ghost=null,this.hit_areas=[],this.is_dragging=!1,this.current_item=null}return e.prototype.mouseCapture=function(e){var t,n;return t=$(e.target),this.mustCaptureElement(t)?this.tree_widget.options.onIsMoveHandle&&!this.tree_widget.options.onIsMoveHandle(t)?null:(n=this.tree_widget._getNodeElement(t),n&&this.tree_widget.options.onCanMove&&(this.tree_widget.options.onCanMove(n.node)||(n=null)),this.current_item=n,null!==this.current_item):null},e.prototype.mouseStart=function(e){var t;return this.refresh(),t=$(e.target).offset(),this.drag_element=new r(this.current_item.node,e.page_x-t.left,e.page_y-t.top,this.tree_widget.element),this.is_dragging=!0,this.current_item.$element.addClass("jqtree-moving"),!0},e.prototype.mouseDrag=function(e){var t,n;return this.drag_element.move(e.page_x,e.page_y),t=this.findHoveredArea(e.page_x,e.page_y),n=this.canMoveToArea(t),n&&t?(t.node.isFolder()||this.stopOpenFolderTimer(),this.hovered_area!==t&&(this.hovered_area=t,this.mustOpenFolderTimer(t)?this.startOpenFolderTimer(t.node):this.stopOpenFolderTimer(),this.updateDropHint())):(this.removeHover(),this.removeDropHint(),this.stopOpenFolderTimer()),!0},e.prototype.mustCaptureElement=function(e){return!e.is("input,select")},e.prototype.canMoveToArea=function(e){var t;return e?this.tree_widget.options.onCanMoveTo?(t=s.getName(e.position),this.tree_widget.options.onCanMoveTo(this.current_item.node,e.node,t)):!0:!1},e.prototype.mouseStop=function(e){return this.moveItem(e),this.clear(),this.removeHover(),this.removeDropHint(),this.removeHitAreas(),this.current_item&&(this.current_item.$element.removeClass("jqtree-moving"),this.current_item=null),this.is_dragging=!1,!1},e.prototype.refresh=function(){return this.removeHitAreas(),this.current_item&&(this.generateHitAreas(),this.current_item=this.tree_widget._getNodeElementForNode(this.current_item.node),this.is_dragging)?this.current_item.$element.addClass("jqtree-moving"):void 0},e.prototype.removeHitAreas=function(){return this.hit_areas=[]},e.prototype.clear=function(){return this.drag_element.remove(),this.drag_element=null},e.prototype.removeDropHint=function(){return this.previous_ghost?this.previous_ghost.remove():void 0},e.prototype.removeHover=function(){return this.hovered_area=null},e.prototype.generateHitAreas=function(){var e;return e=new i(this.tree_widget.tree,this.current_item.node,this.getTreeDimensions().bottom),this.hit_areas=e.generate()},e.prototype.findHoveredArea=function(e,t){var n,o,r,i,s;if(o=this.getTreeDimensions(),e<o.left||t<o.top||e>o.right||t>o.bottom)return null;for(i=0,r=this.hit_areas.length;r>i;)if(s=i+r>>1,n=this.hit_areas[s],t<n.top)r=s;else{if(!(t>n.bottom))return n;i=s+1}return null},e.prototype.mustOpenFolderTimer=function(e){var t;return t=e.node,t.isFolder()&&!t.is_open&&e.position===s.INSIDE},e.prototype.updateDropHint=function(){var e;if(this.hovered_area)return this.removeDropHint(),e=this.tree_widget._getNodeElementForNode(this.hovered_area.node),this.previous_ghost=e.addDropHint(this.hovered_area.position)},e.prototype.startOpenFolderTimer=function(e){var t;return t=function(t){return function(){return t.tree_widget._openNode(e,t.tree_widget.options.slide,function(){return t.refresh(),t.updateDropHint()})}}(this),this.stopOpenFolderTimer(),this.open_folder_timer=setTimeout(t,this.tree_widget.options.openFolderDelay)},e.prototype.stopOpenFolderTimer=function(){return this.open_folder_timer?(clearTimeout(this.open_folder_timer),this.open_folder_timer=null):void 0},e.prototype.moveItem=function(e){var t,n,o,r,i,a;return this.hovered_area&&this.hovered_area.position!==s.NONE&&this.canMoveToArea(this.hovered_area)&&(o=this.current_item.node,a=this.hovered_area.node,r=this.hovered_area.position,i=o.parent,r===s.INSIDE&&(this.hovered_area.node.is_open=!0),t=function(e){return function(){return e.tree_widget.tree.moveNode(o,a,r),e.tree_widget.element.empty(),e.tree_widget._refreshElements()}}(this),n=this.tree_widget._triggerEvent("tree.move",{move_info:{moved_node:o,target_node:a,position:s.getName(r),previous_parent:i,do_move:t,original_event:e.original_event}}),!n.isDefaultPrevented())?t():void 0},e.prototype.getTreeDimensions=function(){var e;return e=this.tree_widget.element.offset(),{left:e.left,top:e.top,right:e.left+this.tree_widget.element.width(),bottom:e.top+this.tree_widget.element.height()+16}},e}(),a=function(){function e(e){this.tree=e}return e.prototype.iterate=function(){var e,t;return t=!0,(e=function(n){return function(o,r){var i,s,a,d,l,u,h,p;if(h=(o.is_open||!o.element)&&o.hasChildren(),o.element){if(i=$(o.element),!i.is(":visible"))return;t&&(n.handleFirstNode(o,i),t=!1),o.hasChildren()?o.is_open?n.handleOpenFolder(o,i)||(h=!1):n.handleClosedFolder(o,r,i):n.handleNode(o,r,i)}if(h){for(a=o.children.length,p=o.children,d=l=0,u=p.length;u>l;d=++l)s=p[d],d===a-1?e(o.children[d],null):e(o.children[d],o.children[d+1]);if(o.is_open)return n.handleAfterOpenFolder(o,r,i)}}}(this))(this.tree,null)},e.prototype.handleNode=function(e,t,n){},e.prototype.handleOpenFolder=function(e,t){},e.prototype.handleClosedFolder=function(e,t,n){},e.prototype.handleAfterOpenFolder=function(e,t,n){},e.prototype.handleFirstNode=function(e,t){},e}(),i=function(e){function t(e,n,o){t.__super__.constructor.call(this,e),this.current_node=n,this.tree_bottom=o}return l(t,e),t.prototype.generate=function(){return this.positions=[],this.last_top=0,this.iterate(),this.generateHitAreas(this.positions)},t.prototype.getTop=function(e){return e.offset().top},t.prototype.addPosition=function(e,t,n){var o;return o={top:n,node:e,position:t},this.positions.push(o),this.last_top=n},t.prototype.handleNode=function(e,t,n){var o;return o=this.getTop(n),e===this.current_node?this.addPosition(e,s.NONE,o):this.addPosition(e,s.INSIDE,o),t===this.current_node||e===this.current_node?this.addPosition(e,s.NONE,o):this.addPosition(e,s.AFTER,o)},t.prototype.handleOpenFolder=function(e,t){return e===this.current_node?!1:(e.children[0]!==this.current_node&&this.addPosition(e,s.INSIDE,this.getTop(t)),!0)},t.prototype.handleClosedFolder=function(e,t,n){var o;return o=this.getTop(n),e===this.current_node?this.addPosition(e,s.NONE,o):(this.addPosition(e,s.INSIDE,o),t!==this.current_node?this.addPosition(e,s.AFTER,o):void 0)},t.prototype.handleFirstNode=function(e,t){return e!==this.current_node?this.addPosition(e,s.BEFORE,this.getTop($(e.element))):void 0},t.prototype.handleAfterOpenFolder=function(e,t,n){return e===this.current_node.node||t===this.current_node.node?this.addPosition(e,s.NONE,this.last_top):this.addPosition(e,s.AFTER,this.last_top)},t.prototype.generateHitAreas=function(e){var t,n,o,r,i,s;for(s=-1,t=[],n=[],o=0,r=e.length;r>o;o++)i=e[o],i.top!==s&&t.length&&(t.length&&this.generateHitAreasForGroup(n,t,s,i.top),s=i.top,t=[]),t.push(i);return this.generateHitAreasForGroup(n,t,s,this.tree_bottom),n},t.prototype.generateHitAreasForGroup=function(e,t,n,o){var r,i,s,a,d;for(d=Math.min(t.length,4),r=Math.round((o-n)/d),i=n,s=0;d>s;)a=t[s],e.push({top:i,bottom:i+r,node:a.node,position:a.position}),i+=r,s+=1;return null},t}(a),r=function(){function e(e,t,n,o){this.offset_x=t,this.offset_y=n,this.$element=$('<span class="jqtree-title jqtree-dragging">'+e.name+"</span>"),this.$element.css("position","absolute"),o.append(this.$element)}return e.prototype.move=function(e,t){return this.$element.offset({left:e-this.offset_x,top:t-this.offset_y})},e.prototype.remove=function(){return this.$element.remove()},e}(),t.exports=o},{"./node":5}],2:[function(e,t,n){var o,r,i,s,a;s=e("./node_element"),r=s.NodeElement,a=e("./util"),i=a.html_escape,o=function(){function e(e){this.tree_widget=e,this.opened_icon_element=this.createButtonElement(e.options.openedIcon),this.closed_icon_element=this.createButtonElement(e.options.closedIcon)}return e.prototype.render=function(e){return e&&e.parent?this.renderFromNode(e):this.renderFromRoot()},e.prototype.renderFromRoot=function(){var e;return e=this.tree_widget.element,e.empty(),this.createDomElements(e[0],this.tree_widget.tree.children,!0,!0)},e.prototype.renderFromNode=function(e){var t,n;return t=$(e.element),n=this.createLi(e),this.attachNodeData(e,n),t.after(n),t.remove(),e.children?this.createDomElements(n,e.children,!1,!1):void 0},e.prototype.createDomElements=function(e,t,n,o){var r,i,s,a,d;for(d=this.createUl(n),e.appendChild(d),i=0,s=t.length;s>i;i++)r=t[i],a=this.createLi(r),d.appendChild(a),this.attachNodeData(r,a),r.hasChildren()&&this.createDomElements(a,r.children,!1,r.is_open);return null},e.prototype.attachNodeData=function(e,t){return e.element=t,$(t).data("node",e)},e.prototype.createUl=function(e){var t,n;return t=e?"jqtree-tree":"",n=document.createElement("ul"),n.className="jqtree_common "+t,n},e.prototype.createLi=function(e){var t;return t=e.isFolder()?this.createFolderLi(e):this.createNodeLi(e),this.tree_widget.options.onCreateLi&&this.tree_widget.options.onCreateLi(e,$(t)),t},e.prototype.createFolderLi=function(e){var t,n,o,r,i,s,a,d;return t=this.getButtonClasses(e),i=this.getFolderClasses(e),r=this.escapeIfNecessary(e.name),s=e.is_open?this.opened_icon_element:this.closed_icon_element,a=document.createElement("li"),a.className="jqtree_common "+i,o=document.createElement("div"),o.className="jqtree-element jqtree_common",a.appendChild(o),n=document.createElement("a"),n.className="jqtree_common "+t,n.appendChild(s.cloneNode(!1)),o.appendChild(n),d=document.createElement("span"),d.className="jqtree_common jqtree-title jqtree-title-folder",o.appendChild(d),d.innerHTML=r,a},e.prototype.createNodeLi=function(e){var t,n,o,r,i,s;return i=["jqtree_common"],this.tree_widget.select_node_handler&&this.tree_widget.select_node_handler.isNodeSelected(e)&&i.push("jqtree-selected"),t=i.join(" "),o=this.escapeIfNecessary(e.name),r=document.createElement("li"),r.className=t,n=document.createElement("div"),n.className="jqtree-element jqtree_common",r.appendChild(n),s=document.createElement("span"),s.className="jqtree-title jqtree_common",s.innerHTML=o,n.appendChild(s),facetCount_span=document.createElement("span"),facetCount_span.className="jqtree-facetCount jqtree_common",facetCount_span.innerHTML=e.facetCount,n.appendChild(facetCount_span),r},e.prototype.getButtonClasses=function(e){var t;return t=["jqtree-toggler"],e.is_open||t.push("jqtree-closed"),t.join(" ")},e.prototype.getFolderClasses=function(e){var t;return t=["jqtree-folder"],e.is_open||t.push("jqtree-closed"),this.tree_widget.select_node_handler&&this.tree_widget.select_node_handler.isNodeSelected(e)&&t.push("jqtree-selected"),e.is_loading&&t.push("jqtree-loading"),t.join(" ")},e.prototype.escapeIfNecessary=function(e){return this.tree_widget.options.autoEscape?i(e):e},e.prototype.createButtonElement=function(e){var t;return"string"==typeof e?(t=document.createElement("div"),t.innerHTML=e,document.createTextNode(t.innerHTML)):$(e)[0]},e}(),t.exports=o},{"./node_element":6,"./util":12}],3:[function(e,t,n){var o,r=function(e,t){return function(){return e.apply(t,arguments)}};o=function(){function e(e){this.selectNode=r(this.selectNode,this),this.tree_widget=e,e.options.keyboardSupport&&$(document).bind("keydown.jqtree",$.proxy(this.handleKeyDown,this))}var t,n,o,i;return n=37,i=38,o=39,t=40,e.prototype.deinit=function(){return $(document).unbind("keydown.jqtree")},e.prototype.moveDown=function(){var e;return e=this.tree_widget.getSelectedNode(),e?this.selectNode(e.getNextNode()):!1},e.prototype.moveUp=function(){var e;return e=this.tree_widget.getSelectedNode(),e?this.selectNode(e.getPreviousNode()):!1},e.prototype.moveRight=function(){var e;return e=this.tree_widget.getSelectedNode(),e&&e.isFolder()&&!e.is_open?(this.tree_widget.openNode(e),!1):!0},e.prototype.moveLeft=function(){var e;return e=this.tree_widget.getSelectedNode(),e&&e.isFolder()&&e.is_open?(this.tree_widget.closeNode(e),!1):!0},e.prototype.handleKeyDown=function(e){var r;if(!this.tree_widget.options.keyboardSupport)return!0;if($(document.activeElement).is("textarea,input,select"))return!0;if(!this.tree_widget.getSelectedNode())return!0;switch(r=e.which){case t:return this.moveDown();case i:return this.moveUp();case o:return this.moveRight();case n:return this.moveLeft()}return!0},e.prototype.selectNode=function(e){return e?(this.tree_widget.selectNode(e),this.tree_widget.scroll_handler&&!this.tree_widget.scroll_handler.isScrolledIntoView($(e.element).find(".jqtree-element"))&&this.tree_widget.scrollToNode(e),!1):!0},e}(),t.exports=o},{}],4:[function(e,t,n){var o,r,i=function(e,t){function n(){this.constructor=e}for(var o in t)s.call(t,o)&&(e[o]=t[o]);return n.prototype=t.prototype,e.prototype=new n,e.__super__=t.prototype,e},s={}.hasOwnProperty;r=e("./simple.widget"),o=function(e){function t(){return t.__super__.constructor.apply(this,arguments)}return i(t,e),t.is_mouse_handled=!1,t.prototype._init=function(){return this.$el.bind("mousedown.mousewidget",$.proxy(this._mouseDown,this)),this.$el.bind("touchstart.mousewidget",$.proxy(this._touchStart,this)),this.is_mouse_started=!1,this.mouse_delay=0,this._mouse_delay_timer=null,this._is_mouse_delay_met=!0,this.mouse_down_info=null},t.prototype._deinit=function(){var e;return this.$el.unbind("mousedown.mousewidget"),this.$el.unbind("touchstart.mousewidget"),e=$(document),e.unbind("mousemove.mousewidget"),e.unbind("mouseup.mousewidget")},t.prototype._mouseDown=function(e){var t;if(1===e.which)return t=this._handleMouseDown(e,this._getPositionInfo(e)),t&&e.preventDefault(),t},t.prototype._handleMouseDown=function(e,n){return!t.is_mouse_handled&&(this.is_mouse_started&&this._handleMouseUp(n),this.mouse_down_info=n,this._mouseCapture(n))?(this._handleStartMouse(),this.is_mouse_handled=!0,!0):void 0},t.prototype._handleStartMouse=function(){var e;return e=$(document),e.bind("mousemove.mousewidget",$.proxy(this._mouseMove,this)),e.bind("touchmove.mousewidget",$.proxy(this._touchMove,this)),e.bind("mouseup.mousewidget",$.proxy(this._mouseUp,this)),e.bind("touchend.mousewidget",$.proxy(this._touchEnd,this)),this.mouse_delay?this._startMouseDelayTimer():void 0},t.prototype._startMouseDelayTimer=function(){return this._mouse_delay_timer&&clearTimeout(this._mouse_delay_timer),this._mouse_delay_timer=setTimeout(function(e){return function(){return e._is_mouse_delay_met=!0}}(this),this.mouse_delay),this._is_mouse_delay_met=!1},t.prototype._mouseMove=function(e){return this._handleMouseMove(e,this._getPositionInfo(e))},t.prototype._handleMouseMove=function(e,t){return this.is_mouse_started?(this._mouseDrag(t),e.preventDefault()):this.mouse_delay&&!this._is_mouse_delay_met?!0:(this.is_mouse_started=this._mouseStart(this.mouse_down_info)!==!1,this.is_mouse_started?this._mouseDrag(t):this._handleMouseUp(t),!this.is_mouse_started)},t.prototype._getPositionInfo=function(e){return{page_x:e.pageX,page_y:e.pageY,target:e.target,original_event:e}},t.prototype._mouseUp=function(e){return this._handleMouseUp(this._getPositionInfo(e))},t.prototype._handleMouseUp=function(e){var t;t=$(document),t.unbind("mousemove.mousewidget"),t.unbind("touchmove.mousewidget"),t.unbind("mouseup.mousewidget"),t.unbind("touchend.mousewidget"),this.is_mouse_started&&(this.is_mouse_started=!1,this._mouseStop(e))},t.prototype._mouseCapture=function(e){return!0},t.prototype._mouseStart=function(e){return null},t.prototype._mouseDrag=function(e){return null},t.prototype._mouseStop=function(e){return null},t.prototype.setMouseDelay=function(e){return this.mouse_delay=e},t.prototype._touchStart=function(e){var t;if(!(e.originalEvent.touches.length>1))return t=e.originalEvent.changedTouches[0],this._handleMouseDown(e,this._getPositionInfo(t))},t.prototype._touchMove=function(e){var t;if(!(e.originalEvent.touches.length>1))return t=e.originalEvent.changedTouches[0],this._handleMouseMove(e,this._getPositionInfo(t))},t.prototype._touchEnd=function(e){var t;if(!(e.originalEvent.touches.length>1))return t=e.originalEvent.changedTouches[0],this._handleMouseUp(this._getPositionInfo(t))},t}(r),t.exports=o},{"./simple.widget":10}],5:[function(e,t,n){var o,r;r={getName:function(e){return r.strings[e-1]},nameToIndex:function(e){var t,n,o;for(t=n=1,o=r.strings.length;o>=1?o>=n:n>=o;t=o>=1?++n:--n)if(r.strings[t-1]===e)return t;return 0}},r.BEFORE=1,r.AFTER=2,r.INSIDE=3,r.NONE=4,r.strings=["before","after","inside","none"],o=function(){function e(t,n,o){null==n&&(n=!1),null==o&&(o=e),this.setData(t),this.children=[],this.parent=null,n&&(this.id_mapping={},this.tree=this,this.node_class=o)}return e.prototype.setData=function(e){var t,n;if("object"!=typeof e)this.name=e;else for(t in e)n=e[t],"label"===t?this.name=n:this[t]=n;return null},e.prototype.initFromData=function(e){var t,n;return n=function(e){return function(n){return e.setData(n),n.children?t(n.children):void 0}}(this),t=function(e){return function(t){var n,o,r,i;for(o=0,r=t.length;r>o;o++)n=t[o],i=new e.tree.node_class(""),i.initFromData(n),e.addChild(i);return null}}(this),n(e),null},e.prototype.loadFromData=function(e){var t,n,o,r;for(this.removeChildren(),t=0,n=e.length;n>t;t++)r=e[t],o=new this.tree.node_class(r),this.addChild(o),"object"==typeof r&&r.children&&o.loadFromData(r.children);return null},e.prototype.addChild=function(e){return this.children.push(e),e._setParent(this)},e.prototype.addChildAtPosition=function(e,t){return this.children.splice(t,0,e),e._setParent(this)},e.prototype._setParent=function(e){return this.parent=e,this.tree=e.tree,this.tree.addNodeToIndex(this)},e.prototype.removeChild=function(e){return e.removeChildren(),this._removeChild(e)},e.prototype._removeChild=function(e){return this.children.splice(this.getChildIndex(e),1),this.tree.removeNodeFromIndex(e)},e.prototype.getChildIndex=function(e){return $.inArray(e,this.children)},e.prototype.hasChildren=function(){return 0!==this.children.length},e.prototype.isFolder=function(){return this.hasChildren()||this.load_on_demand},e.prototype.iterate=function(e){var t;return t=function(n,o){var r,i,s,a,d;if(n.children){for(a=n.children,i=0,s=a.length;s>i;i++)r=a[i],d=e(r,o),d&&r.hasChildren()&&t(r,o+1);return null}},t(this,0),null},e.prototype.moveNode=function(e,t,n){return e.isParentOf(t)?void 0:(e.parent._removeChild(e),n===r.AFTER?t.parent.addChildAtPosition(e,t.parent.getChildIndex(t)+1):n===r.BEFORE?t.parent.addChildAtPosition(e,t.parent.getChildIndex(t)):n===r.INSIDE?t.addChildAtPosition(e,0):void 0)},e.prototype.getData=function(){var e;return(e=function(t){return function(t){var n,o,r,i,s,a,d;for(n=[],o=0,i=t.length;i>o;o++){s=t[o],a={};for(r in s)d=s[r],"parent"!==r&&"children"!==r&&"element"!==r&&"tree"!==r&&Object.prototype.hasOwnProperty.call(s,r)&&(a[r]=d);s.hasChildren()&&(a.children=e(s.children)),n.push(a)}return n}}(this))(this.children)},e.prototype.getNodeByName=function(e){var t;return t=null,this.iterate(function(n){return n.name===e?(t=n,!1):!0}),t},e.prototype.addAfter=function(e){var t,n;return this.parent?(n=new this.tree.node_class(e),t=this.parent.getChildIndex(this),this.parent.addChildAtPosition(n,t+1),n):null},e.prototype.addBefore=function(e){var t,n;return this.parent?(n=new this.tree.node_class(e),t=this.parent.getChildIndex(this),this.parent.addChildAtPosition(n,t),n):null},e.prototype.addParent=function(e){var t,n,o,r,i,s;if(this.parent){for(r=new this.tree.node_class(e),r._setParent(this.tree),i=this.parent,s=i.children,n=0,o=s.length;o>n;n++)t=s[n],r.addChild(t);return i.children=[],i.addChild(r),r}return null},e.prototype.remove=function(){return this.parent?(this.parent.removeChild(this),this.parent=null):void 0},e.prototype.append=function(e){var t;return t=new this.tree.node_class(e),this.addChild(t),t},e.prototype.prepend=function(e){var t;return t=new this.tree.node_class(e),this.addChildAtPosition(t,0),t},e.prototype.isParentOf=function(e){var t;for(t=e.parent;t;){if(t===this)return!0;t=t.parent}return!1},e.prototype.getLevel=function(){var e,t;for(e=0,t=this;t.parent;)e+=1,t=t.parent;return e},e.prototype.getNodeById=function(e){return this.id_mapping[e]},e.prototype.addNodeToIndex=function(e){return null!=e.id?this.id_mapping[e.id]=e:void 0},e.prototype.removeNodeFromIndex=function(e){return null!=e.id?delete this.id_mapping[e.id]:void 0},e.prototype.removeChildren=function(){return this.iterate(function(e){return function(t){return e.tree.removeNodeFromIndex(t),!0}}(this)),this.children=[]},e.prototype.getPreviousSibling=function(){var e;return this.parent?(e=this.parent.getChildIndex(this)-1,e>=0?this.parent.children[e]:null):null},e.prototype.getNextSibling=function(){var e;return this.parent?(e=this.parent.getChildIndex(this)+1,e<this.parent.children.length?this.parent.children[e]:null):null},e.prototype.getNodesByProperty=function(e,t){return this.filter(function(n){return n[e]===t})},e.prototype.filter=function(e){var t;return t=[],this.iterate(function(n){return e(n)&&t.push(n),!0}),t},e.prototype.getNextNode=function(e){var t;return null==e&&(e=!0),e&&this.hasChildren()&&this.is_open?this.children[0]:this.parent?(t=this.getNextSibling(),t?t:this.parent.getNextNode(!1)):null},e.prototype.getPreviousNode=function(){var e;return this.parent?(e=this.getPreviousSibling(),e?e.hasChildren()&&e.is_open?e.getLastChild():e:this.parent.parent?this.parent:null):null},e.prototype.getLastChild=function(){var e;return this.hasChildren()?(e=this.children[this.children.length-1],e.hasChildren()&&e.is_open?e.getLastChild():e):null},e}(),t.exports={Node:o,Position:r}},{}],6:[function(e,t,n){var o,r,i,s,a,d,l=function(e,t){function n(){this.constructor=e}for(var o in t)u.call(t,o)&&(e[o]=t[o]);return n.prototype=t.prototype,e.prototype=new n,e.__super__=t.prototype,e},u={}.hasOwnProperty;d=e("./node"),a=d.Position,s=function(){function e(e,t){this.init(e,t)}return e.prototype.init=function(e,t){return this.node=e,this.tree_widget=t,e.element||(e.element=this.tree_widget.element),this.$element=$(e.element)},e.prototype.getUl=function(){return this.$element.children("ul:first")},e.prototype.getSpan=function(){return this.$element.children(".jqtree-element").find("span.jqtree-title")},e.prototype.getLi=function(){return this.$element},e.prototype.addDropHint=function(e){return e===a.INSIDE?new o(this.$element):new i(this.node,this.$element,e)},e.prototype.select=function(){return this.getLi().addClass("jqtree-selected")},e.prototype.deselect=function(){return this.getLi().removeClass("jqtree-selected")},e}(),r=function(e){function t(){return t.__super__.constructor.apply(this,arguments)}return l(t,e),t.prototype.open=function(e,t){var n,o;return null==t&&(t=!0),this.node.is_open?void 0:(this.node.is_open=!0,n=this.getButton(),n.removeClass("jqtree-closed"),n.html(""),n.append(this.tree_widget.renderer.opened_icon_element.cloneNode(!1)),o=function(t){return function(){return t.getLi().removeClass("jqtree-closed"),e&&e(),t.tree_widget._triggerEvent("tree.open",{node:t.node})}}(this),t?this.getUl().slideDown("fast",o):(this.getUl().show(),o()))},t.prototype.close=function(e){var t,n;return null==e&&(e=!0),this.node.is_open?(this.node.is_open=!1,t=this.getButton(),t.addClass("jqtree-closed"),t.html(""),t.append(this.tree_widget.renderer.closed_icon_element.cloneNode(!1)),n=function(e){return function(){return e.getLi().addClass("jqtree-closed"),e.tree_widget._triggerEvent("tree.close",{node:e.node})}}(this),e?this.getUl().slideUp("fast",n):(this.getUl().hide(),n())):void 0},t.prototype.getButton=function(){return this.$element.children(".jqtree-element").find("a.jqtree-toggler")},t.prototype.addDropHint=function(e){return this.node.is_open||e!==a.INSIDE?new i(this.node,this.$element,e):new o(this.$element)},t}(s),o=function(){function e(e){var t,n;t=e.children(".jqtree-element"),n=e.width()-4,this.$hint=$('<span class="jqtree-border"></span>'),t.append(this.$hint),this.$hint.css({width:n,height:t.outerHeight()-4})}return e.prototype.remove=function(){return this.$hint.remove()},e}(),i=function(){function e(e,t,n){this.$element=t,this.node=e,this.$ghost=$('<li class="jqtree_common jqtree-ghost"><span class="jqtree_common jqtree-circle"></span><span class="jqtree_common jqtree-line"></span></li>'),n===a.AFTER?this.moveAfter():n===a.BEFORE?this.moveBefore():n===a.INSIDE&&(e.isFolder()&&e.is_open?this.moveInsideOpenFolder():this.moveInside())}return e.prototype.remove=function(){return this.$ghost.remove()},e.prototype.moveAfter=function(){return this.$element.after(this.$ghost)},e.prototype.moveBefore=function(){return this.$element.before(this.$ghost)},e.prototype.moveInsideOpenFolder=function(){return $(this.node.children[0].element).before(this.$ghost)},e.prototype.moveInside=function(){return this.$element.after(this.$ghost),this.$ghost.addClass("jqtree-inside")},e}(),t.exports={FolderElement:r,NodeElement:s}},{"./node":5}],7:[function(e,t,n){var o,r,i,s;s=e("./util"),r=s.indexOf,i=s.isInt,o=function(){function e(e){this.tree_widget=e}return e.prototype.saveState=function(){var e;return e=JSON.stringify(this.getState()),this.tree_widget.options.onSetStateFromStorage?this.tree_widget.options.onSetStateFromStorage(e):this.supportsLocalStorage()?localStorage.setItem(this.getCookieName(),e):$.cookie?($.cookie.raw=!0,$.cookie(this.getCookieName(),e,{path:"/"})):void 0},e.prototype.getStateFromStorage=function(){var e;return e=this._loadFromStorage(),e?this._parseState(e):null},e.prototype._parseState=function(e){var t;return t=$.parseJSON(e),t&&t.selected_node&&i(t.selected_node)&&(t.selected_node=[t.selected_node]),t},e.prototype._loadFromStorage=function(){return this.tree_widget.options.onGetStateFromStorage?this.tree_widget.options.onGetStateFromStorage():this.supportsLocalStorage()?localStorage.getItem(this.getCookieName()):$.cookie?($.cookie.raw=!0,$.cookie(this.getCookieName())):null},e.prototype.getState=function(){var e,t;return e=function(e){return function(){var t;return t=[],e.tree_widget.tree.iterate(function(e){return e.is_open&&e.id&&e.hasChildren()&&t.push(e.id),!0}),t}}(this),t=function(e){return function(){var t;return function(){var e,n,o,r;for(o=this.tree_widget.getSelectedNodes(),r=[],e=0,n=o.length;n>e;e++)t=o[e],r.push(t.id);return r}.call(e)}}(this),{open_nodes:e(),selected_node:t()}},e.prototype.setInitialState=function(e){var t;return e?(t=this._openInitialNodes(e.open_nodes),this._selectInitialNodes(e.selected_node),t):!1},e.prototype._openInitialNodes=function(e){var t,n,o,r,i;for(o=!1,t=0,n=e.length;n>t;t++)i=e[t],r=this.tree_widget.getNodeById(i),r&&(r.load_on_demand?o=!0:r.is_open=!0);return o},e.prototype._selectInitialNodes=function(e){var t,n,o,r,i;for(i=0,t=0,n=e.length;n>t;t++)r=e[t],o=this.tree_widget.getNodeById(r),o&&(i+=1,this.tree_widget.select_node_handler.addToSelection(o));return 0!==i},e.prototype.setInitialStateOnDemand=function(e){return e?this._setInitialStateOnDemand(e.open_nodes,e.selected_node):void 0},e.prototype._setInitialStateOnDemand=function(e,t){var n,o;return o=function(o){return function(){var r,i,s,a,d;for(s=[],r=0,i=e.length;i>r;r++)d=e[r],a=o.tree_widget.getNodeById(d),a?a.is_loading||(a.load_on_demand?n(a):o.tree_widget._openNode(a,!1)):s.push(d);return e=s,o._selectInitialNodes(t)?o.tree_widget._refreshElements():void 0;
			}}(this),n=function(e){return function(t){return e.tree_widget._openNode(t,!1,o)}}(this),o()},e.prototype.getCookieName=function(){return"string"==typeof this.tree_widget.options.saveState?this.tree_widget.options.saveState:"tree"},e.prototype.supportsLocalStorage=function(){var e;return e=function(){var e,t;if("undefined"==typeof localStorage||null===localStorage)return!1;try{t="_storage_test",sessionStorage.setItem(t,!0),sessionStorage.removeItem(t)}catch(n){return e=n,!1}return!0},null==this._supportsLocalStorage&&(this._supportsLocalStorage=e()),this._supportsLocalStorage},e.prototype.getNodeIdToBeSelected=function(){var e;return e=this.getStateFromStorage(),e&&e.selected_node?e.selected_node[0]:null},e}(),t.exports=o},{"./util":12}],8:[function(e,t,n){var o;o=function(){function e(e){this.tree_widget=e,this.previous_top=-1,this._initScrollParent()}return e.prototype._initScrollParent=function(){var e,t,n;return t=function(e){return function(){var t,n,o,r,i,s;if(t=["overflow","overflow-y"],(o=function(e){var n,o,r,i;for(o=0,r=t.length;r>o;o++)if(n=t[o],"auto"===(i=$.css(e,n))||"scroll"===i)return!0;return!1})(e.tree_widget.$el[0]))return e.tree_widget.$el;for(s=e.tree_widget.$el.parents(),r=0,i=s.length;i>r;r++)if(n=s[r],o(n))return $(n);return null}}(this),n=function(e){return function(){return e.scroll_parent_top=0,e.$scroll_parent=null}}(this),"fixed"===this.tree_widget.$el.css("position")&&n(),e=t(),e&&e.length&&"HTML"!==e[0].tagName?(this.$scroll_parent=e,this.scroll_parent_top=this.$scroll_parent.offset().top):n()},e.prototype.checkScrolling=function(){var e;return e=this.tree_widget.dnd_handler.hovered_area,e&&e.top!==this.previous_top?(this.previous_top=e.top,this.$scroll_parent?this._handleScrollingWithScrollParent(e):this._handleScrollingWithDocument(e)):void 0},e.prototype._handleScrollingWithScrollParent=function(e){var t;return t=this.scroll_parent_top+this.$scroll_parent[0].offsetHeight-e.bottom,20>t?(this.$scroll_parent[0].scrollTop+=20,this.tree_widget.refreshHitAreas(),this.previous_top=-1):e.top-this.scroll_parent_top<20?(this.$scroll_parent[0].scrollTop-=20,this.tree_widget.refreshHitAreas(),this.previous_top=-1):void 0},e.prototype._handleScrollingWithDocument=function(e){var t;return t=e.top-$(document).scrollTop(),20>t?$(document).scrollTop($(document).scrollTop()-20):$(window).height()-(e.bottom-$(document).scrollTop())<20?$(document).scrollTop($(document).scrollTop()+20):void 0},e.prototype.scrollTo=function(e){var t;return this.$scroll_parent?this.$scroll_parent[0].scrollTop=e:(t=this.tree_widget.$el.offset().top,$(document).scrollTop(e+t))},e.prototype.isScrolledIntoView=function(e){var t,n,o,r,i;return t=$(e),this.$scroll_parent?(i=0,r=this.$scroll_parent.height(),o=t.offset().top-this.scroll_parent_top,n=o+t.height()):(i=$(window).scrollTop(),r=i+$(window).height(),o=t.offset().top,n=o+t.height()),r>=n&&o>=i},e}(),t.exports=o},{}],9:[function(e,t,n){var o;o=function(){function e(e){this.tree_widget=e,this.clear()}return e.prototype.getSelectedNode=function(){var e;return e=this.getSelectedNodes(),e.length?e[0]:!1},e.prototype.getSelectedNodes=function(){var e,t,n;if(this.selected_single_node)return[this.selected_single_node];n=[];for(e in this.selected_nodes)t=this.tree_widget.getNodeById(e),t&&n.push(t);return n},e.prototype.getSelectedNodesUnder=function(e){var t,n,o;if(this.selected_single_node)return e.isParentOf(this.selected_single_node)?[this.selected_single_node]:[];o=[];for(t in this.selected_nodes)n=this.tree_widget.getNodeById(t),n&&e.isParentOf(n)&&o.push(n);return o},e.prototype.isNodeSelected=function(e){return e.id?this.selected_nodes[e.id]:this.selected_single_node?this.selected_single_node.element===e.element:!1},e.prototype.clear=function(){return this.selected_nodes={},this.selected_single_node=null},e.prototype.removeFromSelection=function(e,t){if(null==t&&(t=!1),e.id){if(delete this.selected_nodes[e.id],t)return e.iterate(function(t){return function(n){return delete t.selected_nodes[e.id],!0}}(this))}else if(this.selected_single_node&&e.element===this.selected_single_node.element)return this.selected_single_node=null},e.prototype.addToSelection=function(e){return e.id?this.selected_nodes[e.id]=!0:this.selected_single_node=e},e}(),t.exports=o},{}],10:[function(e,t,n){var o,r=[].slice;o=function(){function e(e,t){this.$el=$(e),this.options=$.extend({},this.defaults,t)}return e.prototype.defaults={},e.prototype.destroy=function(){return this._deinit()},e.prototype._init=function(){return null},e.prototype._deinit=function(){return null},e.register=function(t,n){var o,i,s,a,d;return a=function(){return"simple_widget_"+n},d=function(t,n){var o;return o=$.data(t,n),o&&o instanceof e?o:null},i=function(e,n){var o,r,i,s,l,u;for(o=a(),s=0,l=e.length;l>s;s++)r=e[s],i=d(r,o),i||(u=new t(r,n),$.data(r,o)||$.data(r,o,u),u._init());return e},s=function(e){var t,n,o,r,i,s;for(t=a(),i=[],o=0,r=e.length;r>o;o++)n=e[o],s=d(n,t),s&&s.destroy(),i.push($.removeData(n,t));return i},o=function(t,n,o){var r,i,s,d,l,u;for(d=null,i=0,s=t.length;s>i;i++)r=t[i],l=$.data(r,a()),l&&l instanceof e&&(u=l[n],u&&"function"==typeof u&&(d=u.apply(l,o)));return d},$.fn[n]=function(){var e,n,a,d,l;return a=arguments[0],n=2<=arguments.length?r.call(arguments,1):[],e=this,void 0===a||"object"==typeof a?(l=a,i(e,l)):"string"==typeof a&&"_"!==a[0]?(d=a,"destroy"===d?s(e):"get_widget_class"===d?t:o(e,d,n)):void 0}},e}(),t.exports=o},{}],11:[function(e,t,n){var o,r,i,s,a,d,l,u,h,p,c,_,f,g,m,v,y,N=function(e,t){function n(){this.constructor=e}for(var o in t)w.call(t,o)&&(e[o]=t[o]);return n.prototype=t.prototype,e.prototype=new n,e.__super__=t.prototype,e},w={}.hasOwnProperty;g=e("./version"),o=e("./drag_and_drop_handler"),r=e("./elements_renderer"),a=e("./key_handler"),d=e("./mouse.widget"),p=e("./save_state_handler"),c=e("./scroll_handler"),_=e("./select_node_handler"),f=e("./simple.widget"),v=e("./node"),l=v.Node,h=v.Position,y=e("./util"),m=e("./node_element"),u=m.NodeElement,i=m.FolderElement,s=function(e){function t(){return t.__super__.constructor.apply(this,arguments)}return N(t,e),t.prototype.defaults={autoOpen:!1,saveState:!1,dragAndDrop:!1,selectable:!0,useContextMenu:!0,onCanSelectNode:null,onSetStateFromStorage:null,onGetStateFromStorage:null,onCreateLi:null,onIsMoveHandle:null,onCanMove:null,onCanMoveTo:null,onLoadFailed:null,autoEscape:!0,dataUrl:null,closedIcon:"&#x25ba;",openedIcon:"&#x25bc;",slide:!0,nodeClass:l,dataFilter:null,keyboardSupport:!0,openFolderDelay:500},t.prototype.toggle=function(e,t){return null==t&&(t=null),null===t&&(t=this.options.slide),e.is_open?this.closeNode(e,t):this.openNode(e,t)},t.prototype.getTree=function(){return this.tree},t.prototype.selectNode=function(e){return this._selectNode(e,!1)},t.prototype._selectNode=function(e,t){var n,o,r,i;if(null==t&&(t=!1),this.select_node_handler){if(n=function(t){return function(){return t.options.onCanSelectNode?t.options.selectable&&t.options.onCanSelectNode(e):t.options.selectable}}(this),r=function(t){return function(){var n;return n=e.parent,n&&n.parent&&!n.is_open?t.openNode(n,!1):void 0}}(this),i=function(e){return function(){return e.options.saveState?e.save_state_handler.saveState():void 0}}(this),!e)return this._deselectCurrentNode(),void i();if(n())return this.select_node_handler.isNodeSelected(e)?t&&(this._deselectCurrentNode(),this._triggerEvent("tree.select",{node:null,previous_node:e})):(o=this.getSelectedNode(),this._deselectCurrentNode(),this.addToSelection(e),this._triggerEvent("tree.select",{node:e,deselected_node:o}),r()),i()}},t.prototype.getSelectedNode=function(){return this.select_node_handler?this.select_node_handler.getSelectedNode():null},t.prototype.toJson=function(){return JSON.stringify(this.tree.getData())},t.prototype.loadData=function(e,t){return this._loadData(e,t)},t.prototype.loadDataFromUrl=function(e,t,n){return"string"!==$.type(e)&&(n=t,t=e,e=null),this._loadDataFromUrl(e,t,n)},t.prototype.reload=function(){return this.loadDataFromUrl()},t.prototype._loadDataFromUrl=function(e,t,n){var o,r,i,s,a,d;if(o=null,r=function(e){return function(){return o=t?$(t.element):e.element,o.addClass("jqtree-loading")}}(this),d=function(){return o?o.removeClass("jqtree-loading"):void 0},a=function(){return"string"===$.type(e)&&(e={url:e}),e.method?void 0:e.method="get"},i=function(e){return function(o){return d(),e._loadData(o,t),n&&$.isFunction(n)?n():void 0}}(this),s=function(t){return function(){return a(),$.ajax({url:e.url,data:e.data,type:e.method.toUpperCase(),cache:!1,dataType:"json",success:function(e){var n;return n=$.isArray(e)||"object"==typeof e?e:$.parseJSON(e),t.options.dataFilter&&(n=t.options.dataFilter(n)),i(n)},error:function(e){return d(),t.options.onLoadFailed?t.options.onLoadFailed(e):void 0}})}}(this),e||(e=this._getDataUrlInfo(t)),r(),e){if(!$.isArray(e))return s();i(e)}else d()},t.prototype._loadData=function(e,t){var n,o;return null==t&&(t=null),n=function(e){return function(){var n,o,r,i;if(e.select_node_handler)for(i=e.select_node_handler.getSelectedNodesUnder(t),n=0,o=i.length;o>n;n++)r=i[n],e.select_node_handler.removeFromSelection(r);return null}}(this),o=function(n){return function(){return t.loadFromData(e),t.load_on_demand=!1,t.is_loading=!1,n._refreshElements(t)}}(this),e?(this._triggerEvent("tree.load_data",{tree_data:e}),t?(n(),o()):this._initTree(e),this.isDragging()?this.dnd_handler.refresh():void 0):void 0},t.prototype.getNodeById=function(e){return this.tree.getNodeById(e)},t.prototype.getNodeByName=function(e){return this.tree.getNodeByName(e)},t.prototype.getNodesByProperty=function(e,t){return this.tree.getNodesByProperty(e,t)},t.prototype.openNode=function(e,t){return null==t&&(t=null),null===t&&(t=this.options.slide),this._openNode(e,t)},t.prototype._openNode=function(e,t,n){var o,r;if(null==t&&(t=!0),o=function(e){return function(t,n,o){var r;return r=new i(t,e),r.open(o,n)}}(this),e.isFolder()){if(e.load_on_demand)return this._loadFolderOnDemand(e,t,n);for(r=e.parent;r;)r.parent&&o(r,!1,null),r=r.parent;return o(e,t,n),this._saveState()}},t.prototype._loadFolderOnDemand=function(e,t,n){return null==t&&(t=!0),e.is_loading=!0,this._loadDataFromUrl(null,e,function(o){return function(){return o._openNode(e,t,n)}}(this))},t.prototype.closeNode=function(e,t){return null==t&&(t=null),null===t&&(t=this.options.slide),e.isFolder()?(new i(e,this).close(t),this._saveState()):void 0},t.prototype.isDragging=function(){return this.dnd_handler?this.dnd_handler.is_dragging:!1},t.prototype.refreshHitAreas=function(){return this.dnd_handler.refresh()},t.prototype.addNodeAfter=function(e,t){var n;return n=t.addAfter(e),this._refreshElements(t.parent),n},t.prototype.addNodeBefore=function(e,t){var n;return n=t.addBefore(e),this._refreshElements(t.parent),n},t.prototype.addParentNode=function(e,t){var n;return n=t.addParent(e),this._refreshElements(n.parent),n},t.prototype.removeNode=function(e){var t;return t=e.parent,t?(this.select_node_handler.removeFromSelection(e,!0),e.remove(),this._refreshElements(t)):void 0},t.prototype.appendNode=function(e,t){var n;return t=t||this.tree,n=t.append(e),this._refreshElements(t),n},t.prototype.prependNode=function(e,t){var n;return t||(t=this.tree),n=t.prepend(e),this._refreshElements(t),n},t.prototype.updateNode=function(e,t){var n;return n=t.id&&t.id!==e.id,n&&this.tree.removeNodeFromIndex(e),e.setData(t),n&&this.tree.addNodeToIndex(e),this.renderer.renderFromNode(e),this._selectCurrentNode()},t.prototype.moveNode=function(e,t,n){var o;return o=h.nameToIndex(n),this.tree.moveNode(e,t,o),this._refreshElements()},t.prototype.getStateFromStorage=function(){return this.save_state_handler.getStateFromStorage()},t.prototype.addToSelection=function(e){return e?(this.select_node_handler.addToSelection(e),this._getNodeElementForNode(e).select(),this._saveState()):void 0},t.prototype.getSelectedNodes=function(){return this.select_node_handler.getSelectedNodes()},t.prototype.isNodeSelected=function(e){return this.select_node_handler.isNodeSelected(e)},t.prototype.removeFromSelection=function(e){return this.select_node_handler.removeFromSelection(e),this._getNodeElementForNode(e).deselect(),this._saveState()},t.prototype.scrollToNode=function(e){var t,n;return t=$(e.element),n=t.offset().top-this.$el.offset().top,this.scroll_handler.scrollTo(n)},t.prototype.getState=function(){return this.save_state_handler.getState()},t.prototype.setState=function(e){return this.save_state_handler.setInitialState(e),this._refreshElements()},t.prototype.setOption=function(e,t){return this.options[e]=t},t.prototype.moveDown=function(){return this.key_handler?this.key_handler.moveDown():void 0},t.prototype.moveUp=function(){return this.key_handler?this.key_handler.moveUp():void 0},t.prototype.getVersion=function(){return g},t.prototype._init=function(){return t.__super__._init.call(this),this.element=this.$el,this.mouse_delay=300,this.is_initialized=!1,this.renderer=new r(this),null!=p?this.save_state_handler=new p(this):this.options.saveState=!1,null!=_&&(this.select_node_handler=new _(this)),null!=o?this.dnd_handler=new o(this):this.options.dragAndDrop=!1,null!=c&&(this.scroll_handler=new c(this)),null!=a&&null!=_&&(this.key_handler=new a(this)),this._initData(),this.element.click($.proxy(this._click,this)),this.element.dblclick($.proxy(this._dblclick,this)),this.options.useContextMenu?this.element.bind("contextmenu",$.proxy(this._contextmenu,this)):void 0},t.prototype._deinit=function(){return this.element.empty(),this.element.unbind(),this.key_handler&&this.key_handler.deinit(),this.tree=null,t.__super__._deinit.call(this)},t.prototype._initData=function(){return this.options.data?this._loadData(this.options.data):this._loadDataFromUrl(this._getDataUrlInfo())},t.prototype._getDataUrlInfo=function(e){var t,n;return t=this.options.dataUrl||this.element.data("url"),n=function(n){return function(){var o,r,i;return i={url:t},e&&e.id?(o={node:e.id},i.data=o):(r=n._getNodeIdToBeSelected(),r&&(o={selected_node:r},i.data=o)),i}}(this),$.isFunction(t)?t(e):"string"===$.type(t)?n():t},t.prototype._getNodeIdToBeSelected=function(){return this.options.saveState?this.save_state_handler.getNodeIdToBeSelected():null},t.prototype._initTree=function(e){var t;return this.tree=new this.options.nodeClass(null,!0,this.options.nodeClass),this.select_node_handler&&this.select_node_handler.clear(),this.tree.loadFromData(e),t=this._setInitialState(),this._refreshElements(),t&&this._setInitialStateOnDemand(),this.is_initialized?void 0:(this.is_initialized=!0,this._triggerEvent("tree.init"))},t.prototype._setInitialState=function(){var e,t,n,o,r;return r=function(e){return function(){var t,n;return e.options.saveState&&e.save_state_handler?(n=e.save_state_handler.getStateFromStorage(),n?(t=e.save_state_handler.setInitialState(n),[!0,t]):[!1,!1]):[!1,!1]}}(this),e=function(e){return function(){var t,n;return e.options.autoOpen===!1?!1:(t=e._getAutoOpenMaxLevel(),n=!1,e.tree.iterate(function(e,o){return e.load_on_demand?(n=!0,!1):e.hasChildren()?(e.is_open=!0,o!==t):!1}),n)}}(this),o=r(),t=o[0],n=o[1],t||(n=e()),n},t.prototype._setInitialStateOnDemand=function(){var e,t;return t=function(e){return function(){var t;return e.options.saveState&&e.save_state_handler?(t=e.save_state_handler.getStateFromStorage(),t?(e.save_state_handler.setInitialStateOnDemand(t),!0):!1):!1}}(this),e=function(e){return function(){var t,n,o,r;return o=e._getAutoOpenMaxLevel(),n=[],t=function(t){return e._openNode(t,!1,r)},(r=function(){return e.tree.iterate(function(n,r){return n.load_on_demand?(n.is_loading||t(n),!1):(e._openNode(n,!1),r!==o)})})()}}(this),t()?void 0:e()},t.prototype._getAutoOpenMaxLevel=function(){return this.options.autoOpen===!0?-1:parseInt(this.options.autoOpen)},t.prototype._refreshElements=function(e){return null==e&&(e=null),this.renderer.render(e),this._triggerEvent("tree.refresh")},t.prototype._click=function(e){var t,n,o;if(t=this._getClickTarget(e.target)){if("button"===t.type)return this.toggle(t.node,this.options.slide),e.preventDefault(),e.stopPropagation();if("label"===t.type&&(o=t.node,n=this._triggerEvent("tree.click",{node:o,click_event:e}),!n.isDefaultPrevented()))return this._selectNode(o,!0)}},t.prototype._dblclick=function(e){var t;return t=this._getClickTarget(e.target),t&&"label"===t.type?this._triggerEvent("tree.dblclick",{node:t.node,click_event:e}):void 0},t.prototype._getClickTarget=function(e){var t,n,o,r;if(o=$(e),t=o.closest(".jqtree-toggler"),t.length){if(r=this._getNode(t))return{type:"button",node:r}}else if(n=o.closest(".jqtree-element"),n.length&&(r=this._getNode(n)))return{type:"label",node:r};return null},t.prototype._getNode=function(e){var t;return t=e.closest("li.jqtree_common"),0===t.length?null:t.data("node")},t.prototype._getNodeElementForNode=function(e){return e.isFolder()?new i(e,this):new u(e,this)},t.prototype._getNodeElement=function(e){var t;return t=this._getNode(e),t?this._getNodeElementForNode(t):null},t.prototype._contextmenu=function(e){var t,n;return t=$(e.target).closest("ul.jqtree-tree .jqtree-element"),t.length&&(n=this._getNode(t))?(e.preventDefault(),e.stopPropagation(),this._triggerEvent("tree.contextmenu",{node:n,click_event:e}),!1):void 0},t.prototype._saveState=function(){return this.options.saveState?this.save_state_handler.saveState():void 0},t.prototype._mouseCapture=function(e){return this.options.dragAndDrop?this.dnd_handler.mouseCapture(e):!1},t.prototype._mouseStart=function(e){return this.options.dragAndDrop?this.dnd_handler.mouseStart(e):!1},t.prototype._mouseDrag=function(e){var t;return this.options.dragAndDrop?(t=this.dnd_handler.mouseDrag(e),this.scroll_handler&&this.scroll_handler.checkScrolling(),t):!1},t.prototype._mouseStop=function(e){return this.options.dragAndDrop?this.dnd_handler.mouseStop(e):!1},t.prototype._triggerEvent=function(e,t){var n;return n=$.Event(e),$.extend(n,t),this.element.trigger(n),n},t.prototype.testGenerateHitAreas=function(e){return this.dnd_handler.current_item=this._getNodeElementForNode(e),this.dnd_handler.generateHitAreas(),this.dnd_handler.hit_areas},t.prototype._selectCurrentNode=function(){var e;return e=this.getSelectedNode(),e&&(m=this._getNodeElementForNode(e))?m.select():void 0},t.prototype._deselectCurrentNode=function(){var e;return e=this.getSelectedNode(),e?this.removeFromSelection(e):void 0},t}(d),s.getModule=function(e){var t;return t={node:v,util:y},t[e]},f.register(s,"tree")},{"./drag_and_drop_handler":1,"./elements_renderer":2,"./key_handler":3,"./mouse.widget":4,"./node":5,"./node_element":6,"./save_state_handler":7,"./scroll_handler":8,"./select_node_handler":9,"./simple.widget":10,"./util":12,"./version":13}],12:[function(e,t,n){var o,r,i,s;o=function(e,t){var n,o,r,i;for(n=o=0,r=e.length;r>o;n=++o)if(i=e[n],i===t)return n;return-1},i=function(e,t){return e.indexOf?e.indexOf(t):o(e,t)},s=function(e){return"number"==typeof e&&e%1===0},r=function(e){return(""+e).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/"/g,"&quot;").replace(/'/g,"&#x27;").replace(/\//g,"&#x2F;")},t.exports={_indexOf:o,html_escape:r,indexOf:i,isInt:s}},{}],13:[function(e,t,n){t.exports="1.0.0"},{}]},{},[11]);
			
			//MEGA MENU
			$(document).off("click","span#mobile-menu-toggle").on("click","span#mobile-menu-toggle",function(){$("a#tracklink").mouseover(),$(this).parent("li").siblings().children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow"),$(this).parent("li").siblings().find("#mobile-menu-toggle + ul").slideUp(),$(this).next().slideToggle(),$(this).toggleClass("menu-dropdown-arrow")}),$(document).on("click","ul.words span#mobile-menu-toggle",function(){var e=$(this).parents("ul.words").siblings("div.departmenthover").attr("id");$(this).parent("li.short.words").index("."+e+" .short.words");if($(".long.words").hide(),div_container=$(this).parent(".short.words"),$(this).hasClass("menu-dropdown-arrow"))for(var o=1;o<$(".long.words").length&&div_container.next().hasClass("long");o++)div_container.next().show(),div_container=div_container.next();else $(".long.words").hide();longStyleContainer=$(this).parent(".short.words").next(".long.words").attr("style"),$(this).parents("li.level1").nextAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow"),$(this).parents("li.level1").prevAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow")});
		</script>
