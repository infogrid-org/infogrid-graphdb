<%@    page contentType="text/html"
 %><%@ taglib prefix="set"   uri="/v/org/infogrid/jee/taglib/mesh/set/set.tld"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="candy" uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"     uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %>
<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/jee/viewlet/wikiobject/WikiObjectDisplayViewlet.css"/>
<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/jee/taglib/mesh/PropertyTag.css"/>
<tmpl:script src="${CONTEXT}/v/org/infogrid/jee/taglib/mesh/PropertyTag.js"/>
<v:viewletAlternatives />
<v:viewlet>
 <div class="content">
  <mesh:property meshObjectName="Subject" propertyType="org.infogrid.model.Wiki/WikiObject_Content"/>
 </div>
</v:viewlet>