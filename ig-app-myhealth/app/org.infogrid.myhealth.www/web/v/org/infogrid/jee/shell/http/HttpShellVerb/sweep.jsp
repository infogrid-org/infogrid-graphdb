<candy:overlay id="org-infogrid-jee-shell-http-HttpShellVerb-sweep">
 <u:safeForm method="post" action="${Viewlet.postUrl}">
  <h2>Do you really want to sweep this MeshObject?</h2>
  <table>
   <tr>
    <td class="label">MeshObject:</td>
    <td>
     <input class="subject" name="shell.subject" size="40" readonly="readonly" />
     <input type="hidden" name="shell.subject.access" value="sweep" />
    </td>
   </tr>
   <tr>
    <td colspan="2">
     <div class="dialog-buttons">
      <table class="dialog-buttons">
       <tr>
        <td><input type="submit" value="Sweep" /></td>
        <td><a href="javascript:overlay_hide( 'org-infogrid-jee-shell-http-HttpShellVerb-sweep' )">Cancel</a></td>
       </tr>
      </table>
     </div>
    </td>
   </tr>
  </table>
 </u:safeForm>
</candy:overlay>