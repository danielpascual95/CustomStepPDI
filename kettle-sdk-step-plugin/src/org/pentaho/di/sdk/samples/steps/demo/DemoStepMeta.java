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

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;


@Step(	
		id = "GeneratorCharts",
		image = "org/pentaho/di/sdk/samples/steps/demo/resources/icono.png",
		i18nPackageName="org.pentaho.di.sdk.samples.steps.demo",
		name="GraficaStep.Name",
		description = "GraficaStep.TooltipDesc",
		categoryDescription="i18n:org.pentaho.di.trans.step:BaseStep.Category.Transform"
)
public class DemoStepMeta extends BaseStepMeta implements StepMetaInterface {

	/**
	 *	The PKG member is used when looking up internationalized strings.
	 *	The properties file with localized keys is expected to reside in 
	 *	{the package of the class specified}/messages/messages_{locale}.properties   
	 */

	private static Class<?> PKG = DemoStepMeta.class; // for i18n purposes
	
	/**
	 * Almacenamiento de los indicadores, descripcion y ruta  
	 */
	private String outputField;
	private String Timeparam;
	private String Deviceparam;
	private String Indicator1;
	private String Indicator2;
	private String Indicator3;
	private String Indicator4;
	private String Indicator5;
	private String Descripcion;
	private String Devicename;
	private String RouteFileName;

	/**
	 * Constructor should call super() to make sure the base class has a chance to initialize properly.
	 */
	public DemoStepMeta() {
		super(); 
	}
	
