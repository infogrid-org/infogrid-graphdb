<%@    taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core"
 %><%@ taglib prefix="tmpl" uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %>
<h1>Attributes known by the Authentication Provider about you:</h1>
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
  <tbody>
   <c:forEach var="pair" items="${org_infogrid_lid_RequestedResource.attributes}">
    <tr>
     <td>${pair.key}:</td>
     <td>${pair.value}</td>
    </tr>
   </c:forEach>
  </tbody>
 </table>
</div>
