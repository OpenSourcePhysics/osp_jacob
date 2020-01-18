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

package jacob.scene.modifiers;

import java.util.Vector;
import java.util.Enumeration;

import jacob.scene.*;
import jacob.system.*;
import jacob.property.*;
import jacob.geometry.*;
import jacob.data.*;

public class Modifiers
    implements Saveable
{
    private Vector modifiers = new Vector();

    private SceneComponent component;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Modifiers( SceneComponent component )
    {
        this.component = component;
    }

//------------------------------------------------------------------------------
//    Children Methods
//------------------------------------------------------------------------------

    public void addModifier( ComponentModifier m )
    {
        modifiers.addElement( m );
        m.setParent( this );
        m.added();
    }

    public void removeModifier( ComponentModifier m )
    {
        modifiers.removeElement( m );
        m.removed();
        m.setParent( null );
    }

    public int countModifiers()
    {
        return modifiers.size();
    }

    public ComponentModifier getModifierAt( int index )
    {
        return (ComponentModifier)modifiers.elementAt( index );
    }

    public ComponentModifier getModifier( Class clazz )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            if ( clazz.isInstance( modifiers.elementAt( i ) ) )
                return (ComponentModifier)modifiers.elementAt( i );
        }
        return null;
    }

    public ComponentModifier getModifier( String type )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier cm = (ComponentModifier)modifiers.elementAt(i);
            if ( cm.getType().equals( type ) )
                return cm;
        }
        return null;
    }

//------------------------------------------------------------------------------
//    Component Modification Methods
//------------------------------------------------------------------------------

    public void preIntegrationModify( long time )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.preIntegrationModify( time );
        }
    }

    public void postIntegrationModify( long time )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.postIntegrationModify( time );
        }
    }

    public void componentDeleated()
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.componentDeleated();
        }
    }

    public void componentMoved( Point2d p )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.componentMoved( p );
        }
    }

    public void componentScaled( double scale )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.componentScaled( scale );
        }
    }

    public void componentRotated( Point2d pivot, double angle )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.componentRotated( pivot, angle );
        }
    }

//------------------------------------------------------------------------------
//    Component Methods
//------------------------------------------------------------------------------

    public SceneComponent getComponent()
    {
        return component;
    }

    public Scene getScene()
    {
        return component.getScene();
    }

//------------------------------------------------------------------------------
//    Update Property Methods
//------------------------------------------------------------------------------

    public void updateProperty( UpdatePropertyEvent pe )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.updateProperty( pe );
        }
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.paintFg( g );
        }
    }

    public void paintBg( SceneGraphics g )
    {
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.paintBg( g );
        }
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------
    
    public void readData( DataElement data )
        throws DataParseException
    {
        Enumeration enumm = data.getElements();
 
        while( enumm.hasMoreElements() )
        {
            DataElement mdata = (DataElement)enumm.nextElement();

            ComponentModifier m = null;
            try
            {
                m = ModifierFactory.createModifier( mdata.getName() );

                addModifier( m );
                m.readData( mdata );
            }
            catch ( UnknownModifierException ex )
            {
                throw new DataParseException( "unknown modifier: " + 
                                              mdata.getName() );
            }
            catch ( DataParseException ex )
            {
                removeModifier( m );
                throw ex;
            }
        }
    }
 
    public void writeData( DataElement data )
    {
        if ( modifiers.size() == 0 )
            return;
 
        DataElement mdata = data.newElement( "modifiers" );
 
        for ( int i = 0; i < modifiers.size(); i++ )
        {
            ComponentModifier m = (ComponentModifier)modifiers.elementAt( i );
            m.writeData( mdata );
        }
    }  
}
