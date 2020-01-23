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

import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import jacob.system.*;

public class PropertySlider extends Label
    implements PropertyObserver, MouseListener, MouseMotionListener
{
    private final static short BORDER_SIZE = 2;

    private PropertyMgr propertyMgr;

    private Property property;

    private double minValue;

    private double maxValue;

    private Color fgcolor;

    private double value = 0;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public PropertySlider( PropertyMgr propertyMgr, String pname,
                           double minValue, double maxValue, Color fgcolor )
    {
        this.propertyMgr = propertyMgr;

        if ( !propertyMgr.isDefined( pname ) )
        {
            SystemMgr.error( "slider property not defined: " + pname );
            return;
        }

        this.property = propertyMgr.getProperty( pname );
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.fgcolor  = fgcolor;

        if ( !(property instanceof PropertyInteger) &&
             !(property instanceof PropertyDouble) )
        {
            SystemMgr.error( "wrong property type for slider: " + pname );
            return;
        }

        if ( minValue >= maxValue )
        {
            SystemMgr.error( "wrong min and max values for slider: " + pname );
            minValue = maxValue = 0;
            return;
        }

        propertyMgr.attachObserver( this );

        addMouseListener( this );
        addMouseMotionListener( this );

        setValue();
    }

//------------------------------------------------------------------------------
//    Slider Value Methods
//------------------------------------------------------------------------------

    private double getPropertyValue()
    {
        if ( property instanceof PropertyInteger )
            return ((PropertyInteger)property).getValue();
        else
            return ((PropertyDouble)property).getValue();
    }

    private void setPropertyValue()
    {
        double v = minValue + value * ( maxValue - minValue );

        if ( property instanceof PropertyInteger )
            ((PropertyInteger)property).setValue( (int)v );
        else
            ((PropertyDouble)property).setValue( v );

        propertyMgr.notifyObservers( this );
    }


    private void setValue()
    {
        double pval = getPropertyValue();

        if ( pval <= minValue )
            value = 0;
        else if ( pval >= maxValue )
            value = 1;
        else
            value = ( pval - minValue ) / ( maxValue - minValue );
        repaint();
    }

    private void setValueFromMouse( MouseEvent e )
    {
        int x     = e.getX();
        int width = getSize().width;

        if ( x < BORDER_SIZE )
            value = 0;
        else if ( x > width - BORDER_SIZE )
            value = 1;
        else
            value = (x - BORDER_SIZE) / (double)(width - BORDER_SIZE * 2);

        setPropertyValue();
        repaint();
    }

//------------------------------------------------------------------------------
//    Update Property
//------------------------------------------------------------------------------

    public void updateProperty( UpdatePropertyEvent pe )
    {
        if ( pe.hasChanged( property.getName() ) )
        {
            setValue();
        }
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paint( Graphics g )
    {
    	
        int width  = getSize().width;
        int height = getSize().height;
        
        g.setColor( getBackground());
        g.fillRect(0, 0, width, height);

        g.setColor( getBackground());
        g.fillRect(0, 0, width, height);

        g.setColor( getBackground().darker() );
        for ( int i = 0; i < BORDER_SIZE; i++ )
        {
            g.drawLine( i, i, width - i, i );
            g.drawLine( i, i, i, height - i );
        }
        g.setColor( getBackground().brighter() );
        for ( int i = 0; i < BORDER_SIZE; i++ )
        {
            g.drawLine( i, height - i, width, height - i );
            g.drawLine( width - i, i, width - i, height - i );
        }

        if ( value > 0 )
        {
            g.setColor( fgcolor );
            g.fillRect( BORDER_SIZE, BORDER_SIZE,
                        (int)((width - BORDER_SIZE * 2) * value),
                        height - BORDER_SIZE * 2 );
        }
    }

//------------------------------------------------------------------------------
//    Mouse Listener Methods
//------------------------------------------------------------------------------

    public void mouseClicked( MouseEvent e )
    {
        setValueFromMouse( e );
    }

    public void mousePressed( MouseEvent e )
    {
        setValueFromMouse( e );
    }

    public void mouseReleased( MouseEvent e ) {}

    public void mouseEntered( MouseEvent e ) {}

    public void mouseExited( MouseEvent e ) {}

    public void mouseDragged( MouseEvent e )
    {
    	System.out.println("dragged " + e);
        setValueFromMouse( e );
    }

    public void mouseMoved( MouseEvent e ) {}

//------------------------------------------------------------------------------
//    Component Size Methods
//------------------------------------------------------------------------------

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    public Dimension getPreferredSize()
    {
        return new Dimension( 60, 20 );
    }

    public Dimension getMaximumSize()
    {
        return getPreferredSize();
    }
}
