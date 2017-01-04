<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fhir="http://hl7.org/fhir"    >


<xsl:template match="/">


    <html>

    <script src="//cdnjs.cloudflare.com/ajax/libs/processing.js/1.4.1/processing-api.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>

	<script>
    	$(function () {
			$('#submitButton').click(submit);
            $('#copyButton').click(selectText);
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
           $('div.change').each(check);
	    };

	    function check(){
	    	var ques = this.getAttribute('data-Ques');
	    	var ans = this.getAttribute('data-Ans');
	    	if(code[ques] == ans.toString()){
	    		this.style.display = "inline";
	    	} else{this.style.display = "none";};
	    };

        function getTextArea(){
            var id = this.id.toString();
            var value = this.value.toString();
            textValue[id] = value;
            
        };

        function submit() {
        	
        	for (var v in code) {
        		document.getElementById(v.concat("code")).textContent = code[v];
        		showNodes(v.concat("div"));
        	};
            for (var v in value) {
            	document.getElementById(v.concat("value")).textContent = value[v];
        	};
            for (var v in textValue) {
            	if(textValue[v] == ""){
            	}else{            	
            	document.getElementById(v.concat("tvalue")).textContent = textValue[v];
            	showNodes(v.concat("div"));
            	};
            };
                     
            $('#copyButton').show();
            

        };

        function showNodes(id){
        	var node = document.getElementById(id);
        	node.style.display = "inline";
        	while(node.parentNode){
        		node = node.parentNode;  
        		node.style.display = "inline";
        		if(node.id.toString() == "generated")  {break;};   		
        	};

        };

        function selectText(containerid) {
            if (document.selection) {
                var range = document.body.createTextRange();
                range.moveToElementText(document.getElementById("generated"));
                range.select();
            } else if (window.getSelection) {
                var range = document.createRange();
                range.selectNode(document.getElementById("generated"));
                window.getSelection().addRange(range);
            }
        };



    
    </script>

	    <body>
            <div style="width:90%;color:navy;background-color:F5F1F0;">
                <h1>
                    <center><xsl:value-of select="fhir:Questionnaire/fhir:title/@value"></xsl:value-of></center>
                </h1>
                
                <table align="right">
                <tr align="left">
                	<th><xsl:value-of select="name(fhir:Questionnaire/fhir:version)"></xsl:value-of></th>
                	<td><xsl:value-of select="fhir:Questionnaire/fhir:version/@value"></xsl:value-of></td>
                </tr>
                <tr align="left">
                    <th><xsl:value-of select="name(fhir:Questionnaire/fhir:status)"></xsl:value-of></th>
                    <td><xsl:value-of select="fhir:Questionnaire/fhir:status/@value"></xsl:value-of></td>
                </tr>
                <tr align="left">
                	<th><xsl:value-of select="name(fhir:Questionnaire/fhir:publisher)"></xsl:value-of></th>
                    <td><xsl:value-of select="fhir:Questionnaire/fhir:publisher/@value"></xsl:value-of></td>
                </tr>
                </table>
               <br/>
               <br/>
               <br/>

                <xsl:for-each select="fhir:Questionnaire/fhir:item">
                	<xsl:call-template name="item_temp">
                	</xsl:call-template>                			
                </xsl:for-each>
            </div>
     			<br/>
    			<button id="submitButton" style="width:45%;bborder-radius:8px;font-size: 20px;background-color: #008CBA;">Submit Form</button>
                <button id="copyButton" style="display:none;width:45%;bborder-radius:8px;font-size: 20px;background-color: #008CBA;">Copy FHIR Questionnaire Response</button>
                
    			<br/>
    			<div id="generated" style="display:none;">
                    <pre><xsl:call-template name="copy">
                	</xsl:call-template></pre>   
                </div>
        </body>
    </html>
</xsl:template>

<xsl:template name="item_temp" >
	<xsl:choose>
        <xsl:when test="fhir:enableWhen">
			<div class="change" data-Ques="{fhir:enableWhen/fhir:question/@value}" data-Ans="{fhir:enableWhen/fhir:answerCoding/fhir:code/@value}" style="display:none">
                <xsl:call-template name="choose_temp">
                </xsl:call-template>
            </div>
        </xsl:when>
        <xsl:otherwise>
            <div id="{fhir:linkId/@value}">
                <xsl:call-template name="choose_temp">
        		</xsl:call-template>
            </div>	
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template name="choose_temp" >
	
	<xsl:choose>
		<xsl:when test="fhir:type/@value='group'">
            <xsl:call-template name="group_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='text'">
            <xsl:call-template name="text_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='choice'">
            <xsl:call-template name="choice_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='string'">
            <xsl:call-template name="text_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='display'">
            <p><xsl:value-of select="fhir:text/@value"/></p>
        </xsl:when>
        <xsl:when test="fhir:type/@value='date'">
            <xsl:call-template name="date_temp">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='integer'">
            <xsl:call-template name="integer_temp">
            </xsl:call-template>
        </xsl:when>
    </xsl:choose>
	
</xsl:template> 

<xsl:template name="group_temp">
	
        <h2 style="background-color:#9C9B9A">              
            <xsl:value-of select="fhir:prefix/@value"/>&#160;<xsl:value-of select="fhir:text/@value"/>
        </h2>
    
		<xsl:for-each select="fhir:item">
        	<xsl:call-template name="item_temp">
        	</xsl:call-template>
    	</xsl:for-each>	
</xsl:template>

<xsl:template name="text_temp">
	<h3>
        <xsl:value-of select="fhir:prefix/@value"/>&#160;<xsl:value-of select="fhir:text/@value"/>
    </h3>
    <textarea  id="{fhir:linkId/@value}" rows="2" cols="100"></textarea>
</xsl:template>

<xsl:template name="choice_temp">
	<h3>
        <xsl:value-of select="fhir:prefix/@value"/>&#160;<xsl:value-of select="fhir:text/@value"/>
    </h3>
	<select id="{fhir:linkId/@value}">
        <xsl:for-each select="fhir:option">
            <option id="{fhir:valueCoding/fhir:code/@value}">
                <xsl:value-of select="fhir:valueCoding/fhir:display/@value"></xsl:value-of>
            </option>
        </xsl:for-each>
    </select>
    <xsl:for-each select="fhir:item">
    	
        	<xsl:call-template name="item_temp">
        	</xsl:call-template>
    	
    </xsl:for-each>
</xsl:template>

<xsl:template name="date_temp">
	<xsl:for-each select="fhir:item">
        	<xsl:call-template name="item_temp">
        	</xsl:call-template>
 	</xsl:for-each>
</xsl:template>

<xsl:template name="integer_temp">
    <h3><xsl:value-of select="fhir:prefix/@value"/>&#160;<xsl:value-of select="fhir:text/@value"/></h3>
    <textarea  id="{fhir:linkId/@value}" rows='1' cols="10" style="display:inline-block;" ></textarea>
    <div style="display:inline-block;">
    <xsl:for-each select="fhir:item">
            <xsl:call-template name="item_temp">
            </xsl:call-template>
    </xsl:for-each>
    </div>
</xsl:template>

<xsl:template name="copy">
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
<xsl:for-each select="fhir:Questionnaire/fhir:item"><xsl:call-template name="item_temp2"></xsl:call-template></xsl:for-each>
&lt;/item&gt;
&lt;/QuestionnaireResponse&gt;    
</xsl:template>

<xsl:template name="item_temp2" >
    <xsl:choose>
        <xsl:when test="fhir:type/@value='group'">
            <xsl:call-template name="group_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='text'">
            <xsl:call-template name="text_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='choice'">
            <xsl:call-template name="choice_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='string'">
            <xsl:call-template name="text_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='date'">
            <xsl:call-template name="date_temp2">
            </xsl:call-template>
        </xsl:when>
        <xsl:when test="fhir:type/@value='integer'">
            <xsl:call-template name="text_temp2">
            </xsl:call-template>
        </xsl:when>
    </xsl:choose>
</xsl:template> 

<xsl:template name="group_temp2">
<div id="{fhir:linkId/@value}div" style="display:none">
&lt;item&gt;       
&lt;linkId value="<xsl:value-of select="fhir:linkId/@value"/>"/&gt;
<xsl:if test="fhir:text">&lt;text value="<xsl:value-of select="fhir:text/@value"/>"/&gt;</xsl:if>   
        <xsl:for-each select="fhir:item">
            <xsl:call-template name="item_temp2">
            </xsl:call-template>
        </xsl:for-each>   
&lt;/item&gt;</div>
</xsl:template>

<xsl:template name="text_temp2">
<div id="{fhir:linkId/@value}div" style="display:none">
&lt;item&gt;
&lt;linkId value="<xsl:value-of select="fhir:linkId/@value"/>"/&gt;
<xsl:if test="fhir:text">&lt;text value="<xsl:value-of select="fhir:text/@value"/>"/&gt;</xsl:if> 
&lt;answer&gt;
&lt;valueString value="<span  id="{fhir:linkId/@value}tvalue"></span>"/&gt;
&lt;/answer&gt;  
&lt;/item&gt;</div> 
</xsl:template>

<xsl:template name="choice_temp2">
<div id="{fhir:linkId/@value}div" style="display:none">
&lt;item&gt;
&lt;linkId value="<xsl:value-of select="fhir:linkId/@value"/>"/&gt;
<xsl:if test="fhir:text">&lt;text value="<xsl:value-of select="fhir:text/@value"/>"/&gt;</xsl:if> 
&lt;answer&gt;
&lt;valueCoding&gt;
&lt;code value="<span  id="{fhir:linkId/@value}code" ></span>"/&gt;
&lt;display value="<span  id="{fhir:linkId/@value}value" ></span>"/&gt;
&lt;/valueCoding&gt;
&lt;/answer&gt;
&lt;/item&gt;</div>
	<xsl:for-each select="fhir:item">
        <xsl:call-template name="item_temp2">
        </xsl:call-template>
    </xsl:for-each>    
</xsl:template>

<xsl:template name="date_temp2">
	
    <xsl:for-each select="fhir:item">
            <xsl:call-template name="item_temp2">
            </xsl:call-template>
    </xsl:for-each>
	
</xsl:template>


</xsl:stylesheet>