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

import jacob.scene.*;
import jacob.system.*;
import jacob.geometry.*;
import jacob.data.*;

public class Inflator extends ComponentModifier
{

    public static final String TYPE = "inflator";

    private double scale = 1;

    private double pressure = 0; 

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Inflator() {} 

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
        scale = 1;
    }

//------------------------------------------------------------------------------
//    Component Modification Methods
//------------------------------------------------------------------------------

//FIXME
    public void preIntegrationModify( long time )
    {
        double plimit = getProperties().inflatorLimitPressure.getValue(); 
        double scaleLimit = getProperties().inflatorScaleLimit.getValue();
        double scaleInc   = getProperties().inflatorScaleInc.getValue();
        double scaleDec   = getProperties().inflatorScaleDec.getValue();
        double tpl = plimit + getProperties().inflatorIncDecTrsh.getValue();
        double bpl = plimit;

        if ( scale < scaleLimit && pressure > tpl )
        {
            getComponent().scale( 1 + scaleInc );
            scale *= 1 + scaleInc;
        }
        else if ( scale > 1 && pressure < bpl )
        {
            getComponent().scale( 1 - scaleDec );
            scale *= 1 - scaleDec;
        }
    } 

    public void postIntegrationModify( long time )
    {
        ParticleIterator iterator;

        SceneComponent c = getComponent();

        pressure = 0;

        for ( int i = 0; i < c.countChildren(); i++ )
        {
            SceneComponent child = (SceneComponent)c.getChildAt( i );

            if ( child instanceof Particle )
            {
                Particle p = (Particle)child;
                if ( p.isOnBorder() )
                    pressure += Math.sqrt( p.getEx() * p.getEx() + 
                                           p.getEy() * p.getEy() );
            }
        }
    }

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException
    {
    }
 
    public void writeData( DataElement data )
    {
        DataElement rdata = data.newElement( TYPE );
    }
}
