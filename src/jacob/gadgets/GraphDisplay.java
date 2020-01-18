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

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class GraphDisplay extends IndicatorDisplay
{

    private int npoints = 0;

    private double ypoints[];

    private int size;

    private int position;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public GraphDisplay() {} 

//------------------------------------------------------------------------------
//    Graph Methods
//------------------------------------------------------------------------------

    protected void initGraph()
    {
        if ( getSize().width != npoints )
        {
            npoints = getSize().width;
            ypoints = new double[npoints];
            size = position = 0;
        }
    } 

//------------------------------------------------------------------------------
//    IndicatorDisplay Methods
//------------------------------------------------------------------------------

    public void setValue( double value )
    {
        if ( npoints >= 1 ) 
        {
            ypoints[position] = value;
            position = ( position + 1 ) % npoints;
            size ++;
            if ( size > npoints ) size = npoints; 
        }
        super.setValue( value );
    }

    public void clear()
    {
        npoints = 0; //FIXME
        initGraph();
        repaint();
    }

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------
   
    public void paint( Graphics g )
    {
        int width  = getSize().width;
        int height = getSize().height;
        int zy = height / 2;
        double scale = getScale();

        initGraph();

        g.setColor( getBackground() );
        g.fillRect( 0, 0, width, height ); 

        if ( size > 2 )
        {
            for ( int i = 0; i < size; i++ )
            {
                int p1 = position - 1 - i;
                if ( p1 < 0 ) p1 = npoints + p1;
               
                if ( ypoints[p1] > 0 )
                    g.setColor( Color.red ); 
                else
                    g.setColor( Color.blue ); 

                g.drawLine( width - i - 1, zy, width - i - 1, 
                            zy - (int)(ypoints[p1]*scale) );
            }
            g.setColor( Color.black );
            g.drawLine( 0, zy, width, zy );
        } 
        g.setColor( getBackground() );
        g.draw3DRect( 0, 0, width-1, height-1, true ); 
    }

//------------------------------------------------------------------------------
//    Size Methods
//------------------------------------------------------------------------------
 
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }
 
    public Dimension getMinimumSize()
    {
        return new Dimension( 120, 80 );
    }   
}
