#! python2
import mysql.connector as mariadb
import unicodedata
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

mariadb_connection = mariadb.connect(user='root', password='1234', database='tesina')
cursor = mariadb_connection.cursor()
cursorInsert = mariadb_connection.cursor()
cursor.execute("SELECT a.id,b.texto from stocks a inner join Noticias b on a.id=b.id")
id_motor="4"
analyser = SentimentIntensityAnalyzer()
filas = cursor.fetchall()
i=0
for texto in filas:
	tt = texto[1]
	snt = analyser.polarity_scores(tt)
	sentimi=''
	sql = "INSERT INTO Sentimientos(id_motor, puntaje_mixto, puntaje_positivo, puntaje_negativo,puntaje_neutral,sentimiento, noticia) values (" + id_motor +", " + str(snt['compound'])  +", " + str(snt['pos']) + ","+ str(snt['neg']) + ","+ str(snt['neu']) + ",'"+ sentimi +"',"+ str(texto[0])+")"
	cursorInsert.execute(sql)
	i=i+1
	if (i%1000==0):
		mariadb_connection.commit()
if (i>0):
	mariadb_connection.commit()
mariadb_connection.close()