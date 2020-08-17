<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="Assignment4.Restaurant"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<script src="https://apis.google.com/js/platform.js" async defer></script>
	
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
		<body>
		<div id="sorted">
		<c:set var="restaurants" value="${requestScope.restaurants}"/>
		
			<c:forEach items="${restaurants}" var="r">
					
				<c:url value="Details.jsp" var="myURL">
					<c:param name="name" value="${r.getName()}" />
					<c:param name="address" value="${r.getAddress()}" />
					<c:param name="phone" value="${r.getPhone()}" />
					<c:param name="price" value="${r.getPrice()}" />
					<c:param name="url" value="${r.getURL()}" />
					<c:param name="image" value="${r.getImage()}" />
					<c:param name="rating" value="${r.getRating()}" />
					<c:param name="cuisine" value="${r.getCuisine()}" />
					
				</c:url>
				
				<div class="media">
					<div class="media-left">
						<a href="${myURL}"> <img style="width: 300px; height: 300px; margin: 10px 60px 10px 100px;" src = "${r.getImage()}" class="rounded" /> </a> <br>
					</div>
					<div class="media-body">
						<h4 style="margin-top: 40px;"> <c:out value="${r.getName()}" /> <br> </h4>
					<div style="font-size: large; line-height: 50px;">	
						<c:out value="${r.getAddress()}" /> <br>
						<a href="${r.getURL()}"> <c:out value="${r.getURL()}" /> </a>
					</div>
					</div>
				</div>
					<hr style="margin: 20px 75px;">
				
			</c:forEach>
		</div>
		
		</body>
</html>