<%@    page contentType="text/html"
 %><%@ taglib prefix="mesh"  uri="/v/org/infogrid/jee/taglib/mesh/mesh.tld"
 %><%@ taglib prefix="candy" uri="/v/org/infogrid/jee/taglib/candy/candy.tld"
 %><%@ taglib prefix="u"     uri="/v/org/infogrid/jee/taglib/util/util.tld"
 %><%@ taglib prefix="v"     uri="/v/org/infogrid/jee/taglib/viewlet/viewlet.tld"
 %><%@ taglib prefix="tmpl"  uri="/v/org/infogrid/jee/taglib/templates/templates.tld"
 %><%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core"
 %>

<candy:overlay id="org-infogrid-jee-shell-http-HttpShellVerb-bless">
 <u:safeForm method="post" action="${Viewlet.postUrl}">
  <h2>Bless a MeshObject</h2>
  <table>
   <tr>
    <td class="label">MeshObject:</td>
    <td>
     <input class="subject" name="shell.subject" size="32" readonly="readonly" />
    </td>
   </tr>
   <tr>
    <td class="label">Type:</td>
    <td>
     <input class="subjecttype" name="shell.subject.bless" size="32" />
    </td>
   </tr>
   <tr>
    <td colspan="2">
     <div class="dialog-buttons">
      <table class="dialog-buttons">
       <tr>
        <td><input type="submit" value="Bless" /></td>
        <td><a href="javascript:overlay_hide( 'org-infogrid-jee-shell-http-HttpShellVerb-bless' )">Cancel</a></td>
       </tr>
      </table>
     </div>
    </td>
   </tr>
  </table>
 </u:safeForm>
</candy:overlay>