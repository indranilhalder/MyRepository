<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/search/helpmeshop" var="searchForHelpMeShopUrl" />


  <div class="close" id="closeConceirge">close</div>
  <h1><spring:theme code="header.helpmeshop.howcanwehelpyou" /></h1>
	<div class="help-me-shop-form"><form name="search_form_for_help_me_shop" method="post" action="${searchForHelpMeShopUrl}">
	<div>
				<span><spring:theme code="header.helpmeshop.iama" /></span>
						<select id="age" name="age">
							<option value="-Select-"><spring:theme code="text.age"/></option>
							<%-- <c:forEach var="i" begin="${startAgeLimit}" end="${endAgeLimit}"
								step="1">
								<c:set value="${ycommerce:determineAgeBand(i)}" var="ageBand"/>
								<option value="${ageBand}">${i}</option>
							</c:forEach> --%>
							<option value="0-3m,3-6m,6-9m,9-12m">0 - 1</option>
							<option value="12-18m,18-24m,1-2y">1 - 2</option>
							<option value="24-48m,2-3y,3-4y,4-5y">2 - 5</option>
							<option value="5-6y,6-7y,7-8y,8-9y">5 - 9</option>
							<option value="9-10y,10-11y,11-12y,12-13y,13-14y">9 - 15</option>
							<option value="16to22">16 - 22</option>
							<option value="22to35">22 - 35</option>
							<option value="35to55">35 - 55</option>
							<option value="55plus">55+</option>
						</select>
				
					
				<span><spring:theme code="header.helpmeshop.yearold" /></span>
						<select id="genderOrTitle" name="genderOrTitle">
							<option value="-Select-"><spring:theme code="text.gender"/></option>
							<%-- <c:forEach items="${genderOrTitleList}" var="genderOrTitle">
								<option value="${genderOrTitle.code}">${genderOrTitle.code}</option>
							</c:forEach> --%>
							<option value="MSH11">Man</option>
							<option value="MSH10">Woman</option>
							<!-- <option value="MPH1112100">Boy</option>
							<option value="MPH1112101" >Girl</option>
							<option value="MPH1112102">Infant</option> -->
						</select>
					
					</div>
					<div>
				<span><spring:theme code="header.helpmeshop.lookingfora" /></span>
						<%-- <select id="typeOfProduct" name="typeOfProduct"
							>
							<option value="-Select-"><spring:theme code="text.product"/></option>
							<c:forEach items="${typeOfProductList}" var="typeOfProduct">
								<option value="${typeOfProduct.name}">${typeOfProduct.name}</option>
							</c:forEach>
						</select> --%>
						<input id="typeOfProduct" type="text" name="typeOfProduct" placeholder="product">
						
						<!-- <input id="typeOfProduct" type="text" value="Causal Shirt" name="typeOfProduct"> -->
					</div>
					<div>
				<span><spring:theme code="header.helpmeshop.becauseiam" /></span>
					<select id="reasonOrEvent" name="reasonOrEvent"
						>
						<option value="-Select-"><spring:theme code="text.occasion"/></option>
						<option value="partyclubwear">Going for Party</option>
						<option value="eveningwear">Going for an Outing</option>
						<option value="ethnicwear">Going for Traditional Occasion</option>
						<option value="workwear">Going for Work</option>
						<option value="casualwear">Going for a Casual day out</option>
						<option value="collegelook">Going for College</option>
						<option value="weddingwear">Going for wedding</option>
					</select>
					</div>
					<div>
				<button type="submit" class="blue" onclick=" return navigateUrl() ;">
						<spring:theme code="header.helpmeshop.helpmeshop" />
					</button>
					</div>
					<!-- <div class="errorMessage"><div id="errConceirge"></div></div> -->
					<input type="hidden" name="CSRFToken" value="${CSRFToken}">

	</form>
	</div>
	<div class="fill-in-concierge">Fill all Values</div>
	<div class="select-value-concierge">Please select a value from the suggested values</div>
	
	<script type="text/javascript">
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
				/* if(age == 0)
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
					} */
					
				$("#genderOrTitle option[value= MSH11]").show();
				$("#genderOrTitle option[value= MSH10]").show();
				
			});
			
			$('#genderOrTitle').on('change',function() {
				
				var categoryCode = $('#genderOrTitle').val();
				if(categoryCode != '-Select-'){
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
			}
				
				
			});
		
		$('div#closeConceirge').on('click touchend',function(e) {
			$("#age").val("-Select-");
			$("#genderOrTitle").val("-Select-");
			$("#reasonOrEvent").val("-Select-");
			$("#typeOfProduct").val("");
			$(e.currentTarget).closest('.banner').removeClass('active');
			
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
	});
	
	</script>