all:pointClient pointServer groupServer groupClient

pointClient:pointClient.c csapp.c csapp.h
	gcc pointClient.c csapp.c csapp.h -o pointClient -lpthread
pointServer:pointServer.c csapp.c csapp.h
	gcc pointServer.c csapp.c csapp.h -lpthread -o pointServer
groupServer:groupServer.c csapp.c csapp.h
	gcc groupServer.c csapp.c csapp.h -lpthread -o groupServer
groupClient:groupClient.c csapp.c csapp.h
	gcc groupClient.c csapp.c csapp.h -lpthread -o groupClient
clean:
	rm pointClient pointServer groupServer groupClient
