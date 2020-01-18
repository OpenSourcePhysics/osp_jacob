//==============================================================================//
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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;

public class SimpleButton extends Component 
{
    private String label; 

    private String actionCommand;

    private int borderWidth = 1;

    private int horTextOffset = 4;

    private int verTextOffset = 4;

    private boolean pressed = false; 

    private ActionListener actionListener;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SimpleButton() 
    {
        this("");
    }

    public SimpleButton( String label ) 
    {
        this.label = label;
        enableEvents( AWTEvent.MOUSE_EVENT_MASK );
    }

//------------------------------------------------------------------------------
//    Attributes Methods
//------------------------------------------------------------------------------

    public String getLabel() 
    {
        return label;
    }
  
    public void setLabel( String label ) 
    {
        this.label = label;
        invalidate();
        repaint();
    }

    public String getActionCommand()
    {
        return actionCommand;
    }

    public void setActionCommand( String command )
    {
        actionCommand = command;
    }

    public int getBorderWitdh() 
    {
        return borderWidth;
    }

    public void setBorderWitdh( int width ) 
    {
        borderWidth = width;
//        invalidate();
        repaint();
    }

    public int getHorTextOffset() 
    {
        return horTextOffset;
    }

    public void setHorTextOffset( int offset ) 
    {
        horTextOffset = offset;
        invalidate();
        repaint();
    }

    public int getVerTextOffset() 
    {
        return verTextOffset;
    }

    public void setVerTextOffset( int offset ) 
    {
        verTextOffset = offset;
        invalidate();
        repaint();
    }

    public void setBackground( Color back )
    {
        super.setBackground( back );
        repaint();
    }

    public void setForeground( Color fore )
    {
        super.setForeground( fore );
        repaint();
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paint( Graphics g ) 
    {
        int w = getSize().width  - 1;
        int h = getSize().height - 1;

        Color back = getBackground();
        Color top, bottom;
      
        if ( pressed ) 
        {
	    top    = back.darker();
	    bottom = back.brighter();
        } 
        else 
        {
	    bottom = back.darker();
	    top    = back.brighter();
        }

        g.setColor( back );
        g.fillRect( 0, 0, w, h ); 
     
        for ( int i = 0; i < borderWidth; i++ )
        {
            g.setColor( bottom );
            g.drawLine( i, h - i, w - i, h - i );
            g.drawLine( w - i, h - i, w - i, i );    
            g.setColor( top ); 
            g.drawLine( i, i, w - i , i );    
            g.drawLine( i, i, i, h - i ); 
        } 

        Font f = getFont();
        if( f != null ) 
        {
	    FontMetrics fm = getFontMetrics( f );
	    g.setColor( getForeground() );
	    g.drawString( label,
		       w/2 - fm.stringWidth( label ) / 2,
		       h/2 + fm.getHeight() / 2 - fm.getMaxDescent() );
        }
    }

//------------------------------------------------------------------------------
//    Size Methods
//------------------------------------------------------------------------------
 
    public Dimension getPreferredSize() 
    {
        Font f = getFont();
        if( f != null && label != null && label != "" )
        {
	    FontMetrics fm = getFontMetrics( f );
	    return new Dimension( fm.stringWidth( label ) + horTextOffset * 2,
			          fm.getHeight() + verTextOffset * 2 );
        } 
        else 
        {
	    return new Dimension( horTextOffset * 2, verTextOffset * 2 );
        }
    }
 
    public Dimension getMinimumSize() 
    {
        return getPreferredSize();
    }

//------------------------------------------------------------------------------
//    Event Methods
//------------------------------------------------------------------------------

    public void processMouseEvent( MouseEvent e ) 
    {
        switch( e.getID() ) 
        {
        case MouseEvent.MOUSE_PRESSED:
            pressed = true;
	    repaint();
            break;
        case MouseEvent.MOUSE_RELEASED:
            if( actionListener != null ) 
            {
                String command;
                command = (actionCommand != null) ? actionCommand : label;
                actionListener.actionPerformed( new ActionEvent(
                   this, ActionEvent.ACTION_PERFORMED, command ) );
            }
            if( pressed ) 
            {
                pressed = false;
		repaint();
            }
            break;
        case MouseEvent.MOUSE_ENTERED:
            break;
        case MouseEvent.MOUSE_EXITED:
            if( pressed ) 
            {
                pressed = false;
		repaint();
            }
            break;
       }
       super.processMouseEvent( e );
   }

//------------------------------------------------------------------------------
//    Action Listener Methods
//------------------------------------------------------------------------------

    public void addActionListener( ActionListener listener ) 
    {
        actionListener = AWTEventMulticaster.add( actionListener, listener );
        enableEvents( AWTEvent.MOUSE_EVENT_MASK );
    }
 
    public void removeActionListener( ActionListener listener ) 
    {
        actionListener = AWTEventMulticaster.remove( actionListener, listener );
    }
}
