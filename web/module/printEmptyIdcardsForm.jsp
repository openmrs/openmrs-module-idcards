<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Print Id Cards" otherwise="/login.htm" redirect="/module/idcards/printEmptyIdcards.form"/>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<openmrs:globalProperty key="idcards.enableIdentifierGeneration" var="enableIdentifierGeneration"/>

<c:if test="${enableIdentifierGeneration == 'true'}">

<script type="text/javascript">
	function toggle(link, id) {
		var trace = document.getElementById(id);
		if (link.innerHTML == '<spring:message code="idcards.printed.log.view"/>') {
			link.innerHTML = '<spring:message code="idcards.printed.log.hide"/>';
			trace.style.display = "block";
		}
		else {
			link.innerHTML = '<spring:message code="idcards.printed.log.view"/>';
			trace.style.display = "none";
		}
		return false;
	}

    function displayInitMrn(displayOn) {
        var initMrnSection = document.getElementsByName("initialMrn");
        var mrnCountText = document.getElementById("mrn_count_text");
        for (var i=0; i<initMrnSection.length; i++) {
            if (displayOn == "true") {
                initMrnSection[i].style.display = "";
                initMrnSection[1].focus();
            }
            else {
                initMrnSection[i].style.display = "none";
                mrnCountText.focus();
            }
        }
    }

    function alertInit(form) {
        var lastIdentifier = document.getElementById("lastIdentifier");
        var initMrnSection = document.getElementById("initMrn");
        var initialMrnText = document.getElementById("initialMrnText");
        if (lastIdentifier.contains("No previous") ||
                (initMrnSection.style.display == ""
                     && initialMrnText.value.length < 1)) {
                alert("Please enter a positive number for the value of the first patient identifier to be printEmptyd.");
                return false;
        }
        else {
            var printEmptyAction = getElementById("printEmptyAction");
            form.action=printEmptyAction.value;
            return printEmptyAction.value;
        }
    }

    function initPage() {
        var lastIdentifier = document.getElementById("lastIdentifierInput");
        if (lastIdentifier.value == "No previous identifiers have been printEmptyd.") {
            displayInitMrn("true");
        }
        else {
            displayInitMrn("false");
        }
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

<h2><spring:message code="idcards.printEmpty.title"/></h2>
<span><spring:message code="idcards.printEmpty.details"/></span>
<br/>


<br/>
<b class="boxHeader"><spring:message code="idcards.printEmpty.instructions"/> </b>
<form id="identifierForm" method="post" class="box" action="${pageContext.request.contextPath}/moduleServlet/idcards/printEmpty">
	<table border="0" cellspacing="2" cellpadding="2">
		<tr>
			<td align="left" valign="top" >
                <input type='text' id="mrn_count_text" size="5" onkeyup="addMrnListItem(this)" />
				<select id="mrn_count" name="mrn_count" editable="true">
                    <option>100</option>
                    <option selected="true">500</option>
					<option>1000</option>
					<option>2000</option>
				</select>
			</td>
            <td>
                &nbsp;<label for="mrn_count"><spring:message code="idcards.printEmpty.number"/></label><br/>
            </td>
		</tr>
		<tr>
			<td align="left" valign="top" >
				
				<openmrs:globalProperty var="min" key="idcards.generateMin"/>
				<openmrs:globalProperty var="max" key="idcards.generateMax"/>
				
				<select name="generated_mrns">
					<option value="none"><spring:message code="idcards.printEmpty.dontUseGeneratedMRNs"/></option>
					<option value="pregenerated" <c:if test="${unprintedQuantity == 0}">disabled</c:if>><spring:message code="idcards.printEmpty.useGeneratedMRNs" arguments="${unprintedQuantity}"/></option>
					<option value="generateNew" <c:if test="${max - min <= 0}">disabled</c:if>><spring:message code="idcards.printEmpty.generateNewMRNs" arguments="${min},${max}"/></option>
				</select>
			</td>
            <td>
                &nbsp;
            </td>
		</tr>
        <tr>
            <td><select id="templateId" name="templateId">
                <c:forEach items="${cardTemplates}" var="template">
                    <c:if test="${template.type == 'Print New Identifiers' || template.type == 'Blank' || template.type == ''}">
                        <option value="${template.templateId}">${template.name}</option>
                    </c:if>
                </c:forEach>
            </select>
            </td>
            <td>
                &nbsp;<label for="templateId"><spring:message code="idcards.printEmpty.template"/></label><br/>
            </td>
        </tr>
        <tr>
			<td align="left" valign="top" >
                <input type="text" name="pdf_password" id="pdf_password" size="25" />
			</td>
            <td>
                &nbsp;<label for="pdf_password"><spring:message code="idcards.printEmpty.password"/></label><br/>
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

<c:choose>
	<c:when test="${param.showLog == 1}">
		<div id="mrnLog">
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
		</div>
	</c:when>
	<c:otherwise>
		<a href="?showLog=1"><spring:message code="idcards.printed.log.view"/></a><br/>
	</c:otherwise>
</c:choose>
<br/>
<script type="text/javascript">
   initPage();
</script>

</c:if>

<openmrs:globalProperty key="idcards.enablePrintingUploadedIdentifiers" var="enablePrintingUploadedIdentifiers"/>

<c:if test="${enablePrintingUploadedIdentifiers == 'true'}">

<b class="boxHeader"><spring:message code="idcards.uploadIdentifiersToPrint"/></b>
<form id="identifierForm"  method="post" enctype="multipart/form-data" class="box" action="${pageContext.request.contextPath}/module/idcards/uploadAndPrintIdCards.form">
	<span><spring:message code="idcards.uploadIdentifiersToPrintInstructions"/></span>
	<br/>
	<table>
		<tr>
			<th><spring:message code="idcards.fileToUpload"/>:</th>
			<td><input type="file" name="inputFile" size="50"/></td>
		</tr>
        <tr>
        	<th><spring:message code="idcards.cardTemplate"/>:</th>
	        <td>
	            <select id="templateId" name="templateId">
	                <c:forEach items="${cardTemplates}" var="template">
	                    <c:if test="${template.type == 'Print New Identifiers' || template.type == 'Blank' || template.type == ''}">
	                        <option value="${template.templateId}">${template.name}</option>
	                    </c:if>
	                </c:forEach>
	            </select>
            </td>
        </tr>
        <tr>
        	<th><spring:message code="idcards.pdfPassword"/>:</th>
			<td><input type="text" name="pdfPassword" id="pdf_password" size="25" /></td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="<spring:message code="general.submit"/>">
			</td>
		</tr>
	</table>
</form>

</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>