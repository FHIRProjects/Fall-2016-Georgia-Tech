<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">


    <html>

    <script src="//cdnjs.cloudflare.com/ajax/libs/processing.js/1.4.1/processing-api.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>

	<script>
    	$(function () {
			$('#submitButton').click(submit);
			$('select').click(select);
            $('textarea').change(getTextArea);
            $('textarea').trigger('change');
            
    	});

        var code = {};
        var value = {};
        var textValue = {};
        

        function select(){
	       var ques = this.id.toString();
	       var ansCode = $(this).find('option:selected').attr('id').toString();
	       var ansValue = this.value.toString();
	       code[ques] = ansCode;
	       value[ques] = ansValue;
           
	    };

        function getTextArea(){
            var id = this.id.toString();
            var value = this.value.toString();
            textValue[id] = value;
            
        };

        function submit() {
        	for (var v in code) {
        	document.getElementById(v.concat("code")).value = code[v];
            document.getElementById(v.concat("code")).size=code[v].length-2;
        	};
            for (var v in value) {
        	document.getElementById(v.concat("value")).value = value[v];
            document.getElementById(v.concat("value")).size = value[v].length-2;
            };
            for (var v in textValue) {
            document.getElementById(v.concat("tvalue")).value = textValue[v];           
            }            
            $('#generated').show();
        };

    
    </script>

	    <body>
                <h1>
                    <center><xsl:value-of select="Questionnaire/title/@value"></xsl:value-of></center>
                </h1>
                
                <table align="right">
                <tr align="left">
                	<th><xsl:value-of select="name(Questionnaire/version)"></xsl:value-of></th>
                	<td><xsl:value-of select="Questionnaire/version/@value"></xsl:value-of></td>
                </tr>
                <tr align="left">
                    <th><xsl:value-of select="name(Questionnaire/status)"></xsl:value-of></th>
                    <td><xsl:value-of select="Questionnaire/status/@value"></xsl:value-of></td>
                </tr>
                <tr align="left">
                	<th><xsl:value-of select="name(Questionnaire/publisher)"></xsl:value-of></th>
                    <td><xsl:value-of select="Questionnaire/publisher/@value"></xsl:value-of></td>
                </tr>
                </table>
               <br/>
               <br/>
               <br/>

                <xsl:for-each select="Questionnaire/item">
                    <xsl:call-template name="item_temp">
                	</xsl:call-template>
                </xsl:for-each>

     			<br/>
    			<button id="submitButton" style="width:50%;bborder-radius:8px;font-size: 20px;background-color: #008CBA;">Submit</button>
    
    			<br/>
    			<div id="generated" style="display:none">
                	<xsl:call-template name="copy">
                	</xsl:call-template>    
                </div>
        </body>
    </html>
</xsl:template>



<xsl:template name="item_temp" >
	<xsl:choose>
		<xsl:when test="type/@value='group'">
            <xsl:call-template name="group_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='text'">
            <xsl:call-template name="text_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='choice'">
            <xsl:call-template name="choice_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='string'">
            <xsl:call-template name="text_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='display'">
            <p><xsl:value-of select="text/@value"/></p>
        </xsl:when>
        <xsl:when test="type/@value='date'">
            <xsl:call-template name="date_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='integer'">
            <xsl:call-template name="integer_temp">
            </xsl:call-template>
        </xsl:when>
    </xsl:choose>
</xsl:template> 

<xsl:template name="group_temp">
	<div id="{linkId/@value}">
		<div style="background-color:#9C9B9A">
        <h2>              
            <xsl:value-of select="prefix/@value"/>&#160;<xsl:value-of select="text/@value"/>
        </h2>
    	</div>
		<xsl:for-each select="item">
        	<xsl:call-template name="item_temp">
        	</xsl:call-template>
    	</xsl:for-each>
	</div>
</xsl:template>

<xsl:template name="text_temp">
	<h3>
        <xsl:value-of select="prefix/@value"/>&#160;<xsl:value-of select="text/@value"/>
    </h3>
    <textarea  id="{linkId/@value}" rows="2" cols="100"></textarea>
</xsl:template>

<xsl:template name="choice_temp">
	<h3>
        <xsl:value-of select="prefix/@value"/>&#160;<xsl:value-of select="text/@value"/>
    </h3>
	<select id="{linkId/@value}">
        <xsl:for-each select="option">
            <option id="{valueCoding/code/@value}">
                <xsl:value-of select="valueCoding/display/@value"></xsl:value-of>
            </option>
        </xsl:for-each>
    </select>
    <xsl:for-each select="item">
    	
        	<xsl:call-template name="item_temp">
        	</xsl:call-template>
    	
    </xsl:for-each>
</xsl:template>

