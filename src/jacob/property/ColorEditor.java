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

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Button;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import jacob.system.SystemMgr;
import jacob.lib.AWTEventAdapter;

//REMIND: maybe we should make this property independent

public class ColorEditor extends Frame 
{
    private ColorChooser colorChooser;

    private PropertyColor pcolor;

    private SystemMgr systemMgr;

    private PropertyMgr propertyMgr;
  
//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public ColorEditor( SystemMgr systemMgr,
                        PropertyColor pcolor )
    {
        super( systemMgr.getPropertyMgr().getLString( "ColorEditorTitle" ) ); 

        this.systemMgr   = systemMgr;
        this.propertyMgr = systemMgr.getPropertyMgr();
        this.pcolor      = pcolor;

        Listener listener = new Listener();

        addWindowListener( listener );

        initComponents( listener, pcolor );

        pack();
        setVisible( true );
    }

//------------------------------------------------------------------------------
//    Init Components Methods 
//------------------------------------------------------------------------------

    private void initComponents( Listener listener, PropertyColor pcolor )
    {
        GridBagLayout      gridbag  = new GridBagLayout();
        GridBagConstraints gridbagC = new GridBagConstraints();

        setLayout( gridbag );

        colorChooser = new ColorChooser( pcolor.getValue().getRed(),
                                         pcolor.getValue().getGreen(),
                                         pcolor.getValue().getBlue() );

        gridbagC.insets     = new Insets( 10, 10, 10, 10 );
        gridbagC.weightx    = 1.0;
        gridbagC.weighty    = 1.0;
        gridbagC.fill       = GridBagConstraints.BOTH;
        gridbagC.gridwidth  = GridBagConstraints.REMAINDER;
        gridbagC.gridheight = 1;

        gridbag.setConstraints( colorChooser, gridbagC );
        add( colorChooser );

        gridbagC.fill      = GridBagConstraints.NONE;
        gridbagC.gridwidth = 1;

        Button button;

        button = new Button( propertyMgr.getLString( "ApplyButton" ) );
        button.addActionListener( listener );
        button.setActionCommand( "CE_APPLY_BUTTON" );

        gridbag.setConstraints( button, gridbagC );
        add( button );

        button = new Button( propertyMgr.getLString( "DoneButton" ) ); 
        button.addActionListener( listener );
        button.setActionCommand( "CE_DONE_BUTTON" );

        gridbag.setConstraints( button, gridbagC );
        add( button );

        button = new Button( propertyMgr.getLString( "CancelButton" ) );
        button.addActionListener( listener );
        button.setActionCommand( "CE_CANCEL_BUTTON" );

        gridbagC.gridwidth = GridBagConstraints.REMAINDER;

        gridbag.setConstraints( button, gridbagC );
        add( button );
    }

//------------------------------------------------------------------------------
//    Event Listener Class
//------------------------------------------------------------------------------

    private class Listener extends AWTEventAdapter
    {
        ColorEditor parent = ColorEditor.this;

        public Listener() {} 


        public void actionPerformed( ActionEvent e )
        {
            Object target = e.getSource();
            String action = e.getActionCommand();

            if ( action.equals( "CE_APPLY_BUTTON" ) )
            {
                pcolor.setValue( colorChooser.getColor() );
                propertyMgr.notifyObservers( parent );
            }
            else if ( action.equals( "CE_DONE_BUTTON" ) )
            {
                pcolor.setValue( colorChooser.getColor() );
                propertyMgr.notifyObservers( parent );
                parent.dispose();
            }
            else if ( action.equals( "CE_CANCEL_BUTTON" ) )
            {
                parent.dispose();
            }
        }

        public void windowClosing( WindowEvent e )
        {
            parent.dispose();
        }
    }
}
