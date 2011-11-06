<%@    page contentType="text/html"
 %><%@ taglib prefix="set"   uri="/v/org/infogrid/jee/taglib/mesh/set/set.tld"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="candy" uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"     uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %>
<v:viewletAlternatives />
<v:viewlet>
 <u:safeForm method="post" action="${Viewlet.postUrl}">
  <table style="width: 100%">
   <tr>
    <td>
     <h2>Enter XML:</h2>
    </td>
   </tr>
   <tr>
    <td>
     <textarea name="bulkXml" style="width: 100%" rows="50">${Viewlet.bulkXml}</textarea>
    </td>
   </tr>
   <tr>
    <td>
     <div class="dialog-buttons">
      <table class="dialog-buttons">
       <tr>
        <td><input type="submit" value="Create"/></td>
       </tr>
      </table>
     </div>
    </td>
   </tr>    
  </table>
 </u:safeForm>
</v:viewlet>