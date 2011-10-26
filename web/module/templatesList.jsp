<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Id Card Templates" otherwise="/login.htm" redirect="/module/idcards/templates.list" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="idcards.title" /></h2>

<a href="template.form"><spring:message code="idcards.template.add" /></a>
<br />
<br />

<div id="templatelist">
	<b class="boxHeader">
		<spring:message code="idcards.template.choose" />
	</b>
	<div class="box">
		<table width="90%">
			<tr>
				<th><spring:message code="general.name" /></th>
				<th><spring:message code="general.type" /></th>
				<th><spring:message code="general.description" /></th>
				<th><spring:message code="general.createdBy" /></th>
				<th><spring:message code="general.changedBy" /></th>
				
			</tr>
		<c:forEach var="template" items="${templates}" varStatus="varStatus">
			<tr>
				<td><a href="template.form?templateId=${template.templateId}">${template.name}</a></td>
				<td>${template.type}</td>
				<td>${template.description}</td>
				<td>${template.creator.firstName} ${template.creator.lastName} - <openmrs:formatDate date="${template.dateCreated}" type="medium" /></td>
				<td>${template.changedBy.firstName} ${template.changedBy.lastName} - <openmrs:formatDate date="${template.dateChanged}" type="medium" /></td>
			</tr>
		</c:forEach>
		</table>
	</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
