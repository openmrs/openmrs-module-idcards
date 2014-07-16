<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Id Card Templates" otherwise="/login.htm" redirect="/module/idcards/template.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp" %>

<style>
	th { text-align: left; }
</style>

<h2>
	<spring:message code="idcards.template.editing" />
</h2>

<spring:hasBindErrors name="template">
	<spring:message code="fix.error" />
	<br />
	<!-- ${errors} -->
</spring:hasBindErrors>
<form method="post">
	<table>
		<spring:bind path="template.name">
			<tr>
				<th><spring:message code="general.name"/></th>
				<td><input type="text" name="${status.expression}" value="${status.value}" size="43" /></td>
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</tr>
		</spring:bind>
		<spring:bind path="template.type">
			<tr>
				<th><spring:message code="general.type"/></th>
				<td><input type="text" name="${status.expression}" value="${status.value}" size="43" />
					<br/>
					<i><spring:message code="idcards.template.type.help"/></i>
				</td>
				
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</tr>
		</spring:bind>
		<spring:bind path="template.description">
			<tr>
				<th valign="top"><spring:message code="general.description"/></th>
				<td><textarea name="${status.expression}" cols="41" rows="3">${status.value}</textarea></td>
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</tr>
		</spring:bind>
		<spring:bind path="template.xml">
			<tr>
				<th valign="top"><spring:message code="idcards.template.xml"/></th>
				<td><textarea name="${status.expression}" cols="115" rows="15"><c:out value="${status.value}" escapeXml="true" /></textarea></td>
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</tr>
		</spring:bind>
		<spring:bind path="template.xslt">
			<tr>
				<th valign="top"><spring:message code="idcards.template.xslt"/></th>
				<td><textarea name="${status.expression}" cols="115" rows="15"><c:out value="${status.value}" escapeXml="true" /></textarea></td>
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</tr>
		</spring:bind>
		<c:if test="${template.creator != null}">
			<tr>
				<td><spring:message code="general.createdBy" /></td>
				<td>
					${template.creator.firstName} ${template.creator.lastName} -
					<openmrs:formatDate date="${template.dateCreated}" type="long" />
				</td>
			</tr>
		</c:if>
		<c:if test="${template.changedBy != null}">
			<tr>
				<td><spring:message code="general.changedBy" /></td>
				<td>
					${template.changedBy.firstName} ${template.changedBy.lastName} -
					<openmrs:formatDate date="${template.dateChanged}" type="long" />
				</td>
			</tr>
		</c:if>
	</table>

	<br />
	<input type="hidden" name="template.templateId" value="${template.templateId}"/>
	
	<input type="hidden" name="action" value="save" id="saveAction"/>
	
	<input type="submit" value='<spring:message code="general.save"/>' onclick="document.getElementById('saveAction').value='save'" />
	
	&nbsp; 
	
	<input type="submit" value='<spring:message code="idcards.template.saveAndPrintEmpty"/>' onclick="document.getElementById('saveAction').value='saveAndPrintEmpty'" />
	
	&nbsp; 
	
	<input type="submit" value='<spring:message code="idcards.template.saveAndReprint"/>' onclick="document.getElementById('saveAction').value='saveAndReprint'" />
	
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
