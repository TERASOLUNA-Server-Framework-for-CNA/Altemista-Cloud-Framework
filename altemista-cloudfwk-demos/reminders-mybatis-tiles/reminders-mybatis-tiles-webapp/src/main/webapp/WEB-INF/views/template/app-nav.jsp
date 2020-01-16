<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://terasoluna.org/tags" prefix="t"%>

   <div class="container-fluid">
     <div class="navbar-header">
       <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar2">
         <span class="sr-only">Toggle navigation</span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
       </button>
       <a style="padding:0" class="navbar-brand" href="<c:url value="/" />">
       	<img src="<c:url value="/images/default_logo.png" />" alt="Your Comapany" height="50"></img>
       </a>
     </div>
     <div id="navbar2" class="navbar-collapse collapse">
       <ul class="nav navbar-nav navbar-right">
         <li class="active"><a href="<c:url value="/" />"><spring:message code="menu.main" /></a></li>
         <li><a href="<c:url value="/logout" />"><spring:message code="menu.logout" /></a></li>
       </ul>
     </div>
   </div>

<t:messagesPanel panelClassName="alert text-center" outerElement="" innerElement="div"/>
