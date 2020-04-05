/*! ******************************************************************************
*
* Pentaho Data Integration
*
* Copyright (C) 2002-2013 by Pentaho : http://www.pentaho.com
*
*******************************************************************************
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
******************************************************************************/

package org.pentaho.di.sdk.samples.steps.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
//import org.eclipse.swt.widgets.
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import javafx.stage.FileChooser;

import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComponent;



/**
 * This class is part of the demo step plug-in implementation.
 * It demonstrates the basics of developing a plug-in step for PDI. 
 * 
 * The demo step adds a new string field to the row stream and sets its
 * value to "Hello World!". The user may select the name of the new field.
 *   
 * This class is the implementation of StepDialogInterface.
 * Classes implementing this interface need to:
 * 
 * - build and open a SWT dialog displaying the step's settings (stored in the step's meta object)
 * - write back any changes the user makes to the step's meta object
 * - report whether the user changed any settings when confirming the dialog 
 * 
 */
public class DemoStepDialog extends BaseStepDialog implements StepDialogInterface {

	/**
	 *	The PKG member is used when looking up internationalized strings.
	 *	The properties file with localized keys is expected to reside in 
	 *	{the package of the class specified}/messages/messages_{locale}.properties   
	 */
	private static Class<?> PKG = DemoStepMeta.class; // for i18n purposes

	// this is the object the stores the step's settings
	// the dialog reads the settings from it when opening
	// the dialog writes the settings to it when confirmed 
	private DemoStepMeta meta;

