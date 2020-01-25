package jacob.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import org.opensourcephysics.controls.Cryptic;
import org.opensourcephysics.controls.OSPLog;
import org.opensourcephysics.controls.XMLControl;
import org.opensourcephysics.controls.XMLControlElement;

public class JacobControl extends XMLControlElement{
	  String myData="<default> </default>";

	  /**
	   * Constructs and loads a control with the specified object.
	   *
	   * @param obj the object.
	   */
	  public JacobControl(Object obj, String data) {
	    super(obj);
	    myData=data;
	    setObjectClass(obj.getClass());
	    saveObject(obj);
	  }
	  
	  /**
	   * Writes this control to a Writer.
	   *
	   * @param out the Writer
	   */
	  public void write(Writer out) {
	    try {
	      output = new BufferedWriter(out);
	      String xml = toXML();
	      // if password-protected, encrypt the xml string and save the cryptic
	      if(getPassword()!=null) {
	        Cryptic cryptic = new Cryptic(xml);
	        XMLControl control = new XMLControlElement(cryptic);
	        xml = control.toXML();
	      }
	      //output.write(xml);
	      output.write(myData);
	      output.flush();
	      output.close();
	    } catch(IOException ex) {
	      OSPLog.info(ex.getMessage());
	    }
	  }
}
