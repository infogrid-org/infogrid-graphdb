var overlayCanvas;

function overlay_show( overlayId, parameters ) {

    var overlay = document.getElementById( overlayId );

    if( !overlay ) {
        alert( "ERROR: cannot find HTML node with id '" + overlayId + "'" );
        return;
    }
    var inputElements = overlay.getElementsByTagName( "input" );
    for( var i=0 ; i<inputElements.length ; ++i ) {
        for( var key in parameters ) {
            if( inputElements[i].name == key ) {
                var value = parameters[key];

                inputElements[i].value = value;
            }
        }
    }
    if( !overlayCanvas ) {
        if (typeof document.createElementNS != 'undefined') {
            overlayCanvas = document.createElementNS('http://www.w3.org/1999/xhtml', "div" );
        } else {
            overlayCanvas = document.createElement("div");
        }
        overlayCanvas.id = "org-infogrid-jee-taglib-candy-OverlayTagCanvas";

        body = document.getElementsByTagName( 'body' )[0];
        body.appendChild( overlayCanvas );
    }

    overlay.style.display    = 'block';
    overlay.style.visibility = 'visible';

    overlayCanvas.style.display    = 'block';
    overlayCanvas.style.visibility = 'visible';
}

function overlay_hide( overlayId ) {
    var overlay = document.getElementById( overlayId );
    overlay.style.display    = 'none';
    overlay.style.visibility = 'hidden';
    overlayCanvas.style.display    = 'none';
    overlayCanvas.style.visibility = 'hidden';
}
