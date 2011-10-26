<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Pre-Generate Random Patient Identifiers" otherwise="/login.htm" redirect="/module/idcards/generateIdentifiers.form"/>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<script type="text/javascript">
	function toggle(link, id) {
		var trace = document.getElementById(id);
		if (link.innerHTML == '<spring:message code="idcards.log.view"/>') {
			link.innerHTML = '<spring:message code="idcards.log.hide"/>';
			trace.style.display = "block";
		}
		else {
			link.innerHTML = '<spring:message code="idcards.log.view"/>';
			trace.style.display = "none";
		}
		return false;
	}
	
    var oNewOption = null;
    /**
     * This function thanks to
     * http://www.ridgway.co.za/archive/2005/10/15/aneditablehtmlselectlist.aspx
     * @param oInput
     */
    function addMrnListItem(oInput) {
        var oSelect = document.getElementById("mrn_count");
        if (oNewOption == null) {
            oNewOption = document.createElement("OPTION");
            oSelect.options.add(oNewOption);
        }
        oNewOption.text = oInput.value;
        oNewOption.value = oInput.value;
        oNewOption.selected = "true";
    }

</script>

<style>
	#mrnLog {
		display: none;
	}
</style>

<h2><spring:message code="idcards.generateIdentifiers.title"/></h2>
<span><spring:message code="idcards.generateIdentifiers.details"/></span>
<br /><br/>
<b class="boxHeader"><spring:message code="idcards.generateIdentifiers.instructions"/> </b>
<form id="identifierForm" method="post" class="box">
    <table name="initialMrn" border="0" cellspacing="2" cellpadding="2">
        <tr id="initMrn" name="initMrn">
            <td align="left" valign="top">
            	<input type="text" id="initialMrnText" size="10" name="initialMrn"/>
            	<spring:message code="idcards.generateIdentifiers.init"/>
            </td>
        </tr>
    </table>
	<table border="0" cellspacing="2" cellpadding="2">
		<tr>
			<td align="left" valign="top" >
                <input type='text' id="mrn_count_text" size="10" onkeyup="addMrnListItem(this)" />
				<select id="mrn_count" name="mrn_count" editable="true">
                    <option>100</option>
                    <option selected="true">500</option>
					<option>1000</option>
					<option>100000</option>
				</select>
			</td>
            <td>
                &nbsp;<label for="mrn_count"><spring:message code="idcards.generateIdentifiers.number"/></label><br/>
            </td>
		</tr>
		<tr>
			<td>
				<input type="submit" value="<spring:message code="general.submit"/>">
			</td>
		</tr>
        <input type="hidden" id="lastIdentifierInput" value="${lastIdentifier}"/>
	</table>
</form>

<br/>

<spring:message code="idcards.unprintedQuantity" arguments="${unprintedQuantity}"/><br/>

<br/>

<c:choose>
	<c:when test="${param.showLog == 1}">
		<b class="boxHeader"><spring:message code="idcards.generated.log"/></b>
		<div class="box">
			<table cellpadding="4" cellspacing="0">
				<tr>
					<th><spring:message code="idcards.first"/></th>
					<th><spring:message code="idcards.last"/></th>
					<th><spring:message code="idcards.count"/></th>
					<th><spring:message code="idcards.generatedBy"/></th>
					<th><spring:message code="idcards.dateGenerated"/></th>
				</tr>
				<c:forEach items="${generatedGroups}" var="groupMap" varStatus="status">
					<tr class="<c:choose><c:when test="${status.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
						<td>${groupMap.key.id}</td>
						<td>${groupMap.key.id + groupMap.value - 1}</td>
						<td>${groupMap.value}</td>
						<td>${groupMap.key.generator}</td>
						<td>${groupMap.key.dateGenerated}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
				
		<br/>
		
		<b class="boxHeader"><spring:message code="idcards.printed.log"/></b>
		<div class="box">
			<table cellpadding="4" cellspacing="0">
				<tr>
					<th><spring:message code="idcards.count"/></th>
					<th><spring:message code="idcards.printed.printedBy"/></th>
					<th><spring:message code="idcards.printed.date"/></th>
					<th><spring:message code="idcards.printed.withTemplate"/></th>
				</tr>
				<c:forEach items="${printedGroups}" var="groupMap" varStatus="status">
					<tr class="<c:choose><c:when test="${status.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
						<td>${groupMap.value}</td>
						<td>${groupMap.key.printedBy}</td>
						<td>${groupMap.key.datePrinted}</td>
						<td>${groupMap.key.printedTemplate.name}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:when>
	<c:otherwise>
		<a href="?showLog=1"><spring:message code="idcards.printed.log.view"/></a>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
   initPage();
</script>

<%@ include file="/WEB-INF/template/footer.jsp" %>