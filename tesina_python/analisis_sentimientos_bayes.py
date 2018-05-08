import mysql.connector as mariadb
from textblob import TextBlob
from textblob.sentiments import NaiveBayesAnalyzer
import unicodedata


mariadb_connection = mariadb.connect(user='root', password='1234', database='tesina')
cursor = mariadb_connection.cursor()
cursorInsert = mariadb_connection.cursor()
cursor.execute("SELECT a.id,b.texto_procesado from Stocks a inner join Noticias b on a.id=b.id")
id_motor="4"
filas = cursor.fetchall()
for texto in filas:
	tt = texto[1]
	blob = TextBlob(tt, analyzer=NaiveBayesAnalyzer())
	
	sql = "INSERT INTO Sentimientos(id_motor, puntaje_positivo, puntaje_negativo,sentimiento, noticia) values (" + id_motor +", " + str(blob.sentiment.p_pos) + ","+ str(blob.sentiment.p_neg) + ",'"+ blob.sentiment.classification +"',"+ str(texto[0])+")"
	cursorInsert.execute(sql)
	
mariadb_connection.commit()
mariadb_connection.close()