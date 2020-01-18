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

import jacob.system.*;
import jacob.scene.*;
import jacob.data.*;

public class Polygon2d extends Shape2d
{
    protected double cx, cy;
 
    protected double xpoints[] = new double[4];

    protected double ypoints[] = new double[4];

    protected int npoints = 0;
    
//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Polygon2d() {}

    public Polygon2d( double xpoints[], double ypoints[], int npoints ) 
    {
        set( xpoints, ypoints, npoints );
    }

//------------------------------------------------------------------------------
//    Polygon Geometry Methods
//------------------------------------------------------------------------------

    public void set( double xpoints[], double ypoints[], int npoints )
    {
        this.xpoints = new double[ npoints ];
        this.ypoints = new double[ npoints ];
        this.npoints = npoints;
        System.arraycopy( xpoints, 0, this.xpoints, 0, npoints );
        System.arraycopy( ypoints, 0, this.ypoints, 0, npoints );
        CalcBoundsAndCenter();
    }

    public void addPoint( Point2d p )
    {
        if ( npoints == xpoints.length )
        {
            double xtmp[] = new double[npoints * 2];
            double ytmp[] = new double[npoints * 2];
 
            System.arraycopy( xpoints, 0, xtmp, 0, xpoints.length );
            System.arraycopy( ypoints, 0, ytmp, 0, ypoints.length );
 
            xpoints = xtmp;
            ypoints = ytmp;
        }
        xpoints[npoints] = p.x;
        ypoints[npoints] = p.y;
        npoints++;
        CalcBoundsAndCenter();
    }

    public void setPoint( int index, Point2d p )
    {
        if ( index < npoints )
        {
            xpoints[index] = p.x;
            ypoints[index] = p.y;
            CalcBoundsAndCenter();
        }
    }

    public Point2d getPoint( int index )
    {
        if ( index < npoints )
            return new Point2d( xpoints[index], ypoints[index] );
        else
            return null;
    }

    public int getPointsNum()
    {
        return npoints;
    }

    private void CalcBoundsAndCenter()
    {
        Bounds2d newbounds = new Bounds2d();
        double tmpx = 0;
        double tmpy = 0;

        for ( int i = 0; i < npoints; i++ )
        {
            tmpx += xpoints[i];
            tmpy += ypoints[i];
            newbounds.add( xpoints[i], ypoints[i] );
        }
        cx = tmpx / npoints;
        cy = tmpy / npoints;
        setBounds( newbounds );
    }

//------------------------------------------------------------------------------
//    Shape Geometry Methods
//------------------------------------------------------------------------------

    public void move( Point2d p )
    {
        double xoff = p.x - cx;
        double yoff = p.y - cy;

        cx = p.x;
        cy = p.y;

        for( int i = 0; i < npoints; i++ )
        {
            xpoints[i] += xoff;
            ypoints[i] += yoff;
        }
        setBounds( bounds.x + xoff, bounds.y + yoff,
                   bounds.width, bounds.height );
    }

    public void scale( double scale )
    {
        Bounds2d newbounds = new Bounds2d();

        for( int i = 0; i < npoints; i++ )
        {
            xpoints[i] = (xpoints[i] - cx) * scale + cx;
            ypoints[i] = (ypoints[i] - cy) * scale + cy;
            newbounds.add( xpoints[i], ypoints[i] );
        }
        setBounds( newbounds );
    }

    public void rotate( Point2d pivot, double angle )
    {
        double cosf = Math.cos( angle );
        double sinf = Math.sin( angle );

        double x0 = cx - pivot.x;
        double y0 = cy - pivot.y;

        cx =   x0 * cosf + y0 * sinf + pivot.x;
        cy = - x0 * sinf + y0 * cosf + pivot.y;
 
        Bounds2d newbounds = new Bounds2d();

        for ( int i = 0; i < npoints; i++ )
        {
            double xi0 = xpoints[i] - pivot.x;
            double yi0 = ypoints[i] - pivot.y;

            xpoints[i] =   xi0 * cosf + yi0 * sinf + pivot.x;
            ypoints[i] = - xi0 * sinf + yi0 * cosf + pivot.y;
            newbounds.add( xpoints[i], ypoints[i] );
        }
        setBounds( newbounds );
    }

    public Point2d getCenter()
    {
        return new Point2d( cx, cy );
    }

    public double getBorderDistance( Point2d p )
    {
        double mind = Double.MAX_VALUE;
 
        for ( int i = 0; i < npoints; i++ )
        {
//FIXME: clean up & make this really work
            double x  = p.x;
            double y  = p.y;
            double x1 = xpoints[i];
            double y1 = ypoints[i];
            double x2 = xpoints[( i + 1 ) % npoints]; 
            double y2 = ypoints[( i + 1 ) % npoints]; 

            double dx = x2 - x1;
            double dy = y2 - y1;

            if ( ( x *  dx + y *  dy + -dx * x1 + -dy * y1 ) >= 0 &&  
                 ( x * -dx + y * -dy +  dx * x2 +  dy * y2 ) >= 0 )  
            {
                double d = Math.abs( ( -dy * x + dx * y + x1 * y2 - x2 * y1 ) / 
                                     Math.sqrt( dx * dx + dy * dy ) );
                if ( d < mind ) 
                    mind = d;
            }
        }
        return mind; 
    }

