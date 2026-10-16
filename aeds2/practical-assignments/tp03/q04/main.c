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
        vehicle->turbo ? "true" : "false", data
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

// =============== STRUCT: Cell ===============

typedef struct Cell {
    Veiculo *vehicle;
    struct Cell *next;
} Cell;

// --------------- Cell Functions ---------------

Cell *createSentinelCell(){
    Cell *cell = (Cell *) malloc(sizeof(Cell));

    cell->vehicle = NULL;
    cell->next = NULL;

    return cell;
}

Cell *createVehicleCell(Veiculo *vehicle){
    Cell *cell = (Cell *) malloc(sizeof(Cell));

    cell->vehicle = vehicle;
    cell->next = NULL;

    return cell;
}

// =============== STRUCT: VehicleList ===============

typedef struct {
    Cell *head;
    Cell *tail;
} VehicleList;

// --------------- VehicleList Functions ---------------

VehicleList *createVehicleList(){
    VehicleList *vehicleList = (VehicleList *) malloc(sizeof(VehicleList));
    
    vehicleList->head = createSentinelCell();
    vehicleList->tail = vehicleList->head;

    return vehicleList;
}

void appendVehicle(Veiculo *vehicle, VehicleList *vehicleList){
    vehicleList->tail->next = createVehicleCell(vehicle);
    vehicleList->tail = vehicleList->tail->next;
}

// --------------- Auxiliary Selection Sort Functions ---------------

void swap(Cell *i, Cell *smaller){
    Veiculo *aux = i->vehicle;
    i->vehicle = smaller->vehicle;
    smaller->vehicle = aux;
}

// =============== Selection Sort ===============

void selection(VehicleList *vehicleList){
    Cell *i = vehicleList->head->next;

    while(i->next !=  NULL){
        Cell *smaller = i;
        Cell *j = i->next;

        while(j != NULL){
            if(strcmp(j->vehicle->modelo, smaller->vehicle->modelo) < 0){
                smaller = j;
            }

            j = j->next;
        }

        swap(i, smaller);
        i = i->next;
    }

}

// =============== main ===============

int main(){
    Veiculo **veiculos = ler("/tmp/veiculos.csv");
    VehicleList *vehicleList = createVehicleList();

    if(veiculos == NULL){
        printf("File not found\n");

        return 1;
    }

    int num;
    scanf("%d", &num);

    int i = 0;
    
    while(num != -1){ 
        while(veiculos[i] != NULL && veiculos[i]->id != num){
            i++;
        }

        if(veiculos[i] != NULL){
            appendVehicle(veiculos[i], vehicleList);
        }

        i = 0;

        scanf("%d", &num);
    }

    selection(vehicleList);

    for(Cell *j = vehicleList->head->next; j != NULL; j = j->next){
        char formatted[1000];

        format(j->vehicle, formatted);
        printf("%s\n", formatted);
    }

    free(veiculos);

    return 0;
}