<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/">
        <html>
<style>
table {
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 100%;
}

td, th {
    border: 1px solid #dddddd;
    text-align: left;
    padding: 8px;
}

tr:nth-child(even) {
    background-color: #dddddd;
}
</style>
            <body>
                <h1>
                    <xsl:value-of select="Questionnaire/title/@value"></xsl:value-of>
                </h1>
                <table>
                <tr>
                <th><xsl:value-of select="name(Questionnaire/version)"></xsl:value-of></th>
                <th><xsl:value-of select="name(Questionnaire/status)"></xsl:value-of></th>
                <th><xsl:value-of select="name(Questionnaire/publisher)"></xsl:value-of></th>
                </tr>
                <tr>
                    <td><xsl:value-of select="Questionnaire/version/@value"></xsl:value-of></td>
                    <td><xsl:value-of select="Questionnaire/status/@value"></xsl:value-of></td>
                    <td><xsl:value-of select="Questionnaire/publisher/@value"></xsl:value-of></td>
                </tr>
                
                
                </table>
                <xsl:for-each select="Questionnaire/item">
                    <table>
                    <tr>
                    <th>
                    <h3>                                          
                        <xsl:value-of select="linkId/@value"></xsl:value-of>: 
                        <xsl:value-of select="text/@value"></xsl:value-of>
                    </h3>
                    </th>
                    </tr>
                    <tr>
                    <td>
                    <div style="display: inline-block;">
                        <xsl:choose>
                            <xsl:when test="type/@value='group'">
                                <xsl:for-each select="item">
                                    <h4>
                                                                      
                                        <xsl:value-of select="linkId/@value"></xsl:value-of>: 
                                        <xsl:value-of select="text/@value"></xsl:value-of>
                                    </h4>
                                    <xsl:choose>
                                        <xsl:when test="type/@value='text'">
                                            <textarea rows="4" cols="50"></textarea>
                                        </xsl:when>
                                        <xsl:when test="type/@value='choice'">
                                            <select>
                                                <xsl:for-each select="option">
                                                    <option>
                                                        <xsl:value-of select="valueCoding/display/@value"></xsl:value-of>
                                                    </option>
                                                </xsl:for-each>
                                            </select>
                                        </xsl:when>
                                        <xsl:when test="type/@value='group'">
                                            <xsl:for-each select="item">
                                                
                                                <h4>
                                                                                           
                                                    <xsl:value-of select="linkId/@value"></xsl:value-of>: 
                                                    <xsl:value-of select="text/@value"></xsl:value-of>
                                                </h4>
                                                <xsl:choose>
                                                    <xsl:when test="type/@value='text'">
                                                        <textarea rows="4" cols="50"></textarea>
                                                    </xsl:when>
                                                    <xsl:when test="type/@value='choice'">
                                                        <select>
                                                            <xsl:for-each select="option">
                                                                <option>
                                                                    <xsl:value-of select="valueCoding/display/@value"></xsl:value-of>
                                                                </option>
                                                            </xsl:for-each>
                                                        </select>
                                                    </xsl:when>
                                                    <xsl:when test="type/@value='display'">
                                                        <p>
                                                            <xsl:value-of select="text/@value"></xsl:value-of>
                                                        </p>
                                                    </xsl:when>
                                                </xsl:choose>
                                            </xsl:for-each>
                                        </xsl:when>
                                        <xsl:when test="type/@value='display'">
                                            <p>
                                                <xsl:value-of select="linkId/@value"></xsl:value-of>:<xsl:value-of select="text/@value"></xsl:value-of>
                                            </p>
                                        </xsl:when>
                                    </xsl:choose>
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:when test="type/@value='display'">
                                <p>
                                    <xsl:value-of select="text/@value"></xsl:value-of>
                                </p>
                            </xsl:when>
                        </xsl:choose>
                    </div>
                </td>
                </tr>
                </table>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>