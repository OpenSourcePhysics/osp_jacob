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

import java.awt.Component;


public abstract class IndicatorDisplay extends Component
{

    private double value;

    private double scale;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public IndicatorDisplay()
    {
        this( 1 );
    } 

    public IndicatorDisplay( double scale ) 
    {
        this.scale = scale;
        this.value = 0;
    }

//------------------------------------------------------------------------------
//    Display Methods
//------------------------------------------------------------------------------

    public void setScale( double scale )
    {
        if ( this.scale != scale )
        {
            this.scale = scale;
            repaint();
        }
    }

    public double getScale()
    {
        return scale;
    }

    public void setValue( double value )
    {
        this.value = value;
        repaint();
    }

    public double getValue()
    {
        return value;
    }

    public double getScaledValue()
    {
        return (value * scale);
    }
 
    public abstract void clear();
}
