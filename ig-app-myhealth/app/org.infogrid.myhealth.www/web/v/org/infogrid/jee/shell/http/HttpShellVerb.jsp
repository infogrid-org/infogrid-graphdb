<%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld" %>
<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/jee/shell/http/HttpShellVerb.css"/>
<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/jee/taglib/candy/OverlayTag.css"/>
<tmpl:script src="${CONTEXT}/v/org/infogrid/jee/taglib/candy/OverlayTag.js"/>
<%@ include file="HttpShellVerb/bless.jsp" %>
<%@ include file="HttpShellVerb/blessRole.jsp" %>
<%@ include file="HttpShellVerb/create.jsp" %>
<%@ include file="HttpShellVerb/delete.jsp" %>
<%@ include file="HttpShellVerb/relate.jsp" %>
<%@ include file="HttpShellVerb/setProperty.jsp" %>
<%@ include file="HttpShellVerb/unbless.jsp" %>
<%@ include file="HttpShellVerb/unblessRole.jsp" %>
<%@ include file="HttpShellVerb/unrelate.jsp" %>

<%@ include file="HttpShellVerb/sweep.jsp" %>
<%@ include file="HttpShellVerb/sweepAll.jsp" %>
