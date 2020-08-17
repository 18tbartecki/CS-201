<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="Assignment4.Restaurant"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Lobster" />
		<script src="https://apis.google.com/js/platform.js" async defer></script>
		<script src='https://kit.fontawesome.com/a076d05399.js'></script>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<link rel="stylesheet" type="text/css" href="style.css" />
		
	</head>
		<body onload="addScore($('#rating'))">
		
		<div id="top">
			<h1 class="display-4 my-5"> <span style=" margin-left: 75px; color: #af0606;" > <a href="Home.jsp">SalEats! </a>  </span> </h1>
			<hr size="1" width="100%" color="#e6e6e6" />
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
  		
  		<input type="hidden" id="favLog" value="${sessionScope.loggedIn}">
				 	
  		
  		<div class=" mx-auto " style="width: 92%;" >
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
							 <input class="form-check-input" type="radio" name="searchtype" value="rating" id="ratingsearch" required/> 
							 <label class="form-check-label" for="ratingsearch">  Rating </label>
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
				
			
		
		<c:set var="name" value="${param.name}" scope="request"/>
		<c:set var="address" value="${param.address}"/>
		<c:set var="phone" value="${param.phone}"/>
		<c:set var="url" value="${param.url}"/>
		<c:set var="image" value="${param.image}"/>
		<c:set var="price" value="${param.price}"/>
		<c:set var="rating" value="${param.rating}"/>
		<c:set var="cuisine" value="${param.cuisine}"/>
		
		<% String restaurant = request.getParameter("name"); 
			String button = (String) session.getAttribute(restaurant);
			
			if(button == null)
			{
				button = "Remove from Favorites";
			}
			
		%>
		
		<c:set var="button" value="<%= button %>"/>
		
		
		<c:set var="username" value="${sessionScope.username}"/>
		
		
		<h2 style="margin-left: 65px;"> <c:out value="${name}" /> </h2>
		
		<hr style="margin: 20px 75px;">
		 
		 <div class="media">
			<div class="media-left">
				<a href="${url}"> <img style="object-fit:cover; border-radius:25px; width: 300px; height: 300px; margin: 10px 60px 20px 100px;" src = "${image}" /> </a> 
			</div>
			<div class="media-body" style="font-size: large; line-height: 50px; margin-top: 5px;">
				Address: <c:out value="${address}" /> <br>
				Phone No. <c:out value="${phone}" /> <br>
				Cuisine: <c:out value="${cuisine}" /> <br>
				Price: <c:out value="${price}" /> <br>
				 <div id="rating" >Rating:  </div>
			</div>
		</div>
			
		
		
		<iframe name="dummyframe" id="dummyframe" style="display: none;"></iframe>
			<div>
				<form name="Add to Favorites" action="FavoritesServlet" method="POST" target="dummyframe">
		 
					<input type="hidden" name="name" value="${name}"> 
					<input type="hidden" name="address" value="${address}">
					<input type="hidden" name="phone" value="${phone}">
					<input type="hidden" name="url" value="${url}"> 
					<input type="hidden" name="image" value="${image}">
					<input type="hidden" name="price" value="${price}">
					<input type="hidden" name="rating" value="${rating}">
					<input type="hidden" name="cuisine" value="${cuisine}">
					<input type="hidden" id="logged" value="${sessionScope.googleSignIn}">
				 				
				 <button style="width:92%; background-color: #ffcc00; color: grey; font-family: Helvetica;" class="btn btn-block btn-large mx-auto my-2" id="favoriteButton" type="submit" name="submit" value="${button}" onclick="changeFavoriteButton()" ><i class="fa fa-star"></i><c:out value="${button}" /></button> 
					
				</form>
			</div>
			
			
			<button style="width:92%; background-color: #af0606; color: white; font-family: Helvetica;" class="btn btn-block btn-large mx-auto my-2" type="submit" name="submit" id="reservationButton" ><i class="fa fa-calendar-plus"></i> Add Reservation </button>
			
		<div class="mx-auto" style="width: 92%">
			<form id="reservationForm" name="reservation" onSubmit="addToCalendar()" target="dummyframe">
				<div class="row my-3">
					<div class="col">
						<input class="form-control" type="text" name="date" id="date" placeholder="Date" onfocus="(this.type='date')" required/>
					</div>
					<div class="col">
						 <input class="form-control" type="text" name="time" id="time" placeholder="Time" onfocus="(this.type='time')" required/>
					 </div>
				</div>
				<div class="row my-3">
					<div class="col">
						<textarea style="height: 100px;" class="form-control" id="extraNotes" name="comment" form="reservationForm" placeholder="Reservation Notes"></textarea>
					</div>
				</div>
				<div class="row my-3" >
					<div class="col">
					 	<button style="background-color: #af0606; color: white; font-family: Helvetica;" class="btn btn-block btn-large mx-auto" type="submit" name="submit" id="submitReservation"><i class="fa fa-calendar-plus"></i> Submit Reservation</button>
					</div>
				</div>	
			</form>
		</div>
						
			
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
			
			$(document).ready(function(){
				$('#reservationButton').click(function(){
					var logged = document.getElementById("logged").value;
					
					if(logged == "true") {
						$('#reservationForm').toggle(500);
					}
					else  {
						alert("Google Account not logged in.");
					}
					});
			});
			
			$(document).ready(function(){
				$('#submitReservation').click(function(){
					var date = document.getElementById("date").value;
					var time = document.getElementById("time").value;
					
					if(date == "" || time == "") {
						alert("Please fill in date and time.");
					}
					else  {
						$('#reservationForm').toggle(500);
					}
					});
			});
			
			function changeFavoriteButton() {
				var button = document.getElementById("favoriteButton");
				var loggedIn = document.getElementById("favLog").value;
				
				if(loggedIn == "true")
				{
					if(button.value == "Add to Favorites")
					{
						button.value = "Remove from Favorites";
						button.innerHTML = "<i class=\"fa fa-star\"></i> Remove from Favorites";
					}
					else
					{
						button.value = "Add to Favorites";
						button.innerHTML = "<i class=\"fa fa-star\"></i> Add to Favorites";
					}
				}
				else
				{
					alert("Account not logged in.");
				}
				
			}
			
			function addScore($domElement) {
				var score = getUrlVars()["rating"];
				
				score *= 20;
					
				$("<span class='stars-container'>")
				   .addClass("stars-" + score.toString())
				   .text("★★★★★")
				   .appendTo($domElement);
				}
			
			function getUrlVars() {
			    var vars = {};
			    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
			        vars[key] = value;
			    });
			    return vars;
			}
			
			function addToCalendar() {
				var date = document.getElementById("date").value;
				
				var time = document.getElementById("time").value;
				//var endTime = startTime.getHours() + 1;
				//document.write(endTime);
				var notes = document.getElementById("extraNotes").value;
				var summary = "${name}" + " Reservation";
				var address = "${address}";
				
				var event = {
					'summary': summary,
					'location': address,
					'description': notes,
					'start': {
						 'dateTime': date+"T"+time+":00-07:00",
						 'timeZone': 'America/Los_Angeles'
					},
					'end': {
						'dateTime': date+"T"+time+":00-07:00",
						 'timeZone': 'America/Los_Angeles'
					}
					
				 };
				
				var request = gapi.client.calendar.events.insert({
					 'calendarId': 'primary',
					 'resource': event
					});
				
				request.execute(function(event) {
					
					  appendPre('Event created: ' + event.htmlLink);
					});		  		
			}	
			
			function signOut() {
		        var auth2 = gapi.auth2.getAuthInstance();
		        auth2.signOut().then(function () {
		          console.log('User signed out.');
		        });
		      }
		
      // Client ID and API key from the Developer Console
      var CLIENT_ID = '123545918809-eg6a2l5cjgjg441bsr7quun6ph5qmpm7.apps.googleusercontent.com';
      var API_KEY = 'AIzaSyB1Enu8-rGhshv4JTecd1DQ9i_6kuqw8BI';

      // Array of API discovery doc URLs for APIs used by the quickstart
      var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];

      
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

          // Handle the initial sign-in state.
          updateSigninStatus(gapi.auth2.getAuthInstance().isSignedIn.get());
         
        }, function(error) {
          appendPre(JSON.stringify(error, null, 2));
        });
      }

      function handleAuthClick(event) {
        gapi.auth2.getAuthInstance().signIn();
      }

      
      function handleSignoutClick(event) {
        gapi.auth2.getAuthInstance().signOut();
      }

    </script>
    

    <script async defer src="https://apis.google.com/js/api.js"
      onload="this.onload=function(){};handleClientLoad()"
      onreadystatechange="if (this.readyState === 'complete') this.onload()">
    </script>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB1Enu8-rGhshv4JTecd1DQ9i_6kuqw8BI&callback=initMap"> </script>
	
		</body>
</html>