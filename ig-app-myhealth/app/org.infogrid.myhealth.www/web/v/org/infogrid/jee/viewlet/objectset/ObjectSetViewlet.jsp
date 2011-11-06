<%@    page contentType="text/html"
 %><%@ taglib prefix="mesh"      uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="objectset" uri="/v/org/infogrid/jee/taglib/mesh/set/set.tld"
 %><%@ taglib prefix="candy"     uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"         uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"         uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="tmpl"      uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %><%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core"
 %>
<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/jee/viewlet/objectset/ObjectSetViewlet.css"/>
<v:viewletAlternatives />
<v:viewlet>
 <h1>ObjectSet found related to: <mesh:meshObjectId meshObjectName="Subject" stringRepresentation="Html" maxLength="30"/></h1>
 <table class="objects">
  <tbody>
   <objectset:iterate meshObjectSetName="Viewlet.viewedMeshObjects.reachedObjects" loopVar="current" ignore="true">
    <objectset:iteratecontentrow>
     <tr class="identifier">
      <th class="identifier">Identifier:</th>
      <td class="type" colspan="2"><mesh:meshObjectLink meshObjectName="current"><mesh:meshObjectId meshObjectName="current" stringRepresentation="Plain" filter="true" maxLength="35" /></mesh:meshObjectLink></td>
     </tr>
     <mesh:blessedByIterate meshObjectName="current" blessedByLoopVar="blessedBy">
      <tr>
       <th></th>
       <th class="type" colspan="2">
        <mesh:type meshTypeName="blessedBy"/>
       </th>
      </tr>
      <mesh:propertyIterate meshObjectName="current" meshTypeName="blessedBy" propertyTypeLoopVar="propertyType" propertyValueLoopVar="propertyValue" skipNullProperty="false">
       <tr>
        <th></th>
        <td class="type"><mesh:type meshTypeName="propertyType" /></td>
        <td class="value">
         <mesh:propertyValue propertyValueName="propertyValue" ignore="true"/>
        </td>
       </tr>
      </mesh:propertyIterate>
     </mesh:blessedByIterate>
    </objectset:iteratecontentrow>
    <objectset:iteratenocontent>
     <tr>
      <td>
       Empty set.
      </td>
     </tr>
    </objectset:iteratenocontent>
   </objectset:iterate>
  </tbody>
 </table> 
</v:viewlet>
