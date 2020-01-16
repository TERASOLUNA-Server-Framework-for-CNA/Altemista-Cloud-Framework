<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="panel panel-default">

	<div class="panel-heading">
		<h3 class="panel-title">Data confirmed</h3>
	</div>
	
	<div class="panel-body">
		<h3>${resultValue}</h3>
		<a href='<c:url value="/example-flow" />'>Start again</a>
	</div>
	
</div>
