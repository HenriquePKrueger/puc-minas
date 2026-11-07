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

            Sorting.insertionSort(sorted, count);

            for(int i = 0; i < Sorting.k; i++){
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
	public static int k = 10; // Partial sorting limit

    // --------------- Insertion Sort ---------------

    public static void insertionSort(Veiculo[] vehicles, int size){
		for(int i = 1; i < size; i++){
			Veiculo key = vehicles[i];
			int j = (i < k) ? i - 1 : k - 1;
			
			while((j >= 0) && (vehicles[j].getMarca().compareTo(key.getMarca()) > 0)){
				vehicles[j + 1] = vehicles[j];
				j--;
			}

			vehicles[j + 1] = key;
		}
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

    public String getMarca(){
        return marca;
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