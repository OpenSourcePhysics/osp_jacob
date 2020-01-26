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

import java.awt.Frame;
import java.awt.Panel;
import java.awt.MenuBar;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.opensourcephysics.controls.XML;
import org.opensourcephysics.controls.XMLControl;
import org.opensourcephysics.display.OSPRuntime;

import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import jacob.system.*;
import javajs.async.AsyncFileChooser;
import jacob.property.*;
import jacob.scene.editor.*;
import jacob.scene.*;
import jacob.lib.*;
import jacob.data.*;
import jacob.gadgets.*;

public class MainPanel extends Panel 
{
	static public boolean isJS = /** @j2sNative true || */ false;
	
    File currentFCDirectory=null;
	
	private Frame frame = null;
    
    private Panel buttonPanel = new Panel();

    private Panel controlPanel = null;

    private MenuBar frameMenuBar = null;

    private DoubleBufferPanel doubleBuff;

    private Listener listener;

    private Scene scene;  

    private PropertyEditor propertyEditor = null;

    private GadgetFrame gadgetFrame = null;

    private PropertyMgr propertyMgr;

    private SystemMgr systemMgr;
    
    static public Button runBtn=null;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

    public MainPanel( SystemMgr systemMgr )
    {
    	if(systemMgr==null)return;
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
        initButtonPanel();
        add("South",buttonPanel);
        systemMgr.getPropertyMgr().attachObserver( listener );
    }
    
