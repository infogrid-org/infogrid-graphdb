function org_infogrid_jee_taglib_mesh_PropertyTag_initProperty_value( nodeName, editIndex, currentValue, isOptional, isReadOnly ) {
    var nullInput  = document.getElementById( nodeName + '.propertyvalue.' + editIndex + '.null' );
    var createSpan = document.getElementById( nodeName + '.' + editIndex + '.span.create' );
    var removeSpan = document.getElementById( nodeName + '.' + editIndex + '.span.remove' );
    var valueSpan  = document.getElementById( nodeName + '.' + editIndex + '.span.value' );

    if( currentValue == null ) {
        nullInput.value = 'true';
        createSpan.style.display = 'block';
        removeSpan.style.display = 'none';
        valueSpan.style.display  = 'none';

    } else if( isOptional ) {
        removeSpan.style.display = 'inline';
    }
}

function org_infogrid_jee_taglib_mesh_PropertyTag_doRemove( nodeName, editIndex ) {
    var nullInput  = document.getElementById( nodeName + '.propertyvalue.' + editIndex + '.null' );
    var createSpan = document.getElementById( nodeName + '.' + editIndex + '.span.create' );
    var removeSpan = document.getElementById( nodeName + '.' + editIndex + '.span.remove' );
    var valueSpan  = document.getElementById( nodeName + '.' + editIndex + '.span.value' );

    nullInput.value = 'true';

    createSpan.style.display = 'block';
    removeSpan.style.display = 'none';
    valueSpan.style.display  = 'none';
}
function org_infogrid_jee_taglib_mesh_PropertyTag_doCreate( nodeName, editIndex ) {
    var nullInput  = document.getElementById( nodeName + '.propertyvalue.' + editIndex + '.null' );
    var createSpan = document.getElementById( nodeName + '.' + editIndex + '.span.create' );
    var removeSpan = document.getElementById( nodeName + '.' + editIndex + '.span.remove' );
    var valueSpan  = document.getElementById( nodeName + '.' + editIndex + '.span.value' );

    nullInput.value = 'false';

    createSpan.style.display = 'none';
    removeSpan.style.display = 'inline';
    valueSpan.style.display  = 'inline';
}

function org_infogrid_jee_taglib_mesh_PropertyTag_mimeSet( nodeName, editIndex, mime ) {
    var mimeEl = document.getElementById( nodeName + '.propertyvalue.' + editIndex + '.mime' );
    var textEl = document.getElementById( nodeName + '.propertyvalue.' + editIndex + '.text' );
    var dataEl = document.getElementById( nodeName + '.propertyvalue.' + editIndex + '.data' );

    if( mimeEl.value.substring( 0, 5 ) == "text/" ) {
        if( textEl != null ) {
            textEl.style.display = 'inline';
        }
        if( dataEl != null ) {
            dataEl.style.display = 'none';
        }
    } else {
        if( textEl != null ) {
            textEl.style.display = 'none';
        }
        if( dataEl != null ) {
            dataEl.style.display = 'inline';
        }
    }
}

function org_infogrid_jee_taglib_mesh_PropertyTag_mimeChanged( nodeName, editIndex, mime ) {
    var mimeEl = document.getElementById( nodeName + '.propertyvalue.' + editIndex + '.mime' );
    var textEl = document.getElementById( nodeName + '.propertyvalue.' + editIndex + '.text' );
    var dataEl = document.getElementById( nodeName + '.propertyvalue.' + editIndex + '.data' );

    if( mimeEl.value.substring( 0, 5 ) == "text/" ) {
        if( textEl != null ) {
            textEl.style.display = 'inline';
        }
        if( dataEl != null ) {
            dataEl.style.display = 'none';
        }
    } else {
        if( textEl != null ) {
            textEl.style.display = 'none';
        }
        if( dataEl != null ) {
            dataEl.style.display = 'inline';
        }
    }
}
