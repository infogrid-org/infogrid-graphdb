<%@    page contentType="text/html"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %>
 <div class="audit">
  <table>
   <tr>
    <td class="label">Created:</td>
    <td><mesh:timeCreated meshObjectName="Subject" /></td>
   </tr>
   <tr>
    <td class="label">Updated:</td>
    <td><mesh:timeUpdated meshObjectName="Subject" /></td>
   </tr>
   <tr>
    <td class="label">Last&nbsp;read:</td>
    <td><mesh:timeRead meshObjectName="Subject" /></td>
   </tr>
  </table>
 </div>
