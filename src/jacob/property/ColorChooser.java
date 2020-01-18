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
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.awt.image.MemoryImageSource;


public class ColorChooser extends Canvas
{
    private static final int CURSOR_NONE = 0;
    private static final int CURSOR_1    = 1;
    private static final int CURSOR_2    = 2;
 
    private static final int PAL1_WIDTH  = 127;
    private static final int PAL1_HEIGHT = 127;
    private static final int PAL2_WIDTH  = 25;
    private static final int PAL2_HEIGHT = PAL1_HEIGHT;
    private static final int PAL_OFFSET  = 12;
    private static final int BORDER_SIZE = 2;

    private float hue, saturation, brightness;

    private Image pal1, pal2;

    private int cursor1x, cursor1y;
    private int cursor2x, cursor2y;

    private int cursorGrab = CURSOR_NONE;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public ColorChooser( int r, int g, int b ) 
    {
        float hsb[] = new float[3];

        Color.RGBtoHSB( r, g, b, hsb ); 

        hue        = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];

        buildPalette();
        calculateCursors();

        enableEvents( AWTEvent.MOUSE_EVENT_MASK |
                      AWTEvent.MOUSE_MOTION_EVENT_MASK );
    }

//------------------------------------------------------------------------------
//    Color Methods
//------------------------------------------------------------------------------

    public Color getColor()
    {
        return Color.getHSBColor( hue, saturation, brightness );
    }

    private void calculateColor()
    {
        hue        = (float)(cursor1x / (float)PAL1_WIDTH);
        saturation = (float)(cursor1y / (float)PAL1_HEIGHT);
        brightness = 1.0f - (float) (cursor2y / (float)PAL2_HEIGHT);
    }

//------------------------------------------------------------------------------
//    Pallete Image Build Methods
//------------------------------------------------------------------------------

    private void buildPalette()
    {
        int ipal1[] = new int[PAL1_WIDTH * PAL1_HEIGHT];
        int ipal2[] = new int[PAL2_WIDTH * PAL2_HEIGHT];

        for ( int iy = 0; iy < PAL1_HEIGHT; iy++ )
        {
            for ( int ix = 0; ix < PAL1_WIDTH; ix++ )
            {
                ipal1[(iy * PAL1_WIDTH) + ix] = 
                    Color.HSBtoRGB( (float) (ix / (float)PAL1_WIDTH), 
                                    (float) (iy / (float)PAL1_HEIGHT),
                                    brightness );
            }
        }

        for (int iy = 0; iy < PAL2_HEIGHT; iy++)
        {
            int color = Color.HSBtoRGB( hue, saturation, 
                1.0f - (float) (iy / (float)PAL2_HEIGHT) );

            for ( int ix = 0; ix < PAL2_WIDTH; ix++ )
            {
                ipal2[(iy * PAL2_WIDTH) + ix] = color; 
            }
        }

        pal1 = createImage( new MemoryImageSource( PAL1_WIDTH, PAL1_HEIGHT,
            ipal1, 0, PAL1_WIDTH ) );

        pal2 = createImage( new MemoryImageSource( PAL2_WIDTH, PAL2_HEIGHT,
            ipal2, 0, PAL2_WIDTH ) );
    }

