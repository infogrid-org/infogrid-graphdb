<%@    taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core"
 %><%@ taglib prefix="tmpl" uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %>
<p>Welcome.</p>
<c:if test="${lid ne null}">
 <p>Go to <a href="${lid}">my own page</a>.</p>
</c:if>