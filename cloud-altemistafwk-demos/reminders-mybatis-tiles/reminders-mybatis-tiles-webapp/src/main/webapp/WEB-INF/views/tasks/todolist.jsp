<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<h1 class="panel-title text-center"><spring:message code="title" /> <span class="badge">${todos.size()}</span></h1>
	</div>
	
	<div class="panel-body">
		<ul class="list-group">
			<c:forEach items="${todos}" var="todo">
				<li class="list-group-item text-right clearfix">
					<span class="pull-left" >${todo.name}</span>
					<div class="btn-group pull-right">
						<a class="button btn btn-default" href="<c:url value="/reminders/edit/${todo.id}"/>">
							<span class="glyphicon glyphicon-pencil"></span>
						</a>
						<a class="button btn btn-danger" href="<c:url value="/reminders/archive/${todo.id}"/>">
							<span class="glyphicon glyphicon-trash"></span>
						</a>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
	
	<div class="panel-footer">
		<div class="btn-group pull-right">
			<a class="btn btn-success" href="<c:url value="/reminders/done"/>">
				<span class="glyphicon glyphicon-trash"></span>&nbsp;<spring:message code="tasks.archived" />
			</a>
			<a class="btn btn-primary" href="<c:url value="/reminders/new"/>">
				<span class="glyphicon glyphicon-plus"></span>&nbsp;<spring:message code="tasks.create" />
			</a>
		</div>
		<div class="clearfix"></div>
	</div>
</div>