//------------------------------------------------------------------------------
//    Cursors Methods
//------------------------------------------------------------------------------

    private void calculateCursors()
    {
        cursor1x = (int)(hue * PAL1_WIDTH);
        cursor1y = (int)(saturation * PAL1_HEIGHT);

        cursor2x = PAL1_WIDTH + PAL_OFFSET + BORDER_SIZE * 2 + PAL2_WIDTH;
        cursor2y = (int)((1.0f - brightness) * PAL2_HEIGHT);
    }

    private int grabCursor( int x, int y )
    {
        if ( x > 0 && x < PAL1_WIDTH  &&
             y > 0 && y < PAL1_HEIGHT )
        {
            return cursorGrab = CURSOR_1;        
        }        
        else if ( x > (PAL1_WIDTH + PAL_OFFSET)                  &&
                  x < (PAL1_WIDTH + PAL_OFFSET * 2 + PAL2_WIDTH) &&
                  y > 0 && y < PAL2_HEIGHT )
        {
            return cursorGrab = CURSOR_2;
        }
        else
        {
            return cursorGrab = CURSOR_NONE;
        }
    }

    private void moveCursor( int x, int y )
    {
        x -= BORDER_SIZE;
        y -= BORDER_SIZE;

        switch( cursorGrab )
        {
        case CURSOR_1:
            if (x < 0) x = 0; 
            else if (x > PAL1_WIDTH) x = PAL1_WIDTH;
      
            if (y < 0) y = 0;
            else if (y > PAL1_HEIGHT) y = PAL1_HEIGHT;
 
            cursor1x = x;
            cursor1y = y;
 
            break;
  
        case CURSOR_2:
            if (y < 0) y = 0;
            else if (y > PAL2_HEIGHT) y = PAL2_HEIGHT;

            cursor2y = y;
      
            break;
        default:
        }
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void update( Graphics g )
    {
        g.setColor( getBackground() );
        g.fillRect( PAL1_WIDTH + PAL2_WIDTH + PAL_OFFSET + BORDER_SIZE * 2 + 1, 0,
                    PAL1_WIDTH + PAL2_WIDTH + PAL_OFFSET * 2 + BORDER_SIZE * 2, 
                    getSize().height );
        paint( g );
    }

    public void paint( Graphics g )
    {
        Color top    = getBackground().brighter();
        Color bottom = getBackground().darker();

        g.drawImage( pal1, BORDER_SIZE, BORDER_SIZE, null );
        g.drawImage( pal2, PAL1_WIDTH + PAL_OFFSET + BORDER_SIZE, 
                     BORDER_SIZE, null );

        drawBorder( g, bottom, top, 0, 0, PAL1_WIDTH + BORDER_SIZE * 2 - 1, 
            PAL1_HEIGHT + BORDER_SIZE * 2 - 1, BORDER_SIZE );
        drawBorder( g, bottom, top, PAL1_WIDTH + PAL_OFFSET, 0, 
            PAL1_WIDTH + PAL_OFFSET + PAL2_WIDTH + BORDER_SIZE * 2 - 1, 
            PAL2_HEIGHT + BORDER_SIZE * 2 - 1, BORDER_SIZE );

        drawCursor2( g );
        
        g.setClip( BORDER_SIZE, BORDER_SIZE, PAL1_WIDTH, PAL1_HEIGHT ); 

        drawCursor1( g );
    }
 
    private void drawCursor1( Graphics g )
    {
        int x = cursor1x + BORDER_SIZE;
        int y = cursor1y + BORDER_SIZE;

        if ( brightness < 0.85 )
            g.setColor( Color.white );
        else
            g.setColor( Color.black );

        g.drawLine( x, y - 2, x, y + 2 ); 
        g.drawLine( x - 2, y, x + 2, y ); 
    }

    private void drawCursor2( Graphics g )
    {
        int x = cursor2x + BORDER_SIZE;
        int y = cursor2y + BORDER_SIZE;

        int xpoints[] = {x, x + 5, x + 5}; 
        int ypoints[] = {y, y - 5, y + 5};

        g.setColor( Color.black );
        g.fillPolygon( xpoints, ypoints, 3 );
    }

    private void drawBorder( Graphics g, Color top, Color bottom,
        int x, int y, int width, int height, int thickness )
    {
        for ( int i = 0; i < thickness; i++ )
        {
            g.setColor( bottom );
            g.drawLine( x + i, height - i, width - i, height - i );
            g.drawLine( width - i, y + i, width - i, height - i );
            g.setColor( top );
            g.drawLine( x + i, y + i, width - i, y + i );
            g.drawLine( x + i, y + i, x + i, height - i );
        }
    }

//------------------------------------------------------------------------------
//    MouseEvent Methods
//------------------------------------------------------------------------------

    protected void processMouseEvent( MouseEvent e )
    {
        int x = e.getX();
        int y = e.getY();

        switch ( e.getID() )
        {
        case MouseEvent.MOUSE_PRESSED:
            if ( grabCursor( x, y ) != CURSOR_NONE ) 
            {
                moveCursor( x, y );
                repaint();
            }
            break;

        case MouseEvent.MOUSE_RELEASED:
            if ( cursorGrab != CURSOR_NONE )
            {
                calculateColor();
                buildPalette();
                repaint();
                cursorGrab = CURSOR_NONE;
            }
            break;

        default:
        }
        super.processMouseEvent( e );
    } 

    protected void processMouseMotionEvent( MouseEvent e )
    {
        int x = e.getX();
        int y = e.getY();

        switch ( e.getID() )
        {
        case MouseEvent.MOUSE_DRAGGED:
            moveCursor( x, y ); 
            repaint();
            break;
        default:
        }

        super.processMouseEvent( e );
    }

//------------------------------------------------------------------------------
//    Component Size Methods
//------------------------------------------------------------------------------

    public Dimension getPreferredSize() 
    {
        return new Dimension( PAL1_WIDTH + PAL2_WIDTH + PAL_OFFSET * 2 + 
            BORDER_SIZE * 2 + 1, Math.max( PAL1_HEIGHT, PAL2_HEIGHT ) + 
            BORDER_SIZE * 2 + 1 );
            
    }

    public Dimension getMaximumSize() 
    {
        return getPreferredSize(); 
    } 

    public Dimension getMinimumSize() 
    {
        return getPreferredSize(); 
    }
}
