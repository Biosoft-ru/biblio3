<#macro _sqlMacro><#assign nested><#nested></#assign>${project.addSQLMacro(nested)}</#macro>

<@_sqlMacro>
MACRO PROJECT_COL(column)
'<sql>
  SELECT DISTINCT CONCAT(p2p.projectID, ': ', column) FROM publication2project p2p
  <if parameter="_cat_">
    LEFT JOIN classifications pCls ON pCls.recordID = CONCAT('projectCategory.', '<parameter:_cat_ />')
    INNER JOIN categories pCat ON (pCat.id = pCls.categoryID AND pCat.name = p2p.projectID )
            OR <parameter:_cat_ /> = (SELECT id FROM categories WHERE name = 'Root')
  </if>
  WHERE p2p.projectID IN (<session:biostore_projects multiple="true"/>)
    AND p2p.publicationID = <var:___ID/>
</sql>'
END
</@>