	// text field holding the name of the field to add to the row stream
	private Text wHelloFieldName;
	private Text wFieldRoute;
	private Text wDeviceFieldName;
	private Combo wStepFieldTime;
	private Combo wStepFieldDevice;
	private Combo Indicator1;
	private Combo Indicator2;
	private Combo Indicator3;
	private Combo Indicator4;
	private Combo Indicator5;
	Button wSAVE;
	Combo wStepnamecombo;
	Label wlFieldDevice;
	Label wlFieldInd1;
	Label wlFieldInd2;
	Label wlFieldInd3;
	Label wlFieldInd4;
	Label wlFieldInd5;
	Label wlabeldevice;
	Label wlSave;
	/**
	 * The constructor should simply invoke super() and save the incoming meta
	 * object to a local variable, so it can conveniently read and write settings
	 * from/to it.
	 * 
	 * @param parent 	the SWT shell to open the dialog in
	 * @param in		the meta object holding the step's settings
	 * @param transMeta	transformation description
	 * @param sname		the step name
	 */
	public DemoStepDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
		super(parent, (BaseStepMeta) in, transMeta, sname);
		meta = (DemoStepMeta) in;
	}

	/**
	 * This method is called by Spoon when the user opens the settings dialog of the step.
	 * It should open the dialog and return only once the dialog has been closed by the user.
	 * 
	 * If the user confirms the dialog, the meta object (passed in the constructor) must
	 * be updated to reflect the new step settings. The changed flag of the meta object must 
	 * reflect whether the step configuration was changed by the dialog.
	 * 
	 * If the user cancels the dialog, the meta object must not be updated, and its changed flag
	 * must remain unaltered.
	 * 
	 * The open() method must return the name of the step after the user has confirmed the dialog,
	 * or null if the user cancelled the dialog.
	 */
	public String open() {

		// store some convenient SWT variables 
		Shell parent = getParent();
		Display display = parent.getDisplay();

		// SWT code for preparing the dialog
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
		props.setLook(shell);
		setShellImage(shell, meta);
		
		// Save the value of the changed flag on the meta object. If the user cancels
		// the dialog, it will be restored to this saved value.
		// The "changed" variable is inherited from BaseStepDialog
		changed = meta.hasChanged();
		
		// The ModifyListener used on all controls. It will update the meta object to 
		// indicate that changes are being made.
		ModifyListener lsMod = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				meta.setChanged();
			}
		};
		
		// ------------------------------------------------------- //
		// SWT code for building the actual settings dialog        //
		// ------------------------------------------------------- //
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(BaseMessages.getString(PKG, "GraficaStep.Title")); 

		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// LABEL TIPO GRAFICA
		wlStepname = new Label(shell, SWT.RIGHT); 
		wlStepname.setText(BaseMessages.getString(PKG, "GraficaStep.Tipo")); 
		props.setLook(wlStepname);
		fdlStepname = new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.right = new FormAttachment(middle, -margin);
		fdlStepname.top = new FormAttachment(0, margin);
		wlStepname.setLayoutData(fdlStepname);
		
		// COMBOBOX TIPO GRAFICA
		wStepnamecombo = new Combo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		String [] CHARTS = {"Dual-Y Chart", "Scatter Chart", "Stack bar","Column Chart","Line Chart","Radar Chart","2 Column chart"};
		wStepnamecombo.setItems(CHARTS);
		wStepnamecombo.select(0);
		props.setLook(wStepnamecombo);
		wStepnamecombo.addModifyListener(lsMod);
		fdStepname = new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top = new FormAttachment(0, margin);
		fdStepname.right = new FormAttachment(100, 0);
		wStepnamecombo.setLayoutData(fdStepname);
		
		//BOTON BROWSE
		wSAVE = new Button(shell, SWT.PUSH);
		wSAVE.setText("Browse...");

		//LABEL RUTA FICHERO
		wlSave = new Label(shell, SWT.RIGHT); 
	    props.setLook(wlSave);
		FormData fdSave = new FormData();
		fdSave.left = new FormAttachment(0, 0);
        fdSave.right = new FormAttachment(middle, -margin);
		fdSave.top = new FormAttachment(wStepnamecombo, margin);
		wlSave.setLayoutData(fdSave);
		wlSave.setText("Nombre archivo ");
		
		//TEXT RUTA FICHERO
		wFieldRoute = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wFieldRoute);
		wFieldRoute.addModifyListener(lsMod);
		FormData fdFieldRoute = new FormData();
		fdFieldRoute.left = new FormAttachment(middle, 0);
		fdFieldRoute.right = new FormAttachment(100, -130); 
		fdFieldRoute.top = new FormAttachment(wStepnamecombo, margin);
		wFieldRoute.setLayoutData(fdFieldRoute);
		
		props.setLook(wSAVE);
		FormData fwbttsave = new FormData();
		fwbttsave.left = new FormAttachment(90, 0);
		fwbttsave.right = new FormAttachment(100,0);  
		fwbttsave.top = new FormAttachment(wStepnamecombo, margin);
		wSAVE.setLayoutData(fwbttsave);
		
		
		//evento al hacer click sobre el boton browse
		wSAVE.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent e) {
	    FileDialog fd = new FileDialog(shell,SWT.SAVE);
	        		 fd.setText("import text");
	        		 fd.setFilterExtensions(new String[] {"*.html;*.HTML"});
	        		 fd.setFilterNames(new String[] {"(*.html)"});
	        		 String selected = fd.open();
	        		 if(selected != null)
	        		 {	        MessageBox diag = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
	        		                String route = fd.getFilterPath()+"\\"+fd.getFileName();
	        		                wFieldRoute.setText(route);
	        		                diag.setMessage("a");
	        		 }
	        		 
	     }
	     });


		//LABEL DESCRIPCION
		Label wlValName = new Label(shell, SWT.RIGHT);
		wlValName.setText(BaseMessages.getString(PKG, "GraficaStep.Label"));
		props.setLook(wlValName);
		FormData fdlValName = new FormData();
		fdlValName.left = new FormAttachment(0, 0);
		fdlValName.right = new FormAttachment(middle, -margin);
		fdlValName.top = new FormAttachment(wFieldRoute, margin);
		wlValName.setLayoutData(fdlValName);
				
		// TEXT DESCRIPCION
		wHelloFieldName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wHelloFieldName);
		wHelloFieldName.addModifyListener(lsMod);
		FormData fdValName = new FormData();
		fdValName.left = new FormAttachment(middle, 0);
		fdValName.right = new FormAttachment(100, 0);
		fdValName.top = new FormAttachment(wFieldRoute, margin);
		wHelloFieldName.setLayoutData(fdValName);
		
		// LABEL TIEMPO
		Label wlFieldTime = new Label(shell, SWT.RIGHT); 
		wlFieldTime.setText(BaseMessages.getString(PKG, "GraficaStep.Tiempo")); 
		props.setLook(wlFieldTime);
		FormData fdFieldTime = new FormData();
		fdFieldTime.left = new FormAttachment(0, 0);
		fdFieldTime.right = new FormAttachment(middle, -margin);
		fdFieldTime.top = new FormAttachment(wHelloFieldName, margin);
		wlFieldTime.setLayoutData(fdFieldTime);
		
		//COMBOBOX TIEMPO
		wStepFieldTime = new Combo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		try {
			String[] inputFields=transMeta.getPrevStepFields(stepMeta).getFieldNames();
			for(String name : inputFields) {
				wStepFieldTime.add(name);
			}
		} catch (KettleStepException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		wStepFieldTime.select(0);
		props.setLook(wStepFieldTime);
		wStepFieldTime.addModifyListener(lsMod);
		FormData fdSteFieldTime = new FormData();
		fdSteFieldTime.left = new FormAttachment(middle, 0);
		fdSteFieldTime.top = new FormAttachment(wHelloFieldName, margin);
		fdSteFieldTime.right = new FormAttachment(100,0);
		wStepFieldTime.setLayoutData(fdSteFieldTime);
		
		
		//LABEL LOCALIZACION
		wlFieldDevice = new Label(shell, SWT.RIGHT); 
		wlFieldDevice.setText(BaseMessages.getString(PKG, "GraficaStep.Localizacion")); //label tipo grafica
		//"System.Label.StepName"
		props.setLook(wlFieldDevice);
		FormData fdFieldDevice = new FormData();
		fdFieldDevice.left = new FormAttachment(0, 0);
		fdFieldDevice.right = new FormAttachment(middle, -margin);
		fdFieldDevice.top = new FormAttachment(45, 45);
		wlFieldDevice.setLayoutData(fdFieldDevice);
		
		
		//COMBOBOX LOCALIZACION
		wStepFieldDevice = new Combo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		try {
			String[] inputFields=transMeta.getPrevStepFields(stepMeta).getFieldNames();
			for(String name : inputFields) {
				wStepFieldDevice.add(name);
			}
		} catch (KettleStepException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
		   }
		wStepFieldDevice.select(0);
		props.setLook(wStepFieldDevice);
		wStepFieldDevice.addModifyListener(lsMod);
		FormData fdSteFieldDevice = new FormData();
		fdSteFieldDevice.left = new FormAttachment(middle, 0);
		fdSteFieldDevice.top = new FormAttachment(45,45);
		fdSteFieldDevice.right = new FormAttachment(100, 0);
		wStepFieldDevice.setLayoutData(fdSteFieldDevice);
		
		//LABEL FILTRO LOCALIZACION
		wlabeldevice = new Label(shell, SWT.RIGHT);
		wlabeldevice.setText(BaseMessages.getString(PKG, "GraficaStep.Localizacionesp"));
		props.setLook(wlabeldevice);
		FormData fdlabeldevice = new FormData();
		fdlabeldevice.left = new FormAttachment(0, 0);
		fdlabeldevice.right = new FormAttachment(middle, -margin);
		fdlabeldevice.top = new FormAttachment(50,50);
		wlabeldevice.setLayoutData(fdlabeldevice);
		
		//TEXT FILTRO LOCALIZACION
		wDeviceFieldName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wDeviceFieldName);
		wDeviceFieldName.addModifyListener(lsMod);
		FormData fdValdevice = new FormData();
		fdValdevice.left = new FormAttachment(middle,0);
		fdlabeldevice.right = new FormAttachment(middle, -margin);
		fdValdevice.top = new FormAttachment(50, 50);
		wDeviceFieldName.setLayoutData(fdValdevice);
		
		
		
		
		 //LABEL INDICADOR 1
		Label wlFieldInd1 = new Label(shell, SWT.RIGHT); 
		wlFieldInd1.setText(BaseMessages.getString(PKG, "GraficaStep.Indicador1")); //label tipo grafica
		props.setLook(wlFieldInd1);
		FormData fdFieldInd1 = new FormData();
		fdFieldInd1.left = new FormAttachment(0, 0);
		fdFieldInd1.right = new FormAttachment(middle, -margin);
		fdFieldInd1.top = new FormAttachment(wStepFieldTime, margin);
		wlFieldInd1.setLayoutData(fdFieldInd1);
				
		//COMBOBOX INDICADOR 1
		Indicator1 = new Combo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		try {
			String[] inputFields=transMeta.getPrevStepFields(stepMeta).getFieldNames();
			for(String name : inputFields) {
				Indicator1.add(name);
				}
			} catch (KettleStepException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}
		Indicator1.select(0);
		props.setLook(Indicator1);
		Indicator1.addModifyListener(lsMod);
		FormData fdindicator1 = new FormData();
		fdindicator1.left = new FormAttachment(middle, 0);
		fdindicator1.top = new FormAttachment(wStepFieldTime, margin);
		fdindicator1.right = new FormAttachment(100,0);
		Indicator1.setLayoutData(fdindicator1);
		
		//LABEL INDICADOR 2
		wlFieldInd2 = new Label(shell, SWT.RIGHT); 
		wlFieldInd2.setText(BaseMessages.getString(PKG, "GraficaStep.Indicador2")); //label tipo grafica
		props.setLook(wlFieldInd2);
		FormData fdFieldInd2 = new FormData();
		fdFieldInd2.left = new FormAttachment(0, 0);
		fdFieldInd2.right = new FormAttachment(middle, -margin);
		fdFieldInd2.top = new FormAttachment(Indicator1, margin);
		wlFieldInd2.setLayoutData(fdFieldInd2);
		
		//BOX INDICADOR 2
		Indicator2 = new Combo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		try {
			String[] inputFields=transMeta.getPrevStepFields(stepMeta).getFieldNames();
			for(String name : inputFields) {
				Indicator2.add(name);
				}
			} catch (KettleStepException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}
		Indicator2.select(0);
		props.setLook(Indicator2);
		Indicator2.addModifyListener(lsMod);
		FormData fdindicator2 = new FormData();
		fdindicator2.left = new FormAttachment(middle, 0);
		fdindicator2.top = new FormAttachment(Indicator1, margin);
		fdindicator2.right = new FormAttachment(100,0);
		Indicator2.setLayoutData(fdindicator2);		
				
		//LABEL INDICADOR 3
		wlFieldInd3 = new Label(shell, SWT.RIGHT); 
		wlFieldInd3.setText(BaseMessages.getString(PKG, "GraficaStep.Indicador3")); //label tipo grafica
		props.setLook(wlFieldInd3);
		FormData fdFieldInd3 = new FormData();
		fdFieldInd3.left = new FormAttachment(0, 0);
		fdFieldInd3.right = new FormAttachment(middle, -margin);
		fdFieldInd3.top = new FormAttachment(Indicator2, margin);
		wlFieldInd3.setLayoutData(fdFieldInd3);
						
		//COMBOBOX INDICADOR 3
		Indicator3 = new Combo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		try {
			String[] inputFields=transMeta.getPrevStepFields(stepMeta).getFieldNames();
			for(String name : inputFields) {
				Indicator3.add(name);
				}
			} catch (KettleStepException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}
		Indicator3.select(0);
		props.setLook(Indicator3);
		Indicator3.addModifyListener(lsMod);
		FormData fdindicator3 = new FormData();
		fdindicator3.left = new FormAttachment(middle, 0);
		fdindicator3.top = new FormAttachment(Indicator2, margin);
		fdindicator3.right = new FormAttachment(100,0);
		Indicator3.setLayoutData(fdindicator3);		
				

		//LABEL INDICADOR 4
		wlFieldInd4 = new Label(shell, SWT.RIGHT); 
		wlFieldInd4.setText(BaseMessages.getString(PKG, "GraficaStep.Indicador4")); //label tipo grafica
		props.setLook(wlFieldInd4);
		FormData fdFieldInd4 = new FormData();
		fdFieldInd4.left = new FormAttachment(0, 0);
		fdFieldInd4.right = new FormAttachment(middle, -margin);
		fdFieldInd4.top = new FormAttachment(Indicator3, margin);
		wlFieldInd4.setLayoutData(fdFieldInd4);
							
		//COMBOBOX INDICADOR 4
		Indicator4 = new Combo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		try {
			String[] inputFields=transMeta.getPrevStepFields(stepMeta).getFieldNames();
			for(String name : inputFields) {
				Indicator4.add(name);
				}
			} catch (KettleStepException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}
		Indicator4.select(0);
		props.setLook(Indicator4);
		Indicator4.addModifyListener(lsMod);
		FormData fdindicator4 = new FormData();
		fdindicator4.left = new FormAttachment(middle, 0);
		fdindicator4.top = new FormAttachment(Indicator3, margin);
		fdindicator4.right = new FormAttachment(100,0);
		Indicator4.setLayoutData(fdindicator4);	
		
					
		//LABEL INDICADOR 5
		wlFieldInd5 = new Label(shell, SWT.RIGHT); 
		wlFieldInd5.setText(BaseMessages.getString(PKG, "GraficaStep.Indicador5")); //label tipo grafica
		props.setLook(wlFieldInd5);
		FormData fdFieldInd5 = new FormData();
		fdFieldInd5.left = new FormAttachment(0, 0);
		fdFieldInd5.right = new FormAttachment(middle, -margin);
		fdFieldInd5.top = new FormAttachment(Indicator4, margin);
		wlFieldInd5.setLayoutData(fdFieldInd5);
								
		//COMBOBOX INDICADOR 5
		Indicator5 = new Combo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		try {
			String[] inputFields=transMeta.getPrevStepFields(stepMeta).getFieldNames();
			for(String name : inputFields) {
				Indicator5.add(name);
				}
			} catch (KettleStepException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
				}
		Indicator5.select(0);
		props.setLook(Indicator5);
		Indicator5.addModifyListener(lsMod);
		FormData fdindicator5 = new FormData();
		fdindicator5.left = new FormAttachment(middle, 0);
		fdindicator5.top = new FormAttachment(Indicator4, margin);
		fdindicator5.right = new FormAttachment(100,0);
		Indicator5.setLayoutData(fdindicator5);	
		
		//Visibilidad por defecto para el tipo de grafica (0)
		 Indicator3.setVisible(false);
		 wlFieldInd3.setVisible(false);
		 Indicator4.setVisible(false);
		 wlFieldInd4.setVisible(false);
		 Indicator5.setVisible(false);
		 wlFieldInd5.setVisible(false);
		 wlabeldevice.setVisible(false);
		 wDeviceFieldName.setVisible(false);
		 	
		
		
	    // OK and cancel buttons
	    wOK = new Button(shell, SWT.PUSH);
	    wOK.setText(BaseMessages.getString(PKG, "System.Button.OK")); 
	    wCancel = new Button(shell, SWT.PUSH);
	    wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel")); 
	    BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, wDeviceFieldName);

	    // Add listeners for cancel and OK
	   lsCancel = new Listener() {
    	public void handleEvent(Event e) {cancel();}
		    };
		lsOK = new Listener() {
			public void handleEvent(Event e) {ok();}
		};

		Listener name = new Listener() {
			public void handleEvent(Event e) {setVisible();}
		};
		
	
		
		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener(SWT.Selection, lsOK);
		wStepnamecombo.addListener(SWT.Selection, name);
		
		
		
		// default listener (for hitting "enter")
		lsDef = new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {ok();}
		};
		wStepnamecombo.addSelectionListener(lsDef);
		//wStepname.addSelectionListener(lsDef);
		wHelloFieldName.addSelectionListener(lsDef);

		// Detect X or ALT-F4 or something that kills this window and cancel the dialog properly
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {cancel();}
		});
		
		// Set/Restore the dialog size based on last position on screen
		// The setSize() method is inherited from BaseStepDialog
		setSize();

		// populate the dialog with the values from the meta object
		populateDialog();
		
		// restore the changed flag to original value, as the modify listeners fire during dialog population 
		meta.setChanged(changed);

		// open dialog and enter event loop 
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		

		// at this point the dialog has closed, so either ok() or cancel() have been executed
		// The "stepname" variable is inherited from BaseStepDialog
		return stepname;
	}
	
	/**
	 * This helper method puts the step configuration stored in the meta object
	 * and puts it into the dialog controls.
	 */
	
	
	private void populateDialog() {
		//wStepname.selectAll();
		
		wHelloFieldName.setText(meta.getOutputField());	
		//wStepFieldTime.setText(meta.getParamTime());
	}

	/**
	 * Called when the user cancels the dialog.  
	 */
	private void cancel() {
		// The "stepname" variable will be the return value for the open() method. 
		// Setting to null to indicate that dialog was cancelled.
		stepname = null;
		// Restoring original "changed" flag on the met aobject
		meta.setChanged(changed);
		// close the SWT dialog window
		dispose();
	}
	
	//Visibilidad de los labels segun el tipo de grafica seleccionado.
	private void setVisible() {
		
		
		   if(wStepnamecombo.getSelectionIndex()==6) {
				
			wStepFieldDevice.setVisible(false);
			wlFieldDevice.setVisible(false);
			Indicator2.setVisible(true);
			wlFieldInd2.setVisible(true);
			Indicator3.setVisible(false);
			wlFieldInd3.setVisible(false);
	    	 Indicator4.setVisible(false);
			 wlFieldInd4.setVisible(false);
			 Indicator5.setVisible(false);
			 wlFieldInd5.setVisible(false);
			 wlabeldevice.setVisible(false);
			wDeviceFieldName.setVisible(false);
			}
		
	   if(wStepnamecombo.getSelectionIndex()==5) {
			
			wStepFieldDevice.setVisible(true);
			wlFieldDevice.setVisible(true);
			wlabeldevice.setVisible(true);
			wDeviceFieldName.setVisible(true);
			Indicator2.setVisible(true);
			 wlFieldInd2.setVisible(true);
			 Indicator3.setVisible(true);
			 wlFieldInd3.setVisible(true);
			 Indicator4.setVisible(true);
			 wlFieldInd4.setVisible(true);
			 Indicator5.setVisible(true);
			 wlFieldInd5.setVisible(true);
		}
       if(wStepnamecombo.getSelectionIndex()==4) {
			
			wStepFieldDevice.setVisible(true);
			wlFieldDevice.setVisible(true);
			Indicator2.setVisible(false);
			 wlFieldInd2.setVisible(false);
			 Indicator3.setVisible(false);
			 wlFieldInd3.setVisible(false);
			 Indicator4.setVisible(false);
			 wlFieldInd4.setVisible(false);
			 Indicator5.setVisible(false);
			 wlFieldInd5.setVisible(false);
			 wlabeldevice.setVisible(true);
				wDeviceFieldName.setVisible(true);
			 
		}
		
		if(wStepnamecombo.getSelectionIndex()==3) {
			
			wStepFieldDevice.setVisible(true);
			wlFieldDevice.setVisible(true);
			Indicator2.setVisible(false);
			 wlFieldInd2.setVisible(false);
			 Indicator3.setVisible(false);
			 wlFieldInd3.setVisible(false);
			 Indicator4.setVisible(false);
			 wlFieldInd4.setVisible(false);
			 
			 wlabeldevice.setVisible(true);
				wDeviceFieldName.setVisible(true);
				Indicator5.setVisible(false);
				 wlFieldInd5.setVisible(false);
			 
		}///////////
		
		if(wStepnamecombo.getSelectionIndex()==2) {
			
			wStepFieldDevice.setVisible(false);
			wlFieldDevice.setVisible(false);
			wlabeldevice.setVisible(false);
			wDeviceFieldName.setVisible(false);
			Indicator2.setVisible(true);
			 wlFieldInd2.setVisible(true);
			 Indicator3.setVisible(true);
			 wlFieldInd3.setVisible(true);
			 Indicator4.setVisible(true);
			 wlFieldInd4.setVisible(true);
			 Indicator5.setVisible(false);
			 wlFieldInd5.setVisible(false);
		}///////////
		
		if(wStepnamecombo.getSelectionIndex()==1) {
		
		wStepFieldDevice.setVisible(false);
		wlFieldDevice.setVisible(false);
		wlabeldevice.setVisible(false);
		wDeviceFieldName.setVisible(false);
		Indicator2.setVisible(false);
		 wlFieldInd2.setVisible(false);
		 Indicator3.setVisible(false);
		 wlFieldInd3.setVisible(false);
		 Indicator4.setVisible(false);
		 wlFieldInd4.setVisible(false);
		 Indicator5.setVisible(false);
		 wlFieldInd5.setVisible(false);
	}
	
if(wStepnamecombo.getSelectionIndex()==0) {
	
		wStepFieldDevice.setVisible(true);
		wlFieldDevice.setVisible(true);
		Indicator2.setVisible(true);
		 wlFieldInd2.setVisible(true);
		 Indicator3.setVisible(false);
		 wlFieldInd3.setVisible(false);
		 Indicator4.setVisible(false);
		 wlFieldInd4.setVisible(false);
		 Indicator5.setVisible(false);
		 wlFieldInd5.setVisible(false);
		 wlabeldevice.setVisible(false);
		 wDeviceFieldName.setVisible(false);
		 	
			
}


		
 	
	}
	
	
	
	
	/**
	 * Called when the user confirms the dialog
	 */
	private void ok() {
		// The "stepname" variable will be the return value for the open() method. 
		// Setting to step name from the dialog control
		int num= wStepnamecombo.getSelectionIndex();
		String separador;
		if(num ==0)
			separador="";
		else
			separador="-";
		
		stepname = "Google Charts"+separador+wStepnamecombo.getItem(num);
		// Setting the  settings to the meta object
		meta.setOutputField(wStepnamecombo.getText());
		
		meta.setParamTime(wStepFieldTime.getText());
		meta.setParamDevice(wStepFieldDevice.getText());
		meta.setIndicator1(Indicator1.getText());
		meta.setIndicator2(Indicator2.getText());
		meta.setDescripcion(wHelloFieldName.getText());
		meta.setIndicator3(Indicator3.getText());
		meta.setIndicator4(Indicator4.getText());
		meta.setIndicator5(Indicator5.getText());
		meta.setDevicename(wDeviceFieldName.getText());
		meta.setRouteFileName(wFieldRoute.getText());
		// close the SWT dialog window
		dispose();
	}
}
