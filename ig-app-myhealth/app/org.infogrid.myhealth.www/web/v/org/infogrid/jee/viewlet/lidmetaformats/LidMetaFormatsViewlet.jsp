<%@    page contentType="text/html"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="candy" uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"     uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %>
<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/jee/viewlet/lidmetaformats/LidMetaFormatsViewlet.css"/>
<v:viewletAlternatives />
<v:viewlet>
 <h1>Rendering choices for: <mesh:meshObjectId meshObjectName="Subject" stringRepresentation="Html" maxLength="30"/></h1>
 <p>In order of preference:</p>
 <ul>
  <c:forEach items="${Viewlet.viewletFactoryChoices}" var="current">
   <li><a href="?lid-format=viewlet:${current.name}">${current.userVisibleName}</a></li>
  </c:forEach>
 </ul>
</v:viewlet>