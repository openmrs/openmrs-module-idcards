<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Reprint Id Cards" otherwise="/login.htm" redirect="/module/idcards/reprintIdcards.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="idcards.print.title"/></h2>

<span><spring:message code="idcards.print.details"/></span>
<br/><br/>
<b class="boxHeader"><spring:message code="idcards.print.instructions"/></b>
<form method="post" class="box" action="${pageContext.request.contextPath}/moduleServlet/idcards/print">
    <span>&nbsp;&nbsp;*&nbsp;<spring:message code="idcards.print.notice"/></span>
    <br/>
    <br/>
	<table>
		<tr>
			<td valign="top"><spring:message code="idcards.print.patientId"/></td>
			<td valign="top">
				<input type="text" name="patientId"/>
				<i><spring:message code="idcards.print.patientId.help"/></i>
			</td>
		</tr>

		<tr>
			<td valign="top"><spring:message code="idcards.print.patientIds"/></td>
			<td valign="top">
				<textarea name="patientIds">${patientIds}</textarea>
				<i><spring:message code="idcards.print.patientIds.help"/></i>
			</td>
		</tr>

		<tr>
			<td valign="top"><spring:message code="idcards.print.patientIdentifiers"/></td>
			<td valign="top">
				<textarea name="patientIdentifiers"></textarea>
				<i><spring:message code="idcards.print.patientIdentifiers.help"/></i>
			</td>
		</tr>
		<tr>
			<td><spring:message code="Cohort.title"/></td>
			<td>
				<select name="cohortId">
					<option value=""></option>
					<openmrs:forEachRecord name="cohort">
						<option value="${record.cohortId}">${record.name}</option>
					</openmrs:forEachRecord>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center"><spring:message code="general.andOr"/></td>
		</tr>
		<tr>
			<td><spring:message code="CohortDefinition.title"/></td>
			<td>
				<select name="cohortDefinitionId">
					<option value=""></option>
					<openmrs:forEachRecord name="reportObject" reportObjectType="Patient Search">
						<option value="${record.reportObjectId}">${record.name}</option>
					</openmrs:forEachRecord>
				</select>
			</td>
		</tr>
        <tr>
            <td>
                <spring:message code="idcards.cardTemplate"/>
            </td>
            <td>
                <select name="cardTemplateId">
                    <c:forEach items="${cardTemplates}" var="template">
                        <c:if test="${template.type == 'Reprint' || template.type == ''}">
                            <option value="${template.templateId}">${template.name}</option>
                        </c:if>
                    </c:forEach>
                </select>
            </td>
        </tr>
		<tr><td colspan="2"><br/></td></tr>
	</table>

	<input type="submit" value="<spring:message code="idcards.print.submit"/>"/>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
