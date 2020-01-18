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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.PopupMenu;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jacob.main.*;
import jacob.scene.editor.*;
import jacob.geometry.*;
import jacob.system.*;
import javajs.async.SwingJSUtils;
import javajs.async.SwingJSUtils.StateHelper;
import javajs.async.SwingJSUtils.StateMachine;
import jacob.property.*;
import jacob.data.*;
import jacob.integrator.*;

public class Scene extends Component implements Runnable, PropertyObserver, Saveable, StateMachine {
	private static final Object sceneLock = new Object();

	private Thread thread = null;

	private boolean running = false;

	private boolean painted = true;

	private long lastRepaintTime = 0;

	private SceneView sceneView;

	private Integrator integrator = null;

	private SceneEditor sceneEditor;

	private PopupMenu popupMenu = null;

	private SystemMgr systemMgr;

	private PropertyMgr propertyMgr;

//------------------------------------------------------------------------------
//    Constructor Methods
//------------------------------------------------------------------------------

	public Scene(SystemMgr systemMgr) {
		this.systemMgr = systemMgr;
		this.propertyMgr = systemMgr.getPropertyMgr();

		addComponentListener(new Listener());
		sceneView = new SceneView(this);
		systemMgr.getPropertyMgr().attachObserver(this);
		if (getProperties().sceneRun.getValue())
			start();
		sceneEditor = new SceneEditor(this);
	}

//------------------------------------------------------------------------------
//    System & Property Methods
//------------------------------------------------------------------------------

	public SystemMgr getSystemMgr() {
		return systemMgr;
	}

	public PropertyMgr getPropertyMgr() {
		return propertyMgr;
	}

	public Properties getProperties() {
		return propertyMgr.getProperties();
	}

//------------------------------------------------------------------------------
//    Scene Methods
//------------------------------------------------------------------------------

	public final Object getSceneLock() {
		return sceneLock;
	}

	public SceneView getView() {
		return sceneView;
	}

	public SceneEditor getEditor() {
		return sceneEditor;
	}

	public SceneIterator createIterator() {
		return sceneView.createIterator();
	}

	public ElementIterator createElementIterator() {
		return new ElementIterator(sceneView.createIterator());
	}

	public ParticleIterator createParticleIterator() {
		return new ParticleIterator(sceneView.createIterator());
	}

	public boolean isRunning() {
		return running;
	}

	public void newScene() {
		synchronized (getSceneLock()) {
			sceneView.delete();
			sceneView = new SceneView(this);
		}
		redraw();
	}

	public void setIntegrator(String type) {
		synchronized (getSceneLock()) {
			try {
				integrator = IntegratorFactory.createIntegrator(type);
			} catch (UnknownIntegratorException ex) {
				integrator = null;
				SystemMgr.error("unknown integrator type: " + type);
			}
		}
	}

	public void setAction(String action) throws MalformedActionException, UnknownActionException {
		sceneEditor.setAction(action);
		redraw();
	}

	public void setListener(SceneListener listener) {
		sceneEditor.setListener(listener);
		redraw();
	}

	public void setPopupMenu(PopupMenu popup) {
		popupMenu = popup;
		add(popup);
	}

	public PopupMenu getPopupMenu() {
		return popupMenu;
	}

//------------------------------------------------------------------------------
//    Scene Component Methods
//------------------------------------------------------------------------------

	public Element getElement(String name) {
		ElementIterator i = createElementIterator();

		while (!i.isDone()) {
			Element e = i.getCurrentElement();

			if (e.getName().equals(name))
				return e;
			i.next();
		}
		return null;
	}

//REMIND: optimize this - called every time an element is
//        created
	public String getFreeElementName() {
		int maxn = 0;

		ElementIterator i = createElementIterator();

		while (!i.isDone()) {
			String name = i.getCurrentElement().getName();
			if (name.length() > 1 && name.substring(0, 1).equals("E")) {
				try {
					int n = Integer.parseInt(name.substring(1));
					if (n > maxn)
						maxn = n;
				} catch (NumberFormatException ex) {
				}
			}
			i.next();
		}
		return "E" + (maxn + 1);
	}

//------------------------------------------------------------------------------
//    Update Property Methods
//------------------------------------------------------------------------------

	public void updateProperty(UpdatePropertyEvent pe) {
		sceneView.updateProperty(pe);

		if (pe.hasChanged("sceneRun")) {
			if (getProperties().sceneRun.getValue())
				start();
			else
				stop();
		}
		redraw();
	}

//------------------------------------------------------------------------------
//    Paint Methods
//------------------------------------------------------------------------------

