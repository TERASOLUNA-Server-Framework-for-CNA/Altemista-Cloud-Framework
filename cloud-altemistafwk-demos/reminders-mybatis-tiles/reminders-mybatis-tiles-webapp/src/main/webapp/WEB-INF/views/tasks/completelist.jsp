<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h1 class="panel-title text-center"><spring:message code="title.tasks.completelist" /></h1>
	</div>
	
	<div class="panel-body">
		<ul class="list-group">
			<c:forEach items="${completeTasks}" var="task">
				<li class="list-group-item clearfix">
					<span class="pull-left" >${task.name}</span>
					<div class="btn-group pull-right">
						<a class="button btn btn-default" href="<c:url value="/reminders/edit/${task.id}"/>">
							<span class="glyphicon glyphicon-pencil"></span>
						</a>
						<a class="button btn btn-danger" href="<c:url value="/reminders/delete/${task.id}"/>">
							<span class="glyphicon glyphicon-fire"></span>
						</a>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
	
	<div class="panel-footer">
		<div class="btn-group pull-right">
			<a class="btn btn-primary" href="<c:url value="/reminders"/>">
				<span class="glyphicon glyphicon-chevron-left"></span>&nbsp;<spring:message code="generic.back" />
			</a>
		</div>
		<div class="clearfix"></div>
	</div>
</div>
