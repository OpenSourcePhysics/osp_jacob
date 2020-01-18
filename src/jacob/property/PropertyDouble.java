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

public class PropertyDouble extends Property
{
    private double value = 0;


    public PropertyDouble( String name, String group )
    {
        this( name, group, 0 );
    }

    public PropertyDouble( String name, String group, int flags )
    {
        super( name, group, flags );
    }

    public synchronized double getValue()
    {
        return value;
    } 

    public synchronized boolean setValue( double value )
    {
        this.value = value;
        setChanged();
        return true;
    }

    public synchronized boolean setValue( String svalue )
    {
        double d;

        try 
        {
            d = Double.valueOf( svalue.trim() ).doubleValue();
        } 
        catch ( NumberFormatException ex ) 
        {
            return false; 
        }
        value = d;
        setChanged();
        return true;
    }

    public synchronized String toString()
    {
        return Double.toString( value );
    }
}
