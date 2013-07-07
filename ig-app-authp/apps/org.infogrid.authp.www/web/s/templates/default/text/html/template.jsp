<%@    page contentType="text/html"
 %><%@ page pageEncoding="UTF-8"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %><%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
 <head>
  <tmpl:inline sectionName="html-title"/>
  <link rel="stylesheet" href="${CONTEXT}/s/templates/default/text/html/default.css" type="text/css" />
  <link rel="stylesheet" href="${CONTEXT}/w/user-pass.css" type="text/css" />
  <tmpl:inline sectionName="html-head"/>
 </head>
 <body>
  <c:if test="${org_infogrid_authp_ContentServlet__show_logout}">
   <div id="canvas-identity">
    <div class="canvas-main">
     <ul>
      <li><a href="?lid=">Sign out</a></li>
     </ul>
    </div>
   </div>
  </c:if>
  <div id="canvas-top">
   <div id="canvas-company-row">
    <div class="canvas-main">
     <div class="canvas-company-logo">
      <a href="${CONTEXT}/" title="[go to top]"><img id="canvas-company-logo" src="${CONTEXT}/s/images/authp-logo.png" alt="[Logo]" /></a>
     </div>
     <h1>People</h1>
    </div>
   </div>
  </div>
  <div id="canvas-middle">
   <div class="canvas-main">
    <noscript>
     <div class="errors">
      <h2>Errors:</h2>
      <p>This site requires Javascript. Please enable Javascript before attempting to proceed.</p>
     </div>
    </noscript>
    <tmpl:ifErrors>
     <div class="errors">
      <h2>Error:</h2>
      <tmpl:inlineErrors stringRepresentation="Html"/>
     </div>
    </tmpl:ifErrors>
    <c:if test="${org_infogrid_authp_ContentServlet__show_login}">
     <script src="w/user-pass.js" type="text/javascript"></script>
    </c:if>
    <tmpl:ifNotEmpty sectionName="html-messages">
     <div class="messages">
      <tmpl:inline sectionName="html-messages"/>
     </div>
    </tmpl:ifNotEmpty>
    <tmpl:inline sectionName="text-default"/>
   </div>
  </div>
  <div id="canvas-bottom">
   <div class="canvas-main footnote">
    <a class="built-on-infogrid" href="http://infogrid.org/"><img src="${CONTEXT}/s/images/built-on-infogrid.png" title="Built on InfoGrid&trade;" alt="[Built on InfoGrid&trade;]"/></a>
    All rights reserved. &copy; 2008-2012 NetMesh Inc. InfoGrid&trade; is a trademark
    of NetMesh Inc. All other marks are marks of their respective owners.
   </div>
  </div>
 </body>
</html>