	/**
	 * Called by Spoon to get a new instance of the SWT dialog for the step.
	 * A standard implementation passing the arguments to the constructor of the step dialog is recommended.
	 * 
	 * @param shell		an SWT Shell
	 * @param meta 		description of the step 
	 * @param transMeta	description of the the transformation 
	 * @param name		the name of the step
	 * @return 			new instance of a dialog for this step 
	 */
	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new DemoStepDialog(shell, meta, transMeta, name);
	}

	/**
	 * Called by PDI to get a new instance of the step implementation. 
	 * A standard implementation passing the arguments to the constructor of the step class is recommended.
	 * 
	 * @param stepMeta				description of the step
	 * @param stepDataInterface		instance of a step data class
	 * @param cnr					copy number
	 * @param transMeta				description of the transformation
	 * @param disp					runtime implementation of the transformation
	 * @return						the new instance of a step implementation 
	 */
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
		return new GraficaStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	/**
	 * Called by PDI to get a new instance of the step data class.
	 */
	public StepDataInterface getStepData() {
		return new DemoStepData();
	}	

	/**
	 * This method is called every time a new step is created and should allocate/set the step configuration
	 * to sensible defaults. The values set here will be used by Spoon when a new step is created.    
	 */
	public void setDefault() {
		outputField = "Introducir descripción";
	}
	

	public String getOutputField() {
		return outputField;
	}
	
	
	
	public String getParamTime(){
		return Timeparam;
	}	
	
	
	
	public void setParamTime(String Timeparam) {
		this.Timeparam=Timeparam;
	}
	
	
	public String getParamDevice(){
		return Deviceparam;
	}	
	
	public void setParamDevice(String Deviceparam) {
		if(Deviceparam != null)
		this.Deviceparam=Deviceparam; 
		else
		this.Deviceparam = " ";
	}

	public String getIndicator1(){
		return Indicator1;
	}	
	
	public void setIndicator1(String Indicator1) {
		if(Indicator1 != null)
		this.Indicator1=Indicator1; 
		else
		this.Indicator1 = " ";
	}
	
	public String getIndicator2(){
		return Indicator2;
	}	
	
	public void setIndicator2(String Indicator2) {
		if(Indicator2 != null)
		this.Indicator2=Indicator2; 
		else
		this.Indicator2 = " ";
	}
	
	public String getIndicator3(){
		return Indicator3;
	}	
	
	public void setIndicator3(String Indicator3) {
		if(Indicator3 != null)
		this.Indicator3=Indicator3; 
			else
			this.Indicator3 = " ";
	}
	
	public String getIndicator4(){
		return Indicator4;
	}	
	
	public void setIndicator4(String Indicator4) {
		if(Indicator4 != null)
			this.Indicator4=Indicator4; 
				else
				this.Indicator4 = " "; 
	}
	
	public String getIndicator5(){
		return Indicator5;
	}	
	
	public void setIndicator5(String Indicator5) {
		if(Indicator5 != null)
			this.Indicator5=Indicator5; 
				else
				this.Indicator5 = " "; 
	}
	

	public void setDescripcion(String descp) {
		
		this.Descripcion=descp;
	}
	
	
	public String getDescripcion() {
		
		return Descripcion;
	}
	
	public void setDevicename(String name) {
		
		this.Devicename=name;
	}
	
	public String getDevicename() {
		
		return Devicename;
	}
	
	public String getRouteFileName() {
		
		return RouteFileName;
	
	}
	
	public void setRouteFileName(String RouteFileName) {
		
		this.RouteFileName=RouteFileName;
	}

	public void setOutputField(String outputField) {
		this.outputField = outputField;
	}
	
	/**
	 * This method is used when a step is duplicated in Spoon. It needs to return a deep copy of this
	 * step meta object. Be sure to create proper deep copies if the step configuration is stored in
	 * modifiable objects.
	 * 
	 * See org.pentaho.di.trans.steps.rowgenerator.RowGeneratorMeta.clone() for an example on creating
	 * a deep copy.
	 * 
	 * @return a deep copy of this
	 */
	public Object clone() {
		Object retval = super.clone();
		return retval;
	}
	
	/**
	 * This method is called by Spoon when a step needs to serialize its configuration to XML. The expected
	 * return value is an XML fragment consisting of one or more XML tags.  
	 * 
	 * Please use org.pentaho.di.core.xml.XMLHandler to conveniently generate the XML.
	 * 
	 * @return a string containing the XML serialization of this step
	 */
	public String getXML() throws KettleValueException {
		
		String xml = XMLHandler.addTagValue("outputfield", outputField);
		xml = xml + " " + XMLHandler.addTagValue("Timeparam", Timeparam);
		xml = xml + " " + XMLHandler.addTagValue("Deviceparam", Deviceparam);
		xml = xml + " " + XMLHandler.addTagValue("Indicator1", Indicator1);
		xml = xml + " " + XMLHandler.addTagValue("Indicator2", Indicator2);
		xml = xml + " " + XMLHandler.addTagValue("Indicator3", Indicator3);
		xml = xml + " " + XMLHandler.addTagValue("Indicator4", Indicator4);
		xml = xml + " " + XMLHandler.addTagValue("Indicator5", Indicator5);
		xml = xml + " " + XMLHandler.addTagValue("Descripcion", Descripcion);
		xml = xml + " " + XMLHandler.addTagValue("Devicename", Devicename);
		xml = xml + " " + XMLHandler.addTagValue("RouteFileName", RouteFileName);
		return xml;
	}

	/**
	 * This method is called by PDI when a step needs to load its configuration from XML.
	 * 
	 * Please use org.pentaho.di.core.xml.XMLHandler to conveniently read from the
	 * XML node passed in.
	 * 
	 * @param stepnode	the XML node containing the configuration
	 * @param databases	the databases available in the transformation
	 * @param metaStore the metaStore to optionally read from
	 */
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {

		try {
			outputField = XMLHandler.getTagValue(stepnode, "outputField");
			Timeparam = XMLHandler.getTagValue(stepnode, "Timeparam");
			Deviceparam = XMLHandler.getTagValue(stepnode, "Deviceparam");
			Indicator1 = XMLHandler.getTagValue(stepnode, "Indicator1");
			Indicator2 = XMLHandler.getTagValue(stepnode, "Indicator2");
			Indicator3 = XMLHandler.getTagValue(stepnode, "Indicator3");
			Indicator4 = XMLHandler.getTagValue(stepnode, "Indicator4");
			Indicator5 = XMLHandler.getTagValue(stepnode, "Indicator5");
			Descripcion = XMLHandler.getTagValue(stepnode, "Descripcion");
			Devicename = XMLHandler.getTagValue(stepnode, "Devicename");
			RouteFileName = XMLHandler.getTagValue(stepnode, "RouteFileName");
		} catch (Exception e) {
			throw new KettleXMLException("Demo plugin unable to read step info from XML node", e);
		}

	}	
	/**
	 * This method is called by Spoon when a step needs to serialize its configuration to a repository.
	 * The repository implementation provides the necessary methods to save the step attributes.
	 *
	 * @param rep					the repository to save to
	 * @param metaStore				the metaStore to optionally write to
	 * @param id_transformation		the id to use for the transformation when saving
	 * @param id_step				the id to use for the step  when saving
	 */
	public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step) throws KettleException
	{
		try{
			rep.saveStepAttribute(id_transformation, id_step, "outputfield", outputField); //$NON-NLS-1$
		}
		catch(Exception e){
			throw new KettleException("Unable to save step into repository: "+id_step, e); 
		}
	}		
	
	/**
	 * This method is called by PDI when a step needs to read its configuration from a repository.
	 * The repository implementation provides the necessary methods to read the step attributes.
	 * 
	 * @param rep		the repository to read from

	 * 
	 * 
	 * @param metaStore	the metaStore to optionally read from
	 * @param id_step	the id of the step being read
	 * @param databases	the databases available in the transformation
	 * @param counters	the counters available in the transformation
	 */
	public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases) throws KettleException  {
		try{
			outputField  = rep.getStepAttributeString(id_step, "outputfield"); //$NON-NLS-1$
		}
		catch(Exception e){
			throw new KettleException("Unable to load step from repository", e);
		}
	}

	/**
	 * This method is called to determine the changes the step is making to the row-stream.
	 * To that end a RowMetaInterface object is passed in, containing the row-stream structure as it is when entering
	 * the step. This method must apply any changes the step makes to the row stream. Usually a step adds fields to the
	 * row-stream.
	 * 
	 * @param inputRowMeta		the row structure coming in to the step
	 * @param name 				the name of the step making the changes
	 * @param info				row structures of any info steps coming in
	 * @param nextStep			the description of a step this step is passing rows to
	 * @param space				the variable space for resolving variables
	 * @param repository		the repository instance optionally read from
	 * @param metaStore			the metaStore to optionally read from
	 */
	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space, Repository repository, 
						  IMetaStore metaStore) throws KettleStepException{		
		
		/*
		 * This implementation appends the outputField to the row-stream
		 */

		// a value meta object contains the meta data for a field
		ValueMetaInterface v = new ValueMetaString(outputField);
		
		// setting trim type to "both"
		v.setTrimType(ValueMetaInterface.TRIM_TYPE_BOTH);
		
		// the name of the step that adds this field
		v.setOrigin(name);
	  	
		// modify the row structure and add the field this step generates  
		inputRowMeta.addValueMeta(v);
	
	}

	/**
	 * This method is called when the user selects the "Verify Transformation" option in Spoon. 
	 * A list of remarks is passed in that this method should add to. Each remark is a comment, warning, error, or ok.
	 * The method should perform as many checks as necessary to catch design-time errors.
	 * 
	 * Typical checks include:
	 * - verify that all mandatory configuration is given
	 * - verify that the step receives any input, unless it's a row generating step
	 * - verify that the step does not receive any input if it does not take them into account
	 * - verify that the step finds fields it relies on in the row-stream
	 * 
	 *   @param remarks		the list of remarks to append to
	 *   @param transmeta	the description of the transformation
	 *   @param stepMeta	the description of the step
	 *   @param prev		the structure of the incoming row-stream
	 *   @param input		names of steps sending input to the step
	 *   @param output		names of steps this step is sending output to
	 *   @param info		fields coming in from info steps 
	 *   @param metaStore	metaStore to optionally read from
	 */
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info, VariableSpace space, Repository repository, IMetaStore metaStore)  {
		
		CheckResult cr;
	
		// See if there are input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, BaseMessages.getString(PKG, "Demo.CheckResult.ReceivingRows.OK"), stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(PKG, "Demo.CheckResult.ReceivingRows.ERROR"), stepMeta);
			remarks.add(cr);
		}	
    	
	}
	
	
	
	public void modifyFile(String filePath, String oldString, String newString)
    {
        File fileToBeModified = new File(filePath);
         
        String oldContent = "";
         
        BufferedReader reader = null;
         
        FileWriter writer = null;
         
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
             
            //Reading all the lines of input text file into oldContent
             
            String line = reader.readLine();
             
            while (line != null) 
            {
                oldContent = oldContent + line + System.lineSeparator();
                 
                line = reader.readLine();
            }
             
            //Replacing oldString with newString in the oldContent
             
            String newContent = oldContent.replaceAll(oldString, newString);
             
            //Rewriting the input text file with newContent
             
            writer = new FileWriter(fileToBeModified);
             
            writer.write(newContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources
                 
                reader.close();
                 
                writer.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }


}
