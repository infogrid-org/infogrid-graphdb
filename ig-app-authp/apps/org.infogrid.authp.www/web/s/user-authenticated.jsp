<%@    taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core"
 %><%@ taglib prefix="tmpl" uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %>
<h1>Attributes available to you about this user:</h1>
<div class="attributes">
 <table>
  <col class="label" />
  <col class="field" />
  <thead>
   <tr>
    <th>Attribute</th>
    <th>Value</th>
   </tr>
  </thead>
  <c:forEach var="pair" items="${org_infogrid_lid_RequestedResource.attributes}">
   <tr>
    <td>${pair.key}:</td>
    <td>${pair.value}</td>
   </tr>
  </c:forEach>
 </table>
</div>
