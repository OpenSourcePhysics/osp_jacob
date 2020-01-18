//==============================================================================
//
//  Copyright (C) 2000  Vojko Valencic <Vojko.Valencic@fe.uni-lj.si>
//                      Savin Zlobec <savin@torina.fe.uni-lj.si>
//
//  This program is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; either version 2
//  of the License, or (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
//==============================================================================                                    

package jacob.main;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.JavaScriptException;

import jacob.system.*;

public class JSScriptRun extends Thread
{
    private JInterface jint;

    private String file;

    private SystemMgr systemMgr; 

    public JSScriptRun( SystemMgr systemMgr, String file, JInterface jint )
    {
        this.systemMgr = systemMgr;
        this.file = file; 
        this.jint = jint;

        // start() //disabled Thread for JS;  
    }

    public void run()
    {
        InputStream is = systemMgr.getInputStream( file );

        if ( is == null ) return;

        try
        {
            Reader fileReader = new BufferedReader( 
                new InputStreamReader( is ) );

            Context cx = Context.enter();
            Scriptable scope = cx.initStandardObjects(null);

            scope.put( "jacob", scope, Context.toObject( jint, scope ) );
 
            cx.evaluateReader( scope, fileReader, file, 1, null );
            Context.exit();
        }
        catch ( FileNotFoundException ex )
        {
            SystemMgr.error( "script file not fount: " + file );
        }
        catch ( IOException ex )
        {  
            SystemMgr.error( "io error reading script file: " + file );
        }
        catch ( JavaScriptException ex )
        { 
            SystemMgr.error( "script error: " + ex.toString() );
        }

        try {is.close();} catch ( Exception ex ) {};
    }
}
