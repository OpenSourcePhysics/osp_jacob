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

package jacob.system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.net.URL;
import java.applet.Applet;

import jacob.main.*;
import jacob.property.*;
import jacob.data.*;

public class SystemMgr
{

    private String propertyData = "data/property.data";

    private String lpropertyData = "data/lang.data";
   
    private String uiData = "data/ui.data"; 

    private Applet applet;

    private PropertyMgr propertyMgr;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SystemMgr( Properties properties )
    {
        this( properties, null );
    } 

    public SystemMgr( Properties properties, Applet applet ) 
    {
        this.applet = applet;
        propertyMgr = new PropertyMgr( this, properties );
        loadProperties();
        loadLProperties();
    }

//------------------------------------------------------------------------------
//    UI Methods
//------------------------------------------------------------------------------

    public DataElement getUIData()
    {
        try 
        { 
            InputStream is = getInputStream( uiData );

            if ( is == null ) return null;

            DataElement data = DataMgr.readXMLElement( is );
            is.close(); 

            if ( !data.getName().equals( "jacob" ) )
            {
                error( "can't find 'jacob' field in ui data" );
                return null; 
            }
            return data.getElement( "ui" );
        }
        catch ( DataParseException ex ) 
        {
            error( "ui data parse error: " + ex.getMessage() );
            return null;
        }
        catch ( IOException ex ) 
        {
            error( "can't read ui data: " + ex.getMessage() );
            return null;
        }
    }

//------------------------------------------------------------------------------
//    Properties Methods
//------------------------------------------------------------------------------

    public PropertyMgr getPropertyMgr()
    {
        return propertyMgr;
    }

    public void loadProperties()
    {
        DataElement data, properties;

        try 
        { 
            InputStream is = getInputStream( propertyData );

            if ( is == null ) return;

            data = DataMgr.readXMLElement( is );
            is.close(); 

            if ( !data.getName().equals( "jacob" ) ) 
            {
                error( "can't find 'jacob' field in property data" );
                return; 
            }
            propertyMgr.readData( data.getElement( "properties" ) );
        }
        catch ( DataParseException ex ) 
        {
            error( "property data parse error: " + ex.getMessage() ); 
            return;
        }
        catch ( IOException ex ) 
        {
            error( "can't read property data: " + ex.getMessage() );
            return;
        }
    }

    public void saveProperties()
    {
        OutputStream os = getOutputStream( propertyData );

        if ( os == null ) return;

        DataElement jacob = DataMgr.createXMLElement( "jacob" );

        propertyMgr.writeData( jacob );

        try 
        {
            DataMgr.writeElement( os, jacob ); 
            os.close(); 
        } 
        catch ( IOException ex ) 
        {
            error( "can't save property data: " + ex.getMessage() );
        }
    }

    public void loadLProperties()
    {
        propertyMgr.loadLocale( getInputStream( lpropertyData ) );
    }

//------------------------------------------------------------------------------
//    Error Notification Methods
//------------------------------------------------------------------------------

    public static void error( String msg ) 
    {
        System.out.println( "Error: " + msg );
    }

//------------------------------------------------------------------------------
//    I/O Methods 
//------------------------------------------------------------------------------

    public InputStream getInputStream( String file )
    {
        try
        {
            if ( isApplet() )
                return new URL( applet.getDocumentBase(), file ).openStream();
            else
                return new FileInputStream( file );
        } 
        catch ( IOException ex )
        {
            error( "can't open file: " + ex.getMessage() );
        }

        return null;
    }

    public OutputStream getOutputStream( String file )
    {
        try
        {
            if ( isApplet() )
//REMIND: what should we do in this case?
                return null;    
            else
                return new FileOutputStream( file );
        } 
        catch ( IOException ex )
        {
            error( "can't open file: " + ex.getMessage() );
        }

        return null;
    }

//------------------------------------------------------------------------------
//    Applet Methods
//------------------------------------------------------------------------------

    public boolean isApplet()
    {
        return (applet == null) ? false : true;
    }

//------------------------------------------------------------------------------
//    Exit Methods
//------------------------------------------------------------------------------

    public void exit( int code )
    {
        if ( !isApplet() )
            System.exit( code ); 
//REMIND: what about exit in Applet mode
    }
}
