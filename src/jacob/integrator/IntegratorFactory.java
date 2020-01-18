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

package jacob.integrator;

public final class IntegratorFactory
{
    private IntegratorFactory() {}

    public static Integrator createIntegrator( String type )
        throws UnknownIntegratorException
    {
         type = type.toLowerCase();

         if ( type.equals( "simple" ) )
             return new SimpleIntegrator();
         else if ( type.equals( "standard" ) )
             return new StandardIntegrator();
         else if ( type.equals( "test" ) )
             return new TestIntegrator();
         else if ( type.equals( "current" ) )
             return new CondIntegrator();
         else if ( type.equals( "kill" ) )
             return new DipoleKillIntegrator();
         else if ( type.equals( "advanced1" ) )
             return new Advanced1Integrator();
         else if ( type.equals( "advanced2" ) )
             return new Advanced2Integrator();
         else if ( type.equals( "runge" ) )
             return new RungeKuttaIntegrator();
         else if ( type.equals( "collision" ) )
             return new CircleCollisionIntegrator();
         else
             throw new UnknownIntegratorException( type );
    }
}