	public void paint(Graphics g) {
		if (getProperties().lockedRepaint.getValue()) {
			synchronized (getSceneLock()) {
				paint(new SceneGraphics(this, g));
			}
		} else {
			paint(new SceneGraphics(this, g));
		}
		painted = true;
	}

	public void paint(SceneGraphics sg) {
		sceneView.paintBg(sg);
		sceneEditor.paintBg(sg);
		sceneView.paintFg(sg);
		sceneEditor.paintFg(sg);

		if (getProperties().showGrid.getValue()) {
			double sw = getSize().width / getProperties().sceneScale.getValue();
			double sh = getSize().height / getProperties().sceneScale.getValue();
			double gs = getProperties().gridSize.getValue();

			sg.setColor(getProperties().gridFg.getValue());
			for (double i = gs; i < sw; i += gs) {
				for (double j = gs; j < sh; j += gs) {
					sg.drawLine(i, j, i, j);
				}
			}
		}
	}

	public void redraw() {
		if (!running)
			repaint();
	}

	public void animationRedraw() {
		if (!running)
			animationRedraw0();
	}

	private void animationRedraw0() {
		long BRTime = System.currentTimeMillis() - lastRepaintTime;
		int minBRTime = getProperties().minTimeBetweenRedraw.getValue();

		if (painted && BRTime > minBRTime) {
			painted = false;
			repaint();
			lastRepaintTime = System.currentTimeMillis();
		}
	}

//------------------------------------------------------------------------------
//    Thread Methods
//------------------------------------------------------------------------------

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		running = true;
	}

	public void stop() {
		if (thread != null && thread.isAlive()) {
			thread.stop();
		}
		thread = null;
		running = false;
		redraw();
	}

	public void run() {

		thread.setPriority(Thread.MIN_PRIORITY);
		stateHelper = new SwingJSUtils.StateHelper(this);
		stateHelper.next(STATE_INIT);
//		while (thread != null) {
//			if (integrator != null)
//				integrator.integrate(this);
//
//			animationRedraw0();
//
//			try {
//				int minTime = getProperties().minThreadSleepTime.getValue();
//				int delayTime = getProperties().animationDelay.getValue();
//
//				thread.sleep(minTime + delayTime);
//			} catch (InterruptedException e) {
//			}
//		}
	}

	// javajs.SwingJSUtils.StateMachine interface

	private final static int STATE_INIT = 0;
	private final static int STATE_LOOP = 1;
	private final static int STATE_DONE = 2;

	private StateHelper stateHelper;

	@Override
	public boolean stateLoop() {
		while (thread != null && !thread.isInterrupted() && stateHelper.isAlive()) {
			switch (stateHelper.getState()) {
			default:
			case STATE_INIT:
				stateHelper.setState(STATE_LOOP);
				break;
			case STATE_LOOP:
				if (integrator != null)
					integrator.integrate(this);
				animationRedraw0();
				int minTime = getProperties().minThreadSleepTime.getValue();
				int delayTime = getProperties().animationDelay.getValue();
				stateHelper.sleep(minTime + delayTime);
				return true;
			case STATE_DONE:
				return false;
			}
		}
		return false;
	}

//------------------------------------------------------------------------------
//    Scene Listener Class
//------------------------------------------------------------------------------

	private class Listener extends ComponentAdapter {
		private Scene parent = Scene.this;

		public Listener() {
		}

		public void componentResized(ComponentEvent e) {
			getView().updateShape();
		}
	}

//------------------------------------------------------------------------------
//    Data R/W Methods
//------------------------------------------------------------------------------

	public void readData(DataElement data) throws DataParseException {
		synchronized (getSceneLock()) {
			sceneView.delete();
			sceneView = new SceneView(this);
			sceneView.readData(data.getElement("view"));

//FIXME: solve nghtbrs in comp loading
			SceneIterator iterator = createIterator();
			while (!iterator.isDone()) {
				SceneComponent c = iterator.getCurrentComponent();
				if (c instanceof ConfiningContainer) {
					ConfiningContainer cc = (ConfiningContainer) c;
					cc.updateNeighbors();
				}
				iterator.next();
			}
		}
		redraw();
	}

	public void writeData(DataElement data) {
		DataElement sdata = data.newElement("scene");

		synchronized (getSceneLock()) {
			sceneView.writeData(sdata);
		}
	}
}
