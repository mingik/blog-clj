# blog-clj

Simple Blog Engine -- blog posts are created first from /newpost and then
displayed on main page. 

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

SQLite database file will be called "db.sq3" and will be created if
needed in the root folder.