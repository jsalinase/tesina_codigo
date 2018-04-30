import mysql.connector as mariadb
from textblob import TextBlob
import unicodedata


mariadb_connection = mariadb.connect(user='root', password='1234', database='tesina')
cursor = mariadb_connection.cursor()

cursor.execute("SELECT texto FROM NOTICIAS LIMIT 10")
filas = cursor.fetchall()
for texto in filas:
	tt = texto[0]
	#print unicodedata.normalize('NFKD',texto).encode('ascii','ignore')#
	blob = TextBlob(tt)
	print(blob.sentiment)

mariadb_connection.close()