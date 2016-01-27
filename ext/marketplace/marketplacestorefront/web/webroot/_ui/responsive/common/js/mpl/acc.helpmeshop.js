var outputdata = [];
	$(function() {
		var $select = $(".18-100");
		for (i = 18; i <= 100; i++) {
			$select.append($('<option></option>').val(i).html(i))
		}
	});
	
	$(document).ready(function() {
		
			switches = $('#switches > li');
			slides = $('#navigation > div.block');
			
		switches.each(function(idx) {
			$(this).data('slide', slides.eq(idx));
		}).click(function() {
			$("#navigation").show();	
			$(this).siblings(".masterBrand").removeClass('active');
			$(this).siblings(".block").removeClass('active');	
			$(this).addClass('active');
			$(this).next().addClass("active");
			$(this).data('slide').addClass('active');
		}).mouseleave(function() {
			
			$("#navigation").hide();
		});

		$("#switches,.shopbybrand #switches").mouseleave(function(){

			$('#switches > li').removeClass('active');
			$('#switches > li').next().removeClass('active');
		});

		$('#helplink,.header-helpArrow').on('click', function(e) {
			e.preventDefault();
			$('.header-helpArrow').hide();
			$('.help').addClass('header-helpBackground');
			$('#helplink').addClass('header-helpTextColor');
			$('#helpexpand').show();
			$('#close').show();
		});
		$('#close').on('click', function(e) {
			e.preventDefault();
			$('.header-helpArrow').show();
			$('#helplink').removeClass('header-helpTextColor');
			$('.help').removeClass('header-helpBackground');
			$('#helpexpand').hide();
			$('#close').hide();
			
		});
		
		$('#age').on('change',function() {
			var age = $('#age option:selected').text();
			$("#genderOrTitle").val("-Select-");
			$("#reasonOrEvent").val("-Select-");
			$("#typeOfProduct").val("");
			if(age == 0)
				{
					$("#genderOrTitle option[value= MSH11]").hide();
					$("#genderOrTitle option[value= MSH10]").hide();
					$("#genderOrTitle option[value= MPH1112102]").show();
					$("#genderOrTitle option[value= MPH1112100]").hide();
					$("#genderOrTitle option[value= MPH1112101]").hide();
				}
			else if(age >=1 && age <= 12)
				{
					$("#genderOrTitle option[value= MSH11]").hide();
					$("#genderOrTitle option[value= MSH10]").hide();
					$("#genderOrTitle option[value= MPH1112102]").hide();
					$("#genderOrTitle option[value= MPH1112100]").show();
					$("#genderOrTitle option[value= MPH1112101]").show();
					
				}
			else if(age > 12)
				{
					$("#genderOrTitle option[value= MSH11]").show();
					$("#genderOrTitle option[value= MSH10]").show();
					$("#genderOrTitle option[value= MPH1112102]").hide();
					$("#genderOrTitle option[value= MPH1112100]").hide();
					$("#genderOrTitle option[value= MPH1112101]").hide();
				}
			else
				{
				$("#genderOrTitle option[value= MSH11]").hide();
				$("#genderOrTitle option[value= MSH10]").hide();
				$("#genderOrTitle option[value= MPH1112102]").hide();
				$("#genderOrTitle option[value= MPH1112100]").hide();
				$("#genderOrTitle option[value= MPH1112101]").hide();
				}
			
		});
		
		$('#genderOrTitle').on('change',function() {
			
			var categoryCode = $('#genderOrTitle').val();
			var dataString = 'categoryCode=' +categoryCode;
			var url = ACC.config.encodedContextPath +"/search"+"/helpmeshopforcategories";
			$.ajax({
				contentType : "application/json; charset=utf-8",
				url : url,
				data : dataString,
				dataType : "json",
				success : function(data) {
					//var outputdata = [];
					outputdata = [];
					 for (var i = 0; i < data.length; i++) {
						 if(data[i].name != null)
							 {
							 outputdata.push(data[i].name);
							 console.log(data[i].name);
							 }
				        }
					 
					/*$.each(data, function (i, item) {
					    $('#typeOfProduct').append($('<option>', { 
					        value: item.name,
					        text : item.name 
					    }));
					});*/
					/* $.each(data, function(i,item) {
					        outputdata = item.name;
					    });*/
					$('#typeOfProduct').autocomplete({
						minLength: 0,
						source: outputdata,
						open: function () {
					        $(this).data("ui-autocomplete").menu.element.addClass("helpmeshop-list");
					        var typeOfProductWidth = $('input#typeOfProduct').width() + 12;
					        $(this).data("ui-autocomplete").menu.element.css("width",typeOfProductWidth+"px");

					    }
					});
				},
				error : function(xhr, status, error) {
					alert("error")
				}
			});
			
			
		});
		
		$('#closeConceirge').on('click',function() {
			//outputdata = [];
			$("#age").val("-Select-");
			$("#genderOrTitle").val("-Select-");
			$("#reasonOrEvent").val("-Select-");
			$("#typeOfProduct").val("");
			
		});
		
		

	});

	function navigateUrl() {
		$('#errConceirge').html('').removeClass('errorMessage');
		var typeOfProduct = $('#typeOfProduct').val();
		var genderOrTitle = $('#genderOrTitle').val();
		var reasonOrEvent = $('#reasonOrEvent').val();
		var age = $('#age').val();
		if (typeOfProduct == "" || genderOrTitle == "-Select-" || reasonOrEvent == "-Select-" || age == "-Select-") {      
		    //alert("Fill all Values");
			$(".select-value-concierge").hide();
			$(".fill-in-concierge").show();
			$('#errConceirge').html('Fill all Values').addClass('errorMessage');
		    return false;
		}else{
			var isValid = false;
			for(i in outputdata)
			{
				if(outputdata[i].match(typeOfProduct))
				{
					isValid = true;
				}
			}
			if(!isValid)
				{
					//alert("Please select a value from the suggested values")
				$(".fill-in-concierge").hide();	 
				$(".select-value-concierge").show();
					$('#errConceirge').html('Please select a value from the suggested values').addClass('errorMessage');
					return false;
				}
			else
				{
					//console.log(typeOfProduct);
					var searchParameters = typeOfProduct + "," + genderOrTitle + ","
					+ reasonOrEvent + "," + age;
					$('#text').val(searchParameters);
					return true;
				}
			
		}
	}
