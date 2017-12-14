/*var digitalData;
var isImageHoverTriggered = false;		// flag to identify mouse hover action
$(document).ready(function(){

    var DTM_SCRIPT_DEV = "<script type='text/javascript' src='//assets.adobedtm.com/9fd06d4068c619c47b289b9c496761efd086a233/satelliteLib-9d27bc3356d4973d37a14f45dd3a41618b35a35a-staging.js'></script>";
    var DTM_SCRIPT_PROD = "<script src='//assets.adobedtm.com/9fd06d4068c619c47b289b9c496761efd086a233/satelliteLib-9d27bc3356d4973d37a14f45dd3a41618b35a35a.js'></script>";
    
    var visitor_ip = ACC.config.VisitorIp;
	
	var user_type = $.cookie("mpl-userType");
	var user_id = $.cookie("mpl-user");
	var site_region = $("#site_section").val();
	var site_currency ='INR';
	var domain_name = document.domain;
	var d = new Date();
	//var user_login_type = $('#userLoginType').val().trim();
	var pageType = $('#pageType').val();
	var pageName= $('#pageName').val().toLowerCase();
	var pageNameU = $('#pageName').val();
	var tealiumOrderFlag = $('#tealiumOrderFlag').val();
	var Promo_Id ="";
	if($("#product_applied_promotion_title").val() && $("#product_applied_promotion_code").val() !=undefined)
	{
		 var promo_title=$("#product_applied_promotion_title").val().toLowerCase().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
		 var promo_id=$("#product_applied_promotion_code").val().toLowerCase().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
		 Promo_Id = promo_title+":" + promo_id;
	}
    var script ="";
	var user_login_type = $('#userLoginType').val().trim().toLowerCase();	
	var product_category ="";
	var page_subcategory_name_L2 ="";
	var page_subcategory_name_l3 ="";
	if($("#product_category").val() !=undefined && $("#product_category").val() !=null){ 
		product_category = $("#product_category").val().toLowerCase().replace(/_+/g, '_') ;  
	}
	if($("#page_subcategory_name").val() !=undefined && $("#page_subcategory_name").val() !=null){ 
		page_subcategory_name_L2 = $("#page_subcategory_name").val().toLowerCase().replace(/_+/g, '_') ;
	}
	if($("#page_subcategory_name_l3").val() !=undefined && $("#page_subcategory_name_l3").val() != null){ 
		page_subcategory_name_L3 = $("#page_subcategory_name_l3").val().toLowerCase().replace(/_+/g, '_');
	}
	
	var sellerList = $('#pdpSellerIDs').val();
	var subdomain = window.location.href.split("/")[2].split(".")[0];
	var subDomain= "";
	if(subdomain != "undefined"){
		subDomain = subdomain;
	}
	 var currentPageURL = window.location.href;

  
	
//   onload generic variables for all pages| Digital data obj defination starts
	digitalData = {
		page : {
			pageInfo : {
				pageName  : pageName,
				domain    : domain_name,
				subDomain : subDomain
			},
			category : {
				primaryCategory : pageType
			}
		}
	}
		console.log("Digital data loaded:"+d.getHours()+":"+d.getMinutes()+":"+ d.getSeconds());
	
*/
var digitalData ;
$(document).ready(function(){
	
			var domain_name = document.domain;
			var pageType = $('#pageType').val();
			var pageName= $('#pageName').val().toLowerCase();
			var subdomain = window.location.href.split("/")[2].split(".")[0];
			var subDomain= "";
			if(subdomain != "undefined"){
				subDomain = subdomain;
			}
			var dateD = new Date(); 
			
			//   onload generic variables for all pages| Digital data obj defination starts
			
				 digitalData = {
					page : {
						pageInfo : {
							pageName  : pageName,
							domain    : domain_name,
							subDomain : subDomain
						},
						category : {
							primaryCategory : pageType
						}
					}
				}	
				console.log("Digital data loaded:"+dateD.getHours()+":"+dateD.getMinutes()+":"+ dateD.getSeconds());

			var user_type = $.cookie("mpl-userType");
			var user_id = $.cookie("mpl-user");
			var currentPageURL = window.location.href;
			var pageType = $('#pageType').val();
			var pageNameU = $('#pageName').val();
			var tealiumOrderFlag = $('#tealiumOrderFlag').val();
			var Promo_Id ="";
			if($("#product_applied_promotion_title").val() && $("#product_applied_promotion_code").val() !=undefined)
			{
				 var promo_title=$("#product_applied_promotion_title").val().toLowerCase().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
				 var promo_id=$("#product_applied_promotion_code").val().toLowerCase().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
				 Promo_Id = promo_title+":" + promo_id;
			}
			
			var product_category ="";
			var page_subcategory_name_L2 ="";
			var page_subcategory_name_l3 ="";
			if($("#product_category").val() !=undefined && $("#product_category").val() !=null){ 
				product_category = $("#product_category").val().toLowerCase().replace(/_+/g, '_') ;  
			}
			if($("#page_subcategory_name").val() !=undefined && $("#page_subcategory_name").val() !=null){ 
				page_subcategory_name_L2 = $("#page_subcategory_name").val().toLowerCase().replace(/_+/g, '_') ;
			}
			if($("#page_subcategory_name_l3").val() !=undefined && $("#page_subcategory_name_l3").val() != null){ 
				page_subcategory_name_L3 = $("#page_subcategory_name_l3").val().toLowerCase().replace(/_+/g, '_');
			}
			
			var sellerList = $('#pdpSellerIDs').val();
			var user_login_type = $('#userLoginType').val().trim().toLowerCase();	
			if(user_id != "anonymous"){
					digitalData.account = {
						login : {
							customerID : user_id
						}	
					}
	         }
	//user login type for g+,fb
	if(user_login_type != '' && user_login_type != undefined && user_login_type != null 
		&& user_login_type != 'email' && user_login_type!= 'guest_user'){

		if(typeof digitalData.account != "undefined"){
			if(typeof digitalData.account.login != "undefined"){
				digitalData.account.login.type = user_login_type;
			}
			else{
				digitalData.account.login = {
					type : user_login_type
				}
			}
		}
		else{
			digitalData.account = {
				login : {
					type : user_login_type
				}
			}
		}
		
	}
	
	
	/*DTM Implementation page wise for on load variables starts*/
	
	// TPR-6295 | For Homepage
	if (pageType == "homepage") {
		if(typeof _satellite !="undefined"){
		       _satellite.track('cpj_home_page');
		}
	
	}
	
	// For PDP
	if (pageType == "product"
			|| pageType == "/sellersdetailpage") {
		try{
			   var product_id = $("#product_id").val().toLowerCase();
			   var product_category = $("#product_category").val().toLowerCase();
			   var product_brand = $("#product_brand").val().toLowerCase();
			   var product_discount = $("#product_discount").val();
			   var pincode = $('#pin').val();
			   var prevPageUrl = document.referrer;
			   var findingMethod ="";
	        //TPR-6300 | Track pdp starts
			   
				/*onload data*/
			  	digitalData.cpj = {
			                 product : {
				                    id : product_id,
				                    category : product_category,
				                    discount : product_discount
			                 },
			                brand : {
				                 name : product_brand
			                }
			  	    }
			
			       if(prevPageUrl != "" && prevPageUrl != 'undefined'){
				           if(prevPageUrl.indexOf('/c-msh') > -1){
					           findingMethod = 'product grid';
				            }
				           else if(prevPageUrl.indexOf("/s/") > -1 || prevPageUrl.indexOf('/m/') > -1){
					           findingMethod = 'seller';
				            }
				           else if(prevPageUrl.indexOf('/c-mbh') > -1){
					           findingMethod = 'brand';
				             }
				           else if(prevPageUrl == document.location.href){
					           findingMethod = 'homePage';
				             }
				           else if(prevPageUrl.indexOf('/search/') > -1){
				        	   findingMethod = 'search_results_page';
				           }
				           else if(prevPageUrl.indexOf('/cart') > -1){
				        	   findingMethod = 'cart';
				           }
				           else if(prevPageUrl.indexOf('/checkout/orderConfirmation') > -1){
				        	   findingMethod = 'orderConfirmation';
				           }
				           else if(prevPageUrl.indexOf('/my-account/viewParticularWishlist') > -1){
				        	   findingMethod = 'wishlist';
				           }
				           else if(prevPageUrl.indexOf('/my-account/reviews') > -1 || prevPageUrl.indexOf('/my-account/orders') > -1){
				        	   findingMethod = 'my-account';
				           }
				          else{
					           findingMethod = '';
				             }
			           } 
			
			           //for external campaigns,banners,recommendations
			           if((currentPageURL.indexOf('icid2') > -1) && (currentPageURL.indexOf('icid') > -1 )){
			        	   findingMethod = 'banner';
			           }
			           
			           if(currentPageURL.indexOf('cid') > -1){
			        	   findingMethod = 'external-campaign';
			           }
			           
			           if(currentPageURL.indexOf('iaclick=true') > -1){
			        	   findingMethod = 'recommendations';
			           }
			          
			           
		             if(findingMethod != ''){
		            	 if(typeof digitalData.cpj.pdp != "undefined"){
		    		         digitalData.cpj.pdp.findingMethod = findingMethod;
		    	           }		
		                 else{
		    		         digitalData.cpj.pdp = {
		    			        findingMethod : findingMethod
		    		        }
		    	          }	
		           }  
		             
		             if(typeof _satellite !="undefined"){
				          _satellite.track('cpj_pdp');
				       }
		             //TPR-6300 | Track pdp ends

		             //TPR-6333 | Track Geo-location of users
		             if(pincode != ''){
		            	 digitalData.geolocation = {
		            			 pin : {
		            				 code : pincode
		            			 }
		            	 }
		             }
		             if(Promo_Id != ""){
		            	 digitalData.cpj.promo = {
		            			 id : Promo_Id
		            	 }

		            	 if(typeof(digitalData.cpj.promo) != "undefined"){
		            		 digitalData.cpj.promo.id = Promo_Id;
		            	 }		
		            	 else{
		            		 digitalData.cpj.promo = {
		            				 id : Promo_Id
		            		 }
		            	 }	
		             }

		             if($("#out_of_stock").val() == "true"){
		            	 //  TISCSXII-2243 |out of stock fix
		            	 dtmErrorTracking("out_of_stock","errorname");

		            	 if(typeof(digitalData.cpj.product) != "undefined"){
		            		 digitalData.cpj.product.id = product_id;
		            		 digitalData.cpj.product.category = product_category;
		            	 }		
		            	 else{
		            		 digitalData.cpj.product = {
		            				 id : product_id,
		            				 category : product_category
		            		 }
		            	 }
		            	 if(typeof _satellite !="undefined"){
		            		 _satellite.track('out_of_stock');
		            	 } 
		             }

			digitalData.page.category.subCategory1 = product_category;
			digitalData.page.category.subCategory2 = page_subcategory_name_L2; 
			digitalData.page.category.subCategory3 = page_subcategory_name_L3;
		}
		   
		catch(e){
			console.log("ERROR on pdp inside dtm call:"+e.message);	
		}
		
	}		
	// Generic Page Script
	if (pageType != 'homepage' && pageType != 'product' && pageType != '/sellersdetailpage' && pageType != 'productsearch'
			&& pageType != 'category' && pageType != 'cart'
			&& pageType != 'multistepcheckoutsummary'
			&& pageType != 'orderconfirmation'
		) 
		{
		//

	}
	
	// TPR-6297 | For PLP
	if (pageType == "category" || pageType == "electronics") {
		try{
		/*		digitalData = {
						page : {
							pageInfo : {
								pageName  : $('#page_name').val().toLowerCase(),
								domain    : domain_name,
								subDomain : subDomain
							},
							category : {
								primaryCategory : pageType
							}
						}
					}*/
				  /*  product impressions*/
				    dtmProductImpressionsPlp();
					//digitalData.page.category.subCategory1 = ListValue("product_category");
				    digitalData.page.category.subCategory1 = product_category;
					digitalData.page.category.subCategory2 = page_subcategory_name_L2; 
					digitalData.page.category.subCategory3 = page_subcategory_name_L3;

					if(typeof _satellite !="undefined"){	
						_satellite.track('cpj_category_pages');
					}
		}
		catch(e){
			console.log("ERROR on plp inside dtm call:"+e.message);
		}
		
	}
		
	//Search
	if(pageType == "productsearch"){
		try{
		/*	digitalData = {
					page : {
						pageInfo : {
							pageName  : $('#page_name').val().toLowerCase(),
							domain    : domain_name,
							subDomain : subDomain
						},
						category : {
							primaryCategory : pageType
						}
					}
				}*/
			
			//TPR-6367  | for null search
		     var isVisible = $('.search-empty.no-results.wrapper:visible').is(':visible');
		     var searchTerm = $('#search_keyword').val().toLowerCase();
		     var searchCategory = $('#searchCategory').val();
		     var searchResults =  $('.search-result h2 span:first').text().replace(/"/g, "");
			    if(isVisible && typeof _satellite !="undefined" ){
				         _satellite.track('null_search');
				         
				        digitalData.internal = {
						 search : {
							 term : searchTerm,
							 category : searchCategory
						  }
				  }
			 }
			//TPR-6367  | for normal  search
			       digitalData.internal = {
						 search : {
							 term : searchTerm,
							 category : searchCategory,
							 results  : searchResults
				 		 }
				  }
			      /*offercount,newcount */
			  dtmSearchTags();
			       /*  product impressions*/
			  dtmProductImpressionsSerp();	
			  if(typeof _satellite !="undefined"){
				    _satellite.track('cpj_search_pages');
				}
		}
		
		catch(e)
		{
			console.log("ERROR in serp inside dtm call:"+e.message);
		}
	  }
	
	// Cart page
	if(pageType == "cart"){
		try{
			var pinCode = $('#pinId').val();
			var product_id ='';
			var product_category ='';
			if($("#product_id").val()!= ''){
				 product_id = JSON.parse($("#product_id").val().toLowerCase());
			}
			if($("#product_category").val()!= ''){
				product_category =  $("#product_category").val().toLowerCase().split(",");
			}
		/*	digitalData.cpj = {
				product : {
					id : JSON.parse($("#product_id").val().toLowerCase()),
					category : JSON.parse(ListValue("product_category"))
				}
			}*/
			digitalData.cpj = {
					product : {
						id : product_id,
						category : product_category
					}
				}
			
			//TPR-6333 | Track Geo-location of users
			  if(pinCode != ''){
				   digitalData.geolocation = {
						pin : {
						code : pinCode
						}
				}
			}
			
			//TPR-6371 | track promotions
			if($('#promolist').val() != '[]') {
				   digitalData.cpj.promo = {
						id : $('#promolist').val().toLowerCase()
				   }
			}
			digitalData.page.category.subCategory1 = product_category ;
			digitalData.page.category.subCategory2 =  page_subcategory_name_L2; 
			digitalData.page.category.subCategory3 = page_subcategory_name_L3;
			
			var sellerList = $('#checkoutSellerIDs').val().replace('_','|');
		  
			digitalData.product = {
					seller : {
						id   : sellerList
					}   
				}
			
			if(typeof _satellite != "undefined"){
			    _satellite.track('cpj_cart_page');
			}
		}
		catch(e)
		{
			console.log("ERROR in cart inside dtm call:"+e.message);
		}
	}
	
	// Checkout pages
	if(pageType =="multistepcheckoutsummary"){
		try{
			var checkoutPageName = pageName +":" + $('#checkoutPageName').val().toLowerCase();
			var product ='';
			var productCategory ='';
	/*		digitalData = {
					page : {
						pageInfo : {
							pageName  : checkoutPageName,
							domain    : domain_name,
							subDomain : subDomain
						},
						category : {
							primaryCategory : "checkout"
						}
					}
				}*/
			
			if($("#product_id").val()!= ''){
				product = JSON.parse($("#product_id").val().toLowerCase());
			}
			if($("#product_category").val()!= ''){
				productCategory =  $("#product_category").val().toLowerCase().split(",");
			}
			
			
			digitalData.cpj = {
					product : {
						id     :  product ,
				      category :  productCategory
				 }
			  }
			digitalData.page.category.subCategory1 = product_category;
			digitalData.page.category.subCategory2 =  page_subcategory_name_L2; 
			digitalData.page.category.subCategory3 = page_subcategory_name_L3;
		}
		catch(e)
		{
			console.log("ERROR in multistepcheckoutsummary dtm call:"+e.message);
		}
	}
	
	//TPR-6296 | brand pages
	
	 if(currentPageURL.indexOf("/c-mbh") > -1 &&
		pageType != 'notfound' && 
		pageType != 'nonbusinesserrorfound' &&
		pageType != 'businesserrorfound' )
		 
	   { 
		       if(pageName !='undefined'){
		    	    var brand = pageName.split('BrandStore-');
			        var brandName ="";
			          if((brand.length) > 1 && typeof(brand[1]) != "undefined" && brand[1] != ''){
						   brandName = brand[1].trim().toLowerCase();
					    }
			          else{
						   brandName =	pageName;
					    } 
		          }
		    
		    	     digitalData.cpj = {
						   brand : {
							  name : brandName
						 }
				     }
		    	     
    	     if(typeof(_satellite) !="undefined"){
				  _satellite.track('cpj_brand_pages');
			  }
  
		}
	
	//TPR-6029|Checkout changes
	if(pageType =="orderconfirmation" && $('#orderIDString').val()!= ''){
		try{
			var product ='';
			var productCategory ='';
			var price ='';
			var quantity ='';
				
			if($("#product_sku").val()!= ''){
				product = JSON.parse($("#product_id").val().toLowerCase());
			}
			if($("#product_category").val()!= ''){
				productCategory =  $("#product_category").val().toLowerCase().split(",");
			}
			if($("#product_unit_price").val()!= ''){
				price = JSON.parse($("#product_unit_price").val());
			}
			if($("#product_quantity").val()!= ''){
				quantity = JSON.parse($("#product_quantity").val());
			}
			
			digitalData.cpj = {
					        product : {
						       id       :  product ,
				               category :  productCategory,
				               price    :  price
				          },
				             payment : {
				        	    quantity : quantity
				           } 
			  }
			
		/*	if(typeof digitalData.cpj.payment !='undefined'){
				digitalData.cpj.payment.quantity = $('#product_quantity').val();
			}
			else{
				digitalData.cpj.payment = {
						quantity : $('#product_quantity').val()
				}
			}*/
			digitalData.page.category.subCategory1 = product_category ;
			digitalData.page.category.subCategory2 =  page_subcategory_name_L2; 
			digitalData.page.category.subCategory3 = page_subcategory_name_L3;
			
			if(typeof _satellite !="undefined"){
				_satellite.track('cpj_order_successful');
			}
		}
		catch(e){
			console.log("ERROR on orderPage in dtm call:"+e.message);
		}
		
	}
	
	//TPR-6707 | track 404 pages
	if(pageType =='notfound' || pageType =='nonbusinesserrorfound' || pageType =='businesserrorfound' || digitalData == 'undefined' ){
		if(typeof(_satellite) !="undefined"){
			_satellite.track('404_error');
		}	
	}
	
	 //TPR-6299 | for merchandising pages
	
     if(pageNameU != 'Product Grid' &&   pageNameU != 'Product Details' &&
    		 pageNameU != 'Cart Page'    &&   pageNameU != 'Checkout-Login Page'  &&  pageNameU != 'Login Page' &&
    		 pageNameU != 'Multi Checkout Summary Page' && pageNameU != 'Order Confirmation Page')
      {	 
            	if(typeof(_satellite) != "undefined"){
    		      _satellite.track('cpj_merchandising_pages');
	  	        }
      }
     
   //TPR-6369 |Error tracking dtm
     if(pageType == 'login'){
    	    if(currentPageURL.indexOf('/login?error=true') > -1  || currentPageURL.indexOf('/login/register') > -1 ){
    			  dtmErrorTracking("login error","login error");
    		  }
        }
     
   //for checkout login page error
     if(pageType == 'checkout-login'){
    	 if(currentPageURL.indexOf('/login?error=true') > -1){
    		 dtmErrorTracking("login error","checkoutlogin error"); 
    	 }
     }
    	
   //checkout register error 
     if(pageType == "notfound"){
    	 if(currentPageURL.indexOf('/checkoutRegister') > -1){
        	 dtmErrorTracking("login error","checkout register error"); 
    	 }
     }
    	
/*  Direct call rule starts here*/
	
    // For icid 
        //SDI-1103
	//var url_string = window.location.href;
	//var url = new URL(url_string);
	//var icid2Param = url.searchParams.get("icid2");
	var icid2Param = getIcid2FromUrl();
	if(typeof icid2Param != "undefined" && icid2Param != null){
		digitalData.internal = {
			campaign : {
				id : icid2Param
			}
		}
		_satellite.track('internal_campaign');
	} 
 
	// For Display Hierarchy
	var breadcrum=[];
	$('.breadcrumbs.wrapper').find('li:not(.active)').each(function(){
		breadcrum.push($(this).find('a').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""));
	})

	var leaf = $('.breadcrumbs.wrapper').find('li.active').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	if(leaf != ""){
		breadcrum.push(leaf);
	}
	if(breadcrum.length > 0){
		/*digitalData.page.display = {
			//hierarchy : JSON.stringify( breadcrum )
				hierarchy :  breadcrum,
				
		}*/
		   if(typeof(digitalData.page.display) != "undefined"){
			     digitalData.page.display.hierarchy = breadcrum;
	           }		
           else {
        	     digitalData.page.display = {
  				      hierarchy :  breadcrum
  		           }
	           }
	}
		
	// Assignment of digial data object
/*	var DTMscript ='<script type="text/javascript"> var digitalData = '+JSON.stringify(digitalData)+'</script>';
	
	if(domain_name =="www.tatacliq.com"){
		script=DTM_SCRIPT_PROD;
	}
	else{
		script=DTM_SCRIPT_DEV;
	}
	DTMscript+=script;
	
	$('#DTMhome').html(DTMscript);*/
	
	//for footer clicks
	$(document).on('click','.yCmsComponent',function(){
		var footer_link = $(this).find('a').html().toLowerCase();
		digitalData.footer = {
				link : {
				   name : footer_link
				}	
			}
		/*if(typeof(_satellite) !="undefined"){
			   _satellite.track('footer_link_clicks');
			}*/
		setTimeout(function() {
			if(typeof(_satellite) !="undefined"){
				   _satellite.track('footer_link_clicks');
				}
			}, 1500);
		
	})
	
	//for header clicks
    $(document).on("click",".headerUl li",function(){
    var header_link = "";	
	var link_name =  $(this).find("a").eq(0).attr("href");
	var id_name =  $(this).find("a").eq(0).attr("id");
	var class_name =$(this).find("span").eq(0).attr("class");
	
	if(id_name == "socialLogin" || class_name == "material-icons"){
		/*if(typeof _satellite !="undefined"){
		    _satellite.track('header_link_clicks');
		}*/
		header_link = "login";
	}
	else if(link_name == "/cart"){
		/*if(typeof _satellite !="undefined"){
		     _satellite.track('header_link_clicks');
		}*/
			header_link = "my_bag";
	}
	else{
	/*	if(typeof _satellite !="undefined"){
	      _satellite.track('header_link_clicks');
		}*/
	   header_link = $(this).find('a').eq(0).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"").trim();
	}
	
	digitalData = {
			header : {
				link : {
					name : header_link
				}
			}
		}
	
	setTimeout(function() {
		if(typeof _satellite !="undefined"){
		      _satellite.track('header_link_clicks');
			}
		}, 1500);
	
	});
	
	/*On Size selection | PDP #29  | tpr- 6301*/ 
	$(document).on('click',".variant-select > li", function(){
		var product_size = $(this).find('a').html();

		if(typeof digitalData.cpj.product != "undefined"){
			  digitalData.cpj.product.size = product_size;
		}
		else{
			   digitalData.cpj.product = {
					size : product_size
			     }
		   }
		
		setTimeout(function() {
			 if(typeof _satellite !="undefined"){
				_satellite.track('cpj_pdp_product_size');
			   }
			}, 1500);
		
	});
	 // TPR-6029 |quick view  size|serp#39
	$(document).on('click',"#quickViewVariant > li", function(){
		var product_size = $(this).find('a').html();

		if(typeof digitalData.cpj.product != "undefined"){
			digitalData.cpj.product.size = product_size;
		}
		else{
			digitalData.cpj.product = {
					size : product_size
			}
		}
		if(typeof _satellite != "undefined"){
			_satellite.track('cpj_qw_product_size');
		}
	});
	// TPR-6366 | need help starts
	$(document).on('click',"#up",function(){
		/*if(typeof _satellite !="undefined"){
		  _satellite.track('need_help');
		}*/
		setTimeout(function() {
			if(typeof _satellite !="undefined"){
				  _satellite.track('need_help');
				}
			}, 1500);
	 });
	
	/*live chat*/
	$(document).on("click","#chatMe",function(){
		/*if(typeof _satellite !="undefined"){
		  _satellite.track('need_help_live_chat');
		}*/
		setTimeout(function() {
			if(typeof _satellite !="undefined"){
				  _satellite.track('need_help_live_chat');
				}
			}, 1500);
	});
	
	/*call*/
	$(document).on("click", "#callMe", function(e) {
		/*if(typeof _satellite !="undefined"){
		   _satellite.track('need_help_call');
		}*/
		setTimeout(function() {
			if(typeof _satellite !="undefined"){
				   _satellite.track('need_help_call');
				}
			}, 1500);
	});
	
	$(document).on('click','#gcbChatRegister',function(){
		/*if(typeof _satellite !="undefined"){
		  _satellite.track('need_help_connect');
		}*/
		setTimeout(function() {
			if(typeof _satellite !="undefined"){
				  _satellite.track('need_help_connect');
				}
			}, 1500);
	});
	
    $(document).on('click','.gwc-chat-registration-skip #gcbChatSkipRegistration',function(){
    	/*if(typeof _satellite !="undefined"){
    	    _satellite.track('need_help_cancel');
    	}*/
    	setTimeout(function() {
    		if(typeof _satellite !="undefined"){
        	    _satellite.track('need_help_cancel');
        	}
			}, 1500);
	});
    $(document).on('click','.button_fwd_wrapper.actions .bcancel',function(){
    	/*if(typeof _satellite !="undefined"){
    	    _satellite.track('need_help_cancel');
    	}*/
    	setTimeout(function() {
    		if(typeof _satellite !="undefined"){
        	    _satellite.track('need_help_cancel');
        	}
			}, 1500);
    })
	//need help ends
    
    // For product colour #30 |TPR-6302
    $(document).on('click',".color-swatch > li", function(){
    	var product_color = $(this).find('img').attr('title').toLowerCase();
    	
    	if(typeof digitalData.cpj.product != "undefined"){
    		digitalData.cpj.product.color = product_color;
    	}
    	else{
    		digitalData.cpj.product = {
    			color : product_color
    		}
    	}
    	
    	if(typeof(_satellite)!="undefined" &&  pageType == "product"){
    	    _satellite.track('cpj_pdp_product_color');
    	}
    })
    /*TPR-6029 | colour selection on quickview #40*/
    $(document).on('click',".color-swatch > li", function(){
     	var product_color = $(this).find('img').attr('title').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
     	
     	if(typeof digitalData.cpj.product != "undefined"){
    		digitalData.cpj.product.color = product_color;
    	}
    	else{
    		digitalData.cpj.product = {	
    			color : product_color
    		}
    	}
     	if(typeof _satellite != "undefined" && pageType != "product"){
     	   	_satellite.track('cpj_qw_product_color');
     	 }
     });
    
    //TPR-6334 | Login-logout start
    var dtm_Login_status = $.cookie("dtmSigninStatus");
	var dtm_Logout_status = $.cookie("dtmSignoutStatus");
	if(typeof dtm_Login_status != "undefined"){
		loginStatusCookieCheck("dtmSigninStatus",dtm_Login_status);
    }
	
    if(typeof dtm_Logout_status != "undefined"){
    	if(typeof _satellite !="undefined"){
    		_satellite.track('logout_successful');
      	}
		deleteCookie("dtmSignoutStatus");
    }
    
    if(pageType == 'login'){
    	window.sessionStorage.setItem('isLoginPageVisited','yes');
    }
    
    if(pageType != 'login' && pageType != 'checkout-login'){
		if(typeof $.cookie("dtmRegistrationJourney") != "undefined"){
    		deleteCookie("dtmRegistrationJourney=Started");
    	}
	}
    // TPR-6338 | signup-start track
    $(document).on('click','.logIn-hi .sign-in-dropdown .headeruserdetails , .logIn-hi .sign-in-dropdown .signin-dropdown-body .register_link',function(){
    	if(typeof $.cookie("dtmRegistrationJourney") == "undefined"){
    		if(typeof _satellite !="undefined"){
    			_satellite.track('signup_start');
    	  	}
    		document.cookie = "dtmRegistrationJourney=Started; path=/;";
    	}
    })
    //TPR-6334 | Login-logout end
	//TPR-6369 |error tracking for unsuccessful order placed
    if(tealiumOrderFlag !='undefined' && tealiumOrderFlag == 'true'){
    	dtmErrorTracking("Order not placed: Unsuccesful error","errorName");
    }
   
    //tpr-TPR-6362 | track checkout activity
    if($("#checkoutPageName").val()=="Select Address"){
    	if(typeof _satellite != "undefined") {  
    		_satellite.track('cpj_checkout_proceed_to_address');
    	}	
    }
    _satellite.pageBottom();
  
});
//SDI-1103
function getIcid2FromUrl()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars["icid2"];
   
}
function differentiateSellerDtm(){
	var sellerList = $('#pdpSellerIDs').val();
	var sellerArray='';
	if(sellerList != undefined &&  sellerList != '' && sellerList != null){
		 sellerArray = $('#pdpSellerIDs').val().split(',').join('|');
	}
	
	var buyboxSeller = $("#sellerSelId").val();
	sellerList = sellerList.substr(1,(sellerList.length)-2);
	sellerList = sellerList.split(',');
	
	var otherSellers='';
	for (var i=0;i<sellerList.length;i++){
		if(sellerList[i].trim() != buyboxSeller){
			if(otherSellers == ''){
				otherSellers = sellerList[i].trim();
			}
			else{
				otherSellers += '_'+sellerList[i].trim();
			}
		}
	}
	
	$('#pdpBuyboxWinnerSellerID').val(buyboxSeller);
	$('#pdpOtherSellerIDs').val(otherSellers);

	//TISCSXII-2186 | pdp fix
	digitalData.product = {
			seller : {
				list : sellerArray,
				id   : $("#pdpBuyboxWinnerSellerID").val(),
				buyBoxWinner : $("#sellerNameId").html().toLowerCase()
			}   
		}
}

