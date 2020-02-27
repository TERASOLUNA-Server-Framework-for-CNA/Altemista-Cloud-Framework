<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="ISO-8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<%-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags --%>
		<title><tiles:getAsString name="title" /></title>
		<%-- Bootstrap core CSS --%>
		<link rel="stylesheet" href="<c:url value="/bootstrap/3.3.6/css/bootstrap.min.css" />">
		<%-- Allows additional elements in head --%>
		<tiles:insertAttribute name="head" />
	</head>
	<body>
		<div class="container">
			<header><tiles:insertAttribute name="header" /></header>
			<nav><tiles:insertAttribute name="nav" /></nav>
			<section><tiles:insertAttribute name="section" /></section>
			<article><tiles:insertAttribute name="article" /></article>
			<aside><tiles:insertAttribute name="aside" /></aside>
			<footer><tiles:insertAttribute name="footer" /></footer>
		</div>
		<%-- Bootstrap core JavaScript
			Placed at the end of the document so the pages load faster --%>
		<script src="<c:url value="/jquery/jquery-2.2.3.min.js" />"></script>
		<script src="<c:url value="/bootstrap/3.3.6/js/bootstrap.min.js" />"></script>
	</body>
</html>
