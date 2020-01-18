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

package jacob.gadgets;

import java.awt.Frame;
import java.awt.Container;
import java.awt.Panel;
import java.awt.MenuBar;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import jacob.main.*;
import jacob.lib.*;
import jacob.scene.*;
import jacob.property.*;
import jacob.system.*;


//FIXME

public class GadgetFrame extends Frame
{

    private Listener listener;

    private MainPanel mainPanel;

    private PropertyMgr propertyMgr;

    private SystemMgr systemMgr;

    private JInterface jinterface;

    private ScrollPane scrollPane;

    private Panel gadgetPanel;

    private GridBagLayout gpGridbag;

    private GridBagConstraints gpGridbagC;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public GadgetFrame( SystemMgr systemMgr, MainPanel mainPanel )
    {
        super( systemMgr.getPropertyMgr().getLString( "GadgetsFrameTitle" ) );

        this.systemMgr   = systemMgr;
        this.propertyMgr = systemMgr.getPropertyMgr();
        this.mainPanel   = mainPanel;
        this.jinterface  = new JInterface( systemMgr, mainPanel );

        listener = new Listener();
        addWindowListener( listener );

        setLayout( new BorderLayout() );

        initMenuBar();

        initScrollPane();

        setSize( 160, 480 );
        setVisible( true );
    }

//------------------------------------------------------------------------------
//    UI Methods
//------------------------------------------------------------------------------

    private void initMenuBar()
    {
        MenuBar mbar = new MenuBar();
        Menu    madd = new Menu( propertyMgr.getLString( "AddGadget" ) );

//FIXME
        for ( int i = 0; i < GadgetFactory.GADGETS.length; i++ )
        {
            MenuItem gitem = new MenuItem(
                propertyMgr.getLString( "Add_" + GadgetFactory.GADGETS[i][0] ) );
            gitem.setActionCommand( GadgetFactory.GADGETS[i][0] );
            gitem.addActionListener( listener );
            madd.add( gitem );
        }
        mbar.add( madd );
        setMenuBar( mbar );
    }

    private void initScrollPane()
    {
        gpGridbag  = new GridBagLayout();
        gpGridbagC = new GridBagConstraints();
        gpGridbagC.insets    = new Insets( 0, 0, 0, 0 );
        gpGridbagC.fill      = GridBagConstraints.BOTH;
        gpGridbagC.gridwidth = GridBagConstraints.REMAINDER;

        scrollPane = new ScrollPane();
        gadgetPanel = new Panel();
        gadgetPanel.setLayout( gpGridbag );
        scrollPane.add( gadgetPanel );
        add( "Center", scrollPane );
    }

    private void addGadgetBox( GadgetBox gbox )
    {
        gpGridbag.setConstraints( gbox, gpGridbagC );
        gadgetPanel.add( gbox );
        gadgetPanel.validate();
        scrollPane.validate();
    }

    private void removeGadgetBox( GadgetBox gbox )
    {
        gadgetPanel.remove( gbox );
        gadgetPanel.validate();
        scrollPane.validate();
    }

//------------------------------------------------------------------------------
//    Event Listener
//------------------------------------------------------------------------------

    private class Listener extends AWTEventAdapter
    {
        public Listener() {}

        public void actionPerformed( ActionEvent e )
        {
            String action = e.getActionCommand();

            try
            {
                Gadget g = GadgetFactory.createGadget( action );
                addGadgetBox( new GadgetBox( g ) );
            }
            catch ( UnknownGadgetException ex )
            {
                throw new InternalException( "unknown gadget type: " + action );
            }
        }

        public void windowClosing( WindowEvent e )
        {
            propertyMgr.getProperties().showGadgets.setValue( false );
            propertyMgr.notifyObservers( this );
        }
    }

//------------------------------------------------------------------------------
//    Gadget Box
//------------------------------------------------------------------------------

    private class GadgetBox extends Panel
        implements MouseListener
    {
        private final int BORDER_SIZE = 2;

        private final int BUTTON_SIZE = 10;

        private Gadget gadget;


        public GadgetBox( Gadget gadget )
        {
            this.gadget = gadget;
            add( gadget );
            gadget.init();
            gadget.start();
            gadget.connect( jinterface );

            addMouseListener( this );
        }

        public void paint( Graphics g )
        {
            int w = getSize().width;
            int h = getSize().height;

            int bx = w - BORDER_SIZE * 2 - BUTTON_SIZE;
            int by = BORDER_SIZE * 2;
            int bw = BUTTON_SIZE;
            int bh = BUTTON_SIZE;

            g.setColor( getBackground() );
            g.fill3DRect( 0, 0, w, h, true );
   			g.setColor( new Color( 0x8e, 0x8e, 0xff ) );// SZ
            g.fill3DRect( 1, 1, w - 2, getBarSize() - 2, true );

            Font f = getFont();
            if( f != null )
            {
                FontMetrics fm = getFontMetrics( f );
                g.setColor( Color.black );
                g.drawString( propertyMgr.getLString( "Title_" +
                              gadget.getType() ), BORDER_SIZE + 2,
                              BORDER_SIZE + 2 + fm.getHeight() -
                              fm.getMaxDescent() );
            }

            g.setColor( getBackground() );
            g.fill3DRect( bx, by, bw, bh, true );
            g.setColor( Color.black );
            g.drawLine( bx + 2, by + 2, bx + bw - 3, by + bh - 3 );
            g.drawLine( bx + 2, by + bh - 3, bx + bw - 3, by + 2 );
        }

        public void mouseClicked( MouseEvent e ) {}

        public void mousePressed( MouseEvent e )
        {
            int mx = e.getX();
            int my = e.getY();
            int bx = getSize().width -  BORDER_SIZE * 2 - BUTTON_SIZE;
            int by = BORDER_SIZE * 2;

            if ( mx > bx && mx < (bx + BUTTON_SIZE) &&
                 my > by && my < (by + BUTTON_SIZE) )
            {
                gadget.stop();
                remove( gadget );
                removeGadgetBox( this );
            }
        }

        public void mouseReleased( MouseEvent e ) {}

        public void mouseEntered( MouseEvent e ) {}

        public void mouseExited( MouseEvent e ) {}


        public void doLayout()
        {
            gadget.setBounds( BORDER_SIZE, BORDER_SIZE + getBarSize(),
                              getSize().width - 2 * BORDER_SIZE,
                              getSize().height - 2 * BORDER_SIZE -
                              getBarSize() );
        }

        public Dimension getMaximumSize()
        {
            return getPreferredSize();
        }

        public Dimension getMinimumSize()
        {
            return getPreferredSize();
        }

        public Dimension getPreferredSize()
        {
            synchronized( getTreeLock() )
            {
                if ( isValid() )
                    return getSize();
                else
                    return getBoxSize( gadget.getPreferredSize() );
            }
        }

        private int getBarSize()
        {
            Font f = getFont();
            if ( f != null )
            {
                FontMetrics fm = getFontMetrics( f );
                return ( BORDER_SIZE * 2 + fm.getHeight() + 4 );
            }
            else
            {
                return 18;
            }
        }

        private Dimension getBoxSize( Dimension dim )
        {
            if ( dim.width < Short.MAX_VALUE )
                dim.width += 2 * BORDER_SIZE;
            if ( dim.height < Short.MAX_VALUE )
                dim.height += 2 * BORDER_SIZE + getBarSize();
            return dim;
        }
    }
}
