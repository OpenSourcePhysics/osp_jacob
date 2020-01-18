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

import jacob.geometry.*;
import jacob.scene.*;

public abstract class ModIntegrator implements Integrator
{
    private long time;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public ModIntegrator() 
    {
        time = 0;
    }

//------------------------------------------------------------------------------
//    Integrator Attributes Methods
//------------------------------------------------------------------------------

    public long getTime()
    {
        return time;
    }

//------------------------------------------------------------------------------
//    Integration Methods
//------------------------------------------------------------------------------

    public void integrate( Scene scene )
    {
        time++;

        synchronized( scene.getSceneLock() )
        {
            preIntegration( scene );
            integrate0( scene );
            postIntegration( scene );
        }
    }

    protected void preIntegration( Scene scene )
    {
        SceneIterator iterator;

        iterator = scene.createParticleIterator();
        while ( !iterator.isDone() )
        {
            particlePreIntegrate( (Particle) iterator.getCurrentComponent() );
            iterator.next();
        }

        iterator = scene.createIterator();
        while ( !iterator.isDone() )
        {
            SceneComponent c = iterator.getCurrentComponent();
            if ( c.hasModifiers() )
                c.getModifiers().preIntegrationModify( time );
            iterator.next();
        }
    }

    protected void postIntegration( Scene scene )
    {
        SceneIterator iterator;

        iterator = scene.createParticleIterator();
        while ( !iterator.isDone() )
        {
            particlePostIntegrate( (Particle) iterator.getCurrentComponent() );
            iterator.next();
        }

        iterator = scene.createIterator();
        while ( !iterator.isDone() )
        {
            SceneComponent c = iterator.getCurrentComponent();
            if ( c.hasModifiers() )
                c.getModifiers().postIntegrationModify( time );
            iterator.next();
        }
    }

    protected abstract void integrate0( Scene scene );

    protected abstract void particlePreIntegrate( Particle p );

    protected abstract void particlePostIntegrate( Particle p );
}