// For add to bag and buy now #31, 32
function dtmAddProductToBag(triggerValue){
	var eventName='cpj_';
	var pageType = $('#pageName').val();
	if( pageType == "Product Details" || pageType == "View Seller Page"){
		eventName+="pdp_"+triggerValue;
	}
	/*if(typeof _satellite !="undefined"){
     	_satellite.track(eventName);
	}*/
	setTimeout(function() {
		if(typeof _satellite !="undefined"){
	     	_satellite.track(eventName);
		}
		}, 1500);
}

//for pdp pincode check
function dtmPdpPincode(msg,productCode,pin){
	try{
		if(typeof(digitalData.page.pin) != "undefined"){
			 digitalData.page.pin.value = pin;
		}		
		else {
			   digitalData.page.pin = {
					value : pin
			   }
		}
		/*digitalData.cpj = {
    		            product : {
	                       id      :  productCode,
	                       category : $('#product_category').val().toLowerCase()
	                  } 
                  }*/
		if(typeof(digitalData.cpj.product) != "undefined"){
			digitalData.cpj.product.id =productCode;
			digitalData.cpj.product.category = $('#product_category').val().toLowerCase();
		}		
		else {
			digitalData.cpj.product = {
					id      :  productCode,
					category : $('#product_category').val().toLowerCase()
			}
		}

	/*	if(typeof _satellite != "undefined"){
			if(msg == 'success'){
				_satellite.track('pin_successful');
			}	
			else{
				_satellite.track('pin_failed');
			}	
		}*/ 
		
		setTimeout(function() {
			if(typeof _satellite != "undefined"){
				if(msg == 'success'){
					_satellite.track('pin_successful');
				}	
				else{
					_satellite.track('pin_failed');
				}	
			} 
			}, 1500);
	}
	catch(e){
		console.log("error fn:dtmPdpPincode"+e.message);
	}   
}

