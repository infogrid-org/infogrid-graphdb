<candy:overlay id="org-infogrid-jee-shell-http-HttpShellVerb-sweepAll">
 <u:safeForm method="post" action="${Viewlet.postUrl}">
  <h2>Do you really want to sweep all MeshObjects in this MeshBase?</h2>
  <input type="hidden" name="shell.command" value="sweepAll" />
  <table>
   <tr>
    <td>
     <div class="dialog-buttons">
      <table class="dialog-buttons">
       <tr>
        <td><input type="submit" value="Sweep All" /></td>
        <td><a href="javascript:overlay_hide( 'org-infogrid-jee-shell-http-HttpShellVerb-sweepAll' )">Cancel</a></td>
       </tr>
      </table>
     </div>
    </td>
   </tr>
  </table>
 </u:safeForm>
</candy:overlay>