/* GIGYA PLUGIN */
function registerUserGigya(eventObject)
{
	var encodedUID = encodeURIComponent(eventObject.UID);
	var encodedTimestamp=encodeURIComponent(eventObject.timestamp);
	var  encodedSignature=encodeURIComponent(eventObject.signature);
// console.log("SOCIAL LOGIN REFERER:-"+ window.location.href)
		 $.ajax({
				url : ACC.config.encodedContextPath + "/oauth2callback/socialLogin/",
				data : {
					'referer' : window.location.href,
					'emailId' : eventObject.user.email,
					'fName':  eventObject.user.firstName,
					'lName' : 	eventObject.user.lastName,
					'uid'		: encodedUID,
					'timestamp'	 :encodedTimestamp,
					'signature' :encodedSignature,
					'provider' :eventObject.user.loginProvider
					},
				type : "GET",
				cache : false,
				success : function(data) {
					// alert("success login page :- "+data);
					if(!data)							
						{
						
						}
						else
						{
							if(data.indexOf(ACC.config.encodedContextPath) > -1)
							{
								window.open(data,"_self");
							}
							else
							{
							var hostName=window.location.host;
							if(hostName.indexOf(':') >=0)
							{
								window.open(ACC.config.encodedContextPath +data,"_self");
							}	
							else
								{
							window.open("https://"+hostName+ACC.config.encodedContextPath +data,"_self");
								}
							}
							
						}	
				},
				error : function(resp) {
					console.log("Error Occured Login Page" + resp);					
				}
			});
	 
}

        // This method is activated when the page is loaded
        function loadGigya() {
            // register for login event
            gigya.socialize.addEventHandlers({
                    context: { str: 'congrats on your' }
                    , onLogin: onLoginHandlerGigya                   
                    });
        }
        // onLogin Event handler
        function onLoginHandlerGigya(eventObj) {
           // console.log(eventObj.context.str + ' ' + eventObj.eventName + '
			// to ' + eventObj.provider
          // + '!\n' + eventObj.provider + ' user ID: ' +
			// eventObj.user.identities[eventObj.provider].providerUID);
            
            registerUserGigya(eventObj);      
            
         }  
       /* End Gigya Social Login */
        
    		var ratingsParams = {
    			categoryID : $('input[name=gigya_product_root_category]').val(),
    			streamID : $('input[name=gigya_product_code]').val(),
    			containerID : 'ratingDiv',
    			linkedCommentsUI : 'commentsDiv',
    			showCommentButton : 'true',
    			onAddReviewClicked:reviewClick,
    		}
    		
    		gigya.comments.showRatingUI(ratingsParams);

    		var params = {
    			categoryID : $('input[name=gigya_product_root_category]').val(),
    			streamID : $('input[name=gigya_product_code]').val(),
    			scope : 'both',
    			privacy : 'public',
    			version : 2,
    			containerID : 'commentsDiv',
    			onCommentSubmitted:reviewCount, 
    			cid : '',
    			enabledShareProviders : 'facebook,twitter',
    			enabledProviders : 'facebook,google,twitter', // login
																// providers
																// that should
																// be displayed
																// when click
																// post
    			onLoad :commentBox,
    			onError: onErrorHandler
    			// userAction: shareUserAction
    		}
    		gigya.comments.showCommentsUI(params);	
    		
    		//TISPRD-3152 FIX
    		var pageName = $('#pageName').val();
    		if(typeof(pageName)!= "undefined"  && pageName == 'Product Details'){
    		$(document).ready(function(){
    			getRating($('input[name=gigya_api_key]').val(),$('input[name=gigya_product_code]').val(),$('input[name=gigya_product_root_category]').val());		
     		});
    		}
    		//TISPRD-3152 FIX ends
    	function commentBox(response)
    	{		
    		$('#commentsDiv .gig-comments-subscribe').hide();
    		$('#commentsDiv .gig-composebox-logout').hide();
    		CheckonReload();
    		
    	}
    	
    	function onErrorHandler(responseObj){
    		$(".gig-composebox-error").text(responseObj.errorDetails);
    		$(".gig-composebox-error").show();
    		//added for tealium to capture review submit error
    		if(pageType == 'product'){
    		utag.link(
   					{"error_type": "review_submit_error", "error_message": responseObj.errorMessage});

    		}
    		}
    		


    	function reviewCount(response) {
    		
    		//TPR-675 PART- A  starts
   		 var categoryId = $('input[name=gigya_product_root_category]').val();
   		 var title= response.commentTitle;
   		 console.log("Review Count inside show comments ui   "+response);
   		 var overall= response.ratings._overall; 
   		 var fit = "not_applicable";
   		 
		 var ease_of_use = "not_applicable";
    		
    		if(categoryId  != "Electronics" )
    			{
    			var fit= response.ratings.Fit;
    			
    			var quality= response.ratings.Quality;
    			
    			var value_for_money= response.ratings['Value for Money'];
    			 
    			}
    	
    		if(categoryId == "Electronics"){
    			
    			var ease_of_use = response.ratings['Ease of use'];
    			
    			var quality= response.ratings.Quality;
    			
    			var value_for_money = response.ratings['Value for Money'];
    			
    		}
    		  utag.link(
   					{"link_text": title.replace(/ /g,'_').toLowerCase() , "event_type" : "review_post" , "review_overall_rating" : overall , "review_quality" : quality ,
   						"review_ease_of_use" : ease_of_use	, "review_fit" : fit , "review_value_for_money": value_for_money,"user_product_rating":overall,"Seller_rating" : " " ,product_id : productIdArray}
   				);
    		  
    		//TPR-675 PART-A  ends
    		 if(typeof _satellite !="undefined"){
        		  _satellite.track('review_submit_click');
        	 }
    		getRating($('input[name=gigya_api_key]').val(),$('input[name=gigya_product_code]').val(),$('input[name=gigya_product_root_category]').val());
    	}
    	
    	function reviewClick(response) {
    		CheckUserLogedIn();
    	}
    	
    	
    	function getRating(key,productCode,category)
    	{
    		var url = "https://comments.us1.gigya.com/comments.getStreamInfo?apiKey="+key+"&categoryID="+category+"&streamId="+productCode+"&includeRatingDetails=true&format=jsonp&callback=?";
    		 
    		$.getJSON(url, function(data){
    		//	console.log(data);
    			if(data!=undefined && data.streamInfo!=undefined){	
    				
    			
    		  	var totalCount=data.streamInfo.ratingCount;
    			//Reverse the source array
    			var ratingArray = data.streamInfo.ratingDetails._overall.ratings;
    			ratingArray  = ratingArray.reverse();
    			
    			  $("div.rate-details div.after").each(function(count){			  
    					var countIndiv=ratingArray[count];								
    					$("div.rate-bar div.rating").eq(count).css({width:countIndiv/totalCount*100+"%"});
    					$("div.rate-details div.after").eq(count).text(ratingArray[count]);
    					
    				})
    				
    				var avgreview=data.streamInfo.avgRatings._overall;
    				var raingcount=data.streamInfo.ratingCount;
    				$(".product-detail ul.star-review a").empty();
    				$(".product-detail ul.star-review li").attr("class","empty");
    				
    	 			var rating = Math.floor(avgreview);
    		 		var ratingDec = avgreview - rating;
    		 		for(var i = 0; i < rating; i++) {
    		 			$("#pdp_rating"+" li").eq(i).removeClass("empty").addClass("full");
    		 			}
    		 		if(ratingDec!=0)
    		 			{
    		 			$("#pdp_rating"+" li").eq(rating).removeClass("empty").addClass("half");
    		 			} 
    		 		
    		 		//TISUATPII-471 fix
    		 		
    		 		if(raingcount == 1){
    					$(".gig-rating-readReviewsLink_pdp").text(raingcount+" REVIEW");
    					$('#ratingDiv .gig-rating-readReviewsLink').text(data.streamInfo.ratingCount+" REVIEW");
    					}
    					else if(raingcount > 0)
    						{
    						$(".gig-rating-readReviewsLink_pdp").text(raingcount+" REVIEWS");
    						$('#ratingDiv .gig-rating-readReviewsLink').text(data.streamInfo.ratingCount+" REVIEWS");
    						}
    				$('#customer').text("Customer Reviews (" + data.streamInfo.ratingCount + ")");
    				
    				//added for tealium to get average product rating
    				if(typeof utag !="undefined"){
   				     utag.link({"product_rating":avgreview});
    				}
    				
    		  }
    		  
    		//TISUATPII-471 fix
    		  var ratingsParams = {
    			categoryID : category,
    			streamID : productCode,
    			containerID : 'ratingDiv',
    			linkedCommentsUI : 'commentsDiv',
    			showCommentButton : 'true',
    			onAddReviewClicked:	function(response) {
    				CheckUserLogedIn();
    		 }
    				
    		
    		  }  
    		  
    		  gigya.comments.showRatingUI(ratingsParams);
//    		$.getJSON("https://comments.us1.gigya.com/comments.getStreamInfo?apiKey="+key+"&categoryID="+category+"&streamId="+productCode+"&includeRatingDetails=true&format=jsonp&callback=hello",
//    		         function(data) {
//    			
//    			$(".rate-details .after").each(function(count){
//    				var totalCount=data.streamInfo.ratingCount;
//    				var countIndiv=data.streamInfo.ratingDetails._overall.ratings[count];
//    				$(".rate-bar .rating").eq(count).css({width:countIndiv/totalCount*100});
//    				$(".rate-details .after").eq(count).text(data.streamInfo.ratingDetails._overall.ratings[count]);
//    				
//    			})
//    			
//    			var avgreview=data.streamInfo.avgRatings._overall;
//    			var raingcount=data.streamInfo.ratingCount;
//    			
//    			rating(avgreview,raingcount);
//    			
//    			$('#customer').text("Customer Reviews (" + data.streamInfo.ratingCount + ")");
//    			
//    		          });
    		

    	});
    }
    	
    	function CheckUserLogedIn() {
    		
    		//CODE change as part of PT defect TISPT-180
    		var user_id	     	= getCookie("mpl-user");
    		var user_type		= getCookie("mpl-userType");
    		if(user_type =='session' && user_id=='anonymous'){
    			gotoLogin();
    		}
    	}
    	/*$(function(){*/
    	//Using document.ready as the js load is being taken up in js call stack as per requirement of UF-95 for login and checkout-login pages
    	$(document).ready(function(){
    		if(typeof($('#loginDiv').html())!= undefined){
    			gigya.socialize.showLoginUI({
    	            height: 100
    	            ,width: 330
    	            ,showTermsLink:false // remove 'Terms' link
    	            ,hideGigyaLink:true // remove 'Gigya' link
    	            ,buttonsStyle: 'signInWith' // Change the default buttons design to "Full Logos" design
    	            //,showWhatsThis: true // Pop-up a hint describing the Login Plugin, when the user rolls over the Gigya link.
    	            ,containerID: 'loginDiv' // The component will embed itself inside the loginDiv Div
    	            ,cid:''
    	            ,enabledProviders : 'facebook,google'
    	            });	
    		}
    		
    		if(typeof($('#loginDivReg').html())!= undefined){
    			gigya.socialize.showLoginUI({
    	            height: 100
    	            ,width: 330
    	            ,showTermsLink:false // remove 'Terms' link
    	            ,hideGigyaLink:true // remove 'Gigya' link
    	            ,buttonsStyle: 'signInWith' // Change the default buttons design to "Full Logos" design
    	            //,showWhatsThis: true // Pop-up a hint describing the Login Plugin, when the user rolls over the Gigya link.
    	            ,containerID: 'loginDivReg' // The component will embed itself inside the loginDiv Div
    	            ,cid:''
    	            ,enabledProviders : 'facebook,google'
    	            });
    		}
    		
	    		if(typeof($('#loginDivCheckout').html())!= undefined){
	    			gigya.socialize.showLoginUI({
	    	            height: 100
	    	            ,width: 330
	    	            ,showTermsLink:false // remove 'Terms' link
	    	            ,hideGigyaLink:true // remove 'Gigya' link
	    	            ,buttonsStyle: 'signInWith' // Change the default buttons design to "Full Logos" design
	    	            //,showWhatsThis: true // Pop-up a hint describing the Login Plugin, when the user rolls over the Gigya link.
	    	            ,containerID: 'loginDivCheckout' // The component will embed itself inside the loginDiv Div
	    	            ,cid:''
	    	            ,enabledProviders : 'facebook,google'
	    	            });	
	    		}
	    		
	    		if(typeof($('#loginDivsiginflyout').html())!= undefined){
	    			 gigya.socialize.showLoginUI({
	    		            height: 100
	    		            ,width: 330
	    		            ,showTermsLink:false // remove 'Terms' link
	    		            ,hideGigyaLink:true // remove 'Gigya' link
	    		            ,buttonsStyle: 'signInWith' // Change the default buttons design to "Full Logos" design
	    		            //,showWhatsThis: true // Pop-up a hint describing the Login Plugin, when the user rolls over the Gigya link.
	    		            ,containerID: 'loginDivsiginflyout' // The component will embed itself inside the loginDiv Div
	    		            ,cid:''
	    		            ,enabledProviders : 'facebook,google'
	    		            });
	
	    		}
    		});
    
    	function getPdpRatingOnSizeSelect(key,productCode,category)//UF-60
    	{
    		//As acc.productDetails.js already has a getRating method hence wrapping getRating of acc.gigya.js in getPdpRatingOnSizeSelect so that it can be reused
    		getRating(key,productCode,category);

    	}
    	function lazyLoadGigyaCommentsUi(){//UF-60
    		
    		if ($(window).scrollTop() + $(window).height() >= ($('#ReviewSecion').offset().top)) {
    	        if($('#ReviewSecion .commentcontent').attr('loaded')!="true") {
    	            //not in ajax.success due to multiple sroll events
    	            $('#ReviewSecion .commentcontent').attr('loaded', true);

    	            //ajax goes here
    	            //by theory, this code still may be called several times
    	            if ($('#ReviewSecion ul#commentsDiv').children().length == 0) {
    	            	var params = {
    	    	    			categoryID : $('input[name=gigya_product_root_category]').val(),
    	    	    			streamID : $('input[name=gigya_product_code]').val(),
    	    	    			scope : 'both',
    	    	    			privacy : 'public',
    	    	    			version : 2,
    	    	    			containerID : 'commentsDiv',
    	    	    			onCommentSubmitted:reviewCount, 
    	    	    			cid : '',
    	    	    			enabledShareProviders : 'facebook,twitter',
    	    	    			enabledProviders : 'facebook,google,twitter', // login
    	    																	// providers
    	    																	// that should
    	    																	// be displayed
    	    																	// when click
    	    																	// post
    	    	    			onLoad :commentBox,
    	    	    			onError: onErrorHandler
    	    	    			// userAction: shareUserAction
    	    	    		}
	    	    	    gigya.comments.showCommentsUI(params);
    	            }
    	        }
    		}
    	}
