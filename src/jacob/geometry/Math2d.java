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

//REMIND: what should we do with this class
//        is it worthed to do things this way?

public final class Math2d 
{

    private Math2d() {};

    public static double[] getPoint( double x1, double y1 )
    {
        double p[] = new double[3];

        p[0] = x1;
        p[1] = y1;
        p[2] = 1;

        return p;
    }

    public static double[] getLine( double x1, double y1, 
                                    double x2, double y2 )
    {
        double l[] = new double[3];

        l[0] = -(y2 - y1);
        l[1] =  (x2 - x1);
        l[2] =  (x1 * y2 - y1 * x2);

        return l;
    }

    public static double dot( double[] a, double[] b )
    {
        return ( a[0] * b[0] + a[1] * b[1] + a[2] * b[2] );
    }

    public static double[] cross( double[] a, double[] b )
    {
        double c[] = new double[3];

        c[0] = a[1] * b[2] - b[1] * a[2];
	c[1] = a[2] * b[0] - b[2] * a[0];
        c[2] = a[0] * b[1] - b[0] * a[1];

        return c; 
    }

    public static double[] getLineIntersection( double[] a, double[] b )
    {
        double p[] = new double[3];

        double d = a[0] * b[1] - b[0] * a[1];

        p[0] = (a[1] * b[2] - b[1] * a[2]) / d;
        p[1] = (a[0] * b[2] - b[0] * a[2]) / d;
        p[2] = 1;

        return p;
    }

    public static double[] getPointLineProject( double[] p, double[] l )
    {
        double pp[] = new double[3];

        double d = (l[0] * p[0] + l[1] * p[1] + l[2]) / 
                   (l[0] * l[0] + l[1] * l[1]); 

//FIXME: move mirror constant into Polygon2d (at this moment Polygon2d is
//       the only class using this methods)
        pp[0] = p[0] - l[0] * d * 1.1; 
        pp[1] = p[1] - l[1] * d * 1.1;
        pp[2] = 1;

        return pp;
    }

    public static double getPointLineDist( double[] p, double[] l )
    {
//FIXME
        return (l[0] * p[0] + l[1] * p[1] + l[2]) / 
                   (l[0] * l[0] + l[1] * l[1]); 
    }
}