    private void initButtonPanel() {
    	buttonPanel.setLayout( new FlowLayout() );
        runBtn=new Button("Run");
    	buttonPanel.add(runBtn);
    	//Button testBtn=new Button("Load");
    	//buttonPanel.add(testBtn);
    	
    	runBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(scene.isRunning()) {
              	  runBtn.setLabel("Run");
              	  scene.stop();
            	}else {
            	  runBtn.setLabel("Pause");
            	  scene.start();
            	}
             }
          }		
        );
    	
    	/*
    	testBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Test button action goes here.
             }
          }		
        );*/
    	
    	
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
		try {
			DataElement data = DataMgr.readXMLElement(is);
			if (!data.getName().equals("jacob"))
				throw new DataParseException("experiment must begin with" + " jacob field");

			DataElement pdata = data.getOptElement("properties");
			if (pdata != null)
				propertyMgr.readData(pdata);
			scene.readData(data.getElement("scene"));
			is.close();
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
    
	public String writeExperimentToBuffer( ) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String result="";
        if ( os == null )
            return result;
 
        try
        {
            DataElement data = DataMgr.createXMLElement( "jacob" );
            propertyMgr.writeExpSpecData( data );
            scene.writeData( data );
            DataMgr.writeElement( os, data );
            result+=os;
            os.close();
        }

        catch ( IOException ex )
        {
            SystemMgr.error( "can't save experiment: io error " +
                             "(" + ex.getMessage() + ")" );
        }
        return result;	
	}
    
	public String writeParticlesToBuffer( ) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String result="";
        if ( os == null )
            return result;
 
        try
        {
            DataElement data = DataMgr.createPDElement();
            scene.writeData( data );
            DataMgr.writeElement( os, data );  
            result+=os;
            os.close();
        }

        catch ( IOException ex )
        {
            SystemMgr.error( "can't save experiment: io error " +
                             "(" + ex.getMessage() + ")" );
        }
        return result;	
	}

   
    
	public void readExperiment(){
    	runBtn.setLabel("Run");
    	scene.stop();
        if ( frame == null ) return;	
		//AsyncFileChooser fc = new AsyncFileChooser();
        AsyncFileChooser fc = OSPRuntime.getChooser();  // static Chooser
		if(fc==null) return;
	    String oldTitle = fc.getDialogTitle();
	    fc.setDialogTitle("Read Jacob Experiment");
	    fc.setCurrentDirectory(currentFCDirectory);
		fc.showOpenDialog(MainPanel.this, new Runnable() {

			@Override
			public void run() {
				fc.setDialogTitle(oldTitle);
				File file = fc.getSelectedFile();
				currentFCDirectory=file;
				if ( file==null ) return;
				InputStream is = getStreamFromFile(file);
				readExperimentStream(is);
			}
			
		}, null);
	}

    public void readExperimentURL(String urlStr) {
    	runBtn.setLabel("Run");
    	scene.stop();
        if ( frame == null ) return;
		String baseURI = (/** @j2sNative document.body.baseURI || */ null); //html page that has script
		String path="";
        if(baseURI!=null)path=baseURI.substring(0,baseURI.lastIndexOf('/')+1);
        urlStr=path+urlStr;
        //System.err.println("Debug path+urlStr="+urlStr);
        
        InputStream is;
		try {
			is=new URL(urlStr).openStream();
	        readExperimentStream(is);
		} catch ( IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Gets an XML.ObjectLoader to save and load data for this program.
     *
     * @return the object loader
     */
    public static XML.ObjectLoader getLoader() {
      return new JacobLoader();
    }
    
    public void writeExperiment(boolean partilcesOnly){
        JFileChooser chooser = OSPRuntime.getChooser();
        if(chooser==null) {
           return;
        }
        String oldTitle = chooser.getDialogTitle();
        chooser.setDialogTitle("Save Jacob Experiment");
        chooser.setCurrentDirectory(currentFCDirectory);
        int result = -1;
        try {
        	result = chooser.showSaveDialog(null);
        } catch (Throwable e) {
        	e.printStackTrace();
        }
        chooser.setDialogTitle(oldTitle);
        if(result==JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            currentFCDirectory=file;
            // check to see if file already exists
            org.opensourcephysics.display.OSPRuntime.chooserDir = chooser.getCurrentDirectory().toString();
            String fileName = file.getAbsolutePath();
            // String fileName = XML.getRelativePath(file.getAbsolutePath());
            if((fileName==null)||fileName.trim().equals("")) {
               return;
            }
            int i = fileName.toLowerCase().lastIndexOf(".jco");
            if(i!=fileName.length()-4) {
               fileName += ".jco";
               file = new File(fileName);
            }
            if(/** @j2sNative false && */file.exists()) {
                int selected = JOptionPane.showConfirmDialog(null, "Replace existing "+file.getName()+"?", "Replace File",
                   JOptionPane.YES_NO_CANCEL_OPTION);
                if(selected!=JOptionPane.YES_OPTION) {
                   return;
                }
             }
            String myData="";
            if(partilcesOnly) {
            	myData=writeParticlesToBuffer( );
            }else {
            	myData=writeExperimentToBuffer( );
            }
            XMLControl xml = new JacobControl(this,myData);  
            xml.write(fileName);
        }
    
    }
    

    public void readScript(){   
    	runBtn.setLabel("Run");
    	scene.stop();
		//AsyncFileChooser fc = new AsyncFileChooser();
    	AsyncFileChooser fc = OSPRuntime.getChooser();
		if(fc==null) return;
	    String oldTitle = fc.getDialogTitle();
	    fc.setDialogTitle("Read Jacob JavaScript");
	    fc.setCurrentDirectory(currentFCDirectory);
		fc.showOpenDialog(MainPanel.this, new Runnable() {

			@Override
			public void run() {
				fc.setDialogTitle(oldTitle);
				File file = fc.getSelectedFile();
				currentFCDirectory=file;
				if ( file==null ) return;
				String urlStr=file.getAbsolutePath();
		        if ( urlStr==null ) return;
		        //System.out.println("Debug: Reading: "+urlStr);
		        new JSScriptRun( systemMgr, urlStr, new JInterface( systemMgr, MainPanel.this ) );
			}   
			
		}, null); 	
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
                writeExperiment(false);
            }
            else if ( action.equals( "FileExportParticles" ) )
            { 
            	writeExperiment(true);
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
