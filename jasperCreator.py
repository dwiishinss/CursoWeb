import subprocess
import os
import re

ISQL_PATH = r"C:\Program Files\Firebird\Firebird_3_0\isql.exe"
DATABASE_PATH = r"C:\db\30\INNOVA.DAT"

def run_isql(query: str):
    with open("temp_query.sql", "w") as f:
        f.write(query)
    command = [
        ISQL_PATH,
        "-user", "SYSDBA",
        "-password", "masterkey",
        DATABASE_PATH,
        "-i", "temp_query.sql",
        "-q"
    ]
    result = subprocess.run(command, text=True, capture_output=True)
    os.remove("temp_query.sql")
    return result.stdout

def map_tipo(tipo_num):
    tipos = {
        7: "java.lang.Short",
        8: "java.lang.Integer",
        9: "QUAD",
        10: "java.lang.Double",
        11: "java.lang.Double",
        12: "java.util.Date",
        13: "java.util.Date",
        14: "java.lang.String",
        16: "java.lang.Long",
        27: "java.lang.Double",
        35: "java.util.Date",
        37: "java.lang.String",
        40: "java.lang.String",
        45: "java.lang.String",
        261: "java.lang.String",
    }
    return tipos.get(tipo_num, f"TIPO_DESCONHECIDO_{tipo_num}")

def get_columns_for_table(table_name):
    query_metadados = f"""
    SELECT
      TRIM(rf.RDB$FIELD_NAME) AS NOME_COLUNA,
      f.RDB$FIELD_TYPE
    FROM
      RDB$RELATION_FIELDS rf
    JOIN
      RDB$FIELDS f ON rf.RDB$FIELD_SOURCE = f.RDB$FIELD_NAME
    WHERE
      rf.RDB$RELATION_NAME = '{table_name}'
    ORDER BY
      rf.RDB$FIELD_POSITION;
    """
    output = run_isql(query_metadados)
    linhas = output.splitlines()
    colunas = {}
    for linha in linhas:
        linha = linha.strip()
        if not linha:
            continue
        if linha.startswith('NOME_COLUNA') or linha.startswith('---') or linha.startswith('='):
            continue
        partes = linha.split()
        if len(partes) >= 2:
            if partes[1].isdigit():
                tipo_num = int(partes[1])
                tipo_nome = map_tipo(tipo_num)
                colunas[partes[0]] = tipo_nome
            else:
                continue
    return colunas

def isql_field_find():
    columns = {}
    type = "fields"
    tables = []
    fields = {}
    with open('query.sql', 'r') as arquive:
        line = arquive.readline()
        line = arquive.readline()
        while line != ';':
            if re.search('FROM', line):
                type = "tables"
            if re.search('WHERE', line):
                type = "where"
            if type == "fields":
                search_AS = re.search('AS', line)
                search_DOT = re.search(r'\.', line)
                name = ''
                main_name = ''
                table = ''
                if(search_AS):
                    name = line[search_AS.start()+3:len(line)-(2 if(line[len(line)-2] == ',') else 1)]
                    main_name = line[search_DOT.start()+1:search_AS.start()-1]
                else:
                    name = line[search_DOT.start()+1:len(line)-(2 if(line[len(line)-2] == ',') else 1)]
                    main_name = name
                table = line[0:search_DOT.start()]
                tables.append(table)
                fields.setdefault(table, []).append([name, main_name])
            elif type == "tables":
                search_AS = re.search('AS', line)
                search_init = re.search(' ', line)
                name_coluna = ''
                main_coluna = ''
                if(line[0:search_init.end()-1] == 'FROM'):
                    search_init = re.search('FROM ', line)
                    if(search_AS):
                        name_coluna = line[search_AS.start()+3:len(line)-(2 if(line[len(line)-2] == ',') else 1)]
                        main_coluna = line[search_init.end():search_AS.start()-1]
                    else:
                        name_coluna = line[search_init.end():len(line)-(2 if(line[len(line)-2] == ',') else 1)]
                        main_coluna = name
                elif(line[0:search_init.end()-1] == 'LEFT'):
                    search_init = re.search('LEFT JOIN ', line)
                    if(search_AS):
                        name_coluna = line[search_AS.start()+3:len(line)-(2 if(line[len(line)-2] == ',') else 1)]
                        main_coluna = line[search_init.end():search_AS.start()-1]
                    else:
                        name_coluna = line[search_init.end():len(line)-(2 if(line[len(line)-2] == ',') else 1)]
                        main_coluna = name
                    line = arquive.readline()
                if name_coluna in fields:
                    columns.setdefault(main_coluna, []).extend(fields[name_coluna])
            line = arquive.readline()
    fields = []
    for i in columns:
        results = get_columns_for_table(i)
        for j in columns[i]:
            fields.append([results[j[1]], j[0], i])
    
    return fields

fields = isql_field_find()
fields.append(["java.lang.String", "CONTROLE", "PAD_CONTROLE"])
fields.append(["java.lang.String", "NOME_CONTROLE", "PAD_CONTROLE"])
fields.append(["java.lang.Object", "LOGOMARCA", "PAD_CONTROLE"])

