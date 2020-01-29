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

import java.applet.Applet;
import java.awt.Frame;
import java.awt.BorderLayout;

import jacob.system.*;

public class Main extends Applet
{
    public static final int JACOB_MAJOR_VERSION = 6;

    public static final int JACOB_MINOR_VERSION = 0;

    public static final int JACOB_MICRO_VERSION = 0;

    public static final String JACOB_EXTRA_VERSION = "";

//-----------------------------------------------------------------------------
//    Applet
//------------------------------------------------------------------------------

    public static MainPanel mainPanel = null;
    public static Frame frame = null;

    public SystemMgr systemMgr = null;

    public void init()
    {
        systemMgr = new SystemMgr( new Properties(), this );
        mainPanel = new MainPanel( systemMgr );
        setLayout( new BorderLayout() );
        add( "Center", mainPanel );
    }

//------------------------------------------------------------------------------
//    Interface Methods
//------------------------------------------------------------------------------

    public JInterface getInterface()
    {
        return new JInterface( systemMgr, mainPanel );
    }
    
    public static void setFrameTitle(String exp) {
    	String titleStr="Jacob "+exp;
        frame.setTitle(titleStr);
    }

//------------------------------------------------------------------------------
//    Application
//------------------------------------------------------------------------------

    public static void main( String[] args )
    {
        SystemMgr systemMgr = new SystemMgr( new Properties() );
        mainPanel = new MainPanel( systemMgr );

        frame = new Frame( "Jacob  JavaScript ver " +
                                 JACOB_MAJOR_VERSION + "." +
                                 JACOB_MINOR_VERSION + "." +
                                 JACOB_MICRO_VERSION +
                                 JACOB_EXTRA_VERSION );

        frame.setLayout( new BorderLayout() );
        frame.add( "Center", mainPanel );

        mainPanel.setFrame( frame );

        //FIXME: make size a property

        //frame.setSize( scene.getProperties().frameXSize.getValue(), scene.getProperties().frameYSize.getValue());
        frame.setSize(800,600);
        frame.setVisible( true );
    }
}