function dtmCartPincodeCheck(selectedPincode,msg){
	try{
		if(typeof(digitalData.page.pin) != "undefined"){
			digitalData.page.pin.value = selectedPincode;
		}		
		else {
			digitalData.page.pin = {
					value : selectedPincode
			}
		}

		if(typeof(digitalData.cpj.product) != "undefined"){
			digitalData.cpj.product.id = $('#product_id').val().toLowerCase();
			digitalData.cpj.product.category = $('#product_category').val().toLowerCase();
		}		
		else {
			digitalData.cpj.product = {
					id      :  $('#product_id').val().toLowerCase(),
					category : $('#product_category').val().toLowerCase()
			}
		}
		if(typeof(_satellite) != "undefined"){
			if(msg == 'success'){
				_satellite.track('pin_successful');
			}	
			else{
				_satellite.track('pin_failed');
			}	
		} 
	}
	catch(e){
		console.log("error fn:dtmCartPincodeCheck"+e.message);
	}  
}


function dtmSearchTags(){
	try{
		var finalOfferCount ='';
		var finalNewCount='';
		var offerCount ='';
		var newCount='';
		if( $('.on-sale').length > 0 ){
		    offerCount = $('.on-sale').length ;
		}
		if(offerCount !='undefined' && offerCount !='null' && offerCount != ''){
			finalOfferCount =offerCount;
		}
		if( $('.new').length > 0 ){
		    newCount =	$('.new').length ;
		}
		if(newCount != 'undefined' && newCount != 'null'  && newCount != ''){
			finalNewCount =newCount;
		}
		
			digitalData.cpj = {
					search : {
						offersCount : finalOfferCount,
						newCount    : finalNewCount
					}
				}
	}
	 catch(e){
	        console.log("error fn:dtmSearchTags"+e.message);
       } 
}
	/*Product Specification*/
	$(document).on("click",".nav-wrapper .nav.pdp",function(){
		if(typeof _satellite !="undefined"){
	      _satellite.track('product_specification_link_click');
		}
	})
	
	/*Search Feedback*/
	$('.feedbackYes.blue').click(function(){	
		if(typeof _satellite !="undefined"){
	       _satellite.track('feedback_submit');
		}
			
	});

      /*Social share pdp*/

	$(document).on('click','.share li',function(){
		var selector = $(this).find('a').attr('class');
		var channel_name='';
		if (selector == 'tw'){
			channel_name ='twitter';
		}
		if (selector == 'fb'){
			channel_name ='facebook';
		}
		if (selector == 'gp'){
			channel_name = 'googleplus';
		}
		if (selector == 'mail mailproduct'){
			channel_name = 'email';
		}
		
	    	if(typeof channel_name != "undefined" && channel_name != null){
			
			   digitalData.social = {
					channel : {
						name : channel_name
				}
			  }
			   digitalData.cpj.product = {
					id:  $('#product_id').val().toLowerCase(),
					category : $('#product_category').val().toLowerCase()
		    	}
	    	  }
	    	
	    	if(typeof(_satellite) !="undefined"){
				  _satellite.track('social_share');
			}
	 });


