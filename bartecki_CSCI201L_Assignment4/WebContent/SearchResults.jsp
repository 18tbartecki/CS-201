<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<script src="https://apis.google.com/js/platform.js" async defer></script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Lobster" />
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<link rel="stylesheet" type="text/css" href="style.css" />
		
		<meta name="google-signin-client_id" content="123545918809-1f8dhmmur8nbgb9kv8oj40ma1vs4l8i4.apps.googleusercontent.com">
		<meta charset="UTF-8">
		<title>Search Results</title>
		
	</head>
		<body>
		<div id="top" role="banner">
				<h1 class="display-4 my-5"> <span style=" margin-left: 75px; color: #af0606;" > <a href="Home.jsp">SalEats! </a>  </span> </h1>
				<hr size="1" width="100%" color="#e6e6e6"/>
			</div>
			
			<div style="position: absolute; top: 70px; right: 75px; ">
				<c:choose>
				<c:when test="${(loggedIn == 'false') || (loggedIn == null)}">
					<a href="Home.jsp"> Home &ensp; </a>
					<a href="SignIn.jsp"> Login / Sign Up </a>
				</c:when>
				<c:otherwise>
					<form name="Favorites List" action="FavoritesServlet" method="POST" style="display: inline;">
						 <a href="Home.jsp"> Home &ensp; </a>
						 <input style="background: none; border: none; outline: none; color: grey; " type="submit" name="submit" value="Favorites" /> 
						 <a href="SignOut.jsp" onclick="signOut()"> Logout &ensp; </a>
					</form>	
				</c:otherwise>
			</c:choose>
			</div>
			
		<div class=" mx-auto " style="width: 90%;" >
		 	<form name="restaurantInfo" action="RestaurantSearchServlet" method="POST">
				<div class="row my-4">
					<div class="col-6">
					 		<input class="form-control" type="text" id="restaurantname" name="name" placeholder="Restaurant Name" required/> 
					 </div>
					 <div class="col-1">
					 		<button style="background-color: #af0606; color: white;" class="form-control" type="submit" name="submit" ><i class=" fa fa-search"></i></button>
				 	</div>
				 	<div class="col-2">
				 		<div class="form-check ">
					 		 <input class="form-check-input" type="radio" name="searchtype" value="best_match" id="match" required/> 	  
							 <label class="form-check-label" for="match">  Best Match </label>
						</div>
						<div class="form-check ">
							 <input class="form-check-input" type="radio" name="searchtype" value="rating" id="rating" required/> 
							 <label class="form-check-label" for="rating">  Rating </label>
						</div>
					</div>
					<div class="col-3">
						<div class="form-check ">	 
						 	 <input class="form-check-input" type="radio" name="searchtype" value="review_count" id="count" required/> 
							 <label class="form-check-label" for="count">  Review Count </label>
						</div>
						<div class="form-check ">	 
							 <input class="form-check-input" type="radio" name="searchtype" value="distance" id="distance" required/> 
				 			 <label class="form-check-label" for="distance">  Distance </label>
				 		</div>
				 	</div>
				 	
				 </div>
				 <div class="row mb-4">
					<div class="col-4">
							<input class="form-control" type="number" id="latitude" name="latitude" placeholder="Latitude" step=".0000000000000001" min="-90" max="90" required/> 
					</div>
					<div class="col-4">
							<input class="form-control" type="number" id="longitude" name="longitude" placeholder="Longitude" step=".0000000000000001" min="-180" max="180" required/> 
					</div>
					<div class="col-4">
							<button class="btn btn-primary form-control" onclick="return on()"><i class=" fa fa-map-marker"></i> Google Maps (Drop a pin!) </button>
					</div>
				</div>
		 	</form>
		 </div>
	
		<div id="overlay" onclick="off()" > 
			<div id="map"> </div>
		</div>
				
			<h2 style="margin-left: 75px;"> Results for "<%= request.getParameter("name") %>" </h2>
			
			<hr style="margin: 20px 75px;">
	
			
			<c:set var="restaurants" value="${requestScope.restaurants}"/>
			
			<c:forEach items="${restaurants}" var="r">
					
			
				<c:url value="Details.jsp" var="myURL">
					<c:param name="name" value="${r.getName()}" />
					<c:param name="address" value="${r.location.getAddress()}" />
					<c:param name="phone" value="${r.getPhone()}" />
					<c:param name="price" value="${r.getPrice()}" />
					<c:param name="url" value="${r.getURL()}" />
					<c:param name="image" value="${r.getImage()}" />
					<c:param name="rating" value="${r.getRating()}" />
					<c:param name="cuisine" value="${r.getCuisine()}" />
					
				</c:url>
				<div class="media">
					<div class="media-left">
						<a href="${myURL}"> <img style="object-fit:cover; border-radius:25px; width: 300px; height: 300px; margin: 10px 60px 10px 100px;" src = "${r.getImage()}" /> </a> </br>
					</div>
					<div class="media-body">
						<h4 style="margin-top: 40px;"> <c:out value="${r.getName()}" /> <br> </h4>
					<div style="font-size: large; line-height: 50px;">	
						<c:out value="${r.location.getAddress()}" /> <br>
						<a href="${r.getURL()}"> <c:out value="${r.getURL()}" /> </a> <br> 
						</div>
					</div>
				</div>
				<hr style="margin: 20px 75px;">
				
			</c:forEach>
			
			<script>
			
			function initMap() {
				var uluru = {lat: 34.02116, lng: -118.287132};
				var map = new google.maps.Map(document.getElementById('map'), {zoom: 4, center: uluru});
				var marker = new google.maps.Marker({position: uluru, draggable: true, map: map});
			
			var lat = marker.getPosition().lat();
			var lng = marker.getPosition().lng();
			google.maps.event.addListener(map, 'click', function(event){
				
				document.getElementById("latitude").value = event.latLng.lat();
				document.getElementById("longitude").value = event.latLng.lng();
			});
			
			}
			 
			function on() {
			  document.getElementById("overlay").style.display = "block";
			  return false;
			}
	
			function off() {
			  document.getElementById("overlay").style.display = "none";
			}
			
			
			var CLIENT_ID = '123545918809-eg6a2l5cjgjg441bsr7quun6ph5qmpm7.apps.googleusercontent.com';
		      var API_KEY = 'AIzaSyB1Enu8-rGhshv4JTecd1DQ9i_6kuqw8BI';
		      var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];

		      // Authorization scopes required by the API; multiple scopes can be
		      // included, separated by spaces.
		      var SCOPES = "https://www.googleapis.com/auth/calendar";
		    
		      function handleClientLoad() {
		        gapi.load('client:auth2', initClient);
		      }

		      function initClient() {
		          gapi.client.init({
		          apiKey: API_KEY,
		          clientId: CLIENT_ID,
		          discoveryDocs: DISCOVERY_DOCS,
		          scope: SCOPES
		        }).then(function () {
		          // Listen for sign-in state changes.
		          gapi.auth2.getAuthInstance().isSignedIn.listen(updateSigninStatus);
		         
		        }, function(error) {
		          appendPre(JSON.stringify(error, null, 2));
		        });
		      }
		      
		      function signOut() {
			        var auth2 = gapi.auth2.getAuthInstance();
			        auth2.signOut().then(function () {
			          console.log('User signed out.');
			        });
			      }
		</script>
					
			<script src="https://apis.google.com/js/platform.js" 
				onload="this.onload=function(){};handleClientLoad()"
      			onreadystatechange="if (this.readyState === 'complete') this.onload()" async defer></script>
			<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB1Enu8-rGhshv4JTecd1DQ9i_6kuqw8BI&callback=initMap"> </script>
		</body>
</html>