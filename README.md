## Tesina MTI

Aquí se presenta el codigo fuente hecho para el proyecto de Tesina. Consta de 4 proyectos.

### tesina_rawdata

Está el codigo que carga a una base de datos MariaDB los archivos de Noticias y Tweets historicos. 

Las noticias y Tweets históricas se pueden encontrar en:

- [Noticias Historicas](https://github.com/philipperemy/financial-news-dataset)
- Tweets Historicos:
  - [Programa](https://github.com/Jefferson-Henrique/GetOldTweets-python)
  - Ejecución: Ver archivo Twitter_Data.xlsx en el código fuente.

En application.properties se debe indicar la ruta donde están estos archivos para poder cargarlo, tambien se debe configurar allí los datos del motor de base de datos.
	
### tesina_cleanse_load

Está el codigo que permite cargar la data rawdata en la estructura que le permitirá analizar sentimientos al paquete de analisis.
Contiene reglas de limpiado de informacion poco relevante para el analisis de sentimientos.

### tesina_analisis

Está el codigo que permite analizar las noticias en su formato ya limpio a traves de Amazon Comprehend y Core-NLP de Stanford

### tesina_opinionfinder

Está el codigo que usa Opinion Finder para procesar las noticias.

### tesina_python

Está el codigo que permite analizar las noticias en su formato ya limpio a traves de TextBlob y Vader con Python.


### tesina_pricing

Está el codigo que permite calcular el costo del uso de Amazon Comprehend para el set de datos que contienen noticias de interés.

### tesina_hipotesis

Está el código que calcula la variación positiva o negativa de la accion en el periodo de tiempo indicado por las noticias que superan un umbral.