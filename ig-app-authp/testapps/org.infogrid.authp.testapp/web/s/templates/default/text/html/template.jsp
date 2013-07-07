<%@    page contentType="text/html"
 %><%@ page pageEncoding="UTF-8"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %><%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
 <head>
  <tmpl:inline sectionName="html-head"/>
 </head>
 <body>
  <c:if test="${org_infogrid_authp_ContentServlet__show_login}">
   <script src="w/user-pass.js" type="text/javascript"></script>
  </c:if>
  <c:if test="${org_infogrid_authp_ContentServlet__show_logout}">
   <a href="?lid=">Sign out</a>
  </c:if>
  <tmpl:ifErrors>
   <div class="errors">
    <tmpl:inlineErrors stringRepresentation="Html"/>
   </div>
  </tmpl:ifErrors>
  <tmpl:ifNotEmpty sectionName="html-messages">
   <div class="messages">
    <tmpl:inline sectionName="html-messages"/>
   </div>
  </tmpl:ifNotEmpty>
  <tmpl:inline sectionName="text-default"/>
 </body>
</html>
