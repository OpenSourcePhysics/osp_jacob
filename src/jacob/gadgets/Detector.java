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

import java.util.Vector;
import java.awt.Color;
import java.awt.Panel;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jacob.main.*;
import jacob.scene.*;
import jacob.scene.editor.*;
import jacob.geometry.*;
import jacob.lib.*;

public abstract class Detector extends Gadget
    implements ActionListener
{
    private Indicator indicator;

    private SimpleButton powerSwitch;

    private boolean active = false;
   
//------------------------------------------------------------------------------
//    Applet Methods
//------------------------------------------------------------------------------

    public void init()
    {
        GridBagLayout      gridbag  = new GridBagLayout();
        GridBagConstraints gridbagC = new GridBagConstraints();
 
        setLayout( gridbag );

        gridbagC.insets     = new Insets( 2, 2, 2, 2 );
        gridbagC.fill       = GridBagConstraints.NONE;
        gridbagC.gridwidth  = 1;
        gridbagC.gridheight = 1;
        gridbagC.anchor     = GridBagConstraints.NORTH;

        powerSwitch = new SimpleButton();
        powerSwitch.setBackground( Color.gray );
        powerSwitch.addActionListener( this );
        powerSwitch.setActionCommand( "ON" );
       
        gridbag.setConstraints( powerSwitch, gridbagC );
        add( powerSwitch );

        gridbagC.insets     = new Insets( 0, 0, 0, 0 );
        gridbagC.fill       = GridBagConstraints.BOTH;
        gridbagC.gridwidth  = GridBagConstraints.REMAINDER;
        gridbagC.anchor     = GridBagConstraints.CENTER;

        indicator = new Indicator();

        gridbag.setConstraints( indicator, gridbagC );
        add( indicator );
    }

    public void start()
    {
    }

    public void stop() 
    {
        sceneUnListen();
    }

//------------------------------------------------------------------------------
//    Indicator Methods
//------------------------------------------------------------------------------

    public void setIndicatorScale( double scale )
    {
        indicator.setScale( scale );
    }

    public double getIndicatorScale()
    {
        return indicator.getScale();
    }

    public void setIndicatorValue( double value )
    {
        indicator.setValue( value );
    }

    public double getIndicatorValue()
    {
        return indicator.getValue();
    }

    public void setIndicatorLabel( String text )
    {
        indicator.setLabel( text );
    }

//------------------------------------------------------------------------------
//    Detector Methods
//------------------------------------------------------------------------------

    public boolean isActive()
    {
        return active;
    }

    protected void startDetector(double x, double y) {}

    protected void stopDetector(double x, double y) {}

    protected abstract void updateIndicator( double x, double y );

//------------------------------------------------------------------------------
//    ActionListener Methods
//------------------------------------------------------------------------------

    public void actionPerformed( ActionEvent e )
    {
        Object source = e.getSource();
        String command = e.getActionCommand();

        if ( command.equals( "ON" ) )
        {
                sceneListen();
        } 
        else if ( command.equals( "OFF" ) ) 
        {
                sceneUnListen();
        }
    }

//------------------------------------------------------------------------------
//    SceneListener Methods
//------------------------------------------------------------------------------

    public void added() 
    {
        powerSwitch.setBackground( Color.green );
        powerSwitch.setActionCommand( "OFF" );
        indicator.setActive( true );
    }

    public void removed()
    {
        powerSwitch.setBackground( Color.gray );
        powerSwitch.setActionCommand( "ON" );
        indicator.setActive( false );
    } 

    public void mousePressed( SceneMouseEvent e )
    {
        active = true;
        Point2d mloc = e.getLocation();
        startDetector(mloc.x, mloc.y);
        updateIndicator( mloc.x, mloc.y );        
        getScene().redraw();
    }

    public void mouseReleased( SceneMouseEvent e )
    {
        active = false;
        indicator.setLabel( "" );
        Point2d mloc = e.getLocation();
        stopDetector(mloc.x, mloc.y);
        getScene().redraw();
    }

    public void mouseDragged( SceneMouseEvent e ) 
    {
        Point2d mloc = e.getLocation();
        updateIndicator( mloc.x, mloc.y );        
        getScene().redraw();
    }
}
