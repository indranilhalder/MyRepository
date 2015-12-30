$("document").ready(function(){
    	var gender = null;
    	var categorySelected = null;
    	var selectedCats = [];
    	var apparelCatCode = null;
    	var electronicsCatCode = null;
    	var dataApparel = null;
		var dataElectronics = null;
		var resourcePath = ACC.config.commonResourcePath;
    	
    	$("#question-0-0,#question-0-1").click(function()
    				{
    					 var genderData="genderData="+$(this).val();
    					 gender = genderData;
    					 var requiredUrl = ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/gender"; 
    					 
    					 var prevSelectedCats = [];
    					 $("input[name=prevSelectedCats]").each(function(){
    						 prevSelectedCats.push($(this).val());
    					 });
    					   $.ajax({
    				        	type: 'GET',
    				        	contentType: "JSON",
    				        	url: requiredUrl,
    				            data: genderData, 	
    				        	success: function(data){
    				        		var htmlData = "";
    				        		var index = 0;
    				        		$(".gender").addClass("gender");
    				        		$(".myInterestGender").css("display","none");
    				        		$("fieldset.products").addClass("products active");
    				        		 $.each(data , function( key, value ) {
    				        			 index++;
    				        			if($.inArray(key,prevSelectedCats)!= -1){
    				        				htmlData=htmlData+"<input class='category-selection' data-l1='"+value.name+"' checked='checked' type='checkbox' name='categoryCode'"+"id='question-1-"
       				                     +index
       				                     +"' value="+key+">"
       				                     +"<label for='question-1-"
       				                     +index
       				                     +"'>"
       				                     +"<img "
       				                     +"src='"+ACC.config.commonResourcePath+"/images/qRectangle_3_copy_9.jpeg'>"
       				                     +"<span>"+value.name+"</span>"
       				                     +"</label>";
    				        				
    				        			}else{
    				        				htmlData=htmlData+"<input class='category-selection' data-l1='"+value.name+"' type='checkbox' name='categoryCode'"+"id='question-1-"
       				                     +index
       				                     +"' value="+key+">"
       				                     +"<label for='question-1-"
       				                     +index
       				                     +"'>"
       				                     +"<img "
       				                     +"src='"+ACC.config.commonResourcePath+"/images/qRectangle_3_copy_9.jpeg'>"
       				                     +"<span>"+value.name+"</span>"
       				                     +"</label>";
    				        			} 
    				                 });
    				        		 $(".products-questionnaire").html(htmlData);
    				        	},
    				        	error: function(){
    				        		alert("Something is not right! Please try after sometime");
    							}
    					});
    					 
    				});
    	
    	
    	/*
    	 * get brands for the category
    	 */

    	$("#catNext").click(function(){
    		selectedCats = [];
    		$('input[name=categoryCode]:checked').each(function() {
    			selectedCats.push($(this).val());
    		});

    		if(selectedCats.length == 1){
    			categorySelected = $('input[name=categoryCode]:checked').attr("data-l1")
    		}else if(selectedCats.length == 2){
    			categorySelected = "both";
    		}

    		var prevSelectedBrands = [];
    		$("input[name=prevSelectedBrands]").each(function(){
    			prevSelectedBrands.push($(this).val());
    		});

    		if(selectedCats.length == 0){
    			$(".error.product").text("Please select atleast one product.");
    			throw new Error("Please select atleast one product");
    		}else{
    			$(".error.product").empty();
    		}
    		var categoryData = null;
    		if(isAutomate){
    			$("#brandNext").text("Update Style Profile");
    		   categoryData="categoryData="+JSON.stringify(selectedCats)+"&modify=false";
    		   $.ajax({
       			type: "GET",
       			contentType: "JSON",
       			url: ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/modifyCategory",
       			data: categoryData, 	
       			error: function(){
       				alert("Something is not right! Please try after sometime");
       			}
       		});
    		}else{
    		   categoryData="categoryData="+JSON.stringify(selectedCats);
    		}
    		
    		var requiredUrl = ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/brands";  
    		
    		$.ajax({
    			type: "GET",
    			contentType: "JSON",
    			url: requiredUrl,
    			data: categoryData, 	
    			success: function(data){
    				var htmlData = "";
    				var index = 0;
    				$(".myInterestGender").css("display","none");
    				$(".myInterestCategory").css("display","none");
    				$("fieldset.brands").addClass("brands active");
    				$.each(data , function( key, value ) {
    					index++;
    					if($.inArray(key,prevSelectedBrands)!= -1){
    						htmlData = htmlData+'<input type="checkbox" class="allBrands" checked="checked" name="brand" '+'id="question-2-'
    						+index+'" value='+key+'>'+'<label for="question-2-'+index+'">'
    						+'<img src="'+ACC.config.commonResourcePath+'/images/logob-bNuon.png">' +
    						'<img class="hover-image" src="'+ACC.config.commonResourcePath+'/images/logou-uNuon.png">'+"<span>"+value.name+"</span>"+'</label>';
    					}else{
    						htmlData = htmlData+'<input type="checkbox" class="allBrands" name="brand" '+'id="question-2-'
    						+index+'" value='+key+'>'+'<label for="question-2-'+index+'">'
    						+'<img src="'+ACC.config.commonResourcePath+'/images/logob-bNuon.png">' +
    						'<img class="hover-image" src="'+ACC.config.commonResourcePath+'/images/logou-uNuon.png">'+"<span>"+value.name+"</span>"+'</label>';
    					}
    				});
    				$("#brandContainer").html(htmlData);
    			},
    			error: function(){
    				alert("Something is not right! Please try after sometime");
    			}
    		});
    	});

    	/*
    	 * get sub brands for the category
    	 */

    	$("#brandNext").click(function()
    			{
    		if(categorySelected == "Electronics"){
    			$("#objHeading").text("What types of electronics are you interested in?");
    		}else if(categorySelected == "Apparel"){
    			$("#objHeading").text("Which outfits represent your personal style?");
    		}else if(categorySelected == "both"){
    			$("#objHeadingApparel").text("Which outfits represent your personal style?");
    		}
    		var selectedBrandCat = [];
    		$('input[name=brand]:checked').each(function() {
    			selectedBrandCat.push($(this).val());
    		});
    		if(selectedBrandCat.length == 0){
    			$(".error.brand").text("Please select atleast one brand.");
    			throw new Error("Please select atleast one brand");
    		}else{
    			$(".error.brand").empty();
    		}
    		
    		if(isAutomate){
     		   $.ajax({
        			type: "GET",
        			contentType: "JSON",
        			url: ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/modifyBrand",
        			data: {"categoryData":JSON.stringify(selectedBrandCat)},
        			success:function(){
        				window.location.href = ACC.config.encodedContextPath + "/my-account"+"/myStyleProfile";
        			},
        			error: function(){
        				alert("Something is not right! Please try after sometime");
        			}
        		});
    		}else{
    			var requiredUrl = ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/subcategories";  
        		$.ajax({
        			type: "GET",
        			contentType: "JSON",
        			url: requiredUrl,
        			data: {"categoryData":selectedCats[0],"selectedCategory":categorySelected,"subCategoryData":JSON.stringify(selectedBrandCat)}, 	
        			success: function(data){
        				$(".myInterestGender").css("display","none");
        				$(".myInterestCategory").css("display","none");
        				$(".brandsCategory").css("display","none");
        				
        				var htmlData = "";
        				var index = 0;
        				if(categorySelected == "both"){
        					
        					$("fieldset.objects-apparel").addClass("active");
        					dataApparel = data[0];
            				dataElectronics = data[1];
            				//added 
            				console.log("Directly navigated to electronic");
            				if($.isEmptyObject(dataApparel)){
            					console.log("in empty apparel block");
            					var htmlData = "";
            					var index = 0;
            		    		$.each(dataElectronics , function( key, value ) {
            						index++;
            						htmlData = htmlData+'<input type="checkbox" name="subCatBrand"'+'id="question-4-'
            						+index+'" value='+key+'>'
            						+'<label for="question-4-'
            						+index+'">'+'<img src="'+ACC.config.commonResourcePath+'/images/dept-2.png">'+"<span>"+value.name+"</span>"+'</label>';
            					});
            		    		$("#objHeadingElectronics").text("What types of electronics are you interested in?");
            		    		$("fieldset.objects-apparel").removeClass("active");
            					$("#electronicObjects").html(htmlData);
            					$("fieldset.objects-electronics").addClass("active");
            					//added
            					console.log("Directly navigated to apparel");
            				}else if($.isEmptyObject(dataElectronics)){
            					
            					console.log("Empty electronics");
            					$.each(dataApparel , function( key, value ) {
                					index++;
                					htmlData = htmlData+'<input type="checkbox" name="subBrand"'+'id="question-3-'
                					+index+'" value='+key+'>'
                					+'<label for="question-3-'
                					+index+'">'+'<img src="'+ACC.config.commonResourcePath+'/images/dept-2.png">'+"<span>"+value.name+"</span>"+'</label>';
                				});
                				$("#apparelObjects").html(htmlData);
                				$("#apparelFinal").text("Create Style Profile");
                				
                	    		
            				}else{
            					$.each(dataApparel , function( key, value ) {
                					index++;
                					htmlData = htmlData+'<input type="checkbox" name="subBrand"'+'id="question-3-'
                					+index+'" value='+key+'>'
                					+'<label for="question-3-'
                					+index+'">'+'<img src="'+ACC.config.commonResourcePath+'/images/dept-2.png">'+"<span>"+value.name+"</span>"+'</label>';
                				});
                				$("#apparelObjects").html(htmlData);
            				}
        				}else{
        					$("fieldset.objects").addClass("active");
        					$.each(data[0] , function( key, value ) {
            					index++;
            					htmlData = htmlData+'<input type="checkbox" name="subBrandFinal"'+'id="question-3-'
            					+index+'" value='+key+'>'
            					+'<label for="question-3-'
            					+index+'">'+'<img src="'+ACC.config.commonResourcePath+'/images/dept-2.png">'+"<span>"+value.name+"</span>"+'</label>';
            				});
            				$("#objects").html(htmlData);
        				}
        			},
        			error: function(){
        				alert("Something is not right! Please try after sometime");
        			}
        		});
    		}
   			});
    	
    	$("#apparelFinal").click(function(){
    		console.log("Create profile with apparel only");
    		if($.isEmptyObject(dataElectronics)){
    			var selectedBrandCat = [];
        		$("input[name=subBrand]:checked").each(function() {
        			selectedBrandCat.push($(this).val());
        		});
        		if(selectedBrandCat.length < 3){
        			$(".error.object").text("Please select atleast three objects.");
        			throw new Error("Please select atleast three objects.");
        		}else{
        			$(".error.object").empty();
        		}
        		var requiredUrl = ACC.config.encodedContextPath + "/my-account"+"/myStyleProfile";
        		var categoryData="categoryData="+JSON.stringify(selectedBrandCat);
        		var requiredUrlData = ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/mySubCategories";
        		$.ajax({
        			type: "GET",
        			contentType: "JSON",
        			url: requiredUrlData,
        			data: categoryData, 	
        			success: function(data){
        				window.location.href=requiredUrl;
        			},
        			error: function(){
        				alert("Something is not right! Please try after sometime");
        			}
        		});
    		}else{
    			var htmlData = "";
    			var index = 0;
        		$.each(dataElectronics , function( key, value ) {
    				index++;
    				htmlData = htmlData+'<input type="checkbox" name="subCatBrand"'+'id="question-4-'
    				+index+'" value='+key+'>'
    				+'<label for="question-4-'
    				+index+'">'+'<img src="'+ACC.config.commonResourcePath+'/images/dept-2.png">'+"<span>"+value.name+"</span>"+'</label>';
    			});
        		$("#objHeadingElectronics").text("What types of electronics are you interested in?");
        		var selectedBrandSubCat = [];
        		$("input[name=subBrand]:checked").each(function() {
        			selectedBrandSubCat.push($(this).val());
        		});
        		if(selectedBrandSubCat.length < 3){
        			$(".error.object").text("Please select atleast three objects.");
        			throw new Error("Please select atleast three objects.");
        		}else{
        			$(".error.object").empty();
        		}
        		$("fieldset.objects-apparel").removeClass("active");
    			$("#electronicObjects").html(htmlData);
    			$("fieldset.objects-electronics").addClass("active");
    		}
    		
    	});

    	
    	$("#final").click(function(){

    		
    		var selectedBrandCat = [];

    		$("input[name=subBrandFinal]:checked").each(function() {
    			
    			selectedBrandCat.push($(this).val());
    		});
    		
    		if(selectedBrandCat.length < 3){
    			$(".error.object").text("Please select atleast three objects.");
    			throw new Error("Please select atleast three objects.");
    		}else{
    			$(".error.object").empty();
    		}
    		
    		var requiredUrl = ACC.config.encodedContextPath + "/my-account"+"/myStyleProfile";
    		var categoryData="categoryData="+JSON.stringify(selectedBrandCat);

    		var requiredUrlData = ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/mySubCategories";

    		jQuery.ajax({
    			type: "GET",
    			contentType: "JSON",
    			url: requiredUrlData,
    			data: categoryData, 	
    			success: function(data){
    				window.location.href=requiredUrl;
    			},
    			error: function(){
    				alert("Something is not right! Please try after sometime");
    			}
    		});
    	});
    	
    	
    	$("#electronicFinal").click(function(){

    		var selectedBrandSubCat = [];

    		$("input[name=subCatBrand]:checked").each(function() {
    			selectedBrandSubCat.push($(this).val());
    		});
    		
    		if(selectedBrandSubCat.length < 3){
    			$(".error.final").text("Please select atleast three objects.");
    			throw new Error("Please select atleast three objects.");
    		}else{
    			$(".error.final").empty();
    		}
    		var requiredUrl = ACC.config.encodedContextPath + "/my-account"+"/myStyleProfile";
    		var categoryData="categoryData="+JSON.stringify(selectedBrandSubCat);

    		var requiredUrlData = ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/mySubCategories";

    		jQuery.ajax({
    			type: "GET",
    			contentType: "JSON",
    			url: requiredUrlData,
    			data: categoryData, 	
    			success: function(data){
    				window.location.href=requiredUrl;
    			},
    			error: function(){
    				alert("Something is not right! Please try after sometime");
    			}
    		});
    	});

    	/*-------------- Previous Buttons Action --------------*/
    	$("#catPrev").click(function(){
    		$(".myInterestGender").show();
    		$(".gender").addClass("gender active");

    	});

    	$("#brandPrev").click(function(){
    		$(".myInterestCategory").show();
    		$(".products").addClass("products active");

    	}); 

    	$("#objPrev").click(function(){
    		$(".brandsCategory").show();
    		$(".brands").addClass("brands active");

    	}); 
    	
    	$("#objPrevApparel").click(function(){
    		$(".brandsCategory").show();
    		$(".brands").addClass("brands active");

    	}); 
    	
    	$("#objPrevElectronics").click(function(){
    		if($.isEmptyObject(dataApparel)){
    			$(".brandsCategory").show();
        		$(".brands").addClass("brands active");
    		}else{
    			$(".brandsSubCategoryApparel").show();
        		$("fieldset.objects-apparel").addClass("active");
    		}
    	}); 


    	/**
    	 * Remove the category from My Interest Page
    	 */	    
    	$(".close.pull-right.my-dept").click(function(e){
    		var selectedCats = [];
    		var selectedBrandCat = [];
    		var ifLastDept = $(".close.pull-right.my-dept").length;
    		//if only one dept then remove the style profile.
    		if(ifLastDept == 1){
    			$.ajax({
    				type: "GET",
    				contentType: "JSON",
    				url: ACC.config.encodedContextPath + "/my-account/myInterest/removePorfile",
    				success: function(data){
    					if(data == true){
    						window.location.href=ACC.config.encodedContextPath + "/my-account/myInterest";
    					}
    				},
    				error: function(){
    					alert("Something is not right! Please try after sometime");
    				}
    			});
    			// 
    		}else{
    			var catId = $(e.target).attr("data-categoryId");
    			selectedCats.push(catId);
    			var categoryData = JSON.stringify(selectedCats);
    			var requiredUrl = ACC.config.encodedContextPath + "/my-account/myInterest/brands";  
    			$.ajax({
    				type: "GET",
    				contentType: "JSON",
    				url: requiredUrl,
    				data: {"categoryData":categoryData , "modify":"false"}, 	
    				success: function(data){
    					$.each(data,function(k,v){
    						selectedBrandCat.push(k);
    					});
    					var bandData = JSON.stringify(selectedBrandCat);
    					var requiredUrl = ACC.config.encodedContextPath + "/my-account/myInterest/removeCategory";  
    					$.ajax({
    						type: "GET",
    						contentType: "JSON",
    						url: requiredUrl,
    						data: {"categoryData": categoryData, "brandData":bandData}, 	
    						success: function(data){
    							window.location.reload();
    						},
    						error: function(){
    							alert("Something is not right! Please try after sometime");
    						}
    					});
    				},
    				error: function(){
    					alert("Something is not right! Please try after sometime");
    				}
    			});
    		}

    	});

    	/**
    	 * Remove the brand form my style page
    	 */	    
    	$(".close.pull-right.my-brand").click(function(e){

    		var selectedBrandCat = [];
    		var brandId = $(e.target).attr("data-brandId");
    		selectedBrandCat.push(brandId);
    		var categoryData="categoryData="+JSON.stringify(selectedBrandCat);
    		$.ajax({
    			type: "GET",
    			contentType: "JSON",
    			data: categoryData, 	
    			url: ACC.config.encodedContextPath + "/my-account/myInterest/removeBrand",
    			success: function(data){
    				window.location.reload();
    			},
    			error: function(){
    				alert("Something is not right! Please try after sometime");
    			}
    		});
    	});
    });

