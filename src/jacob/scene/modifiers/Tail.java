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

package jacob.scene.modifiers;

import java.awt.Color;

import jacob.scene.*;
import jacob.system.*;
import jacob.property.*;
import jacob.geometry.*;
import jacob.data.*;

public class Tail extends ComponentModifier
{

    public static final String TYPE = "tail";

    private int npoints;

    private double xpoints[];

    private double ypoints[];

    private int size;

    private int position;

    private Color color = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Tail()
    {
        int npoints = 0;
        xpoints = null;
        ypoints = null;
    }

//------------------------------------------------------------------------------
//    Type Methods
//------------------------------------------------------------------------------

    public String getType()
    {
        return TYPE;
    }

//------------------------------------------------------------------------------
//    Status Methods
//------------------------------------------------------------------------------

    public void added()
    {
        npoints = getProperties().tailLength.getValue();

        xpoints = new double[npoints];
        ypoints = new double[npoints];
        size = 0;
        position = 0;
    }

    public void removed()
    {
        int npoints = 0;
        xpoints = null;
        ypoints = null;
    }

//------------------------------------------------------------------------------
//    Component Modification Methods
//------------------------------------------------------------------------------

    public void componentMoved( Point2d p )
    {
        addPoint( getComponent().getLocation() );
    }

    public void componentRotated( Point2d pivot, double angle )
    {
        addPoint( getComponent().getLocation() );
    }

//------------------------------------------------------------------------------
//    Tail Methods
//------------------------------------------------------------------------------

    private void addPoint( Point2d p )
    {
        if ( npoints < 1 ) return;
        xpoints[position] = p.x;
        ypoints[position] = p.y;
        position = ( position + 1 ) % npoints;
        size ++;
        if ( size > npoints ) size = npoints;
    }

//------------------------------------------------------------------------------
//    Update Property Methods
//------------------------------------------------------------------------------

    public void updateProperty( UpdatePropertyEvent pe )
    {
        int tlen = getProperties().tailLength.getValue();

        if ( tlen != npoints )
        {
            npoints = tlen;
            xpoints = new double[npoints];
            ypoints = new double[npoints];
            size = 0;
            position = 0;
        }
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g )
    {
        if ( size > 2 )
        {
            if ( color == null )
                g.setColor( getProperties().tailFg.getValue() );
            else
                g.setColor( color );

//REMIND: should we optimize this (in case of VERY long tails)?
            for ( int i = 0; i < size - 1; i++ )
            {
                int p1 = position - 1 - i;
                int p2 = p1 - 1;
                if ( p1 < 0 ) p1 = npoints + p1;
                if ( p2 < 0 ) p2 = npoints + p2;
                g.drawLine( xpoints[p1], ypoints[p1],
                            xpoints[p2], ypoints[p2] );
            }
        }
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
        color = data.getColorAttribute( "fgcolor", null );
    }

    public void writeData( DataElement data )
    {
        DataElement tdata = data.newElement( TYPE );

        if ( color != null ) tdata.setAttribute( "fgcolor", color );
    }
}
