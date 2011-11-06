<%@    page contentType="text/html"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="candy" uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"     uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %>

 <table class="attributes">
  <thead>
   <tr>
    <th class="type">
     <v:ifState viewletState="edit">
      <div class="slide-in-button"><a href="javascript:overlay_show( 'org-infogrid-jee-shell-http-HttpShellVerb-bless', { 'shell.subject' : '<mesh:meshObjectId meshObjectName="Subject" stringRepresentation="Plain" filter="true" />' } )" title="Bless this MeshObject"><img src="${CONTEXT}/s/images/medal_bronze_add.png" alt="Add type"/></a></div>
     </v:ifState>
     Type
    </th>
    <th>Property</th>
    <th>Value</th>
   </tr>
  </thead>
  <tbody>
   <mesh:blessedByIterate meshObjectName="Subject" blessedByLoopVar="blessedBy">
    <tr>
     <th class="entityType" colspan="3">
      <v:ifState viewletState="edit">
       <div class="slide-in-button"><a href="javascript:overlay_show( 'org-infogrid-jee-shell-http-HttpShellVerb-unbless', { 'shell.subject' : '<mesh:meshObjectId meshObjectName="Subject" stringRepresentation="Plain" filter="true" />', 'shell.subject.unbless' : '<mesh:meshTypeId meshTypeName="blessedBy" stringRepresentation="Plain" filter="true" />' } )" title="Unbless this MeshObject"><img src="${CONTEXT}/s/images/medal_bronze_delete.png" alt="Delete"/></a></div>
      </v:ifState>
      <mesh:type meshTypeName="blessedBy"/> <mesh:meshTypeId meshTypeName="blessedBy"/>
     </th>
    </tr>
    <mesh:propertyIterate meshObjectName="Subject" meshTypeName="blessedBy" propertyTypeLoopVar="propertyType" propertyValueLoopVar="propertyValue" skipNullProperty="false">
     <tr>
      <th class="type"></th>
      <td class="name"><mesh:type meshTypeName="propertyType" /></td>
      <td class="value">
       <mesh:property meshObjectName="Subject" propertyTypeName="propertyType" ignore="true" state="${Viewlet.viewletState.name}"/>
      </td>
     </tr>
    </mesh:propertyIterate>
   </mesh:blessedByIterate>
  </tbody>
 </table>
