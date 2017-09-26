/*Facebook Variables to be Provided by TCS*/
var fbID = '';
var fbAccessToken = '';

/* Which widget we are working with */
var widgetMode = '';

/* Initialize company specific variables */
// ecompany = 'dev.tul.com';
rootEP = $('#rootEPForHttp').val();
if (location.protocol === "https:") {
	rootEP = $('#rootEPForHttps').val();
}
recEndPoint = rootEP + '/SocialGenomix/recommendations/products';


// *******************************************************************************
// Populating Dynamic Parameter Values For IA
var allsizes = [ "XXS", "XS", "S", "M", "L", "XL", "XXL" ];
//PRDI-294
var hotDropdownselected = 'All Departments';
var sortDropdownselected = "";
var currentPageURL = window.location.href;
ecompany = $('#ecompanyForIA').val();
var DamMediaHost = $('#DamMediaHost').val();
var mplStaticResourceHost = $('#mplStaticResourceHost').val();
user_id = getCookie("mpl-user");
user_type = getCookie("mpl-userType");
spid = $('#ia_product_code').val();
domain = $('#ia_product_rootCategory_type').val();
search_string = $('#ia_search_text').val();
searchCategory_id = $('#selectedSearchCategoryId').val(); // For Normal search
searchCategory_idFromMicrosite = $('#selectedSearchCategoryIdMicrosite').val(); // For
																				// Microsite
																				// search
var daysDif = '';
var is_new_product = false;

if (searchCategory_id) {
	if (searchCategory_id.indexOf("MSH") > -1) {
		category_id = searchCategory_id;
	} else if (searchCategory_id.indexOf("MBH") > -1) {
		brand_id = searchCategory_id;
	} else if (searchCategory_id !== 'all') {
		seller_id = searchCategory_id;
	} else {
		category_id = '';
	}
}
if (searchCategory_idFromMicrosite) { // only for microsite search
	if (searchCategory_idFromMicrosite.indexOf("MBH") > -1) {
		brand_id = searchCategory_idFromMicrosite;
	} else {
		category_id = '';
	}
}
// Array[productCode] for wishlist and cart pages
$.each($('input[name=productArrayForIA]'), function(prodArrayIndex, val) {
	if (prodArrayIndex < 5) {
		site_product_array.push(val.value);
	}
});

// For homepage,search,searchEmpty,wishlist,cartPage,orderConfirmation
site_page_type = $('#ia_site_page_id').val();

if (site_page_type == 'productDetails') {
	site_page_type = 'productpage';
}

if (site_page_type == 'myStyleProfile') {
	site_page_type = 'viewAllTrending'; // My Recommendation Page
	$.each($('input[name=categoryArrayForIA]'), function(catArrayIndex, val) {
		if (catArrayIndex < 5) {
			category_array.push(val.value);
		}
	});
	$.each($('input[name=brandArrayForIA]'), function(brandArrayIndex, val) {
		if (brandArrayIndex < 5) {
			brand_array.push(val.value);
		}
	});

}
// changes start for url structure changes
if (currentPageURL.indexOf("/c-msh") > -1
		|| currentPageURL.indexOf("/c-ssh") > -1) {
	site_page_type = 'category_landing_page';
	category_id = $('#ia_category_code').val();

}
if (currentPageURL.indexOf("/s/") > -1) {
	site_page_type = 'seller';
	seller_id = currentPageURL.split('/').pop();
	if (seller_id.indexOf('?') > 0) {
		seller_id = seller_id.substr(0, seller_id.indexOf('?'));
	}
}
if (currentPageURL.indexOf("/c-mbh") > -1) {
	site_page_type = 'brand';
	brand_id = $('#ia_category_code').val();

}
// changes end
if (currentPageURL.indexOf("/m/") > -1) {
	
	seller_id = $('#mSellerID').val();
	//INC144319294
	if(seller_id != undefined && seller_id != "")
	{
		site_page_type = 'seller';
	}
}
var footerPageType = $('#ia_footer_page_id').val();
if (footerPageType === 'footerLinkPage') {
	// site_page_type = 'footerPage';
	site_page_type = 'marketplace';
}
if (currentPageURL.indexOf("/search/helpmeshop") > -1) {
	category_id = $('#helpMeShopSearchCatId').val();
}
if (site_page_type === 'orderConfirmationPage') {
	site_page_type = 'orderConfirmation';
}
// *******************************************************************************
/* Only proceed on page types with recommendations */
site_page_array = [ "homepage", "search", "searchEmpty", "cartPage",
		"orderConfirmation", "wishlist", "seller", "brand", "productpage",
		"product", "category_landing_page", "viewSellers", "viewAllTrending",
		"marketplace" ];

/* Get prior information from IA-set Cookies and start timers */
setTimeout(function() {
	init_iaplugin();

	if (jQuery.inArray(site_page_type, site_page_array) > -1) {
		callTataRec();
	}
}, 2000);


/*
1121425 alternate for jquery unique function 
*/    

function distinctVal(arr){
    var newArray = [];
    for(var i=0, j=arr.length; i<j; i++){
        if(newArray.indexOf(arr[i]) == -1)
              newArray.push(arr[i]);  
    }
    return newArray;
}
/*
 * Check if user has logged into the site
 */
function checkUser() {
	user_type = getCookie("mpl-userType");
	if (user_type.indexOf("facebook") === 0
			|| user_type.indexOf("FACEBOOK_LOGIN") === 0) {
		// uid = fbID;
		// TISPRD-2183 FIX
		window.fbAsyncInit = function() {
			FB.getLoginStatus(function(response) {
				if (response.status === "connected") {
					uid = FB.getUserID();
				}
			});
		};
		// TISPRD-2183 FIX end

		/* New login, use current credentials */
		if (getCookie("IAUSERTYPE") !== 'REGISTERED'
				|| getCookie("IAUSERTYPE") !== 'site_user') {
			/*
			 * New elevated id we're not familiar with, get a new session id
			 * before continuing
			 */
			if (getCookie("IAUSERID").length > 0
					&& getCookie("IAUSERID").indexOf(uid) !== 0) {
				jQuery.ajax({
					type : "GET",
					url : rootEP + '/SocialGenomix/recommendations/init/jsonp',
					jsonp : 'callback',
					dataType : 'jsonp',
					data : {
						'referring_url' : document.referrer,
						'ecompany' : ecompany
					},
					contentType : 'application/javascript',
					success : function(response) {
						document.cookie = 'IASESSIONID=' + response.session_id
								+ '; path=/';
						ssid = response.session_id;
						callEventApi('login', null);
					}
				});
			} else {
				callEventApi('login', null);
			}
			// callFBApi(uid, FB.getAccessToken(), ssid);
			// TISPRD-2183 FIX
			window.fbAsyncInit = function() {
				FB.getLoginStatus(function(response) {
					if (response.status === "connected") {
						uid = FB.getUserID();
						callFBApi(uid, FB.getAccessToken(), ssid);
					}
				});
			};
			// TISPRD-2183 FIX end
		}
	} else {
		if (getCookie("IAUSERTYPE").indexOf("facebook") === 0
				|| getCookie("IAUSERTYPE").indexOf("FACEBOOK_LOGIN") === 0) {
			/*
			 * Previously logged into Facebook, not anymore. Send former
			 * credentials in Logout Event
			 */
			callEventApi('logout', {
				"user_id" : getCookie("IAUSERID"),
				"user_type" : getCookie("IAUSERTYPE")
			});
		}
		if (user_type.indexOf("anonymous") === 0
				|| user_type.indexOf("session") === 0) {
			/* Set if we're a session user */
			if (getCookie("IAUSERTYPE").indexOf("REGISTERED") === 0
					|| getCookie("IAUSERTYPE").indexOf("site_user") === 0) {
				/*
				 * Previously logged in as site user, not anymore. Send former
				 * credentials in Logout Event
				 */
				callEventApi('logout', {
					"user_id" : getCookie("IAUSERID"),
					"user_type" : getCookie("IAUSERTYPE")
				});
			}
			user_type = "session";
			uid = ssid;
		} else if (user_type.indexOf("REGISTERED") === 0
				|| user_type.indexOf("site_user") === 0) {
			/* Set if we're a site user */
			uid = getCookie("mpl-user");
			user_type = "site_user";

			/* New login, use current credentials */
			if (getCookie("IAUSERTYPE") !== 'site_user') {
				/*
				 * New elevated id we're not familiar with, get a new session id
				 * before continuing
				 */
				if (getCookie("IAUSERID").length > 0
						&& getCookie("IAUSERID").indexOf(uid) !== 0) {
					jQuery.ajax({
						type : "GET",
						url : rootEP
								+ '/SocialGenomix/recommendations/init/jsonp',
						jsonp : 'callback',
						dataType : 'jsonp',
						data : {
							'referring_url' : document.referrer,
							'ecompany' : ecompany
						},
						contentType : 'application/javascript',
						success : function(response) {
							document.cookie = 'IASESSIONID='
									+ response.session_id + '; path=/';
							ssid = response.session_id;
							callEventApi('login', null);
						}
					});
				} else {
					callEventApi('login', null);
				}
				document.cookie = 'IAEMAILID=' + uid + '; path=/';
			}
		}
	}
	document.cookie = 'IAUSERID=' + uid + '; path=/';
	document.cookie = 'IAUSERTYPE=' + user_type + '; path=/';
}

