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

package jacob.geometry;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import jacob.main.Properties;

public class Graphics2d 
{
    private double scale;

    private Graphics g;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Graphics2d( Graphics g, double scale ) 
    {
        this.scale = scale;
        this.g = g;
    }

//------------------------------------------------------------------------------
//    Graphics Methods
//------------------------------------------------------------------------------

    public double getScale()
    {
        return scale;
    }

    public Graphics getGraphics()
    {
        return g;
    }

    public Color getColor()
    {
        return g.getColor();
    }

    public void setColor( Color c )
    {
        g.setColor( c );
    }

    public Font getFont()
    {
        return g.getFont();
    }

    public void setFont( Font f )
    {
        g.setFont( f );
    }

    public FontMetrics getFontMetrics() 
    {
	return g.getFontMetrics();
    }

    public FontMetrics getFontMetrics( Font f )
    {
	return g.getFontMetrics( f );
    }

    public void drawLine( double x1, double y1, double x2, double y2 )
    {
        g.drawLine( (int)(x1 * scale), (int)(y1 * scale), 
                    (int)(x2 * scale), (int)(y2 * scale) );
    }

    public void drawRect( double x, double y, 
                          double width, double height)
    {
        g.drawRect( (int)(x * scale), (int)(y * scale), 
                    (int)(width * scale), (int)(height * scale) );
    }

    public void fillRect( double x, double y, 
                          double width, double height)
    {
        g.fillRect( (int)(x * scale), (int)(y * scale), 
                    (int)(width * scale), (int)(height * scale) );
    }

    public void draw3DRect( double x, double y, 
                            double width, double height, boolean raised )
    {
        g.draw3DRect( (int)(x * scale), (int)(y * scale), 
                      (int)(width * scale), (int)(height * scale), raised );
    }

    public void fill3DRect( double x, double y, 
                            double width, double height, boolean raised )
    {
        g.fill3DRect( (int)(x * scale), (int)(y * scale), 
                      (int)(width * scale), (int)(height * scale), raised );
    }

    public void drawOval( double x, double y, 
                          double width, double height )
    {
        g.drawOval( (int)(x * scale), (int)(y * scale), 
                    (int)(width * scale), (int)(height * scale) );
    }

    public void fillOval( double x, double y, 
                          double width, double height )
    {
        g.fillOval( (int)(x * scale), (int)(y * scale), 
                    (int)(width * scale), (int)(height * scale) );
    }

    public void drawString( String str, double x, double y )
    {
        g.drawString( str, (int)(x * scale), (int)(y * scale) );
    }

    public void drawPolyline( double xpoints[], double ypoints[], int npoints )
    {
        for ( int i = 0; i < npoints - 1; i++ )
        {
            int j = i + 1;

            g.drawLine( (int)( xpoints[i] * scale ),
                        (int)( ypoints[i] * scale ),
                        (int)( xpoints[j] * scale ),
                        (int)( ypoints[j] * scale ) );
        }
    }

    public void drawPolygon( double xpoints[], double ypoints[], int npoints )
    {
        for ( int i = 0; i < npoints; i++ )
        {
            int j = ( i + 1 ) % npoints;

            g.drawLine( (int)( xpoints[i] * scale ),
                        (int)( ypoints[i] * scale ),
                        (int)( xpoints[j] * scale ),
                        (int)( ypoints[j] * scale ) );
        }
    }

    public void fillPolygon( double xpoints[], double ypoints[], int npoints )
    {
        int xints[] = new int[ npoints ];
        int yints[] = new int[ npoints ];

        for ( int i = 0; i < npoints; i++ )
        {
            xints[i] = (int)( xpoints[i] * scale );
            yints[i] = (int)( ypoints[i] * scale );
        }
        g.fillPolygon( xints, yints, npoints );
    }

    public void drawArrow( double x1, double y1, double x2, double y2, 
                           int headsize )
    {
        x1 *= scale; y1 *= scale;
        x2 *= scale; y2 *= scale;

        double an = Math.sqrt( (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) );

        if ( an < 1 || an > 5000 ) return;

        double ax = (x2 - x1) / an;
        double ay = (y2 - y1) / an;

        int px[] = new int[3];
        int py[] = new int[3];

        px[0] = (int)(x2 + headsize * 2 * ax);
        py[0] = (int)(y2 + headsize * 2 * ay);
 
        px[1] = (int)(x2 + headsize * ay);
        py[1] = (int)(y2 - headsize * ax);
 
        px[2] = (int)(x2 - headsize * ay);
        py[2] = (int)(y2 + headsize * ax);
 
        g.fillPolygon( px, py, 3 );
        g.drawLine( (int)x1, (int)y1, (int)x2, (int)y2 );
    }
}
