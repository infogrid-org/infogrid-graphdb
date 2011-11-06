<%@    page contentType="text/html"
 %><%@ taglib prefix="set"   uri="/v/org/infogrid/jee/taglib/mesh/set/set.tld"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="candy" uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"     uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %><%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.infogrid.jee.viewlet.log4j.Log4jConfigurationViewlet" %>
<%@ page import="org.infogrid.util.logging.log4j.Log4jLog" %>
<tmpl:stylesheet href="${CONTEXT}/v/org/infogrid/jee/viewlet/log4j/Log4jConfigurationViewlet.css"/>
<v:viewletAlternatives />
<v:viewlet>
 <h1>Log4j Configuration</h1>
 <u:safeForm method="post" action="${Viewlet.postUrl}">
  <table class="log4j">
   <thead>
    <tr>
     <th>Logger name</th>
     <th>Logging level</th>
    </tr>
   </thead>
   <tbody>
<%

Log4jConfigurationViewlet vl = (Log4jConfigurationViewlet) request.getAttribute( "Viewlet" );
Iterator<Log4jLog> iter = vl.loggerIterator();
while( iter.hasNext() ) {
    Log4jLog current = iter.next();
    Log4jConfigurationViewlet.LEVEL currentLevel
            = current.getDelegate().getLevel() != null ? Log4jConfigurationViewlet.LEVEL.valueOf( current.getDelegate().getLevel().toString() ) : null;
 %>
    <tr>
     <td class="name"><%= current.getDelegate().getName() %></td>
     <td class="value">
      <select name="field-<%= current.getDelegate().getName() %>">
       <option>parent</option>
<%
    for( Log4jConfigurationViewlet.LEVEL l : Log4jConfigurationViewlet.LEVEL.values() ) {
        if( currentLevel == l ) {
 %>
 <option selected="selected"><%= l %></option>
<%
        } else {
 %>
 <option><%= l %></option>
<%
        }
    }
 %>
      </select>
     </td>
    </tr>
<%
}
 %>
    <tr>
     <td colspan="2">
      <div class="dialog-buttons">
       <table class="dialog-buttons">
        <tr>
         <td><input type="submit" value="Change"/></td>
        </tr>
       </table>
      </div>
     </td>
    </tr>
   </tbody>
  </table>
 </u:safeForm>
</v:viewlet>