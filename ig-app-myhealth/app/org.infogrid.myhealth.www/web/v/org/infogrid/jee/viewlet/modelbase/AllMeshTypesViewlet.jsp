<%@    page contentType="text/html"
 %><%@ taglib prefix="set"   uri="/v/org/infogrid/jee/taglib/mesh/set/set.tld"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="candy" uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"     uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %>
<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/jee/viewlet/modelbase/AllMeshTypesViewlet.css"/>
<v:viewletAlternatives />
<v:viewlet>
 <h1>All MeshTypes in the ModelBase</h1>
 <c:forEach var="sa" items="${Viewlet.subjectAreas}">
  <table class="sa">
   <tr class="satitle">
    <td>
     <a name="<mesh:meshTypeId meshTypeName="sa" stringRepresentation="Plain" filter="true" />"></a>
     <span class="label">Subject&nbsp;Area:</span>
     <b><mesh:type meshTypeName="sa" propertyName="name"/></b>
     (Version <mesh:type meshTypeName="sa" propertyName="versionNumber" nullString="&lt;none given&gt;"/>)
    </td>
   </tr>
   <tr class="sacontent">
    <td>
     <table class="sacontent">
      <tr>
       <td class="label">Identifier</td>
       <td><code><mesh:meshTypeId meshTypeName="sa" stringRepresentation="Plain" filter="true" /></code></td>
      </tr>
      <tr>
       <td class="label">Name:</td>
       <td><mesh:type meshTypeName="sa" propertyName="name" filter="false"/></td>
      </tr>
      <tr>
       <td class="label">User&nbsp;name:</td>
       <td><mesh:type meshTypeName="sa" propertyName="userVisibleNameMap"/></td>
      </tr>
      <c:forEach var="current" items="${sa.userVisibleNameMap.pairIterator}">
       <tr>
        <td class="label">User&nbsp;name<br />(Locale: ${current.name})</td>
        <td>${current.value}</td>
       </tr>
      </c:forEach>
      <tr>
       <td class="label">Dependencies:</td>
       <td>
        <ul>
         <c:forEach var="current" items="${sa.subjectAreaDependencies}">
          <li><a href="#<mesh:meshTypeId meshTypeName="current" stringRepresentation="Plain" filter="true" />"><mesh:type meshTypeName="current" propertyName="name"/></a></li>
         </c:forEach>
        </ul>
       </td>
      </tr>
      <tr>
       <td class="label">Description:</td>
       <td><mesh:type meshTypeName="sa" propertyName="userVisibleDescription" filter="false"/></td>
      </tr>
      <c:forEach var="current" items="${sa.userVisibleDescriptionMap.pairIterator}">
       <tr>
        <td class="label">User&nbsp;description<br />(Locale: ${current.name})</td>
        <td>${current.value}</td>
       </tr>
      </c:forEach>
     </table>

     <c:forEach var="amo" items="${sa.entityTypes}">
      <table class="amo">
       <tr class="amotitle">
        <td>
         <a name="<mesh:meshTypeId meshTypeName="amo" stringRepresentation="Plain" filter="true" />"></a>
         <span class="label">Entity&nbsp;Type:</span>
         <b><mesh:type meshTypeName="amo" propertyName="name"/></b>
        </td>
       </tr>
       <tr class="amocontent">
        <td>
         <table class="entitycontent">
          <tr>
           <td class="label">Identifier:</td>
           <td><code><mesh:meshTypeId meshTypeName="amo" stringRepresentation="Plain" filter="true" /></code></td>
          </tr>
          <tr>
           <td class="label">Name:</td>
           <td><mesh:type meshTypeName="amo" propertyName="name" filter="false"/></td>
          </tr>
          <tr>
           <td class="label">User&nbsp;name:</td>
           <td><mesh:type meshTypeName="amo" propertyName="userVisibleNameMap"/></td>
          </tr>
          <c:forEach var="current" items="${amo.userVisibleNameMap.pairIterator}">
           <tr>
            <td class="label">User&nbsp;name<br />(Locale: ${current.name})</td>
            <td><mesh:propertyValue propertyValueName="current.value"/></td>
           </tr>
          </c:forEach>
          <tr>
           <td class="label">Is&nbsp;abstract:</td>
           <td><mesh:type meshTypeName="amo" propertyName="isAbstract" filter="false"/></td>
          </tr>
          <tr>
           <td class="label">Inherits&nbsp;from:</td>
           <td>
            <table class="amoinheritance">
             <tr>
              <c:forEach var="superpath" items="${amo.pathsFromInheritanceRoots}">
               <td>
                <ul>
                 <c:forEach var="current" items="${superpath}">
                  <li>
                   <a href="#<mesh:meshTypeId meshTypeName="current" stringRepresentation="Plain" filter="true" />"><mesh:type meshTypeName="current" propertyName="name" filter="false"/></a>
                  </li>
                 </c:forEach>
                </ul>
               </td>
              </c:forEach>
             </tr>
            </table>
           </td>
          </tr>
          <tr>
           <td class="label">Description:</td>
           <td><mesh:type meshTypeName="amo" propertyName="userVisibleDescription" filter="false"/></td>
          </tr>
          <c:forEach var="current" items="${amo.userVisibleDescriptionMap.pairIterator}">
           <tr>
            <td class="label">User&nbsp;description<br />(Locale: ${current.name})</td>
            <td><mesh:propertyValue propertyValueName="current.value" filter="false"/></td>
           </tr>
          </c:forEach>
          <tr>
           <td class="label">Properties:</td>
           <td class="properties">
            <ul class="properties">
             <c:forEach var="prop" items="${amo.inheritedPropertyTypes}">
              <li><a href="#<mesh:meshTypeId meshTypeName="prop" stringRepresentation="Plain" filter="true" />"><mesh:type meshTypeName="prop" propertyName="name" /></a></li>
             </c:forEach>
            </ul>
           </td>
          </tr>
          <tr>
           <td colspan="2" class="property">
            <c:forEach var="prop" items="${amo.localPropertyTypes}">
             <table class="property">
              <tr class="propertytitle">
               <td>
                <a name="<mesh:meshTypeId meshTypeName="prop" stringRepresentation="Plain" filter="true" />"></a>
                <span class="label">Property&nbsp;Type:</span>
                <b><mesh:type meshTypeName="prop" propertyName="name"/></b>
               </td>
              </tr>
              <tr class="propertycontent">
               <td>
                <table class="propertycontent">
                 <tr>
                  <td class="label">Identifier:</td>
                  <td><code><mesh:meshTypeId meshTypeName="prop" stringRepresentation="Plain" filter="true" /></code></td>
                 </tr>
                 <tr>
                  <td class="label">Name:</td>
                  <td><mesh:type meshTypeName="prop" propertyName="name" filter="false"/></td>
                 </tr>
                 <tr>
                  <td class="label">User&nbsp;name:</td>
                  <td><mesh:type meshTypeName="prop" propertyName="userVisibleNameMap"/></td>
                 </tr>
                 <c:forEach var="current" items="${prop.userVisibleNameMap.pairIterator}">
                  <tr>
                   <td class="label">User&nbsp;name<br />(Locale: ${current.name})</td>
                   <td>${current.value}</td>
                  </tr>
                 </c:forEach>                   
                 <tr>
                  <td class="label">Data&nbsp;type:</td>
                  <td><mesh:type meshTypeName="prop" propertyName="dataType" filter="false" stringRepresentation="Plain" /></td>
                 </tr>
                 <tr>
                  <td class="label">Optional:</td>
                  <td><mesh:type meshTypeName="prop" propertyName="isOptional" filter="false"/></td>
                 </tr>
                </table>
               </td>
              </tr>
             </table>
            </c:forEach>
           </td>
          </tr>
         </table>
        </td>
       </tr>
      </table>
     </c:forEach>

     <c:forEach var="amo" items="${sa.relationshipTypes}">
      <table class="amo">
       <tr class="amotitle">
        <td>
         <a name="<mesh:meshTypeId meshTypeName="amo" stringRepresentation="Plain" filter="true" />"></a>
         <span class="label">Relationship&nbsp;Type:</span>
         <b><mesh:type meshTypeName="amo" propertyName="name"/></b>
        </td>
       </tr>
       <tr class="amocontent">
        <td>
         <table class="relationshipcontent">
          <tr>
           <td class="label">Identifier:</td>
           <td><code><mesh:meshTypeId meshTypeName="amo" stringRepresentation="Plain" filter="true" /></code></td>
          </tr>
          <tr>
           <td class="label">Name:</td>
           <td><mesh:type meshTypeName="amo" propertyName="name" filter="false"/></td>
          </tr>
          <tr>
           <td class="label">User&nbsp;name:</td>
           <td><mesh:type meshTypeName="amo" propertyName="userVisibleNameMap"/></td>
          </tr>
          <c:forEach var="current" items="${amo.userVisibleNameMap.pairIterator}">
           <tr>
            <td class="label">User&nbsp;name<br />(Locale: ${current.name})</td>
            <td>${current.value}</td>
           </tr>
          </c:forEach>
          <tr>
           <td class="label">Inherits&nbsp;from:</td>
           <td>
            <table class="amoinheritance">
             <tr>
              <c:forEach var="superpath" items="${amo.pathsFromInheritanceRoots}">
               <td>
                <ul>
                 <c:forEach var="current" items="${superpath}">
                  <li>
                   <a href="#<mesh:meshTypeId meshTypeName="current" stringRepresentation="Plain" filter="true" />"><mesh:type meshTypeName="current" propertyName="name" filter="false"/></a>
                  </li>
                 </c:forEach>
                </ul>
               </td>
              </c:forEach>
             </tr>
            </table>
           </td>
          </tr>
          <tr>
           <td colspan="2">
            <table class="roletypes">
             <tr>
              <td class="label">RoleTypes:</td>
              <td class="label left">Source:</td>
              <td class="label left">Destination:</td>
             </tr>
             <tr>
              <td class="label">EntityType:</td>
              <td>
               <c:choose>
                <c:when test="${amo.source.entityType != null}">
                 <a href="#<mesh:meshTypeId meshTypeName="amo.source.entityType.identifier" stringRepresentation="Plain" filter="true"/>"><mesh:type meshTypeName="amo" propertyName="source.entityType.name" nullString="(Any)" filter="false"/></a>
                </c:when>
                <c:otherwise>
                (Any)
                </c:otherwise>
               </c:choose>
              </td>
              <td>
               <c:choose>
                <c:when test="${amo.destination.entityType != null}">
                <a href="#<mesh:meshTypeId meshTypeName="amo.destination.entityType.identifier" stringRepresentation="Plain" filter="true"/>"><mesh:type meshTypeName="amo" propertyName="destination.entityType.name" nullString="(Any)" filter="false"/></a>
                </c:when>
                <c:otherwise>
                (Any)
                </c:otherwise>
               </c:choose>
              </td>
             </tr>
             <tr>
              <td class="label">Multiplicity:</td>
              <td><span class="multiplicity">(<mesh:type meshTypeName="amo" propertyName="source.multiplicity"/>)</span></td>
              <td><span class="multiplicity">(<mesh:type meshTypeName="amo" propertyName="destination.multiplicity"/>)</span></td>
             </tr>
             <tr>
              <td class="label">Identifier:</td>
              <td><code><mesh:meshTypeId meshTypeName="amo.source.identifier" stringRepresentation="Plain" filter="true" /></code></td>
              <td><code><mesh:meshTypeId meshTypeName="amo.destination.identifier" stringRepresentation="Plain" filter="true" /></code></td>
             </tr>
            </table>
           </td>
          </tr>
          <tr>
           <td class="label">Description:</td>
           <td><mesh:type meshTypeName="amo" propertyName="userVisibleDescription" filter="false"/></td>
          </tr>
          <c:forEach var="current" items="${amo.userVisibleDescriptionMap.pairIterator}">
           <tr>
            <td class="label">User&nbsp;description<br />(Locale: ${current.name})</td>
            <td>${current.value}</td>
           </tr>
          </c:forEach>
         </table>
        </td>
       </tr>         
      </table>
     </c:forEach>
    </td>
   </tr>
  </table>
 </c:forEach>
</v:viewlet>