<xsl:template name="date_temp">
	<xsl:for-each select="item">
        	<xsl:call-template name="item_temp">
        	</xsl:call-template>
 	</xsl:for-each>
</xsl:template>

<xsl:template name="integer_temp">
    <h3><xsl:value-of select="prefix/@value"/>&#160;<xsl:value-of select="text/@value"/></h3>
    <textarea  id="{linkId/@value}" rows='1' cols="10" style="display:inline-block;" ></textarea>
    <div style="display:inline-block;">
    <xsl:for-each select="item">
            <xsl:call-template name="item_temp">
            </xsl:call-template>
    </xsl:for-each>
    </div>
</xsl:template>

<xsl:template name="copy">
	<!--<textarea id="result" style="width: 80%; height: 100em" readonly="readonly">-->
	<pre>
&lt;QuestionnaireResponse xmlns="http://hl7.org/fhir"&gt;
    &lt;identifier&gt;
        &lt;value value="1234567890"/&gt;
    &lt;/identifier&gt;
    &lt;questionnaire&gt;
        &lt;reference value="http://fhirtest.uhn.ca/baseDstu2.1/Questionnaire/39880"/&gt;
    &lt;/questionnaire&gt;
    &lt;status value="completed"/&gt;
    &lt;subject&gt;
        &lt;reference value="http://fhirtest.uhn.ca/baseDstu2.1/Patient/proband"/&gt;
    &lt;/subject&gt;
    &lt;author&gt;
        &lt;reference value="http://fhirtest.uhn.ca/baseDstu2.1/Practitioner/f007"/&gt;
    &lt;/author&gt;
    &lt;authored value="2016-01-08"/&gt;
    &lt;item&gt;
        &lt;linkId value="root"/&gt;            
            <xsl:for-each select="Questionnaire/item">            
                <xsl:call-template name="item_temp2">
                </xsl:call-template>
            </xsl:for-each>
    &lt;/item&gt;
&lt;/QuestionnaireResponse&gt;
    </pre>
</xsl:template>

<xsl:template name="item_temp2" >
        &lt;item&gt;
    <xsl:choose>
        <xsl:when test="type/@value='group'">
            <xsl:call-template name="group_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='text'">
            <xsl:call-template name="text_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='choice'">
            <xsl:call-template name="choice_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='string'">
            <xsl:call-template name="text_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='date'">
            <xsl:call-template name="date_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="type/@value='integer'">
            <xsl:call-template name="integer_temp2">
            </xsl:call-template>
        </xsl:when>
    </xsl:choose>
        &lt;/item&gt;
</xsl:template> 

<xsl:template name="group_temp2">       
            &lt;linkId value="<xsl:value-of select="linkId/@value"/>"/&gt;
            &lt;text value="<xsl:value-of select="text/@value"/>"/&gt;        
        <xsl:for-each select="item">
            <xsl:call-template name="item_temp2">
            </xsl:call-template>
        </xsl:for-each>    
</xsl:template>

<xsl:template name="text_temp2">
            &lt;linkId value="<xsl:value-of select="linkId/@value"/>"/&gt;
            &lt;text value="<xsl:value-of select="text/@value"/>"/&gt;
            &lt;answer&gt;
                &lt;valueString value="<input id="{linkId/@value}tvalue" value="n/a" type="text" style="border:none"/>"/&gt;
            &lt;/answer&gt;    
</xsl:template>

<xsl:template name="choice_temp2">
        &lt;linkId value="<xsl:value-of select="linkId/@value"/>"/&gt;
        &lt;text value="<xsl:value-of select="text/@value"/>"/&gt;
        &lt;answer&gt;
            &lt;valueCoding&gt;
                &lt;code value="<input id="{linkId/@value}code" type="text" value="n/a" style="border:none;"/>"/&gt;
                &lt;display value="<input id="{linkId/@value}value" value="n/a" type="text" style="border:none"/>"/&gt;
            &lt;/valueCoding&gt;
        &lt;/answer&gt;    
    <xsl:for-each select="item">
        <xsl:call-template name="item_temp2">
        </xsl:call-template>
    </xsl:for-each>
</xsl:template>

<xsl:template name="date_temp2">
    <xsl:for-each select="item">
            <xsl:call-template name="item_temp2">
            </xsl:call-template>
    </xsl:for-each>
</xsl:template>

<xsl:template name="integer_temp2">
        &lt;linkId value="<xsl:value-of select="linkId/@value"/>"/&gt;
        &lt;text value="<xsl:value-of select="text/@value"/>"/&gt;
        &lt;answer&gt;
            &lt;valueString value="<input id="{linkId/@value}tvalue" value="n/a" type="text" style="border:none"/>"/&gt;
        &lt;/answer&gt;
</xsl:template>

</xsl:stylesheet>