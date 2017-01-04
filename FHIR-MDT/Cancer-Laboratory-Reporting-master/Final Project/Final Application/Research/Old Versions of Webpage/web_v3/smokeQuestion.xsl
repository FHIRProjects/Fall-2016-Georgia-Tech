<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="/">
<html>
<script src="//cdnjs.cloudflare.com/ajax/libs/processing.js/1.4.1/processing-api.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>

<script>
$(function () {
    $('#submitButton').click(submit);
});

var template = [].join('\r\n');

function submit() {
    var questions = {
        q1: $('#q1').text(),
        q2: $('#q2').text(),
        q3: $('#q3').text()
    }
    var answers = {
        q1: $('#Q1Answer option:selected').text(),
        q2: $('#Q2Answer option:selected').text(),
        q3: $('#Q3Answer option:selected').text(),
    };

    $('#result').val(questions.q1 + "\n" + answers.q1 + "\n" + questions.q2 + "\n" + answers.q2 + "\n" + questions.q3 + "\n" + answers.q3);
    $('#generated').show();
    

}




</script>
<body>
    <h2>Survey about smoking history!</h2>
    <br/>

    <div>
    	<h3 id="q1"><xsl:value-of select="Q1/@value"/></h3>
        <select id="Q1Answer">
            <option value="a1"><xsl:value-of select="Q1/A1/@value"/></option>
            <option value="a2" selected="selected"><xsl:value-of select="Q1/A2/@value"/></option>
        </select>
        
    </div>
    
    <div>
    	<h3 id="q2"><xsl:value-of select="Q1/Q2/@value"/></h3>
        <select id="Q2Answer">
            <option value="a1"><xsl:value-of select="Q1/Q2/A1/@value"/></option>
            <option value="a2"><xsl:value-of select="Q1/Q2/A2/@value"/></option>
            <option value="a3"><xsl:value-of select="Q1/Q2/A3/@value"/></option>
            <option value="a4"><xsl:value-of select="Q1/Q2/A4/@value"/></option>
            <option value="a5" selected="selected"><xsl:value-of select="Q1/Q2/A5/@value"/></option>
        </select>
    </div>

    <div>
    	<h3 id="q3"><xsl:value-of select="Q1/Q2/Q3/@value"/></h3>
        <select id="Q3Answer">
            <option value="a1"><xsl:value-of select="Q1/Q2/Q3/A1/@value"/></option>
            <option value="a2"><xsl:value-of select="Q1/Q2/Q3/A2/@value"/></option>
            <option value="a3"><xsl:value-of select="Q1/Q2/Q3/A3/@value"/></option>
            <option value="a4" selected="selected"><xsl:value-of select="Q1/Q2/Q3/A4/@value"/></option>
        </select>
    </div>

    
    <br/>
    <button id="submitButton">Submit</button>
    
    <br/>
    <div id="generated" style="display:none">
      <textarea id="result" style="width: 80%; height: 10em" readonly="readonly"></textarea>
    </div>
    

  </body>
  </html>
</xsl:template>



</xsl:stylesheet>