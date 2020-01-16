<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<div class="panel panel-default">

	<div class="panel-heading">
		<h3 class="panel-title">Input data</h3>
	</div>
	
	<div class="panel-body">
		<form action="${requestScope.flowExecutionUrl}" method="post">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
			
			<label for="name">Name:</label>
			<input type="text" name="name" value="${exampleVariable.name}"/>
			
			<input type="submit" name="_eventId_cancel" value="Cancel" class="btn btn-default"/>
			<input type="submit" name="_eventId_submit" value="Submit" class="btn btn-primary" />
		</form>
	</div>
	
</div>
