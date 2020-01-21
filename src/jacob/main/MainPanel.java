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

package jacob.main;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Menu;
import java.awt.PopupMenu;
import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.util.Vector;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jacob.system.*;
import javajs.async.AsyncFileChooser;
import jacob.property.*;
import jacob.scene.editor.*;
import jacob.scene.*;
import jacob.scene.modifiers.*;
import jacob.lib.*;
import jacob.data.*;
import jacob.integrator.*;
import jacob.gadgets.*;

public class MainPanel extends Panel 
{
	static public boolean isJS = /** @j2sNative true || */ false;
	
    private Frame frame = null;

    private Panel controlPanel = null;

    private MenuBar frameMenuBar = null;

    private DoubleBufferPanel doubleBuff;

    private Listener listener;

    private Scene scene;  

    private PropertyEditor propertyEditor = null;

    private GadgetFrame gadgetFrame = null;

    private PropertyMgr propertyMgr;

    private SystemMgr systemMgr;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public MainPanel( SystemMgr systemMgr )
    {
        this.systemMgr = systemMgr;
        this.propertyMgr = systemMgr.getPropertyMgr();

        scene = new Scene( systemMgr );
        scene.setIntegrator( 
           propertyMgr.getProperties().integratorType.getValue() );

        listener = new Listener();

        setLayout( new BorderLayout() );
              
        doubleBuff = new DoubleBufferPanel( 
           propertyMgr.getProperties().doubleBuff.getValue() );
        doubleBuff.setLayout( new BorderLayout() );

        doubleBuff.add( "Center", scene );
        add( "Center", doubleBuff );
       
        initUI();
 
        if ( controlPanel != null && 
             propertyMgr.getProperties().showControls.getValue() ) 
        {
            add( "East", controlPanel );
        }

        if (  propertyMgr.getProperties().showGadgets.getValue() )
        {
            gadgetFrame = new GadgetFrame( systemMgr, MainPanel.this ); 
            gadgetFrame.setVisible( true );
        }

        systemMgr.getPropertyMgr().attachObserver( listener );
    }

    private void initUI()
    {
        DataElement data = systemMgr.getUIData();

        if ( data == null ) return;

        DataElement mData = data.getOptElement( "menubar" );

        if ( mData != null )
        {
            frameMenuBar = UIBuilder.buildMenuBar( systemMgr, mData, 
                                                   listener );

            scene.setPopupMenu( UIBuilder.buildPopupMenu( systemMgr, mData, 
                                                          listener ) );
        }

        DataElement cdata = data.getOptElement( "controls" );

        if ( cdata != null ) 
        {
            controlPanel = new ControlPanel( systemMgr, cdata );
        }
    }

//------------------------------------------------------------------------------
//    Scene methods   
//------------------------------------------------------------------------------

    public Scene getScene()
    {
        return scene;
    }

//------------------------------------------------------------------------------
//    Frame methods   
//------------------------------------------------------------------------------

    public void setFrame( Frame frame ) 
    {
        if ( frameMenuBar != null ) 
            frame.setMenuBar( frameMenuBar );

        frame.addWindowListener( listener );

        this.frame = frame;
    }

    public Frame getFrame()
    {
        return frame;
    }

//------------------------------------------------------------------------------
//    Data R/W methods   
//------------------------------------------------------------------------------

	public InputStream getStreamFromFile(File file) {
	    InputStream is=null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return is;
	}
	
