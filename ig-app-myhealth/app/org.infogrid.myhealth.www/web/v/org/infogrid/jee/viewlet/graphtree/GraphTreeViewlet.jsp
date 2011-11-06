<%@    page contentType="text/html"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="tree"  uri="/v/org/infogrid/jee/taglib/mesh/tree/tree.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="set"   uri="/v/org/infogrid/jee/taglib/mesh/set/set.tld"
 %><%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %>
<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/jee/viewlet/graphtree/GraphTreeViewlet.css"/>
<v:viewletAlternatives />
<v:viewlet formId="viewlet">
 <h1>Graph Tree for: <mesh:meshObjectId meshObjectName="Subject" stringRepresentation="Html" maxLength="30"/></h1>

 <div class="content">
  <div class="org-infogrid-jee-viewlet-graphtree-GraphTreeViewlet-content">
   <set:pathIterate traversalPathSetName="Viewlet.viewedMeshObjects.traversalPathSet" loopVar="path">
    <set:iteratecontentrow>
     <v:includeViewlet reachedByName="path" requestContext="include"/>
    </set:iteratecontentrow>
   </set:pathIterate>
  </div>
  <div class="org-infogrid-jee-viewlet-graphtree-GraphTreeViewlet-sidebar">
   <dl class="level1">
    <tree:treeIterate startObjectName="Subject" traversalSpecification="xpath:* xpath:*" levelVar="level" meshObjectLoopVar="current" traversalPathLoopVar="traversalPath">
     <tree:down>
      <dd><dl class="level${level}">
     </tree:down>
     <tree:up>
       </dl></dd>
     </tree:up>
     <tree:nodeBefore>
      <dt class="<v:ifIsReachedObject object='${current}'>reached</v:ifIsReachedObject> <v:ifIsReachedObject traversalPath='${traversalPath}'>selected</v:ifIsReachedObject>">
       <mesh:meshObjectLink meshObjectName="Subject" traversalPathName="traversalPath" addArguments="lid-format=viewlet:org.infogrid.jee.viewlet.graphtree.GraphTreeViewlet">
        <mesh:meshObject meshObjectName="current" maxLength="20" />
       </mesh:meshObjectLink>
      </dt>
     </tree:nodeBefore>
    </tree:treeIterate>
   </dl>
  </div>
 </div>
</v:viewlet>
