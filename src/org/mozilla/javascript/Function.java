/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * The contents of this file are subject to the Netscape Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/NPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express oqr
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is Netscape
 * Communications Corporation.  Portions created by Netscape are
 * Copyright (C) 1997-1999 Netscape Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s): 
 * Norris Boyd
 *
 * Alternatively, the contents of this file may be used under the
 * terms of the GNU Public License (the "GPL"), in which case the
 * provisions of the GPL are applicable instead of those above.
 * If you wish to allow use of your version of this file only
 * under the terms of the GPL and not to allow others to use your
 * version of this file under the NPL, indicate your decision by
 * deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL.  If you do not delete
 * the provisions above, a recipient may use your version of this
 * file under either the NPL or the GPL.
 */

// API class

package org.mozilla.javascript;

/**
 * This is interface that all functions in JavaScript must implement.
 * The interface provides for calling functions and constructors.
 *
 * @see org.mozilla.javascript.Scriptable
 * @author Norris Boyd
 */

public interface Function extends Scriptable {
    /**
     * Call the function.
     *
     * Note that the array of arguments is not guaranteed to have
     * length greater than 0.
     *
     * @param cx the current Context for this thread
     * @param scope the scope to execute the function relative to. This is
     *              set to the value returned by getParentScope() except
     *              when the function is called from a closure.
     * @param thisObj the JavaScript <code>this</code> object
     * @param args the array of arguments
     * @return the result of the call
     * @exception JavaScriptException if an uncaught exception
     *            occurred while executing the function
     */
    public Object call(Context cx, Scriptable scope, Scriptable thisObj,
                       Object[] args)
        throws JavaScriptException;

    /**
     * Call the function as a constructor.
     *
     * This method is invoked by the runtime in order to satisfy a use
     * of the JavaScript <code>new</code> operator.  This method is
     * expected to create a new object and return it.
     *
     * @param cx the current Context for this thread
     * @param scope an enclosing scope of the caller except
     *              when the function is called from a closure.
     * @param args the array of arguments
     * @return the allocated object
     * @exception JavaScriptException if an uncaught exception
     *            occurred while executing the constructor
     */
    public Scriptable construct(Context cx, Scriptable scope, Object[] args)
        throws JavaScriptException;
}
