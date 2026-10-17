import java.util.*;
import java.io.*;

public class Main{
	public static void main(String[] args){
		LeitorCsv reader = new LeitorCsv();
        
		try{
			Veiculo[] vehicles = reader.ler("/tmp/veiculos.csv");
            Scanner sc = new Scanner(System.in);

            Veiculo[] sorted = new Veiculo[600];
            int count = 0;

            int id = sc.nextInt();
            
            while(id != -1){
                int i = 0;

                while(vehicles[i] != null && vehicles[i].getId() != id){
                    i++;
                }

                if(vehicles[i] != null){
                    sorted[count] = vehicles[i];
                    count++;
                }

                id = sc.nextInt();
            }

            Sorting.mergesort(sorted, count);

            for(int i = 0; i < count; i++){
                System.out.println(sorted[i].format());
            }
                        
		} catch(FileNotFoundException e){
			System.out.println("File not found");
        }
	}
}

// =============== CLASS: LeitorCsv ===============

class LeitorCsv{
	public Veiculo[] ler(String caminhoArquivo) throws FileNotFoundException{
		Veiculo[] vehicles = new Veiculo[600];

		Scanner file = new Scanner(new File(caminhoArquivo));
		file.nextLine();

		int count = 0;

		while(file.hasNextLine()){
			vehicles[count] = Veiculo.parseVeiculo(file.nextLine());
			count++;
		}

		return vehicles;
	}
}

// =============== CLASS: Sorting ===============

class Sorting{

    // --------------- Mergesort ---------------

    public static void mergesort(Veiculo[] vehicles, int size){
        if(size < 2){
            return;
        }

        int mid = size / 2;
        int rightSize = (size - mid);

        Veiculo[] left = new Veiculo[mid];
        Veiculo[] right = new Veiculo[rightSize];

        for(int i = 0; i < mid; i++){
            left[i] = vehicles[i];
        }

        for(int j = mid; j < size; j++){
            right[j - mid] = vehicles[j];
        }

        mergesort(left, mid);
        mergesort(right, rightSize);
        merge(vehicles, left, right, mid, rightSize);
    }

    private static void merge(Veiculo[] vehicles, Veiculo[] left, Veiculo[] right, int leftSize, int rightSize){
        int i = 0;
        int j = 0;
        int k = 0;

        while(i < leftSize && j < rightSize){
            if(compare(left[i], right[j]) <= 0){
                vehicles[k++] = left[i++];
            }
            else{
                vehicles[k++] = right[j++];
            }
        }

        while(i < leftSize){
            vehicles[k++] = left[i++];
        }

        while(j < rightSize){
            vehicles[k++] = right[j++];
        }
    }

    // --------------- Auxiliary Methods ---------------

    private static int compare(Veiculo a, Veiculo b){
        if(a.getConsumoCidade() > b.getConsumoCidade()){
            return 1;
        }
        else if(a.getConsumoCidade() < b.getConsumoCidade()){
            return -1;
        }

        return a.getCategoria().compareTo(b.getCategoria());
    }
}

// =============== CLASS: Veiculo ===============

class Veiculo{
	private int id;
	private String marca;
	private String modelo;
	private int ano;
	private String categoria;
	private String[] combustivel;
	private int cilindros;
	private double cilindrada;
	private String transmissao;
	private String tracao;
	private double consumoCidade;
	private double consumoEstrada;
	private double co2;
	private boolean turbo;
	private Data dataRegistro;

    // --------------- Main Methods ---------------

	public static Veiculo parseVeiculo(String s){
		Veiculo vehicle = new Veiculo();

		String[] data = s.split(",");

		vehicle.id = Integer.parseInt(data[0]);
		vehicle.marca = data[1];
		vehicle.modelo = data[2];
		vehicle.ano = Integer.parseInt(data[3]);
		vehicle.categoria = data[4];
		vehicle.combustivel = vehicle.parseCombustivel(data[5]);
		vehicle.cilindros = Integer.parseInt(data[6]);
		vehicle.cilindrada = Double.parseDouble(data[7]);
		vehicle.transmissao = data[8];
		vehicle.tracao = data[9];
		vehicle.consumoCidade = Double.parseDouble(data[10]);
		vehicle.consumoEstrada = Double.parseDouble(data[11]);
		vehicle.co2 = Double.parseDouble(data[12]);
		vehicle.turbo = Boolean.parseBoolean(data[13]);
		vehicle.dataRegistro = Data.parseData(data[14]);

		return vehicle;
	}

	public String format(){
		return String.format(Locale.US,"[%d ## %s ## %s ## %d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %.2f ## %.2f ## %.1f ## %b ## %s]", 
		id,
        marca,
        modelo,
        ano,
        categoria,
        "[" + String.join(",", combustivel) + "]", //Or "Arrays.toString(combustivel)", but it adds a space after the comma
        cilindros,
        cilindrada,
        transmissao,
        tracao,
        consumoCidade,
		consumoEstrada,
        co2,
        turbo,
        dataRegistro.format());
	}

    // --------------- Auxiliary Methods ---------------

	private String[] parseCombustivel(String s){
		return s.split(";");
	}

    // --------------- Getters and Setters ---------------

    public int getId(){
        return id;
    }

    public double getCo2(){
        return co2;
    }

    public double getConsumoCidade(){
        return consumoCidade;
    }

    public String getCategoria(){
        return categoria;
    }
}

// =============== CLASS: Data ===============

class Data{
	private int ano;
	private int mes;
	private int dia;

	public static Data parseData(String s){
		Data date = new Data();

		String[] data = s.split("-");

		date.ano = Integer.parseInt(data[0]);
		date.mes = Integer.parseInt(data[1]);
		date.dia = Integer.parseInt(data[2]);

		return date;
	}
    
	public String format(){
		return String.format("%02d/%02d/%d", dia, mes, ano);
	}
}