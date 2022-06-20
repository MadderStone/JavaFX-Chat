#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

char ** allClientMessages = NULL;
int nbMessages = 0;

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

int sendMessage(int socket, char * query){
    printf("Sending: %s.\n",query);
    int nb  = send(socket, query, strlen(query)+1, 0);
    if(nb == -1){
        perror("Erreur send");
        exit(0);
    }
    return nb;
}

char * getHistory(int socket){
    printf("HISTORY\n");
    int i, j, cpt;
    char * result = NULL;
    for(i=0;i<nbMessages;i++){
        cpt += strlen(*(allClientMessages+i))+1;
        result = (char*)realloc(result, sizeof(char)*cpt);
        strcat(result, *(allClientMessages+i));
        strcat(result, "\n");
    }
    return result;
}

int saveMessage(char * message){
    nbMessages++;
    allClientMessages = (char **)realloc(allClientMessages, sizeof(char *) * nbMessages);
    *(allClientMessages+(nbMessages-1)) = (char *)malloc(sizeof(char)*(strlen(message)+1));
    *(allClientMessages+(nbMessages-1)) = message;
    //*(allClientMessages+(nbMessages)) = '\0';
    /*
    int i;
    for(i=0;i<nbMessages;i++){
        printf("%s\n", *(allClientMessages+i));
    }*/
}

char * readMess(int sockId){
    char * message = (char *)malloc(sizeof(char));
    char c = 1;
    int i = 0;
    while(c != '\0'){
        message = realloc(message, ((i+1)*sizeof(char)));
        recv(sockId, &c, 1, 0);
        *(message + i) = c;
        i++;
    }
    printf("Received: %s.\n", message);
    if(message[0] == '?'){
        printf("%d\n", strcmp(&message[0], "?"));
        sendMessage(sockId, getHistory(sockId));
        return NULL;
    }
    saveMessage(message);
    sendMessage(sockId, mirror(message));
    return message;
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
        while(1 == 1){
            char* requete = readMess(sclient);
        }
    }
    
   } 
}


