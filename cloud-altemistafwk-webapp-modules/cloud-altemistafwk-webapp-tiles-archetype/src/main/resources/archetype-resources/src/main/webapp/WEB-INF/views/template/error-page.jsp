<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="ISO-8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<%-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags --%>
		<title><tiles:getAsString name="title" /></title>
	</head>
	<body>
		<header><tiles:insertAttribute name="header" /></header>
		<article><tiles:insertAttribute name="article" /></article>
		<footer><tiles:insertAttribute name="footer" /></footer>
	</body>
</html>
