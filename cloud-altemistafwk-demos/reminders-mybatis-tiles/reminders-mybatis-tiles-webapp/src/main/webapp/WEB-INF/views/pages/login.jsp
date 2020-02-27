<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="login-card">
	<div class="login-form">
		<c:url var="loginUrl" value="/login" />
		<form action="${loginUrl}" method="post" class="form-horizontal">
			<c:if test="${param.error != null}">
				<div class="alert alert-danger">
					<p>
						<spring:message code="e.rm.lo.0001" />
					</p>
				</div>
			</c:if>
			<div class="input-group input-sm">
				<label class="input-group-addon" for="username"><span
					class="glyphicon glyphicon-user"></span></label> <input type="text"
					class="form-control" id="username" name="ssoId"
					placeholder="<spring:message code="login.username" />" required>
			</div>
			<div class="input-group input-sm">
				<label class="input-group-addon" for="password"><span
					class="glyphicon glyphicon-asterisk"></span></label> <input type="password"
					class="form-control" id="password" name="password"
					placeholder="<spring:message code="login.password" />" required>
			</div>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />

			<div class="form-actions">
				<input type="submit" class="btn btn-block btn-primary btn-default"
					value="<spring:message code="login.login" />">
			</div>
		</form>
	</div>
</div>