/*
 * Scan through the webpage and call the APIs for each element we detect
 */
function callForEachElement(params) {
	/* Check against all product widget elements */
	for (var i = 0; i < productWidgetElement.length; i++) {
		if (document.getElementById(productWidgetElement[i]) !== null) {
			var endpoint = '/SocialGenomix/recommendations/products';
			if (productWidget[i] !== "normal") {
				endpoint += '/' + productWidget[i];

			}
			if (productWidget[i].indexOf("hot") === 0
					&& site_page_type === "viewAllTrending") {
				params.count = '100';
			} else {
				params.count = '15';
			}
			if (productWidget[i].indexOf("search") === 0
					&& site_page_type === 'viewAllTrending') {
				params.count = '100';
			}
			if (productWidget[i].indexOf("search") === 0
					&& site_page_type === 'search') {
				params.count = '15';
			}
			params.htmlElement = productWidgetElement[i];
			callRecApi(params, rootEP + endpoint);
		}
	}

	/* Check against all brand widget elements */
	for (var i = 0; i < brandWidgetElement.length; i++) {
		if (document.getElementById(brandWidgetElement[i]) !== null) {
			var endpoint = '/SocialGenomix/recommendations/';
			endpoint += brandWidget[i];

			params.count = '9';
			params.htmlElement = brandWidgetElement[i];
			callRecApi(params, rootEP + endpoint);
		}
	}

	/* Check against all category widget elements */
	for (var i = 0; i < categoryWidgetElement.length; i++) {
		if (document.getElementById(categoryWidgetElement[i]) !== null) {
			var endpoint = '/SocialGenomix/recommendations/';
			endpoint += categoryWidget[i];

			params.count = '8';
			if (categoryWidgetElement[i].indexOf('ia_categories_recent') > -1) {
				params.count = '2';
			}
			params.htmlElement = categoryWidgetElement[i];
			callRecApi(params, rootEP + endpoint);
		}
	}

	/* Check against all collection widget elements */
	for (var i = 0; i < collectionWidgetElement.length; i++) {
		if (document.getElementById(collectionWidgetElement[i]) !== null) {
			var endpoint = '/SocialGenomix/recommendations/';
			endpoint += collectionWidget[i];

			params.count = '3';
			params.htmlElement = collectionWidgetElement[i];
			callRecApi(params, rootEP + endpoint);
		}
	}
}

/* Filter based on categories, price ranges, and sale status */
function getFilteredRecommendations(widgetElement, respData, priceRanges, sale,
		requestId) {
	/* Return an array where each index corresponds to the html of a page */
	var filteredRespData = [];
	var page = -1;
	var afterFilter = 0;

	jQuery.each(respData, function() {
		/*
		 * Narrow by whether product is on sale, exclude product if the price is
		 * not less than original price
		 */
		// if((parseInt(this.price) >== parseInt(this.original_price)) &&
		// sale.length > 0) {
		if ((parseInt(this.discounted_price) === 0) && sale.length > 0) {
			return;
		}
		/* Check if within sale range if price range specified */
		var saleSatisfied = false;
		if (parseInt(this.discounted_price) > 0) {
			saleSatisfied = true;
		} else {// no sale filter
			saleSatisfied = false;
		}
		/* Check if within price range if price range specified */
		var priceSatisfied = false;
		if (priceRanges.length === 0) { // no price filter
			priceSatisfied = true;
		}
		for (var i = 0; i < priceRanges.length; i++) {
			var prices = priceRanges[i].split(' - ');
			if (priceRanges[i].indexOf('And') > -1) {
				var pricess = priceRanges[i].replace('And', '-');
				prices = pricess.split(' - ');
			}
			var min = parseInt(prices[0].substr(1));
			var max = parseInt(prices[1].substr(1));
			if (isNaN(max)) {
				max = '100000000';
			}
			if (min <= parseInt(this.price) && parseInt(this.price) <= max) {
				priceSatisfied = true;
				break; // condition met, stop searching
			}
		}
		/*
		 * If all our conditions are met, make HTML for this and add it to the
		 * page
		 */
		if (priceRanges.length > 0 && sale.length > 0) {

			if (priceSatisfied === true && saleSatisfied === true) {
				if (afterFilter % 20 === 0) {
					page++;
					filteredRespData[page] = "";
				}
				filteredRespData[page] += makeProductHtml(widgetElement, this,
						requestId);
				afterFilter++;
			}
		} else if (priceRanges.length > 0) {
			if (priceSatisfied === true) {
				if (afterFilter % 20 === 0) {
					page++;
					filteredRespData[page] = "";
				}
				filteredRespData[page] += makeProductHtml(widgetElement, this,
						requestId);
				afterFilter++;
			}
		} else if (sale.length > 0) {
			if (saleSatisfied === true) {
				if (afterFilter % 20 === 0) {
					page++;
					filteredRespData[page] = "";
				}
				filteredRespData[page] += makeProductHtml(widgetElement, this,
						requestId);
				afterFilter++;
			}
		} else {
			if (afterFilter % 20 === 0) {
				page++;
				filteredRespData[page] = "";
			}
			filteredRespData[page] += makeProductHtml(widgetElement, this,
					requestId);
			afterFilter++;
		}
	});
	for (var i = 0; i < 5; i++) {
		if (i <= page) {
			$('#iapage' + (i + 1)).css("visibility", "visible");
			$('#iapage_next').css("visibility", "visible");
			$('#iapage' + (i + 1)).removeClass('active');
			$('#iapage1').addClass('active');
		} else {
			$('#iapage' + (i + 1)).css("visibility", "hidden");
			$('#iapage_next').css("visibility", "hidden");
		}
	}
	return filteredRespData;
}

function reorderRecommendations(widgetElement, respData, requestId) {
	var reorderedData = "";
	jQuery.each(respData, function() {
		reorderedData += makeProductHtml(widgetElement, this, requestId);
	});
	return reorderedData;
}
function reorderRecommendationGrid(widgetElement, respData, requestId) {
	var reorderedData = [];
	var i = 0;
	var page = -1;
	jQuery.each(respData, function() {
		if (i % 20 === 0) {
			page++;
			reorderedData[page] = "";
		}
		reorderedData[page] += makeProductHtml(widgetElement, this, requestId);
		i++;
	});
	return reorderedData;
}

/* TCS-provided add to cart code */
// Add to Bag changes incorporated given by IA start
function submitAddToCart(site_productid, site_ussid, iaref) {
	var site_product_id = site_productid;
	var site_uss_id = site_ussid;

	var addToCartFormData = 'qty=1&pinCodeChecked=false&productCodePost='
			+ site_product_id + '&wishlistNamePost=N&ussid=' + site_uss_id
			+ '&CSRFToken=' + ACC.config.CSRFToken + '';
	$
			.ajax({
				url : ACC.config.encodedContextPath + '/cart/add',
				data : addToCartFormData,
				type : 'post',
				cache : false,
				success : function(data) {
					if (data.indexOf("cnt:") >= 0) {
						$("#status" + site_product_id).html("");
						// $("#status"+site_product_id).html("<font
						// color='#00CBE9'>Bagged and ready!</font>");
						// $("#status"+site_product_id).show().fadeOut(5000);
						// ACC.product.displayAddToCart(data,formId,false);
						ACC.product.showTransientCart(site_uss_id);
						$(
								"span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count")
								.text(data.substring(4));

						// TISEE-882
						if (window.location.href.toLowerCase().indexOf('cart') >= 0) {
							location.reload();
						}

					} else if (data == "reachedMaxLimit") {
						$("#status" + site_product_id).html("");
						$("#status" + site_product_id)
								.html(
										"<br/><font color='#ff1c47'>You are about to fill your bag completely!Please decrease the item quantity!</font>");
						$("#status" + site_product_id).show().fadeOut(5000);
					}

					else if (data == "crossedMaxLimit") {
						$("#status" + site_product_id).html("");
						$("#status" + site_product_id).html(
								"<font color='#ff1c47'>Bag is full!</font>");
						$("#status" + site_product_id).show().fadeOut(5000);
					} else if (data == "outofinventory") {
						$("#status" + site_product_id)
								.html(
										"<font color='#ff1c47'>Sorry, we don't seem to have the quantity you need. You might want to lower the quantity.</font>");
						$("#status" + site_product_id).show().fadeOut(6000);
						return false;
					} else if (data == "willexceedeinventory") {
						$("#status" + site_product_id)
								.html(
										"<font color='#ff1c47'>Please decrease the quantity</font>");
						$("#status" + site_product_id).show().fadeOut(6000);
						return false;
					} else {
						$("#status" + site_product_id).html("");
						$("#status" + site_product_id)
								.html(
										"<br/><font color='#ff1c47'>Oops!Something went wrong!</font>");
						$("#status" + site_product_id).show().fadeOut(5000);
					}

				},
				complete : function() {
					// $('#ajax-loader').hide();
					/* PT Issues fix */
					forceUpdateHeader();
				},
				error : function(resp) {
					// alert("Add to Bag unsuccessful");
				}

			});
	if (spid.indexOf(site_product_id) === -1) {
		// Add to Bag changes incorporated given by IA
		params = {
			'count' : '0',
			'referring_site_product_id' : site_product_id
		};
		if (iaref) {
			params.referring_request_id = iaref;
		}
		params = buildParams(params);
		callRecApi(params, rootEP + '/SocialGenomix/recommendations/products');
		callEventApi('add_to_cart', {
			"pname" : [ 'site_product_id', 'quantity' ],
			"pvalue" : [ spid, '1' ]
		});
	} else {
		callEventApi('add_to_cart', {
			"pname" : [ 'site_product_id', 'quantity' ],
			"pvalue" : [ spid, '1' ]
		});
	}
}
// Add to Bag changes incorporated given by IA end

