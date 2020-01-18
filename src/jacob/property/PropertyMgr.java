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

package jacob.property;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.awt.Color;
import java.lang.reflect.Field;

import jacob.main.*;
import jacob.system.*;
import jacob.data.*;

public class PropertyMgr 
    implements Saveable
{
    private Properties properties;
 
    private Hashtable propertyMap = new Hashtable();

//REMIND: this is here mainly because we like properties
//        displayed (in PropertyEditor) in some meaningfull order
    private Vector propertyVector = new Vector();

    private PropertyResourceBundle localeProperties;

    private Vector propertyObservers = new Vector();

    private Vector changedProperties = new Vector();

    private SystemMgr systemMgr;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public PropertyMgr( SystemMgr systemMgr, Properties properties ) 
    {
        this.systemMgr  = systemMgr;
        this.properties = properties;

        init();
    }

//------------------------------------------------------------------------------
//    Init Methods 
//------------------------------------------------------------------------------

    private void init()
    {
        try
        {
            Field fields[] = properties.getClass().getFields();

            for ( int i = 0; i < fields.length; i++ )
            {
                Object obj = fields[i].get( properties );

                if ( obj instanceof Property )
                {
                    Property p = (Property)obj;  
 
                    propertyMap.put( p.getName(), p );
                    propertyVector.addElement( p );
                    p.setMgr( this );
                }
            }
        }
        catch ( Exception ex )
        {
            throw new InternalException( "can't init properties: " +
                ex.toString() );
        }
    }

//------------------------------------------------------------------------------
//    Get Property Methods 
//------------------------------------------------------------------------------

    public boolean isDefined( String name )
    {
        Property p = (Property) propertyMap.get( name );

        if ( p == null )
            return false;
        else
            return true;
    }

    public Property getProperty( String name )
    {
        Property p = (Property) propertyMap.get( name );

        if ( p == null )
        {
            throw new InternalException( "unknown property: " + name );
        }
        return p;
    }

    public int getInteger( String name )
    {
        return ((PropertyInteger)getProperty( name )).getValue();
    }

    public double getDouble( String name )
    {
        return ((PropertyDouble)getProperty( name )).getValue();
    }

    public boolean getBoolean( String name )
    {
        return ((PropertyBoolean)getProperty( name )).getValue();
    }

    public String getString( String name )
    {
        return ((PropertyString)getProperty( name )).getValue();
    }

    public Color getColor( String name )
    {
        return ((PropertyColor)getProperty( name )).getValue();
    }

    public String getLString( String name )
    {
        try
        {
            return localeProperties.getString( name );
        }
        catch ( Exception ex )
        {
            return name;
//            throw new InternalError( "can't get locale string: " + 
//                ex.getMessage() ); 
        }
    }

    public Properties getProperties()
    {
        return properties;
    }

    public Enumeration getPropertiesEnum()
    {
        return propertyVector.elements();
    }

//------------------------------------------------------------------------------
//    Set Property Methods
//------------------------------------------------------------------------------

    public boolean setProperty( String name, String svalue ) 
    {
        return getProperty( name ).setValue( svalue );
    }

    public boolean setInteger( String name, int value )
    {
        return ((PropertyInteger)getProperty( name )).setValue( value );
    }

    public boolean setDouble( String name, double value )
    {
        return ((PropertyDouble)getProperty( name )).setValue( value );
    }

    public boolean setBoolean( String name, boolean value )
    {
        return ((PropertyBoolean)getProperty( name )).setValue( value );
    }

    public boolean setString( String name, String value ) 
    {
        return ((PropertyString)getProperty( name )).setValue( value );
    }

    public boolean setColor( String name, Color value )
    {
        return ((PropertyColor)getProperty( name )).setValue( value );
    }

//------------------------------------------------------------------------------
//    Observer Methods                            
//------------------------------------------------------------------------------

    public synchronized void attachObserver( PropertyObserver po )
    {
        if ( propertyObservers.contains( po ) )
        { 
            throw new InternalError( "duplicated property observer: " +
                po.getClass().getName() );
        }
        propertyObservers.addElement( po );
    }

    public synchronized void detachObserver( PropertyObserver po )
    {
        if ( !propertyObservers.contains( po ) )
        {
            throw new InternalError( "property observer not registered: " +
                po.getClass().getName() );
        }
        propertyObservers.removeElement( po );
    }

    public synchronized void notifyObservers( Object source )
    {
        notifyObservers( source, false );
    }

    public synchronized void notifyObservers( Object source, boolean force )
    {
        if ( !force && changedProperties.size() == 0 )
            return;

        for ( int i = 0; i < propertyObservers.size(); i++ )
        {
            PropertyObserver po = 
                (PropertyObserver) propertyObservers.elementAt( i );

            if ( po != source )
                po.updateProperty(
                    new UpdatePropertyEvent( this, source, 
                                             changedProperties ) );
        }
        changedProperties.removeAllElements();
    }

    public synchronized void setChanged( String name )
    {
       if ( !changedProperties.contains( name ) )
           changedProperties.addElement( name );
    }

    public synchronized boolean hasChanged( String name )
    {
       if ( changedProperties.contains( name ) ) 
           return true;
       else
           return false;
    }

//------------------------------------------------------------------------------
//    Data R/W Methods 
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        Enumeration penum = data.getElements();

        while( penum.hasMoreElements() )
        {
            DataElement pData = (DataElement)penum.nextElement();

            if ( pData.getName().equals( "property" ) )
            {
                String pName  = pData.getStringAttribute( "name" );
                String pValue = pData.getStringAttribute( "value" );
                Property p = (Property)propertyMap.get( pName );

                if ( p == null )
                {
                    SystemMgr.error( "unknown property in data: " +
                        pName );
                }
                else
                {
                    if ( !p.setValue( pValue ) )
                    {
                        SystemMgr.error( "can't parse property data: " +
                            pName + " (" + pValue + ")" );
                    } 
                }
            }
        }
        notifyObservers( this );
    }

    public void writeData( DataElement data )
    {
        DataElement properties = data.newElement( "properties" );
        Enumeration penum = getPropertiesEnum();

        while( penum.hasMoreElements() )
        {
            Property p = (Property)penum.nextElement();
            DataElement pdata = properties.newElement( "property" );
            pdata.setAttribute( "name",  p.getName() );
            pdata.setAttribute( "value", p.toString() );
        } 
    }

    public void writeExpSpecData( DataElement data )
    {
        DataElement properties = data.newElement( "properties" );
        Enumeration penum = getPropertiesEnum();
 
        while( penum.hasMoreElements() )
        {
            Property p = (Property)penum.nextElement();

            if ( p.isFlagSet( Property.EXPSPEC ) )
            {
                DataElement pdata = properties.newElement( "property" );
                pdata.setAttribute( "name",  p.getName() );
                pdata.setAttribute( "value", p.toString() );
            }
        }   
    }

//REMIND: should we use DataElements for locale data?
    public void loadLocale( InputStream is )
    {
        try
        {
            localeProperties = new PropertyResourceBundle( is );
        }
        catch ( IOException ex )
        {
            SystemMgr.error( "can't load locale sensitive properties: " +
                ex.getMessage() );
        } 
    }
}
