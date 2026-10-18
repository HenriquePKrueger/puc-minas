#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>

// =============== STRUCT: Data ===============

typedef struct {
    int ano;
    int mes;
    int dia;
} Data;

// --------------- DataFunctions ---------------

Data parseData(char *s){
    Data date;
    sscanf(s, "%d-%d-%d", &date.ano, &date.mes, &date.dia);

    return date;
}

void formatData(Data d, char *buffer){
    sprintf(buffer, "%02d/%02d/%d", d.dia, d.mes, d.ano);
}

// =============== STRUCT: Veiculo ===============

typedef struct {
    int id;
    char marca[100];
    char modelo[100];
    int ano;
    char categoria[100];
    char combustivel[5][100];
    int cilindros;
    double cilindrada;
    char transmissao[100];
    char tracao[100];
    double consumoCidade;
    double consumoEstrada;
    double co2;
    bool turbo;
    Data dataRegistro;
} Veiculo;

// --------------- Auxiliary VeiculoFunctions ---------------

void parseCombustivel(char *s, Veiculo *vehicle){
    char *fuel = strtok(s, ";");
    int column = 0;

    while(fuel != NULL){
        strcpy(vehicle->combustivel[column], fuel);
        column++;
        fuel = strtok(NULL, ";");
    }
}

// --------------- VeiculoFunctions ---------------

Veiculo *parseVeiculo(char *s){
    Veiculo *vehicle = (Veiculo *)malloc(sizeof(Veiculo));

    char data[20];
    char combustivel[100];
    char turbo[10];

    sscanf(s, "%d, %[^,], %[^,], %d, %[^,], %[^,], %d, %lf, %[^,], %[^,], %lf, %lf, %lf, %[^,], %[^\n]",
        &vehicle->id,
        vehicle->marca,
        vehicle->modelo,
        &vehicle->ano,
        vehicle->categoria,
        combustivel,
        &vehicle->cilindros,
        &vehicle->cilindrada,
        vehicle->transmissao,
        vehicle->tracao,
        &vehicle->consumoCidade,
        &vehicle->consumoEstrada,
        &vehicle->co2,
        turbo,
        data
    );

    vehicle->turbo = strcmp(turbo, "true") == 0;
    vehicle->dataRegistro = parseData(data);
    vehicle->combustivel[0][0] = '\0';

    parseCombustivel(combustivel, vehicle);

    return vehicle;
}

void format(Veiculo *vehicle, char *formatted){
    char combustivel[100];
    combustivel[0] = '\0';

    char data[20];
    int column = 0;

    while(vehicle->combustivel[column][0] != '\0'){
        if (column > 0){
            strcat(combustivel, ",");
        }

        strcat(combustivel, vehicle->combustivel[column]);
        column++;
    }

    formatData(vehicle->dataRegistro, data);

    sprintf(formatted, "[%d ## %s ## %s ## %d ## %s ## [%s] ## %d ## %.1f ## %s ## %s ## %.2f ## %.2f ## %.1f ## %s ## %s]",
        vehicle->id,
        vehicle->marca,
        vehicle->modelo,
        vehicle->ano,
        vehicle->categoria,
        combustivel,
        vehicle->cilindros,
        vehicle->cilindrada,
        vehicle->transmissao,
        vehicle->tracao,
        vehicle->consumoCidade,
        vehicle->consumoEstrada, 
        vehicle->co2,
        vehicle->turbo ? "true" : "false",
        data
    );
}

// --------------- LeitorCsvFunctions ---------------

Veiculo **ler(char *caminhoArquivo){
    Veiculo **veiculos = (Veiculo **)malloc(600 * sizeof(Veiculo *));

    FILE *file = fopen(caminhoArquivo, "r");

    if(file == NULL){
        return NULL;
    }

    char s[1000];
    fgets(s, sizeof(s), file);
    int count = 0;

    while(fgets(s, sizeof(s), file) != NULL){
        veiculos[count] = parseVeiculo(s);
        count++;
    }

    while(count < 600){
        veiculos[count] = NULL;
        count++;
    }

    fclose(file);
    return veiculos;
}

// --------------- Quicksort Auxiliary Functions ---------------

int compare(Veiculo *a, Veiculo *b){
    if(a->consumoEstrada < b->consumoEstrada){
        return -1;
    }
    else if(a->consumoEstrada > b->consumoEstrada){
        return 1;
    }

    return strcmp(a->marca, b->marca);
}

Veiculo *choosePivot(Veiculo *left, Veiculo *mid, Veiculo *right){
    Veiculo *pivot;

    if((compare(left, mid) > 0) && (compare(left, right) < 0)){
        pivot = left;
    }
    else if((compare(mid, left) > 0) && (compare(mid, right) < 0)){
        pivot = mid;
    }
    else{
        pivot = right;
    }

    return pivot;
}

// =============== Quicksort ===============

void quicksort(Veiculo **array, int left, int right){
    int i = left;
    int j = right;

    Veiculo *pivot = choosePivot(array[left], array[(left + right) / 2], array[right]);

    while(i <= j){
        while(compare(array[i], pivot) < 0){
            i++;
        }

        while(compare(array[j], pivot) > 0){
            j--;
        }

        if(i <= j){
            Veiculo *aux = array[i];
            array[i] = array[j];
            array[j] = aux;

            i++;
            j--;
        }
    }

    if(left < j){
        quicksort(array, left, j);
    }

    if(i < right){
        quicksort(array, i, right);
    }
}

// =============== main ===============

int main(){
    Veiculo **veiculos = ler("/tmp/veiculos.csv");
    Veiculo **sorted = (Veiculo **) malloc(600 * sizeof(Veiculo *));

    if(veiculos == NULL){
        printf("File not found\n");

        return 1;
    }

    int num;
    scanf("%d", &num);

    int i = 0;
    int count = 0;

    while(num != -1){ 
        while(veiculos[i] != NULL && veiculos[i]->id != num){
            i++;
        }

        if(veiculos[i] != NULL){
            sorted[count] = veiculos[i];
            count++;
        }

        i = 0;

        scanf("%d", &num);
    }

    quicksort(sorted, 0, (count - 1));

    for(int i = 0; i < count; i++){
        char formatted[1000];

        format(sorted[i], formatted);
        printf("%s\n", formatted);
    }

    for(int i = 0; i < 600; i++){
        if(veiculos[i] != NULL){
            free(veiculos[i]);
        }
    }

    free(veiculos);
    free(sorted);

    return 0;
}