<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message code="admin.title.short"/></a>
	</li>
	
	<openmrs:globalProperty key="idcards.enableIdentifierGeneration" var="enableIdentifierGeneration"/>
	<c:if test="${enableIdentifierGeneration == 'true'}">
		<openmrs:hasPrivilege privilege="Pre-Generate Random Patient Identifiers">
			<li <c:if test='<%= request.getRequestURI().contains("idcards/generateIdentifiers") %>'>class="active"</c:if>>
				<a href="${pageContext.request.contextPath}/module/idcards/generateIdentifiers.form">
					<spring:message code="idcards.generateIdentifiers.title"/>
				</a>
			</li>
		</openmrs:hasPrivilege>
	</c:if>
	<openmrs:hasPrivilege privilege="Print Id Cards">
		<li <c:if test='<%= request.getRequestURI().contains("idcards/printEmptyIdcards") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/idcards/printEmptyIdcards.form">
				<spring:message code="idcards.printEmpty.title"/>
			</a>
		</li>
	</openmrs:hasPrivilege>

    <openmrs:hasPrivilege privilege="Reprint Id Cards">
        <li <c:if test='<%= request.getRequestURI().contains("idcards/reprintIdcards") %>'>class="active"</c:if>>
            <a href="${pageContext.request.contextPath}/module/idcards/reprintIdcards.form">
                <spring:message code="idcards.print.title"/>
            </a>
        </li>
    </openmrs:hasPrivilege>

    <openmrs:hasPrivilege privilege="Manage Id Card Templates">
        <li <c:if test='<%= request.getRequestURI().contains("idcards/template") %>'>class="active"</c:if>>
            <a href="${pageContext.request.contextPath}/module/idcards/templates.list">
                <spring:message code="idcards.templates.title"/>
            </a>
        </li>
    </openmrs:hasPrivilege>

	<openmrs:extensionPoint pointId="org.openmrs.admin.idcards.localHeader" type="html">
			<c:forEach items="${extension.links}" var="link">
				<li <c:if test="${fn:endsWith(pageContext.request.requestURI, link.key)}">class="active"</c:if> >
					<a href="${pageContext.request.contextPath}/${link.key}"><spring:message code="${link.value}"/></a>
				</li>
			</c:forEach>
	</openmrs:extensionPoint>
	
</ul>