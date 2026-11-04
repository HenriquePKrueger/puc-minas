import java.util.*;
import java.io.*;

public class Main{
	public static void main(String[] args){
		LeitorCsv reader = new LeitorCsv();
        
		try{
			Veiculo[] vehicles = reader.ler("/tmp/veiculos.csv");
            Scanner sc = new Scanner(System.in);

            VehicleList vehicleList = new VehicleList();

            int id = sc.nextInt();
            
            while(id != -1){
                int i = 0;

                while(vehicles[i] != null && vehicles[i].getId() != id){
                    i++;
                }

                if(vehicles[i] != null){
                    vehicleList.appendVehicle(vehicles[i]);
                }

                id = sc.nextInt();
            }

            Sorting.quicksort(vehicleList.getHead().getNext(), vehicleList.getTail());

            for(Cell i = vehicleList.getHead().getNext(); i != null; i = i.getNext()){
                System.out.println(i.getVehicle().format());
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

    // --------------- Quicksort ---------------

    public static void quicksort(Cell left, Cell right){
        if(left == null || right == null || left == right) {
            return;
        }

        Cell i = left;
        Cell j = right;

        Cell pivot = partition(left, right);

        if((pivot != left)){
            quicksort(left,  pivot.getPrev());
        }

        if((pivot != right)){
            quicksort(pivot.getNext(), right);
        }
    }

    // --------------- Auxiliary Methods ---------------

    private static Cell partition(Cell left, Cell right){
        Cell mid = findMid(left, right);

        swap(mid, right);

        Veiculo pivot = right.getVehicle();

        Cell i = left.getPrev();

        for(Cell j = left; j != right; j = j.getNext()){
            if(compare(j.getVehicle(), pivot) <= 0){
                i = i.getNext();
                swap(i, j);  
            }
        }

        i = i.getNext();
        swap(i, right);

        return i;
    }

    private static void swap(Cell a, Cell b){
        Veiculo aux = a.getVehicle();
        a.setVehicle(b.getVehicle());
        b.setVehicle(aux);
    }

    private static Cell findMid(Cell left, Cell right){
        while((left != right) && (left.getNext() != right)){
            left = left.getNext();
            right = right.getPrev();
        }

        return left;
    }

    private static int compare(Veiculo a, Veiculo b){
        if(a.getConsumoEstrada() > b.getConsumoEstrada()){
            return 1;
        }
        else if(a.getConsumoEstrada() < b.getConsumoEstrada()){
            return -1;
        }

        return a.getMarca().compareTo(b.getMarca());
    }
}

// =============== CLASS: VehicleList ===============

class VehicleList{
    private Cell head;
    private Cell tail;

    public VehicleList(){
        head = new Cell();
        tail = this.head;
    }

    public void appendVehicle(Veiculo vehicle){
        Cell cell = new Cell(vehicle);

        tail.setNext(cell);
        tail.getNext().setPrev(tail);
        tail = tail.getNext();
    }

    // --------------- Getters and Setters ---------------

    public Cell getHead(){
        return head;
    }

    public Cell getTail(){
        return tail;
    }
}

// =============== CLASS: Cell ===============

class Cell{
    private Cell next;
    private Cell prev;
    private Veiculo vehicle;

    // Create sentinel cell
    public Cell(){
        this(null);
    }

    public Cell(Veiculo vehicle){
        next = null;
        prev = null;
        this.vehicle = vehicle;
    }

    // --------------- Getters and Setters ---------------

    public Veiculo getVehicle(){
        return vehicle;
    }

    public void setVehicle(Veiculo vehicle){
        this.vehicle = vehicle;
    }

    public Cell getNext(){
        return next;
    }

    public void setNext(Cell next){
        this.next = next;
    }

    public Cell getPrev(){
        return prev;
    }

    public void setPrev(Cell prev){
        this.prev = prev;
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

    public double getConsumoEstrada(){
        return consumoEstrada;
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