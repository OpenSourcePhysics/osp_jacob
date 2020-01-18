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

import java.applet.Applet;
import java.util.Vector;

import jacob.main.*;
import jacob.scene.*;
import jacob.scene.editor.*;
import jacob.geometry.*;
import jacob.lib.*;

public abstract class Gadget extends Applet
    implements SceneListener
{
    private Scene scene = null;

    private SceneEditor sceneEditor = null;

//------------------------------------------------------------------------------
//    Connect Methods
//------------------------------------------------------------------------------

    public void connect( JInterface jint )
    {
        scene = jint.getScene();
        start();
    }

    public boolean isConnected()
    {
        return scene != null ? true : false;
    }

//------------------------------------------------------------------------------
//    Type Methods
//------------------------------------------------------------------------------

    public abstract String getType();

//------------------------------------------------------------------------------
//    Scene Methods
//------------------------------------------------------------------------------

    public Scene getScene()
    {
        return scene;
    }

    public Properties getProperties()
    {
        return scene.getProperties();
    }

    public SceneEditor getEditor()
    {
        return sceneEditor;
    }

    public void sceneListen()
    {
        scene.setListener( this );
    }

    public void sceneUnListen()
    {
        if ( sceneEditor != null ) //FIXME
            scene.setListener( null );
    }

//------------------------------------------------------------------------------
//    Scene Listener Methods
//------------------------------------------------------------------------------

    public void added() {}

    public void removed() {}

    public void setEditor( SceneEditor editor )
    {
        sceneEditor = editor;
    }

    public void mouseClicked( SceneMouseEvent e ) {}

    public void mousePressed( SceneMouseEvent e ) {}

    public void mouseReleased( SceneMouseEvent e ) {}

    public void mouseEntered( SceneMouseEvent e ) {}

    public void mouseExited( SceneMouseEvent e ) {}

    public void mouseDragged( SceneMouseEvent e ) {}

    public void mouseMoved( SceneMouseEvent e ) {}

    public void paintFg( SceneGraphics g ) {}

    public void paintBg( SceneGraphics g ) {}

//------------------------------------------------------------------------------
//    Particle Methods
//------------------------------------------------------------------------------

    public Vector getParticlesData()
    {
        Vector pdata = new Vector();
        synchronized ( scene.getSceneLock() )
        {
            ParticleIterator pi = scene.createParticleIterator();
            while ( !pi.isDone() )
            {
                pdata.addElement( new ParticleData( pi.getCurrentParticle() ) );
                pi.next();
            }
        }
        return pdata;
    }

    public Vector getParticlesData( SceneContainer c )
    {
        Vector pdata = new Vector();
        synchronized ( scene.getSceneLock() )
        {
            ParticleIterator pi = new ParticleIterator( c.createIterator() );
            while ( !pi.isDone() )
            {
                pdata.addElement( new ParticleData( pi.getCurrentParticle() ) );
                pi.next();
            }
        }
        return pdata;
    }

    public Vector getLocalParticlesData( SceneContainer c )
    {
        Vector pdata = new Vector();
        synchronized ( scene.getSceneLock() )
        {
            for ( int i = 0; i < c.countChildren(); i++ )
            {
                SceneComponent child = c.getChildAt( i );
                if ( child instanceof Particle )
                    pdata.addElement( new ParticleData( (Particle)child ) );
            }
        }
        return pdata;
    }

    public Vector getElementsData(boolean getpdata)
    {
        Vector edata = new Vector();
        synchronized ( scene.getSceneLock() )
        {
            ElementIterator ei = scene.createElementIterator();
            while ( !ei.isDone() )
            {
                edata.addElement( new ElementData( ei.getCurrentElement(), getpdata ) );
                ei.next();
            }
        }
        return edata;
	}

    public Vector getElementsData(boolean getpdata, double x, double y)
    {
        Vector edata = new Vector();
        synchronized ( scene.getSceneLock() )
        {
            ElementIterator ei = scene.createElementIterator();
            while ( !ei.isDone() )
            {
                if (ei.getCurrentElement().contains(new Point2d(x, y)))
                    edata.addElement(new ElementData(ei.getCurrentElement(), getpdata, x, y));
                else
                    edata.addElement(new ElementData(ei.getCurrentElement(), getpdata));
                ei.next();
            }
        }
        return edata; 
	}

    public ElementData getElementDataAt(boolean getpdata, double x, double y)
    {
        synchronized ( scene.getSceneLock() )
        {
            ElementIterator ei = scene.createElementIterator();
            while ( !ei.isDone() )
            {
                if (ei.getCurrentElement().contains(new Point2d(x, y)))
                    return new ElementData( ei.getCurrentElement(), getpdata, x, y );
                ei.next();
            }
        }
        return null;
	}
}
