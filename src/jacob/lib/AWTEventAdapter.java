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

package jacob.lib;

import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener; 
import java.awt.event.ComponentListener; 
import java.awt.event.ContainerListener; 
import java.awt.event.FocusListener; 
import java.awt.event.ItemListener; 
import java.awt.event.KeyListener; 
import java.awt.event.MouseListener; 
import java.awt.event.MouseMotionListener;
import java.awt.event.TextListener;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent; 
import java.awt.event.ComponentEvent; 
import java.awt.event.ContainerEvent; 
import java.awt.event.FocusEvent; 
import java.awt.event.ItemEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.MouseEvent; 
import java.awt.event.TextEvent;
import java.awt.event.WindowEvent;


public class AWTEventAdapter implements 
    ActionListener,
    AdjustmentListener, 
    ComponentListener, 
    ContainerListener, 
    FocusListener, 
    ItemListener, 
    KeyListener, 
    MouseListener, 
    MouseMotionListener, 
    TextListener, 
    WindowListener
{
    public AWTEventAdapter() {}

    public void actionPerformed( ActionEvent e ) {}

    public void adjustmentValueChanged( AdjustmentEvent e ) {}

    public void componentResized( ComponentEvent e ) {}

    public void componentMoved( ComponentEvent e ) {}

    public void componentShown( ComponentEvent e ) {}

    public void componentHidden( ComponentEvent e ) {}
    
    public void componentAdded( ContainerEvent e ) {}

    public void componentRemoved( ContainerEvent e ) {}

    public void focusGained( FocusEvent e ) {}

    public void focusLost( FocusEvent e ) {}

    public void itemStateChanged( ItemEvent e ) {}

    public void keyTyped( KeyEvent e ) {}

    public void keyPressed( KeyEvent e ) {}

    public void keyReleased( KeyEvent e ) {}

    public void mouseClicked( MouseEvent e ) {}

    public void mousePressed( MouseEvent e ) {}

    public void mouseReleased( MouseEvent e ) {}

    public void mouseEntered( MouseEvent e ) {}

    public void mouseExited( MouseEvent e ) {}

    public void mouseDragged( MouseEvent e ) {}

    public void mouseMoved( MouseEvent e ) {}

    public void textValueChanged( TextEvent e ) {}

    public void windowOpened( WindowEvent e ) {}

    public void windowClosing( WindowEvent e ) {}

    public void windowClosed( WindowEvent e ) {}

    public void windowIconified( WindowEvent e ) {}

    public void windowDeiconified( WindowEvent e ) {}

    public void windowActivated( WindowEvent e ) {}

    public void windowDeactivated( WindowEvent e ) {}
}
