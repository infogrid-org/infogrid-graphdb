function toggle_css_class( id, clazz ) {
    clazz = clazz.replace( /^\s+/, "" ).replace( /\s+$/, "" );
    var div = document.getElementById( id );
    var has = div.className.replace( /^\s+/, "" ).replace( /\s+$/, "" ).split( /\s+/ );

    for( var i=0 ; i<has.length ; ++i ) {
        if( clazz == has[i] ) {
            var tmp = "";
            var sep = "";
            for( var j=0 ; j<has.length ; ++j ) {
                if( j != i ) {
                    tmp += sep + has[j];
                    sep = " ";
                }
            }
            div.className = tmp;
            return;
        }
    }
    if( has.length > 0 ) {
        div.className += " " + clazz;
    } else {
        div.className = clazz;
    }
}
