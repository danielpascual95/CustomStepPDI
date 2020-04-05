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

import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase.Fields;
import org.pentaho.di.core.row.value.ValueMetaString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.util.*;
import java.util.ArrayList;

/**
 * This class is part of the demo step plug-in implementation.
 * It demonstrates the basics of developing a plug-in step for PDI. 
 * 
 * The demo step adds a new string field to the row stream and sets its
 * value to "Hello World!". The user may select the name of the new field.
 *   
 * This class is the implementation of StepInterface.
 * Classes implementing this interface need to:
 * 
 * - initialize the step
 * - execute the row processing logic
 * - dispose of the step 
 * 
 * Please do not create any local fields in a StepInterface class. Store any
 * information related to the processing logic in the supplied step data interface
 * instead.  
 * 
 */




public class GraficaStep extends BaseStep implements StepInterface {

	//ArrayList para almacenar cada campo de la row en su correspindiente arrayList
	ArrayList<String> lineas = new ArrayList<String>();
	ArrayList<String> Valorestiempo = new ArrayList<String>();
	ArrayList<String> ValoresLocalizacion = new ArrayList<String>();
	ArrayList<String> Valoresind1 = new ArrayList<String>();
	ArrayList<String> Valoresind2 = new ArrayList<String>();
	ArrayList<String> Valoresind3 = new ArrayList<String>();
	ArrayList<String> Valoresind4 = new ArrayList<String>();
	ArrayList<String> Valoresind5 = new ArrayList<String>();
	String date;
	String m3;
	String Ncliente;
	/**
	 * The constructor should simply pass on its arguments to the parent class.
	 * 
	 * @param s 				step description
	 * @param stepDataInterface	step data class
	 * @param c					step copy
	 * @param t					transformation description
	 * @param dis				transformation executing
	 */
	public GraficaStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
		super(s, stepDataInterface, c, t, dis);
	}
	
	

	/**
	 * This method is called by PDI during transformation startup. 
	 * 
	 * It should initialize required for step execution. 
	 * 
	 * The meta and data implementations passed in can safely be cast
	 * to the step's respective implementations. 
	 * 
	 * It is mandatory that super.init() is called to ensure correct behavior.
	 * 
	 * Typical tasks executed here are establishing the connection to a database,
	 * as wall as obtaining resources, like file handles.
	 * 
	 * @param smi 	step meta interface implementation, containing the step settings
	 * @param sdi	step data interface implementation, used to store runtime information
	 * 
	 * @return true if initialization completed successfully, false if there was an error preventing the step from working. 
	 *  
	 */
	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		// Casting to step-specific implementation classes is safe
		DemoStepMeta meta = (DemoStepMeta) smi;
		DemoStepData data = (DemoStepData) sdi;

		return super.init(meta, data);
	}	

	/**
	 * Once the transformation starts executing, the processRow() method is called repeatedly
	 * by PDI for as long as it returns true. To indicate that a step has finished processing rows
	 * this method must call setOutputDone() and return false;
	 * 
	 * Steps which process incoming rows typically call getRow() to read a single row from the
	 * input stream, change or add row content, call putRow() to pass the changed row on 
	 * and return true. If getRow() returns null, no more rows are expected to come in, 
	 * and the processRow() implementation calls setOutputDone() and returns false to
	 * indicate that it is done too.
	 * 
	 * Steps which generate rows typically construct a new row Object[] using a call to
	 * RowDataUtil.allocateRowData(numberOfFields), add row content, and call putRow() to
	 * pass the new row on. Above process may happen in a loop to generate multiple rows,
	 * at the end of which processRow() would call setOutputDone() and return false;
	 * 
	 * @param smi the step meta interface containing the step settings
	 * @param sdi the step data interface that should be used to store
	 * 
	 * @return true to indicate that the function should be called again, false if the step is done
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		// safely cast the step settings (meta) and runtime info (data) to specific implementations 
		DemoStepMeta meta = (DemoStepMeta) smi;
		DemoStepData data = (DemoStepData) sdi;


		String typechart1 = meta.getOutputField();
		String typechart = typechart1;
		String registro;
		
		// get incoming row, getRow() potentially blocks waiting for more rows, returns null if no more rows expected
		Object[] r = getRow(); 

		// if no more rows are expected, indicate step is finished and processRow() should not be called again
		if (r == null){
			setOutputDone();
			return false;
		}

		
		//Variables
		String paramtiempo,paramlocalizacion,paramlocalizacionname; //Tiempo, Localizacion
		String paramind1,paramind2,paramind3,paramind4,paramind5; //Indicadores
		String paramDescripcion,ruta; //Descripcion,ruta File name
		
		//Varibles indices para almacer el indice del campo correspondiente que se quiere guardar.
		int indiceparametroTiempo,indiceparametroLocalizacion;
		int indiceIndicador1,indiceIndicador2,indiceIndicador3,indiceIndicador4,indiceIndicador5;
		File file;
		// the "first" flag is inherited from the base step implementation
		// it is used to guard some processing tasks, like figuring out field indexes
		// in the row structure that only need to be done once
		
		try {
		if (first) {
			first = false;
			// clone the input row structure and place it in our data object
			data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
			 
			// use meta.getFields() to change it, so it reflects the output row structure 
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this, null, null);
		}
		
		//Obtencion de los valores de la row
		paramDescripcion = meta.getDescripcion(); //descripcion
		paramtiempo = meta.getParamTime(); //tiempo
		paramlocalizacion = meta.getParamDevice(); //Device
		paramind1 = meta.getIndicator1(); 
		paramind2 = meta.getIndicator2();
		paramind3 = meta.getIndicator3();
		paramind4 = meta.getIndicator4();
		paramind5 = meta.getIndicator5();
		paramlocalizacionname = meta.getDevicename(); //nombre del filtro.
		ruta = meta.getRouteFileName();  //ruta donde se almacena el fichero.
		
		//Obteniendo el indice del row a partir del primer valor obtenido.
		indiceparametroTiempo =  getInputRowMeta().indexOfValue(paramtiempo);
		indiceparametroLocalizacion = getInputRowMeta().indexOfValue(paramlocalizacion);
		indiceIndicador1 = getInputRowMeta().indexOfValue(paramind1);
		indiceIndicador2 = getInputRowMeta().indexOfValue(paramind2);
		indiceIndicador3 = getInputRowMeta().indexOfValue(paramind3);
		indiceIndicador4 = getInputRowMeta().indexOfValue(paramind4);
		indiceIndicador5 = getInputRowMeta().indexOfValue(paramind5);
		
		//Obtencion del string a partir del indice
		String parametroTiempo = data.outputRowMeta.getString(r, indiceparametroTiempo);
		String parametroLocalizacion = data.outputRowMeta.getString(r, indiceparametroLocalizacion);
		String parametroIndicador1 = data.outputRowMeta.getString(r, indiceIndicador1);
		String parametroIndicador2 = data.outputRowMeta.getString(r, indiceIndicador2);
		String parametroIndicador3 = data.outputRowMeta.getString(r, indiceIndicador3);
		String parametroIndicador4 = data.outputRowMeta.getString(r, indiceIndicador4);
		String parametroIndicador5 = data.outputRowMeta.getString(r, indiceIndicador5);
		
		//Almacenamietno en arraylist para posteriormente imprimir.
		Valorestiempo.add(parametroTiempo);
		ValoresLocalizacion.add(parametroLocalizacion);
		Valoresind1.add(parametroIndicador1);
		Valoresind2.add(parametroIndicador2);
		Valoresind3.add(parametroIndicador3);
		Valoresind4.add(parametroIndicador4);
		Valoresind5.add(parametroIndicador5);
		
		PrintWriter pw;
		int chart= TypeChart(typechart);
		
		 switch (chart) {
         case 1: 
        	 //Obtencion de la row que se esta leyendo
        	 registro = data.outputRowMeta.getString(r);
        	 //Se muestran en el Preview Data cada lectura
        	 Object[] outputRow = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "Lectura row " + registro);
        	 //Creacion del fichero
        	 file = new File(ruta);
        	 //listado de registros
        	 lineas.add(registro);
    	
    		 file.createNewFile();
    		 pw = new PrintWriter(file);
    		 //codigo html
    		 pw.println("\n" + 
    					"\n" + 
    					"<html>\n" + 
    					"  <head>\n" + 
    					"    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" + 
    					"    <script type=\"text/javascript\">\n" + 
    					"      google.charts.load('current', {'packages':['corechart', 'bar']});\n" + 
    					"      google.charts.setOnLoadCallback(drawStuff);\n" + 
    					"\n" + 
    					"      function drawStuff() {\n" + 
    					"\n" + 
    					"        var button = document.getElementById('change-chart');\n" + 
    					"        var chartDiv = document.getElementById('chart_div');\n" + 
    					"\n" + 
    					"        var data = google.visualization.arrayToDataTable([\n" + 
    					"          ['"+paramtiempo+"', '"+paramind1+"', '"+paramind2+"',{ role: 'annotation'}],");
    			
    			//pinta arraylist de registros
    			for(int i = 0; i< lineas.size(); i++) {
    				if(i+1 == lineas.size()) {
    					//pw.println(lineas.get(i));
    					pw.println("['"+Valorestiempo.get(i)+"'," +Valoresind1.get(i)+","+ Valoresind2.get(i)+",'" +ValoresLocalizacion.get(i)+ "']");
    				}
    				else {
    					if(i!=0) {
    						pw.println("['"+Valorestiempo.get(i)+"',"+ Valoresind1.get(i)+","+Valoresind2.get(i)+",'"+ ValoresLocalizacion.get(i)+"'],");
    						//pw.println(lineas.get(i)+",");
    					}
    				}
    			}
    			
    			pw.println(" ]);\n" + 
    					"\n" + 
    					"\n" + 
    					"        var classicOptions = {\n" + 
    					"          width: 900,\n" + 
    					"          series: {\n" + 
    					"            0: {targetAxisIndex: 0, type:'line'},\n" + 
    					"            1: {targetAxisIndex: 1}\n" + 
    					"          },\n" + 
    					"          title: '"+paramDescripcion+"',\n" + 
    					"          vAxes: {\n" + 
    					"            // Adds titles to each axis.\n" + 
    					"            0: {title: '"+ paramind1  +"'},\n" + 
    					"            1: {title: '"+paramind2+"'}\n" + 
    					"          }\n" + 
    					"        };\n" + 
    					"\n" + 
    				
    					"\n" + 
    					"        function drawClassicChart() {\n" + 
    					"          var classicChart = new google.visualization.ColumnChart(chartDiv);\n" + 
    					"          classicChart.draw(data, classicOptions);\n" +
    					"button.innerText = 'Change to Material';\n" + 
    					"          button.onclick = drawMaterialChart;\n"+
    					"        }\n" + 
    					"\n" + 
    					"        drawClassicChart();\n" + 
    					"    };\n" + 
    					"    </script>\n" + 
    					"  </head>\n" + 
    					"  <body>\n" + 
    					
    					"    <br><br>\n" + 
    					"    <div id=\"chart_div\" style=\"width: 800px; height: 500px;\"></div>\n" + 
    					"  </body>\n" + 
    					"</html>");
    			pw.close();
    			
    		//	}
    		putRow(data.outputRowMeta, outputRow); 
                  break;
         case 2:  
        		//Obtencion de la row que se esta leyendo
        	 	registro = data.outputRowMeta.getString(r);
        	 	//Se muestran en el Preview Data cada lectura
        	 	Object[] outputRow2 = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "Lectura row " + registro);
        	 	//listado de registros
        		lineas.add(registro);
     			file = new File(ruta);
     			
     				file.createNewFile();
     				 pw = new PrintWriter(file);
     				//codigo html
     				pw.println(
     					"\n" + 
     					"\n" + 
     					"<html>\n" + 
     					"  <head>\n" + 
     					"    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" + 
     					"    <script type=\"text/javascript\">\n" + 
     					"google.charts.load('current', {'packages':['corechart']}); \n" + 
     					"google.charts.setOnLoadCallback(drawChart); \n" +
     					" function drawChart() { \n"+
     					"   var data = google.visualization.arrayToDataTable(["+
     					"     ['"+paramtiempo+"',"+"'BIEN','MODERADO','PERJUDICIAL','MUY PERJUDICIAL','MUY PERJUDICIAL','DAÑINO','"+  paramind1 +"',{'role': 'style'}"+"], "
     					);
     				
     				//pinta arraylist de registros
     				for(int i = 0; i< lineas.size(); i++) {
     					if(i+1 == lineas.size()) {
     						//pw.println(lineas.get(i));
     						pw.println("['"+Valorestiempo.get(i)+"'," +Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","  +"colores("+Valoresind1.get(i) +")" +   "]");
     					
     					}
     					else {
     						if(i!=0) {
     							pw.println("['"+Valorestiempo.get(i)+"',"+ Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","+Valoresind1.get(i)+","+ "colores("+Valoresind1.get(i) +")"  +"],");
     							//pw.println(lineas.get(i)+",");
     						}
     					}
     				}
     				
     				pw.println( " ]);\n" +
     					  "var options = { \n"+
     					  "        title: '" + paramDescripcion + "', \n"+
     					  "       hAxis: {title: '"+paramtiempo+"', minValue: 0, maxValue: 15}, \n"+
     					  "      vAxis: {title: '"+paramind1+"', minValue: 0, maxValue: 15}, \n"+
     					  "  colors: ['#00FF00', '#FFFF00','#FF8000','#FF0000','#A901DB','#610B21'],\n" + 
     					  "  series: {   6: {\n" + 
     					  "        visibleInLegend: false\n" + 
     					  "    }}\n" + 
     		
     					  "    }; \n"+
     					  "   function colores(data){  \n"+
     		 			  "   if(data > -1 &&  data < 51){\n"+
     		 			  "      return '#00FF00';\n"+
     		 			  "   }\n"+
     		 			  "    if(data > 50 &&  data < 101){\n"+
     		 			  "      return '#FFFF00';\n"+
     		 			  "   }\n"+
     		 			  "   if(data > 100 &&  data < 151){\n"+
     		 			  "      return '#FF8000';\n"+
     		 			  "   }\n"+
     		 			  "    if(data > 150 &&  data < 201){\n"+
     		 			  "      return '#FF0000';\n"+
     		 			  "   }\n"+
     		 			  "   if(data > 200 &&  data < 301){\n"+
     		 					"      return '#A901DB';\n"+
     		 					"   }\n"+
     		 					"   \n"+
     		 					"   else return '#610B21';\n"+
     		 					"    };\n"+
     				    "    var chart = new google.visualization.ScatterChart(document.getElementById('chart_div')); \n"+
     				    "    chart.draw(data, options); \n"+
     					  "  } \n"+
     					  " </script> \n"+
     					  "</head>\n"+
     					  " <body> \n"+
     					  "    <div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>\n" + 
     					  " </body> \n"+
     					  "</html>\n" );
     				pw.close();
     				
     			
     			putRow(data.outputRowMeta, outputRow2); 
                  break;
         case 3:  
        	 	//Obtencion de la row que se esta leyendo
     	 		registro = data.outputRowMeta.getString(r);
     	 		//Se muestran en el Preview Data cada lectura
     	 		Object[] outputRow3 = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "Lectura row " + registro);
     	 		//listado de registros
        		lineas.add(registro);
     	 		file = new File(ruta);
    			
     	 	
    				file.createNewFile();
    				pw = new PrintWriter(file);
    				//codigo html
    				pw.println(
    						"<html>\r\n" + 
    						"   <head>\r\n" + 
    						"      <script type = \"text/javascript\" src = \"https://www.gstatic.com/charts/loader.js\">\r\n" + 
    						"      </script>\r\n" + 
    						"      <script type = \"text/javascript\">\r\n" + 
    						"         google.charts.load('current', {packages: ['corechart']});     \r\n" + 
    						"      </script>\r\n" + 
    						"   </head>\r\n" + 
    						"   \r\n" + 
    						"   <body>\r\n" + 
    						"      <div id = \"container\" style = \"width: 900px; height: 500px;\">\r\n" + 
    						"      </div>\r\n" + 
    						"      <script language = \"JavaScript\">\r\n" + 
    						"         function drawChart() {\r\n" + 
    						"            // Define the chart to be drawn.\r\n" + 
    						"            var data = google.visualization.arrayToDataTable(["+
    						"          ['"+paramtiempo+"', '"+paramind1+"', '"+paramind2+"', '"+paramind3 +"', '"+paramind4+"'],");
    						
    				
    				//pinta arraylist de registros
    				for(int i = 0; i< lineas.size(); i++) {
    					if(i+1 == lineas.size()) {
    						//pw.println(lineas.get(i));
    						pw.println("['"+Valorestiempo.get(i)+"'," +Valoresind1.get(i)+","+Valoresind2.get(i)+","+Valoresind3.get(i)+","+Valoresind4.get(i)+ "]");
    					
    					}
    					else {
    						if(i!=0) {
    						pw.println("['"+Valorestiempo.get(i)+"',"+ Valoresind1.get(i)+","+Valoresind2.get(i)+","+Valoresind3.get(i)+","+Valoresind4.get(i) +"],");
    							//pw.println(lineas.get(i)+",");
    						}
    					}
    				}
    				
    				pw.println( 
    						" ]);\n" +
    						"            var options = {title: '"+paramDescripcion+ "',vAxis: {title: '"+paramtiempo+"' }, hAxis: {title: 'Indicadores'}, isStacked:true};  \r\n" + 
    			            "\r\n" + 
    			            " var chart = new google.visualization.BarChart(document.getElementById('container'));\r\n" + 
    			            " chart.draw(data, options);\r\n" + 
    			            "         }\r\n" + 
    			            "         google.charts.setOnLoadCallback(drawChart);\r\n" + 
    			            "      </script>\r\n" + 
    			            "   </body>\r\n" + 
    			            "</html>" );
    				pw.close();
    				
    			

    			putRow(data.outputRowMeta, outputRow3);
        	 
                  break;
         case 4:  
        	 	//Obtencion de la row que se esta leyendo
  	 			registro = data.outputRowMeta.getString(r);
  	 			//Se muestran en el Preview Data cada lectura
  	 			Object[] outputRow4 = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "Lectura row " + registro);
  	 			//listado de registros
  	    		lineas.add(registro);
  	 			file = new File(ruta);
 			
     		
     			file.createNewFile();
     			pw = new PrintWriter(file);
     			//codigo html
     			pw.println(
     					"<html>\r\n" + 
     					"   <head>\r\n" + 
     					"      <title></title>\r\n" + 
     					"      <script type = \"text/javascript\" src = \"https://www.gstatic.com/charts/loader.js\">\r\n" + 
     					"      </script>\r\n" + 
     					"      <script type = \"text/javascript\">\r\n" + 
     					"         google.charts.load('current', {packages: ['corechart']});     \r\n" + 
     					"      </script>\r\n" + 
     					"   </head>\r\n" + 
     					"   \r\n" + 
     					"   <body>\r\n" + 
     					"      <div id = \"container\" style = \"width: 550px; height: 400px; margin: 0 auto\">\r\n" + 
     					"      </div>\r\n" + 
     					"      <script language = \"JavaScript\">\r\n" + 
     					"         function drawChart() {\r\n" + 
     					"            // Define the chart to be drawn.\r\n" + 
     					"            var data = google.visualization.arrayToDataTable(["+
     					
     					"          ['"+paramtiempo+"', '"+paramind1+"'],");
     			
     			//pinta arraylist de registros
     			for(int i = 0; i< lineas.size(); i++) {
     				if(i+1 == lineas.size()) {
     					//pw.println(lineas.get(i));
     					if(ValoresLocalizacion.get(i).contains(paramlocalizacionname)==true)
     					pw.println("['"+Valorestiempo.get(i)+"'," +Valoresind1.get(i)+ "]");
     				
     				}
     				else {
     					if(i!=0) {
     						if(ValoresLocalizacion.get(i).contains(paramlocalizacionname)==true)
     						pw.println("['"+Valorestiempo.get(i)+"',"+ Valoresind1.get(i)+"],");
     						//pw.println(lineas.get(i)+",");
     					}
     			}
     			}
     			
     			pw.println(" ]);\n" + 
     					" var options = {title: '"+paramDescripcion+"'};  \r\n" + 
     					"\r\n" + 
     					"            // Instantiate and draw the chart.\r\n" + 
     					"            var chart = new google.visualization.ColumnChart(document.getElementById('container'));\r\n" + 
     					"            chart.draw(data, options);\r\n" + 
     					"         }\r\n" + 
     					"         google.charts.setOnLoadCallback(drawChart);\r\n" + 
     					"      </script>\r\n" + 
     					"   </body>\r\n" + 
     					"</html>"
     					
     					
     					);
     			pw.close();
     			
     		
     		
     		
     		putRow(data.outputRowMeta, outputRow4);
                  break;
         case 5:  
        	 	//Obtencion de la row que se esta leyendo
  	 			registro = data.outputRowMeta.getString(r);
  	 			//Se muestran en el Preview Data cada lectura
  	 			Object[] outputRow5 = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "Lectura row " + registro);
  	 			//listado de registros
  	    		lineas.add(registro);
  	 			file = new File(ruta);
  	 		
        		
        
        			file.createNewFile();
        			pw = new PrintWriter(file);
        			//codigo html
        			pw.println(
        					"<html>\r\n" + 
        					"   <head>\r\n" + 
        					"      <title></title>\r\n" + 
        					"      <script type = \"text/javascript\" src = \"https://www.gstatic.com/charts/loader.js\">\r\n" + 
        					"      </script>\r\n" + 
        					"      <script type = \"text/javascript\">\r\n" + 
        					"         google.charts.load('current', {packages: ['corechart']});     \r\n" + 
        					"      </script>\r\n" + 
        					"   </head>\r\n" + 
        					"   \r\n" + 
        					"   <body>\r\n" + 
        					"      <div id = \"container\" style = \"width: 550px; height: 400px; margin: 0 auto\">\r\n" + 
        					"      </div>\r\n" + 
        					"      <script language = \"JavaScript\">\r\n" + 
        					"         function drawChart() {\r\n" + 
        					"            // Define the chart to be drawn.\r\n" + 
        					"            var data = google.visualization.arrayToDataTable(["+
        					
        					"          ['"+paramtiempo+"', '"+paramind1+"'],");
        			
        			//pinta arraylist de registros
        			for(int i = 0; i< lineas.size(); i++) {
        				if(i+1 == lineas.size()) {
        					if(ValoresLocalizacion.get(i).contains(paramlocalizacionname)==true)
        					pw.println("['"+Valorestiempo.get(i)+"'," +Valoresind1.get(i)+ "]");
        				
        				}
        				else {
        					if(i!=0) {
        						if(ValoresLocalizacion.get(i).contains(paramlocalizacionname)==true)
        						pw.println("['"+Valorestiempo.get(i)+"',"+ Valoresind1.get(i)+"],");
        					}
        			}
        			}
        			
        			pw.println(" ]);\n" + 
        					" var options = {title: '"+paramDescripcion+"',type:'line'};  \r\n" + 
        					"\r\n" + 
        					"            // Instantiate and draw the chart.\r\n" + 
        					"            var chart = new google.visualization.ColumnChart(document.getElementById('container'));\r\n" + 
        					"            chart.draw(data, options);\r\n" + 
        					"         }\r\n" + 
        					"         google.charts.setOnLoadCallback(drawChart);\r\n" + 
        					"      </script>\r\n" + 
        					"   </body>\r\n" + 
        					"</html>"
        					
        					
        					);
        			pw.close();
        			
        		
        		putRow(data.outputRowMeta, outputRow5);
                  break;
         case 6:  
        	 	//Obtencion de la row que se esta leyendo
        	 	registro = data.outputRowMeta.getString(r);
        	 	//Se muestran en el Preview Data cada lectura
        	 	Object[] outputRow6 = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "Lectura row " + registro);
        	 	//listado de registros
        		lineas.add(registro);
        	 	file = new File(ruta);
 			
     		
     			file.createNewFile();
     			pw = new PrintWriter(file);
     			//codigo html
     			pw.println("<html>\r\n" + 
     					"<head>\r\n" + 
     					"	<title>My first chart using FusionCharts Suite XT</title>\r\n" + 
     					"	<script type=\"text/javascript\" src=\"https://cdn.fusioncharts.com/fusioncharts/latest/fusioncharts.js\"></script>\r\n" + 
     					"	<script type=\"text/javascript\" src=\"https://cdn.fusioncharts.com/fusioncharts/latest/themes/fusioncharts.theme.fusion.js\"></script>\r\n" + 
     					"	<script type=\"text/javascript\">\r\n" + 
     					"		FusionCharts.ready(function(){\r\n" + 
     					"			var chartObj = new FusionCharts({\r\n" + 
     					"    type: 'radar',\r\n" + 
     					"    renderAt: 'chart-container',\r\n" + 
     					"    width: '600',\r\n" + 
     					"    height: '350',\r\n" + 
     					"    dataFormat: 'json',\r\n" + 
     					"    dataSource: {\r\n" + 
     					"        \"chart\": {\r\n" + 
     					"            caption: '"+ paramDescripcion +"'," +  
     					"            \"numberPreffix\": \"$\",\r\n" + 
     					"            \"theme\": \"fusion\",\r\n" + 
     					"            \"radarfillcolor\": \"#ffffff\",\r\n" + 
     					"        },\r\n" + 
     					"        \"categories\": [{\r\n" + 
     					"            \"category\": [{\r\n" + 
     					"                \"label\": '" +paramind1 + "'\r\n" + 
     					"            }, {\r\n" + 
     					"                \"label\": '"+paramind2+"'\r\n" + 
     					"            }, {\r\n" + 
     					"                \"label\": '"+paramind3+"'\r\n" + 
     					"            }, {\r\n" + 
     					"                \"label\": '"+paramind4+"'\r\n" + 
     					"            }, {\r\n" + 
     					"                \"label\": '"+paramind5+"'\r\n" + 
     					"            }]\r\n" + 
     					"        }],"+
     					"dataset: ["
     					);
     			//pinta arraylist de registros
     			for(int i = 0; i< lineas.size(); i++) {
     				if(i+1 == lineas.size()) {
     					//pw.println(lineas.get(i));
     					if(ValoresLocalizacion.get(i).contains(paramlocalizacionname)==true)
     					pw.println("{\r\n" + 
     					//		"            \"seriesname\": \"User Ratings\",\r\n" + 
     							"            \"data\": [{\r\n" + 
     							"                value:'"+Valoresind1.get(i)+"'" + 
     							"            }, {\r\n" + 
     							"                value:'"+Valoresind2.get(i)+"'" + 
     							"            }, {\r\n" + 
     							"               value:'"+Valoresind3.get(i)+"'" + 
     							"            }, {\r\n" + 
     							"                value:'"+Valoresind4.get(i)+"'" + 
     							"            }, {\r\n" + 
     							"               value:'"+Valoresind5.get(i)+"'" + 
     							"            }]\r\n" + 
     							"        }");
     				
     				}
     				else {
     					if(i!=0) {
     						if(ValoresLocalizacion.get(i).contains(paramlocalizacionname)==true)
     						pw.println("{\r\n" + 
     		//						"            seriesname : '"+ parametroTiempo+"',\r\n" + 
     								"            \"data\": [{\r\n" + 
     								"                value:'"+Valoresind1.get(i)+"'" + 
     								"            }, {\r\n" + 
     								"                value:'"+Valoresind2.get(i)+"'" + 
     								"            }, {\r\n" + 
     								"               value:'"+Valoresind3.get(i) +"'"+ 
     								"            }, {\r\n" + 
     								"                value:'"+Valoresind4.get(i)+"'" + 
     								"            }, {\r\n" + 
     								"               value:'"+Valoresind5.get(i)+"'" + 
     								"            }]\r\n" + 
     								"        },");
     					
     					}
     			}
     			}
     			
     			pw.println( 
     		"]\r\n" + 
     	//	"        }]\r\n" + 
     		"    }\r\n" + 
     		"}\r\n" + 
     		");\r\n" + 
     		"			chartObj.render();\r\n" + 
     		"		});\r\n" + 
     		"	</script>\r\n" + 
     		"	</head>\r\n" + 
     		"	<body>\r\n" + 
     		"		<div id=\"chart-container\">FusionCharts XT will load here!</div>\r\n" + 
     		"	</body>\r\n" + 
     		"</html>"
     					);
     			pw.close();
     			
     		putRow(data.outputRowMeta, outputRow6); 
                  break;
                  
         case 7:  
     	 	//Obtencion de la row que se esta leyendo
	 			registro = data.outputRowMeta.getString(r);
	 			//Se muestran en el Preview Data cada lectura
	 			Object[] outputRow7 = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "Lectura row " + registro);
	 			//listado de registros
	    		lineas.add(registro);
	 			file = new File(ruta);
	
  		 
  			file.createNewFile();
  			pw = new PrintWriter(file);
  			//codigo html
  			pw.println(
  					"<html>\r\n" + 
  					"   <head>\r\n" + 
  					"      <title></title>\r\n" + 
  					"      <script type = \"text/javascript\" src = \"https://www.gstatic.com/charts/loader.js\">\r\n" + 
  					"      </script>\r\n" + 
  					"      <script type = \"text/javascript\">\r\n" + 
  					"         google.charts.load('current', {packages: ['corechart']});     \r\n" + 
  					"      </script>\r\n" + 
  					"   </head>\r\n" + 
  					"   \r\n" + 
  					"   <body>\r\n" + 
  					"      <div id = \"container\" style = \"width: 550px; height: 400px; margin: 0 auto\">\r\n" + 
  					"      </div>\r\n" + 
  					"      <script language = \"JavaScript\">\r\n" + 
  					"         function drawChart() {\r\n" + 
  					"            // Define the chart to be drawn.\r\n" + 
  					"            var data = google.visualization.arrayToDataTable(["+
  					
  					"          ['"+paramtiempo+"', '"+paramind1+"','"+paramind2+"'],");
  			
  			//pinta arraylist de registros
  			for(int i = 0; i< lineas.size(); i++) {
  				if(i+1 == lineas.size()) {
  					//pw.println(lineas.get(i));
  					pw.println("['"+Valorestiempo.get(i)+"'," +Valoresind1.get(i)+","+ Valoresind2.get(i)+ "]");
  				
  				}
  				else {
  					if(i!=0) {
  						pw.println("['"+Valorestiempo.get(i)+"',"+ Valoresind1.get(i)+","+Valoresind2.get(i)+"],");
  						//pw.println(lineas.get(i)+",");
  					}
  			}
  			}
  			
  			pw.println(" ]);\n" + 
  					" var options = {title: '"+paramDescripcion+"'};  \r\n" + 
  					"\r\n" + 
  					"            // Instantiate and draw the chart.\r\n" + 
  					"            var chart = new google.visualization.ColumnChart(document.getElementById('container'));\r\n" + 
  					"            chart.draw(data, options);\r\n" + 
  					"         }\r\n" + 
  					"         google.charts.setOnLoadCallback(drawChart);\r\n" + 
  					"      </script>\r\n" + 
  					"   </body>\r\n" + 
  					"</html>"
  					
  					
  					);
  			pw.close();
  			
  		putRow(data.outputRowMeta, outputRow7);
               break;
         default:
                  break;
     }
	
	
		
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		// put the row to the output row stream

		// log progress if it is time to to so
		if (checkFeedback(getLinesRead())) {
			logBasic("Linenr " + getLinesRead()); // Some basic logging
		}

		// indicate that processRow() should be called again
		return true;
	}

	
	//Metodo para obetener el indice de la grafica seleccionada.
	
	public int TypeChart(String type) {
		int i=0;
		if (type.contains("Dual") == true)
			i=1;
		else
			if (type.contains("Scatter") == true)
				i=2;
			else
				if (type.contains("Stack") == true)
					i=3;
				else
					if(type.contains("2") ==true)
						i=7;
					else
						if(type.contains("Line") == true)
							i=5;
						else
							if(type.contains("Radar") == true)
								i=6;
							else
								if(type.contains("Column") == true)
									i=4;
		return i;
		}
	
	/**
	 * This method is called by PDI once the step is done processing. 
	 * 
	 * The dispose() method is the counterpart to init() and should release any resources
	 * acquired for step execution like file handles or database connections.
	 * 
	 * The meta and data implementations passed in can safely be cast
	 * to the step's respective implementations. 
	 * 
	 * It is mandatory that super.dispose() is called to ensure correct behavior.
	 * 
	 * @param smi 	step meta interface implementation, containing the step settings
	 * @param sdi	step data interface implementation, used to store runtime information
	 */
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {

		// Casting to step-specific implementation classes is safe
		DemoStepMeta meta = (DemoStepMeta) smi;
		DemoStepData data = (DemoStepData) sdi;
		
		super.dispose(meta, data);
		
	}
	
	
	
	
	
	


}