	public InputStream getStreamFromName(String name) {
		InputStream is = null;
		try {
			is = systemMgr.getInputStream(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return is;
		
	}
    
    public void readExperimentStream(InputStream is) {
		if (is == null) return;
		System.out.println("Debug: Read experiment:");
		try {
			DataElement data = DataMgr.readXMLElement(is);
			System.out.println("Debug: data name="+data.getName());
			if (!data.getName().equals("jacob"))
				throw new DataParseException("experiment must begin with" + " jacob field");

			DataElement pdata = data.getOptElement("properties");
			System.out.println("Debug: pdata name="+pdata.getName());
			if (pdata != null)
				propertyMgr.readData(pdata);
			scene.readData(data.getElement("scene"));
			is.close();
			System.out.println("Debug: End read:");
		}
		catch (IOException ex) {
			SystemMgr.error("can't load experiment: io error " + "(" + ex.getMessage() + ")");
		} catch (DataParseException ex) {
			SystemMgr.error("can't load experiment: data parse error " + "(" + ex.getMessage() + ")");
			try {
				is.close();
			} catch (IOException ex2) {
			}
		}
	}
    
	
	public InputStream getStreamFromFileName(String fileName) {
	    InputStream is;
		try {
			 is = systemMgr.getInputStream( fileName );
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return is;	
	}
    

    public void writeExperiment( String file )
    {
        OutputStream os = systemMgr.getOutputStream( file );

        if ( os == null )
            return;
 
        try
        {
            DataElement data = DataMgr.createXMLElement( "jacob" );
            propertyMgr.writeExpSpecData( data );
            scene.writeData( data );
            DataMgr.writeElement( os, data );   
            os.close();
        }
        catch ( IOException ex )
        {
            SystemMgr.error( "can't save experiment: io error " +
                             "(" + ex.getMessage() + ")" );
        }
    }

    public void writeParticles( String file )
    {
        OutputStream os = systemMgr.getOutputStream( file );

        if ( os == null )
            return;
 
        try
        {
            DataElement data = DataMgr.createPDElement();
            scene.writeData( data );
            DataMgr.writeElement( os, data );   
            os.close();
        }
        catch ( IOException ex )
        {
            SystemMgr.error( "can't save particles: io error " +
                             "(" + ex.getMessage() + ")" );
        }
    }
    
    public void readExperiment(){
    	if(isJS) {
    		readExperimentJS();  // uses async JavaScript dialog
    	}else {
    		readExperimentJava();// uses regular Java dialog
    	}
    }
    
	public void readExperimentJS(){
		AsyncFileChooser fc = new AsyncFileChooser();
		fc.showOpenDialog(MainPanel.this, new Runnable() {

			@Override
			public void run() {
				File file = fc.getSelectedFile();
				System.out.println("FileChooser returned " + file.length() + " bytes for " + file);
				InputStream is = getStreamFromFile(file);
				readExperimentStream(is);
			}
			
		}, null);
	}

    public void readExperimentJava() {
        if ( frame == null ) return;
        //FIXME: localize this
        FileDialog loadDialog = new FileDialog( MainPanel.this.frame, "Load",
                                                FileDialog.LOAD );
        loadDialog.show();
        String dir  = loadDialog.getDirectory();
        String file = loadDialog.getFile();
        
        if ( file == null || dir == null ) return;
        InputStream is=getStreamFromName( dir + file );
        readExperimentStream(is);
    }

    public void writeExperiment()
    {
        if ( frame == null ) return;
//FIXME: localize this
        FileDialog saveDialog = new FileDialog( MainPanel.this.frame, "Save",
                                                FileDialog.SAVE );
        saveDialog.show();
        String dir  = saveDialog.getDirectory();
        String file = saveDialog.getFile();
        
        if ( file == null || dir == null ) return;
        writeExperiment( dir + file );
    }

    public void writeParticles()
    {
        if ( frame == null ) return;
//FIXME: localize this
        FileDialog saveDialog = new FileDialog( MainPanel.this.frame, "Save",
                                                FileDialog.SAVE );
        saveDialog.show();
        String dir  = saveDialog.getDirectory();
        String file = saveDialog.getFile();
        
        if ( file == null || dir == null ) return;
        writeParticles( dir + file );
    }

//FIXME
    public void readScript()
    {
        if ( frame == null ) return;
//FIXME: localize this
        FileDialog loadDialog = new FileDialog( MainPanel.this.frame, "Load",
                                                FileDialog.LOAD );
        loadDialog.show();
        String dir  = loadDialog.getDirectory();
        String file = loadDialog.getFile();
 
        if ( file == null || dir == null ) return;
        new JSScriptRun( systemMgr, dir + file, 
                         new JInterface( systemMgr, this ) );
    }

//------------------------------------------------------------------------------
//    Event Listener 
//------------------------------------------------------------------------------

    private class Listener extends AWTEventAdapter
        implements PropertyObserver
    {
        public Listener() {} 

        public void actionPerformed( ActionEvent e )
        {
            String action = e.getActionCommand().trim();

            int i = action.indexOf( ":" );

            if ( i == -1 )
            {
                localAction( action );
            }
            else
            {
                String group = action.substring( 0, i );
                String gaction = action.substring( i+1 );

                try
                {
                    if ( group.equals( "Scene" ) )
                        scene.setAction( gaction );
                    else
                        SystemMgr.error( "unknown action: " + action );
                }
                catch ( MalformedActionException ex )
                {
                    SystemMgr.error( "malformed action: " + action +
                                     " (" + ex.getMessage() + ")" );
                }
                catch ( UnknownActionException ex )
                {
                    SystemMgr.error( "unknown action: " + ex.getMessage() );
                }
            }
        }

        private void localAction( String action )
        {
            if ( action.equals( "FileNew" ) )
            {  
                scene.newScene();
            }
            else if ( action.equals( "FileSave" ) )
            { 
                writeExperiment();
            }
            else if ( action.equals( "FileExportParticles" ) )
            { 
                writeParticles();
            }
            else if ( action.equals( "FileOpen" ) )
            {
                readExperiment();  
            }
            else if ( action.equals( "FileOpenScript" ) )
            {
                readScript();  
            }
            else if ( action.equals( "FileExit" ) )
            {
                systemMgr.exit( 0 );
            }
            else if ( action.equals( "EditProperties" ) )
            {
                if ( propertyEditor == null )
                    propertyEditor = new PropertyEditor( systemMgr );
                propertyEditor.setVisible( true ); 
            }
            else
            {
                SystemMgr.error( "unknown action: " + action );
            }
        }

        public void windowClosing( WindowEvent e ) 
        {
            systemMgr.exit( 0 );
        }

        public void updateProperty( UpdatePropertyEvent pe ) 
        {
//REMIND: move integrator change into Scene.java
            if ( pe.getProperties().integratorType.hasChanged() )
            {
                scene.setIntegrator( 
                    pe.getProperties().integratorType.getValue() );
            }

            if ( pe.getProperties().doubleBuff.hasChanged() )
                doubleBuff.setBuffer( 
                    pe.getProperties().doubleBuff.getValue() );

            if ( pe.getProperties().showControls.hasChanged() )
            {
                if ( pe.getProperties().showControls.getValue() )
                    MainPanel.this.add( "East", MainPanel.this.controlPanel );
                else
                    MainPanel.this.remove( MainPanel.this.controlPanel );

                MainPanel.this.validate();
            }

            if ( pe.getProperties().showGadgets.hasChanged() )
            {
                if ( pe.getProperties().showGadgets.getValue() )
                {
                    if ( gadgetFrame == null )
                        gadgetFrame = new GadgetFrame( systemMgr, 
                                                       MainPanel.this );
                    gadgetFrame.setVisible( true );
                }
                else
                {
                    if ( gadgetFrame != null ) 
                        gadgetFrame.setVisible( false );
                }
            }       
        }
    }
}
