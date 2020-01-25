package jacob.main;

import org.opensourcephysics.controls.XML;
import org.opensourcephysics.controls.XMLControl;

/**
 * An xml loader for the GROrbitsApp program.
 *
 * @author W. Christian
 */
public class JacobLoader implements XML.ObjectLoader {
  /**
   * Creates a new GROrbitsApp.
   *
   * @param control XMLControl
   * @return Object
   */
  public Object createObject(XMLControl control) {
    
	jacob.main.MainPanel model = new jacob.main.MainPanel(null);
    return model;
  }
  
  /**
   * Saves the jacob.main.MainPanel's data in the xml control.
   *
   * @param control XMLControl
   * @param obj Object
   */
  public void saveObject(XMLControl control, Object obj) {
    
	jacob.main.MainPanel model = (jacob.main.MainPanel) obj;
    control.setValue("boolean test",true);
    control.setValue("integer test",1);
    control.setValue("String test","a string");
  }
  
  /**
   * Loads the GROrbitsApp with data from the xml control.
   *
   * @param control XMLControl
   * @param obj Object
   * @return Object
   */
  public Object loadObject(XMLControl control, Object obj) {
    
	jacob.main.MainPanel model = (jacob.main.MainPanel) obj;
    // load object not implemented
    return obj;
  }
}
