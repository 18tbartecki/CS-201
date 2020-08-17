<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	
	<head>
		<meta charset="UTF-8">
		<meta name="google-signin-client_id" content="123545918809-eg6a2l5cjgjg441bsr7quun6ph5qmpm7.apps.googleusercontent.com">
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Lobster" />
		<link rel="stylesheet" type="text/css" href="style.css" />
		<title>Sign In</title>
		
	</head>
		<body>
		
		<c:set var="logusername" value="${requestScope.logusername}"/>
		<c:set var="logerror" value="${requestScope.logerror}"/>
		
		<c:set var="username" value="${requestScope.username}"/>
		<c:set var="error" value="${requestScope.error}"/>
		<c:set var="email" value="${requestScope.email}"/>
			
		<c:set var="loggedIn" value="${sessionScope.loggedIn}"/>
		
		<div id="top">
				<h1 class="display-4 my-5"> <span style=" margin-left: 75px; color: #af0606;" > <a href="Home.jsp">SalEats! </a>  </span> </h1>
				<hr size="1" width="100%" color="#e6e6e6" />
		</div>
		<div style="position: absolute; top: 70px; right: 100px; ">
    		<c:choose>
				<c:when test="${(loggedIn == 'false') || (loggedIn == null)}">
					<a href="Home.jsp"> Home &ensp; </a>
					<a href="SignIn.jsp"> Login / Sign Up &ensp; </a>
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
		
		
	    <div class="mx-auto" style="width: 90%;">
	    <div style="margin-top: 130px;" class="row">
			<div class="col">
				<p class="h2 mb-4"> Login </p>
				<form name="Login" action="LoginServlet" method="POST">
					<font color="red"> <c:out value="${logerror}" /> </font>
					<div class="form-group">	
							<label class="mb-2" for="username"> Username </label>
							<input class="form-control mb-4" type="text" id="username" name="username" value="${logusername}" required/> 		
					</div>
					<div class="form-group">	
							<label class="mb-2" for="password"> Password </label>
							<input class="form-control mb-4" type="password" name="password" id="password" required/>
					</div>
					
					<button style="background-color: #af0606; color: white; font-family: Helvetica;" class="btn btn-large btn-block my-4" type="submit" name="submit" ><i class=" fa fa-sign-in"> </i> Sign In</button>
				 	
				 	<hr class ="mx-5" >
				 	
					<div id="my-signin2" class="btn-block" ></div>
				</form>
			</div>
			<div class="col">
				<p class="h2 mb-4"> Sign Up </p>
				<form name="SignIn" action="SignupServlet" method="POST" onsubmit="return checkSignIn(this)">
					<div id="error"><font color="red"> <c:out value="${error}" /> </font></div>
					<div class="form-group">
						<label class="mb-2" for="email"> Email </label>
						<input class="form-control mb-4" type="email" id="email" name="email" value="${email}" required/> 
					</div>
					<div class="form-group">
						<label class="mb-2" for="username">Username  </label>
						<input class="form-control mb-4" type="text" id="username" name="username" value="${username}" required/> 
					</div>
					<div class="form-group">
						<label class="mb-2" for="password">Password </label>
						<input class="form-control mb-4" type="password" name="password" id="password" required/>
					</div>
					<div class="form-group">
						<label class="mb-2" for="confirmpassword">Confirm Password </label>
						<input class="form-control mb-4" type="password" name="confirmpassword" id="confirmpassword" required/>
					</div>
					<div class="form-check">
						<input type="checkbox" name="checkbox" value="check" id="terms" class="form-check-input"/> 
						<label class="form-check-label" for="terms">  I have read and agree to all terms and conditions of SalEats.</label>
					</div>
						 <button style="background-color: #af0606; color: white; font-family: Helvetica;" class="btn  btn-large btn-block my-4" type="submit" name="submit" ><i class=" fa fa-user-plus"></i> Create Account</button>
				 	
				</form>
					
			</div>
		</div>
		</div>
		
		
		<script>
			function checkSignIn(form)
			{
				if(!form.checkbox.checked)
				{
					document.getElementById("error").innerHTML = "<font color=\"red\"> Please indicate you have read and agree to the terms and conditions. </font>";  
					return false;
				}
				else if(form.password.value != form.confirmpassword.value) 
				{
					document.getElementById("error").innerHTML = "<font color=\"red\"> Passwords do not match </font>";  
					return false;
				}
				else
				{
					return true;
				}	
			}
			
			function onSignIn(googleUser) {
		        // Useful data for your client-side scripts:
		        var profile = googleUser.getBasicProfile();
		        console.log("ID: " + profile.getId()); // Don't send this directly to your server!
		        console.log('Full Name: ' + profile.getName());
		        console.log('Given Name: ' + profile.getGivenName());
		        console.log('Family Name: ' + profile.getFamilyName());
		        console.log("Image URL: " + profile.getImageUrl());
		        console.log("Email: " + profile.getEmail());
		        var name = profile.getName();
		        var email = profile.getEmail();
		        // The ID token you need to pass to your backend:
		        var id_token = googleUser.getAuthResponse().id_token;
		  		addGoogleUser(name, email, id_token);
		        
		      }
			
				function onSuccess(googleUser) {
			      console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
			      onSignIn(googleUser);
			    }
				
			    function onFailure(error) {
			      console.log(error);
			    }
			    
			    function renderButton() {
			      gapi.signin2.render('my-signin2', {
			        'scope': 'profile email',
			        'width': 'btn-block',
			        'height': 42,
			        'longtitle': true,
			        'theme': 'dark',
			        'onsuccess': onSuccess,
			        'onfailure': onFailure
			      });
			    }
			    
			    function addGoogleUser(name, email, id) {
					$.ajax({
						url: "SignupServlet",
						data: {
							username: name,
							email: email,
							password: id
						},
						success: function(result) {
							window.location.href = "http://localhost:8080/bartecki_CSCI201L_Assignment4/Home.jsp";		        
						}
					});	
				}
			    
			    function signOut() {
			        gapi.auth2.getAuthInstance().signOut();
			      }
			    
			    
			      var CLIENT_ID = '123545918809-eg6a2l5cjgjg441bsr7quun6ph5qmpm7.apps.googleusercontent.com';
			      var API_KEY = 'AIzaSyB1Enu8-rGhshv4JTecd1DQ9i_6kuqw8BI';
			      
			      var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];

			      var SCOPES = "https://www.googleapis.com/auth/calendar";
			   
			      var authorizeButton = document.getElementById('my-signin2');

			      
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
			         
			          authorizeButton.onclick = handleAuthClick;
			        }, function(error) {
			          appendPre(JSON.stringify(error, null, 2));
			        });
			      }
			      
			      
			      function handleAuthClick(event) {
			        gapi.auth2.getAuthInstance().signIn();
			      }

			    
		</script>
		
		
		<script src="https://apis.google.com/js/platform.js?onload=renderButton" 
				onload="this.onload=function(){};handleClientLoad()"
      			onreadystatechange="if (this.readyState === 'complete') this.onload()" async defer></script>
		
		</body>
</html>