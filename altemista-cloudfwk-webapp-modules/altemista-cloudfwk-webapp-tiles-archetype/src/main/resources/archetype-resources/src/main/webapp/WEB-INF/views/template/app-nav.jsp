#set($dollar = '$')
<%@page import="org.springframework.util.ClassUtils"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://terasoluna.org/tags" prefix="t"%>

<% boolean hasLogout = ClassUtils.isPresent("org.springframework.security.web.authentication.logout.LogoutFilter", this.getClass().getClassLoader()); %>

<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar2">
				<span class="sr-only"><spring:message code="navbar.toggle" /></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a style="padding: 0" class="navbar-brand" href="<c:url value="/" />">
				<img src="<c:url value="/images/${appShortId}-logo.png" />" alt="<spring:message code="navbar.brand.alttext" />" height="50"></img>
			</a>
		</div>
		<div id="navbar2" class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right">
				<li class="active">
					<a href="<c:url value="/" />"><spring:message code="navbar.label.main" /></a>
				</li>
				<% if (hasLogout) { %>
					<li>
						<a href="#" onclick="${dollar}('#logoutForm').submit()"><spring:message code="navbar.label.logout" /></a>
						<form id="logoutForm" class="hidden" action="<c:url value="/logout" />" method="post">
							<input type="hidden" name="${dollar}{_csrf.parameterName}" value="${dollar}{_csrf.token}" />
						</form>
					</li>
				<% } %>
			</ul>
		</div>
	</div>
</nav>

<t:messagesPanel panelClassName="alert text-center" outerElement="" innerElement="div"/>
