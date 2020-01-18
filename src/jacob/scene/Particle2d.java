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

package jacob.scene;

import java.awt.Graphics;
import java.awt.Color;

import jacob.system.*;
import jacob.geometry.*;
import jacob.data.*;

public class Particle2d extends Shape2d
{
    protected double x, y;
 
//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Particle2d() {}

    public Particle2d( double x, double y )
    {
        set( x, y );
    }

//------------------------------------------------------------------------------
//    Particle Geometry Methods
//------------------------------------------------------------------------------

    public void set( double x, double y )
    {
        this.x = x;
        this.y = y;
        setBounds( x, y, 0, 0 );
    }

//------------------------------------------------------------------------------
//    Shape Geometry Methods
//------------------------------------------------------------------------------

    public void move( Point2d p )
    {
//REMIND: move translation in Point2d
        double xoff = p.x - x;
        double yoff = p.y - y;

        x = p.x;
        y = p.y;
        setBounds( bounds.x + xoff, bounds.y + yoff,
                   bounds.width, bounds.height );
    }

    public void scale( double scale ) {}

    public void rotate( Point2d pivot, double angle )
    {
//REMIND: move rotation in Point2d
        double cosf = Math.cos( angle );
        double sinf = Math.sin( angle );

        double x0 = x - pivot.x;
        double y0 = y - pivot.y;

        x =   x0 * cosf + y0 * sinf + pivot.x;
        y = - x0 * sinf + y0 * cosf + pivot.y;
        setBounds( x, y, 0, 0 );
    }

    public Point2d getCenter()
    {
        return new Point2d( x, y );
    }

    public double getBorderDistance( Point2d p )
    {
        throw new InternalException( "illegal operation" );
    }

    public boolean contains( Point2d p )
    {
        throw new InternalException( "illegal operation" );
    }

    public Point2d getConfinedPoint( Point2d p )
    {
        throw new InternalException( "illegal operation" );
    }

    public Point2d getConfinedPoint( Point2d oldp, Point2d newp )
    {
        throw new InternalException( "illegal operation" );
    }

//------------------------------------------------------------------------------
//    Particle Paint Methods
//------------------------------------------------------------------------------

    public void paintParticle( SceneGraphics g, double charge )
    {
        Graphics gg = g.getGraphics();
        double scale = g.getScale();
        int size = g.getProperties().particleSize.getValue();
        int ix = (int)(x * scale);
        int iy = (int)(y * scale);
        
        if ( charge > 0 )
        {
            gg.setColor( g.getProperties().particlePosBg.getValue() );
            gg.fillOval( ix - size, iy - size, size * 2, size * 2 ); 

            gg.setColor( g.getProperties().particlePosFg.getValue() );
            gg.drawLine( ix - size + 2, iy, ix + size - 2, iy );
            gg.drawLine( ix, iy - size + 2, ix, iy + size - 2 );
        }
        else if ( charge < 0 )
        {
            gg.setColor( g.getProperties().particleNegBg.getValue() );
            gg.fillOval( ix - size, iy - size, size * 2, size * 2 );

            gg.setColor( g.getProperties().particleNegFg.getValue() );
            gg.drawLine( ix - size + 2, iy, ix + size - 2, iy );
        }
        else 
        {
            gg.setColor( Color.black ); 
            gg.fillOval( ix - size, iy - size, size * 2, size * 2 );
        }
    }

    public void paintParticleHl( SceneGraphics g, double charge )
    {
        Graphics gg = g.getGraphics();
        double scale = g.getScale();
        int size = g.getProperties().particleSize.getValue();
        int ix = (int)(x * scale);
        int iy = (int)(y * scale);
            
        gg.setColor( g.getProperties().particleHl.getValue() );
        gg.drawOval( ix - size, iy - size, 
                     size * 2, size * 2 ); //FIXME
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void draw( Graphics2d g ) {} 

    public void fill( Graphics2d g ) {}

//------------------------------------------------------------------------------
//    Data R/W Methods 
//------------------------------------------------------------------------------

    public void readData( DataElement data ) 
        throws DataParseException {}

    public void writeData( DataElement data ) {}
}
