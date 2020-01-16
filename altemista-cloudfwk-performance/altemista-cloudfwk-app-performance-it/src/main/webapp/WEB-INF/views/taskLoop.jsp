<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:forEach items="${tasks}" var="task">
	<li class="list-group-item">
		<c:set var="task" value="${task}" scope="request"></c:set>
		<jsp:useBean id="startTimeDate" class="java.util.Date"/>
		<jsp:setProperty name="startTimeDate" property="time" value="${task.startTimeMillis}"/>
		
		<c:if test="${!task.nestedTasks.isEmpty()}">
			<button class="btn btn-primary btn-xs" data-toggle="collapse" data-target="#nested${task.startTimeMillis}">
				<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
			</button>
		</c:if>
		
		<span class="label label-danger">${task.totalTimeMillis} ms</span>
		
		<span class="label label-default"><fmt:formatDate value="${startTimeDate}" pattern="HH:mm:ss.SSSS dd/MM/yyyy" /></span>
		
		<p><c:out value="${task.taskInfo.description}" /></p>
		
		<c:if test="${!task.nestedTasks.isEmpty()}">
			<div id="nested${task.startTimeMillis}" class="collapse">
				<ul class="list-group">
					<c:set var="tasks" value="${task.nestedTasks}" scope="request"></c:set>
					<jsp:include page="taskLoop.jsp" />
				</ul>
			</div>
		</c:if>
	</li>
</c:forEach>
