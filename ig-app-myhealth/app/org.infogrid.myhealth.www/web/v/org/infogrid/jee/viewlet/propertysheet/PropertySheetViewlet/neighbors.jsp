<%@    page contentType="text/html"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="candy" uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"     uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %>
 <table class="neighbors">
  <thead>
   <tr>
    <th class="role">Roles</th>
    <th class="neighbor">
     <v:notIfState viewletState="edit">
      <div class="slide-in-button">
       <u:callJspo page="/o/relate.jspo" action="${Viewlet.postUrl}" linkTitle="Add a neighbor" submitLabel="Relate">
        <u:callJspoParam name="from" value="${Subject}"/>
        <img src="${CONTEXT}/s/images/link_add.png" alt="Add a neighbor" />
       </u:callJspo>
      </div>
     </v:notIfState>
     Neighbor
    </th>
   </tr>
  </thead>
  <tbody>
   <mesh:neighborIterate meshObjectName="Subject" neighborLoopVar="neighbor">
    <tr>
     <td class="role">
      <v:notIfState viewletState="edit">
       <div class="slide-in-button">
        <u:callJspo page="/o/blessRole.jspo" action="${Viewlet.postUrl}" linkTitle="Add a role" submitLabel="Add role">
         <u:callJspoParam name="from" value="${Subject}"/>
         <u:callJspoParam name="to"   value="${neighbor}"/>
         <img src="${CONTEXT}/s/images/medal_silver_add.png" alt="Add a role" />
        </u:callJspo>
       </div>
      </v:notIfState>
      <ul>
       <mesh:roleIterate startObjectName="Subject" destinationObjectName="neighbor" roleTypeLoopVar="roleType">
        <li>
         <v:notIfState viewletState="edit">
          <div class="slide-in-button">
           <u:callJspo page="/o/unblessRole.jspo" action="${Viewlet.postUrl}" linkTitle="Delete a role" submitLabel="Delete role">
            <u:callJspoParam name="from" value="${Subject}"/>
            <u:callJspoParam name="to"   value="${neighbor}"/>
            <u:callJspoParam name="role" value="${roleType}"/>
            <img src="${CONTEXT}/s/images/medal_silver_delete.png" alt="Delete a role" />
           </u:callJspo>
          </div>
         </v:notIfState>
         <mesh:type meshTypeName="roleType" filter="false"/>
        </li>
       </mesh:roleIterate>
      </ul>
     </td>
     <td class="neighbor">
      <v:notIfState viewletState="edit">
       <div class="slide-in-button">
        <u:callJspo page="/o/unrelate.jspo" action="${Viewlet.postUrl}" linkTitle="Unrelate from neighbor" submitLabel="Unrelate">
         <u:callJspoParam name="from" value="${Subject}"/>
         <u:callJspoParam name="to"   value="${neighbor}"/>
         <img src="${CONTEXT}/s/images/link_delete.png" alt="Unrelate from neighbor" />
        </u:callJspo>
       </div>
      </v:notIfState>
      <mesh:meshObjectLink meshObjectName="neighbor"><mesh:meshObjectId meshObjectName="neighbor" stringRepresentation="Plain" filter="true" maxLength="35" /></mesh:meshObjectLink>
     </td>
    </tr>
   </mesh:neighborIterate>
  </tbody>
 </table>
