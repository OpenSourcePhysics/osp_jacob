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

package jacob.scene;

import java.awt.Graphics;

import jacob.main.Properties;
import jacob.geometry.Graphics2d;

public class SceneGraphics extends Graphics2d
{
    private Scene scene;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public SceneGraphics( Scene scene, Graphics g )
    {
        super( g, scene.getProperties().sceneScale.getValue() );
        this.scene = scene;        
    }

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
}
