#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

int createServer(int port, struct sockaddr_in * sa){
    int sd = socket(PF_INET, SOCK_STREAM, 0);
    if(sd == -1){
        perror("Erreur de création du serveur");
        exit(0);
    }
    int berr = bind(sd, (struct sockaddr *)sa, sizeof(*sa));
    if(berr == -1){
        perror("Erreur bind");
        exit(0);
    }
    int lerr = listen(sd, SOMAXCONN);
    if(lerr == -1){
        perror("Erreur listen");
        exit(0);
    }
    return sd;
}

int createConnection(int serverSocket, struct sockaddr_in * sa){
    int sd = socket(PF_INET, SOCK_STREAM, 0);
    if(sd == -1){
        perror("Erreur de socket");
        exit(0);
    }
    int size = sizeof(*sa);
    int err = accept(serverSocket, (struct sockaddr *)sa, &size);
    if(err == -1){
        perror("Erreur de connection");
        exit(0);
    }
    return err;
}

int sendMessage(int socket, char * query){
    int nb  = send(socket, query, strlen(query)+1, 0);
    if(nb == -1){
        perror("Erreur send");
        exit(0);
    }
    return nb;
}

char * readMess(int sockId){
    char * message = (char *)malloc(sizeof(char));
    char c = 1;
    int i = 0;
    while(c != '\0'){
        recv(sockId, &c, 1, 0);
        *(message + i) = c;
        i++;
        message = realloc(message, ((i+1)*sizeof(char)));
    }
    *(message + i) = '\0';
    return message;
}

char * mirror(char * word){
    char * new  = (char*)malloc(strlen(word)+1);
    int i, j;
    j = 0;
    for(i = strlen(word)-1; i >= 0; i--){
        *(new+j) = *(word+i);
        j++;
    }
    *(new+j) = '\0';
    return new;
}

int main()
{
    struct sockaddr_in sa ;
    sa.sin_family = AF_INET ;
    sa.sin_port = htons(5000) ;
    sa.sin_addr.s_addr = INADDR_ANY ;

    int socket = createServer(5000, &sa);
   while(1 == 1){
    int sclient = createConnection(socket, &sa);
     if(sclient == -1){
        perror("Client refused");
        close(sclient);
    }
    

   
    else{
        char* requete = readMess(sclient);
        printf("Reçu: %s\n", requete);
        sendMessage(sclient, mirror(requete));
    }
    
   } 
}


