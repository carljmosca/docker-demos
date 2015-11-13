docker run -d -e MOVIEDB_API_KEY=my-key -p 80:8080 --link tmdb-db:mysql --name=tmdb carljmosca/tmdb-demo