/* Make quickview visible and on top */
function showQuickview(productElement) {

	var qv = productElement.getElementsByClassName("IAQuickView")[0];
	qv.style.zIndex = 11;
	qv.style.visibility = "visible";
	// Added as part of TPR-859 (size on hover)
	var size_bottom = $(productElement).find(".short-info").height() + 31;
	$(productElement).find(".sizesAvailable").css("bottom", size_bottom + "px");
	if ($(productElement).find(".sizesAvailable").length > 0) {
		$(productElement).find(".IAQuickView").addClass("size_on_hover");
	}
}
/* Make quickview and Add to cart visible and on top */
function showBoth(productElement) {

	var qv = productElement.getElementsByClassName("IAQuickView")[0];
	var a2c = productElement.getElementsByClassName("iaAddToCartButton")[0];
	qv.style.zIndex = 11;
	qv.style.visibility = "visible";
	a2c.style.zIndex = 11;
	a2c.style.visibility = "visible";
}
/* Make quickview and Add to cart invisible and behind other divs */
function hideBoth(productElement) {
	var qv = productElement.getElementsByClassName("IAQuickView")[0];
	var a2c = productElement.getElementsByClassName("iaAddToCartButton")[0];
	qv.style.zIndex = -1;
	qv.style.visibility = "hidden";
	a2c.style.zIndex = -1;
	a2c.style.visibility = "hidden";
}
/* Make quickview invisible and behind other divs */
function hideQuickView(productElement) {
	var qv = productElement.getElementsByClassName("IAQuickView")[0];
	qv.style.zIndex = -1;
	qv.style.visibility = "hidden";
}
/* Create pop-up Quickview window */
function popupwindow(productId) {
	ACC.colorbox.open("QV",
			{
				href : ACC.config.encodedContextPath + "/p/" + productId
						+ "/quickView",
				onComplete : function() {
					$(".imageList ul li img").css("height", "102px");
					ia_quickviewGallery();
				}
			});
	params = {
		'count' : '0',
		'site_product_id' : productId
	};
	params = buildParams(params);
	callRecApi(params, rootEP + '/SocialGenomix/recommendations/products');
}

function ia_quickviewGallery() {
	$(document)
			.ready(
					function() {
						var mainImageHeight = $(".main-image").find(
								"img.picZoomer-pic").height();
						var thumbnailImageHeight = (mainImageHeight / 5);
						$(".imageList ul li img").css("height",
								thumbnailImageHeight);
						$("#previousImage").css("opacity", "0.5");
						$("#nextImage").css("opacity", "1");
						var listHeight = $(".imageList li").height();
						if ($("#previousImage").length) {
							$(".imageList").css("height",
									(listHeight * imagePageLimit) + "px");
							$(".productImageGallery").css("height",
									(listHeight * imagePageLimit + 100) + "px");
						}
						$(".imageListCarousel").show();

						if ('ontouchstart' in window) {
							$(
									".quick-view-popup #variantForm .select-size span.selected")
									.next("ul").hide();
							$(
									".quick-view-popup #variantForm .select-size span.selected")
									.click(
											function() {
												$(this).next("ul").toggleClass(
														"select_height_toggle");
											});
						}

					});

}

function compareDateWithToday(SaleDate) {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1; // January is 0!
	var yyyy = today.getFullYear();

	if (dd < 10) {
		dd = '0' + dd
	}
	if (mm < 10) {
		mm = '0' + mm
	}

	var today = new Date(mm + '/' + dd + '/' + yyyy + ' 00:00:00');
	// adjust diff for for daylight savings
	// var SaleDate = new Date("10/21/2015 00:00:00");
	var hoursToAdjust = Math.abs(today.getTimezoneOffset() / 60)
			- Math.abs(SaleDate.getTimezoneOffset() / 60);
	// apply the tz offset
	SaleDate.addHours(hoursToAdjust);
	// The number of milliseconds in one day
	var ONE_DAY = 1000 * 60 * 60 * 24
	// Convert both dates to milliseconds
	var today_ms = today.getTime()
	var SaleDate_ms = SaleDate.getTime()
	// Calculate the difference in milliseconds
	var difference_ms = Math.abs(today_ms - SaleDate_ms)
	// Convert back to days and return
	return Math.round(difference_ms / ONE_DAY)
}
// you'll want this addHours function too

