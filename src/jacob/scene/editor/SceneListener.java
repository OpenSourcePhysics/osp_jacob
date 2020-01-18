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

import java.awt.event.MouseListener; 
import java.awt.event.MouseMotionListener; 

import jacob.scene.*;

public interface SceneListener
{
    public void added(); 

    public void removed(); 

    public void setEditor( SceneEditor editor );

    public void mouseClicked( SceneMouseEvent e ); 

    public void mousePressed( SceneMouseEvent e ); 

    public void mouseReleased( SceneMouseEvent e ); 

    public void mouseEntered( SceneMouseEvent e ); 

    public void mouseExited( SceneMouseEvent e );  

    public void mouseDragged( SceneMouseEvent e ); 

    public void mouseMoved( SceneMouseEvent e );

    public void paintFg( SceneGraphics g ); 

    public void paintBg( SceneGraphics g );
}
