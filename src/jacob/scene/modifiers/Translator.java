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
import jacob.property.*;
import jacob.geometry.*;
import jacob.data.*;

public class Translator extends ComponentModifier
{
    public static final String TYPE = "translator";

    private double ex = 0;

    private double ey = 0;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public Translator() {}

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
        ex = ey = 0;
    }

//------------------------------------------------------------------------------
//    Component Modification Methods
//------------------------------------------------------------------------------

    public void preIntegrationModify( long time )
    {
        Point2d loc = getComponent().getLocation();

        double tconst = getProperties().translationConst.getValue();


        loc.x += ex * tconst;
        loc.y += ey * tconst;

        getComponent().move( loc );

        ex = ey = 0;
    }

    public void postIntegrationModify( long time )
    {
        ParticleIterator iterator;

        iterator = new ParticleIterator( getComponent().createIterator() );
        double econst = getProperties().evectorScale.getValue();
		double mconst = getProperties().mvectorScale.getValue();

        while ( !iterator.isDone() )
        {
//REMIND: this could be done in Integrator (for speed reasons)
            Particle p = iterator.getCurrentParticle();
//FIXED VV 15.9.2002
//            ex += p.getEx();
//            ey += p.getEy();
          if ( econst > 0 )
         {
			 ex += p.getEx();
             ey += p.getEy();
		 }
		 if ( mconst > 0 )
		 {
             ex += p.getMx();
             ey += p.getMy();
	     }

            iterator.next();
        }
    }
//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

    public void paintBg( SceneGraphics g )
    {
        g.setColor( getProperties().elementTranBg.getValue() );

    }


    public void paintFg( SceneGraphics g )
    {
		//new by Dragan Simeonov 9.11.02

        ////old
        //Point2d loc  = getComponent().getLocation();
        g.setColor( getProperties().elementTranBg.getValue() );
        //g.fillOval(loc.x-10, loc.y-10, 20, 20);

        getComponent().getShape().fill( g );

                Point2d loc  = getComponent().getLocation();

        double econst = getProperties().evectorScale.getValue();
		        g.setColor( getProperties().eforceFg.getValue() );
		        g.drawArrow( loc.x, loc.y, loc.x+ex*econst, loc.y+ey*econst,
                     getProperties().arrowHeadSize.getValue() );
    }


//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

    public void readData( DataElement data )
        throws DataParseException {}

    public void writeData( DataElement data )
    {
        DataElement rdata = data.newElement( TYPE );
    }
}