function automateMyrecomendationBrandModification(catids){
	$("#brandNext").text("Update Style Profile");
	selectedCats = [];
	selectedCats = catids.split(",");
	if(selectedCats.length == 1){
		categorySelected = $('input[name=categoryCode]:checked').attr("data-l1");
	}else if(selectedCats.length == 2){
		categorySelected = "both";
	}

	var prevSelectedBrands = [];
	$("input[name=prevSelectedBrands]").each(function(){
		prevSelectedBrands.push($(this).val());
	});
	if(selectedCats.length == 0){
		$(".error.product").text("Please select atleast one product.");
		throw new Error("Please select atleast one product");
	}

	var categoryData="categoryData="+selectedCats+"&modify=false";
	var requiredUrl = ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/brands";  
	$.ajax({
		type: "GET",
		contentType: "JSON",
		url: requiredUrl,
		data: categoryData, 	
		success: function(data){
			var htmlData = "";
			var index = 0;
			$(".myInterestGender").css("display","none");
			$(".myInterestCategory").css("display","none");
			$("fieldset.brands").addClass("brands active");
			$.each(data , function( key, value ) {
				index++;
				if($.inArray(key,prevSelectedBrands)!= -1){
					htmlData = htmlData+'<input type="checkbox" class="allBrands" checked="checked" name="brand" '+'id="question-2-'
					+index+'" value='+key+'>'+'<label for="question-2-'+index+'">'
					+'<img src="'+ACC.config.commonResourcePath+'/images/logob-bNuon.png">' +
					'<img class="hover-image" src="'+ACC.config.commonResourcePath+'/images/logou-uNuon.png">'+"<span>"+value.name+"</span>"+'</label>';
				}else{
					htmlData = htmlData+'<input type="checkbox" class="allBrands" name="brand" '+'id="question-2-'
					+index+'" value='+key+'>'+'<label for="question-2-'+index+'">'
					+'<img src="'+ACC.config.commonResourcePath+'/images/logob-bNuon.png">' +
					'<img class="hover-image" src="'+ACC.config.commonResourcePath+'/images/logou-uNuon.png">'+"<span>"+value.name+"</span>"+'</label>';
				}
			});
			$("#brandContainer").html(htmlData);
		},
		error: function(){
			alert("Something is not right! Please try after sometime");
		}
	});
}

	function myRecomendationCategoryModification(gender){
		 var genderData="genderData="+gender;
		 var requiredUrl = ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/gender?automate=true"; 
		 
		 var prevSelectedCats = [];
		 $("input[name=prevSelectedCats]").each(function(){
			 prevSelectedCats.push($(this).val());
		 });
		   $.ajax({
	        	type: 'GET',
	        	contentType: "JSON",
	        	url: requiredUrl,
	            data: genderData, 	
	        	success: function(data){
	        		var htmlData = "";
	        		var index = 0;
	        		$(".gender").addClass("gender");
	        		$(".myInterestGender").css("display","none");
	        		$("fieldset.products").addClass("products active");
	        		 $.each(data , function( key, value ) {
	        			 index++;
	        			if($.inArray(key,prevSelectedCats)!= -1){
	        				htmlData=htmlData+"<input class='category-selection' data-l1='"+value.name+"' checked='checked' type='checkbox' name='categoryCode'"+"id='question-1-"
		                     +index
		                     +"' value="+key+">"
		                     +"<label for='question-1-"
		                     +index
		                     +"'>"
		                     +"<img "
		                     +"src='"+ACC.config.commonResourcePath+"/images/qRectangle_3_copy_9.jpeg'>"
		                     +"<span>"+value.name+"</span>"
		                     +"</label>";
	        				
	        			}else{
	        				htmlData=htmlData+"<input class='category-selection' data-l1='"+value.name+"' type='checkbox' name='categoryCode'"+"id='question-1-"
		                     +index
		                     +"' value="+key+">"
		                     +"<label for='question-1-"
		                     +index
		                     +"'>"
		                     +"<img "
		                     +"src='"+ACC.config.commonResourcePath+"/images/qRectangle_3_copy_9.jpeg'>"
		                     +"<span>"+value.name+"</span>"
		                     +"</label>";
	        			} 
	                 });
	        		 $(".products-questionnaire").html(htmlData);
	        	},
	        	error: function(){
	        		alert("Something is not right! Please try after sometime");
				  }
		});
}