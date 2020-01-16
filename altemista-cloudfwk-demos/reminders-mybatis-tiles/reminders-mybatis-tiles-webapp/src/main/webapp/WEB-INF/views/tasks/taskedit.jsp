<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form action="${pageContext.request.contextPath}/reminders/save" method="POST" modelAttribute="task">
	<div class="panel panel-default">
		<div class="panel-heading">
			<c:if test="${task.id == -1}">
				<h1 class="panel-title"><spring:message code="tasks.create" /></h1>
			</c:if>
			<c:if test="${task.id != -1}">
				<h1 class="panel-title text-center"><spring:message code="tasks.edit" /></h1>
			</c:if>
		</div>
		
		<div class="panel-body">
			<div class="form-horizontal">
				<div class="form-group">
					<form:label path="name" class="control-label col-sm-2"><spring:message code="task.detail.description" />:</form:label>
					<div class="col-sm-10">
						<form:input path="name" type="text" required="true" maxlength="1024" class="form-control" placeholder="Enter the task description"/>
					</div>
				</div>
			
				<div class="form-group">
					<form:label path="date" class="control-label col-sm-2"><spring:message code="task.detail.duedate" />:</form:label>
					<div class="col-sm-2">
						<form:input path="date" required="true" type="text" maxlength="10" class="form-control date" placeholder="dd/mm/yyyy"/>
					</div>
				</div>
			</div>
		</div>

		<div class="panel-footer">
			<c:if test="${task.id != -1}">
				<div class="btn-group">
					<a class="btn btn-danger"
						href="<c:url value="/reminders/delete/${task.id}"/>">
						<span class="glyphicon glyphicon-trash"></span>&nbsp;<spring:message code="generic.delete" />
					</a>
				</div>
			</c:if>
			<div class="btn-group pull-right">
				<a class="btn btn-default" href="<c:url value="/reminders"/>">
					<span class="glyphicon glyphicon-remove"></span>&nbsp;<spring:message code="generic.back" />
				</a>
				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-ok"></span>&nbsp;<spring:message code="generic.submit" />
				</button>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>
</form:form>
