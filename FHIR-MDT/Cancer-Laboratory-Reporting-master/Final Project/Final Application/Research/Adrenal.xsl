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
        });

        var choice = {};

        function select(){
	       var ques = this.id;
	       var ans = $(this).find('option:selected').attr('id');
	       choice.ques = ans;
	    };



        function submit() {
            $('#result').val("in development");
            $('#generated').show();
        }
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
      				<textarea id="result" style="width: 80%; height: 10em" readonly="readonly"></textarea>
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
                <!--<p><xsl:value-of select="prefix/@value"/>&#160;<xsl:value-of select="text/@value"/></p>-->
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
    <textarea  id="{linkId/@value}" rows="2" cols="100"><xsl:value-of select="linkId/@value"/></textarea>
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
    <textarea  id="{linkId/@value}" rows='1' cols="10" style="display:inline-block;" ><xsl:value-of select="linkId/@value"/></textarea>
    <div style="display:inline-block;">
    <xsl:for-each select="item">
            <xsl:call-template name="item_temp">
            </xsl:call-template>
    </xsl:for-each>
    </div>
</xsl:template>

</xsl:stylesheet>