<%@    page contentType="text/html"
 %><%@ page pageEncoding="UTF-8"
 %><%@ taglib prefix="c"    uri='http://java.sun.com/jsp/jstl/core'
 %><%@ taglib prefix="u"    uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="tmpl" uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
 <head>
  <tmpl:inline sectionName="html-title"/>
  <link rel="stylesheet" href="${CONTEXT}/s/templates/bland/bland.css" type="text/css" />
  <tmpl:inline sectionName="html-head"/>
 </head>
 <body>
  <div id="canvas-top">
   <div id="canvas-app-row">
    <div class="canvas-main">
     <h1><a href="${CONTEXT}/">The Unnamed App</a></h1>
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
      <tmpl:inlineErrors stringRepresentation="Html"/>
     </div>
    </tmpl:ifErrors>
    <tmpl:inline sectionName="text-default"/>
   </div>
  </div>
  <div id="canvas-bottom">
   <div class="canvas-main footnote">&copy; 2001-2010 NetMesh Inc. All rights
    reserved. NetMesh and InfoGrid are trademarks or registered trademarks of NetMesh Inc.
    Learn more about <a href="http://infogrid.org/">InfoGrid</a>.</div>
  </div>
 </body>
</html>
