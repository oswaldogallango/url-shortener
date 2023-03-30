# url-shortener

Cette application est une petite démo, pour générer des URL raccourcie à partir d'URL complètes. Ces URL sont stockées dans une base de données Mysql.

### Pour générer l'url raccourcie
On fait un appel Post à l'API /shorten-url

POST http://localhost:8080/shorten-url

avec un object JSON
 
    {
       "fullUrl": "http://www.google.com"
    }

dans le corp et on aura une réponse avec ce format:

    {
       "fullUrl": "http://www.google.com",
       "shortUrl": "http://localhost:8080/2OER4oCW"
    }

Si le format de l'URL n'est pas valide, on aura une erreur 400.

    {
       "status": 400,
       "message": "L'URL complète (http://www.google.com? sfgjj) est invalide",
       "timestamp": 1680213700873
    }

### Pour Obtenir l'url complete
On fait une appel Get à l'URL raccourcie situe dans le champ shortUrl

GET http://localhost:8080/2OER4oCW

et on aura une response avec ce format:

    {
       "fullUrl": "http://www.google.com"
    }

Si l'URL raccourcie n' existe pas on obtiendra une erreur 404
    
    {
       "status": 404,
       "message": "Le code d’URL raccourcie (abc123) n'existe pas",
       "timestamp": 1680210355352
    }
