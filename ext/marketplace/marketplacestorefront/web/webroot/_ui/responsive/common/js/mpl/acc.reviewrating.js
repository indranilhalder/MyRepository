if(typeof(arrayrating)!= "undefined"){
	$(document).ready(function(){
		
		$(".edit").click(function(e){
			e.preventDefault;
			var indexElement = $(this).attr("data-index");
			$("div[data-rating-all="+indexElement+"]").hide();
			if(indexElement!= undefined){
				var reviewHeading = $(".reviewHeading"+indexElement);
				var reviewComment = $(".reviewComment"+indexElement);
				var reviewMedia = $(".reviewMedia"+indexElement);
				
				var updateButtons = $(".updateButtons"+indexElement);
				
				var reviewCommentText = $(".reviewComment"+indexElement).text();
				var reviewHeadingText = $(".reviewHeading"+indexElement).text();
				if(reviewHeadingText == ""){
					reviewHeadingText = $("input[name=updateReviewHeading"+indexElement+"]").val();
				}
				$(reviewHeading).html("<input class='inputBox' type='text' name='updateReviewHeading"+indexElement+"' value='"+reviewHeadingText+"'/>");
				$(reviewComment).html("<textarea name='updateReviewComment"+indexElement+"' rows='5' cols='30'>"+reviewCommentText+"</textarea>");
				if($(".hiddenMediaUrl"+indexElement).val()!= ""){
					$(reviewMedia).html("<input class='inputBox' type='text' name='updateReviewMedia"+indexElement+"' value='"+$(".hiddenMediaUrl"+indexElement).val()+"'/>");
				}
				$(reviewHeading).find('input.inputBox').focus();
				$(".rating-div"+indexElement).show();
				$(".rating-div"+indexElement).find("ul").removeClass("rate");
				$(updateButtons).show();
				
				$(".rating-div"+indexElement+" .rateEdit span").removeAttr('style');
				
				var arrayIndex = arrayrating[indexElement];
				
				for(key in arrayIndex) {
					var $rate = $(".rating-div"+indexElement+" .rateEdit[data-rating-name"+indexElement+"="+key+"]");
					$rate.data($rate.attr('data-rating-name'+indexElement),arrayIndex[key]/20);
					$rate.find("li span").removeClass('full');
					for( var i = 0; i < $rate.data($rate.attr('data-rating-name'+indexElement)) ; i++) {	
						$rate.find("li span").eq(i).addClass("full");		
				      }
				}
				
			}
			
		});
		
		$("input[name=cancel]").click(function(){
			
			var indexElement =  $(this).attr("data-index");
			$("div[data-rating-all="+indexElement+"]").show();
			var reviewHeading = $(".reviewHeading"+indexElement);
			var reviewComment = $(".reviewComment"+indexElement);
			var reviewUrl = $(".reviewMedia"+indexElement);
			
			if(indexElement != undefined){
				
				var originalHeading = $(".hiddenReviewHeading"+indexElement).val();
				var originalComment = $(".hiddenReviewComment"+indexElement).val();
				var originalUrl = $(".hiddenMediaUrl"+indexElement).val();

				$(reviewHeading).html(originalHeading);
				$(reviewComment).html(originalComment);
				$(reviewUrl).html(originalUrl);
				$(this).parent().hide();
			}
			$(".rating-div"+indexElement).hide();
			ratingReview(arrayrating);
			$(".errorUpdateReview"+indexElement).empty();
			$(".errorUpdateRating"+indexElement).empty();
		});
		
		$("input[name=update]").click(function(){
			var isValidated = true;
			var indexElement =  $(this).attr("data-index");
			//alert("inside update");
			var updatedReviewHeading = $("input[name=updateReviewHeading"+indexElement+"]").val();
			var updatedCommentTitle = $("textarea[name=updateReviewComment"+indexElement+"]").val();
			var updatedMediaUrl = $("input[name=updateReviewMedia"+indexElement+"]").val();
			
			if(updatedReviewHeading == undefined ||updatedReviewHeading.replace(/\s/g, '')  == "")		
			{		
			    $(".errorUpdateReview"+indexElement).html("<p>Please enter comments.Comment Title cannot be left blank.</p>");		
			    isValidated=false;		
			}else if(updatedReviewHeading.length > 250){
				$(".errorUpdateReview"+indexElement).html("<p>Review title cannot be greater than 250 charecters.</p>");		
			    isValidated=false;
			}
			if(updatedCommentTitle == undefined || updatedCommentTitle.replace(/\s/g, '')  == "")		
			{		
			    $(".errorUpdateReview"+indexElement).html("<p>Please enter comments.Comment text cannot be left blank.</p>");		
			    isValidated=false;		
			}else if(updatedCommentTitle.length > 5000){
				$(".errorUpdateReview"+indexElement).html("<p>Review text cannot be greater than 5000 charecters.</p>");		
			    isValidated=false;	
			}
			if(updatedMediaUrl == undefined || updatedMediaUrl.replace(/\s/g, '')  == "")		
			{		
			    $(".errorUpdateReview"+indexElement).html("<p>Please enter attachment URL. Attachment URL cannot be left blank.</p>");		
			    isValidated=false;		
			}else if(updatedMediaUrl.length > 100){
				$(".errorUpdateReview"+indexElement).html("<p>Attachment URL cannot be greater than 100 charecters.</p>");		
			    isValidated=false;	
			}
			//TISSTRT-290 fix
			if((updatedReviewHeading.length > 250) && (updatedCommentTitle.length > 5000))		
			{		
			    $(".errorUpdateReview"+indexElement).html("<p>Review title cannot be greater that 250 characters<br/>Review text cannot be greater than 5000 charecters.</p>");		
			    isValidated=false;		
			}
			var x = updatedReviewHeading.length;
			var y = updatedCommentTitle.length;
			if(x > 0 && y > 0 && isValidated ) {
				$(".errorUpdateReview"+indexElement).html("");
				$(".review-block"+indexElement).block({message:$("#updateReviewcontainer").html()});
			}
			
			
		});
		
		$(document).on("click","button.updateReviewConfirmation",function(){
			//validate the text first
			var isReload = true;
			var isValidated=true;
			var isValidated_e =true;
			var indexElement =  $(this).parents("li.review-li").attr("data-index");
			$(".errorUpdateReview"+indexElement).empty();
			$(".errorUpdateRating"+indexElement).empty();
			
			var updatedReviewHeading = $("input[name=updateReviewHeading"+indexElement+"]").val();
			var updatedCommentTitle = $("textarea[name=updateReviewComment"+indexElement+"]").val();
			var updatedMediaUrl = $("input[name=updateReviewMedia"+indexElement+"]").val();
			
			var categoryID = $(".categoryID"+indexElement).val();
			var streamID = $(".streamID"+indexElement).val();
			var commentID = $(".commentID"+indexElement).val();
			
			if(categoryID!= "" && streamID!= "" && commentID != ""){
				
				var updated_rating_overall = $("ul[data-rating-name"+indexElement+"=overall] li span.full").length;
				var updated_rating_fit = $("ul[data-rating-name"+indexElement+"=fit] li span.full").length;
				var updated_rating_vfm = $("ul[data-rating-name"+indexElement+"=value_for_money] li span.full").length;
				var updated_rating_quality = $("ul[data-rating-name"+indexElement+"=quality] li span.full").length;
				var updated_rating_efu = $("ul[data-rating-name"+indexElement+"=easeOfUse] li span.full").length;
				
				var ratings = null;
				var ratingsJSON = null;
				
				if(categoryID == "Electronics"){
					ratings = "{'_overall':"+updated_rating_overall+", 'Quality':"+updated_rating_quality+",'Ease of use':"+updated_rating_efu+",'Value for Money':"+updated_rating_vfm+"}";
					ratingsJSON = {overall:updated_rating_overall,easeOfUse:updated_rating_efu,value_for_money:updated_rating_vfm,quality:updated_rating_quality};
				}else if(categoryID == 'Clothing' || categoryID == 'Footwear' ){
					ratings = "{'_overall':"+updated_rating_overall+", 'Quality':"+updated_rating_quality+",'Fit':"+updated_rating_fit+",'Value for Money':"+updated_rating_vfm+"}";	
					ratingsJSON = {overall:updated_rating_overall,fit:updated_rating_fit,value_for_money:updated_rating_vfm,quality:updated_rating_quality};
				}else{
					ratings = "{'_overall':"+updated_rating_overall+", 'Quality':"+updated_rating_quality+",'Fit':"+updated_rating_fit+",'Value for Money':"+updated_rating_vfm+"}";
				}
				
				if(isValidated){
					$.ajax({
						url:"review/edit",
						type:"POST",
						dataType:"JSON",
						data:{categoryID:categoryID,streamID:streamID,commentID:commentID,commentText:updatedCommentTitle,commentTitle:updatedReviewHeading,updatedMediaUrl:updatedMediaUrl,ratings:ratings},
						beforeSend:function(){
							var msg = "<h1 style='color:white'><span style='line-height:40px;font-size:'>Please Wait...<span></h1>";
							$(".review-block"+indexElement).block({ message: msg });
						},
						success:function(data){
							if(data){
								if(data.status == "success"){
									if(indexElement!= undefined){
										var reviewHeading = $(".reviewHeading"+indexElement);
										var reviewComment = $(".reviewComment"+indexElement);
										var updateButtons = $(".updateButtons"+indexElement);
										var starRating = $(".rating-div"+indexElement);
										
										$(reviewHeading).html(updatedReviewHeading);
										$(reviewComment).html(updatedCommentTitle);
										$(updateButtons).hide();
										$(starRating).hide();
										var easeOfUse = null;
										var fit = null;
										
										if(categoryID == "Electronics"){
											easeOfUse = ratingsJSON.easeOfUse;
											easeOfUse = (easeOfUse/5) * 100;
										}else{
											fit = ratingsJSON.fit;
											fit = (fit/5) * 100;
										}
										
																			
										var quality = ratingsJSON.quality;
										quality = (quality/5) * 100;
										
										var value = ratingsJSON.value_for_money;
										value = (value/5) * 100;
										
										//over all rating 
										$("ul.rating-stars[data-rating-name"+indexElement+"=_overall] li span").remove();
										$("ul.rating-stars[data-rating-name"+indexElement+"=_overall] li").append('<span></span>')
										for (var i = 0; i < updated_rating_overall; i++) {
											$("ul.rating-stars[data-rating-name"+indexElement+"=_overall] li span").eq(i).addClass("full");
										}
										
										if(easeOfUse == null){
											$("div[data-rating"+indexElement+"=fit]").attr("style","width:"+fit+"%");
										}else{
											$("div[data-rating"+indexElement+"=easeOfUse]").attr("style","width:"+easeOfUse+"%");
										}
										$("div[data-rating"+indexElement+"=value]").attr("style","width:"+value+"%");
										$("div[data-rating"+indexElement+"=quality]").attr("style","width:"+quality+"%");
										$("div[data-info-id="+indexElement+"]").show();
									}
								}else if(data.status == "failed"){
									$("div[data-danger-id="+indexElement+"]").show();
									if(data.error != ""){
										console.log(">>> "+data.error);
										var htmlError = $("div[data-danger-id="+indexElement+"]").html();
										htmlError = htmlError+"<br><strong><b>Error:</b>"+data.error;
										$("div[data-danger-id="+indexElement+"]").html(htmlError);
										isReload = false;
									}
									
									
								}
							}
						},
						fail:function(){
							globalErrorPopup("Please try after some time.");
						},
						complete:function(){
							$(".review-block"+indexElement).unblock();
							if(isReload){
								window.location.reload();
							}
							}
					});
				}
			} 
		});
		$(".delete").click(function(){
			var indexElement =  $(this).attr("data-del-index");
			$(".review-block"+indexElement).block({message:$("#deleteReviewcontainer").html()});
			$(this).attr("current-delete","true");
		});
		
		$(document).on("mouseenter",".rateEdit li",function() {
			$(this).parent().find("li span").removeAttr('style');
			$(this).parent().find("li span").removeClass("full");
			for (var i = 0; i <= $(this).index(); i++) {
				$(this).parent().find("li span").eq(i)
						.addClass("full");
			}
		});

		$(document).on("mouseleave",".rateEdit li",function() {
			$(this).parent().find("span").removeClass("full");
		});
		
		$(document).on("mouseleave",".rateEdit",function(){		
			var indexElement =  $(this).parents(".rating-wrapper").siblings(".update-wrapper").find("input[name='update']").attr("data-index");
			for( var i =0; i < $(this).data($(this).attr('data-rating-name'+indexElement)); i++) {		
	       		$(this).find("li span").eq(i).addClass("full");		
	        }		
		});

		$(document).on("click",".rateEdit li",function() {
			var indexElement =  $(this).parents(".rating-wrapper").siblings(".update-wrapper").find("input[name='update']").attr("data-index");
			$(this).parent().addClass("rating-done");
			$(this).parent().data($(this).parent().attr('data-rating-name'+indexElement),$(this).parent().find("li span.full").length);
					
				}); 
		
		$(".close-info,.close-danger").click(function(){
			$(this).parent().hide();
		});
		
		$('#reviewPluginContainer').on('hidden.bs.modal', function () {
		   window.location.reload();
		});
		ratingReview(arrayrating);
							
	});
			
			function deleteReview() {

				var currentDeleteHref = $("a[current-delete=true]");
				var indexElement = $(currentDeleteHref).attr("data-del-index");

				var categoryID = $(".categoryID" + indexElement).val();
				var streamID = $(".streamID" + indexElement).val();
				var commentID = $(".commentID" + indexElement).val();

				if (categoryID != "" && streamID != "" && commentID != "") {

					$.ajax({
								url : "review/delete",
								type : "POST",
								dataType : "JSON",
								data : {
									categoryID : categoryID,
									streamID : streamID,
									commentID : commentID
								},
								beforeSend : function() {
									$(".review-block" + indexElement).unblock();
									var msg = "<h1 style='color:white'><span style='line-height:40px;font-size:'>Please Wait...<span></h1>";
									$(".review-block" + indexElement).block({
										message : msg
									});
								},
								success : function(data) {
									if (data.status == "success") {
										$("div[data-info-id=" + indexElement + "]")
												.show();
										$(".review-block" + indexElement).slideUp(
												"slow");
										setTimeout(function() {
											location.reload();
										}, 2000);

									}
								},
								fail : function() {
									globalErrorPopup("Please try after some time.");
								},
								complete : function() {
									$(".review-block" + indexElement).unblock();
								}
							});
				}
			}
			function closeModal(element) {
				var blockedElement = $(element).closest("li");
				$(blockedElement).unblock();
			}
			function ratingReview(r) {
				var index = 0;
				$.each(r,function(rk,rv){
				 	$.each(rv,function(k,v){
						v = (5 * v)/100;
						var base = Math.floor(v);
						for (var i = 0; i < base; i++) {
						$("ul.rating-stars[data-rating-name"+index+"="+k+"] li span").eq(i).addClass("full");
					}
						$(".rating-stars[data-rating-name"+index+"="+k+"] li span").eq(base).css("width",(v - base) * 100 + "%");
					}); 
				 	index++;
				});
				}
			
		    function reviewPopUpDisplay(rootCategory , productCode , productTitle,productBrand,id){
		    	
		    	$(".popUpProductTitle").text(productTitle);
		    	$(".popUpProductBrand").text(productBrand);
		    	var x = $("#new-review-link"+productCode).siblings().find(".picZoomer-pic").attr("src");
		    	
		    	if(typeof(x) == "undefined" || x == ""){
		    		var noimg = $("#no-image-link"+productCode).find("div.image").find("img").attr("src");
		    		$(".review-image").attr("src", noimg);
		    	}else{
		    		$(".review-image").attr("src", x);
		    	}
				var ratingsParams = {
						categoryID : rootCategory,
						streamID : productCode,
						containerID : 'ratingDiv',
						linkedCommentsUI : 'commentsDiv',
						showCommentButton : 'true',
						onAddReviewClicked:reviewClick,
					}
					
					gigya.comments.showRatingUI(ratingsParams);

					var params = {
						categoryID : rootCategory,
						streamID : productCode,
						scope : 'both',
						privacy : 'public',
						version : 2,
						containerID : 'commentsDiv',
						/*onCommentSubmitted:reviewCount,*/ 
						cid : '',
						enabledShareProviders : 'facebook,twitter',
						enabledProviders : 'facebook,google,twitter', // login providers that should be displayed when click post
						/*onLoad :commentBox,*/
						//userAction: shareUserAction
					}
					gigya.comments.showCommentsUI(params);	
		    }	
			
			function reviewClick(response) {
				CheckUserLogedIn();
			}
			
}
