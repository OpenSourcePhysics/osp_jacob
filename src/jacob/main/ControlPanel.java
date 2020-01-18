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

import java.awt.Panel;
import java.awt.Color;
import java.awt.Label;
import java.awt.ScrollPane;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.Enumeration;

import jacob.system.*;
import jacob.property.*;
import jacob.lib.*;
import jacob.data.*;

public class ControlPanel extends Panel 
{
    private ScrollPane scrollPane;

    private SystemMgr systemMgr;

    private PropertyMgr propertyMgr;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public ControlPanel( SystemMgr systemMgr, DataElement data )
    {
        this.systemMgr   = systemMgr;
        this.propertyMgr = systemMgr.getPropertyMgr();

        initPanel( data );

//        propertyMgr.attachObserver( this );
    }

//------------------------------------------------------------------------------
//    Init
//------------------------------------------------------------------------------

    private void initPanel( DataElement data )
    {
        setLayout( new BorderLayout() );

        scrollPane = new ScrollPane();

        add( "Center", scrollPane );

        scrollPane.add( initComponents( data ) );
    }

    private Panel initComponents( DataElement data )
    {
        Panel panel = new Panel();

        GridBagLayout      gridbag  = new GridBagLayout();
        GridBagConstraints gridbagC = new GridBagConstraints();

        panel.setLayout( gridbag );       
 
//        gridbagC.fill    = GridBagConstraints.VERTICAL;
        gridbagC.weightx = 1.0;
//        gridbagC.anchor  = GridBagConstraints.WEST;

        Enumeration enumm = data.getElements();

        while ( enumm.hasMoreElements() )
        {
            DataElement cdata = (DataElement)enumm.nextElement();

            if ( cdata.getName().equals( "pslider" ) )
            {
                addPropertySlider( panel, gridbag, gridbagC, cdata );
            }
            else
            {
                SystemMgr.error( "unknown control component: " + 
                                 cdata.getName() );
            }
        }

        return panel;
    }

    private void addPropertySlider( Panel panel,
                                    GridBagLayout gridbag, 
                                    GridBagConstraints gridbagC,
                                    DataElement data )
    {
        PropertySlider slider;

        slider = UIBuilder.buildPropertySlider( systemMgr, data );

        if ( slider != null )
        {
            gridbagC.insets = new Insets( 5, 0, 1, 0 ); 
            gridbagC.gridwidth = GridBagConstraints.REMAINDER; 

            gridbag.setConstraints( slider, gridbagC );
            panel.add( slider );

            gridbagC.insets = new Insets( 1, 0, 5, 0 ); 

//FIXME: localize this
            Label l = new Label( data.getStringAttribute( "label", "" ) );
            gridbag.setConstraints( l, gridbagC );
            panel.add( l );
        }
    }
}