print("Nome do arquivo: ",end="")
name = input()
print("Tipo do arquivo(vertical, horizontal): ",end="")
tipo = input()
print("Número de agrupamentos: ",end="")
groups = int(input())

text_fields = []
percents = []
for i in range(groups+1):
    text_fields.append([])
    label = ''
    percent = -1
    percentTotal = 0
    while label != '-':
        print(f'Label do campo {len(text_fields[i])} do grupo {i}(- para finalizar): ', end='')
        label = input()
        if label != '-':
            print(f'percentual do campo {len(text_fields[i])} do grupo {i}: ', end='')
            percent = int(input())
            percentTotal+=percent

            text_fields[i].append([label, percent])
    percents.append(percentTotal)

name_param = ''
type = ''
params = [["p_agrupamento", "java.lang.Short"]]
while name_param != '-':
    print('Nome do parametro(- para finalizar): ',end='')
    name_param = input()
    if name_param!='-':
        print('Tipo do parametro: ',end='')
        type = input()
        params.append([name_param, type])

width = 595 if(tipo=='vertical') else 842
height = 842 if(tipo=='vertical') else 595

query = """"""
setted = False
with open("query.sql", 'r', encoding='utf-8') as arquive:
    line = arquive.readline()
    query += line
    query += """PAD_CONTROLE.CONTROLE,
PAD_CONTROLE.NOME AS NOME_CONTROLE,
PAD_CONTROLE.LOGOMARCA,
"""
    line = arquive.readline()
    while line != '':
        if ('WHERE' in line or line == ';') and setted == False:
            setted = True
            query += """LEFT JOIN PAD_CONTROLE
ON PAD_CONTROLE.CONTROLE = '0001'
"""
        query += line
        line = arquive.readline()

