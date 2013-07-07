<%@    page contentType="text/html"
 %><%@ taglib prefix="set"   uri="/v/org/infogrid/jee/taglib/mesh/set/set.tld"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="candy" uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"     uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %><%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %>

<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/myhealth/viewlet/patients/PatientsCollectionViewlet.css"/>
<v:viewletAlternatives />
<v:viewlet>
 <div class="toolbar">
  <ul>
   <li>
    <u:callJspo page="/o/personcollection/create-patient.jspo" action="${Viewlet.postUrl}" linkTitle="Create a new patient" submitLabel="Create">
     <img src="${CONTEXT}/s/images/add.png" alt="[add]" />
    </u:callJspo>
   </li>
  </ul>
 </div>

 <h1>Patients:</h1>

 <set:traverseIterate startObject="users" traversalSpecification="org.infogrid.model.MyHealth/PersonCollection_Collects_Person-S" loopVar="person">
  <set:iteratenocontent>
   <p>None so far.</p>
  </set:iteratenocontent>
  <set:iterateheader>
   <ul>
  </set:iterateheader>
  <set:iteratecontentrow>
   <li>
    <mesh:meshObjectLink meshObjectName="person"><mesh:meshObject meshObjectName="person"/></mesh:meshObjectLink>
   </li>
  </set:iteratecontentrow>
  <set:iteratefooter>
   </ul>
  </set:iteratefooter>
 </set:traverseIterate>
</v:viewlet>