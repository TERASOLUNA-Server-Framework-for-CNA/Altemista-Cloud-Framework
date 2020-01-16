<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<meta charset="ISO-8859-1">
<head><title>${rootArtifactId}</title></head>
<body>

	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title"><spring:message code="webflow.example.title" arguments="1" /></h3>
		</div>
		
		<div class="panel-body">
			<form action="${requestScope.flowExecutionUrl}" method="post">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
				
				<label for="name"><spring:message code="webflow.example.label.name" />:</label><input type="text" name="name" value="${exampleModel.name}"/>
				
				<input type="submit" name="_eventId_submit" value="<spring:message code="webflow.example.button.submit" />" class="btn btn-primary" />
			</form>
		</div>
	</div>
	
</body>
</html>
