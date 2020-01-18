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

package jacob.scene.editor;

import java.awt.PopupMenu; 
import java.awt.event.MouseEvent; 

import jacob.main.*;
import jacob.scene.*;
import jacob.scene.modifiers.*;
import jacob.geometry.*;

public abstract class SceneAction
    implements SceneListener
{

    protected SceneEditor sceneEditor = null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SceneAction() {}

//------------------------------------------------------------------------------
//    Status Methods
//------------------------------------------------------------------------------

    public void added() {}

    public void removed() {}

//------------------------------------------------------------------------------
//    Editor Methods
//------------------------------------------------------------------------------

    public void setEditor( SceneEditor editor )
    {
        sceneEditor = editor;
    }

    public SceneEditor getEditor()
    {
        return sceneEditor;
    }

    public Scene getScene()
    {
        return sceneEditor.getScene();
    }

    public Properties getProperties()
    {
        return sceneEditor.getScene().getProperties();
    }

//------------------------------------------------------------------------------
//    Scene Mouse Events 
//------------------------------------------------------------------------------

    public void mouseClicked( SceneMouseEvent e ) 
    {
         MouseEvent ee = e.getMouseEvent();
         if ( !ee.isMetaDown() )
         {
            mouseNormalClicked( e );
         }
    }

    public void mousePressed( SceneMouseEvent e ) 
    {
         MouseEvent ee = e.getMouseEvent();
         if ( ee.isMetaDown() )
         {
             PopupMenu popup = getScene().getPopupMenu();

             if ( popup != null )
                 popup.show( getScene(), ee.getX(), ee.getY() );
         }
         else
         { 
            mouseNormalPressed( e );
         }
    }

    public void mouseReleased( SceneMouseEvent e ) 
    {
         MouseEvent ee = e.getMouseEvent();
         if ( !ee.isMetaDown() )
            mouseNormalReleased( e );
    } 

    public void mouseNormalClicked( SceneMouseEvent e ) {} 

    public void mouseNormalPressed( SceneMouseEvent e ) {}

    public void mouseNormalReleased( SceneMouseEvent e ) {} 

    public void mouseEntered( SceneMouseEvent e ) {}

    public void mouseExited( SceneMouseEvent e ) {} 

    public void mouseDragged( SceneMouseEvent e ) 
    {
        if ( !e.isMetaDown() )
            mouseNormalDragged( e );
    }

    public void mouseNormalDragged( SceneMouseEvent e ) {}

    public void mouseMoved( SceneMouseEvent e ) {}

//------------------------------------------------------------------------------
//    Paint Methods 
//------------------------------------------------------------------------------

    public void paintFg( SceneGraphics g ) {} 

    public void paintBg( SceneGraphics g ) {}

    public void paintSnappedPointer( SceneGraphics g ) 
    {
        sceneEditor.paintSnappedPointer( g );
    }

//------------------------------------------------------------------------------
//    Get Component Methods
//------------------------------------------------------------------------------

    public SceneComponent getComponentAt( String type, Point2d p )
    {
        return sceneEditor.getComponentAt( type, p );
    }

    public SceneComponent getElementAt( Point2d p )
    {
        return sceneEditor.getElementAt( p );
    }

    public SceneComponent getParticleAt( Point2d p )
    {
        return sceneEditor.getParticleAt( p );
    }

    public SceneComponent getElementOrParticleAt( Point2d p )
    {
        return sceneEditor.getElementOrParticleAt( p );
    }

//FIXME
    public Object getElementOrModifierAt( Point2d p, Class modtype )
    {
        return sceneEditor.getElementOrModifierAt( p, modtype );
    }
}