Date.prototype.addHours = function(h) {
	this.setHours(this.getHours() + h);
	return this;
}
/* Creates HTML of individual products */
function makeProductHtml(widgetElement, obj, rid) {
	// Start tpr-3736
	var iaUssid=obj.site_uss_id;
	var mop="";
	var mrp=""
	var spPrice="";
	var isProductPresent=false;
	//PRDI-274
	var html = '';
	
	if(iaResponseData != null && iaResponseData != undefined)
	{
	   for(var i in iaResponseData) {
		if(iaUssid==i){
	    if (iaResponseData.hasOwnProperty(i)) {
	    	var priceData = iaResponseData[i];
	    	for(var j = 0; j < priceData.length; j++)
	    	{
	    	   mrp=priceData[0];
	    	   mop=priceData[1];
	    	   spPrice=priceData[2];
	    	}
	    }
	    isProductPresent=true;
	    break;
	    
	}
	 }
}
	//End tpr-3736
	
	/* This is a bad image */
	if(isProductPresent){
	if (typeof obj.image_url === "undefined") {
		return;
	}
	if (obj.start_date != undefined) {
		daysDif = compareDateWithToday(new Date(obj.start_date))
		if ((daysDif < 8) && (daysDif >= 0)) {
			is_new_product = true;
		}
	}

	if (obj.colors != undefined) {
		if (typeof obj.colors === "string") { // arrays always
			obj.colors = [ obj.colors ];
		}

		jQuery
				.each(
						obj.colors,
						function(icount, itemColor) {
							if (itemColor == 'Pewter') {
								obj.colors[icount] = '#8E9294';
							} else if (itemColor == 'Peach') {
								obj.colors[icount] = '#FFDAB9';
							} else if (itemColor == 'Multi') {
								obj.colors[icount] = '/store/_ui/responsive/common/images/multi.jpg';
							} else if (itemColor == 'Metallic') {
								obj.colors[icount] = '#37FDFC';
							}
						});
	}
	/* IA Changes Start for store/mpl/en */

	var IAurl = obj.url + '/p/' + obj.site_product_id + '/?iaclick=true&req='
			+ rid; /*
					 * iaclick=true for tracking our clicks vs. other services,
					 * pass request id to track clicks
					 */
	/* IA Changes End for store/mpl/en */
	if (spid.length > 0) { /*
							 * pass if product page or if this is applicable for
							 * whatever other reason
							 */
		IAurl += '&rspid=' + spid;
	}
	//PRDI-274
	//var html = '';

	/* TISPRO-303 Changes-checking with 'type' */
	if ((obj.colors != null && obj.colors.length < 2)
			&& (obj.sizes != null && obj.sizes.length < 2)
			&& (obj.type == 'Electronics')) {
		html += '<li onmouseover="showBoth(this)" onmouseout="hideBoth(this)" class="look slide product-tile '
				+ widgetElement
				+ '_list_elements productParentList" style="display: inline-block; position: relative;">';
		// html += '<div onclick=popupwindow("'+obj.site_product_id+'")
		// class="IAQuickView" style="position: absolute; text-transform:
		// uppercase;cursor: pointer; bottom: 31%;left: 0px; z-index: -1;
		// visibility: hidden; color: #00cbe9;display: inline-block; width: 50%;
		// text-align: center;background: #f8f9fb;background-color: rgba(248,
		// 249, 251,0.77);-webkit-font-smoothing:
		// antialiased;height:70px;font-size:12px;"><span>Quick
		// View</span></div><div
		// onclick=submitAddToCart("'+obj.site_product_id+'","'+obj.site_uss_id+'")
		// class="iaAddToCartButton" style="position: absolute; text-transform:
		// uppercase;cursor: pointer; bottom: 31%; z-index: -1; visibility:
		// hidden; color: #00cbe9;display: inline-block;right:0; text-align:
		// center;background: #f8f9fb;background-color: rgba(248, 249,
		// 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;width:
		// 50%;font-size:12px;"><span>Add To Bag</span></div>';

	} else {
		html += '<li onmouseover="showQuickview(this)" onmouseout="hideQuickView(this)" class="look slide product-tile '
				+ widgetElement
				+ '_list_elements productParentList" style="display: inline-block;position: relative;">';
		// html += '<div onclick=popupwindow("'+obj.site_product_id+'")
		// class="IAQuickView" style="position: absolute; text-transform:
		// uppercase;cursor: pointer; bottom: 31%; z-index: -1; visibility:
		// hidden; color: #00cbe9;display: block; width: 100%; text-align:
		// center;background: #f8f9fb;background-color: rgba(248, 249,
		// 251,0.77);-webkit-font-smoothing: antialiased;height:
		// 70px;font-size:12px;"><span>Quick View</span></div>';

	}
	html += '<a href="' + IAurl
			+ '" class="product-tile" style="position: relative;">';
	if (obj.image_url.indexOf("/") > -1) {
		html += '<div class="image" style="position: relative; left: 0;"><img class="product-image" style="font-size: 16px;text-overflow: ellipsis;" src="'
				+ obj.image_url + '" alt="' + obj.name + '"/>';
		if (is_new_product == true) {
			html += '<div style="z-index: 1;" class="new"><span>New</span></div>';
		}
		if (obj.online_exclusive == true) {
			html += '<div class="online-exclusive"><span>online exclusive</span></div>';
		}
		/* TISPRD-2119 Changes for Quick View position */
		if ((obj.colors != null && obj.colors.length < 2)
				&& (obj.sizes != null && obj.sizes.length < 2)
				&& (obj.type == 'Electronics')) {
			html += '<div onclick=popupwindow("'
					+ obj.site_product_id
					+ '") class="IAQuickView ia_both" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0;left: 0px; z-index: -1; visibility: hidden; color: #00cbe9;display: inline-block; width: 50%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height:70px;font-size:12px;"><span>Quick View</span></div><div onclick=submitAddToCart("'
					+ obj.site_product_id
					+ '","'
					+ obj.site_uss_id
					+ '") class="iaAddToCartButton ia_both" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: inline-block;right:0; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;width: 50%;font-size:12px;"><span>Add To Bag</span></div>';

		} else {
			html += '<div onclick=popupwindow("'
					+ obj.site_product_id
					+ '") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;font-size:12px;"><span>Quick View</span></div>';

		}
		/* TISPRD-2119 Changes for Quick View position end */
		html += '</div>';

	} else {
		/* TISPRD-3135 changes for default image -removal of /store */
		html += '<div class="image" style="position: relative; left: 0;"><img class="product-image" style="font-size: 16px;text-overflow: ellipsis;" src="/_ui/desktop/theme-blue/images/missing-product-300x300.jpg" alt="'
				+ obj.name + '"/>';
		if (is_new_product == true) {
			html += '<div style="z-index: 1;" class="new"><span>New</span></div>';
		}
		if (obj.online_exclusive == true) {
			html += '<div class="online-exclusive"><span>online exclusive</span></div>';
		}
		/* TISPRD-2119 Changes for Quick View position */
		if ((obj.colors != null && obj.colors.length < 2)
				&& (obj.sizes != null && obj.sizes.length < 2)
				&& (obj.type == 'Electronics')) {
			html += '<div onclick=popupwindow("'
					+ obj.site_product_id
					+ '") class="IAQuickView ia_both" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0;left: 0px; z-index: -1; visibility: hidden; color: #00cbe9;display: inline-block; width: 50%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height:70px;font-size:12px;"><span>Quick View</span></div><div onclick=submitAddToCart("'
					+ obj.site_product_id
					+ '","'
					+ obj.site_uss_id
					+ '") class="iaAddToCartButton ia_both" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: inline-block;right:0; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;width: 50%;font-size:12px;"><span>Add To Bag</span></div>';

		} else {
			html += '<div onclick=popupwindow("'
					+ obj.site_product_id
					+ '") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;font-size:12px;"><span>Quick View</span></div>';

		}
		/* TISPRD-2119 Changes for Quick View position end */
		html += '</div>';

	}
	// html += '<div class="image" style="position: absolute; left: 0;
	// line-height: 347px; height: 347px; width: 221px; background:center
	// no-repeat url('+obj.image_url+'); background-size:contain;"></div>';
	html += '<div class="short-info ia_short-info" style="position: relative; padding:0;">';
	html += '<ul class="color-swatch" style="top: -3px; ">';
	if (obj.colors.length < 3) {
		jQuery
				.each(
						obj.colors,
						function(icount, itemColor) {
							if (icount == 1) {
								html += '<li style="padding: 0;margin-bottom: 0px;"><span style="background-color: '
										+ itemColor
										+ ';" title='
										+ itemColor
										+ '></span></li>';
							}
						});
	} else if (obj.colors.length > 2) {
		jQuery
				.each(
						obj.colors,
						function(icounts, itemColors) {
							if (icounts < 6) {
								html += '<li style="padding: 0;margin-bottom: 0px;"><span style="background-color: '
										+ itemColors
										+ ';" title='
										+ itemColors
										+ '></span></li>';
							}
						});
	}

	var moreColors = obj.colors.length - 6;
	if (obj.colors.length > 6) {
		html += '<li style="font-size:12px;letter-spacing: 1px;border: none;padding: 0;margin-bottom: 0px;">+'
				+ moreColors + 'more</li>';
	}

	html += '</ul>';
	html += '<p class="company" >' + obj.brand + '</p>';
	html += '<span class="product-name" style="text-overflow: ellipsis;word-break: break-word;">'
			+ obj.name + '</span>';
	html += '<div class="price">';
	console.log("**********MRP::" + mrp); 
	console.log("**********MOP::" + mop); 

	// MOP == price,MRP== original price and deiscount price --Below rule is
	// based on MRP > price > discount_price, MRP should be striked out always
	//Start tpr-3736
	if (parseInt(spPrice) > 0) {
		
		console.log("%%%%%%%%%%%%%%%%%%%%MRP::" + mrp); 
		console.log("%%%%%%%%%%%%%%%%%%%%%MOP::" + mop); 
		
		if (spPrice != null || spPrice != ''
				|| spPrice != "undefined") {

			if (mrp != null
					&& parseInt(mrp) > parseInt(spPrice)) {
				html += '<p class="old mrprice">₹'
						+ parseInt(mrp) + '</p>';
			} else if (mop != null
					&& parseInt(mop) > parseInt(spPrice)
					&& parseInt(mop) > 0) {
				html += '<p class="old moprice">₹' + parseInt(mop)
						+ '</p>';
			}
			// TISPRO-317 changes
			html += '<p class="sale discprice" id="spPrice">₹'
					+ parseInt(spPrice) + '</p>';
		}
	} 
	else if (Math.round(mrp) == (mop)) {
		console.log("MRP::" + mrp); 
		console.log("MOP::" + mop); 
		if(isNaN(mop)) {
			console.log("--------Inside isNan----");
			html += '<p class="normal moprice">₹' + mop + '</p>';
		}
		else {
		html += '<p class="normal moprice">₹' + parseInt(mop) + '</p>';
	}
		
	}

	else if (mop != null || mop != '' || mop != "undefined") {
		
		console.log("#################MRP::" + mrp); 
		console.log("####################MOP::" + mop); 
		if (parseInt(mop) > 0) {
			if (mrp != null
					&& parseInt(mrp) > parseInt(mop)) {
				html += '<p class="old mrprice">₹'
						+ parseInt(mrp) + '</p>';
			}
			html += '<p class="sale moprice">₹' + parseInt(mop) + '</p>';
		}
	}
	if (!obj.sizes) {
		
		html += '</div></div></a>';
		html += '<p style="font-size: 12px;margin-top: 33px;color: rgb(255, 28, 71);" id="status'
				+ obj.site_product_id + '"></p>';
		html += '</li>';
		
		/* Calculating execution time */
		var end = new Date(); 
		console.log("END TIME "+ end.getHours() + ":" + end.getMinutes() + ":" + end.getSeconds()); 
		
		
		return html;
		//End tpr-3736
	} else if (obj.sizes != '' || obj.sizes != null || obj.sizes != "undefined") {
		
		if (obj.sizes.length > 0) {
			var sortedSizes = []; /*
									 * This will stay empty if it's a numerical
									 * size list
									 */
			$.each(obj.sizes, function(index, item) {// Converting obj.sizes
														// array values to
														// UPPERCASE
				obj.sizes[index] = item.toUpperCase();
			});
			for (var i = 0; i < allsizes.length; i++) {
				if (obj.sizes.indexOf(allsizes[i]) > -1) {
					sortedSizes.push(allsizes[i]); /*
													 * Include smallest sizes
													 * first
													 */
				}
			}
			if (sortedSizes.length > 0) { /*
											 * Use this if it's a string-based
											 * size array
											 */
				obj.sizes = sortedSizes;
			} else {
				obj.sizes.sort() /*
									 * Not a string-based size array, sort
									 * normally
									 */
			}
			html += '</div><span class="sizesAvailable">Size : <span class="size-col">['
					+ obj.sizes + '] </span></span>';
		}
	}
	html += '</div></a>';
	html += '<p style="font-size: 12px;margin-top: 33px;color: rgb(255, 28, 71);" id="status'
			+ obj.site_product_id + '"></p>';
	html += '</li>';
	//PRDI-274
	//return html;
	}
	//PRDI-274
	return html;
	}

