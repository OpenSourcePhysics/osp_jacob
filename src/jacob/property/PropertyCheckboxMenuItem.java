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

import java.awt.CheckboxMenuItem;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import jacob.system.*;

public class PropertyCheckboxMenuItem extends CheckboxMenuItem
    implements PropertyObserver, ItemListener
{
    private PropertyMgr propertyMgr;

    private PropertyBoolean property;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public PropertyCheckboxMenuItem( PropertyMgr propertyMgr, String property )
    {
        this( propertyMgr, property, "" );
    }

    public PropertyCheckboxMenuItem( PropertyMgr propertyMgr, String property, 
                                     String label )
    {
        super( label, propertyMgr.getBoolean( property ) );

        this.propertyMgr = propertyMgr;

        if ( !propertyMgr.isDefined( property ) )
        {
            SystemMgr.error( "checkbox item property not defined: " + 
                             property );
            return;
        }

        if ( !(propertyMgr.getProperty( property ) instanceof PropertyBoolean) )
        {
            SystemMgr.error( "boolean type property required for checkbox " +
                             "item: " + property );
            return;
        }
 
        this.property = (PropertyBoolean)propertyMgr.getProperty( property );

        propertyMgr.attachObserver( this );
        addItemListener( this );
    }

//------------------------------------------------------------------------------
//    Update Property Methods
//------------------------------------------------------------------------------

    public void updateProperty( UpdatePropertyEvent pe )
    {
        if ( pe.hasChanged( property.getName() ) )
        {
            setState( property.getValue() );
        }
    }

//------------------------------------------------------------------------------
//    Item Listener Methods
//------------------------------------------------------------------------------

    public void itemStateChanged( ItemEvent e )
    {
        property.setValue( getState() );
        propertyMgr.notifyObservers( this );
    }
}