with open(f'{name}.jrxml', 'w', encoding='utf-8') as arquive:
    arquive.write(f"""<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.8.0.final using JasperReports Library version 6.8.0-2ed8dfabb690ff337a5797129f2cd92902b0c87b  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="teste" pageWidth="{595 if(tipo=='vertical') else 842}" pageHeight="{842 if(tipo=='vertical') else 595}" columnWidth="{555 if(tipo=='vertical') else 802}" {'' if(tipo=='vertical') else 'orientation="Landscape"'} leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="011fb82a-da30-4d7f-bc62-f14ebae6785b">
	<property name="com.jaspersoft.studio.data.defaultdataadapter" value="Innova"/>
	<property name="com.jaspersoft.studio.data.sql.tables" value=""/>""")
	
    for param in params:
        arquive.write(f"""	<parameter name="{param[0]}" class="{param[1]}"/>""")

    arquive.write(f"""<queryString>
		<![CDATA[{query}]]>
	</queryString>""")
	
    for field in fields:
        model = f"""<field name="{field[1]}" class="{field[0]}">
                        <property name="com.jaspersoft.studio.field.label" value="{field[1]}"/>
                        <property name="com.jaspersoft.studio.field.tree.path" value="{field[2]}"/>
                    </field>"""
        arquive.write(model)


    for group in range(groups+1):
        arquive.write(f"""<group name="Group{1}" isReprintHeaderOnEachPage="true">
		<groupHeader>
			<band height="15">
				<printWhenExpression><![CDATA[$P{'{p_agrupamento}'}==0]]></printWhenExpression>""")
        acumulatedTotal = 0
        for i in range(len(text_fields[group])):
            arquive.write(f"""<staticText>
					<reportElement mode="Transparent" x="{acumulatedTotal}" y="0" width="{int((text_fields[group][i][1]/percents[group])*(width-40))}" height="15" backcolor="#EDEDED" uuid="d4fa78cf-ef0e-487a-b3ff-a9c64bb27cb1">
						<property name="com.jaspersoft.studio.unit.x" value="px"/>
					</reportElement>
					<box>
						<topPen lineWidth="0.0" lineStyle="Solid" lineColor="#000000"/>
						<leftPen lineWidth="0.0" lineStyle="Solid" lineColor="#000000"/>
						<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
						<rightPen lineWidth="0.0" lineStyle="Solid" lineColor="#000000"/>
					</box>
					<textElement verticalAlignment="Middle">
						<font isBold="true"/>
						<paragraph leftIndent="3"/>
					</textElement>
					<text><![CDATA[{text_fields[group][i][0]}]]></text>
				</staticText>""")
            acumulatedTotal += int((text_fields[group][i][1]/percents[group])*(width-40))
				
        arquive.write("""</band>
		</groupHeader>
	</group>""")
    
    
    arquive.write(f"""<background>
		<band splitType="Stretch"/>
	</background>
                  <pageHeader>
		<band height="51" splitType="Stretch">
			<rectangle>
				<reportElement x="0" y="9" width="{width-40}" height="39" uuid="0ec213bb-78c9-4fbd-ba19-6f3d1faad573"/>
				<graphicElement>
					<pen lineWidth="0.5"/>
				</graphicElement>
			</rectangle>
			<staticText>
				<reportElement x="{width-120}" y="9" width="37" height="23" uuid="4a5f69df-3762-4f16-9f62-2898e1ea1b74">
					<property name="com.jaspersoft.studio.unit.height" value="pixel"/>
					<property name="com.jaspersoft.studio.unit.width" value="pixel"/>
				</reportElement>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font fontName="Arial" size="8"/>
					<paragraph leftIndent="1"/>
				</textElement>
				<text><![CDATA[Emissão:]]></text>
			</staticText>
			<image vAlign="Middle">
				<reportElement x="14" y="13" width="86" height="31" uuid="66ade4df-5ebd-43b6-9cf1-ab34c9a1ff80"/>
				<imageExpression><![CDATA[$F{'{LOGOMARCA}'}]]></imageExpression>
			</image>
			<textField>
				<reportElement x="100" y="13" width="{width-221}" height="14" uuid="5870d47b-ddbb-427c-8348-f38262ac1691"/>
				<textElement textAlignment="Center" verticalAlignment="Top">
					<font fontName="Arial"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{'{NOME_CONTROLE}'}]]></textFieldExpression>
			</textField>
			<textField pattern="dd/MM/yyyy">
				<reportElement x="{width-82}" y="10" width="42" height="11" uuid="f25eb819-e19c-424c-b89d-334b9731dd6d">
					<property name="com.jaspersoft.studio.unit.height" value="pixel"/>
					<property name="com.jaspersoft.studio.unit.width" value="pixel"/>
				</reportElement>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font fontName="Arial" size="7"/>
				</textElement>
				<textFieldExpression><![CDATA[new Date()]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="{width-120}" y="33" width="37" height="15" uuid="eb594fd5-c6be-4e42-ab00-1eec001c7668">
					<property name="com.jaspersoft.studio.unit.height" value="pixel"/>
				</reportElement>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font fontName="Arial" size="8"/>
					<paragraph leftIndent="1"/>
				</textElement>
				<text><![CDATA[Página:]]></text>
			</staticText>
			<line>
				<reportElement x="{width-121}" y="32" width="81" height="1" uuid="22637a3f-bbe4-4d9f-b398-756edc58ab94">
					<property name="com.jaspersoft.studio.unit.width" value="px"/>
					<property name="com.jaspersoft.studio.unit.x" value="px"/>
				</reportElement>
				<graphicElement>
					<pen lineWidth="0.5"/>
				</graphicElement>
			</line>
			<line>
				<reportElement x="{width-121}" y="10" width="1" height="38" uuid="445bfb3d-0721-4843-9280-99af94d7469c">
					<property name="com.jaspersoft.studio.unit.x" value="px"/>
				</reportElement>
				<graphicElement>
					<pen lineWidth="0.5"/>
				</graphicElement>
			</line>
			<textField>
				<reportElement x="{width-82}" y="35" width="20" height="11" uuid="0f46324c-8ef1-4bea-b852-724a2dc94586">
					<property name="com.jaspersoft.studio.unit.height" value="pixel"/>
					<property name="com.jaspersoft.studio.unit.width" value="pixel"/>
				</reportElement>
				<textElement textAlignment="Right">
					<font fontName="Arial" size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$V{'{PAGE_NUMBER}'} + "/"]]></textFieldExpression>
			</textField>
			<textField evaluationTime="Report">
				<reportElement x="{width-60}" y="35" width="20" height="11" uuid="5abb57f2-a082-439a-ad53-fbaae293bc0b">
					<property name="com.jaspersoft.studio.unit.height" value="pixel"/>
					<property name="com.jaspersoft.studio.unit.width" value="pixel"/>
				</reportElement>
				<textElement textAlignment="Left">
					<font fontName="Arial" size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$V{'{PAGE_NUMBER}'}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement x="100" y="29" width="{width-221}" height="15" uuid="7d351782-b350-4727-b095-7685f0f9a0f1">
					<property name="local_mesure_unitheight" value="pixel"/>
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
				</reportElement>
				<textElement textAlignment="Center">
					<font fontName="Arial"/>
				</textElement>
				<textFieldExpression><![CDATA[""]]></textFieldExpression>
			</textField>
			<textField pattern="HH:mm">
				<reportElement x="{width-82}" y="21" width="42" height="11" uuid="89834070-da42-4973-925b-b465d3f78d93">
					<property name="com.jaspersoft.studio.unit.height" value="pixel"/>
					<property name="com.jaspersoft.studio.unit.width" value="pixel"/>
				</reportElement>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font fontName="Arial" size="7"/>
					<paragraph rightIndent="2"/>
				</textElement>
				<textFieldExpression><![CDATA[new Date()]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="{width-143}" y="0" width="103" height="9" uuid="85f5edd9-4575-4d48-84b0-afc01df917ec">
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
				</reportElement>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font fontName="Arial" size="7"/>
				</textElement>
				<text><![CDATA[Lemon Software LTDA]]></text>
			</staticText>
		</band>
	</pageHeader>
</jasperReport>
""")
    