/* Call Recommendation API, retry if no session id */
function callTataRec() {
	if (ssid) {
		/* See if we have any login credentials */
		checkUser();
		/* Initialize params object we'll be passing around */
		var params = {};
		if (site_page_type === "product" || site_page_type === "productpage") {
			jQuery.ajax({

				type : "GET",

				url : rootEP
						+ '/SocialGenomix/recommendations/products/increment',

				jsonp : 'callback',

				dataType : 'jsonp',

				data : {
					'site_product_id' : spid,
					'ecompany' : ecompany,
					'session_id' : ssid
				},

				contentType : 'application/javascript',

				success : function(response) {
				}

			});
			document.cookie = 'prev_start_time=' + start_time.getTime()
					+ '; path=/';
			/* Check previous pages, add extra parameters if applicable */
			refCheck();
			if (referring_request_id) {
				jQuery.extend(params, {
					'referring_request_id' : referring_request_id
				});
				if (referring_site_product_id) {
					jQuery.extend(params, {
						'referring_site_product_id' : referring_site_product_id
					});
				}
			}
			if (document.getElementById('outOfStockId')) {
				if (document.getElementById('outOfStockId').style.display
						.indexOf("none") === -1) {
					params.out_of_stock = "true";
				} else {
					params.out_of_stock = "false";
				}
			}
			callForEachElement(buildParams(params));
		} else if (site_page_type === "marketplace") {
			/* We will be doing something else here soon */
			callForEachElement(buildParams(params));
		} else if (site_page_type === "search"
				|| site_page_type === "searchEmpty"
				|| site_page_type === "category" || site_page_type === "brand") {
			/*
			 * These pages contain grid view params.count = 100;
			 */
			callForEachElement(buildParams(params));
		} else {
			callForEachElement(buildParams(params));
		}
	} else {
		/* We haven't initialized, get a session id from IA */
		jQuery.ajax({
			type : "GET",
			url : rootEP + '/SocialGenomix/recommendations/init/jsonp',
			jsonp : 'callback',
			dataType : 'jsonp',
			data : {
				'referring_url' : document.referrer
			},
			contentType : 'application/javascript',
			success : function(response) {
				document.cookie = "IASESSIONID=" + response.session_id
						+ '; path=/';
				ssid = response.session_id;
				callTataRec();
			}
		});
	}
}

function iaSortNameAsc(a, b) {
	var aName = a.name.toLowerCase();
	var bName = b.name.toLowerCase();
	return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
}
function iaSortNameDesc(a, b) {
	var aName = a.name.toLowerCase();
	var bName = b.name.toLowerCase();
	return ((aName > bName) ? -1 : ((aName > bName) ? 1 : 0));
}
function iaSortPriceAsc(a, b) {
	return ((a.price < b.price) ? -1 : ((a.price > b.price) ? 1 : 0));
}
function iaSortPriceDesc(a, b) {
	return ((a.price > b.price) ? -1 : ((a.price < b.price) ? 1 : 0));
}

