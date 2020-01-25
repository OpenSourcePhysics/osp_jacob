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

package jacob.lib;

import java.awt.Color;
import java.awt.Menu;
import java.awt.PopupMenu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.CheckboxMenuItem;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import org.opensourcephysics.js.JSUtil;

import jacob.data.*;
import jacob.system.*;
import jacob.property.*;

public final class UIBuilder
{

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    private UIBuilder() {}

//------------------------------------------------------------------------------
//    MenuBar Methods
//------------------------------------------------------------------------------

    public static MenuBar buildMenuBar( SystemMgr systemMgr,
                                        DataElement data,
                                        ActionListener listener )
    {
        MenuBar mbar;
        Enumeration menum = data.getElements();

        mbar = new MenuBar();

        while ( menum.hasMoreElements() )
        {
            DataElement mData = (DataElement)menum.nextElement();

            if ( mData.getName().equals( "menu" ) )
            {
                Menu menu = buildMenu( systemMgr, mData, listener );
                if ( menu != null ) mbar.add( menu );
            }
        }
        return mbar;
    }

//------------------------------------------------------------------------------
//    Menu Methods
//------------------------------------------------------------------------------

    public static Menu buildMenu( SystemMgr systemMgr,
                                  DataElement data,
                                  ActionListener listener )
    {
        try
        {
            Menu menu;
            String label = data.getStringAttribute( "label" );
            menu = new Menu( systemMgr.getPropertyMgr().getLString( label ) );
            buildMenuItems( menu, systemMgr, data, listener );
            return menu;
        }
        catch ( DataParseException ex )
        {
            SystemMgr.error( "can't parse menu data: " + ex.getMessage() );
        }
        return null;
    }

    public static PopupMenu buildPopupMenu( SystemMgr systemMgr,
                                            DataElement data,
                                            ActionListener listener )
    {
        PopupMenu menu;
        menu = new PopupMenu();
        buildMenuItems( menu, systemMgr, data, listener );
        return menu;
    }

    public static void buildMenuItems( Menu menu,
                                       SystemMgr systemMgr,
                                       DataElement data,
                                       ActionListener listener )
    {
        Enumeration menum = data.getElements();

        while ( menum.hasMoreElements() )
        {
            DataElement iData = (DataElement)menum.nextElement();

            if ( iData.getName().equals( "menu" ) )
            {
                Menu mitem = buildMenu( systemMgr, iData, listener );
                if ( mitem != null ) menu.add( mitem );
            }
            else if ( iData.getName().equals( "menuitem" ) )
            {
                MenuItem item =  buildMenuItem( systemMgr, iData,
                                                listener );
                if ( item != null ) menu.add( item );
            }
            else if ( iData.getName().equals( "pcheckbox" ) )
            {
                CheckboxMenuItem item = buildPCheckboxMenuItem( systemMgr,
                                                                iData );
                if ( item != null ) menu.add( item );
            }
            else if ( iData.getName().equals( "separator" ) )
            {
                menu.addSeparator();
            }
        }
    }

//------------------------------------------------------------------------------
//    MenuItem Methods
//------------------------------------------------------------------------------

    public static MenuItem buildMenuItem( SystemMgr systemMgr,
                                          DataElement data,
                                          ActionListener listener )
    {
        try
        {
            MenuItem item;
            String label  = data.getStringAttribute( "label" );
            String action = data.getStringAttribute( "action" );
            if(JSUtil.isJS && action.equals("FileOpenScript")) return null;  //Do not run JS in JS simulation
            label = systemMgr.getPropertyMgr().getLString( label );
            item = new MenuItem( label );
            item.setActionCommand( action );
            item.addActionListener( listener );
            return item;
        }
        catch ( DataParseException ex )
        {
            SystemMgr.error( "can't parse menu item data: " + ex.getMessage() );
        }
        return null;
    }

//------------------------------------------------------------------------------
//    PropertyCheckboxMenuItem Methods
//------------------------------------------------------------------------------

    public static CheckboxMenuItem buildPCheckboxMenuItem( SystemMgr systemMgr,
                                                           DataElement data )
    {
        try
        {
            CheckboxMenuItem item;
            String label    = data.getStringAttribute( "label" );
            String property = data.getStringAttribute( "property" );

            label = systemMgr.getPropertyMgr().getLString( label );
            item = new PropertyCheckboxMenuItem( systemMgr.getPropertyMgr(),
                                                 property, label );
            return item;
        }
        catch ( DataParseException ex )
        {
            SystemMgr.error( "can't parse property checkbox menu item data: " +
                ex.getMessage() );
        }
        return null;
    }

//------------------------------------------------------------------------------
//    PropertySlider Methods
//------------------------------------------------------------------------------

    public static PropertySlider buildPropertySlider( SystemMgr systemMgr,
                                                      DataElement data )
    {
        try
        {
            PropertySlider slider;

            String property = data.getStringAttribute( "property" );
            double minValue = data.getDoubleAttribute( "minvalue" );
            double maxValue = data.getDoubleAttribute( "maxvalue" );
// SZ       Color  fgcolor  = data.getColorAttribute( "fgcolor", Color.blue );
            Color  fgcolor  = data.getColorAttribute( "fgcolor", new Color( 0x93, 0x94, 0x99 ));// VV 24.3.02
            slider = new PropertySlider( systemMgr.getPropertyMgr(),
                                         property, minValue, maxValue,
                                         fgcolor );
            return slider;
        }
        catch ( DataParseException ex )
        {
            SystemMgr.error( "can't parse property slider data: " +
                             ex.getMessage() );
        }
        return null;
    }
}