    public boolean contains( Point2d p )
    {
        int ncross = 0;
        int j = npoints - 1;

        for ( int i = 0; i < npoints; i++ )
        {
            if ( xpoints[j] >= p.x && xpoints[i] <  p.x ||
                 xpoints[j] <  p.x && xpoints[i] >= p.x  )
            {
                //linear interpolation 
                double piy = ypoints[j] + 
                             (p.x - xpoints[j]) / 
                             (xpoints[i] - xpoints[j]) * 
                             (ypoints[i] - ypoints[j]);

                if ( piy == p.y ) 
                    return true;
                else if ( piy > p.y )
                    ncross++;
            }
            j = i;        
        }
        return ( ncross % 2 != 0 );
    }

//FIXME: find a better way 
    public Point2d getConfinedPoint( Point2d p )
    {
        if ( contains( p ) )
            return p;

        int index1 = -1;
        int index2 = -1;
        double mind1 = Double.MAX_VALUE;
        double mind2 = Double.MAX_VALUE;

        for ( int i = 0; i < npoints; i++ )
        {
            int j = (i + 1) % npoints;

            double di = p.distance2( xpoints[i], ypoints[i] );
            double dj = p.distance2( xpoints[j], ypoints[j] );

            if ( (di < mind1 && dj < mind2) ||
                 (dj < mind1 && di < mind2) )
            {
                mind1 = di;
                mind2 = dj;
                index1 = i;
                index2 = j;
            } 
        }
System.out.println( "closest segment = " + index1 + " - " + index2 ); 
        double pp[] = Math2d.getPoint( p.x, p.y );
        double sl[] = Math2d.getLine( xpoints[index1], ypoints[index1], 
                                      xpoints[index2], ypoints[index2] );

        double pn[] = Math2d.getPointLineProject( pp, sl );
        Point2d newp = new Point2d( pn[0], pn[1] );

        if ( contains( newp ) )
        {
            return newp;
        }
        else
        {
            if ( mind1 < mind2 )
                return new Point2d( xpoints[index1], ypoints[index1] );
            else
                return new Point2d( xpoints[index2], ypoints[index2] );
        } 
    }

    public Point2d getConfinedPoint( Point2d oldp, Point2d newp )
    {
        return getConfinedPoint0( oldp, newp, 0 );
    }

//REMIND: what should we do with Math2d class
    private Point2d getConfinedPoint0( Point2d oldp, Point2d newp, int rdepth )
    {
        if ( contains( newp ) ) 
            return newp;

        if ( rdepth > 1  )
            return oldp;

        double minx = Math.min( oldp.x, newp.x );
        double miny = Math.min( oldp.y, newp.y );
        double maxx = Math.max( oldp.x, newp.x );
        double maxy = Math.max( oldp.y, newp.y );

        double pl[] = Math2d.getLine( oldp.x, oldp.y, newp.x, newp.y );
        double p1[] = Math2d.getPoint( oldp.x, oldp.y );
        double p2[] = Math2d.getPoint( newp.x, newp.y );

        for ( int i = 0; i < npoints; i++ )
        {
            double x1 = xpoints[i];
            double y1 = ypoints[i];
            double x2 = xpoints[( i + 1 ) % npoints]; 
            double y2 = ypoints[( i + 1 ) % npoints]; 

            if ( ( maxx >= Math.min( x1, x2 ) ) && 
                 ( maxy >= Math.min( y1, y2 ) ) &&
                 ( minx <= Math.max( x1, x2 ) ) && 
                 ( miny <= Math.max( y1, y2 ) ) ) 
            {
                double sl[] = Math2d.getLine( x1, y1, x2, y2 );
                double s1[] = Math2d.getPoint( x1, y1 );
                double s2[] = Math2d.getPoint( x2, y2 );

                if ( Math2d.dot( sl, p1 ) * Math2d.dot( sl, p2 ) <= 0 && 
                     Math2d.dot( pl, s1 ) * Math2d.dot( pl, s2 ) <= 0 )
                {
                    double pp[] = Math2d.getPointLineProject( p2, sl );
                    Point2d prop = new Point2d( pp[0], pp[1] );
                    return getConfinedPoint0( oldp, prop, ++rdepth );
                } 
            }
        }
        if ( !contains( oldp ) )
        {
System.out.println( "WOOOPLA" );
            return getConfinedPoint( oldp );
        }
        else
        {
            return oldp;
        }
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void draw( Graphics2d g )
    {
        if ( npoints > 0 )
            g.drawPolygon( xpoints, ypoints, npoints );
    }

    public void fill( Graphics2d g )
    {
        if ( npoints > 0 )
            g.fillPolygon( xpoints, ypoints, npoints );
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        double points[][] = data.getDoubleMatrixAttribute( "points" );

        if ( points[0].length != 2 )
            throw new DataParseException( "Malformed polygon points" );

        npoints = points.length;
        xpoints = new double[npoints];
        ypoints = new double[npoints];

        for ( int i = 0; i < points.length; i++ )
        {
            xpoints[i] = points[i][0];
            ypoints[i] = points[i][1];
        }
        CalcBoundsAndCenter();
    }

    public void writeData( DataElement data )
    {
        DataElement pdata = data.newElement( "polygon" );
        double points[][] = new double[npoints][2];

        for ( int i = 0; i < npoints; i++ )
        {
            points[i][0] = xpoints[i];
            points[i][1] = ypoints[i];
        }
        pdata.setAttribute( "points", points );    
    }
}