/* Given a response and widget kind, create the HTML we use to update page */
var iaResponseData=null;
function updatePage(response, widgetMode) {
	
//	response={"request_id":"9f5b15d6-eda5-47d3-84de-a2cba9fb77ec","status":200,"status_txt":"OK","data":{"location":null,"recommendations":[{"sizes":"","ia:weightage":-0.00010499,"site_uss_id":"100090CKJShirtGreyCMH393Z5W1BR07L","colors":["Gold"],"original_price":11499,"discounted_price":10499,"subbrand":"","site_product_id":"MP000000000107552","ia:seller_list":[[-0.00010499,{"availability_status":1,"available_quantity":1103,"price":11499,"msrp":11499,"weightage":-0.00010499,"seller_id":"100136","sku":"100136MEIZUM3NOTE01","discount":10499}]],"type":"Electronics","online_exclusive":false,"url":"","category":"MSH1210100","price":11499,"is_new_product":false,"image_url":"xxx","average_user_rating":2.05341359009462,"ia:seller":{"availability_status":1,"available_quantity":1103,"price":11499,"msrp":11499,"weightage":-0.00010499,"seller_id":"100136","sku":"100136MEIZUM3NOTE01","discount":10499},"name":"Meizu M3 Note 4G Dual Sim 32 GB (Gold)","brand":"Meizu","gender":"electronics"}],"filter_key":null,"filter_value":null},"server":"analytics-general-15"};
	
	//response={"request_id":"9f5b15d6-eda5-47d3-84de-a2cba9fb77ec","status":200,"status_txt":"OK","data":{"location":null,"recommendations":[{"sizes":"","ia:weightage":-0.00010499,"site_uss_id":"S00001000000000000000001","colors":["Gold"],"original_price":2499.00,"discounted_price":0.00,"subbrand":"","site_product_id":"987654321","ia:seller_list":[[-0.00010499,{"availability_status":1,"available_quantity":1103,"price":2499.00,"msrp":2499.00,"weightage":-0.00010499,"seller_id":"100136","sku":"100136MEIZUM3NOTE01","discount":10499}]],"type":"Electronics","online_exclusive":false,"url":"","category":"MSH1210100","price":2499.00,"is_new_product":false,"image_url":"xxx","average_user_rating":2.05341359009462,"ia:seller":{"availability_status":1,"available_quantity":1103,"price":2499.00,"msrp":2499.00,"weightage":-0.00010499,"seller_id":"100136","sku":"100136MEIZUM3NOTE01","discount":10499},"name":"Meizu M3 Note 4G Dual Sim 32 GB (Gold)","brand":"Meizu","gender":"electronics"},{"sizes":"","ia:weightage":-0.0001599,"site_uss_id":"123654098765485130011713","colors":["black"],"original_price":20528,"discounted_price":15990,"subbrand":"","site_product_id":"987654322","ia:seller_list":[[-0.0001599,{"availability_status":1,"available_quantity":16,"price":17850,"msrp":20528,"weightage":-0.0001599,"seller_id":"123846","sku":"123846NXG2KSL024","discount":15990}]],"type":"Electronics","online_exclusive":false,"url":"","category":"MSH1223100","price":17850,"is_new_product":false,"image_url":"https://img.tatacliq.com/images/252Wx374H/MP000000000757620_252Wx374H_20161128063952.jpeg","average_user_rating":2.148081518779059,"ia:seller":{"availability_status":1,"available_quantity":16,"price":17850,"msrp":20528,"weightage":-0.0001599,"seller_id":"123846","sku":"100090CKJShirtGreyCMH393Z5W1BR07XL","discount":15990},"name":"Acer Aspire ES1521 15.6\" Laptop (AMD, 1TB HDD) Black","brand":"Acer","gender":"electronics"}],"filter_key":null,"filter_value":null},"server":"analytics-general-15"};
	
	
	var widgetElement = "";
	if (typeof response.data === "undefined") {
		/* Either analytics is down or we passed a bad parameter */
		return;
	}
	if (response.data === null) {
		return;
	}
	/* Product Widgets */
	if (jQuery.inArray(widgetMode, productWidget) > -1) {
		/* If it doesn't exist, we can stop */
		widgetElement = productWidgetElement[jQuery.inArray(widgetMode,
				productWidget)];
		if (!document.getElementById(widgetElement)) {
			return;
		}
		/* It exists, lets name the payload because we may be modifying it */
		var respData = response.data.recommendations;

		/* Use Carousel by default */
		var slider = true;
		/*
		 * But for cases where we have an array of recommendations the size of
		 * 100, we will be using grid view
		 */
		//Srart tpr-3736
		var arrayList = response['data']['recommendations'];
		//console.log("***The response is*** " + JSON.stringify(arrayList));
		var usidList = [];
		var arrIndx = -1;
		for ( var i in arrayList) {
			var ussid = arrayList[i]['site_uss_id'];
			usidList[++arrIndx] = ussid;
		}
		var ussisLists=usidList.join(",");
		var requiredUrl = ACC.config.encodedContextPath + "/p-getIAResponse";
		var dataString = 'iaUssIds=' + ussisLists;
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			async : false,
			data : dataString,
			cache : false,
			dataType : "json",
			success : function(data) {
				
				iaResponseData=data['iaResponse'];
			}
		});
		// End tpr-3736
		if (!respData) {
			// console.log('No response data found for *'+widgetMode+'*
			// wigdet');
			return;
		}

		if (site_page_type === "viewAllTrending") {
			slider = false;
		}
		if (widgetMode === "recent") {
			slider = true;
		}

		/* Make everything blank to begin with */
		document.getElementById(widgetElement).innerHTML = "";
		html = '';
		recIndex = 0;

		/* Staticly-built list of filters */

		var categoryFilters = [];
		/* Category code for Dropdown Filter in hot and search widgets */
		var categoryCodeForFilters = [];
		$.each($('input#for_ia_hot_dropdown_name'), function(i, val) {
			categoryFilters.push(val.value.split("||")[0]);
		});
		$.each($('input#for_ia_hot_dropdown_code'), function(i, val) {
			categoryCodeForFilters.push(val.value);
		});
		/* Removing duplicate categories */
		categoryFilters = distinctVal(categoryFilters);
		// categoryCodeForFilters = jQuery.unique(categoryCodeForFilters);
		categoryCodeForFilters = distinctVal(categoryCodeForFilters);
		/* SortBY dropdown */
		var sortHtml = '<div class="select-view ia_select-view-sort">';
		sortHtml += '<div class="select-list ia_select-list-sort"><span class="selected sortSelected">Sort by: '
				+ sortDropdownselected
				+ '</span><ul id="ia_category_select" style="width: auto;">';
		sortHtml += '<li class="sort_li" id="name-asc">Name: A to Z</li>';
		sortHtml += '<li class="sort_li" id="name-desc">Name: Z to A</li>';
		sortHtml += '<li class="sort_li" id="price-asc">Price: Low to High</li>';
		sortHtml += '<li class="sort_li" id="price-desc">Price: High to Low</li>';
		sortHtml += '</ul></div></div>';

		var catHtml = '<div class="select-view ">';
		// for release 2 changes in home-page headers-All Departments
		//PRDI-294
		catHtml += '<div class="select-list"><span class="selected hotSelected">'+ hotDropdownselected +'</span><ul id="ia_category_select" style="width: auto;">';
		for (var i = 0; i < categoryFilters.length; i++) {
			if (i == 0) {
				//PRDI-294
				catHtml += '<li class="category_li" id="">All Departments</li>';
			}
			catHtml += '<li class="category_li" id="'
					+ categoryCodeForFilters[i] + '">' + categoryFilters[i]
					+ '</li>';
		}
		catHtml += '</ul></div></div>';

		if (slider) {
			if (site_page_type === 'search'
					&& widgetElement === 'ia_products_search') {
				html += '<h2><span style="color: black !important;">Best Sellers</span>';
			} else if (site_page_type === 'viewSellers'
					&& widgetElement === 'ia_products') {
				html += '<h2><span style="color: black !important;">You May Also Need</span>';
			} else {
				// for release 2 changes in pdp-page
				if (site_page_type === 'productpage'
						&& widgetElement === 'ia_products_complements') {
					html += '<h2><span style="color: black !important;">Things That Go With This</span>';
				} else {

					html += '<h2><span style="color: black !important;">'
							+ productWidgetTitle[jQuery.inArray(widgetMode,
									productWidget)] + '</span>';
				}
			}

			/* For hot we need a scrolldown bar to select filters */
			if (site_page_type === "homepage"
					|| site_page_type === "viewAllTrending"
					&& widgetMode != "recent") {
				html += catHtml;
			}
			html += '</h2>';
			html += '<div class="spacer" style="padding: 0 25px;"><div class="slider product ready"><div class="frame"><ul id="'
					+ widgetElement
					+ '_list" class="overflow owl-carousel" style="width: 0.953empx; left: 0px;">';
		} else {
			if (site_page_type === "homepage"
					|| site_page_type === "viewAllTrending") {
				html += '<div class="listing wrapper"><div class="left-block"><ul class="listing-leftmenu"><h3><span>Filter By</span></h3><div>';
				html += '<li class="price"><h4 class="active">price</h4>';
				html += '<ul class="checkbox-menu price">';
				html += '<li><input type="checkbox" id="0-500"> <label for="0-500">₹0 - ₹500</label></li><li><input type="checkbox" id="500-1000"> <label for="500-1000">₹500 - ₹1000</label></li><li><input type="checkbox" id="1000-2000"> <label for="1000-2000">₹1000 - ₹2000</label></li><li><input type="checkbox" id="2000-3000"> <label for="2000-3000">₹2000 - ₹3000</label></li><li><input type="checkbox" id="3000-4000"> <label for="3000-4000">₹3000 - ₹4000</label></li><li><input type="checkbox" id="4000-5000"> <label for="4000-5000">₹4000 - ₹5000</label></li>';
				html += '<li><input type="checkbox" id="5000-10000"> <label for="5000-10000">₹5000 - ₹10000</label></li><li><input type="checkbox" id="10000-25000"> <label for="10000-25000">₹10000 - ₹25000</label></li><li><input type="checkbox" id="25000-50000"> <label for="25000-50000">₹25000 - ₹50000</label></li><li><input type="checkbox" id="And>50000"> <label for="And>50000">₹50000 And Above</label></li>';
				html += '</ul></li><li class="on sale"><h4 class="active">Sale</h4><ul class="checkbox-menu on-sale">';
				html += '<li><input type="checkbox" id="sale-filter"><label for="sale-filter">On Sale</label></li></ul></li></div></ul></div>';
				html += '<div class="right-block">';

				html += '<div><h1 class="ia_trending">'
						+ productWidgetTitle[jQuery.inArray(widgetMode,
								productWidget)] + '</h1>';
				html += '</div>';
				/* SortBY dropdown only for search widget */
				if (widgetMode === "search") {
					html += sortHtml;
				}

				html += catHtml;

			}
			html += '<ul id="'
					+ widgetElement
					+ '_list" class="product-list" style="width: 100%; float: left;margin-top: 55px; ">';

		}

		var pageData = []; // array of strings each containing 20 products as
							// HTML
		var page = -1; // index of page, immediately becomes 0 upon first
						// product
		var activePage = 'iapage1'; // page we're currently on if in grid view

		if (slider) {
			/* Make recommendation html for a carousel */
			
			jQuery.each(respData, function(i, v) {
				html += makeProductHtml(widgetElement, this,
						response.request_id);
				recIndex++;
			});
			if (widgetMode === "hot" && site_page_type == "homepage") {
				html += '</ul></div>';
				/* IA Changes Start for store/mpl/en */
				html += '</div></div><a href="http://'+window.location.host+'/viewAllTrending" class="button hotShowHide" style="display: inline-block;font-size: 12px;height: 42px;line-height: 42px;">Shop the Hot List</a>';  /*UF-249*/
			}
			/* IA Changes End for store/mpl/en */
			else {
				html += '</ul></div>';
				html += '</div></div>';
			}

		} else {

			/* Make recommendation html for gridview by pages */
			jQuery.each(respData, function(i, v) {
				if (recIndex % 20 === 0) {
					page++;
					pageData[page] = "";
				}
				pageData[page] += makeProductHtml(widgetElement, this,
						response.request_id);
				recIndex++;
			});
			html += pageData[0]; // start off with first page
			html += '</ul>';

			html += '<ul id="'
					+ widgetElement
					+ 'page_numbers" class="pagination" style="position: absolute; right: 0;line-height: 0px;cursor: pointer;margin-top: 15px;padding:0px;">';
			html += '<li id="iapage1" class="number first active iapage" style="padding: 5px;"><a>1</a></li>';
			for (var i = 1; i < pageData.length; i++) {
				html += '<li id="iapage' + (i + 1)
						+ '" class="number iapage" style="padding: 5px;"><a>'
						+ (i + 1) + '</a></li>';
			}
			html += '<li id="iapage_next" class="next" style="padding: 5px;"><a>Next <span class="lookbook-only"></span></a></li>';
			html += '</ul>';
			html += '</div>';
		}

		/* Insert the HTML */
		if (recIndex > 0) {
			document.getElementById(widgetElement).innerHTML = html;
		}

		/* if trending, reupdate based on user selection on dropdown. */
		if (widgetMode === "hot" || widgetMode === "search") {
			jQuery(".category_li").on('click', function() { //
				var category_name = jQuery(this).text();
				$('.hotSelected').text(category_name);
				// selected category id from hot dropdown
				category_id = jQuery(this).attr('id');
				// console.log("category id slected in hot widget dropdown :
				// "+category_id);
				var params = {
					'count' : '10'
				};
				if (!slider) {
					params.count = '100';
				}

				params.category_id = category_id;
				hotDropdownselected = category_name;

				if (category_id === "All Department") {
					category_id = "";
					hotDropdownselected = 'All Department';
				}

				// params.category = category_id;
				var endpoint = '/SocialGenomix/recommendations/products/hot';
				// $( ".owl-item" ).css( "display", "none" );
				callRecApi(buildParams(params), rootEP + endpoint);
			});
		}

		/* SortBY dropdown */
		if (widgetMode === "search") {
			jQuery(".sort_li")
					.on(
							'click',
							function() { //
								var sort_type = jQuery(this).text();
								$('.sortSelected').text(sort_type);
								// selected category id from hot dropdown
								sort_key = jQuery(this).attr('id');
								// params.category_id = category_id;
								sortDropdownselected = sort_type;

								if (sort_key === "name-asc") {
									respData.sort(iaSortNameAsc);
								} else if (sort_key === "name-desc") {
									respData.sort(iaSortNameDesc);
								} else if (sort_type === "price-asc") {
									respData.sort(iaSortPriceAsc);
								} else {
									respData.sort(iaSortPriceDesc);
								}
								if (slider) {
									document.getElementById(widgetElement
											+ '_list').innerHTML = reorderRecommendation(
											widgetElement, respData,
											response.request_id);
								} else {
									pageData = reorderRecommendationGrid(
											widgetElement, respData,
											response.request_id);
									document.getElementById(widgetElement
											+ '_list').innerHTML = pageData[0];
									/* Reset page numbers */
									var pages = document
											.getElementById("ia_products_hotpage_numbers").childNodes;
									for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																								// next
										pages[i].visibility = 'visible';
									}
									for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																								// next
										pages[i].visibility = 'hidden';
									}
									activePage = 'iapage1';
								}
							});
		}

		/* Setup UI */
		if (slider) {
			if (site_page_type == 'search'
					&& widgetElement == 'ia_products_search') {
				/* Animate Carousel */
				$("#" + widgetElement + "_list").owlCarousel({

					items : 4,
					loop : true,
					nav : true,
					dots : false,
					navText : [],
					responsive : {
						// breakpoint from 0 up
						0 : {
							items : 1,
							stagePadding : 50,
						},
						// breakpoint from 480 up
						480 : {
							items : 2,
							stagePadding : 50,
						},
						// breakpoint from 768 up
						768 : {
							items : 3,
						},
						// breakpoint from 768 up
						1280 : {
							items : 5,
						}
					}

				/*
				 * items : 4,
				 * 
				 * scrollPerPage: true,
				 * 
				 * itemsDesktop : [1199,3],
				 * 
				 * itemsDesktopSmall : [980,2],
				 * 
				 * itemsTablet: [768,2],
				 * 
				 * itemsMobile : [479,1],
				 * 
				 * navigation: true,
				 * 
				 * navigationText : [],
				 * 
				 * pagination:false,
				 * 
				 * rewindNav : false
				 */

				});

			}
			/* Animate Carousel */
			$("#" + widgetElement + "_list").owlCarousel({

				items : 5,
				loop : true,
				nav : true,
				dots : false,
				navText : [],
				responsive : {
					// breakpoint from 0 up
					0 : {
						items : 1,
						stagePadding : 50,
					},
					// breakpoint from 480 up
					480 : {
						items : 2,
						stagePadding : 50,
					},
					// breakpoint from 768 up
					768 : {
						items : 3,
					},
					// breakpoint from 768 up
					1280 : {
						items : 5,
					}
				}

			/*
			 * items : 5,
			 * 
			 * scrollPerPage: true,
			 * 
			 * itemsDesktop : [1199,4],
			 * 
			 * itemsDesktopSmall : [980,3],
			 * 
			 * itemsTablet: [768,2],
			 * 
			 * itemsMobile : [479,1],
			 * 
			 * navigation: true,
			 * 
			 * navigationText : [],
			 * 
			 * pagination:false,
			 * 
			 * rewindNav : false
			 */
			});

		} else {

			/* Paginate GridView */
			/* Go to page clicked */
			jQuery(".iapage")
					.on(
							'click',
							function() {
								if (pageData[parseInt(jQuery(this).text()) - 1] != undefined) {
									document.getElementById(widgetElement
											+ '_list').innerHTML = pageData[parseInt(jQuery(
											this).text()) - 1];
								} else {
									document.getElementById(widgetElement
											+ '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
								}
								jQuery('#' + activePage).removeClass('active');
								jQuery(this).addClass('active');
								activePage = this.id;
							});

			/* Go to next page if it exists */
			jQuery("#iapage_next")
					.on(
							'click',
							function() {
								var pageIndex = parseInt(activePage.replace(
										/^\D+/g, '')) + 1;
								if (pageIndex > 5) {
									pageIndex = pageIndex % 5;
								}
								if (document.getElementById('iapage'
										+ pageIndex) !== null) {
									var el = document.getElementById('iapage'
											+ pageIndex);
									if (pageData[parseInt(jQuery(el).text()) - 1] != undefined) {
										document.getElementById(widgetElement
												+ '_list').innerHTML = pageData[parseInt(jQuery(
												el).text()) - 1];
									} else {
										document.getElementById(widgetElement
												+ '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
									}
									jQuery('#' + activePage).removeClass(
											'active');
									jQuery(document.getElementById(el.id))
											.addClass('active');
									activePage = el.id;
								}
							});

			/* Filter by price range, and whether it's on sale */
			var priceRanges = [];
			var saleOptions = [];

			/* Select Price Filter */
			jQuery(".price li label")
					.on(
							'click',
							function() {
								/*
								 * Follow the checkmark which isn't visible as
								 * an html element
								 */
								if (priceRanges.indexOf(this.textContent) > -1) {
									priceRanges.splice(priceRanges
											.indexOf(this.textContent), 1);
								} else {
									priceRanges.push(this.textContent);
								}
								pageData = getFilteredRecommendations(
										widgetElement, respData, priceRanges,
										saleOptions, response.request_id);
								if (pageData[0] != undefined
										|| pageData[0] != null) {
									document.getElementById(widgetElement
											+ '_list').innerHTML = pageData[0];
								} else {
									document.getElementById(widgetElement
											+ '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
								}
								/* Reset page numbers */
								var pages = document
										.getElementById("ia_products_hotpage_numbers").childNodes;
								for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																							// next
									pages[i].visibility = 'visible';
								}
								for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																							// next
									pages[i].visibility = 'hidden';
								}
								activePage = 'iapage1';
							});

			/* Select sale filter */
			jQuery(".on-sale li label")
					.on(
							'click',
							function() {
								/*
								 * Follow the checkmark which isn't visible as
								 * an html element
								 */
								if (saleOptions.indexOf(this.textContent) > -1) {
									saleOptions.splice(saleOptions
											.indexOf(this.textContent), 1);
								} else {
									saleOptions.push(this.textContent);
								}
								pageData = getFilteredRecommendations(
										widgetElement, respData, priceRanges,
										saleOptions, response.request_id);
								if (pageData[0] != undefined) {
									document.getElementById(widgetElement
											+ '_list').innerHTML = pageData[0];
								} else {
									document.getElementById(widgetElement
											+ '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
								}
								/* Reset page numbers */
								var pages = document
										.getElementById("ia_products_hotpage_numbers").childNodes;
								for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																							// next
									pages[i].visibility = 'visible';
								}
								for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																							// next
									pages[i].visibility = 'hidden';
								}
								activePage = 'iapage1';
							});
		}
	} else if (jQuery.inArray(widgetMode, brandWidget) > -1) {

		/* Brand Widgets */

		widgetElement = brandWidgetElement[jQuery.inArray(widgetMode,
				brandWidget)];
		if (!document.getElementById(widgetElement)) {
			return;
		}
		document.getElementById(widgetElement).innerHTML = "";
		var html = "";
		var respData = response.data.brands;
		/* response check addition for 'Hot Brands' start */
		if (response.data.brands.length > 0) {
			html += '<div class="wrapper"><h1 class="">'
					+ brandWidgetTitle[jQuery.inArray(widgetMode, brandWidget)]
					+ '</h1><ul class="feature-brands ia_feature_brands">';
		} else {
			$("#ia_brands_hot_searches").css("background-color", "#FFFFFF");
		}
		/* response check addition for 'Hot Brands' end */
		numRec = 0;

		jQuery
				.each(
						respData,
						function() {
							queryUrl = this.url + '?req=' + response.request_id;
							if (numRec < 3) {
								html += '<li><a href="' + queryUrl
										+ '" style=""><img class="image" src="'
										+ this.image_url
										+ '" width="432" height="439">';
								// logo and tagline, to be made obsolete with
								// larger image
								html += '<img class="logo" src="'
										+ this.logo_image_url
										+ '" width="140" height="43">';
								html += '<span>' + this.description + '</span>';
								html += '</a></li>';
							} else if (numRec === 3) {
								html += '</ul><ul class="more-brands">';
							}
							if (numRec >= 3) {
								html += '<li><a href="'
										+ queryUrl
										+ '"><img class="logo  hover-logo" src="'
										+ this.logo_image_url
										+ '" width="160" height="43">';
								html += '<img class="background" src="'
										+ this.overlay_image_url
										+ '" width="206" height="206" style="left: 0";></a></li>';
							}
							numRec++;
							if (numRec === 9) {
								return false;
							}
						});

		html += '</ul></div></div>';
		document.getElementById(widgetElement).innerHTML = html;
	} else if (jQuery.inArray(widgetMode, categoryWidget) > -1) {

		/* Category Widgets */

		widgetElement = categoryWidgetElement[jQuery.inArray(widgetMode,
				categoryWidget)];
		if (!document.getElementById(widgetElement)) {
			return;
		}
		document.getElementById(widgetElement).innerHTML = "";
		var html = "";
		var respData = "";
		if (widgetMode === "categories/recent") {
			respData = response.data.recommendations;
		} else {
			respData = response.data.categories;
		}
		if (respData === null) {
			return;
		}
		if (widgetMode !== "categories/recent") {
			html += '<div class="wrapper">';
		}
		if (widgetMode === "categories/recent") {
			html += '<h1 class="">'
					+ categoryWidgetTitle[jQuery.inArray(widgetMode,
							categoryWidget)] + '</h1>';
		} else {
			html += '<h1 class="">'
					+ categoryWidgetTitle[jQuery.inArray(widgetMode,
							categoryWidget)] + '</h1>';
			html += '<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference owl-carousel owl-theme" id="mplCategoryCarousel" style="opacity: 1; display: block;">';
		}
		var numRec = 0;
		jQuery
				.each(
						respData,
						function() {
							queryUrl = this.url + '?req=' + response.request_id;
							if (widgetMode === "categories/recent") { // FlyOut
																		// Menu
								html += '<div style="display:inline-block; padding: 0; width: 100px; margin: 0px 10px;"><a href="'
										+ queryUrl
										+ '"><img class="image" src="'
										+ this.image_url + '" width="113"/>';
								html += '<p style="text-align: center;margin-top: 5px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">'
										+ this.name + '</p></a></div>';
							} else { // Normal Widget
								html += '<div class="item slide">';
								html += '<a href="'
										+ queryUrl
										+ '"><img src="'
										+ this.image_url
										+ '" class="image" style="height: 324px;"></a><span>'
										+ this.name + '</span>';
								html += '<a class="shop_link" href="'
										+ queryUrl
										+ '"><b>SHOP NOW</b></a></div>';
							}
							numRec++;
							/*
							 * if(numRec === respData.length) { return false; }
							 */
						});
		if (widgetMode !== "categories/recent") {
			html += '</div>';
		}

		html += '</div>';

		if (widgetMode !== "categories/recent") {
			html += '</div>';
		}
		if (numRec > 0) {
			document.getElementById(widgetElement).innerHTML = html;
		}
		var carousel = $("#mplCategoryCarousel");

		carousel.owlCarousel({
			items : 4,
			loop : true,
			nav : true,
			dots : false,
			navText : [],
			responsive : {
				// breakpoint from 0 up
				0 : {
					items : 1,
					stagePadding : 50,
				},
				// breakpoint from 480 up
				480 : {
					items : 2,
					stagePadding : 50,
				},
				// breakpoint from 768 up
				768 : {
					items : 3,
				},
				// breakpoint from 768 up
				1280 : {
					items : 5,
				}
			}
		/*
		 * items : 4,
		 * 
		 * navigation:true,
		 * 
		 * navigationText : [],
		 * 
		 * pagination:false,
		 * 
		 * itemsDesktop : [1199,3],
		 * 
		 * itemsDesktopSmall : [980,2],
		 * 
		 * itemsTablet: [768,2],
		 * 
		 * itemsMobile : [479,1],
		 * 
		 * rewindNav: false,
		 * 
		 * lazyLoad:true,
		 * 
		 * navigation : true,
		 * 
		 * rewindNav : false,
		 * 
		 * scrollPerPage : true
		 */
		});

	} else if (jQuery.inArray(widgetMode, collectionWidget) > -1) {

		/* Collection Widgets */

		widgetElement = collectionWidgetElement[jQuery.inArray(widgetMode,
				collectionWidget)];
		if (!document.getElementById(widgetElement)) {
			return;
		}
		document.getElementById(widgetElement).innerHTML = "";
		document.getElementById(widgetElement).className = "feature-collections";
		var html = "";
		var respData = response.data.bundles;
		if (respData === null) {
			return;
		}
		html += '<div class="wrapper background"><h1 class="">'
				+ collectionWidgetTitle[jQuery.inArray(widgetMode,
						collectionWidget)] + '</h1><ul class="collections">';
		var numRec = 0;
		jQuery
				.each(
						respData,
						function() {
							queryUrl = this.url + '?req=' + response.request_id;
							if (numRec < 1) { // first one show both images
								html += '<li class="chef sub "><img class="background brush-strokes-sprite sprite-water_color_copy_4" src="//'
										+ mplStaticResourceHost
										+ '/store/_ui/responsive/common/images/transparent.png">';
								html += '<img class="background brush-strokes-sprite sprite-water_color_copy_3" src="//'
										+ mplStaticResourceHost
										+ '/store/_ui/responsive/common/images/transparent.png">';
								html += '<h3>' + this.name + '</h3>';
								html += '<img class="image" src="//'
										+ DamMediaHost
										+ ''
										+ this.top_left_image_url
										+ '" style="margin-right: 20px;width: calc(40% - 140px);">';
								html += '<img class="image" src="//'
										+ DamMediaHost
										+ ''
										+ this.top_right_image_url
										+ '" style="width: calc(60% - 140px);width: -webkit-calc(60% - 140px);">';
								html += '<span>' + this.description + '';
								html += '<a href="' + queryUrl
										+ '">Shop The Collection</a></span>';
								html += '</li>';
							} else if (numRec < 2) { // show only one image,
														// different format
								html += '<li class=" sub "><img class="image" src="//'
										+ DamMediaHost
										+ ''
										+ this.bottom_left_image_url
										+ '"><div class="copy"><h3>'
										+ this.name + '</h3>';
								html += '<a href="'
										+ queryUrl
										+ '">The Hottest New Styles</a></div></li>';
							} else if (numRec < 3) {
								html += '<li class="spotlight sub "><img class="image" src="//'
										+ DamMediaHost
										+ ''
										+ this.bottom_right_image_url
										+ '"><div class="copy"><h3>'
										+ this.name + '</h3>';
								html += '<a href="'
										+ queryUrl
										+ '">The Hottest New Styles</a></div></li>';
							} else {
								return;
							}
							numRec++;
						});
		html += '</ul></div></div>';
		if (numRec > 0) {
			document.getElementById(widgetElement).innerHTML = html;
		}
	} else {

		/* We should never get to this part of the code */

		// console.log("widget mode we don't account for: " + widgetMode);
	}

}
$(document).on('click', ".IAQuickView,.iaAddToCartButton", function(e) {
	e.preventDefault();
})