/*	Review Link click on pdp*/
   $(document).on('click','.gig-rating-readReviewsLink_pdp',function(){
	   if(typeof _satellite !="undefined"){
	     _satellite.track('review_link_click');	
	   }
  });
   
/*Compare now page*/
   $(document).on('click','.shop-now',function(){
		 if(typeof _satellite !="undefined"){
			  _satellite.track('shop_now_clicks');
		 }
	 });
   
  /*Sort by SERP/PLP */
	$(document).on('click', '.list_title .UlContainer .sort', function() {
	    var value = $(this).text().trim().toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g, "_").replace(/['"]/g, "");
	    digitalData.page.sort = {
	        option: value
	    }
	    if (typeof _satellite != "undefined") {
	        _satellite.track('sort_option_tracking');
	    }
	});
 
   //Comma separated strings changed to array of strings
	function ListValue(divId){
		try{
			var categoryList = $("#"+divId).val().replace(/_+/g, '_');
			var categoryArray = categoryList.split(",");
			var finalCategoryArray=[];
			for (i = 0; i < categoryArray.length; i++) { 
				finalCategoryArray.push("\""+categoryArray[i]+"\"");
			}
			return "["+finalCategoryArray+"]";
		  }
		catch(e){
	        console.log("error fn:ListValue"+e.message);
         } 
	}

	/*PDP, quickview image hover- TPR-6340*/
	$(document).on("mouseover",".zoomContainer",function(e) {
		if($('#pageType').val() != "/compare"){
			if($('#pageType').val() == "product"
				|| $('#pageType').val() == "/sellersdetailpage"){
				if(!isImageHoverTriggered){
					if(typeof _satellite != "undefined"){
						_satellite.track('cpj_pdp_image_hover');
						isImageHoverTriggered = true;
					}
				}
			}
			else {
				   if(typeof _satellite != "undefined" && (!isImageHoverTriggered)){
					    _satellite.track('cpj_qw_image_hover');
					  isImageHoverTriggered = true;
			        }
			    }
		   }
	});	
	
	/*PDP thumbnail image click*/
	$(document).on("click",".product-info > .product-image-container > .productImageGallery .imageListCarousel .thumb",function(){
		var thumbnail_value = $(this).parent().attr('class');
		var thumbnail_type = $(this).find('img').attr('data-type');
		if(thumbnail_type == "image"){
			if (typeof _satellite != "undefined"  && pageType == 'product') {
				_satellite.track('cpj_pdp_image_click');
		    }
		}
		/*TPR-6289 - video tracking*/
		else if(thumbnail_type == "video"){
			if (typeof _satellite != "undefined") {
				_satellite.track('product_video');
		    }
			var productCode = $("input[name=productCodeMSD]").val();
			var productCategory = $('#product_category').val().trim().toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g, "_").replace(/['"]/g, "");
			if(typeof digitalData.cpj.product != "undefined"){
				digitalData.cpj.product.id = productCode;
				digitalData.cpj.product.category = productCategory;
			}
			else{
				digitalData.cpj.product = {
	         		id :  productCode,
	         		category : productCategory
	         	}
			}
			if(typeof digitalData.product.video != "undefined"){
				digitalData.product.video.name = thumbnail_value;
			}
			else{
				digitalData.product.video = {
					name :  thumbnail_value
	         	}
			}
		}
	})
	
	//TPR-6340 |qv image click
	$(document).on("click",".quick-view-popup > .product-image-container > .productImageGallery .imageListCarousel .thumb",function(){
		if(typeof _satellite != "undefined") {
			_satellite.track('cpj_qw_image_click');
	    }
   });
	
	// TPR-6287 | filter tacking start
	var restrictionFlag='false';
	
	$(document).on('click','.jqtree-title.jqtree_common',function(){
		var filter_value= $(this).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['-]/g,"");
		var filter_type=$(this).parents('form').siblings('div.facet-name.js-facet-name.facet_desktop').find('h3').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/'/g,"");
		
		if(typeof digitalData.filter != "undefined"){
			if(typeof digitalData.filter.temp != "undefined"){
				digitalData.filter.temp.type = filter_type;
				digitalData.filter.temp.value = filter_value;
			}
			else{
				digitalData.filter.temp = {
					type : filter_type,
					value : filter_value
				}
			}
		}
		else{
			digitalData.filter = {
				temp :  {
					type : filter_type,
					value : filter_value
				}
	     	}
		}
		if (typeof _satellite != "undefined") {
			_satellite.track('filter_temp');
	    }
		restrictionFlag='true';
		
	})
	
	
	function setupSessionValuesDtm(){
		try{
			if($('.bottom-pagination .facet-list.filter-opt').children().length > 0){
				var filterTypeList=[];
				var filterTypeFinalList=[];
				var filterValueList=[];
				var sessionPageUrl=window.location.href;
				var finalFilterCombination='';
				
				$('.bottom-pagination .facet-list.filter-opt').children().each(function(){
					var filterType = $(this).children().eq(0).attr('class').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
					filterTypeList.push(filterType);
					var filterValue = $(this).children().eq(1).attr('value').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
					filterValueList.push(filterValue);
					if(finalFilterCombination == ''){
						finalFilterCombination = filterType +":"+ filterValue;
					}
					else{
						finalFilterCombination = finalFilterCombination +"|"+ filterType +":"+ filterValue;
					}
				})
				
				$.each(filterTypeList, function(i, el){
					if($.inArray(el, filterTypeFinalList) === -1){
						filterTypeFinalList.push(el)
					};
				});
				if(filterValueList.length > 0 && filterTypeList.length > 0){
					if(typeof digitalData.filter != "undefined"){
						if(typeof digitalData.filter.final != "undefined"){
							digitalData.filter.final.type = filterTypeFinalList.toString();
							digitalData.filter.final.value = filterValueList.toString();
							digitalData.filter.final.combination = finalFilterCombination;
						}
						else{
							digitalData.filter.final = {
								type : filterTypeFinalList.toString(),
								value : filterValueList.toString(),
								combination : finalFilterCombination
							}
						}
					}
					else{
						digitalData.filter = {
							final :  {
								type : filterTypeFinalList.toString(),
								value : filterValueList.toString(),
								combination : finalFilterCombination
							}
				     	}
					}
				}
				
				if (typeof _satellite != "undefined") {
					_satellite.track('filter_final');
			    }
			}
			
		}
		catch(e){
	             console.log("error fn:setupSessionValuesDtm"+e.message);
	            } 
	}
	
	
	$(window).unload(function(event) {
		var pageType = $('#pageType').val();
		if(pageType == 'category' || pageType == 'productsearch'){
			if(restrictionFlag != 'true'){
				setupSessionValuesDtm();
			}
		}
	});
	// TPR-6287 | filter tacking end
	
	//for TPR-6334
	function loginStatusCookieCheck(cookieName, cookieValue){
		try{
			var location = "header_login_popup";
	    	
	    	var user_login_type = $('#userLoginType').val().trim();
	    	if(user_login_type == "guest_user"){
	    		user_login_type = "email";
	    	}
	    	
	    	if(typeof _satellite !="undefined"){
	    		if(cookieValue == "AccountLoginSuccess"){
	    			_satellite.track('login_successful');
	    			if(window.sessionStorage.getItem('isLoginPageVisited') != null){
	    				location = "login_page";
	    				window.sessionStorage.removeItem('isLoginPageVisited');
	    	    	}
	        	}
	    		//For TPR-6362
	    		else if(cookieValue == "CheckoutLoginSuccess"){
	    			_satellite.track('cpj_checkout_login');
	    			location = "checkout_login_page";
	        	}
	    		else if(cookieValue == "RegistrationSuccess"){
	    			_satellite.track('signup_success');
	    			if(typeof $.cookie("dtmRegistrationJourney") != "undefined"){
	    				deleteCookie("dtmRegistrationJourney");
	    			}
	        	}
	    		else if(cookieValue == "LoginFailure"){
	    			_satellite.track('login_failed');
	        	}
	    	}
	    		
//			if(typeof digitalData.account != "undefined"){
//				if(typeof digitalData.account.login != "undefined"){
//					digitalData.account.login.type = user_login_type;
//					digitalData.account.login.location = location;
//				}
//				else{
//					digitalData.account.login = {
//						type : user_login_type,
//						location : location
//					}
//				}
//			}
			else{
				digitalData.account = {
					login : {
						type : user_login_type,
						location : location
					}
				}
			}
	    	deleteCookie(cookieName);
		}
		catch(e){
                console.log("error fn:loginStatusCookieCheck"+e.message);
              }  	
	 }

	//TPR-6364 start | Track add and remove functionality from wishlist
	function dtmAddToWishlist(pagetype,productId,category){
		try{
			if(typeof(digitalData.cpj.product) != "undefined"){
				digitalData.cpj.product.id = productId;
				digitalData.cpj.product.category = category;
			}		
			else {
				digitalData.cpj.product = {
						id  : productId,
						category : category
				}
			}

			if(typeof(digitalData.page.wishList) != "undefined"){
				digitalData.page.wishList.location = pagetype;
			}		
			else {
				digitalData.page.wishList = {
						location : pagetype 
				}
			}
			if(typeof _satellite != "undefined") {
				_satellite.track('add_to_wishlist');
			}

		}
		catch(e){
			console.log("error fn:dtmAddToWishlist"+e.message);
		}  
	}
	
	function dtmRemoveFromWishlist(pagetype,productId,category){
		try{
			if(typeof(digitalData.cpj.product) != "undefined"){
				digitalData.cpj.product.id = productId;
				digitalData.cpj.product.category = category;
			}		
			else {
				digitalData.cpj.product = {
						id  : productId,
						category : category
				}
			}

			if(typeof(digitalData.page.wishList) != "undefined"){
				digitalData.page.wishList.location = pagetype;
			}		
			else {
				digitalData.page.wishList = {
						location : pagetype 
				}
			}
			if (typeof _satellite != "undefined") {
				_satellite.track('remove_from_wishlist');
			}
		}
		catch(e){
			console.log("error fn:dtmRemoveFromWishlist"+e.message);
		} 
	}
	
	 //TPR-6290 |Product Comparison Tracking
    function dtmAddToComparedList(productList){
    	try{
    		var pipeProducts = '';
        	if(productList != undefined ||productList != null ){
        		 pipeProducts = productList.join("|");
        	   }
        	
        	 digitalData.product = {
        		 comparison : {
        			 array   : pipeProducts
        		 }
        	 }
        	 
    	}
    	 catch(e){
                console.log("error fn:dtmAddToComparedList"+e.message);
               } 
	   }
    
    function dtmAddToCompare(productId,category){
    	try{
    		var productL = '';
        	var categoryL = '';
        	if(productId !=undefined &&  productId !=null){
        		productL = productId.toLowerCase(); 
        	}
        	if(category !=undefined &&  category !=null){
        		categoryL = category.toLowerCase(); 
        	}
        	
        	digitalData.cpj = {
        		product : {
        				id  :  productL,
        		 category   :  categoryL	
        	 }
        	}
        	
        	   /*if(typeof(digitalData.cpj.product) != "undefined"){
        		     digitalData.cpj.product.id = productL;
        		     digitalData.cpj.product.category = categoryL;
                 }		
                else {
     	                digitalData.cpj.product = {
     	            		 id  :  productL,
     	           		 category   :  categoryL
    		           }
                 }*/
    	}
    	catch(e){
            console.log("error fn:dtmAddToCompare"+e.message);
           }
    }
    
  //TPR-6305 |track emi options starts
    function  dtmEmiTrack(){
    	if(typeof(_satellite) != "undefined"){
	 		_satellite.track('cpj_pdp_emi');
	 	}
    }
    
    function dtmEmiBankTrack(emiBankSelected){
    	try{
    		/*digitalData.cpj = {
	    	emi :  {
		    bank : emiBankSelected
		     }
              }*/

	            if(typeof(digitalData.cpj.emi) != "undefined"){
		               digitalData.cpj.emi.bank = emiBankSelected;
	              }		
	            else {
	                  digitalData.cpj.emi = {
	         		           bank : emiBankSelected
	                     }
	                 }
    	  }
    	catch(e){
            console.log("error fn:dtmEmiBankTrack"+e.message);
           }
    	
      }
    //track emi options ends
    
  //TPR-6367  | for  search
	$(document).on('click','#searchButton',function(){
		if (typeof _satellite != "undefined") {
			_satellite.track('internal_search');
	    }
		
	})
	
	//TPR-6369 | Error Tracking
	function dtmErrorTracking(errorType,errorName){
		try{

			/*digitalData.page = {
				error : {
					type  : errorType,
					name  : errorName
				}
			}*/
			if(typeof(digitalData.page.error) != "undefined"){
				digitalData.page.error.type = errorType;
				digitalData.page.error.name = errorName; 
			}		
			else {
				digitalData.page.error = {
						type :  errorType,
						name  : errorName
				}
			}
			if (typeof (_satellite)!= "undefined") {
				_satellite.track('error_tracking');
			}
			//order failure track
			if(errorType == "Order not placed: Unsuccesful error"){
				if (typeof(_satellite)!= "undefined") {
					_satellite.track('cpj_order_fail');
				}
				var product ='';
				var productCategory ='';
				if($("#product_id").val()!= ''){
					product = JSON.parse($("#product_id").val().toLowerCase());
				}
				if($("#product_category").val()!= ''){
					productCategory =  $("#product_category").val().toLowerCase().split(",");	
				}	
				/*digitalData.cpj = {
			    		    product : {
			    			 	         id  :  product,
			    		            category :  productCategory,	
			    		              price  :  $('#product_unit_price').val()
			    	               } 
			                 }*/

				if(typeof(digitalData.cpj.product) != "undefined"){
					digitalData.cpj.product.id = product;
					digitalData.cpj.product.category = productCategory;
					digitalData.cpj.product.price = $('#product_unit_price').val();
				}		
				else {
					digitalData.cpj.product = {
							id  :  product,
							category   :  productCategory,
							price  :  $('#product_unit_price').val()
					}
				}
				if(typeof (digitalData.cpj.payment)!= "undefined"){
					digitalData.cpj.payment.quantity =$('#product_quantity').val();
				}
				if(typeof (digitalData.cpj.order)!= "undefined"){
					digitalData.cpj.order.failureReason = errorName;
				}
			}
		}
		catch(e){
			console.log("error fn:dtmErrorTracking"+e.message);
		}
	}
   //TPR-6288 | My account Order cancel
	function dtmOrderCancelSuccess(productId,category,reasonCancel){
		try{
			//TISPRDT-6213 fix
			digitalData.cpj = {
					product : {
						id  :  productId,
						category   :  category	
					}
			}

			digitalData.order = {
					cancellation : {
						reason  : reasonCancel
					}
			}
			if (typeof (_satellite) != "undefined") {
				_satellite.track('order_cancellation');
			}
		}
		catch(e){
			console.log("error fn:dtmOrderCancelSuccess"+e.message);
		}	

	}
	  //TPR-6288 | My account Order Return
	function dtmOrderReturn(dtmReturnReason,dtmReturnProduct,dtmReturnProductCat){
		try{
			digitalData.cpj = {
					product : {
						id  :  dtmReturnProduct,
						category   :  dtmReturnProductCat	
					}
			}


			digitalData.order = { 
					return : {
						reason : dtmReturnReason
					}
			}
			if (typeof (_satellite) != "undefined") {
				_satellite.track('order_returns');
			}
		}
		catch(e){
			console.log("error fn:dtmOrderReturn"+e.message);
		}
	}
	
	//TPR-6029|DTM CHECKOUT Changes
	function dtmPaymentModeSelection(mode){
		try{
			if(typeof(digitalData.cpj)!= 'undefined' ){
				if(typeof(digitalData.cpj.payment) != 'undefined' ){
					digitalData.cpj.payment.mode = mode;
				}
				else{
					digitalData.cpj.payment = {
							mode : mode
					}
				}
			}
			else{
				digitalData.cpj = {
						payment  : {
							mode : mode 
						}  
				}
			}
			if(typeof (_satellite)!= 'undefined'){
				_satellite.track('cpj_checkout_payment_selection');
			}
		}
		catch(e){
			console.log("error fn:dtmPaymentModeSelection"+e.message);
		}
	}

	function dtmCouponCheck(msg,couponCode){
		try{
			if(typeof(digitalData.cpj)!= 'undefined'){
				  if(typeof (digitalData.cpj.coupon)!= 'undefined'){
					  digitalData.cpj.coupon.code = couponCode;
				  }
				else{
					  digitalData.cpj.coupon = {
							code : couponCode
						}
				  } 
			 }
			if(msg == 'success'){
				 if(typeof (_satellite)!= 'undefined'){
					_satellite.track('cpj_checkout_payment_coupon_success');
			    }
			 }
			else{
				  if(typeof (_satellite)!= 'undefined'){
					_satellite.track('cpj_checkout_payment_coupon_fail');
				 }
			}	
			
		}
		catch(e){
              console.log("error fn:dtmCouponCheck"+e.message);
          }
	}
	
	function dtmStoreSelection(storeName){
		try{
			/*digitalData.cpj = {
					  checkout: {
						 storeName : storeName
					}
			}*/

			if(typeof(digitalData.cpj.checkout) != "undefined"){
				digitalData.cpj.checkout.storeName = storeName;
			}		
			else {
				digitalData.cpj.checkout = {
						storeName : storeName
				}
			}
			if(typeof (_satellite)!= 'undefined'){
				_satellite.track('cpj_checkout_store_selection');
			}
		}
		catch(e){
			console.log("error fn:dtmStoreSelection"+e.message);
		}
	}
	
     //TPR-6372 | Track widgets powered by recommendation
	$(document).on("click",".trending .owl-stage .owl-item",function(){
		var name = $('#ia_products_hot').find('span:first').text();
		var partner  = "IA";
		var location = $('#pageType').val();
		var widgetName = name+":"+partner+":"+location ;
		
		/*digitalData.page = {
				widget : {
					name : widgetName
				}
		}*/
		    if(typeof(digitalData.page.widget) != "undefined"){
		         digitalData.page.widget.name = widgetName;
             }		
           else {
	              digitalData.page.widget = {
	            		  name :  widgetName
		           }
               }
		    if(typeof _satellite != "undefined") {
				_satellite.track('widget_tracking');
		    }
	})
	
	//TPR-6308 | Product Recommendation
  $(document).on('click','.iaAddToCartButton.ia_both',function(){
	   if(typeof(_satellite) != "undefined") {  
	          if($('#pageType').val() == "product" ){
		           _satellite.track('cpj_pdp_product_reco_cart_add');
	            }
	  
	          if($('#pageType').val() == "cart" ){
	         _satellite.track('cpj_cart_product_reco_cart_add');
	           }
	 }
  });

	//tpr-6362 |track checkout activity |8
$(document).on('click','.add_address_button',function(){
	if(typeof (_satellite)!= "undefined") {  
		_satellite.track('cpj_checkout_add_address');
	}
})	
	
//samsung chat tracking

$(document).on('mouseup','.samsung-chat-div',function(){
	if(typeof (_satellite)!= "undefined") {  
		_satellite.track('samsung_chat');
	}
})
//for g+,fb location track |TISPRDT-6012
$(document).on('mouseup','#loginDivsiginflyout',function(){
	 var loginLocation = "header_login_popup";
	if(typeof digitalData.account != "undefined"){
		if(typeof digitalData.account.login != "undefined"){
			digitalData.account.login.location = loginLocation;
		}
		else{
			digitalData.account.login = {
				location : loginLocation
			}
		}
	}
	else{
		digitalData.account = {
			login : {
				location : loginLocation
			}
		}
	}
		
})

$(document).on('mouseup','#loginDiv',function(){
	 var loginLocation = "login_page";
    if(typeof digitalData.account != "undefined"){
		if(typeof digitalData.account.login != "undefined"){
			digitalData.account.login.location = loginLocation;
		}
		else{
			digitalData.account.login = {
				location : loginLocation
			}
		}
	}
	else{
		digitalData.account = {
			login : {
				location : loginLocation
			}
		}
	}
})

$(document).on('mouseup','#loginDivCheckout',function(){
	 var loginLocation = "checkout_login_page";
    if(typeof digitalData.account != "undefined"){
		if(typeof digitalData.account.login != "undefined"){
			digitalData.account.login.location = loginLocation;
		}
		else{
			digitalData.account.login = {
				location : loginLocation
			}
		}
	}
	else{
		digitalData.account = {
			login : {
				location : loginLocation
			}
		}
	}
})

//TISPRDT-6012 ends
//UF-472|Responsive search icon starts
$(document).on('mouseup','.simpleSearchToggle',function(){
	if(typeof (_satellite)!= "undefined") {  
		_satellite.track('searchToggle');
	}
}) 

//UF-472|Responsive search icon starts

/*product impressions start*/
function dtmProductImpressionsSerp(){
	try{
		var count = 10; 
		var productArray= [];
	    var searchResult = $("ul.product-list li.product-item").length;
		if(searchResult < count ){
			count = searchResult;
	    }
	   for(var i=0;i< count;i++)
	   {
		 var selector = 'ul.product-list li.product-item:eq('+i+') span.serpProduct #productCode';
		  product = $(selector).val().toLowerCase();
		  productArray.push(product);

	   }
	     var impressions = productArray.join("|");
	   
	     	  digitalData.page.products = {
	     	    	impression : impressions
	          }
		
	     	  digitalData.cpj.product = {
	     		         id : productArray
	              }
	}
	catch(e){
        console.log("error fn:dtmProductImpressionsSerp"+e.message);
      }
 }

function dtmProductImpressionsPlp(){
	try{
			var count = 10; 
			var productArray= [];
		    var searchResult = $("ul.product-listing.product-grid li.product-item").length;
			if(searchResult < count ){
				count = searchResult;
		    }
		   for(var i=0;i< count;i++)
		   {
		      var  selector = 'ul.product-listing.product-grid li.product-item:eq('+i+') span.serpProduct #productCode';
		      if(typeof selector !="undefined"){
		  	    product = $(selector).val().toLowerCase();
		      }
		      productArray.push(product);
		    }
		     var impressions = productArray.join("|");
				     digitalData.page.products = {
				  	    	impression : impressions
				       }
		 
				     digitalData.cpj = {  
				    		     product : {
				    	               id : productArray
				                   }
				        }
	}
	catch(e){
        console.log("error fn:dtmProductImpressionsPlp"+e.message);
      }
     
}	
/*product impressions end*/	

// TPR-6292 | product qv
function dtmQVTrack(productCodeArray,category,brand){
	try{
		digitalData.cpj = {
	    		product : {
	    				     id  :  productCodeArray,
	    		      category   :  category	
	    	      },
	    	    brand : { 
	    		   name : brand
	    		}      
	    	}
		if(typeof(_satellite) != "undefined"){
			_satellite.track('cpj_qw');
	  	}
		
	}
	catch(e){
        console.log("error fn:dtmQVTrack"+e.message);
      }	
	
}

	
